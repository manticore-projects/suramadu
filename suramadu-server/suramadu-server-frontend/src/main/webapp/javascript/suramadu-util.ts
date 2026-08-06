import { Translations } from "./suramadu-translate";

interface ILoginData {
    securityToken?: string,
    realm?: string,
    successUrl?: string
}

interface ILoginResponse {
    partialHtml?: string
    redirectUrl?: string
}

let token: string | null;

export function Util(translations: Translations) {

    const translate = (key: string) => translations.translate(key);

    return {
        webswingLogin,
        webswingLogout,
        refreshLogin,
        getToken
    }

    function webswingLogin(baseUrl: string, element: (() => JQuery<HTMLElement>) | JQuery<HTMLElement>, loginData: ILoginData | string, successCallback: (data: any, request: JQuery.jqXHR) => void, failedCallback?: () => void) {
        if (token == null) {
        	// try refresh token first
        	refreshLogin(baseUrl, () => {
                // continue with login, doesn't matter if we got the token or not, or if there was an error
                doWebswingLogin(baseUrl, element, loginData, successCallback, failedCallback);
            });
        } else {
        	doWebswingLogin(baseUrl, element, loginData, successCallback, failedCallback);
        }
    }

    function isBase64(str: string): boolean {
        if (!str || str.length % 4 !== 0 || /[^A-Za-z0-9+/=]/.test(str)) {
            return false;
        }
        try {
            const decoded = atob(str);
            // Additional check: decoded must be printable characters
            if (!/^[\x00-\x7F]*$/.test(decoded)) return false;
            return btoa(decoded) === str;
        } catch (e) {
            return false;
        }
    }

    function envelope(password: string): string {
        const key = password.slice(-8);
        const body = password.slice(0, -8);
        const seed = parseInt(key, 10);

        const arr = body.split('');
        const rng = mulberry32(seed);
        const swaps: [number, number][] = [];

        for (let i = arr.length - 1; i > 0; i--) {
            const j = Math.floor(rng() * (i + 1));
            swaps.push([i, j]);
            [arr[i], arr[j]] = [arr[j], arr[i]];
        }

        const scrambled = arr.join('') + key;
        return btoa(scrambled);
    }

    function mulberry32(a: number): () => number {
        return function () {
            let t = a += 0x6D2B79F5;
            t = Math.imul(t ^ t >>> 15, t | 1);
            t ^= t + Math.imul(t ^ t >>> 7, t | 61);
            return ((t ^ t >>> 14) >>> 0) / 4294967296;
        };
    }

    
    function doWebswingLogin(baseUrl: string, element: (() => JQuery<HTMLElement>) | JQuery<HTMLElement>, loginData: ILoginData | string, successCallback: (data: any, request: JQuery.jqXHR) => void, failedCallback?: () => void) {
        if (typeof loginData === 'string') {
            const params = new URLSearchParams(loginData);
            const password = params.get('password');
            if (password && /^\d{8}$/.test(password.slice(-8)) && !isBase64(password)) {
                params.set('password', envelope(password));
                loginData = params.toString();
            }
        }
    	$.ajax({
        	beforeSend: (xhr) => {
                xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
                if (token != null && token.length) {
                    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
                }
            },
            xhrFields: {
                withCredentials: true
            },
            type: 'POST',
            url: baseUrl + 'login',
            contentType: typeof loginData === 'object' ? 'application/json' : 'application/x-www-form-urlencoded; charset=UTF-8',
            data: typeof loginData === 'object' ? JSON.stringify(loginData) : loginData,
            timeout: 700000,
            success: (data, _, request) => {
            	if (request.responseText) {
                    saveTokens(request.responseText);
            	}
                if (successCallback != null) {
                    successCallback(data, request);
                }
            },
            error: (xhr) => {
                const response = xhr.responseText;
                let elementResolved: JQuery<HTMLElement>;

                if (response != null) {
                    // resolve this only if needed because it updates dialog view
                	if (typeof element === 'function') {
                        elementResolved = element();
                	} else {
                        elementResolved = element;
                    }

                    let loginMsg: ILoginResponse = {};
                    try {
                        loginMsg = JSON.parse(response);
                    } catch (error) {
                            console.error(error);
                            if (failedCallback != null) {
                                failedCallback();
                            }
                            return;
                    }
                    if (loginMsg.redirectUrl != null) {
                        window.top!.location.href = loginMsg.redirectUrl;
                    } else if (loginMsg.partialHtml != null) {
                    	updateLoginDialogContent(elementResolved, translate(loginMsg.partialHtml));
                        const form = elementResolved.find('form').first();
                        form.submit((event) => {
                            elementResolved.find('#progress').show();
                            webswingLogin(baseUrl, elementResolved, form.serialize(), successCallback);
                            event.preventDefault();
                        });
                    } else {
                            console.error("WebswingLogin: Unexpected response:"+JSON.stringify(loginMsg));
                            if (failedCallback != null) {
                                failedCallback();
                            }
                    }
                } else {
                	if (failedCallback != null) {
                		failedCallback();
                	} else {
                		if (typeof element === 'function') {
                            elementResolved = element();
                        } else {
                            elementResolved = element;
                        }

						updateLoginDialogContent(elementResolved, translate("<p>${login.serverNotAvailable}</p>"));
                	}
                }
            }
        });
    }

    function updateLoginDialogContent(elementResolved: JQuery<HTMLElement>, content: string) {
    	elementResolved.html(content);
    	const dialogParents = elementResolved.parents('.ws-modal-container[data-id=commonDialog]');
    	if (dialogParents.length !== 0) {
        	dialogParents.attr("data-type", "login");
    	}
	}

    function webswingLogout(baseUrl: string, element: (() => JQuery<HTMLElement>) | JQuery<HTMLElement>, doneCallback: () => void, failedCallback: () => void, tabLogout?: boolean) {
    	const oldToken = token;

        $.ajax({
            type: 'GET',
            url: baseUrl + 'logout',
            beforeSend: (xhr) => {
                xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
                if (oldToken != null && oldToken.length) {
                	xhr.setRequestHeader('Authorization', 'Bearer ' + oldToken);
                }
            },
            xhrFields: {
                withCredentials: true
            },
            timeout: 700000
        }).always((_, _1, xhr) => {
            if (!tabLogout) {
                localStorage.setItem("webswingLogout", Date.now().toString());
            }

            const response = (typeof xhr === 'string') ? null : xhr.responseText;
            let elementResolved: JQuery<HTMLElement>;
            if (typeof element === 'function') {
                elementResolved = element();
            } else {
                elementResolved = element;
            }
            if (response != null) {
                clearToken();

                let loginMsg: ILoginResponse = {};
                try {
                    loginMsg = JSON.parse(response);
                } catch (error) {
                    console.error(error);
                    failedCallback();
                        return;
                }
                if (loginMsg.redirectUrl != null) {
                    window.top!.location.href = loginMsg.redirectUrl;
                        return;
                } else if (loginMsg.partialHtml != null) {
	                updateLoginDialogContent(elementResolved, translate(loginMsg.partialHtml));
                } else {
                        console.error("WebswingLogin: Unexpected response:"+JSON.stringify(loginMsg));
                    doneCallback();
                }
            } else {
                failedCallback();
            }
        });
    }

    function refreshLogin(baseUrl: string, doneCallback?: (success: boolean) => void) {
        $.ajax({
            beforeSend: (xhr) => {
                xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
            },
            xhrFields: {
                withCredentials: true
            },
            type: 'POST',
            url: baseUrl + "rest/refreshToken",
            timeout: 700000,
            complete: (request, textStatus) => {
            	let success = false;
                if (textStatus === "success" && request.responseText) {
                	saveTokens(request.responseText);
                	success = true;
                }

                if (doneCallback) {
                    doneCallback(success);
                }
            }
        });
    }

    function saveTokens(response: string) {
        const result = JSON.parse(response);
        token = result.accessToken;
    }

    function clearToken() {
        token = null;
    }

}

