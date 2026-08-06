import html from "./templates/dialog.html"
import networkHtml from './templates/network.html'
import { ModuleDef } from "./suramadu-inject";

export const dialogInjectable = {
    cfg: 'webswing.config' as const,
    continueSession: 'base.continueSession' as const,
    kill: 'base.kill' as const,
    newSession: 'webswing.newSession' as const,
    reTrySession: 'webswing.reTrySession' as const,
    logout: 'login.logout' as const,
    translate: 'translate.translate' as const,
    mutePingWarning: 'ping.mutePingWarning' as const,
    switchMode: 'touch.switchMode' as const,
    getFocusedWindow: 'base.getFocusedWindow' as const,
    getMainWindowVisibilityState: 'base.getMainWindowVisibilityState' as const,
    focusDefault: 'focusManager.focusDefault' as const,
    isAutoLogout: 'socket.isAutoLogout' as const,
    startNewSessionOnShutdown: 'base.startNewSessionOnShutdown' as const,
    clearInstanceId: 'socket.clearInstanceId' as const
}

export interface IDialogService {
    'dialog.show': (msg: IDialogContent) => JQuery<HTMLElement>,
    'dialog.hide': () => void,
    'dialog.current': () => IDialogContent | null | undefined,
    'dialog.currentBar': () => IDialogContent | null | undefined,
    'dialog.showBar': (msg: IDialogContent) => JQuery<HTMLElement>,
    'dialog.hideBar': () => void,
    'dialog.showNetworkBar': (msg: IDialogContent) => void,
    'dialog.hideNetworkBar': () => void,
    'dialog.showOverlay': (win: Window, msg: IDialogContent) => void,
    'dialog.hideOverlay': (win: Window, msg: IDialogContent) => void
    'dialog.content': IDialogs,
}

interface IDialogs {
    emptyMessage: IDialogContent
    logingOut: IDialogContent
    readyDialog: IDialogContent
    initializingDialog: IDialogContent
    startingDialog: IDialogContent
    reconnectingDialog: IDialogContent
    applicationBusyDialog: IDialogContent
    unauthorizedAccess: IDialogContent
    applicationAlreadyRunning: IDialogContent
    sessionStolenNotification: IDialogContent
    disconnectedDialog: IDialogContent
    connectionErrorDialog: IDialogContent
    tooManyClientsNotification: IDialogContent
    stoppedDialog: IDialogContent
    reconnectInstanceNotFound: IDialogContent
    timedoutDialog: IDialogContent
    cookiesDisabledDialog: IDialogContent
    continueOldSessionDialog: IDialogContent
    longPollingWarningDialog: IDialogContent
    inactivityTimeoutWarningDialog: IDialogContent
    networkOfflineWarningDialog: IDialogContent
    networkSlowWarningDialog: IDialogContent
    touchSwitchModeMouseDialog: IDialogContent
    touchSwitchModeTouchDialog: IDialogContent
    dockingVisibilityOverlay: IDialogContent
    dockingModalityOverlay: IDialogContent
}


interface IDialogContent {
    header?: string;
    content: string;
    buttons?: Array<IDialogButton|(()=>IDialogButton|false)>;
    events?: {
        [K: string]: (evt: JQuery.TriggeredEvent) => void;
    };
    severity?: number,
    type?: 'visibility-overlay' | 'modality-overlay',
    focused?: boolean,
    autoReconnect?: boolean
}
interface IDialogButton { label: string,  action: () => void}
interface IActiveDialog { win: Window, dialog: JQuery<HTMLElement>, content: JQuery<HTMLElement>, header: JQuery<HTMLElement>, currentContent?: IDialogContent }
interface IActiveBar { win: Window, bar: JQuery<HTMLElement>, barContent: JQuery<HTMLElement>, barHeader: JQuery<HTMLElement>, currentContentBar?: IDialogContent }

export class DialogModule extends ModuleDef<typeof dialogInjectable, IDialogService> {

    private dialogMap: { [K: string]: IActiveDialog } = {}; // <window>: {dialog, content, header, currentContent}
    private barMap: { [K: string]: IActiveBar } = {}; // <window>: {bar, barContent, barHeader, currentContentBar}
    private networkBar?: JQuery<HTMLElement>;
    private reconnectTimer: number | null = null;
    private reconnectInterval: number | null = null;
    private reconnectStart?: number;

