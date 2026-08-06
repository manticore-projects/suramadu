import { ModuleDef, IInjected } from "./suramadu-inject";

export const externalApiInjectable = {
    start: 'webswing.start' as const,
    disconnect: 'webswing.disconnect' as const,
    configure: 'webswing.configure' as const,
    kill: 'base.kill' as const,
    setControl: 'webswing.setControl' as const,
    repaint: 'base.repaint' as const,
    instanceId: 'socket.instanceId' as const,
    requestComponentTree: 'base.requestComponentTree' as const,
    getWindows: 'base.getWindows' as const,
    getWindowById: 'base.getWindowById' as const,
    performAction: 'base.performAction' as const,
    logout: 'login.logout' as const
}

export interface IExternalApiService {
    "external.api": () => IInjected<typeof externalApiInjectable>
}

export default class ExternalApiModule extends ModuleDef<typeof externalApiInjectable, IExternalApiService> {
    public provides() {
        return {
            "external.api": this.getApi
        }
    }

    public getApi() {
        return this.api;
    }
}