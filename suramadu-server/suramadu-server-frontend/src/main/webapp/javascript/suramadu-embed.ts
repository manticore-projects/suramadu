import Webswing from "./webswing"
import { loadTranslations } from "./suramadu-translate";
import "../css/scss/style.scss";

loadTranslations().then(
    i18n => {
        const webswing = Webswing(i18n);
        const globalName = $('[data-webswing-global-var]');

        if (globalName != null && globalName.length !== 0) {
            const name: string = globalName.data('webswingGlobalVar');
            (window as any)[name] = webswing;
            webswing.scan(window);
        } else {
            webswing.scan(window);
        }
    });