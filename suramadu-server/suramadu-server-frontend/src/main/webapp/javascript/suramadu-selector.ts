import { Util, getParam, checkCookie, getImageString } from "./suramadu-util"
import { loadTranslations } from "./suramadu-translate";
import DOMPurify from 'dompurify';

loadTranslations().then(
    (translations) => {
        const translate = (key: string) => translations.translate(key);
        const util = Util(translations);
        if (!checkCookie()) {
            const sanitized = DOMPurify.sanitize(translate('${dialog.cookiesDisabledDialog}'));
            $('#suramadu-content').html(sanitized);
            return;
        }

        window.addEventListener('storage', (event) => {
            if (event.key === 'webswingLogout') {
                location.reload();
            }
        });

        const login = util.webswingLogin;
        let user: any;
        const loginData = {
            securityToken: getParam("token"),
            realm: getParam("realm")
        };
        const url = window.location.href;
        const baseUrl = url.substring(0, Math.min(url.indexOf('#'), url.indexOf('?')));
        login(baseUrl, $('#suramadu-content'), loginData, loadApps);

        // @ts-ignore
        function loadApps(data: any, request: any) {
            localStorage.setItem("webswingLogin", Date.now().toString());

            user = request.getResponseHeader('webswingUsername');
            $.ajax({
                xhrFields: {
                    withCredentials: true
                },
                type: 'GET',
                url: 'rest/apps',
                beforeSend: (xhr) => {
                    xhr.setRequestHeader('Authorization', 'Bearer ' + util.getToken());
                },
                success: (result) => {
                    loadAdminConsoleAccess((showAdmin: any) => {
                        if (showAdmin) {
                            loadAdminConsoleUrl((adminConsoleUrl: any) => {
                                if (adminConsoleUrl && adminConsoleUrl.length) {
                                    show(result, true, adminConsoleUrl);
                                } else {
                                    show(result, false);
                                }
                            });
                        } else {
                            show(result, false);
                        }
                    });
                }
            });
        }

        function loadAdminConsoleUrl(callback: any) {
            $.ajax({
                xhrFields: {
                    withCredentials: true
                },
                type: 'GET',
                url: 'rest/adminConsoleUrl',
                beforeSend: (xhr) => {
                    xhr.setRequestHeader('Authorization', 'Bearer ' + util.getToken());
                },
                success(data) {
                    callback(data);
                },
                error() {
                    callback(false);
                }
            });
        }

        function loadAdminConsoleAccess(callback: any) {
            $.ajax({
                xhrFields: {
                    withCredentials: true
                },
                type: 'GET',
                url: 'rest/adminConsoleAccess',
                beforeSend: (xhr) => {
                    xhr.setRequestHeader('Authorization', 'Bearer ' + util.getToken());
                },
                success: (data) => {
                    callback(data && JSON.parse(data));
                },
                error: () => {
                    callback(false);
                }
            });
        }

        function formatUsername(user: string): string {
            const emailRegex: RegExp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (emailRegex.test(user)) {
                user = user.substring(0, user.indexOf('@'));
            }

            return user
                .split(/[._\-]/)
                .filter((part: string) => part.length > 0)
                .map((part: string) => part.charAt(0).toUpperCase() + part.slice(1).toLowerCase())
                .join(' ');
        }

        function show(apps: any, showAdmin: boolean, adminConsoleUrl?: string) {
            let displayName = formatUsername(user);
            let header: any = '<h1 class="ws-selector-title">${selector.welcome} <span>' + displayName + '</span>${selector.message}</h1>';

            const links = '${selector.lang} '+(showAdmin ? ' <a href="' + adminConsoleUrl + '" id="admin">${selector.admin}</a> | ' : '')
                 + '<a href="#" id="logout">${selector.logout}</a>';
            let content;
            $('#commonDialog').addClass('ws-selector')
            if (apps == null || apps.length === 0) {
                header = null;
                content = '<p id="commonDialog-title">${selector.noApp}</p>';
            } else {
                content = '<div class="ws-selector-container">';
                let counter = 0;
                for (const i in apps) {
                    if (apps.hasOwnProperty(i)) {
                        const app = apps[i];
                        content += '<div class="ws-selector-btn">'
                            + '<a href="' + app.url + '" role="button" aria-labelledby="selector-btn-' + counter + '">'
                            + '<img src="' + getImageString(app.base64Icon) + '" class="ws-selector-btn-thumb"/>'
                            + '<div id="selector-btn-' + counter + '" class="ws-selector-btn-label">' + app.name + '</div>'
                            + '</a></div>';
                        counter++;
                    }
                }
                content += '</div>';
            }
            let sanitized = DOMPurify.sanitize(translate(header));
            $('#suramadu-header').html(sanitized);

            sanitized = DOMPurify.sanitize(translate(links));
            $('#suramadu-links').html(sanitized);

            sanitized = DOMPurify.sanitize(translate(content));
            $('#suramadu-content').html(sanitized);

            // Bind logout click after DOM insertion
            // (replaces javascript: URI which DOMPurify strips)
            $('#logout').on('click', (e) => {
                e.preventDefault();
                logout();
            });
        }

        function logout() {
            const clearElement=()=>{
                $('#suramadu-header').html('');
                $('#suramadu-links').html('');
                const content =$('#suramadu-content');
                content.html('');
                return content;
            }
            util.webswingLogout("",clearElement,()=>location.reload(), ()=>location.reload(),false);
        }
    }
).catch((e) => {
    console.error("Failed to load Translations", e);
})