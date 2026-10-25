/*
 * Main-thread client for the DirectDraw render worker (suramadu-dd-worker.ts).
 *
 * One instance per DirectDraw lifetime: BaseModule creates it in initDirectDraw() and
 * disposes it whenever the main-thread DirectDraw instance is replaced (resetState) or the
 * session ends, so the worker-side constant pool tracks the server's view of the client cache.
 *
 * Failure model: any failure — worker script missing (404), feature check failed inside the
 * worker, a per-frame interpreter error — permanently marks the client failed, rejects all
 * in-flight draws and invokes onFailed exactly once. BaseModule then falls back to
 * main-thread rendering. Per-frame errors cannot be retried in the worker because the
 * constant pool may have been left inconsistent.
 */

export interface IWorkerFrame {
    bitmap: ImageBitmap;
    width: number;
    height: number;
}

interface IPendingDraw {
    resolve: (frame: IWorkerFrame) => void;
    reject: (error: Error) => void;
}

export interface IWorkerClientOptions {
    logDebug?: boolean;
    logTrace?: boolean;
    dpr: number;
}

export class DirectDrawWorkerClient {

    private worker: Worker | null = null;
    private state: "pending" | "ready" | "failed" = "pending";
    private seq = 0;
    private pending: { [seq: number]: IPendingDraw } = {};
    private preReadyQueue: Array<() => void> = [];
    private onFailed: ((reason: string) => void) | null;

    private blobUrl: string | null = null;

    constructor(workerUrl: string, options: IWorkerClientOptions, onFailed: (reason: string) => void) {
        this.onFailed = onFailed;
        // The served pages' Content Security Policy only allows blob: workers
        // (worker-src blob:), so the bundle cannot be started from its URL directly.
        // Fetch it and start the worker from a Blob URL instead — the same approach
        // the ping-monitor worker uses. Draws issued meanwhile queue until "ready".
        fetch(workerUrl, { credentials: "same-origin" })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("HTTP " + response.status + " loading " + workerUrl);
                }
                return response.text();
            })
            .then((code) => {
                if (this.state === "failed") {
                    return; // disposed while the bundle was loading
                }
                this.blobUrl = URL.createObjectURL(new Blob([code], { type: "application/javascript" }));
                this.worker = new Worker(this.blobUrl);
                this.worker.onerror = (e: ErrorEvent) => this.failInternal("worker error: " + (e && e.message ? e.message : "unknown"), true);
                this.worker.onmessage = (e: MessageEvent) => this.handleMessage(e.data);
                this.worker.postMessage({ t: "init", logDebug: !!options.logDebug, logTrace: !!options.logTrace, dpr: options.dpr });
            })
            .catch((e) => {
                this.failInternal("could not create worker: " + e, true);
            });
    }

    private releaseBlobUrl() {
        if (this.blobUrl != null) {
            URL.revokeObjectURL(this.blobUrl);
            this.blobUrl = null;
        }
    }

    public isUsable(): boolean {
        return this.state !== "failed";
    }

    /**
     * Render one window frame in the worker. `data` is copied before transfer, so the caller's
     * Uint8Array stays valid (it is a subarray view into the whole decoded AppFrame buffer —
     * transferring that buffer would corrupt the other windows of the same frame — and the
     * original is needed for the local fallback if this call rejects).
     */
    public draw(winId: string, data: Uint8Array): Promise<IWorkerFrame> {
        if (this.state === "failed" || this.worker == null) {
            return Promise.reject(new Error("DirectDraw worker unavailable"));
        }
        // Snapshot the bytes NOW, at call time. win.directDraw is a subarray view into the
        // WebSocket message's ArrayBuffer, which the socket layer reuses for later frames. If
        // the send is deferred (worker not ready yet) and we sliced at send time, a later frame
        // could already have overwritten the buffer, so the worker would decode stale bytes and
        // fault on a missing constant. The copy owns its memory and is what gets transferred.
        const payload = data.slice();
        return new Promise<IWorkerFrame>((resolve, reject) => {
            const send = () => {
                if (this.state !== "ready" || this.worker == null) {
                    reject(new Error("DirectDraw worker unavailable"));
                    return;
                }
                const seq = ++this.seq;
                this.pending[seq] = { resolve, reject };
                this.worker.postMessage({ t: "draw", seq, winId, data: payload.buffer }, [payload.buffer]);
            };
            if (this.state === "pending") {
                this.preReadyQueue.push(send);
            } else {
                send();
            }
        });
    }

    public closeWindow(winId: string) {
        if (this.state !== "failed" && this.worker != null) {
            this.worker.postMessage({ t: "closeWindow", winId });
        }
    }

    public dispose() {
        this.releaseBlobUrl();
        this.onFailed = null; // deliberate teardown, not a failure
        if (this.worker != null) {
            try {
                this.worker.postMessage({ t: "dispose" });
            } catch (e) { /* ignore */ }
            this.worker.terminate();
            this.worker = null;
        }
        this.state = "failed";
        this.rejectAll("DirectDraw worker disposed");
    }

    private handleMessage(m: any) {
        if (m == null || this.state === "failed") {
            return;
        }
        switch (m.t) {
            case "ready":
                this.releaseBlobUrl();
                if (m.ok) {
                    this.state = "ready";
                    const queued = this.preReadyQueue;
                    this.preReadyQueue = [];
                    queued.forEach((send) => send());
                } else {
                    this.failInternal("" + m.reason, true);
                }
                break;
            case "frame": {
                const p = this.pending[m.seq];
                delete this.pending[m.seq];
                if (p) {
                    p.resolve({ bitmap: m.bitmap, width: m.width, height: m.height });
                } else if (m.bitmap && m.bitmap.close) {
                    m.bitmap.close(); // nobody is waiting (disposed meanwhile) — don't leak GPU memory
                }
                break;
            }
            case "fail": {
                // a failed frame leaves the worker-side constant pool suspect — fail the client
                const p = this.pending[m.seq];
                delete this.pending[m.seq];
                this.failInternal("render failed for window " + m.winId + ": " + m.error, true);
                if (p) {
                    p.reject(new Error("" + m.error));
                }
                break;
            }
            case "log":
                console.error("DirectDraw worker: " + m.error);
                break;
            default:
                break;
        }
    }

    private failInternal(reason: string, terminate: boolean) {
        if (this.state === "failed") {
            return;
        }
        this.state = "failed";
        this.releaseBlobUrl();
        if (terminate && this.worker != null) {
            this.worker.terminate();
            this.worker = null;
        }
        this.rejectAll(reason);
        if (this.onFailed != null) {
            const cb = this.onFailed;
            this.onFailed = null;
            cb(reason);
        }
    }

    private rejectAll(reason: string) {
        const queued = this.preReadyQueue;
        this.preReadyQueue = [];
        queued.forEach((send) => send()); // state is "failed": each send() rejects its promise
        for (const seq in this.pending) {
            if (this.pending.hasOwnProperty(seq)) {
                this.pending[seq].reject(new Error(reason));
            }
        }
        this.pending = {};
    }
}
