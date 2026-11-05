import htmlTemplate from './templates/clipboard.html'
import pasteHtmlTemplate from './templates/paste.html'
import { ModuleDef } from './suramadu-inject';
import { detectChrome, getImageString, isIOS } from './suramadu-util';
import { appFrameProtoOut } from './proto/proto.out';
import { appFrameProtoIn } from './proto/proto.in';
export const clipboardInjectable = {
    cfg: 'webswing.config' as const,
    send: 'socket.send' as const,
    focusDefault: 'focusManager.focusDefault' as const,
    translate: 'translate.translate' as const,
    getFocusedWindow: 'base.getFocusedWindow' as const
}

export interface IClipboardService {
    'clipboard.cut': (e: ClipboardEvent) => void;
    'clipboard.copy': (event: ClipboardEvent, cut?: boolean) => void;
    'clipboard.paste': (event: ClipboardEvent, special: boolean) => void;
    'clipboard.displayCopyBar': (data: appFrameProtoOut.ICopyEventMsgOutProto) => void;
    'clipboard.displayPasteDialog': (requestCtx: appFrameProtoOut.IPasteRequestMsgOutProto) => void;
    'clipboard.dispose': () => void;
}

const copyEventType = appFrameProtoIn.CopyEventMsgInProto.CopyEventMsgTypeProto
const ieWindow = window as typeof window & { clipboardData: any }

// how long we wait for the server's CopyEventMsgOut before aborting a direct clipboard write
const DIRECT_COPY_TIMEOUT_MS = 10000;

export class ClipboardModule extends ModuleDef<typeof clipboardInjectable, IClipboardService> {
    public html?: string
    public pasteHtml?: string;
    public copyBar?: JQuery<HTMLElement> & { wsEventData?: appFrameProtoOut.ICopyEventMsgOutProto, minimized?: boolean };
    public pasteDialog?: JQuery<HTMLElement>;
    // pending direct (async Clipboard API) write started inside the Ctrl+C user gesture,
    // resolved when the server's clipboard content arrives
    private directCopy?: { resolve: (text: string, html: string) => void; cancel: () => void; };
    // last successfully written direct-copy content, to drop duplicate CopyEventMsgOut
    // (one Swing copy action may call Clipboard.setContents() more than once)
    private lastDirectCopy?: { text: string, time: number };

    public ready = () => {
        this.html = this.api.translate(htmlTemplate);
        this.pasteHtml = this.api.translate(pasteHtmlTemplate);
        this.prewarmPastePermission();
    };

    // Trigger the browser's one-time clipboard-read permission prompt on the first user
    // click instead of stalling the first in-app paste action behind it. Only on browsers
    // whose Permissions API knows 'clipboard-read' (Chromium); Firefox/Safari prompt
    // per-read natively and would show a confusing unprompted paste popup here.
    private prewarmPastePermission() {
        const permissions = (navigator as any).permissions;
        if (permissions == null || typeof permissions.query !== 'function') {
            return;
        }
        const handler = () => {
            document.removeEventListener('mousedown', handler, true);
            if (!this.directPasteSupported() || !this.api.cfg.hasControl) {
                return;
            }
            permissions.query({ name: 'clipboard-read' }).then((status: any) => {
                if (status.state === 'prompt') {
                    (navigator.clipboard as any).readText().catch(() => undefined);
                }
            }).catch(() => undefined);
        };
        document.addEventListener('mousedown', handler, true);
    }

    public provides() {
        return {
            'clipboard.cut': this.cut,
            'clipboard.copy': this.copy,
            'clipboard.paste': this.paste,
            'clipboard.displayCopyBar': this.displayCopyBar,
            'clipboard.displayPasteDialog': this.displayPasteDialog,
            'clipboard.dispose': this.close
        }
    }

    public cut(event: ClipboardEvent) {
        this.copy(event, true);
    }

