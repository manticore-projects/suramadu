import { Util } from './suramadu-util'
import { Injector, IInjected } from './suramadu-inject'
import { IWebswingInstanceService, WebswingInstanceModule, webswingInstanceInjectable } from './suramadu-instance';
import { BaseModule, baseInjectable, IBaseService } from './suramadu-base';
import { SocketModule, socketInjectable, ISocketService } from './suramadu-socket';
import { ILoginService, LoginModule, loginInjectable } from './suramadu-login';
import { IIdentityService, IdentityModule } from './suramadu-identity';
import { IJsLinkService, JsLinkModule, jsLinkInjectable } from './suramadu-jslink';
import { ClipboardModule, IClipboardService, clipboardInjectable } from './suramadu-clipboard';
import { PlaybackModule, playbackInjectable, IPlaybackService } from './suramadu-playback';
import { ITranslateService, TranslateModule, Translations } from './suramadu-translate';
import { PingModule, pingInjectable, IPingService } from './suramadu-ping';
import ExternalApiModule, { IExternalApiService, externalApiInjectable } from './suramadu-externalapi';
import { IAccessibleService, AccessibleModule, accessibleInjectable } from './suramadu-accessible';
import { IFocusManagerService, focusManagerInjectable, FocusManagerModule } from './suramadu-focus';
import { ICanvasService, CanvasModule, canvasInjectable } from './suramadu-canvas';
import { DialogModule, dialogInjectable, IDialogService } from './suramadu-dialog';
import { filesInjectable, FilesModule, IFilesService } from './suramadu-files';
import { touchInjectable, TouchModule, ITouchService } from './suramadu-touch';
import { inputInjectable, InputModule, IInputService } from './suramadu-input';

export interface IServices extends IAccessibleService, IFocusManagerService, IWebswingInstanceService, IBaseService, IInputService, IDialogService, ICanvasService, ILoginService, IJsLinkService, IIdentityService, ISocketService, IClipboardService, IPingService, ITranslateService, IPlaybackService, IExternalApiService, ITouchService, IFilesService {
}

interface IInstanceMap {
    [k: string]: IInjected<typeof externalApiInjectable>
}


export default function Webswing(i18n: Translations) {
    domPolyfills();
    const util = Util(i18n);
    const global = {
        scan: scanForInstances,
        bootstrap,
        $
    }
    return global;

    function scanForInstances(root: any) {
        root = root != null ? root : global;
        const result: IInstanceMap = {};
        const instances = $('[data-webswing-instance]');
        instances.each((_, instance) => {
            const id = $(instance).data('webswingInstance');
            const active = $(instance).data('webswingActive');
            if (!active) {
                let customization;
                if (root[id] != null && root[id].options != null) {
                    $(instance).data("webswingOptions", root[id].options);
                    if (root[id].options.customization != null && typeof root[id].options.customization === 'function') {
                        customization = root[id].options.customization;
                    }
                }
                $(instance).find("#loading").remove();
                const wsInstance = bootstrap($(instance), customization);
                $(instance).attr('data-webswing-active', 'true');
                if (id != null) {
                    result[id] = wsInstance;
                }
            }
        });
        for (const exportName in result) {
            if (result.hasOwnProperty(exportName)) {
                if (root[exportName] != null && root[exportName].disconnect != null) {
                    console.warn("Bootstrapping Webswing instance named '" + exportName + "'.Instance with this name has already been bootstrapped. Disconnecting old instance.");
                    root[exportName].disconnect();
                    delete root[exportName];
                }
                root[exportName] = $.extend(result[exportName], root[exportName]);
            }
        }
    }

    function bootstrap(element: JQuery<HTMLElement>, customization?: (i: Injector) => void) {
        const inj = new Injector();

        inj.addModule('webswing', new WebswingInstanceModule(inj, element), webswingInstanceInjectable);
        inj.addModule('dialog', new DialogModule(inj), dialogInjectable);
        inj.addModule('canvas', new CanvasModule(inj), canvasInjectable);
        inj.addModule('base', new BaseModule(inj), baseInjectable);
        inj.addModule('input', new InputModule(inj), inputInjectable);
        inj.addModule('touch', new TouchModule(inj), touchInjectable);
        inj.addModule('socket', new SocketModule(inj), socketInjectable);
        inj.addModule('files', new FilesModule(inj), filesInjectable);
        inj.addModule('login', new LoginModule(inj, util), loginInjectable);
        inj.addModule('identity', new IdentityModule(inj), {});
        inj.addModule('jslink', new JsLinkModule(inj), jsLinkInjectable);
        inj.addModule('clipboard', new ClipboardModule(inj), clipboardInjectable);
        inj.addModule('playback', new PlaybackModule(inj), playbackInjectable);
        inj.addModule('translate', new TranslateModule(inj, i18n), {});
        inj.addModule('ping', new PingModule(inj), pingInjectable);
        inj.addModule('accessible', new AccessibleModule(inj), accessibleInjectable);
        inj.addModule('focusManager', new FocusManagerModule(inj), focusManagerInjectable);
        const extModule = new ExternalApiModule(inj)
        inj.addModule('external', extModule, externalApiInjectable);


        if (customization != null) {
            customization(inj);
        }
        inj.injectAndVerify();

        return extModule.getApi();
    }

}


function domPolyfills() {
    if (!Element.prototype.matches) {
        // fix for IE matches selector
        Element.prototype.matches = (Element.prototype as any).msMatchesSelector;
    }

    if (!Element.prototype.closest) {
        Element.prototype.closest = function (s: any) {
            let el: any = this;
            do {
                if (el.matches(s)) {
                    return el;
                }
                el = el.parentElement || el.parentNode;
            } while (el !== null && el.nodeType === 1);
            return null;
        };
    }
}