    public provides() {
        return {
            'dialog.show': this.show,
            'dialog.hide': this.hide,
            'dialog.current': this.current,
            'dialog.content': this.configuration(),
            'dialog.currentBar': this.currentBar,
            'dialog.showBar': this.showBar,
            'dialog.hideBar': this.hideBar,
            'dialog.showNetworkBar': this.showNetworkBar,
            'dialog.hideNetworkBar': this.hideNetworkBar,
            'dialog.showOverlay': this.showOverlay,
            'dialog.hideOverlay': this.hideOverlay
        }
    }
    private configuration(): IDialogs {
        return {
            emptyMessage: this.messageDialog('dialog.emptyMessage', true),
            logingOut: this.messageDialog('dialog.logingOut'),
            readyDialog: this.messageDialog('dialog.readyDialog'),
            initializingDialog: this.messageDialog('dialog.initializingDialog', true),
            startingDialog: this.messageDialog('dialog.startingDialog', true),
            reconnectingDialog: this.messageDialog('dialog.reconnectingDialog', true),
            applicationBusyDialog: this.messageDialog('dialog.applicationBusyDialog', true),
            unauthorizedAccess: this.retryMessageDialog('dialog.unauthorizedAccess'),
            applicationAlreadyRunning: this.retryMessageDialog('dialog.applicationAlreadyRunning'),
            sessionStolenNotification: this.retryMessageDialog('dialog.sessionStolenNotification'),
            disconnectedDialog: this.retryMessageDialog('dialog.disconnectedDialog', true),
            connectionErrorDialog: this.retryMessageDialog('dialog.connectionErrorDialog', true),
            tooManyClientsNotification: this.retryMessageDialog('dialog.tooManyClientsNotification', true),
            stoppedDialog: this.finalMessageDialog('dialog.stoppedDialog'),
            timedoutDialog: this.finalMessageDialog('dialog.timedoutDialog'),
            cookiesDisabledDialog: this.messageDialog('dialog.cookiesDisabledDialog'),
            continueOldSessionDialog: {
                header: '<span class="ws-message--neutral">${dialog.continueH} <span data-id="dialogHide" class="ws-icon-cancel-circled ws-right"></span></span>',
                content: '<p data-id="continueMsg">${dialog.continueC1}<a href="javascript:void(0)" data-id="restartLink">${dialog.continueC2}</a></p>',
                events: {
                    'restartLink_click': () => {
                        this.api.kill();
                        this.api.startNewSessionOnShutdown();
                    },
                    'dialogHide_click': () => this.hideBar(),
                    'continueMsg_mouseenter': () => {
                        const winBar = this.getBar();
                        if (winBar && winBar.currentContentBar) {
                            winBar.currentContentBar.focused = true;
                        }
                    },
                    'continueMsg_mouseleave': () => {
                        const winBar = this.getBar();
                        if (winBar && winBar.currentContentBar) {
                            winBar.currentContentBar.focused = false;
                        }
                    }
                }
            },
            longPollingWarningDialog: {
                header: '<span class="ws-message--warning"><span class="ws-icon-warn"></span> ${dialog.longPollingH}</span>',
                content: '<p>${dialog.longPollingC} </p>',
                buttons: [{
                    label: '<span class="ws-icon-cancel-circled"></span>  ${dialog.longPollingB}',
                    action: () => {
                        this.hideBar();
                    }
                }]
            },
            inactivityTimeoutWarningDialog: {
                header: '<span class="ws-message--warning"><span class="ws-icon-warn"></span> ${dialog.inactivityTimeoutH}</span>',
                content: '<p>${dialog.inactivityTimeoutC}  </p>',
                buttons: [{
                    label: '<span class="ws-icon-cancel-circled"></span>  ${dialog.inactivityTimeoutB}',
                    action: () => {
                        this.hideBar();
                    }
                }]
            },
            networkOfflineWarningDialog: {
                content: '<span class="ws-message--error"><span class="ws-icon-warn"></span>${dialog.networkMonitor.offline}</span>',
                severity: 2
            },
            networkSlowWarningDialog: {
                content: '<span class="ws-message--warning"><span class="ws-icon-warn"></span>${dialog.networkMonitor.warn}</span>',
                severity: 1
            },
            touchSwitchModeMouseDialog: {
                content: '<p><span class="ws-message--neutral">${dialog.touchSwitchMode.mouse}</span> <span class="ws-right"><a href="javascript:void(0)" data-id="barHide">${dialog.touchSwitchMode.cancelButton}</a></span></p>',
                buttons: [{
                    label: '${dialog.touchSwitchMode.switchButton}',
                    action: () => {
                        this.hideBar();
                        this.api.switchMode(true, false);
                    }
                }],
                events: {
                    'barHide_click': () => {
                        this.hideBar();
                        this.api.switchMode(false, true);
                    }
                }
            },
            touchSwitchModeTouchDialog: {
                content: '<p><span class="ws-message--neutral">${dialog.touchSwitchMode.touch}</span> <span class="ws-right"><a href="javascript:void(0)" data-id="barHide">${dialog.touchSwitchMode.cancelButton}</a></span></p>',
                buttons: [{
                    label: '${dialog.touchSwitchMode.switchButton}',
                    action: () => {
                        this.hideBar();
                        this.api.switchMode(true, false);
                    }
                }],
                events: {
                    'barHide_click': () => {
                        this.hideBar();
                        this.api.switchMode(false, true);
                    }
                }
            },
            reconnectInstanceNotFound: {
                content: 
                    '<p id="commonDialog-autoReconnect">${dialog.autoReconnect}<span id="commonDialog-autoReconnect-time"></span></p>' +  
                    '<p id="commonDialog-description">${dialog.reconnectInstanceNotFound}</p>',
                buttons: [{
                    label: '<span class="ws-icon-arrows-cw"></class> ${dialog.reconnectInstanceNotFound.retry}',
                    action: () => {
                        this.api.reTrySession();
                    }
                }, {
                    label: '<span class="ws-icon-certificate"></class> ${dialog.reconnectInstanceNotFound.newSession}',
                    action: () => {
                        this.api.clearInstanceId();
                        this.api.reTrySession();
                    }
                }, () => !this.api.cfg.disableLogout && {
                    label: '<span class="ws-icon-logout"></class> ${dialog.reconnectInstanceNotFound.logout}',
                    action: () => {
                        this.api.logout();
                    }
                }],
                autoReconnect: true
            },
            dockingVisibilityOverlay: {
                type: 'visibility-overlay',
                content: '<div class="visibility-overlay"><div class="visibility-message">${dialog.overlay.docking.visibility}</div></div>'
            },
            dockingModalityOverlay: {
                type: 'modality-overlay',
                content: '<div class="modality-overlay"><div class="modality-message">${dialog.overlay.docking.modality}<div class="modality-button"><button>${dialog.overlay.docking.focusWindow}</button></div></div></div>'
            }
        };
    }