    public copy(event: ClipboardEvent, cut?: boolean) {

        if (this.copyBar == null) {
            if (this.directCopySupported()) {
                // start the async clipboard write NOW, while the Ctrl+C/CMD+C user activation
                // is still alive; the ClipboardItem promises resolve once the server responds
                this.startDirectCopy();
            } else if (this.api.cfg.ieVersion && this.api.cfg.ieVersion <= 11) {
                ieWindow.clipboardData.setData('Text', '');
            } else {
                event.clipboardData?.setData('text/plain', '');
            }
            this.api.send({
                copy: {
                    type: cut === true ? copyEventType.cut : copyEventType.copy
                }
            });
        } else {
            const data = this.copyBar.wsEventData;
            if (this.api.cfg.ieVersion && this.api.cfg.ieVersion <= 11) {
                // handling of copy events only for IE
                const clipboardData = ieWindow.clipboardData;
                if (data?.text != null) {
                    event.preventDefault();
                    clipboardData.setData('Text', data.text);
                }
                this.close();
            } else {
                // handling of copy events for rest of browsers
                event = (event as any).originalEvent || event;
                event.preventDefault();
                if (data?.text != null) {
                    event.clipboardData?.setData('text/plain', data.text);
                }
                if (data?.html != null && !this.api.cfg.ieVersion) {
                    event.clipboardData?.setData('text/html', data.html);
                }
                if (data?.img != null && detectChrome()) {
                    try {
                        const img = getImageString(data.img);
                        fetch(img)
                            .then(response => response.blob())
                            .then(blob => (navigator.clipboard as any).write([new ClipboardItem({ 'image/png': blob })]));
                    } catch (e) {
                        console.error('Failed copying image to clipboard: ' + e);
                    }
                }
                this.close();
            }
        }
    }

    public paste(event: ClipboardEvent, special: boolean) {
        if (this.api.cfg.hasControl) {
            let text: string | undefined = '';
            let html: string | undefined = '';
            if (this.api.cfg.ieVersion && this.api.cfg.ieVersion <= 11) {
                text = ieWindow.clipboardData.getData('Text');
                html = text;
            } else {
                const data = event.clipboardData;
                text = data?.getData('text/plain');
                html = data?.getData('text/html');
                if (data?.items != null) {
                    // tslint:disable-next-line: prefer-for-of
                    for (let i = 0; i < data.items.length; i++) {
                        if (data.items[i].type.indexOf('image') === 0) {
                            const img = data.items[i];
                            const reader = new FileReader();
                            reader.onload = (readerEvt) => {
                                this.sendPasteEvent(special, text, html, readerEvt.target?.result as string | null);
                            };
                            const imgAsFile = img.getAsFile();
                            if (imgAsFile) {
                                reader.readAsDataURL(imgAsFile);
                            }
                            return;
                        }
                    }
                }
            }
            this.sendPasteEvent(special, text, html);
        }
    }

    public sendPasteEvent(special: boolean, text?: string, html?: string, img?: string | null) {
        const pasteObj = {
            special: !!special,
            text: text != null && text.length !== 0 ? text : undefined,
            html: html != null && html.length !== 0 ? html : undefined,
            img: img != null ? img : undefined
        };
        this.api.send({
            paste: pasteObj
        });
    }

    /* ---------- direct clipboard (async Clipboard API) ---------- */

    private directClipboardEnabled(): boolean {
        return this.api.cfg.directClipboard !== false && window.isSecureContext === true
            && !this.api.cfg.ieVersion && navigator.clipboard != null;
    }

    private directCopySupported(): boolean {
        return this.directClipboardEnabled() && typeof ClipboardItem !== 'undefined'
            && typeof (navigator.clipboard as any).write === 'function';
    }

    private directPasteSupported(): boolean {
        const clip = navigator.clipboard as any;
        return this.directClipboardEnabled()
            && (typeof clip?.read === 'function' || typeof clip?.readText === 'function');
    }

