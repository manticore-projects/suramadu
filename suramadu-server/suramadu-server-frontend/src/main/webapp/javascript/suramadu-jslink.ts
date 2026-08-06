import { ModuleDef, IInjected } from "./suramadu-inject";
import { GUID } from "./suramadu-util";
import { appFrameProtoIn } from "./proto/proto.in";
import { appFrameProtoOut } from "./proto/proto.out";

export const jsLinkInjectable = {
    cfg: 'webswing.config' as const,
    external: 'external.api' as const,
    send: 'socket.send' as const,
    awaitResponse: 'socket.awaitResponse' as const
}

export interface IJsLinkService {
    'jslink.process': (jsRequest: appFrameProtoOut.IJsEvalRequestMsgOutProto) => void
}

type JsEvalRequest = appFrameProtoOut.IJsEvalRequestMsgOutProto

const JsEvalRequestType = appFrameProtoOut.JsEvalRequestMsgOutProto.JsEvalRequestTypeProto
const idMemberName = '__webswing_jslink_id';
const instanceObjKey = 'instanceObject'
export class JsLinkModule extends ModuleDef<typeof jsLinkInjectable, IJsLinkService> {

    private referenceCache: { [K: string]: any } = {};

    public ready = () => {
        this.referenceCache[instanceObjKey] = this.api.external();
    };

    public provides() {
        return {
            'jslink.process': this.process
        }
    };

    public process(jsRequest: JsEvalRequest) {
        let response = {};
        try {
            if (jsRequest.evalString == null) {
                return
            }
            if (jsRequest.type === JsEvalRequestType.eval) {
                const indirectEval = eval;
                const result = indirectEval(jsRequest.evalString);
                response = this.buildResponse(result, jsRequest.correlationId);
            } else if (jsRequest.type === JsEvalRequestType.call) {
                const ref = jsRequest.thisObjectId != null ? this.referenceCache[jsRequest.thisObjectId] : window;
                const args = this.decodeParams(jsRequest);
                const result = ref[jsRequest.evalString].apply(ref, args);
                response = this.buildResponse(result, jsRequest.correlationId);
            } else if (jsRequest.type === JsEvalRequestType.setMember || jsRequest.type === JsEvalRequestType.setSlot) {
                const ref = jsRequest.thisObjectId != null ? this.referenceCache[jsRequest.thisObjectId] : window;
                const args = this.decodeParams(jsRequest);
                ref[jsRequest.evalString] = args != null ? args[0] : null;
                response = this.buildResponse(null, jsRequest.correlationId);
            } else if (jsRequest.type === JsEvalRequestType.getMember || jsRequest.type === JsEvalRequestType.getSlot) {
                const ref = jsRequest.thisObjectId != null ? this.referenceCache[jsRequest.thisObjectId] : window;
                response = this.buildResponse(ref[jsRequest.evalString], jsRequest.correlationId);
            } else if (jsRequest.type === JsEvalRequestType.deleteMember) {
                const ref = jsRequest.thisObjectId != null ? this.referenceCache[jsRequest.thisObjectId] : window;
                delete ref[jsRequest.evalString];
                response = this.buildResponse(null, jsRequest.correlationId);
            }
            if (jsRequest.garbageIds != null) {
                jsRequest.garbageIds.forEach((id) => {
                    delete this.referenceCache[id];
                });
            }
            this.api.send(response);
        } catch (e) {
            response = this.buildResponse(e, jsRequest.correlationId);
            this.api.send(response);
            throw e;
        }
    }

    public buildResponse(obj: any, correlationId?: string | null) {
        const result: appFrameProtoIn.IAppFrameMsgInProto = {
            jsResponse: this.serializeObject(obj)
        };
        result.jsResponse!.correlationId = correlationId;
        return result;
    }

    public serializeObject(object: any) {
        const jsResponse: appFrameProtoIn.IJsResultMsgInProto = {};
        if (object == null) {
            jsResponse.value=null;
            return jsResponse;
        } else if (Object.prototype.toString.call(object) === '[object Error]') {
            jsResponse.error = object.toString();
            return jsResponse;
        } else {
            jsResponse.value = {};
            if (typeof object === 'number' || typeof object === 'string' || typeof object === 'boolean') {
                jsResponse.value.primitive = JSON.stringify(object);
            } else if (object instanceof JavaObjectRef) {
                jsResponse.value.javaObject = {
                    id: object.id
                };
            } else {
                if (object[idMemberName] == null) {
                    object[idMemberName] = GUID();
                    this.referenceCache[object[idMemberName]] = object;
                }
                jsResponse.value.jsObject = {
                    id: object[idMemberName]
                };
            }
            return jsResponse;
        }
    }

    public decodeParams(jsRequest: appFrameProtoOut.IJsEvalRequestMsgOutProto) {
        const result = [];
        const args = jsRequest.params;
        if (args != null) {
            for (const arg of args) {
                result.push(this.decodeJsParam(arg));
            }
        }
        return result;
    }

    public decodeJsParam(param: appFrameProtoOut.IJsParamMsgOutProto): any {
        if (param.primitive != null) {
            return JSON.parse(param.primitive);
        } else if (param.jsObject != null && param.jsObject.id != null) {
            return this.referenceCache[param.jsObject.id];
        } else if (param.javaObject != null) {
            return new JavaObjectRef(param.javaObject, this, this.api);
        } else if (param.array != null) {
            const array = [];
            for (const p of param.array) {
                array.push(this.decodeJsParam(p));
            }
            return array;
        }
        return null;
    }
}

// tslint:disable-next-line: max-classes-per-file
class JavaObjectRef {
    [K: string]: any,
    public id?: string | null
    constructor(javaRefMsg: appFrameProtoIn.IJavaObjectRefMsgInProto, ctx: JsLinkModule, api: IInjected<typeof jsLinkInjectable>) {
        this.id = javaRefMsg.id;
        if (javaRefMsg.methods != null) {
            for (const methodName of javaRefMsg.methods) {
                const javaProxyFactory = (m: string) => {
                    // tslint:disable-next-line: only-arrow-functions
                    return function() {
                        const currentArguments = arguments;
                        return new Promise((resolve, reject) => {
                            const jCorrelationId = GUID();
                            const params: appFrameProtoOut.IJsParamMsgOutProto[] = [];
                            for (let i = 0; i < currentArguments.length; i++) {
                                const serializedObject = ctx.serializeObject(currentArguments[i]);
                                params[i] = serializedObject.value!;
                            }
                            const request = {
                                javaRequest: {
                                    correlationId: jCorrelationId,
                                    objectId: javaRefMsg.id,
                                    method: m,
                                    params
                                }
                            };

                            api.awaitResponse((result: any) => {
                                if (Object.prototype.toString.call(result) === '[object Error]') {
                                    reject(result);
                                } else if (result.error != null) {
                                    reject(new Error(result.error));
                                } else if (result.value != null) {
                                    resolve(ctx.decodeJsParam(result.value));
                                }
                            }, request, jCorrelationId, api.cfg.javaCallTimeout);
                        });
                    };
                }
                this[methodName] = javaProxyFactory(methodName);
            }
        }
    }
}