    private messageDialog(msg: string, withSpinner?: boolean) {
        let content = '<p id="commonDialog-description">${' + msg + '}</p>';
        if (withSpinner) {
            content = '<div class="ws-spinner"><div class="ws-spinner-dot-1"></div> <div class="ws-spinner-dot-2"></div></div>' + content;
        }
        return {
            content
        };
    }

    private finalMessageDialog(msg: string) {
        return {
            content: '<p id="commonDialog-description">${' + msg + '}</p>',
            buttons: [{
                label: '<span class="ws-icon-certificate"></class> ${dialog.finalB1}',
                action: () => {
                    this.api.newSession();
                }
            }, ()=>!this.api.cfg.disableLogout && {
                label: '<span class="ws-icon-logout"></class> ${dialog.finalB2}',
                action: () => {
                    this.api.logout();
                }
            }]
        };
    }

    private retryMessageDialog(msg: string, autoReconnect?: boolean) {
        return {
            content:
                '<p id="commonDialog-autoReconnect">${dialog.autoReconnect}<span id="commonDialog-autoReconnect-time"></span></p>' +  
                '<p id="commonDialog-description">${' + msg + '}</p>',
            buttons: [{
                label: '<span class="ws-icon-arrows-cw"></class> ${dialog.retryB1}',
                action: () => {
                    this.api.reTrySession();
                }
            }, ()=>!this.api.cfg.disableLogout && {
                label: '<span class="ws-icon-logout"></class> ${dialog.retryB2}',
                action: () => {
                    this.api.logout();
                }
            }],
            autoReconnect
        };
    }

