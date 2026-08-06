import { ModuleDef } from "./suramadu-inject";
import { readCookie, createCookie, eraseCookie, GUID } from "./suramadu-util";

export interface IIdentityService {
    'identity.get': () => string;
    'identity.dispose': () => void;
}

const cookieName = 'webswingID';
export class IdentityModule extends ModuleDef<{}, IIdentityService> {
    public provides() {
        return {
            'identity.get': this.get,
            'identity.dispose': this.dispose
        }
    };
    public get() {
        let id = readCookie(cookieName);
        if (id != null) {
            return id;
        } else {
            id = GUID();
            createCookie(cookieName, id, 1);
            return id;
        }
    }

    public dispose() {
        eraseCookie(cookieName);
    }

}