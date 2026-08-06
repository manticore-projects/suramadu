/**
 * admin-console-login-init.js
 *
 * Extracted from adminConsoleLogin.html so that the page can run under a strict
 * Content-Security-Policy (script-src 'self') without needing 'unsafe-inline'.
 *
 * Loaded via:
 *   <script data-webswing-global-var="webswing"
 *           src="javascript/admin-console-login-init.js"></script>
 *
 * The data-webswing-global-var attribute on the <script> tag tells
 * suramadu-embed.js to expose its API as window.webswing.
 */
(function (window, document) {

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Returns a validated same-origin base URL for this page.
     * Throws if the current origin is not http/https.
     */
    function getBaseUrl() {
        var origin   = window.location.origin;
        var pathname = window.location.pathname;

        if (!/^https?:\/\//.test(origin)) {
            throw new Error("Unexpected origin scheme.");
        }

        var base = origin + pathname;
        // Strip the filename so we get the directory.
        base = base.replace(/adminConsoleLogin\.html([?#].*)?$/, "");
        // Ensure trailing slash.
        if (base.charAt(base.length - 1) !== "/") {
            base = base + "/";
        }
        return base;
    }

    /**
     * Reads a named query-string parameter from the current URL.
     * Values are NOT injected into the DOM here, so URL-decoding is sufficient.
     * Apply additional encoding if you ever place them into HTML/href/script contexts.
     */
    function getParam(name) {
        try {
            return new URLSearchParams(window.location.search).get(name);
        } catch (e) {
            // IE11 fallback.
            // Escape every RegExp metacharacter (including backslash) so the name is matched
            // literally; escaping only the brackets leaves a backslash injection gap. (CWE-116)
            var safeName = name.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
            var results  = new RegExp("[?&]" + safeName + "=([^&#]*)").exec(window.location.search);
            return results === null ? null : decodeURIComponent(results[1]);
        }
    }

    /**
     * Validates that a redirect target is either same-origin or the known
     * localhost dev port (9004). Blocks open-redirect and XSS-via-location attacks.
     */
    function isTrustedRedirectUrl(url) {
        try {
            var parsed = new URL(url);
            if (parsed.origin === window.location.origin) { return true; }
            if (parsed.origin === "http://localhost:9004")  { return true; }
            return false;
        } catch (e) {
            return false;
        }
    }

    /**
     * Redirects only after validating the target URL.
     * Falls back to the generic error page on failure.
     */
    function safeRedirect(url) {
        if (isTrustedRedirectUrl(url)) {
            window.location.href = url;
        } else {
            console.error("Blocked untrusted redirect to:", url);
            window.location.href = baseUrl + "adminConsoleLoginError.html?error=unknown";
        }
    }

    // -------------------------------------------------------------------------
    // XHR helpers
    // -------------------------------------------------------------------------

    function loadAdminConsoleUrl(callback) {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState !== XMLHttpRequest.DONE) { return; }
            if (xmlhttp.status === 200) {
                if (callback) { callback(xmlhttp.responseText); }
            } else if (xmlhttp.status === 401 || xmlhttp.status === 403) {
                safeRedirect(baseUrl + "adminConsoleLoginError.html?error=unauthorized");
            } else {
                safeRedirect(baseUrl + "adminConsoleLoginError.html?error=unknown");
            }
        };
        xmlhttp.open("GET", baseUrl + "rest/adminConsoleUrl", true);
        xmlhttp.withCredentials = true;
        xmlhttp.send();
    }

    function getAdminLoginToken(callback) {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState !== XMLHttpRequest.DONE) { return; }
            if (xmlhttp.status === 200) {
                if (callback) { callback(xmlhttp.responseText); }
            } else if (xmlhttp.status === 401 || xmlhttp.status === 403) {
                safeRedirect(baseUrl + "adminConsoleLoginError.html?error=unauthorized");
            } else {
                safeRedirect(baseUrl + "adminConsoleLoginError.html?error=unknown");
            }
        };
        xmlhttp.open("GET", baseUrl + "rest/adminConsoleToken", true);
        xmlhttp.withCredentials = true;
        xmlhttp.send();
    }

    // -------------------------------------------------------------------------
    // Base URL — abort the whole script if origin looks wrong
    // -------------------------------------------------------------------------

    var baseUrl;
    try {
        baseUrl = getBaseUrl();
    } catch (e) {
        console.error("Blocked page init: unexpected origin.", e);
        document.body.textContent = "Security error: unexpected page origin.";
        return;   // Halt IIFE execution; no globals will be set.
    }

    // -------------------------------------------------------------------------
    // Webswing instance config — assigned to window so suramadu-embed.js
    // can find it via the div's data-webswing-instance="webswingInstance0"
    // -------------------------------------------------------------------------

    window.webswingInstance0 = {
        options: {
            autoStart:         false,
            connectionUrl:     baseUrl,
            securityToken:     getParam("securityToken"),
            realm:             getParam("realm"),
            adminConsoleLogin: true,
            onReady: function (api) {
                getAdminLoginToken(function (data) {
                    if (!data) {
                        console.error("Failed to get accessId!");
                        return;
                    }

                    var json;
                    try {
                        json = JSON.parse(data);
                    } catch (e) {
                        console.error("Invalid JSON from adminConsoleToken.", e);
                        safeRedirect(baseUrl + "adminConsoleLoginError.html?error=unknown");
                        return;
                    }

                    if (!json || !json.accessId) {
                        console.error("Missing accessId in token response.");
                        safeRedirect(baseUrl + "adminConsoleLoginError.html?error=unknown");
                        return;
                    }

                    // Encode accessId before placing it in a URL to prevent
                    // fragment/query injection.
                    var encodedAccessId = encodeURIComponent(json.accessId);

                    loadAdminConsoleUrl(function (adminConsoleUrl) {
                        if (!document.referrer || document.referrer.length === 0) {
                            safeRedirect(baseUrl + "adminConsoleLoginError.html?error=unknown");
                        } else if (!adminConsoleUrl || adminConsoleUrl.length === 0) {
                            safeRedirect(baseUrl + "adminConsoleLoginError.html?error=not_found");
                        } else if (document.referrer.indexOf("http://localhost:9004") === 0) {
                            safeRedirect("http://localhost:9004/admin/login.html?accessId=" + encodedAccessId);
                        } else {
                            if (!isTrustedRedirectUrl(adminConsoleUrl + "login.html")) {
                                console.error("Blocked redirect to untrusted adminConsoleUrl:", adminConsoleUrl);
                                safeRedirect(baseUrl + "adminConsoleLoginError.html?error=unknown");
                                return;
                            }
                            safeRedirect(adminConsoleUrl + "login.html?accessId=" + encodedAccessId);
                        }
                    });
                });
            }
        }
    };

    // -------------------------------------------------------------------------
    // suramadu-embed.js loader
    // -------------------------------------------------------------------------

    var loader = function () {
        // IE11 compat: synthesise origin if absent.
        if (!window.location.origin) {
            window.location.origin =
                window.location.protocol + "//" + window.location.hostname +
                (window.location.port ? ":" + window.location.port : "");
        }

        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState !== XMLHttpRequest.DONE) { return; }

            // Allowlist-validate version before embedding in a script src URL.
            var rawVersion = xmlhttp.status === 200 ? xmlhttp.responseText.trim() : "";
            var version    = /^[\w.\-]{1,64}$/.test(rawVersion) ? rawVersion : "undefined";

            var script = document.createElement("script");
            script.src = baseUrl + "javascript/suramadu-embed.js?version=" +
                         encodeURIComponent(version);

            var tag = document.getElementsByTagName("script")[0];
            tag.parentNode.insertBefore(script, tag);
        };

        xmlhttp.open("GET", baseUrl + "rest/version", true);
        xmlhttp.send();
    };

    if (window.addEventListener) {
        window.addEventListener("load", loader, false);
    } else {
        window.attachEvent("onload", loader);
    }

})(window, document);