    private startDirectCopy() {
        this.cancelDirectCopy(); // abort a stale pending write (e.g. rapid repeated Ctrl+C)

        let resolveText!: (b: Blob) => void;
        let rejectText!: (r?: any) => void;
        let resolveHtml!: (b: Blob) => void;
        let rejectHtml!: (r?: any) => void;
        const textPromise = new Promise<Blob>((res, rej) => { resolveText = res; rejectText = rej; });
        const htmlPromise = new Promise<Blob>((res, rej) => { resolveHtml = res; rejectHtml = rej; });
        // suppress unhandled-rejection noise when the write is aborted
        textPromise.catch(() => undefined);
        htmlPromise.catch(() => undefined);

        const timeout = window.setTimeout(() => this.cancelDirectCopy(), DIRECT_COPY_TIMEOUT_MS);

        this.directCopy = {
            resolve: (text: string, html: string) => {
                clearTimeout(timeout);
                resolveText(new Blob([text], { type: 'text/plain' }));
                resolveHtml(new Blob([html], { type: 'text/html' }));
            },
            cancel: () => {
                clearTimeout(timeout);
                rejectText(new Error('Suramadu: direct copy aborted'));
                rejectHtml(new Error('Suramadu: direct copy aborted'));
            }
        };

        // must be called synchronously within the user gesture
        navigator.clipboard.write([new ClipboardItem({
            'text/plain': textPromise,
            'text/html': htmlPromise
        })]).catch((e) => {
            // aborted or not permitted - clipboard stays unchanged
            console.debug('Suramadu: direct clipboard write did not complete.', e);
        });
    }

    private cancelDirectCopy() {
        if (this.directCopy != null) {
            this.directCopy.cancel();
            this.directCopy = undefined;
        }
    }

    private htmlToText(html: string): string {
        try {
            return new DOMParser().parseFromString(html, 'text/html').body.textContent || '';
        } catch (e) {
            return '';
        }
    }

    private textToHtml(text: string): string {
        const escaped = text.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
        return '<pre>' + escaped + '</pre>';
    }

    /* ------------------------------------------------------------ */

    public displayCopyBar(data: appFrameProtoOut.ICopyEventMsgOutProto) { // trigered by swing app
        const hasText = data.text != null && data.text.length !== 0;
        const hasHtml = data.html != null && data.html.length !== 0;

        if (this.directCopy != null) {
            if (hasText || hasHtml) {
                // complete the pending direct write - no copy bar, no second Ctrl+C
                const text = hasText ? data.text! : this.htmlToText(data.html!);
                const html = hasHtml ? data.html! : this.textToHtml(data.text!);
                this.directCopy.resolve(text, html);
                this.directCopy = undefined;
                this.lastDirectCopy = { text, time: Date.now() };
                return;
            }
            // image- or file-only content: abort the text write and fall back to the copy bar
            this.cancelDirectCopy();
        } else if (hasText || hasHtml) {
            // unsolicited copy event: an in-app copy action (menu/button), or a second
            // Clipboard.setContents() within one copy action
            const text = hasText ? data.text! : this.htmlToText(data.html!);
            const html = hasHtml ? data.html! : this.textToHtml(data.text!);
            if (this.lastDirectCopy != null && this.lastDirectCopy.text === text
                && Date.now() - this.lastDirectCopy.time < 2000) {
                // duplicate of content just written directly - drop silently
                return;
            }
            if (this.directCopySupported()) {
                // try a silent direct write (Chromium grants clipboard-write to the focused
                // tab without a gesture; elsewhere the triggering click may still be alive);
                // fall back to the copy bar if not permitted
                navigator.clipboard.write([new ClipboardItem({
                    'text/plain': new Blob([text], { type: 'text/plain' }),
                    'text/html': new Blob([html], { type: 'text/html' })
                })]).then(() => {
                    this.lastDirectCopy = { text, time: Date.now() };
                }).catch(() => {
                    this.showCopyBar(data);
                });
                return;
            }
        }

        this.showCopyBar(data);
    }

