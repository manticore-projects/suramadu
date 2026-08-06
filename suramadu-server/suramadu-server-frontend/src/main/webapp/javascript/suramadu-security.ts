import $ from "jquery";
import { loadTranslations } from "./suramadu-translate";
import DOMPurify from 'dompurify';

const baseUrl = (window as any).webswingRequestBaseUrl ? ((window as any).webswingRequestBaseUrl + '/') : '';
loadTranslations(baseUrl).then((translations) => {
    const translate = (key: string) => translations.translate(key);

    showPartial(window.location.href, $('#suramadu-content'), (window as any).webswingPartialHtml);

    function showPartial(url: string, element: any, html: any) {
        const sanitized = DOMPurify.sanitize(translate(html));
        element.html(sanitized);
        const form = element.find('form').first();
        if (form != null) {
            form.submit((event: any) => {
                loadPartial(url, element, form.serialize());
                event.preventDefault();
            });
        }
    }

    function loadPartial(url: string, element: any, data: any) {
        if (typeof element === 'function') {
            element = element();
        }
        $.ajax({
            xhrFields: {
                withCredentials: true
            },
            type: 'POST',
            url,
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data,
            complete: (xhr) => {
                const response = xhr.responseText;
                let loginMsg: any = {};
                if (response != null) {
                    try {
                        loginMsg = JSON.parse(response);
                    } catch (error) {
                        if (baseUrl) {
                            window.top!.location.href = baseUrl;
                            return;
                        } else {
                            const sanitized = DOMPurify.sanitize(translate("<p>Invalid request</p>"));
                            element.html(sanitized);
                        }
                    }
                    if (loginMsg.redirectUrl != null) {
                        window.top!.location.href = loginMsg.redirectUrl;
                    } else if (loginMsg.partialHtml != null) {
                        showPartial(url, element, loginMsg.partialHtml);
                    } else {
                        const sanitized = DOMPurify.sanitize(translate("<p>${login.unexpectedError}</p>"));
                        element.html(sanitized);
                    }
                } else {
                    const sanitized = DOMPurify.sanitize(translate("<p>${login.serverNotAvailable}</p>"));
                    element.html(sanitized);
                }
            }
        });
    }

}).catch((e) => {
    console.error("Failed to load Translations", e);
})