    private setup() {
        // try cleanup
        for (const winName in this.dialogMap) {
            if (!this.dialogMap[winName].win || this.dialogMap[winName].win.closed) {
                delete this.dialogMap[winName];
            }
        }
        for (const winName in this.barMap) {
            if (!this.barMap[winName].win || this.barMap[winName].win.closed) {
                delete this.barMap[winName];
            }
        }

        const win = this.api.getFocusedWindow();

        if (this.dialogMap[win.name]) {
            return;
        }

        const rootElement: JQuery<HTMLElement> = ($(win.document).find(".suramadu-element-content") as unknown) as JQuery<HTMLElement>;

        rootElement[0].querySelector('div[data-id="commonDialog"]')?.remove();
        rootElement[0].querySelector('div[data-id="commonBar"]')?.remove();

        rootElement.append(html);
        const dialog = rootElement.find('div[data-id="commonDialog"]');
        const winDlg = {
            win, dialog, content: dialog.find('div[data-id="content"]'),
            header: dialog.find('div[data-id="header"]')
        }

        const bar = rootElement.find('div[data-id="commonBar"]');
        const winBar = {
            win, bar, barContent: bar.find('div[data-id="content"]'),
            barHeader: bar.find('div[data-id="header"]')
        }

        winBar.bar.hide();

        this.dialogMap[win.name] = winDlg;
        this.barMap[win.name] = winBar;

        let spinnerTimer: number | undefined;

        $(document).ajaxStart(() => {
            spinnerTimer = setTimeout(() => {
                if (winDlg.dialog.is(":visible")) {
                	if (winDlg.dialog.find('.ws-spinner:visible').length == 0) {
                		rootElement.find('#ajaxProgress').slideDown('fast');
                	}
                }
            }, 200);
        }).ajaxComplete(() => {
            if (spinnerTimer) {
                clearTimeout(spinnerTimer);
            }
            rootElement.find('#ajaxProgress').hide();
        });
    }

    private show(msg: IDialogContent) {
        const winDlg = this.getDialog();

        this.resetAutoReconnect();

		winDlg.dialog.attr("data-type", "common");

		if (msg == null) {
            winDlg.currentContent = undefined;
            return winDlg.content;
        }

        if (winDlg.dialog.is(":visible") && winDlg.currentContent === msg) {
            // do not re-create the same dialog if it's already showing
            this.checkAndSetupAutoReconnect(msg, winDlg.content);
        	return winDlg.content;
        }

        winDlg.currentContent = msg;
        this.generateContent(msg, winDlg.dialog, winDlg.header, winDlg.content);
        winDlg.dialog.slideDown('fast');

        const initFocus = winDlg.dialog.find(".init-focus");
        if (initFocus.length) {
            initFocus[0].focus();
        }

        return winDlg.content;
    }

    private showBar(msg: IDialogContent) {
        const winBar = this.getBar();
        
        if (msg == null) {
            winBar.currentContentBar = undefined;
            return winBar.barContent;
        }

        winBar.currentContentBar = msg;
        this.generateContent(msg, winBar.bar, winBar.barHeader, winBar.barContent);
        winBar.bar.slideDown('fast');
        return winBar.barContent;
    }

    private generateContent(msg: IDialogContent, dialog: JQuery<HTMLElement>, header: JQuery<HTMLElement>, content: JQuery<HTMLElement>) {
        if (msg.header != null) {
            header.html(this.api.translate(msg.header));
            if (dialog.is(":visible")) {
                header.fadeIn(200);
            } else {
                header.show();
            }
        } else {
            header.hide();
            header.html('');
        }

        content.html(this.api.translate(msg.content));

        for (const e in msg.events) {
            if (msg.events.hasOwnProperty(e)) {
                const element = dialog.find('*[data-id="' + e.substring(0, e.lastIndexOf('_')) + '"]');
                element.bind(e.substring(e.lastIndexOf('_') + 1), msg.events[e]);
            }
        }

        if (msg.buttons) {
            for (let i = 0; i < msg.buttons.length; i++) {
                const btnOrFn = msg.buttons[i];
                let btn:IDialogButton|false;
                if(typeof btnOrFn ==='function'){
                    btn= btnOrFn();
                }else{
                    btn= btnOrFn;
                }
                if(btn){
                    const button = $('<button class="ws-btn">' + this.api.translate(btn.label) + '</button><span> </span>');
                    if (i === 0) {
                        button.addClass("init-focus");
                        }
                    button.on('click', btn.action);
                    content.append(button);
                }
            }
        }

        this.checkAndSetupAutoReconnect(msg, content);

        if (dialog.is(":visible")) {
            content.fadeIn('fast');
        }
    }

    private hide() {
        const winDlg = this.getDialog();

        if (!winDlg) {
            return;
        }

        this.resetAutoReconnect();

        winDlg.currentContent = undefined;
        winDlg.content.html('');
        if (winDlg.dialog && winDlg.dialog.is(":visible")) {
            winDlg.dialog.fadeOut('fast', () => {
                this.api.focusDefault();
            });
        }
    }

    private hideBar() {
        const winBar = this.getBar();

        if (!winBar) {
            return;
        }

        winBar.currentContentBar = undefined;
        winBar.barContent.html('');
        winBar.bar.fadeOut('fast');
    }

    private current() {
        const winDlg = this.getDialog();

        if (!winDlg) {
            return null;
        }

        return winDlg.currentContent;
    }