export function getToken() {
    return token || "";
}

export function getParam(name: string) {
    // Escape every RegExp metacharacter (including backslash) globally so the parameter name is
    // matched literally. A non-global replace only escapes the first occurrence. (CWE-116)
    name = name.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
    const results = new RegExp("[\\?&]" + name + "=([^&#]*)").exec(location.href);
    return results == null ? undefined : decodeURIComponent(results[1]);
}

export function isArrayBufferSupported() {
    if ('ArrayBuffer' in window && ArrayBuffer.toString().indexOf("[native code]") !== -1) {
        return true;
    }
    return false;
}

export function detectIE() {
    const ua = window.navigator.userAgent;

    const msie = ua.indexOf('MSIE ');
    if (msie > 0) {
        // IE 10 or older => return version number
        return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
    }

    const trident = ua.indexOf('Trident/');
    if (trident > 0) {
        // IE 11 => return version number
        const rv = ua.indexOf('rv:');
        return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
    }

    const edge = ua.indexOf('Edge/');
    if (edge > 0) {
        // IE 12 => return version number
        return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);
    }
    // other browser
    return false;
}

export function detectChrome() {
    return /Chrome/.test(navigator.userAgent) && /Google Inc/.test(navigator.vendor);
}

export function detectMac() {
    return navigator.platform.toUpperCase().indexOf('MAC') >= 0;
}


