import htmlTemplate from './templates/upload.html'
import 'blueimp-file-upload'
import { ModuleDef } from './suramadu-inject'
import { isIOS } from "./suramadu-util";
import { appFrameProtoOut } from './proto/proto.out';
import { commonProto } from "./proto/proto.common";

export const filesInjectable = {
    cfg: 'webswing.config' as const,
    instanceId: 'socket.instanceId' as const,
    send: 'socket.send' as const,
    sendSimpleEvent: 'socket.sendSimpleEvent' as const,
    translate: 'translate.translate' as const,
    getFocusedWindow: 'base.getFocusedWindow' as const,
    focusDefault: 'focusManager.focusDefault' as const
}

export interface IFilesService {
    'files.process': (event: appFrameProtoOut.IFileDialogEventMsgOutProto) => void,
    'files.close': () => void,
    'files.download': (url: string) => void,
    'files.link': (url: string) => void,
    'files.print': (url: string) => void,
    'files.redirect': (url: string) => void
}

const FileEventType = appFrameProtoOut.FileDialogEventMsgOutProto.FileDialogEventTypeProto;
const SimpleEventType = commonProto.SimpleEventMsgInProto.SimpleEventTypeProto
interface IActiveFileDialogHolder {
    win: Window;
    autoUpload: (data: appFrameProtoOut.IFileDialogEventMsgOutProto, instanceId?: string) => void;
    autoSave: (data: appFrameProtoOut.IFileDialogEventMsgOutProto, instanceId?: string) => void;
    open: (data: appFrameProtoOut.IFileDialogEventMsgOutProto, instanceId?: string) => void;
    close: () => void;
    download: (url: string) => void, link: (url: string) => void, print: (url: string) => void
}
interface IDownloadDialogData {
    title: string;
    message: string;
    buttonMessage: string;
}

export class FilesModule extends ModuleDef<typeof filesInjectable, IFilesService> {
    private html?: string;
    private holderMap: { [K: string]: IActiveFileDialogHolder } = {};


    public ready = () => {
        this.html = this.api.translate(htmlTemplate);
    };

    public provides() {
        return {
            'files.process': this.process,
            'files.close': this.close,
            'files.download': this.download,
            'files.link': this.link,
            'files.print': this.print,
            'files.redirect': this.redirect
        }
    }

    private process(event: appFrameProtoOut.IFileDialogEventMsgOutProto) {
        if (event.eventType === FileEventType.AutoUpload) {
            this.autoUpload(event, this.api.instanceId());
        } else if (event.eventType === FileEventType.AutoSave) {
            this.autoSave(event, this.api.instanceId());
        } else if (event.eventType === FileEventType.Open) {
            this.open(event, this.api.instanceId());
        } else if (event.eventType === FileEventType.Close) {
            this.close();
        }
    }

    private getHolder() {
        const win = this.api.getFocusedWindow();

        // try cleanup
        for (const winName in this.holderMap) {
            if (!this.holderMap[winName].win || this.holderMap[winName].win.closed) {
                delete this.holderMap[winName];
            }
        }

        if (!this.holderMap[win.name]) {
            this.setup(win);
        }

        return this.holderMap[win.name];
    }

    private autoUpload(data: appFrameProtoOut.IFileDialogEventMsgOutProto, instanceId?: string) {
        this.getHolder().autoUpload(data, instanceId);
    }

    private autoSave(data: appFrameProtoOut.IFileDialogEventMsgOutProto, instanceId?: string) {
        this.getHolder().autoSave(data, instanceId);
    }

    private open(data: appFrameProtoOut.IFileDialogEventMsgOutProto, instanceId?: string) {
        this.getHolder().open(data, instanceId);
    }

    private close() {
        this.getHolder().close();
    }

    private download(url: string) {
        this.getHolder().download(url);
    }

    private link(url: string) {
        this.getHolder().link(url);
    }

    private print(url: string) {
        this.getHolder().print(url);
    }

    private redirect(url: string) {
        window.location.href = url;
    }