    private showCopyBar(data: appFrameProtoOut.ICopyEventMsgOutProto) {
        if (this.copyBar != null) {
            this.close();
        }

        const doc = this.api.getFocusedWindow().document;
        const rootElement = ($(doc).find(".suramadu-element-content") as unknown) as JQuery<HTMLElement>;

        rootElement.append(this.html!);
        this.copyBar = rootElement.find('div[data-id="copyBar"]')!;

        this.copyBar.wsEventData = data;
        const closeBtn = this.copyBar.find('button[data-id="closeBtn"]');
        closeBtn.click(() => {
            this.close();
        });
        this.copyBar?.show("fast");

        const showTab = (tab: JQuery<HTMLElement>, type: 'more' | 'text') => {
            this.copyBar?.find('.ws-btn--selected').removeClass('ws-btn--selected');
            this.copyBar?.find('.ws-clipboard-item--active').removeClass('ws-clipboard-item--active');
            $(tab).addClass('ws-btn--selected');
            this.copyBar?.find('div[data-id="' + type + '"]').addClass('ws-clipboard-item--active');
        }

        /* TEXT TAB */
        let copyBtn = this.copyBar.find('button[data-id="text"]');
        if (isIOS()) {
            copyBtn.text("Copy to clipboard");
        } else if (this.api.cfg.isMac) {
            const macCopyMsg = $('<p>Copy to clipboard with CMD+C</p>');
            copyBtn.after(macCopyMsg);
            copyBtn.remove();
            copyBtn = macCopyMsg;
        }
        if ((data.text != null && data.text.length !== 0) || (data.html != null && data.html.length !== 0)) {
            const textarea = this.copyBar.find('div[data-id="textarea"]');
            if (data.text != null && data.text.length !== 0) {
                textarea.append($('<pre class="ws-clipboard-text-pre"></pre>').text(data.text));
            } else {
                textarea.html('<iframe class="ws-clipboard-text-iframe" src="data:text/html;charset=utf-8,' + encodeURIComponent(data.html ? data.html : "") + '"></iframe>');
            }
            copyBtn.on('mouseenter', () => {
                showTab(copyBtn, 'text');
                this.maximize();
            });
        }
        copyBtn.on('click', () => {
            if (isIOS()) {
                const textarea = doc.createElement('textarea');
                try {
                    textarea.setAttribute('readonly', 'false');
                    textarea.setAttribute('contenteditable', 'true');
                    textarea.style.position = 'fixed'; // prevent scroll from jumping to the bottom when focus is set.
                    textarea.value = " ";

                    doc.body.appendChild(textarea);

                    textarea.focus();
                    textarea.select();

                    const range = doc.createRange();
                    range.selectNodeContents(textarea);

                    const sel = window.getSelection();
                    sel?.removeAllRanges();
                    sel?.addRange(range);

                    textarea.setSelectionRange(0, textarea.value.length);
                    doc.execCommand('copy');
                } finally {
                    doc.body.removeChild(textarea);
                }
            } else {
                doc.execCommand("copy");
            }
        });

        /* More TAB */
        if ((data.files != null && data.files.length !== 0) || data.img != null) {
            if (data.files != null && data.files.length !== 0) {
                const fileListElement = this.copyBar.find('#wsFileList');
                for (const fileName of data.files) {
                    let link = $('<a>');
                    if (fileName.indexOf("#") === 0) {
                        link = $('<span>');
                        link.html(fileName.substring(1));
                    } else {
                        link.html(fileName);
                        link.on('click', (event) => {
                            this.api.send({
                                copy: {
                                    type: copyEventType.getFileFromClipboard,
                                    file: $(event.currentTarget).html()
                                }
                            });
                        });
                    }
                    fileListElement.append(link);
                    fileListElement.append("<br/>");
                }
            } else {
                this.copyBar.find('div[data-id="files"]').remove();
            }
            if (data.img != null) {
                const clipImgDataUrl = getImageString(data.img);
                this.copyBar.find('div[data-id="image"]').append('<a target="_blank" download="clipboard.png" href="' + clipImgDataUrl + '"><img src="' + clipImgDataUrl + '" id="wsCopyImage" class="ws-clipboard-img-thumb"></a>');
            } else {
                this.copyBar.find('div[data-id="image"]').remove();
            }

            const moreBtn = this.copyBar.find('button[data-id="more"]');
            moreBtn.on('mouseenter', () => {
                showTab(moreBtn, 'more');
                this.maximize();
            });
        } else {
            const moreBtn = this.copyBar.find('button[data-id="more"]');
            moreBtn.remove();
        }

        this.copyBar.find('div[data-id="contentBar"]').hide();

        if ((data.text != null && data.text.length !== 0) || (data.html != null && data.html.length !== 0) || (detectChrome() && data.img != null)) {
            this.copyBar.on('mouseleave', () => this.minimize());
        } else if ((data.files != null && data.files.length !== 0) || data.img != null) {
            const moreBtn = this.copyBar.find('button[data-id="more"]');
            showTab(moreBtn, 'more');
            copyBtn.remove();
            moreBtn.remove();
            this.maximize();
        } else {
            this.close();
        }

    }