    private currentBar() {
        const winBar = this.getBar();

        if (!winBar) {
            return null;
        }

        return winBar.currentContentBar;
    }

    private getDialog() {
        const win = this.api.getFocusedWindow();

        if (!this.dialogMap[win.name]) {
            this.setup();
        }

        return this.dialogMap[win.name];
    }

    private getBar() {
        const win = this.api.getFocusedWindow();

        if (!this.barMap[win.name]) {
            this.setup();
        }

        return this.barMap[win.name];
    }

    private showNetworkBar(msg: IDialogContent) {
        const doc = this.api.getFocusedWindow().document;
        const rootElement = ($(doc).find(".suramadu-element-content") as unknown) as JQuery<HTMLElement>;

        if (this.networkBar != null && !$.contains(doc as any, this.networkBar[0])) {
            this.hideNetworkBar();
        }

        if (this.networkBar == null) {
            rootElement.append(this.api.translate(networkHtml));
            this.networkBar = rootElement.find('div[data-id="networkBar"]');
            this.networkBar.find('a[data-id="hide"]').on('click', () => {
                this.api.mutePingWarning(msg.severity!);
                this.hideNetworkBar();
            });
        }

        const translatedMsg = this.api.translate(msg.content);
        const msgElement = this.networkBar.find('span[data-id="message"]');
        if (msgElement.html() !== translatedMsg) {
            // if we replace the same message again and again, it will trigger screen reader to read it again
            msgElement.html(translatedMsg);
        }
        this.networkBar.show("fast");
    }

    private hideNetworkBar() {
        if (this.networkBar != null) {
            this.networkBar.hide("fast");
            this.networkBar.remove();
            this.networkBar = undefined;
        }
    }

    private showOverlay(win: Window, msg: IDialogContent) {
        let overlay = $(win.document as any).find(".suramadu-element-content ." + msg.type);

        if (overlay.length === 0) {
            overlay = $(this.api.translate(msg.content));
            $(win.document).find('.suramadu-element-content').append(overlay);
        }

        overlay.addClass("active");

        if (msg.type === 'visibility-overlay') {
            $(win.document).find(".suramadu-element-content").addClass("suramadu-disabled");
            $(win.document).find(".modality-overlay").addClass("suppressed");
        } else if (msg.type === 'modality-overlay') {
            overlay.toggleClass("suppressed", this.api.getMainWindowVisibilityState() === 'hidden');
            overlay.find("button").off("click");
        }
    }

    private hideOverlay(win: Window, msg: IDialogContent) {
        const overlay = $(win.document).find(".suramadu-element-content ." + msg.type);

        if (overlay.length === 0) {
            return;
        }

        overlay.removeClass("active");

        if (msg.type === 'visibility-overlay') {
            $(win.document).find(".suramadu-element-content").removeClass("suramadu-disabled");
            $(win.document).find(".modality-overlay").removeClass("suppressed");
        } else if (msg.type === 'modality-overlay') {
            (overlay as any).removeClass("suppressed", this.api.getMainWindowVisibilityState() === 'hidden'); // TODO: why type error (remove cast to any)
            overlay.find("button").off("click");
        }
    }

    private resetAutoReconnect() {
        if (this.reconnectTimer != null) {
            clearTimeout(this.reconnectTimer);
            this.reconnectTimer = null;
        }
        if (this.reconnectInterval != null) {
            clearInterval(this.reconnectInterval);
            this.reconnectInterval = null;
        }
        if (this.reconnectStart) {
            this.reconnectStart = 0;
        }
    }

    private checkAndSetupAutoReconnect(msg: IDialogContent, content: JQuery<HTMLElement>) {
        if (!msg.autoReconnect || this.api.cfg.autoReconnect === null || this.api.cfg.autoReconnect <= 0) {
            return;
        }

        content.find("#commonDialog-autoReconnect").show();
        this.reconnectStart = new Date().getTime()
        this.reconnectTimer = setTimeout(() => {
            this.api.reTrySession();
            if (this.reconnectInterval != null) {
                clearInterval(this.reconnectInterval);
                this.reconnectInterval = null;
            }
        }, this.api.cfg.autoReconnect!);
        this.reconnectInterval = setInterval(() => {
            const seconds = Math.round((this.api.cfg.autoReconnect! - (new Date().getTime() - this.reconnectStart!)) / 1000);
            if (seconds === 0) {
                content.find("#commonDialog-autoReconnect-time").text("");
            } else {
                content.find("#commonDialog-autoReconnect-time").text("(" + seconds + ")");
            }
        }, 250);
    }

}