    private setup(win: Window) {
        const doc = $(win.document);
        const rootElement = (doc.find(".suramadu-element-content") as unknown) as JQuery<HTMLElement>;
        rootElement.append(this.html!);

        let jqXHRFileupload: any[] = [];
        let uploadingFiles: any[] = [];
        let uploadedFiles: any[] = [];
        let timeout: number | undefined;
        let errorTimeout: number | undefined;
        let closeAfterErrorTimeout: boolean;

        const uploadBar = rootElement.find('div[data-id="uploadBar"]');
        const fileDialogTransferBarClientId = uploadBar.find('input[data-id="fileDialogTransferBarClientId"]');

        const fileActionButtonGroup = uploadBar.find('div[data-id="fileActionButtonGroup"]');
        const deleteSelectedButton = uploadBar.find('button[data-id="deleteSelectedButton"]');
        const downloadSelectedButton = uploadBar.find('button[data-id="downloadSelectedButton"]');
        const uploadBtn = uploadBar.find('div[data-id="fileUploadBtn"]');

        const dropZone = uploadBar.find('div[data-id="fileDropArea"]');
        const fileUpload = uploadBar.find('form[data-id="fileupload"]');
        const cancelBtn = uploadBar.find('button[data-id="cancelBtn"]');
        const getFileInput = () => uploadBar.find('input[data-id="fileInput"]');

        const uploadProgressBar = rootElement.find('div[data-id="fileDialogTransferProgressBar"]');
        const uploadProgress = rootElement.find('div[data-id="progress"] .ws-progress-bar');
        const fileDialogErrorMessage = rootElement.find('div[data-id="fileDialogErrorMessage"]');
        const fileDialogErrorMessageContent = rootElement.find('div[data-id="fileDialogErrorMessageContent"]');
        const fileDialogSuccessMessage = rootElement.find('div[data-id="fileDialogSuccessMessage"]');
        const fileDialogSuccessMessageContent = rootElement.find('div[data-id="fileDialogSuccessMessageContent"]');

        const autoUploadBar = rootElement.find('div[data-id="autoUploadBar"]');
        const autoFileupload = autoUploadBar.find('form[data-id="autoFileupload"]')
        const uploadAutoUploadButton = autoUploadBar.find('button[data-id="uploadAutoUploadButton"]');
        const cancelAutoUploadButton = autoUploadBar.find('button[data-id="cancelAutoUploadButton"]');
        const getAutoFileInput = () => autoUploadBar.find('input[data-id="autoFileInput"]');
        const autoUploadfileDialogTransferBarClientId = autoUploadBar.find('input[data-id="autoUploadfileDialogTransferBarClientId"]');

        const autoSaveBar = rootElement.find('div[data-id="autoSaveBar"]');
        const autoSaveInput = autoSaveBar.find('input[data-id="autoSaveInput"]');
        const autoSaveButton = autoSaveBar.find('button[data-id="autoSaveButton"]');
        const cancelAutoSaveButton = autoSaveBar.find('button[data-id="cancelAutoSaveButton"]');

        const linkDialog = rootElement.find('div[data-id="downloadLinkDialog"]');

        // hide all
        uploadProgressBar.hide();
        autoUploadBar.hide();
        autoUploadBar.detach();
        autoSaveBar.hide();
        autoSaveBar.detach();
        uploadBar.hide();
        uploadBar.detach();
        linkDialog.hide();
        linkDialog.detach();

        const fileuploadadd = (_: JQueryEventObject, data: any, fileuploadelement: any) => {
        	fileDialogSuccessMessageContent.html("");
        	fileDialogSuccessMessage.hide("fast");
        	
            fileuploadelement.url = this.getFileUrl("file");
            data.url = this.getFileUrl("file");
            data.files.forEach((file: any) => {
                uploadingFiles.push(file.name);
            });
            jqXHRFileupload.push(data);
            setProgressBarVisible(true);
        }

        const fileuploadfail = (_: any, data: any) => {
            setProgressBarVisible(false);
            if (data.jqXHR.statusText !== 'abort') {
            	fileDialogErrorMessageContent.append('<p>' + data.jqXHR.responseText + '</p>');
                if (!errorTimeout) {
                    this.animateShow(fileDialogErrorMessage);
                } else {
                    clearTimeout(errorTimeout);
                }
                data.files.forEach((file: any) => {
                    const index = uploadingFiles.indexOf(file.name);
                    uploadingFiles.splice(index, 1);
                });
                checkUploadingFinished();
                errorTimeout = setTimeout(() => {
                    errorTimeout = undefined;
                    fileDialogErrorMessageContent.html("");
                    fileDialogErrorMessage.hide("fast");
                    if (closeAfterErrorTimeout) {
                        close();
                    }
                }, 3000);
            }
        }

        // Changes the looks of the progress bar based on the percentage of
        // data loaded
        const fileuploadprogressall = (_: any, data: any) => {
            const progress = parseInt(String(data.loaded / data.total * 100), 10);
            const $jsProgressBar = rootElement.find(".ws-progress-bar");
            const $jsProgressText = rootElement.find(".ws-progress-text");
            $jsProgressBar.css('width', progress + '%');
            $jsProgressText.find("em").text(progress + "%");
        }

        const fileuploaddone = (_: any, data: any) => {
            const $jsProgressText = rootElement.find(".ws-progress-text");
            $jsProgressText.find("em").text(this.api.translate("files.progComplete"));
            const finishedFile = data.result.files[0].name;
            const fileId = data.result.files[0].id;
            
            this.api.send({
                upload: {
                    fileId
                }
            });
            
            uploadedFiles.push(finishedFile);
            uploadingFiles.splice(uploadingFiles.indexOf(finishedFile), 1);
            checkUploadingFinished();
        }
        
        const checkUploadingFinished = () => {
        	if (uploadingFiles.length === 0) {
        		if (uploadedFiles.length > 0) {
        			fileDialogSuccessMessageContent.html(this.api.translate('<p>${files.uploadComplete}</p>'));
        	        this.animateShow(fileDialogSuccessMessage);
        		}
        		this.filesSelected(uploadedFiles);
        		uploadedFiles = [];
        		setTimeout(() => {
        		   	setProgressBarVisible(false);
        		}, 5000);
                jqXHRFileupload = [];
            }
        }

        // TODO fix typings for fileupload plugin (remove all "any" types)
        const autoJqUpload = (autoFileupload as any).fileupload({
            xhrFields: {
                withCredentials: true
            },
            url: this.getFileUrl("file"),
            dataType: 'json',
            dropZone: null
        });
        autoJqUpload.bind('fileuploadfail', fileuploadfail);
        autoJqUpload.bind('fileuploadprogressall', fileuploadprogressall);
        autoJqUpload.bind('fileuploadadd', (e: any, data: any) => fileuploadadd(e, data, autoJqUpload));
        autoJqUpload.bind('fileuploaddone', fileuploaddone);

        const jqUpload = (fileUpload as any).fileupload({
            xhrFields: {
                withCredentials: true
            },
            url: this.getFileUrl("file"),
            dataType: 'json',
            dropZone
        });
        jqUpload.bind('fileuploadfail', fileuploadfail);
        jqUpload.bind('fileuploadprogressall', fileuploadprogressall);
        jqUpload.bind('fileuploadadd', (e: any, data: any) => fileuploadadd(e, data, jqUpload));
        jqUpload.bind('fileuploaddone', fileuploaddone);

        const cancelFileSelection = () => {
            this.sendMessageEvent(SimpleEventType.cancelFileSelection);
            cancelUpload();
        }

        const cancelUpload = () => {
            this.filesSelected([]);
            jqXHRFileupload.forEach((el) => {
                el.abort();
            });
            setProgressBarVisible(false);
        }

        cancelAutoSaveButton.bind('click', cancelFileSelection);
        cancelAutoUploadButton.bind('click', cancelFileSelection);
        cancelBtn.bind('click', cancelFileSelection);

        deleteSelectedButton.bind('click', () => {
            this.sendMessageEvent(SimpleEventType.deleteFile);
        });

        downloadSelectedButton.bind('click', () => {
            this.sendMessageEvent(SimpleEventType.downloadFile);
        });

        rootElement.bind('drop', (e) => {
            e.preventDefault();
        });

        uploadAutoUploadButton.bind('click', () => {
            getAutoFileInput().click();
        });

        uploadBtn.bind('click', () => {
        	getFileInput().click();
        });

        autoSaveButton.bind('click', () => {
            const fileString = autoSaveInput.val() as string;
            if (validateFilename() && fileString.length > 0) {
                this.filesSelected([fileString]);
            }
        });

        const validateFilename = () => {
            const fileString = autoSaveInput.val() as string;
            if (fileString && fileString.match(/^[a-zA-Z0-9. _-]*$/)) {
                fileDialogErrorMessageContent.html("");
                fileDialogErrorMessage.hide("fast");
                return true;
            } else {
                fileDialogErrorMessageContent.html(this.api.translate('<p>${files.saveInvlidFilename}</p>'));
                this.animateShow(fileDialogErrorMessage);
                return false;
            }
        }

        autoSaveInput.bind('input', validateFilename);

        const setProgressBarVisible = (bool: boolean) => {
            if (bool) {
                uploadProgress.css('width', '0%');
                uploadProgressBar.fadeIn(200);
            } else {
                uploadProgressBar.fadeOut(200);
                uploadProgress.css('width', '0%');
            }
        }

        const displayDownloadLinkDialog = (msg: IDownloadDialogData, url: string) => {
            if (linkDialog.closest(rootElement).length === 0) {
                rootElement.append(linkDialog);
            }

            const title = linkDialog.find('div[data-id="title"]');
            title.html(this.api.translate(msg.title));

            const message = linkDialog.find('div[data-id="message"]');
            message.html(this.api.translate(msg.message));

            const linkBtn = linkDialog.find('button[data-id="downloadLinkButton"]');
            linkBtn.html(this.api.translate(msg.buttonMessage));
            linkBtn.off('click').on('click', () => {
                window.open(url, '_blank');
                closeDownloadLinkDialog(linkDialog, rootElement);
            }).blur();

            const closeBtn = linkDialog.find('button[data-id="cancelDownloadLinkButton"]');
            closeBtn.off('click').on('click', () => {
                closeDownloadLinkDialog(linkDialog, rootElement);
            }).blur();

            linkDialog.show();
            if ((doc as any).activeElement) {
                (doc as any).activeElement.blur();
            }
            linkBtn[0].focus();
        }

        rootElement.bind('dragover', () => {
            if (!timeout) {
                dropZone.addClass('ws-filebar-dropArea--ondrag');
            } else {
                clearTimeout(timeout);
            }

            timeout = setTimeout(() => {
                timeout = undefined;
                dropZone.removeClass('ws-filebar-dropArea--ondrag');
            }, 100);
        });

        const fhAutoUpload = (data: appFrameProtoOut.IFileDialogEventMsgOutProto, instanceId?: string) => {
            if (autoUploadBar.closest(rootElement).length === 0) {
                rootElement.append(autoUploadBar);
                setProgressBarVisible(false);
                fileDialogSuccessMessageContent.html("");
                fileDialogSuccessMessage.hide("fast");
            }
            uploadingFiles = [];
            uploadedFiles = [];
            autoUploadfileDialogTransferBarClientId.val(instanceId!);
            getAutoFileInput().prop("multiple", data.isMultiSelection);
            getAutoFileInput().attr("accept", data.filter!);
            this.animateShow(autoUploadBar);
        };

        const fhAautoSave = (data: appFrameProtoOut.IFileDialogEventMsgOutProto, _?: string) => {
            if (autoSaveBar.closest(rootElement).length === 0) {
                rootElement.append(autoSaveBar);
            }
            autoSaveInput.val(data.selection!);
            fileDialogSuccessMessageContent.html("");
            fileDialogSuccessMessage.hide("fast");
            this.animateShow(autoSaveBar);
        };

        const fhOpen = (data: appFrameProtoOut.IFileDialogEventMsgOutProto, instanceId?: string) => {
            closeAfterErrorTimeout = false;
            if (uploadBar.closest(rootElement).length === 0) {
                rootElement.append(uploadBar);
                setProgressBarVisible(false);
                fileDialogSuccessMessageContent.html("");
                fileDialogSuccessMessage.hide("fast");
            }
            fileDialogTransferBarClientId.val(instanceId!);

            appendOrDetach(downloadSelectedButton, fileActionButtonGroup, data.allowDownload!);
            appendOrDetach(uploadBtn, fileActionButtonGroup, data.allowUpload!);
            appendOrDetach(deleteSelectedButton, fileActionButtonGroup, data.allowDelete!);

            this.showOrHide(dropZone, data.allowUpload!);
            this.showOrHide(cancelBtn, (data.allowDownload! || data.allowUpload! || data.allowDelete!) && !data.customDialog);
            getFileInput().prop("multiple", data.isMultiSelection);
            getFileInput().attr("accept", data.filter!);

            this.showOrHide(uploadBar, data.allowDownload || data.allowUpload || data.allowDelete!);
        };

        const fhClose = () => {
            if (uploadBar != null && uploadBar.closest(rootElement).length !== 0) {
                if (!errorTimeout) {
                    uploadBar.hide("fast", () => {
                        this.api.focusDefault();
                    });
                    uploadBar.detach();
                } else {
                    closeAfterErrorTimeout = true;
                }
            }
            if (autoUploadBar != null && autoUploadBar.closest(rootElement).length !== 0) {
                autoUploadBar.hide("fast", () => {
                    this.api.focusDefault();
                });
                autoUploadBar.detach();
            }
            if (autoSaveBar != null && autoSaveBar.closest(rootElement).length !== 0) {
                autoSaveBar.hide("fast", () => {
                    this.api.focusDefault();
                });
                autoSaveBar.detach();
            }
        };

        const fhDownload = (url: string) => {
            if (isIOS()) {
                const msg = {
                    "title": "${files.downloadLink.download.title}",
                    "message": "${files.downloadLink.download.message}",
                    "buttonMessage": "${files.downloadLink.download.button}"
                };
                displayDownloadLinkDialog(msg, this.getFileUrl(url));
            } else {
                const hiddenIFrameID = 'hiddenDownloader' + url;
                let iframe = doc[0].getElementById(hiddenIFrameID) as HTMLIFrameElement;
                if (iframe != null) {
                    iframe.parentNode?.removeChild(iframe);
                }
                iframe = doc[0].createElement('iframe');
                iframe.id = hiddenIFrameID;
                iframe.style.display = 'none';
                doc[0].body.appendChild(iframe);
                iframe.src = this.getFileUrl(url);
            }
        }

        const fhLink = (url: string) => {
            if (isIOS()) {
                const msg = {
                    "title": "${files.downloadLink.link.title}",
                    "message": "${files.downloadLink.link.message}",
                    "buttonMessage": "${files.downloadLink.link.button}"
                };
                displayDownloadLinkDialog(msg, url);
            } else {
                window.open(url, '_blank');
            }
        };

        const fhPrint = (url: string) => {
            const finalUrl = this.api.cfg.connectionUrl + 'print/web/viewer.html?file=' + encodeURIComponent(this.getFileUrl(url));

            if (isIOS()) {
                const msg = {
                    "title": "${files.downloadLink.print.title}",
                    "message": "${files.downloadLink.print.message}",
                    "buttonMessage": "${files.downloadLink.print.button}"
                };
                displayDownloadLinkDialog(msg, finalUrl);
            } else {
                window.open(finalUrl, '_blank');
            }
        };

        this.holderMap[win.name] = {
            win,
            autoSave: fhAautoSave,
            autoUpload: fhAutoUpload,
            close: fhClose,
            download: fhDownload,
            link: fhLink,
            open: fhOpen,
            print: fhPrint
        };

    }

