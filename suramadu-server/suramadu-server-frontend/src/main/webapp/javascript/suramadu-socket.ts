import { ModuleDef } from "./suramadu-inject";
import { appFrameProtoOut } from "./proto/proto.out";
import { appFrameProtoIn } from "./proto/proto.in";
import { serverBrowserFrameProto } from "./proto/proto.frame";
import { commonProto } from "./proto/proto.common";
import { fixConnectionUrl, getToken } from './suramadu-util';

export const socketInjectable = {
    cfg: 'webswing.config' as const,
    processMessage: 'base.processMessage' as const,
    showDialog: 'dialog.show' as const,
    showBar: 'dialog.showBar' as const,
    hideDialog: 'dialog.hide' as const,
    currentDialog: 'dialog.current' as const,
    dialogs: 'dialog.content' as const,
    reTrySession: 'webswing.reTrySession' as const,
    startPing: 'ping.start' as const,
    disposePing: 'ping.dispose' as const,
    repaint: 'base.repaint' as const,
};

export interface ISocketService {
    'socket.connect': () => void,
    'socket.send': (message: AppFrameIn) => void,
    'socket.sendHandshake': (handshake: HandshakeMessage) => void,
    'socket.sendTimestamp': (timestamp: TimestampsMessage) => void,
    'socket.sendPlaybackCommand': (command: PlaybackCommandMessage) => void,
    'socket.sendSimpleEvent': (event: SimpleEventMessage) => void,
    'socket.instanceId': () => string | undefined,
    'socket.awaitResponse': (callback: (result: JsResultMsgOrError) => void, request: AppFrameIn, correlationId: string, timeout: number) => void,
    'socket.dispose': () => void,
    'socket.isAutoLogout': () => boolean,
    'socket.clearInstanceId': () => void
}

export type AppFrame = AppFrameOut

type SocketFrameMessage = serverBrowserFrameProto.IServerToBrowserFrameMsgOutProto
type AppFrameOut = appFrameProtoOut.IAppFrameMsgOutProto & {
    receivedTimestamp?: number
}
type HandshakeMessage = commonProto.IConnectionHandshakeMsgInProto
type TimestampsMessage = commonProto.ITimestampsMsgInProto
type PlaybackCommandMessage = serverBrowserFrameProto.IPlaybackCommandMsgInProto
type SimpleEventMessage = commonProto.ISimpleEventMsgInProto
type AppFrameIn = appFrameProtoIn.IAppFrameMsgInProto
type JsResultMsgOrError = appFrameProtoOut.IJsResultMsgOutProto & Partial<Error>

export class SocketModule extends ModuleDef<typeof socketInjectable, ISocketService> {
    
    private maxRetries = 1;
    private retryNumber = 0;
    private socketOpen: boolean = false;

    private socket?: WebSocket;
    private instanceId?: string;
    private autoLogout: boolean | null = null;
    private responseHandlers: { [K: string]: (result: JsResultMsgOrError) => void } = {};

    public provides() {
        return {
            'socket.connect': this.connect,
            'socket.send': this.send,
            'socket.sendHandshake': this.sendHandshake,
            'socket.sendTimestamp': this.sendTimestamp,
            'socket.sendPlaybackCommand': this.sendPlaybackCommand,
            'socket.sendSimpleEvent': this.sendSimpleEvent,
            'socket.instanceId': this.getInstanceId,
            'socket.awaitResponse': this.awaitResponse,
            'socket.dispose': this.dispose,
            'socket.isAutoLogout': this.isAutoLogout,
            'socket.clearInstanceId': this.clearInstanceId
        }
    }

    public connect() {
    	this.connectWebSocket();
    }

    public dispose() {
        if (this.socket ) {
            this.socket.onclose = null;
            this.socketOpen = false;
            this.socket.close(1000, "Disconnecting instance.");
        }
        this.socket = undefined;
        // do not clear instanceId, used for reconnect
        // this.instanceId = undefined;
    }

