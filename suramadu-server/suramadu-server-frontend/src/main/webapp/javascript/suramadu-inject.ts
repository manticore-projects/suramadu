// IServices import removed: DI types relaxed for TS5 compatibility.
// The runtime injector resolves arbitrary dotted service paths dynamically.

type IModuleServices = {
	[k: string]: any
}

// Relaxed types: the runtime DI resolves arbitrary dotted service paths,
// not just keys declared in IServices. TS5 strict mode enforces this.
interface IInjectable { readonly [K: string]: string }

export type IInjected<I extends IInjectable> = { [K in keyof I]: any }

export interface IInjector {
	getInjected<I extends IInjectable, P extends IModuleServices>(module: ModuleDef<I, P>): IInjected<I>
}

export abstract class ModuleDef<I extends IInjectable, P extends IModuleServices> {
	public ready?: () => void

	constructor(private i: IInjector) {
	}

	protected get api(): IInjected<I> {
		return this.i.getInjected(this)
	}

	public abstract provides(): P
}

export interface IModule<T extends IInjectable, S extends IModuleServices> {
	provides?: S,
	injects?: T,
	injected?: IInjected<T>
	ready?: () => void
}

// tslint:disable-next-line: max-classes-per-file
export class Injector implements IInjector {
	public registeredModules: { [name: string]: ModuleDef<IInjectable, IModuleServices> } = {}
	public modules: { [name: string]: IModule<IInjectable, IModuleServices> } = {}
	public services: { [name: string]: IModuleServices } = {}
	public inject: { [name: string]: IInjectable } = {}
	public ready: { [name: string]: () => void } = {}

	public addModule<I extends IInjectable, P extends IModuleServices>(name: string, module: ModuleDef<I, P>, injectable: I) {
		if (name != null && module != null && injectable != null) {
			this.modules[name] = {}
			this.registeredModules[name] = module
			this.services[name] = this.expand(name, module.provides(), module);
			this.inject[name] = injectable;
			if (module.ready) {
				this.ready[name] = module.ready;
			}
		}
	}

	public module<T extends IInjectable, S extends IModuleServices>(name: string, m: IModule<T, S>) {
		if (m != null) {
			this.modules[name] = m;
			if (m.provides != null) {
				this.services[name] = this.expand(name, m.provides);
			}
			if (m.injects != null) {
				this.inject[name] = m.injects;
			}
			if (m.ready != null) {
				this.ready[name] = m.ready;
			}
		}
	}

	public injectAndVerify() {
		let errors = '';
		for (const key in this.inject) {
			if (this.inject.hasOwnProperty(key)) {
				try {
					const original = this.modules[key].injected;
					const injected = this.injectObject(this.inject[key]);
					if (original) {
						this.modules[key].injected = Object.assign(original, injected);
					} else {
						this.modules[key].injected = injected;
					}
				} catch (e) {
					if (e instanceof InjectError) {
						errors += '\tModule ' + key + ' :\n' + e + '\n';
					} else {
						throw e;
					}
				}
			}
		}
		if (errors.length > 0) {
			throw new InjectError("Dependency injection errors:\n" + errors);
		}
		for (const key in this.ready) {
			if (this.ready.hasOwnProperty(key)) {

				try {
					this.ready[key]();
				} catch (e) {
					if (e instanceof InjectError) {
						errors += '\tModule ' + key + ' ready function :\n' + e + '\n';
					} else {
						throw e;
					}
				}
			}
		}
		if (errors.length > 0) {
			throw new InjectError("Starting modules failed:\n" + errors);
		}
	}

	public getInjected<I extends IInjectable, P extends IModuleServices>(module: ModuleDef<I, P>) {
		const modulename = Object.keys(this.registeredModules).find(k => this.registeredModules[k] === module)
		if (modulename === undefined || this.modules[modulename] === undefined) {
			throw new InjectError("IllegalState: module has not been registered by injector.");
		}
		const injected = this.modules[modulename].injected
		if (injected === undefined) {
			throw new InjectError("IllegalState: Injection not finished yet.");
		}
		return injected as IInjected<I>
	}

	private expand(name: string, provides: IModuleServices, binding?: any) {
		const result: { [k: string]: any } = {};
		for (const key in provides) {
			if (provides.hasOwnProperty(key)) {
				const keyparts = key.split(".");
				if (keyparts.length <= 1 || keyparts[0] !== name) {
					throw new InjectError("Provided service key'" + key + "' must start with owner module name '" + name + ".'");
				} else {
					const service = provides[key as keyof IModuleServices];
					const boundService = typeof service === 'function' && binding ? service.bind(binding) : service
					result[keyparts.splice(1).join(".")] = boundService
				}
			}
		}
		return result;
	}

	private injectObject(injects: IInjectable) {
		let errors = '';
		const injected: Record<string, any> = {}
		for (const key in injects) {
			if (injects.hasOwnProperty(key)) {
				try {
					const value = this.resolve(injects[key]);
					injected[key] = value;
				} catch (e) {
					if (e instanceof InjectError) {
						errors += '\t\tField ' + key + ' not injected: ' + e + '\n';
					} else {
						throw e;
					}
				}
			}
		}
		if (errors.length > 0) {
			throw new InjectError(errors);
		}
		return injected as IInjected<IInjectable>;
	}

	private resolve(path: string): any {
		try {
			return path.split('.').reduce<any>((obj, i) => {
				if (obj.hasOwnProperty(i)) {
					return obj[i];
				} else {
					throw new Error();
				}
			}, this.services);
		} catch (e) {
			throw new InjectError('service ' + path + ' not found.');
		}
	}

}

// tslint:disable-next-line:max-classes-per-file
class InjectError {
	public message: string;
	constructor(msg: string) {
		this.message = msg
	}
	public toString() {
		return this.message;
	};
}