    private animateShow(element: JQuery<HTMLElement>) {
        element.show('fast', () => {
            const initFocus = element.find(".init-focus");
            if (initFocus.length) {
                initFocus[0].focus();
            }
        });
    }

    private animateHide(element: JQuery<HTMLElement>) {
        element.hide('fast', () => {
            this.api.focusDefault();
        });
    }

    private showOrHide(element: JQuery<HTMLElement>, bool: boolean) {
        if (bool) {
            this.animateShow(element);
        } else {
            this.animateHide(element);
        }
    }

    private filesSelected(files: string[]) {
        this.api.send({
            selected: {
                files
            }
        });
    }

    private sendMessageEvent(message: commonProto.SimpleEventMsgInProto.SimpleEventTypeProto) {
        this.api.sendSimpleEvent({
            type: message
        });
    }

    private getFileUrl(url: string) {
        let baseUrl = this.api.cfg.connectionUrl + url;
        return baseUrl;
    }

}

function appendOrDetach(element: JQuery<HTMLElement>, parent: JQuery<HTMLElement>, bool: boolean) {
    element.detach();
    if (bool) {
        parent.append(element);
    }
}

function closeDownloadLinkDialog(linkDialog: JQuery<HTMLElement>, rootElement: JQuery<HTMLElement>) {
    if (linkDialog != null && linkDialog.closest(rootElement).length !== 0) {
        linkDialog.hide("fast");
        linkDialog.detach();
    }
}