    public send(message: AppFrameIn) {
        const appFrameProto = serverBrowserFrameProto.BrowserToServerFrameMsgInProto.create();
        const proto = appFrameProtoIn.AppFrameMsgInProto.fromObject(message);
        const msg = appFrameProtoIn.AppFrameMsgInProto.encode(proto).finish();
        appFrameProto.appFrameMsgIn = msg;
        this.sendAppFrame(appFrameProto);
    }
    
    public sendHandshake(handshake: HandshakeMessage) {
        const appFrameProto = serverBrowserFrameProto.BrowserToServerFrameMsgInProto.create();
        appFrameProto.handshake = handshake;
        this.sendAppFrame(appFrameProto);
    }
    
    public sendTimestamp(timestamps: TimestampsMessage) {
        const appFrameProto = serverBrowserFrameProto.BrowserToServerFrameMsgInProto.create();
        appFrameProto.timestamps = [timestamps];
        this.sendAppFrame(appFrameProto);
    }
    
    public sendSimpleEvent(event: SimpleEventMessage) {
        const appFrameProto = serverBrowserFrameProto.BrowserToServerFrameMsgInProto.create();
        appFrameProto.events = [event];
        this.sendAppFrame(appFrameProto);
    }

    public sendPlaybackCommand(command: PlaybackCommandMessage) {
        const appFrameProto = serverBrowserFrameProto.BrowserToServerFrameMsgInProto.create();
        appFrameProto.playback = command;
        this.sendAppFrame(appFrameProto);
    }

    public awaitResponse(callback: (result: JsResultMsgOrError) => void, request: AppFrameIn, correlationId: string, timeout: number) {
        this.send(request);
        this.responseHandlers[correlationId] = callback;
        setTimeout(() => {
            if (this.responseHandlers[correlationId] != null) {
                delete this.responseHandlers[correlationId];
                callback(new Error("Java call timed out after " + timeout + " ms."));
            }
        }, timeout);
    }

    public getInstanceId() {
        return this.instanceId;
    }
    
    public clearInstanceId() {
        this.instanceId = undefined;
	}

    private sendAppFrame(appFrameProto: serverBrowserFrameProto.BrowserToServerFrameMsgInProto) {
        if (this.socket && this.socket != null) {
            if (this.socketOpen) {
                const appFrame = serverBrowserFrameProto.BrowserToServerFrameMsgInProto.encode(appFrameProto).finish();
                this.sendAppFrameInternal(appFrame);
            } else {
                if (this.api.currentDialog() === this.api.dialogs.sessionStolenNotification
                    || this.api.currentDialog() === this.api.dialogs.disconnectedDialog
                    || this.api.currentDialog() === this.api.dialogs.reconnectingDialog) {
                    return;
                } else {
                    this.api.showDialog(this.api.dialogs.disconnectedDialog);
                }
            }
        }
    }

    private sendAppFrameInternal(appFrame: Uint8Array) {
        if (this.socket) {
            switch (this.socket.readyState) {
                case this.socket.OPEN:
                    this.socket.send(typedArrayToBuffer(appFrame));
                    break;
                case this.socket.CONNECTING:
                    setTimeout(() => {
                        this.sendAppFrameInternal(appFrame);
                    }, 10);
                    break;
                default:
                    // ignore
                    break;
            }
        }
    }

