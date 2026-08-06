import { ModuleDef, IInjector } from "./suramadu-inject";
import { msg as defaultMsg } from "./nls/msg";

export interface ITranslateService {
    'translate.translate': (key: string) => string;
    'translate.getLocale': () => string;
}

interface IStringMap { [k: string]: string }

export class TranslateModule extends ModuleDef<{}, ITranslateService> {

    constructor(i: IInjector, private t: Translations) {
        super(i);
    }

    public provides() {
        return {
            'translate.translate': (key: string) => this.t.translate(key),
            'translate.getLocale': () => this.t.getLocale()
        }
    }
}

export async function loadTranslations(baseUrl?: string) {
    baseUrl = baseUrl ? baseUrl : '';
    const langs = await loadAvailableLanguages(baseUrl);
    const msg = await loadLocalizedMsgs(baseUrl);
    return new Translations(langs, msg);
}

// tslint:disable-next-line: max-classes-per-file
export class Translations {
    private msg: typeof defaultMsg.root & IStringMap;

    constructor(avalableLangs: IStringMap | null, localizedMsgs: IStringMap) {
        this.msg = { ...defaultMsg.root, ...localizedMsgs };

        if (avalableLangs != null && Object.keys(avalableLangs).length > 1) {
            const locale = this.getLocale();
            const onchange = "localStorage.setItem('webswingLang',this.options.item(this.selectedIndex).value);location.reload();"
            let selector = '<select class="suramadu-lang-selector" aria-labelledby="selector-label" onchange="' + onchange + '">';
            for (const key in avalableLangs) {
                if (avalableLangs.hasOwnProperty(key)) {
                    const selected = locale === key ? 'selected' : '';
                    selector += '<option ' + selected + ' value="' + key + '">' + avalableLangs[key] + '</option>';
                }
            }
            selector += '</select>'
            this.msg["selector.lang"] = '<span id="selector-label">${selector.lang.label}</span>' + selector;
        } else {
            this.msg['selector.lang'] = ''
        }
    }

    public getLocale() {
        return getSelectedLang();
    }

    public translate(key: string) {
        const result = key;
        return this.replace(result, this.msg);
    }

    private replace(source: string, object: IStringMap) {
        if (source != null && source.indexOf('${') > -1) {
            for (let i = 0; i < 3; i++) {
                let res = source;
                for (const key in object) {
                    if (object.hasOwnProperty(key)) {
                        res = this.replaceAll(res, '${' + key + '}', object[key]);
                    }
                }
                if (res === source) {
                    return res;
                } else {
                    source = res;
                }
            }
        } else if (source != null && object[source.trim()] != null) {
            return object[source];
        }
        return source;
    }


    private escapeRegExp(source: string) {
        return source.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
    }

    private replaceAll(source: string, find: string, replace: string) {
        return source.replace(new RegExp(this.escapeRegExp(find), 'g'), replace);
    }
}

async function loadAvailableLanguages(baseUrl: string) {
    const lang = getSelectedLang();
    try {
        const translatedLangs = await load(baseUrl + "javascript/nls/" + lang + "/select.json");
        return translatedLangs;
    } catch (e) {
        try {
            const defaultLangs = await load(baseUrl + "javascript/nls/select.json");
            return defaultLangs;
        } catch (e1) {
            return null;
        }
    }
}

async function loadLocalizedMsgs(baseUrl: string) {
    const lang = getSelectedLang();
    const langParts = lang.split("-");
    while (langParts.length > 0) {
        try {
            const messages = await load(baseUrl + "javascript/nls/" + langParts.join("-") + "/msg.json");
            return messages;
        } catch (e) {
            langParts.pop();
        }
    }
    return null;
}

async function load(path: string) {
    const res = await fetch(path);
    try {
        const definedValue = await res.json();
        if (definedValue !== null) {
            return definedValue.root ? definedValue.root : definedValue;
        }
    } catch (e) {
        if (res.ok) {
            console.error("Failed to parse translations from " + path, e);
        } else {
            throw e;
        }
    }
    return null
}

function getSelectedLang() {
    const browserLang = navigator.language;
    let storedLang = null;
    try {
        storedLang = localStorage.getItem("webswingLang");
    } catch (e) {
        console.error(e);
    }
    return storedLang || browserLang;
}

