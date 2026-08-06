import { ModuleDef } from "./suramadu-inject";
import { getDpr } from "./suramadu-util";
import { appFrameProtoOut } from "./proto/proto.out";

export const canvasInjectable = {
    cfg: 'webswing.config' as const,
    sendHandshake: 'base.handshake' as const,
    repaint: 'base.repaint' as const,
    touchBarEnabled: 'touch.touchBarEnabled' as const
}

export interface ICanvasService {
    'canvas.init': () => void,
    'canvas.dispose': () => void,
    'canvas.get': () => HTMLCanvasElement,
    'canvas.width': () => number,
    'canvas.height': () => number,
    'canvas.getDesktopSize': () => {
        width: number;
        height: number;
    },
    'canvas.processComponentTree': (componentTree: appFrameProtoOut.IComponentTreeMsgOutProto[]) => void,
    'canvas.canvasConfig': ICanvasConfig
}

interface ICanvasConfig {
	disposeOnDisconnect: boolean
}

export class CanvasModule extends ModuleDef<typeof canvasInjectable, ICanvasService> {
    private canvas?: JQuery<HTMLCanvasElement>;
    private resizeCheck?: number;
    private lastRootWidth = 0;
    private lastRootHeight = 0;
    private touchWidth = 0;
    private touchHeight = 0;

    public ready = () => {
        this.init();
    };

    public provides() {
        return {
            'canvas.init': this.init,
            'canvas.dispose': this.dispose,
            'canvas.get': this.get,
            'canvas.width': this.width,
            'canvas.height': this.height,
            'canvas.getDesktopSize': this.getDesktopSize,
            'canvas.processComponentTree': this.processComponentTree,
            'canvas.canvasConfig': this.canvasConfig(),
        }
    }

    public init() {
        const dpr = getDpr();
        if (this.canvas == null) {
            if (this.api.cfg.rootElement.parent().data("touch-width")) {
                this.touchWidth = this.api.cfg.rootElement.parent().data("touch-width");
            }
            if (this.api.cfg.rootElement.parent().data("touch-height")) {
                this.touchHeight = this.api.cfg.rootElement.parent().data("touch-height");
            }
            this.api.cfg.rootElement.append('<canvas role="presentation" aria-hidden="true" data-id="canvas" style="display:block; width:' + this.width() + 'px;height:' + this.height() + 'px;" width="' + this.width() * dpr + '" height="' + this.height() * dpr + '" tabindex="-1"/>');
            this.api.cfg.rootElement.append('<input role="presentation" aria-hidden="true" data-id="input-handler" class="ws-input-hidden" type="text" autocorrect="off" autocapitalize="none" autocomplete="off" value="" />');
            this.canvas = this.api.cfg.rootElement.find('canvas[data-id="canvas"]') as JQuery<HTMLCanvasElement>;
            this.canvas.addClass("suramadu-canvas");
            // canvas[0].getContext("2d").scale(dpr, dpr);
            this.lastRootWidth = this.width();
            this.lastRootHeight = this.height();
            this.api.cfg.rootElement.attr("role", "application");
        }
        if (this.resizeCheck == null) {
            this.resizeCheck = setInterval(() => {
                if (!this.api.cfg.mirror && !this.api.cfg.touchMode && this.canvas != null && (Math.round(this.canvas.width()!) !== this.width() || Math.round(this.canvas.height()!) !== this.height())) {
                    if (this.api.cfg.rootElement.is(".composition")) {
                        // when using compositing window manager, the root canvas has 0 size
                        // we need to do a handshake only if the root element has changed size
                        if (this.lastRootWidth !== this.width() || this.lastRootHeight !== this.height()) {
                            this.lastRootWidth = this.width();
                            this.lastRootHeight = this.height();
                            this.api.sendHandshake();
                        }
                    } else {
                        let snapshot = null;
                        if(this.get().width!==0 && this.get().height !== 0){
                        	snapshot = this.get().getContext("2d")?.getImageData(0, 0, this.get().width, this.get().height);
                        }
                        const w = this.width();
                        const h = this.height();
                        this.get().width = w * dpr;
                        this.get().height = h * dpr;
                        this.get().style.width = w + 'px';
                        this.get().style.height = h + 'px';
                        if(snapshot!=null){
                        	this.get().getContext("2d")?.putImageData(snapshot!, 0, 0);
                        }
                        this.api.sendHandshake();
                    }
                }
            }, 500);
        }
    }

    public dispose() {
        if (this.canvas != null) {
            this.canvas = undefined;
        }
        this.api.cfg.rootElement.find('canvas').remove();
        $(".suramadu-canvas, .suramadu-html-canvas, .internal-frames-wrapper").remove();
        this.api.cfg.rootElement[0].querySelector("input.ws-input-hidden")?.remove();

        if (this.resizeCheck != null) {
            clearInterval(this.resizeCheck);
            this.resizeCheck = undefined;
        }
    }

    public width() {
        if (this.api.cfg.touchMode && this.touchWidth !== 0) {
            return this.touchWidth;
        }
        return Math.floor(this.api.cfg.rootElement.width()!);
    }

    public height() {
        if (this.api.cfg.touchMode && this.touchHeight !== 0) {
            return this.touchHeight;
        }

        return Math.floor(this.api.cfg.rootElement.height()!);
    }

    public get() {
        if (this.canvas == null || this.resizeCheck != null) {
            this.init();
        }
        return this.canvas![0];
    }

    public processComponentTree(_: appFrameProtoOut.IComponentTreeMsgOutProto[]) {
        // not implemented, to be customized
    }

    public getDesktopSize() {
        if (this.api.cfg.rootElement.is(".composition")) {
            return { width: this.width(), height: this.height() };
        }
        if (this.api.cfg.touchMode && this.touchWidth !== 0 && this.touchHeight !== 0) {
            return { width: this.width(), height: this.height() };
        }
        return { width: this.get().offsetWidth, height: this.get().offsetHeight };
    }

    private canvasConfig() {
		return {
            disposeOnDisconnect: true
		};
	}

}
