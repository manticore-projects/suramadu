/**
 * suramadu-init.js
 *
 * Extracted from index.html so that the page can run under a strict
 * Content-Security-Policy (script-src 'self') without needing 'unsafe-inline'.
 *
 * Loaded via:
 *   <script data-webswing-global-var="webswing"
 *           src="javascript/suramadu-init.js"></script>
 *
 * The data-webswing-global-var attribute on the <script> tag tells
 * suramadu-embed.js to expose its API as window.webswing.
 */
(function (window, document) {

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Returns a validated same-origin base URL derived from the current page
     * location. Only the parsed origin and pathname are used — never raw href.
     * Throws if the current origin scheme is not http or https.
     */
    function getSameOriginBaseUrl() {
        var origin   = window.location.origin;
        var pathname = window.location.pathname;

        if (!/^https?:\/\//.test(origin)) {
            throw new Error("Unexpected origin scheme.");
        }

        var base = origin + pathname;
        // Ensure trailing slash so relative paths resolve correctly.
        if (base.charAt(base.length - 1) !== "/") {
            base = base + "/";
        }
        return base;
    }

    /**
     * Reads a single named query-string parameter from the current URL.
     * Returns null if the parameter is absent.
     * NOTE: values are only passed to the Webswing JS API (not injected into
     * the DOM), so URL-decoding is sufficient here.  If you ever place these
     * values into innerHTML / script / href, apply further context-specific
     * encoding at that point.
     */
    function getParam(name) {
        // Use URLSearchParams when available (all modern browsers).
        try {
            return new URLSearchParams(window.location.search).get(name);
        } catch (e) {
            // IE11 fallback — keep the original regex but avoid eval paths.
            // Escape every RegExp metacharacter (including backslash) so the name is matched
            // literally; escaping only the brackets leaves a backslash injection gap. (CWE-116)
            var safeName = name.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
            var results  = new RegExp("[?&]" + safeName + "=([^&#]*)").exec(window.location.search);
            return results === null ? null : decodeURIComponent(results[1]);
        }
    }

    // -------------------------------------------------------------------------
    // Base URL — abort the whole script if origin looks wrong
    // -------------------------------------------------------------------------

    var baseUrl;
    try {
        baseUrl = getSameOriginBaseUrl();
    } catch (e) {
        console.error("Blocked script load: unexpected origin.", e);
        return;   // Halt IIFE execution; no globals will be set.
    }

    // -------------------------------------------------------------------------
    // Webswing instance config — assigned to window so suramadu-embed.js
    // can find it via the div's data-webswing-instance="webswingInstance0"
    // -------------------------------------------------------------------------

    window.webswingInstance0 = {
        options: {
            autoStart:         true,
            args:              getParam("args"),
            recording:         getParam("recording"),
            debugPort:         getParam("debugPort"),
            recordingPlayback: getParam("recordingPlayback"),
            securityToken:     getParam("securityToken"),
            realm:             getParam("realm"),
            debugLog:          getParam("debugLog")
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

            // Allowlist-validate the version string before embedding it in a
            // script src URL.
            var rawVersion = xmlhttp.status === 200 ? xmlhttp.responseText.trim() : "";
            var version    = /^[\w.\-]{1,64}$/.test(rawVersion) ? rawVersion : "undefined";

            var script = document.createElement("script");
            script.src = baseUrl + "javascript/suramadu-embed.js?version=" +
                         encodeURIComponent(version);

            // suramadu-embed.js scans document.querySelectorAll('script[data-webswing-global-var]')
            // to find its config. The attribute already exists on the suramadu-init.js
            // <script> tag in the DOM — do NOT copy it onto this injected element or
            // there will be two matching elements, causing double initialisation and
            // broken keyboard event listeners.
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