    private connectWebSocket() {
        let connectionUrl = fixConnectionUrl(this.api.cfg.connectionUrl);
        if (this.api.cfg.mirror && this.api.cfg.mirrorConnectionUrl) {
            connectionUrl = fixConnectionUrl(this.api.cfg.mirrorConnectionUrl!);
        }
        const wsBaseUrl = connectionUrl.replace(/(http)(s)?\:\/\//, "ws$2://");
        let url = wsBaseUrl + 'async/swing-bin';
        if (this.api.cfg.recordingPlayback) {
            url = wsBaseUrl + 'async/swing-play';
        }
        
        url += "?X-suramadu-token=" + encodeURIComponent(getToken()!);
        
        if (this.api.cfg.recordingPlayback) {
            url += "&file=" + encodeURIComponent(this.api.cfg.recordingPlayback);
        }
        if (this.api.cfg.args != null) {
        	url += "&X-suramadu-args=" + encodeURIComponent(this.api.cfg.args);
        }
        if (this.api.cfg.recording != null) {
        	url += "&X-suramadu-recording=" + encodeURIComponent(this.api.cfg.recording);
        }
        if (this.api.cfg.debugPort != null) {
        	url += "&X-suramadu-debugPort=" + encodeURIComponent(this.api.cfg.debugPort);
        }
        if (this.api.cfg.mirror) {
        	url += "&X-suramadu-instanceId=" + encodeURIComponent(this.api.cfg.clientId!);
        }

        this.autoLogout = null;

        this.socket = new WebSocket(url);
        this.socket.binaryType = "arraybuffer";

        this.socket.onopen = () => {
            // empty
            this.socketOpen = true;
            this.api.repaint();
            this.api.startPing();
        };
        
        this.socket.onmessage = (event) => {
            this.retryNumber = 0;
            const receivedTimestamp = new Date().getTime();
            try {
                const frame: SocketFrameMessage = this.decodeResponse(event.data);
                let data: AppFrame = {};

                if (frame.connectionInfo != null) {
                    // tslint:disable-next-line: no-console
                    console.log("Connected to server [" + frame.connectionInfo.serverId + "] and session pool [" + frame.connectionInfo.sessionPoolId + "].")
                    this.autoLogout = frame.connectionInfo.autoLogout!;
                }

                if (frame.appFrameMsgOut && frame.appFrameMsgOut != null) {
                    const appFrameProto = appFrameProtoOut.AppFrameMsgOutProto.decode(frame.appFrameMsgOut);
                    const appFrameData: AppFrameOut = appFrameProtoOut.AppFrameMsgOutProto.toObject(appFrameProto);
                    data = appFrameData;

                    if (data.instanceId != null) {
                        this.instanceId = data.instanceId;
                    }
                    // javascript2java response handling
                    if (data.javaResponse != null && data.javaResponse.correlationId != null) {
                        const correlationId = data.javaResponse.correlationId;
                        if (this.responseHandlers[correlationId] != null) {
                            const callback = this.responseHandlers[correlationId];
                            delete this.responseHandlers[correlationId];
                            callback(data.javaResponse);
                        }
                    }
                    data.receivedTimestamp = receivedTimestamp;
                }

                this.api.processMessage(data);
            } catch (e) {
                console.error((e as Error).stack);
                return;
            }
        };

        this.socket.onclose = (_event) => {
            this.socketOpen = false;
            this.api.disposePing();
            this.autoLogout = null;

            if (this.api.currentDialog() === this.api.dialogs.stoppedDialog || this.api.currentDialog() === this.api.dialogs.timedoutDialog
                || this.api.currentDialog() === this.api.dialogs.sessionStolenNotification) {
                return;
            }

            const token = getToken();
            const isLoggedOut = !token || token == null || !token.length;

            if (this.retryNumber >= this.maxRetries || isLoggedOut) {
                this.api.hideDialog();
                this.api.showDialog(this.api.dialogs.disconnectedDialog);
                this.retryNumber = 0;
            } else {
                this.api.showDialog(this.api.dialogs.reconnectingDialog);
                this.retryNumber++;
                this.api.reTrySession();
            }
        };

        this.socket.onerror = (_event) => {
            this.api.showDialog(this.api.dialogs.connectionErrorDialog);
        };
    }

    private decodeResponse(message: ArrayBuffer): SocketFrameMessage {
        if (message.byteLength === 1) {
            return {};// ignore atmosphere heartbeat
        }
        const uint8View = new Uint8Array(message);

        const proto = serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto.decode(uint8View);
        const data: SocketFrameMessage = serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto.toObject(proto);
        
        return data;
    }

    private isAutoLogout() {
        return this.autoLogout !== null && this.autoLogout;
    }
}

function typedArrayToBuffer(array: Uint8Array) {
    return array.buffer.slice(array.byteOffset, array.byteLength + array.byteOffset)
}