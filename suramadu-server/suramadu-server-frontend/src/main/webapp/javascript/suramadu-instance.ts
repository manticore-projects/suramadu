import { isArrayBufferSupported, detectIE, detectMac, checkCookie } from './suramadu-util'
import { ModuleDef, IInjector } from './suramadu-inject';
import { HtmlOrCanvasWindow, WindowCloseEvent } from './suramadu-base';

export const webswingInstanceInjectable = {
    cfg: 'webswing.config' as const,
    start: 'webswing.start' as const,
    disconnect: 'webswing.disconnect' as const,
    login: 'login.login' as const,
    connect: 'socket.connect' as const,
    showDialog: 'dialog.show' as const,
    dialogs: 'dialog.content' as const,
    disposeIdentity: 'identity.dispose' as const,
    disposeBase: 'base.dispose' as const,
    disposeCanvas: 'canvas.dispose' as const,
    disposeAccessible: 'accessible.dispose' as const,
    disposeTouch: 'touch.dispose' as const,
    disposeInput: 'input.dispose' as const,
    disposeSocket: 'socket.dispose' as const,
    disposeFileDialog: 'files.close' as const,
    disposeCopyBar: 'clipboard.dispose' as const,
    disposePing: 'ping.dispose' as const,
    startPing: 'ping.start' as const,
    showPlaybackControls: 'playback.showControls' as const,
    externalApi: 'external.api' as const,
    canvasConfig: 'canvas.canvasConfig' as const,
    clearInstanceId: 'socket.clearInstanceId' as const
}

export interface IWebswingInstanceService {
    'webswing.config': IInstanceContext;
    'webswing.start': () => void;
    'webswing.newSession': () => void;
    'webswing.reTrySession': () => void;
    'webswing.disconnect': () => void;
    'webswing.setControl': (value: boolean) => void;
    'webswing.configure': (options?: any, appletParams?: any) => void;
}

interface IInstanceContext {
    rootElementWrapper: JQuery<HTMLElement>;
    rootElement: JQuery<HTMLElement>;
    autoStart: boolean;
    autoReconnect: number | null;
    disableLogout:boolean;
    args: string | null;
    connectionUrl: string;
    mirrorConnectionUrl: string | null;
    mirror: boolean;
    recordingPlayback: boolean;
    typedArraysSupported: boolean;
    recording: boolean;
    debugPort: number | null;
    javaCallTimeout: number;
    documentBase: string;
    ieVersion: number | boolean;
    isMac: boolean;
    clientId: string | null;
    viewId: string | null;
    browserId: string | null
    hasControl: boolean;
    mirrorMode: boolean;
    canPaint: boolean;
    virtualKB: boolean;
    debugLog: boolean;
    traceLog: boolean;
    touchMode: boolean;
    securityToken?: string;
    realm?: string;
    onReady?: (api: any) => void;
    params?: Array<{ name: string, value: any }>
    pingParams: {
        count: number;
        interval: number;
        maxLatency: number;
        notifyIf: number;
    };
    compositingWindowsListener: {
        windowOpening(htmlOrCanvasWindow: HtmlOrCanvasWindow): void;
        windowOpened(htmlOrCanvasWindow: HtmlOrCanvasWindow): void;
        windowClosing(htmlOrCanvasWindow: HtmlOrCanvasWindow, windowCloseEvent: WindowCloseEvent): void;
        windowClosed(htmlOrCanvasWindow: HtmlOrCanvasWindow): void;
        windowModalBlockedChanged(htmlOrCanvasWindow: HtmlOrCanvasWindow): void;
    };
    hideTouchBar: boolean;
}

export class WebswingInstanceModule extends ModuleDef<typeof webswingInstanceInjectable, IWebswingInstanceService> {

    constructor(i: IInjector, private rootElement: JQuery<HTMLElement>) {
        super(i);
    }

    public ready = () => {
        this.configure();
        if (typeof this.api.cfg.onReady === 'function') {
            this.api.login(() => {
                this.api.cfg.onReady!(this.api.externalApi());
            });
        }
        if (this.api.cfg.autoStart) {
            this.api.start();
        } else {
            this.api.showDialog(this.api.dialogs.readyDialog);
        }
    }

    public provides() {
        return {
            'webswing.config': this.defaultCtxConfig(),
            'webswing.start': this.start,
            'webswing.newSession': this.newSession,
            'webswing.reTrySession': this.reTrySession,
            'webswing.disconnect': this.disconnect,
            'webswing.setControl': this.setControl,
            'webswing.configure': this.configure
        }
    }

    public defaultCtxConfig(): IInstanceContext {
        return {
            rootElementWrapper: setupRootElement(this.rootElement),
            rootElement: setupRootElementContent(this.rootElement),
            autoStart: false,
            autoReconnect: null,
            disableLogout:false,
            args: null,
            connectionUrl: location.origin + location.pathname,
            mirrorConnectionUrl: null,
            mirror: false,
            recordingPlayback: false,
            typedArraysSupported: isArrayBufferSupported(),
            recording: false,
            debugPort: null,
            javaCallTimeout: 3000,
            documentBase: document.location.origin + document.location.pathname,
            ieVersion: detectIE(),
            isMac: detectMac(),
            /* webswing instance context */
            clientId: null,
            browserId: null,
            viewId: null,
            hasControl: false,
            mirrorMode: false,
            canPaint: false,
            virtualKB: false,
            debugLog: false,
            traceLog: false,
            touchMode: false,
            pingParams: { count: 6, interval: 5, maxLatency: 500, notifyIf: 3 },
            compositingWindowsListener: {
                // tslint:disable-next-line: no-empty
                windowOpening(_) { },
                // tslint:disable-next-line: no-empty
                windowOpened(_) { },
                // tslint:disable-next-line: no-empty
                windowClosing(_) { },
                // tslint:disable-next-line: no-empty
                windowClosed(_) { },
                // tslint:disable-next-line: no-empty
                windowModalBlockedChanged(_) { }
            },
            hideTouchBar: false
        };
    }