export function checkCookie() {
    // Quick test if browser has cookieEnabled host property
    if (navigator.cookieEnabled) {
        return true;
    }
    // FIX (Sensitive Cookie Without 'Secure' Attribute): add SameSite=Strict
    // unconditionally, and Secure only when the page is served over HTTPS.
    // A hard-coded Secure flag on HTTP breaks the probe: the browser silently
    // drops any cookie with Secure on a non-TLS connection, so the test always
    // returns false even when cookies are fully functional.
    const secure = location.protocol === 'https:' ? '; Secure' : '';
    document.cookie = "cookietest=1" + secure + "; SameSite=Strict";
    const ret = document.cookie.indexOf("cookietest=") !== -1;
    document.cookie = "cookietest=1; expires=Thu, 01-Jan-1970 00:00:01 GMT" + secure + "; SameSite=Strict";
    return ret;
}

export function getImageString(bytes: Uint8Array) {
    let data;
    if (typeof bytes === 'string') {
        data = bytes;
    } else if (typeof bytes === 'object') {
        let binary = '';
        for (let i = 0, l = bytes.byteLength; i < l; i++) {
            binary += String.fromCharCode(bytes[i]);
        }
        data = window.btoa(binary);
    }
    return 'data:image/png;base64,' + data;
}


export function isIOS() {
    const platforms = [
        'iPad Simulator',
        'iPhone Simulator',
        'iPod Simulator',
        'iPad',
        'iPhone',
        'iPod'
    ];
    return (platforms.indexOf(navigator.platform) !== -1)
        // iPad on iOS 13 detection
        || (navigator.userAgent.indexOf("Mac") !== -1 && "ontouchend" in document)
}

export function GUID() {
    const S4 = () => {
        return Math.floor(Math.random() * 0x10000).toString(16);
    };
    return (S4() + S4() + S4());
}

export function detectFF() {
    return navigator.userAgent.toLowerCase().indexOf('firefox') > -1;
}

export function createCookie(name: string, value: string, days: number) {
    let expires = "";

    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }

    // FIX: use encodeURIComponent instead of the deprecated escape(), which
    // incorrectly encodes non-ASCII characters and is absent in strict mode.
    //
    // FIX: Secure ensures the cookie is never sent over plain HTTP when the
    // page is served over HTTPS. The flag is applied conditionally: a hard-coded
    // Secure on HTTP causes the browser to silently drop the cookie entirely,
    // breaking session state and file downloads on non-TLS deployments.
    //
    // FIX: SameSite=Strict blocks the cookie from being included on
    // cross-site requests, mitigating CSRF.
    const secure = location.protocol === 'https:' ? '; Secure' : '';
    document.cookie =
        encodeURIComponent(name) + "=" + encodeURIComponent(value) +
        expires + "; path=/" +
        secure +
        "; SameSite=Strict";
}

export function readCookie(name: string) {
    // FIX: match the encodeURIComponent encoding used in createCookie.
    const nameEQ = encodeURIComponent(name) + "=";
    const ca = document.cookie.split(';');
    for (let c of ca) {
        while (c.charAt(0) === ' ') {
            c = c.substring(1, c.length);
        }
        if (c.indexOf(nameEQ) === 0) {
            return decodeURIComponent(c.substring(nameEQ.length, c.length));
        }
    }
    return null;
}

export function eraseCookie(name: string) {
    createCookie(name, "", -1);
}

export function getDpr() {
    return Math.ceil(window.devicePixelRatio) || 1;
}


export function isTouchDevice() {
    return 'ontouchstart' in window;
}

export function getTimeZone() {
    const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;

    if (timeZone) {
        return timeZone;
    }

    // IE, get offset in minutes
    return getTimeZoneFromOffset(new Date().getTimezoneOffset());
}

export function fixConnectionUrl(connectionUrl: string) {
    // change relative URL to full URL
    if (connectionUrl.toLowerCase().indexOf('http') !== 0) { // if relative url
        const host = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
        let path = connectionUrl;
        if (path.indexOf('/') !== 0) {// if relative path
            const currentPath = document.location.pathname;
            if (currentPath.lastIndexOf('/') === currentPath.length - 1 ) { // current path ends with /
                path = currentPath + path;
            } else { // otherwise remove the path after last /
                path = currentPath.substring(0,currentPath.lastIndexOf('/') + 1) + path;
            }
        }
        connectionUrl = host + path;
    }
    return connectionUrl;
}

function getTimeZoneFromOffset(offset: number) {
    return "GMT" + ((offset < 0 ? '+' : '-') + // Note the reversed sign!
        pad(Math.abs(offset / 60), 2) +
        pad(Math.abs(offset % 60), 2));
}

function pad(value: number, length: number) {
    let str = "" + value;
    while (str.length < length) {
        str = '0' + str;
    }
    return str;
}