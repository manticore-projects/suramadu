import { ModuleDef, IInjector } from "./suramadu-inject";
import { Util } from "./suramadu-util"

export const loginInjectable = {
    cfg: 'webswing.config' as const,
    start: 'webswing.start' as const,
    disconnect: 'webswing.disconnect' as const,
    showDialog: 'dialog.show' as const,
    dialogs: 'dialog.content' as const,
}

export interface ILoginService {
    'login.login': (successCallback: () => void, failedCallback?: () => void) => void;
    'login.logout': () => void;
    'login.touchSession': (tabLogout?: boolean) => void;
    'login.user': () => string | null | undefined;
}

export class LoginModule extends ModuleDef<typeof loginInjectable, ILoginService> {
    public user?: string | null;

    constructor(i: IInjector, private util: ReturnType<typeof Util>) {
        super(i);
        this.init();
    }

    public provides() {
        return {
            'login.login': this.login,
            'login.logout': this.logout,
            'login.touchSession': this.touchSession,
            'login.user': this.getUser
        }
    }

    public login(successCallback: () => void, failedCallback?: () => void) {
        let surl
        try {
            surl = window.top!.location.href
        } catch (e) {
            surl = window.location.href
        }

        const loginData = {
            securityToken: this.api.cfg.securityToken,
            realm: this.api.cfg.realm,
            successUrl: surl
        };
        const dialogContent = () => {
            return this.api.showDialog(this.api.dialogs.emptyMessage);
        }
        this.util.webswingLogin(this.api.cfg.connectionUrl, dialogContent, loginData, (_, request) => {
        	localStorage.setItem("webswingLogin", Date.now().toString());
            this.user = request.getResponseHeader('webswingUsername');
            if (successCallback != null) {
                successCallback();
            }
        }, failedCallback);
    }

    public logout(tabLogout?: boolean) {
        const dialogContent = this.api.showDialog(this.api.dialogs.logingOut);
        this.util.webswingLogout(this.api.cfg.connectionUrl, dialogContent, () => {
            this.api.disconnect();
            this.api.start();
        }, () => {
            this.api.showDialog(this.api.dialogs.disconnectedDialog);
        }, tabLogout);
    }

    public touchSession(tabLogout?: boolean) {
        this.util.refreshLogin(this.api.cfg.connectionUrl, (success: boolean) => {
            if (!success) {
                this.logout(tabLogout);
            }
        });
    }

    public getUser() {
        return this.user;
    }
    
    private init() {
    	window.addEventListener('storage', (event) => this.tabLogout(event));
    }

    private tabLogout(event: StorageEvent) {
        if (event.key === 'webswingLogout') {
            this.touchSession(true);
        } else if (event.key === 'webswingLogin') {
        	// force refresh token
        	this.touchSession();
        }
    }
}