    public start() {
        if (!checkCookie()) {
            this.api.showDialog(this.api.dialogs.cookiesDisabledDialog)
            return;
        }
        this.api.login(() => {
            this.api.showDialog(this.api.dialogs.initializingDialog);
            this.api.connect();
        }, () => {
            this.api.showDialog(this.api.dialogs.disconnectedDialog);
        });
    }

    public newSession() {
        this.api.disconnect();
        this.api.disposeIdentity();
        this.api.clearInstanceId();
        this.api.start();
    }

    public reTrySession() {
        this.api.disconnect();
        this.api.start();
    }

    public disconnect() {
        this.api.disposeFileDialog();
        this.api.disposeBase();
        this.api.disposeInput();
        this.api.disposeTouch();
        if (this.api.canvasConfig.disposeOnDisconnect) {
            this.api.disposeCanvas();
        }
        this.api.disposeAccessible();
        this.api.disposeSocket();
        this.api.disposeCopyBar();
        this.api.disposePing();
    }

    public configure(options?: any, appletParams?: any) {
        const cfg = this.api.cfg;
        options = options != null ? options : readOptions(cfg.rootElementWrapper);
        if (options != null) {
            cfg.autoStart = options.autoStart != null ? JSON.parse(options.autoStart) : cfg.autoStart;
            cfg.autoReconnect = options.autoReconnect != null ? JSON.parse(options.autoReconnect) : cfg.autoReconnect;
            cfg.disableLogout = options.disableLogout != null ? JSON.parse(options.disableLogout) : cfg.disableLogout;
            cfg.securityToken = options.securityToken != null ? options.securityToken : cfg.securityToken;
            cfg.realm = options.realm != null ? options.realm : cfg.realm;
            cfg.args = options.args != null ? options.args : cfg.args;
            cfg.recording = options.recording != null ? JSON.parse(options.recording) : cfg.recording;
            cfg.clientId = options.clientId != null ? options.clientId : cfg.clientId;
            cfg.mirror = options.mirrorMode != null ? JSON.parse(options.mirrorMode) : cfg.mirror;
            cfg.connectionUrl = options.connectionUrl != null ? options.connectionUrl : cfg.connectionUrl;
            cfg.mirrorConnectionUrl = options.mirrorConnectionUrl != null ? options.mirrorConnectionUrl : cfg.mirrorConnectionUrl;
            cfg.debugPort = options.debugPort != null ? options.debugPort : cfg.debugPort;
            cfg.debugLog = options.debugLog != null ? JSON.parse(options.debugLog) : cfg.debugLog;
            cfg.traceLog = options.traceLog != null ? JSON.parse(options.traceLog) : cfg.traceLog;
            cfg.javaCallTimeout = options.javaCallTimeout != null ? parseInt(options.javaCallTimeout, 10) : cfg.javaCallTimeout;
            cfg.pingParams = options.pingParams != null ? options.pingParams : cfg.pingParams;
            if (cfg.connectionUrl.substr(cfg.connectionUrl.length - 1) !== '/') {
                cfg.connectionUrl = cfg.connectionUrl + '/';
            }
            if (cfg.mirrorConnectionUrl != null && cfg.mirrorConnectionUrl.substr(cfg.mirrorConnectionUrl.length - 1) !== '/') {
                cfg.mirrorConnectionUrl = cfg.mirrorConnectionUrl + '/';
            }
            if (options.recordingPlayback != null) {
                cfg.recordingPlayback = options.recordingPlayback;
                this.api.showPlaybackControls();
            }
            cfg.onReady = typeof options.onReady === 'function' ? options.onReady : cfg.onReady;
            cfg.compositingWindowsListener = typeof options.compositingWindowsListener === 'object' ? options.compositingWindowsListener : cfg.compositingWindowsListener;
            cfg.hideTouchBar = options.hideTouchBar != null ? JSON.parse(options.hideTouchBar) : cfg.hideTouchBar;
        }
        appletParams = appletParams != null ? appletParams : readAppletParams(cfg.rootElementWrapper);
        if (appletParams != null) {
            cfg.params = [];
            for (const prop in appletParams) {
                if (typeof appletParams[prop] === 'string') {
                    cfg.params.push({
                        name: prop,
                        value: appletParams[prop]
                    });
                }
            }
        }
    }

    public setControl(value: boolean) {
        this.api.cfg.hasControl = value ? true : false;
    }

}


function readOptions(element: JQuery<HTMLElement>) {
    const options = element.data("webswingOptions");
    return options;
}

function setupRootElement(rootElement: JQuery<HTMLElement>) {
    return rootElement.addClass('suramadu-element');
}

function setupRootElementContent(rootElement: JQuery<HTMLElement>) {
    const detachedContent = rootElement.children().detach();
    const wrapper = $("<div class='suramadu-element-content' />");
    rootElement.append(wrapper);
    wrapper.append(detachedContent);

    return wrapper;
}

function readAppletParams(element: JQuery<HTMLElement>) {
    const result: { [K: string]: string | null } = {};
    const params = $(element).find('suramadu-param');
    if(params.length>0){
        for (const param of params) {
            const name = param.getAttribute('name');
            const val = param.getAttribute('value');
            if (name !== null) {
                result[name] = val;
            }
        }
    }
    return result;
}