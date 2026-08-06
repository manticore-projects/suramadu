import { ModuleDef } from "./suramadu-inject";
import { fixConnectionUrl } from "./suramadu-util";

export const pingInjectable = {
    cfg: 'webswing.config' as const,
    sendTimestamp: 'socket.sendTimestamp' as const,
    showWarning: 'dialog.showNetworkBar' as const,
    hideWarning: 'dialog.hideNetworkBar' as const,
    dialogContent: 'dialog.content' as const
}

export interface IPingService {
    'ping.mutePingWarning': (severity: number) => void,
    'ping.start': () => void,
    'ping.dispose': () => void
}

interface IPingMessage {
    result: string;
    latency: number;
}

export class PingModule extends ModuleDef<typeof pingInjectable, IPingService> {
    private intervalHandle?: number | null;
    private worker?: Worker;
    private ping?: IPingMessage[];
    private severity?: number;
    private mute?: number;
    private count = 6;
    private interval = 5
    private maxLatency = 500
    private notifyIf = 3;

    public ready = () => {
        const params = this.api.cfg.pingParams;
        if (params != null) {
            this.count = params.count || this.count;
            this.interval = params.interval || this.interval;
            this.maxLatency = params.maxLatency || this.maxLatency;
            this.notifyIf = params.notifyIf || this.notifyIf;
        }
    };

    public provides() {
        return {
            'ping.mutePingWarning': this.mutePingWarning,
            'ping.start': this.start,
            'ping.dispose': this.dispose
        }
    }

    public mutePingWarning(severity: number) {
        this.mute = severity;
    }

    public start() {
        if(!this.worker){
            this.mute = 0;
            this.ping = getArrayWithLimitedLength(this.count);
            const connectionUrl = fixConnectionUrl(this.api.cfg.connectionUrl);
            const blobURL = URL.createObjectURL(new Blob(['onmessage=(', PingMonitor.toString(), ')("' + connectionUrl + '",' + this.interval + ')'], { type: 'application/javascript' }));
            this.worker = new Worker(blobURL);
            this.worker.onmessage = (e) => {
                const p: IPingMessage = JSON.parse(e.data);
                this.ping?.push(p);
                if (p.result === 'ok') {
                    this.api.sendTimestamp({
                        ping: p.latency
                    });
                    // first success after offline status - clear pings
                    if (this.severity === 2) {
                        this.ping = getArrayWithLimitedLength(this.count);
                    }
                }
                this.evaluateNetworkStatus();
            };
            this.intervalHandle = setInterval(() => {
                this.worker?.postMessage('doPing');
            }, this.interval * 1000);
        }
    }

    public dispose() {
        if (this.worker != null) {
            this.worker.terminate();
            this.worker=undefined;
        }
        if (this.intervalHandle != null) {
            clearInterval(this.intervalHandle);
            this.intervalHandle = null;
        }

    }

    private evaluateNetworkStatus() {
        let msg = null;
        let warns = 0;
        let fails = 0;
        if (this.ping) {
            for (const p of this.ping) {
                if ("error" === p.result || "timeout" === p.result) {
                    warns++;
                    fails++;
                } else if (p.latency > this.maxLatency) {
                    warns++;
                }
            }
        }
        if (fails === this.count) {
            msg = this.api.dialogContent.networkOfflineWarningDialog;
            this.severity = 2;
        } else if (warns >= this.notifyIf) {
            msg = this.api.dialogContent.networkSlowWarningDialog;
            this.severity = 1;
        } else {
            this.severity = 0;
        }

        if (msg != null && this.severity !== this.mute) {
            this.api.showWarning(msg)
        } else {
            this.api.hideWarning();
        }
    }

}

function getArrayWithLimitedLength(length: number) {
    const array = new Array();
    array.push = function () {
        if (this.length >= length) {
            this.shift();
        }
        return Array.prototype.push.apply(this, arguments as any);
    }
    return array;
}

function PingMonitor(connectionUrl: string, is: number) {
    const interval = is * 1000;

    function ping() {
        const startTime = new Date().getTime();
        const oReq = new XMLHttpRequest();
        oReq.addEventListener("load", () => {
            result("ok", (new Date().getTime() - startTime));
        });
        oReq.addEventListener("error", () => {
            result("error", (new Date().getTime() - startTime));
        });
        oReq.addEventListener("timeout", () => {
            result("timeout", (new Date().getTime() - startTime));
        });
        oReq.open("GET", connectionUrl + "rest/ping");
        oReq.timeout = interval;
        oReq.send();
    }

    function result(status: string, ms: number) {
        const pingMessage = { result: status, latency: ms }
        postMessage(JSON.stringify(pingMessage), undefined as any); // webworker postMessage does not define second param
    }


    return function onmessage(msg: MessageEvent) {
        if (msg.data === 'doPing') {
            ping();
        }
    }
}
