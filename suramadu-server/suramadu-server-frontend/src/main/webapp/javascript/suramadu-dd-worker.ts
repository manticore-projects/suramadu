/*
 * DirectDraw render worker.
 *
 * Owns the DirectDraw interpreter (protobuf decode + instruction replay) and one persistent
 * OffscreenCanvas per Swing window, keeping the expensive part of frame rendering off the main
 * thread. For every "draw" message it replays the window's instruction stream onto that
 * window's OffscreenCanvas and posts the result back as a transferred ImageBitmap; the main
 * thread blits the bitmap into the regular per-window canvas, so every existing consumer of
 * window pixels (dirty-rect blit to the root canvas, moveAction, getPixels, undocking, mirror
 * view, internal windows) is untouched.
 *
 * The DirectDraw constant pool lives HERE for the lifetime of the worker. The worker is
 * created/terminated in lockstep with the main thread's DirectDraw instance (see
 * BaseModule.initDirectDraw), so pool lifetime semantics are unchanged.
 *
 * Protocol (all messages are plain objects with a "t" discriminator):
 *   in : init        { logDebug, logTrace, dpr }
 *   out: ready       { ok, reason? }                 — ok=false => main thread renders locally
 *   in : draw        { seq, winId, data:ArrayBuffer }  (buffer transferred)
 *   out: frame       { seq, winId, bitmap, width, height } (bitmap transferred)
 *   out: fail        { seq, winId, error }           — main thread falls back for this frame
 *   in : closeWindow { winId }                       — frees the window's OffscreenCanvas
 *   in : dispose     {}                              — worker closes itself
 *   out: log         { error }                       — forwarded interpreter errors
 */
import { DirectDraw } from "suramadu-directdraw-javascript";

interface IInitMsg { t: "init"; logDebug: boolean; logTrace: boolean; dpr: number; }
interface IDrawMsg { t: "draw"; seq: number; winId: string; data: ArrayBuffer; }
interface ICloseWindowMsg { t: "closeWindow"; winId: string; }
interface IDisposeMsg { t: "dispose"; }
type InMsg = IInitMsg | IDrawMsg | ICloseWindowMsg | IDisposeMsg;

// WorkerGlobalScope.postMessage has a different signature than Window.postMessage; the
// frontend is compiled against the DOM lib, so go through an untyped alias.
const scope = self as any;
const post: (msg: any, transfer?: Transferable[]) => void = scope.postMessage.bind(scope);

let dd: DirectDraw | null = null;
// Per-window render targets. OffscreenCanvas is cast to HTMLCanvasElement because the
// DirectDraw interpreter only touches width/height/getContext("2d"), which both share.
let canvases: { [winId: string]: HTMLCanvasElement } = {};
// The DirectDraw interpreter holds a single cumulative constant pool and shared scratch
// state; it must process one frame at a time. The main thread is ack-gated (one frame in
// flight) in steady state, but the pre-ready queue can release a burst at once, so chain
// draws here to guarantee serial execution regardless of arrival pattern.
let renderTail: Promise<void> = Promise.resolve();

function init(m: IInitMsg) {
    const ok = typeof scope.OffscreenCanvas !== "undefined"
        && typeof scope.createImageBitmap === "function"
        && typeof scope.fonts !== "undefined"; // FontFaceSet in workers (Chromium, Firefox 105+)
    if (ok) {
        dd = new DirectDraw({
            logDebug: m.logDebug,
            logTrace: m.logTrace,
            dpr: m.dpr,
            onErrorMessage: (err: Error) => post({ t: "log", error: "" + (err && err.stack ? err.stack : err) })
        });
        canvases = {};
    }
    post({ t: "ready", ok, reason: ok ? undefined : "OffscreenCanvas/createImageBitmap/FontFaceSet not available in this worker" });
}

function draw(m: IDrawMsg) {
    // Serialize: each frame waits for the previous to finish before touching the shared
    // interpreter state. .catch keeps the chain alive after a failed frame.
    renderTail = renderTail.then(() => drawOne(m)).catch(() => undefined);
}

function drawOne(m: IDrawMsg): Promise<void> {
    if (dd == null) {
        post({ t: "fail", seq: m.seq, winId: m.winId, error: "worker not initialized" });
        return Promise.resolve();
    }
    let canvas = canvases[m.winId];
    if (canvas == null) {
        // drawBin sizes it to the window on first use (resize-with-content-preserve path)
        canvas = new scope.OffscreenCanvas(1, 1) as HTMLCanvasElement;
        canvases[m.winId] = canvas;
    }
    const bytes = new Uint8Array(m.data);
    const fail = (err: unknown) => {
        post({ t: "fail", seq: m.seq, winId: m.winId, error: "" + (err && (err as Error).message ? (err as Error).message : err) });
    };
    // Promise.resolve() lifts drawBin's PromiseLike into a real Promise (returns Promise<void>).
    return Promise.resolve(dd.drawBin(bytes, canvas)).then(
        (result: HTMLCanvasElement) => {
            return scope.createImageBitmap(result).then((bitmap: ImageBitmap) => {
                post({ t: "frame", seq: m.seq, winId: m.winId, bitmap, width: result.width, height: result.height }, [bitmap]);
            });
        },
        fail
    ).then(undefined, fail);
}

scope.onmessage = (e: MessageEvent) => {
    const m = e.data as InMsg;
    switch (m.t) {
        case "init":
            init(m);
            break;
        case "draw":
            draw(m);
            break;
        case "closeWindow":
            delete canvases[m.winId];
            break;
        case "dispose":
            if (dd != null) {
                dd.dispose();
                dd = null;
            }
            canvases = {};
            scope.close();
            break;
        default:
            break;
    }
};
