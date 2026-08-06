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


export class ClipboardModule extends ModuleDef<typeof clipboardInjectable, IClipboardService> {
    public html?: string
    public pasteHtml?: string;
    public copyBar?: JQuery<HTMLElement> & { wsEventData?: appFrameProtoOut.ICopyEventMsgOutProto, minimized?: boolean };
    public pasteDialog?: JQuery<HTMLElement>;

    public ready = () => {
        this.html = this.api.translate(htmlTemplate);
        this.pasteHtml = this.api.translate(pasteHtmlTemplate);
    };

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
            if (this.api.cfg.ieVersion && this.api.cfg.ieVersion <= 11) {
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

    public displayCopyBar(data: appFrameProtoOut.ICopyEventMsgOutProto) { // trigered by swing app
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
        if (this.copyBar != null) {
            this.copyBar.hide("fast");
            this.copyBar.remove();
            this.copyBar = undefined;
        }
        this.api.focusDefault();
    }

    public displayPasteDialog(requestCtx: appFrameProtoOut.IPasteRequestMsgOutProto) {
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