    public minimize() {
        if (this.copyBar != null) {
            this.copyBar.find('.ws-btn--selected').removeClass('ws-btn--selected');
            this.copyBar.find('div[data-id="contentBar"]').slideUp('fast');
            this.copyBar.minimized = true;
        }
        this.api.focusDefault();
    }

    public maximize() {
        if (this.copyBar != null) {
            this.copyBar.find('div[data-id="contentBar"]').slideDown('fast');
            this.copyBar.minimized = false;
        }
    }

    public close() {
        this.cancelDirectCopy();
        if (this.copyBar != null) {
            this.copyBar.hide("fast");
            this.copyBar.remove();
            this.copyBar = undefined;
        }
        this.api.focusDefault();
    }

    public displayPasteDialog(requestCtx: appFrameProtoOut.IPasteRequestMsgOutProto) {
        if (this.directPasteSupported()) {
            // try to read the local clipboard directly (browser may show a native permission
            // prompt or paste confirmation); fall back to the legacy dialog if not allowed
            this.readLocalClipboard().then((content) => {
                this.sendPasteEvent(false, content.text, content.html, content.img);
            }).catch(() => {
                this.showPasteDialog(requestCtx);
            });
            return;
        }
        this.showPasteDialog(requestCtx);
    }

    private async readLocalClipboard(): Promise<{ text?: string, html?: string, img?: string }> {
        const clip = navigator.clipboard as any;
        if (typeof clip.read === 'function') {
            try {
                const items: any[] = await clip.read();
                let text: string | undefined;
                let html: string | undefined;
                let img: string | undefined;
                for (const item of items) {
                    for (const type of item.types) {
                        if (type === 'text/plain') {
                            text = await (await item.getType(type)).text();
                        } else if (type === 'text/html') {
                            html = await (await item.getType(type)).text();
                        } else if (type.indexOf('image/') === 0 && img == null) {
                            img = await blobToDataURL(await item.getType(type));
                        }
                    }
                }
                return { text, html, img };
            } catch (e) {
                // permission denied or read failed - try plain text below
            }
        }
        if (typeof clip.readText === 'function') {
            const text = await clip.readText();
            return { text };
        }
        throw new Error('Clipboard read not supported');
    }

    private showPasteDialog(requestCtx: appFrameProtoOut.IPasteRequestMsgOutProto) {
        if (this.pasteDialog != null) {
            this.closePasteDialog();
        }

        const rootElement = ($(this.api.getFocusedWindow().document).find(".suramadu-element-content") as unknown) as JQuery<HTMLElement>;

        rootElement.append(this.pasteHtml!);
        this.pasteDialog = rootElement.find('div[data-id="pasteDialog"]');

        const title = this.pasteDialog.find('div[data-id="title"]');
        title.html(this.api.translate(requestCtx.title ? requestCtx.title : ""));

        const message = this.pasteDialog.find('div[data-id="message"]');
        message.html(this.api.translate(requestCtx.message ? requestCtx.message : ""));

        const textarea = this.pasteDialog.find('textarea[data-id="textarea"]');
        textarea.focus();
        textarea.on('paste', (event) => {
            event.preventDefault();
            event.stopPropagation();
            this.paste(event.originalEvent as ClipboardEvent, false);
            this.closePasteDialog();
            return false;
        });

        const closeBtn = this.pasteDialog.find('button[data-id="closeBtn"]');
        closeBtn.on('click', () => {
            this.api.send({
                paste: {}
            });
            this.closePasteDialog();
        });
    }

    public closePasteDialog() {
        if (this.pasteDialog != null) {
            this.pasteDialog.hide("fast");
            this.pasteDialog.remove();
            this.pasteDialog = undefined;
        }
        this.api.focusDefault();
    }

}

function blobToDataURL(blob: Blob): Promise<string> {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result as string);
        reader.onerror = () => reject(reader.error);
        reader.readAsDataURL(blob);
    });
}