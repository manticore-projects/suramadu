import { appFrameProtoOut } from "./proto/proto.out";
import { appFrameProtoIn } from "./proto/proto.in";
import { commonProto } from "./proto/proto.common";
import { DirectDraw as WebswingDirectDraw } from "suramadu-directdraw-javascript";
import { getDpr, GUID, getImageString, getTimeZone, fixConnectionUrl } from './suramadu-util';
import { ModuleDef, IInjected } from "./suramadu-inject";
import { AppFrame } from "./suramadu-socket";
import DOMPurify from 'dompurify';

export const baseInjectable = {
	getExternalApi: 'external.api' as const,
	cfg: 'webswing.config' as const,
	disconnect: 'webswing.disconnect' as const,
	getInstanceId: 'socket.instanceId' as const,
	send: 'socket.send' as const,
	sendHandshake: 'socket.sendHandshake' as const,
	getCanvas: 'canvas.get' as const,
	getDesktopSize: 'canvas.getDesktopSize' as const,
	processComponentTree: 'canvas.processComponentTree' as const,
	registerInput: 'input.register' as const,
	sendInput: 'input.sendInput' as const,
	sendSimpleEvent: 'socket.sendSimpleEvent' as const,
	sendTimestamp: 'socket.sendTimestamp' as const,
	registerUndockedCanvas: 'input.registerUndockedCanvas' as const,
	registerTouch: 'touch.register' as const,
	initCanvas: 'canvas.init' as const,
	touchCursorChanged: 'touch.cursorChanged' as const,
	touchBarConfig: 'touch.touchBarConfig' as const,
	getUser: 'login.user' as const,
	login: 'login.login' as const,
	logout: 'login.logout' as const,
	touchSession: 'login.touchSession' as const,
	getIdentity: 'identity.get' as const,
	disposeIdentity: 'identity.dispose' as const,
	getLocale: 'translate.getLocale' as const,
	translate: 'translate.translate' as const,
	showDialog: 'dialog.show' as const,
	showDialogBar: 'dialog.showBar' as const,
	hideDialog: 'dialog.hide' as const,
	hideDialogBar: 'dialog.hideBar' as const,
	showOverlay: 'dialog.showOverlay' as const,
	hideOverlay: 'dialog.hideOverlay' as const,
	dialogBarContent: 'dialog.currentBar' as const,
	currentDialog: 'dialog.current' as const,
	dialogs: 'dialog.content' as const,
	processFileDialogEvent: 'files.process' as const,
	closeFileDialog: 'files.close' as const,
	openLink: 'files.link' as const,
	print: 'files.print' as const,
	download: 'files.download' as const,
	redirect: 'files.redirect' as const,
	displayCopyBar: 'clipboard.displayCopyBar' as const,
	displayPasteDialog: 'clipboard.displayPasteDialog' as const,
	processJsLink: 'jslink.process' as const,
	playbackInfo: 'playback.playbackInfo' as const,
	paintDoneCallback: 'base.paintDoneCallback' as const,
	performAction: 'base.performAction' as const,
	handleActionEvent: 'base.handleActionEvent' as const,
	handleAccessible: 'accessible.handleAccessible' as const,
	isAccessiblityEnabled: 'accessible.isEnabled' as const,
	showWindowSwitcher: 'accessible.showWindowSwitcher' as const,
	manageFocusEvent: 'focusManager.manageFocusEvent' as const,
	disposeSocket: 'socket.dispose' as const,
	disposeCanvas: 'canvas.dispose' as const,
	newSession: 'webswing.newSession' as const,
	clearInstanceId: 'socket.clearInstanceId' as const,
	touchRefreshScale: 'touch.refreshScale' as const
}

export interface IBaseService {
	'base.startApplication': () => void,
	'base.startMirrorView': (clientId: string) => void,
	'base.continueSession': () => void,
	'base.kill': () => void,
	'base.handshake': () => void,
	'base.processMessage': (data: AppFrame) => void,
	'base.dispose': () => void,
	'base.repaint': () => void,
	'base.requestComponentTree': () => void,
	'base.paintDoneCallback': () => void,
	'base.getWindows': (canvasOnly: boolean) => Array<CanvasWindow | HtmlWindow>,
	'base.getWindowById': (winId: string) => CanvasWindow | HtmlWindow | undefined,
	'base.performAction': (options: {actionName?: string, data?: string, binaryData?: Uint8Array, windowId?: string}) => void,
	'base.handleActionEvent': (actionName?: string | null, data?: string | null, binaryData?: Uint8Array | null) => void,
	'base.hasUndockedWindows': () => boolean,
	'base.getFocusedWindow': () => Window,
	'base.getAllWindows': () => Window[],
	'base.getMainWindowVisibilityState': () => DocumentVisibilityState,
	'base.closeWindow': (id: string) => void
	'base.startNewSessionOnShutdown': () => void
}

export type IDockableWindow = Window & typeof globalThis & {
    inputHandler: HTMLInputElement
	pagePosition: { x: number, y: number }
	resizeTimer: number | null
	createTime:number
	ignoreDockResizeTimestamp:number
}
export type HtmlOrCanvasWindow = HtmlWindow | CanvasWindow

type IAudioClip = HTMLAudioElement & {
	loopCount: number,
	playbackTimer?: number
}

type ITimestampAppFrameMsgOut = AppFrame & {
	receivedTimestamp: number;
}

const LinkActionType = appFrameProtoOut.LinkActionMsgOutProto.LinkActionTypeProto;
const WindowType = appFrameProtoOut.WindowMsgOutProto.WindowTypeProto;
const DockMode = appFrameProtoOut.WindowMsgOutProto.DockModeProto;
const DockState = appFrameProtoOut.WindowMsgOutProto.DockStateProto;
const WindowEventType = appFrameProtoIn.WindowEventMsgInProto.WindowEventTypeProto;
const SimpleEvent = appFrameProtoOut.SimpleEventMsgOutProto;
const SimpleEventType = commonProto.SimpleEventMsgInProto.SimpleEventTypeProto;
const AudioEventType = appFrameProtoOut.AudioEventMsgOutProto.AudioEventTypeProto;
const ActionEventType = appFrameProtoIn.ActionEventMsgInProto.ActionEventTypeProto;
const JFRAME_MAXIMIZED_STATE = 6;
const compositionBaseZIndex = 100;

export class BaseModule extends ModuleDef<typeof baseInjectable, IBaseService> {

	public windowImageHolders: { [K: string]: CanvasWindow | HtmlWindow } = {}; // <id> : <CanvasWindow/HtmlWindow>

	private timer1?: number;
	private timer3?: number;
	private timerDockResize?: number;
	private drawingLock?: AppFrame | null;
	private drawingQ: AppFrame[] = [];
	private warningTimeout?: number;
	private internalFrameWrapperHolders: {[K: string]: HTMLElement} = {}; // <id> : <IFW element>
	private closedWindows: {[K: string]: boolean} = {}; // <id> : <boolean>, map of windows requested to be closed, rendering of windows in this map will be ignored, until message about closed window is received
	private directDraw?: WebswingDirectDraw;
	private compositingWM = false;
	private audio: {[K: string]: IAudioClip} = {};
	private focusedUndockedWindow: Window | null = null;
	private dockResizeInterval = 250;
	private popupStartupResizeInterval = 1000;
	private audioPlaybackPingInterval = 1000;
	private startNewSessionOnShutdown = false;

	public ready = () => {
		this.directDraw = new WebswingDirectDraw({ logTrace: this.api.cfg.traceLog, logDebug: this.api.cfg.debugLog, ieVersion: this.api.cfg.ieVersion, dpr: getDpr() });
	};

	public provides() {
		return {
			'base.startApplication': this.startApplication,
			'base.startMirrorView': this.startMirrorView,
			'base.continueSession': this.continueSession,
			'base.kill': this.kill,
			'base.handshake': this.handshake,
			'base.processMessage': this.processMessage,
			'base.dispose': this.dispose,
			'base.repaint': this.repaint,
			'base.requestComponentTree': this.requestComponentTree,
			'base.paintDoneCallback': this.paintDoneCallback,
			'base.getWindows': this.getWindows,
			'base.getWindowById': this.getWindowById,
			'base.performAction': this.performAction,
			'base.handleActionEvent': this.handleActionEvent,
			'base.hasUndockedWindows': this.hasUndockedWindows,
			'base.getFocusedWindow': this.getFocusedWindow,
			'base.getAllWindows': this.getAllWindows,
			'base.getMainWindowVisibilityState': this.getMainWindowVisibilityState,
			'base.closeWindow': this.closeWindow,
			'base.startNewSessionOnShutdown': () => { this.startNewSessionOnShutdown = true; }
		}
	}

	public startApplication() {
		this.initialize(null, false);
	}

	public startMirrorView(clientId: string | null) {
		this.initialize(clientId, true)
	}

	public initialize(clientId: string | null, isMirror: boolean) {
		this.api.disposeCanvas();
		this.api.initCanvas();
		this.api.registerInput();
		if (!isMirror) {
			this.api.registerTouch(this.api.touchBarConfig);
		}
		window.addEventListener('beforeunload', () => this.beforeUnloadEventHandler());
		if (!isMirror) {
			window.addEventListener('hashchange', () => this.handshake());
		}
		document.addEventListener("visibilitychange", () => this.visibilityChangeHandler());
		this.resetState();
		this.api.cfg.clientId = clientId;
		this.api.cfg.canPaint = true;
		this.api.cfg.hasControl = !isMirror;
		this.api.cfg.mirrorMode = isMirror;
		this.handshake();
		if (isMirror) {
			this.repaint();
		}
		this.api.showDialog(this.api.dialogs.startingDialog);
	}

	public beforeUnloadEventHandler() {
		this.dispose();
		this.api.disposeSocket();
	}

	public continueSession() {
		this.api.hideDialog();
		this.api.cfg.canPaint = true;
		this.handshake();
		this.repaint();
		this.ack();
	}

	public resetState() {
		if (this.compositingWM) {
			for (const winId in this.windowImageHolders) {
				if (this.windowImageHolders.hasOwnProperty(winId)) {
					const win = this.windowImageHolders[winId];

					try {
						if (win.dockOwner) {
							// close any undocked popup windows
							win.preventDockClose = true;
							win.dockOwner.close();
						}
					} catch (err) {
						console.error(err);
					}
				}
			}
		}

		this.api.cfg.clientId = '';
		this.api.cfg.viewId = GUID();
		this.api.cfg.hasControl = false;
		this.api.cfg.mirrorMode = false;
		this.api.cfg.canPaint = false;
		this.api.cfg.browserId = this.api.getIdentity();
		clearInterval(this.timer1);
		clearInterval(this.timer3);
		clearInterval(this.timerDockResize);
		this.timer1 = setInterval(() => this.api.sendInput(), 100);
		this.timer3 = setInterval(() => this.servletHeartbeat(), 120000);
		this.timerDockResize = setInterval(() => this.dockResize(), this.dockResizeInterval);
		this.compositingWM = false;
		this.startNewSessionOnShutdown = false;
		this.windowImageHolders = {};
		this.internalFrameWrapperHolders = {};
		this.closedWindows = {};
		if (this.directDraw) {
			this.directDraw.dispose();
		}
		this.directDraw = new WebswingDirectDraw({ logTrace: this.api.cfg.traceLog, logDebug: this.api.cfg.debugLog, ieVersion: this.api.cfg.ieVersion, dpr: getDpr() });
		this.drawingQ = [];
	}

	public sendMessageEvent(message: commonProto.SimpleEventMsgInProto.SimpleEventTypeProto) {
		this.api.sendSimpleEvent({
			type: message
		});
	}

	public servletHeartbeat() {
		// touch servlet session to avoid timeout
		// (refresh JWT token, there is no session)
		this.api.touchSession();
	}

	public repaint() {
		this.sendMessageEvent(SimpleEventType.repaint);
	}

	public ack(data?: ITimestampAppFrameMsgOut) {
		this.sendMessageEvent(SimpleEventType.paintAck);
		if (data && data != null) {
			this.api.sendTimestamp({
				startTimestamp: data.startTimestamp,
				sendTimestamp: data.sendTimestamp,
				renderingTime: "" + (new Date().getTime() - data.receivedTimestamp)
			});
		}
	}

	public kill() {
		if (this.api.cfg.hasControl) {
			this.sendMessageEvent(SimpleEventType.killSwing);
		}
	}

	public unload() {
		if (!this.api.cfg.mirrorMode) {
			this.sendMessageEvent(SimpleEventType.unload);
		}
	}

	public handshake() {
		this.api.sendHandshake(this.getHandShake());
	}

	public dispose() {
		clearInterval(this.timer1);
		this.unload();
		this.api.sendInput();
		this.resetState();
		this.api.closeFileDialog();
		this.api.hideDialogBar();
		window.removeEventListener('beforeunload', this.beforeUnloadEventHandler);
		document.removeEventListener("visibilitychange", this.visibilityChangeHandler);
		if (this.directDraw) {
			this.directDraw.dispose();
		}

		for (const id in this.audio) {
			if (id in this.audio) {
				this.stopAudioPing(this.audio[id]);
			}
		}
	}

	public requestComponentTree() {
		this.sendMessageEvent(SimpleEventType.requestComponentTree);
		this.repaint();
	}

	public processMessage(data: AppFrame) {
		if (data.playback != null) {
			this.api.playbackInfo(data);
		}
		if (data.startApplication != null) {
			if (this.api.cfg.mirror) {
				this.startMirrorView(this.api.cfg.clientId);
			} else {
				this.startApplication();
			}
			return;
		}
		if (data.event != null && !this.api.cfg.recordingPlayback) {
			if (data.event === SimpleEvent.shutDownAutoLogoutNotification) {
				this.api.disconnect();
				if (this.api.cfg.mirror === false) {
					this.api.logout();
				}
			} else if (data.event === SimpleEvent.shutDownNotification) {
				if (this.startNewSessionOnShutdown) {
					this.startNewSessionOnShutdown = false;
					this.api.newSession();
				} else {
					if (this.api.currentDialog() !== this.api.dialogs.timedoutDialog) {
						this.api.showDialog(this.api.dialogs.stoppedDialog);
					}
					this.api.clearInstanceId();
					this.api.disconnect();
				}
			} else if (data.event === SimpleEvent.applicationAlreadyRunning) {
				this.api.showDialog(this.api.dialogs.applicationAlreadyRunning);
				this.api.clearInstanceId();
				this.api.disconnect();
			} else if (data.event === SimpleEvent.tooManyClientsNotification) {
				this.api.showDialog(this.api.dialogs.tooManyClientsNotification);
				this.api.clearInstanceId();
				this.api.disconnect();
			} else if (data.event === SimpleEvent.reconnectInstanceNotFound) {
				this.api.showDialog(this.api.dialogs.reconnectInstanceNotFound);
				this.api.disconnect();
			} else if (data.event === SimpleEvent.continueOldSession) {
				this.continueSession();
				const oldBarContent = this.api.dialogBarContent();
				this.api.showDialogBar(this.api.dialogs.continueOldSessionDialog);
				let contentBar = this.api.dialogBarContent();
				if(contentBar){//can be null if customization removes continueOldSessionDialog content
					contentBar.focused = false;
					window.setTimeout(() => {
						const content = this.api.dialogBarContent();
						if (content && !content.focused) {
							this.api.hideDialogBar();
							if (oldBarContent) {
								this.api.showDialogBar(oldBarContent);
							}
						}
					}, 5000);
				}

			} else if (data.event === SimpleEvent.sessionStolenNotification) {
				this.api.cfg.canPaint = false;
				this.api.showDialog(this.api.dialogs.sessionStolenNotification);
			} else if (data.event === SimpleEvent.unauthorizedAccess) {
				this.api.cfg.canPaint = false;
				this.api.showDialog(this.api.dialogs.unauthorizedAccess);
				this.api.clearInstanceId();
			} else if (data.event === SimpleEvent.sessionTimeoutWarning) {
				window.clearTimeout(this.warningTimeout);
				this.api.showDialogBar(this.api.dialogs.inactivityTimeoutWarningDialog);
				this.warningTimeout = window.setTimeout(() => {
					this.api.hideDialogBar();
				}, 2000);
			} else if (data.event === SimpleEvent.sessionTimedOutNotification) {
				this.api.showDialog(this.api.dialogs.timedoutDialog);
				this.api.clearInstanceId();
				this.api.disconnect();
			} else if (data.event === SimpleEvent.applicationBusy) {
				if (this.api.currentDialog() == null) {
					this.api.showDialog(this.api.dialogs.applicationBusyDialog);
				}
			}
			return;
		}
		if (data.jsRequest != null && this.api.cfg.mirrorMode === false && !this.api.cfg.recordingPlayback) {
			this.api.processJsLink(data.jsRequest);
		}
		if (data.pixelsRequest != null && this.api.cfg.mirrorMode === false && !this.api.cfg.recordingPlayback) {
			const pixelsResponse = this.getPixels(data.pixelsRequest);
			this.api.send(pixelsResponse);
		}
		if (this.api.cfg.canPaint) {
			this.queuePaintingRequest(data);
		}
		if (data.componentTree) {
			this.api.processComponentTree(data.componentTree);
		}
		if (data.windowSwitchList) {
			this.api.showWindowSwitcher($(this.getFocusedWindow().document.querySelector(".suramadu-element-content") as HTMLElement), data.windowSwitchList);
		}
	}

	public queuePaintingRequest(data: AppFrame) {
		this.drawingQ.push(data);
		if (!this.drawingLock) {
			this.processNextQueuedFrame();
		}
	}

	public processNextQueuedFrame() {
		this.drawingLock = null;
		if (this.drawingQ.length > 0) {
			this.drawingLock = this.drawingQ.shift();
			try {
				this.processRequest(this.drawingLock!);
			} catch (error) {
				this.drawingLock = null;
				throw error;
			}
		} else {
			// empty queue
			this.api.paintDoneCallback();
		}
	}

	public paintDoneCallback() {
		// to be customized
	}

	public processRequest(data: AppFrame) {
		let windowsData = null;
		if (data.windows != null && data.windows.length > 0) {
			windowsData = data.windows;
		}
		if (data.compositingWM && !this.compositingWM) {
			this.compositingWM = true;
			this.api.cfg.rootElement.addClass("composition");
			$(this.api.getCanvas()).css({ "width": "0px", "height": "0px", "position": "absolute", "top": "0", "left": "0" });
		}

		if (windowsData != null) {
			this.api.hideDialog();
		}
		if (data.linkAction != null && !this.api.cfg.recordingPlayback) {
			if (data.linkAction.action === LinkActionType.url) {
				this.api.openLink(data.linkAction.src);
			} else if (data.linkAction.action === LinkActionType.print) {
				this.api.print('file?id=' + data.linkAction.src);
			} else if (data.linkAction.action === LinkActionType.file) {
				this.api.download('file?id=' + data.linkAction.src);
			} else if (data.linkAction.action === LinkActionType.redirect && !this.api.cfg.mirrorMode) {
				this.api.redirect(data.linkAction.src);
			}
		}
		if (data.moveAction != null) {
			// this applies only to non-CWM
			this.copy(data.moveAction.sx!, data.moveAction.sy!, data.moveAction.dx!, data.moveAction.dy!, data.moveAction.width!, data.moveAction.height!, this.api.getCanvas().getContext("2d")!);
		}
		if (data.focusEvent != null) {
			this.api.manageFocusEvent(data.focusEvent);
		}
		if (data.accessible != null) {
			this.api.handleAccessible(data.accessible, $(this.getFocusedWindow().document.querySelector(".suramadu-element-content") as HTMLElement));
		}
		if (data.cursorChangeEvent && data.cursorChangeEvent != null && this.api.cfg.hasControl && !this.api.cfg.recordingPlayback) {
			const element = this.compositingWM ? this.findCanvasWindowsById(data.cursorChangeEvent.winId!) : $("canvas.suramadu-canvas");
			element.each((_, canvas) => {
				const style = this.getCursorStyle(data.cursorChangeEvent!);
				if (style && style != null) {
					canvas.style.cursor = style;
				}
			});
			this.api.touchCursorChanged(data.cursorChangeEvent);
		}
		if (data.copyEvent != null && this.api.cfg.hasControl && !this.api.cfg.recordingPlayback) {
			this.api.displayCopyBar(data.copyEvent);
		}
		if (data.pasteRequest != null && this.api.cfg.hasControl && !this.api.cfg.recordingPlayback) {
			this.api.displayPasteDialog(data.pasteRequest);
		}
		if (data.fileDialogEvent != null && this.api.cfg.hasControl && !this.api.cfg.recordingPlayback) {
			this.api.processFileDialogEvent(data.fileDialogEvent);
		}
		if (data.closedWindow != null) {
			this.closeWindowInternal(data.closedWindow.id);
		}
		if (data.actionEvent != null && this.api.cfg.hasControl && !this.api.cfg.recordingPlayback) {
			try {
				if (data.actionEvent.windowId && this.windowImageHolders[data.actionEvent.windowId]) {
					this.windowImageHolders[data.actionEvent.windowId].handleActionEvent(data.actionEvent.actionName, data.actionEvent.data, data.actionEvent.binaryData);
				} else {
					this.api.handleActionEvent(data.actionEvent.actionName, data.actionEvent.data, data.actionEvent.binaryData);
				}
			} catch (e) {
				console.error(e);
			}
		}
		if (data.audioEvent != null) {
			switch (data.audioEvent.eventType) {
				case AudioEventType.play: {
					let clip = this.audio[data.audioEvent.id!];

					if (!clip) {
						// new clip
						const blob = new Blob([new Uint8Array(data.audioEvent.data! as Uint8Array)], { type: 'audio/mpeg' });
						const url = window.URL.createObjectURL(blob);

						clip = new Audio() as IAudioClip;
						clip.src = url;
						clip.id = data.audioEvent.id!;
						clip.loopCount = data.audioEvent.loop || 0;
						if (data.audioEvent.time != null && data.audioEvent.time > 0) {
							clip.currentTime = data.audioEvent.time;
						}
						clip.addEventListener('ended', (evt) => this.audioEndedHandler(evt));

						this.audio[data.audioEvent.id!] = clip;

						this.startAudioPing(clip);
						clip.play();
					} else {
						// existing clip
						// if a clip is running we ignore another play event
						if (clip.paused) {
							clip.loopCount = data.audioEvent.loop || 0;
							if (data.audioEvent.time != null && data.audioEvent.time > 0) {
								clip.currentTime = data.audioEvent.time;
							}

							this.startAudioPing(clip);
							clip.play();
						}
					}

					break;
				}
				case AudioEventType.stop: {
					const clip = this.audio[data.audioEvent.id!];

					if (clip) {
						clip.pause();
						this.stopAudioPing(clip);
					}
					break;
				}
				case AudioEventType.update: {
					const clip = this.audio[data.audioEvent.id!];

					if (clip) {
						if (data.audioEvent.time != null && data.audioEvent.time > 0) {
							clip.currentTime = data.audioEvent.time;
						}
					}
					break;
				}
				case AudioEventType.dispose: {
					const clip = this.audio[data.audioEvent.id!];

					if (clip && clip.paused) {
						delete this.audio[data.audioEvent.id!];
					}
					break;
				}
			}
		}

		if (windowsData != null) {
			const winMap: {[K: string]: boolean} = {};

			// first is always the background
			for (let i=0; i<windowsData.length; i++) {
				const win = windowsData[i];
				if (win.id === 'BG') {
					if (this.api.cfg.mirrorMode || this.api.cfg.recordingPlayback) {
						this.adjustCanvasSize(this.api.getCanvas(), win.width!, win.height!);
					}
					if (win.content && win.content != null) {
						for (const winContent of win.content) {
							if (winContent != null) {
								this.clear(win.posX! + winContent.positionX!, win.posY! + winContent.positionY!, winContent.width!, winContent.height!, this.api.getCanvas().getContext("2d")!);
							}
						}
					}
					windowsData.splice(i, 1);
				} else if (win.internalWindows) {
					for (const intWin of win.internalWindows) {
						winMap[intWin.id] = true;
					}
				}
				winMap[win.id] = true;
			}

			if (this.compositingWM) {
				// with CWM we always get all the windows, so if an already open window is missing in windowsData we should close it
				for (const winId in this.windowImageHolders) {
					if (this.windowImageHolders.hasOwnProperty(winId)) {
						const win = this.windowImageHolders[winId];

						if (!winMap[winId] && win.element && $(win.element).is(":not(.close-prevented)")) {
							this.closeWindowInternal(winId);
						}
					}
				}
			}

			// regular windows (background removed)
			windowsData.reduce((sequence, window, index) => {
				return sequence.then(() => {
					return this.renderWindow(window, this.compositingWM ? windowsData.length - index - 1 : index, data.directDraw!);
				}).catch((e) => this.errorHandler(e));
			}, Promise.resolve()).then(() => {
				if (this.compositingWM) {
					// dispose of empty internal-frames-wrappers
					for (const id in this.internalFrameWrapperHolders) {
						if (this.internalFrameWrapperHolders.hasOwnProperty(id)) {
							const ifw = this.internalFrameWrapperHolders[id];
							if ($(ifw).is(":empty")) {
								$(ifw).remove();
							}
						}
					}
				}

				this.ack(data as ITimestampAppFrameMsgOut);
				this.processNextQueuedFrame();
			}).catch((e: Error) => this.errorHandler(e));

			this.checkCanvasAccessibleInfo();
			this.api.touchRefreshScale();
		} else {
			this.processNextQueuedFrame();
		}
	}

	public dockResize() {
		const popups = [];
		const now = new Date().getTime();
		for (const winId in this.windowImageHolders) {
			if (this.windowImageHolders.hasOwnProperty(winId)) {
				// go through each undocked popup only once
				const popup = this.windowImageHolders[winId].dockOwner;
				if (popup && popup != null && popups.indexOf(popup) === -1 && (now - popup.createTime > this.popupStartupResizeInterval) && (now - popup.ignoreDockResizeTimestamp > this.dockResizeInterval)) {
					popups.push(popup);
					let maxWidth = 0;
					let maxHeight = 0;
					const elements = popup.document.querySelectorAll("canvas.suramadu-canvas:not(.internal)");
					elements.forEach(element => {
						const rect = element.getBoundingClientRect();
						if (rect.left + rect.width > maxWidth) {
							maxWidth = rect.left + rect.width;
						}
						if (rect.top + rect.height > maxHeight) {
							maxHeight = rect.top + rect.height;
						}
					});
					const deltaX = maxWidth - popup.innerWidth;
					const deltaY = maxHeight - popup.innerHeight;
					if (deltaX !== 0 || deltaY !== 0) {
						popup.resizeBy(deltaX, deltaY);
					}
				}
			}
		}
	}

	public checkCanvasAccessibleInfo() {
		const enabled = this.api.isAccessiblityEnabled();
		const canvasSelector = enabled ? ".suramadu-element-content canvas.accessibility-off" : ".suramadu-element-content canvas:not(.accessibility-off)";
		const rootSelector = enabled ? ".suramadu-element-content.accessibility-off" : ".suramadu-element-content:not(.accessibility-off)";

		$(rootSelector).each((_, el) => {
			this.setRootElementAccessibleInfo($(el), enabled);
		});
		$(canvasSelector).each((_, el) => {
			this.setCanvasAccessibleInfo($(el), enabled);
		});
		for (const winId in this.windowImageHolders) {
			if (this.windowImageHolders.hasOwnProperty(winId)) {
				const dockOwner = this.windowImageHolders[winId].dockOwner;
				if (dockOwner != null && dockOwner.document) {
					const rootEls = dockOwner.document.querySelectorAll(rootSelector);
					rootEls.forEach(element => {
						this.setRootElementAccessibleInfo($(element as HTMLElement), enabled);
					});
					const canvasEls = dockOwner.document.querySelectorAll(canvasSelector);
					canvasEls.forEach(element => {
						this.setCanvasAccessibleInfo($(element as HTMLElement), enabled);
					});
				}
			}
		}
	}

	public setRootElementAccessibleInfo(rootElement: JQuery<HTMLElement>, enabled: boolean) {
		if (enabled) {
			rootElement.removeAttr("aria-label").removeClass("accessibility-off");
		} else {
			rootElement.attr("aria-label", this.api.translate("accessibility.turnOn")).addClass("accessibility-off");
		}
	}

	public setCanvasAccessibleInfo(canvas: JQuery<HTMLElement>, enabled: boolean) {
		if (enabled) {
			canvas.removeAttr("aria-label").removeAttr("role").removeClass("accessibility-off");
		} else {
			canvas.attr("role", "img").attr("aria-label", this.api.translate("accessibility.turnOn")).addClass("accessibility-off");
		}
	}

	public audioEndedHandler(e: Event) {
		const clip = e.srcElement as IAudioClip;

		if (!clip) {
			return;
		}

		if (!clip.loopCount || clip.loopCount === 1) {
			clip.loopCount = 0;
			clip.currentTime = 0;
			clip.pause();
			this.stopAudioPing(clip);
			this.api.send({ audio: { id: clip.id, stop: true } });
			return;
		}

		clip.loopCount = clip.loopCount - 1;
		clip.play();
	}

	public startAudioPing(clip: IAudioClip) {
		if (clip.playbackTimer) {
			clearInterval(clip.playbackTimer);
		}
		clip.playbackTimer = setInterval(() => {
			this.api.send({ audio: { id: clip.id, ping: true } });
		}, this.audioPlaybackPingInterval);
	}

	public stopAudioPing(clip: IAudioClip) {
		if (clip && clip.playbackTimer) {
			clearInterval(clip.playbackTimer);
		}
	}

	public closeWindow(id:string) {
		this.api.send({ window: { id, eventType: WindowEventType.close } });
		this.closedWindows[id] = true;
	}

	public closeWindowInternal(id:string) {
		const canvasWindow = this.windowImageHolders[id];

		if (canvasWindow) {
			const winCloseEvent = new WindowCloseEvent(canvasWindow.id);

			if (this.compositingWM) {
				this.windowClosing(canvasWindow, winCloseEvent);
				try {
					canvasWindow.windowClosing(winCloseEvent);
				} catch (e) {
					console.error(e);
				}

				if (canvasWindow.htmlWindow && winCloseEvent.isDefaultPrevented()) {
					$(canvasWindow.element).addClass("close-prevented");

					if (canvasWindow.element.ownerDocument!.defaultView !== window) {
						// dock undocked close-prevented HtmlWindows to main window
						this.api.cfg.rootElement.append($(canvasWindow.element).detach());
					}
				} else {
					if ((canvasWindow as CanvasWindow).dockState === DockState.undocked && canvasWindow.dockOwner) {
						canvasWindow.preventDockClose = true;
						canvasWindow.dockOwner.close();
					}
					$(canvasWindow.element).remove();
					delete this.windowImageHolders[id];
					this.checkUndockedModalBlocked();
				}

				this.windowClosed(canvasWindow);
				try {
					canvasWindow.windowClosed();
				} catch (e) {
					console.error(e);
				}
			} else {
				$(canvasWindow.element).remove();
				delete this.windowImageHolders[id];
			}
		}

		delete this.closedWindows[id];
	}

	public renderWindow(win: appFrameProtoOut.IWindowMsgOutProto, index: number, directDraw: boolean) {
		if (this.closedWindows[win.id]) {
			// ignore this window as it has been requested to be closed
			return Promise.resolve();
		}

		if (directDraw) {
			if (this.compositingWM) {
				return this.renderDirectDrawComposedWindow(win, index);
			}
			return this.renderDirectDrawWindow(win, this.api.getCanvas().getContext("2d")!);
		} else {
			if (this.compositingWM) {
				return this.renderPngDrawComposedWindow(win, index);
			}
			return this.renderPngDrawWindow(win, this.api.getCanvas().getContext("2d")!);
		}
	}

	public renderPngDrawWindow(win: appFrameProtoOut.IWindowMsgOutProto, context: CanvasRenderingContext2D) {
		return this.renderPngDrawWindowInternal(win, context);
	}

	public renderPngDrawComposedWindow(win: appFrameProtoOut.IWindowMsgOutProto, index: number) {
		if ((win.type === WindowType.basic || win.type === WindowType.html) && (typeof this.windowImageHolders[win.id] === 'undefined' || this.windowImageHolders[win.id] == null)) {
			let canvas: HTMLCanvasElement | undefined;

			if (win.type === WindowType.basic) {
				canvas = document.createElement("canvas");
				canvas.classList.add("suramadu-canvas");
			}

			this.openNewComposedWindow(win, canvas);
		}

		const htmlOrCanvasWin = this.windowImageHolders[win.id];

		this.handleWindowDockState(win.dockState, htmlOrCanvasWin);
		this.handleWindowModalityAndBounds(win, htmlOrCanvasWin, index, true);
		this.handleWindowState(win.state, htmlOrCanvasWin);
		this.drawInternalWindows(win.internalWindows);

		if (win.type === WindowType.basic) {
			const canvas = this.windowImageHolders[win.id].element as HTMLCanvasElement;
			return this.renderPngDrawWindowInternal(win, canvas.getContext("2d")!);
		}

		return Promise.resolve();
	}

	public renderPngDrawWindowInternal(win: appFrameProtoOut.IWindowMsgOutProto, context: CanvasRenderingContext2D) {
		if (!win.content) {
			this.drawInternalWindows(win.internalWindows);
			return Promise.resolve();
		}

		return win.content.reduce((sequence, winContent) => {
			return sequence.then(() => {
				return new Promise((resolved, _) => {
					if (winContent != null) {
						const imageObj = new Image();
						const onloadFunction = () => {
							context.save();
							if (this.compositingWM) {
								context.drawImage(imageObj, winContent.positionX!, winContent.positionY!);
							} else {
								context.setTransform(getDpr(), 0, 0, getDpr(), 0, 0);
								context.drawImage(imageObj, win.posX! + winContent.positionX!, win.posY! + winContent.positionY!);
							}
							context.restore();

							imageObj.onload = null;
							imageObj.src = '';
							if ((imageObj as any).clearAttributes != null) {
								(imageObj as any).clearAttributes();
							}

							this.drawInternalWindows(win.internalWindows);

							resolved();
						};
						imageObj.onload = () => {
							// fix for ie - onload is fired before the image
							// is ready for rendering to canvas.
							// This is an ugly quickfix
							if (this.api.cfg.ieVersion && (this.api.cfg.ieVersion as number) <= 10) {
								window.setTimeout(onloadFunction, 20);
							} else {
								onloadFunction();
							}
						};
						imageObj.src = getImageString(winContent.base64Content!);
					}
				});
			}, this.errorHandler);
		}, Promise.resolve());
	}

	public renderDirectDrawWindow(win: appFrameProtoOut.IWindowMsgOutProto, context: CanvasRenderingContext2D) {
		return new Promise<void>((resolved, rejected) => {
			let ddPromise;
			const wih = (this.windowImageHolders[win.id] && this.windowImageHolders[win.id] != null) ? this.windowImageHolders[win.id].element as HTMLCanvasElement : undefined;

			if (this.directDraw) {
				if (typeof win.directDraw === 'string') {
					ddPromise = this.directDraw.draw64(win.directDraw, wih);
				} else {
					ddPromise = this.directDraw.drawBin(win.directDraw!, wih);
				}
			}

			if (ddPromise) {
				ddPromise.then((canvas: HTMLCanvasElement) => {
					this.windowImageHolders[win.id] = new CanvasWindow(win.id, win.ownerId, this.getOwnerParents(win.ownerId), canvas, false, win.name, win.title, win.classType, this);
					const dpr = getDpr();
					if (win.content && win.content != null) {
						for (const winContent of win.content) {
							if (winContent != null) {
								const sx = Math.min(canvas.width, Math.max(0, winContent.positionX! * dpr));
								const sy = Math.min(canvas.height, Math.max(0, winContent.positionY! * dpr));
								const sw = Math.min(canvas.width - sx, winContent.width! * dpr - (sx - winContent.positionX! * dpr));
								const sh = Math.min(canvas.height - sy, winContent.height! * dpr - (sy - winContent.positionY! * dpr));

								const dx = win.posX! * dpr + sx;
								const dy = win.posY! * dpr + sy;
								const dw = sw;
								const dh = sh;
								if (dx <= context.canvas.width && dy <= context.canvas.height && dx + dw > 0 && dy + dh > 0 && sw > 0 && sh > 0 && dw > 0 && dh > 0) {
									context.drawImage(canvas, sx, sy, sw, sh, dx, dy, dw, dh);
								}
							}
						}
					}
					resolved();
				}, (error: Error) => {
					rejected(error);
				});
			}
		});
	}

	public renderDirectDrawComposedWindow(win: appFrameProtoOut.IWindowMsgOutProto, index: number) {
		return new Promise<void>((resolved, rejected) => {
			let ddPromise;
			const wih = (this.windowImageHolders[win.id] && this.windowImageHolders[win.id] != null) ? this.windowImageHolders[win.id].element as HTMLCanvasElement : undefined;

			if (win.type !== WindowType.basic) {
				// we don't need to draw html window, also do not create canvas
				ddPromise = new Promise<HTMLCanvasElement | undefined>((resolve, _) => {
					resolve(undefined);
				})
			} else if (win.directDraw == null) {
				// no render data
				if (!wih) {
					// new window and no data -> do not process
					resolved();
					return;
				}

				ddPromise = Promise.resolve(wih);
			} else if (typeof win.directDraw === 'string' && this.directDraw) {
				ddPromise = this.directDraw.draw64(win.directDraw, wih);
			} else if (this.directDraw) {
				ddPromise = this.directDraw.drawBin(win.directDraw, wih);
			}

			if (ddPromise) {
				ddPromise.then((canvas: HTMLCanvasElement | undefined) => {
					// new canvas window or html window is opening
					if ((canvas || win.type === WindowType.html) && (typeof this.windowImageHolders[win.id] === 'undefined' || this.windowImageHolders[win.id] == null)) {
						this.openNewComposedWindow(win, canvas);
					}

					const htmlOrCanvasWin = this.windowImageHolders[win.id];

					this.handleWindowDockState(win.dockState, htmlOrCanvasWin);
					this.handleWindowModalityAndBounds(win, htmlOrCanvasWin, index, false);
					this.handleWindowState(win.state, htmlOrCanvasWin);
					this.drawInternalWindows(win.internalWindows);

					resolved();
				}, (error: Error) => {
					rejected(error);
				});
			}
		});
	}

	public openNewComposedWindow(win: appFrameProtoOut.IWindowMsgOutProto, canvas?: HTMLCanvasElement) {
		if (win.type === WindowType.html) {
			const htmlDiv = document.createElement("div");
			htmlDiv.classList.add("suramadu-html-canvas");
			this.windowImageHolders[win.id] = new HtmlWindow(win.id, win.ownerId, this.getOwnerParents(win.ownerId), htmlDiv, win.name, this);
		} else {
			this.windowImageHolders[win.id] = new CanvasWindow(win.id, win.ownerId, this.getOwnerParents(win.ownerId), canvas!, false, win.name, win.title, win.classType, this);
			this.windowImageHolders[win.id].dockMode = win.dockMode!;
			this.windowImageHolders[win.id].dockState = win.dockState;
		}

		const element = this.windowImageHolders[win.id].element;
		const $element = $(element);

		$element.attr('data-id', win.id).css("position", "absolute");
		if (win.ownerId) {
			$element.attr('data-ownerid', win.ownerId);
		}

		this.windowOpening(this.windowImageHolders[win.id]);

		if (this.windowImageHolders[win.id].dockMode === DockMode.autoUndock || this.windowImageHolders[win.id].dockState === DockState.undocked) {
			this.undockWindow(win.id, win.title, this.windowImageHolders[win.id].element, win.posX!, win.posY!, win.width!, win.height!, () => this.windowOpened(this.windowImageHolders[win.id]));
		} else if (win.ownerId && (this.windowImageHolders[win.ownerId] && this.windowImageHolders[win.ownerId] != null) && (this.windowImageHolders[win.ownerId] as CanvasWindow).isRelocated()) {
			this.windowImageHolders[win.ownerId].element.parentNode!.append(element);
			this.windowOpened(this.windowImageHolders[win.id]);
		} else {
			this.api.cfg.rootElement.append(element);
			this.windowOpened(this.windowImageHolders[win.id]);
		}


		if (!this.api.cfg.mirrorMode) {
			this.performActionInternal({ actionName: "", eventType: ActionEventType.init, data: "", windowId: win.id });
		}
	}

	public undockWindow(id: string, title: string | null | undefined, element: HTMLElement, posX: number, posY: number, width: number, height: number, callback?: () => void) {
		if (this.api.cfg.mirrorMode) {
			return;
		}

		let ownerWin: IDockableWindow;
		if (element.parentNode) {
			ownerWin = element.parentNode.ownerDocument!.defaultView as IDockableWindow;
		} else {
			ownerWin = window as IDockableWindow;
		}
		let pagePos = ownerWin.pagePosition;
		if (!pagePos) {
			pagePos = { x: 0, y: 0 };
		}

		let baseUrl = fixConnectionUrl(this.api.cfg.connectionUrl);
		baseUrl = baseUrl.lastIndexOf('/') !== baseUrl.length - 1 ? baseUrl + "/" : baseUrl;

		const popup = window.open("", id, "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes," +
			"width=" + width + "," +
			"height=" + height + "," +
			"top=" + (posY + pagePos.y) + "," +
			"left=" + (posX + pagePos.x)) as IDockableWindow;
		if (!popup) {
			return;
		}

		const canvasWin = this.windowImageHolders[id] as CanvasWindow;

		canvasWin.dockOwner = popup;
		canvasWin.preventDockClose = false;

		popup.createTime = new Date().getTime();
		popup.ignoreDockResizeTimestamp = new Date().getTime();

		popup.document.title = title || "";
		popup.document.body.style.margin = "0";
		popup.document.body.style.width = "100%";
		popup.document.body.style.height = "100%";

		const body = popup.document.body;
		const wrapper = popup.document.createElement("div");
		wrapper.classList.add("suramadu-element");

		const content = popup.document.createElement("div");
		content.classList.add("suramadu-element-content");
		content.classList.add("composition");
		content.setAttribute("role", "application");

		wrapper.appendChild(content);
		body.appendChild(wrapper);

        const sanitized = DOMPurify.sanitize("<link rel='stylesheet' href='" + baseUrl + "css/style.css' type='text/css'>");
		$(popup.document.head).append(sanitized);

		// to allow directdraw.fontProvided
		$(popup.document.head).append(sanitized);

		const fonts = document.querySelectorAll("style[data-dd-ctx]");
		// tslint:disable-next-line: prefer-for-of
		for (let i = 0; i < fonts.length; i++) {
			const font = fonts[i].cloneNode() as Element;
			font.innerHTML = fonts[i].innerHTML;
			body.appendChild(font);
		}

		setTimeout(() => {
			if (!popup) {
				return;
			}
			// timeout to avoid race condition, the popup needs to be ready
			const winHeightOffset = popup.outerHeight - popup.innerHeight - 9;
			const winWidthOffset = popup.outerWidth - popup.innerWidth - 8;
			popup.moveBy(0 - winWidthOffset, 0 - winHeightOffset);

			const origRect = element.getBoundingClientRect();

			for (const winId in this.windowImageHolders) {
				if (this.windowImageHolders.hasOwnProperty(winId)) {
					const cw = this.windowImageHolders[winId] as CanvasWindow;
					if (winId === id || (cw.isInParentHierarchy(id) && cw.dockState !== DockState.undocked)) {
						// undock the canvas window and all of its child windows (with html windows)
						const el = cw.element;
						const elRect = el.getBoundingClientRect();
						const location = { x: elRect.left - origRect.left, y: elRect.top - origRect.top };

						if (el.parentNode) {
							el.parentNode.removeChild(el);
						}

						content.appendChild(el);

						$(el).css({ "left": location.x + 'px', "top": location.y + 'px' });
						if (!cw.htmlWindow) {
							cw.setLocation(location.x, location.y);
							cw.ignoreNextPositioning = true;
						}

						// undock also any internal frames wrapper divs that this window owns
						for (const ifwId in this.internalFrameWrapperHolders) {
							if (this.internalFrameWrapperHolders.hasOwnProperty(ifwId)) {
								const ifw = this.internalFrameWrapperHolders[ifwId];
								if ($(ifw).data("ownerid") === winId) {
									const intEl = ifw;
									const intElRect = intEl.getBoundingClientRect();
									const intLocation = { x: intElRect.left - elRect.left, y: intElRect.top - elRect.top };

									if (intEl.parentNode) {
										intEl.parentNode.removeChild(intEl);
									}

									content.appendChild(intEl);

									$(intEl).css({ "left": intLocation.x + 'px', "top": intLocation.y + 'px' });
								}
							}
						}
					}
				}
			}

			this.api.registerUndockedCanvas(popup);

			this.checkUndockedModalBlocked();

			this.repaint();
			if (callback) {
				callback();
			}
		}, 100);

		popup.onbeforeunload = () => {
			const winE = this.windowImageHolders[id] as CanvasWindow;
			if (winE) {
				if (winE.isModalBlocked()) {
					// if winE is modal blocked, then dock this window instead of closing, otherwise it will not close in swing, hang in javascript closed state and not render
					winE.dock();
				} else if (!winE.preventDockClose) {
					winE.close();
				}
			}
			this.focusedUndockedWindow = null;
		};
		popup.addEventListener("blur", () => {
			this.focusedUndockedWindow = null;
		});
		popup.addEventListener("focus", () => {
			// try focus canvas window with highest z-index contained inside this popup
			let maxZIndex = 0;
			let topCanvasId = id;
			$("canvas.suramadu-canvas", $(popup.document)).each((_, el) => {
				const zIndex = parseInt($(el).css("z-index"), 10);
				if (zIndex > maxZIndex) {
					maxZIndex = zIndex;
					topCanvasId = $(el).data("id");
				}
			});
			if (this.windowImageHolders[topCanvasId]) {
				(this.windowImageHolders[topCanvasId] as CanvasWindow).requestFocus();
			}
			this.focusedUndockedWindow = popup;
		});
		popup.addEventListener("resize", () => {
			popup.ignoreDockResizeTimestamp = new Date().getTime();
			if (!popup) {
				return;
			}
			if (popup.resizeTimer != null) {
				clearTimeout(popup.resizeTimer);
			}
			popup.resizeTimer = setTimeout(() => {
				popup.resizeTimer = null;

				// auto resize canvas to parent window
				const winE = this.windowImageHolders[id];
				const rectP = winE.element.parentElement!.getBoundingClientRect();
				const rectC = winE.element.getBoundingClientRect();
				if (!winE.htmlWindow && (rectC.width !== rectP.width || rectC.height !== rectP.height)) {
					winE.setBounds(0, 0, rectP.width, rectP.height);
				}
			}, 100);
		});
	}

	public dockWindow(canvasWindow: CanvasWindow) {
		if (this.api.cfg.mirrorMode) {
			return;
		}

		const ownerId = canvasWindow.ownerId;
		let parentWin = this.api.cfg.rootElement[0].ownerDocument?.defaultView;
		if (ownerId && (this.windowImageHolders[ownerId] && this.windowImageHolders[ownerId] != null) && (this.windowImageHolders[ownerId] as CanvasWindow).isRelocated()) {
			const parentElement = this.windowImageHolders[ownerId].element.parentNode;
			if (parentElement) {
				parentWin = parentElement.ownerDocument!.defaultView;
			}
		}

		const popup = canvasWindow.dockOwner!;
		const canvasWindowParent = canvasWindow.element.parentNode; // leave this reference here because canvasWindow.element.parentNode changes after it is docked

		for (const winId in this.windowImageHolders) {
			if (this.windowImageHolders.hasOwnProperty(winId)) {
				const canvasWin = this.windowImageHolders[winId];
				if (canvasWin.element.parentNode === canvasWindowParent) {
					const element = canvasWin.element;

					let left = 0;
					let top = 0;
					const rect = element.getBoundingClientRect();

					element.parentNode!.removeChild(element);

					if (parentWin && ownerId && (this.windowImageHolders[ownerId] && this.windowImageHolders[ownerId] != null) && (this.windowImageHolders[ownerId] as CanvasWindow).isRelocated()) {
						// dock back to window's parent if it is relocated (could be detached or undocked in another window)
						left = popup.screenX - parentWin.screenX + rect.left;
						top = popup.screenY - parentWin.screenY + rect.top;

						$(canvasWin.element).css({"left": left + 'px', "top": top + 'px'});
						this.windowImageHolders[ownerId].element.parentNode!.append(element);
					} else {
						// dock back to root parent (the base canvas location)
						let pagePos = popup.pagePosition;
						if (!pagePos) {
							pagePos = { x: 0, y: 0 };
						}
						let parentPagePos = (window as IDockableWindow).pagePosition;
						if (!parentPagePos) {
							parentPagePos = { x: 0, y: 0 };
						}

						const origPos = { x: pagePos.x !== 0 ? pagePos.x : popup.screenX, y: pagePos.y !== 0 ? pagePos.y : popup.screenY };
						const rootRect = this.api.cfg.rootElement[0].getBoundingClientRect();
						left = origPos.x + rect.left - parentPagePos.x - rootRect.left;
						top = origPos.y + rect.top - parentPagePos.y - rootRect.top;

						$(canvasWin.element).css({"left": left + 'px', "top": top + 'px'});
						this.api.cfg.rootElement.append(element);
					}

					$(canvasWin.element).css({ "left": left + 'px', "top": top + 'px' });
					if (!canvasWin.htmlWindow) {
						canvasWin.setLocation(left, top);
					}

					// dock also any internal frames wrapper divs that this window owns
					for (const ifwId in this.internalFrameWrapperHolders) {
						if (this.internalFrameWrapperHolders.hasOwnProperty(ifwId)) {
							const ifw = this.internalFrameWrapperHolders[ifwId];
							if ($(ifw).data("ownerid") === winId) {
								let intLeft = 0;
								let intTop = 0;
								const intRect = ifw.getBoundingClientRect();

								if (parentWin && ownerId && (this.windowImageHolders[ownerId] && this.windowImageHolders[ownerId] != null) && (this.windowImageHolders[ownerId] as CanvasWindow).isRelocated()) {
									// dock back to window's parent if it is relocated (could be detached or undocked in another window)
									intLeft = popup.screenX - parentWin.screenX + intRect.left;
									intTop = popup.screenY - parentWin.screenY + intRect.top;

									this.windowImageHolders[ownerId].element.parentNode!.append(ifw);
								} else {
									// dock back to root parent (the base canvas location)
									let pagePos = popup.pagePosition;
									if (!pagePos) {
										pagePos = { x: 0, y: 0 };
									}
									let parentPagePos = (window as IDockableWindow).pagePosition;
									if (!parentPagePos) {
										parentPagePos = { x: 0, y: 0 };
									}

									const origPos = { x: pagePos.x !== 0 ? pagePos.x : popup.screenX, y: pagePos.y !== 0 ? pagePos.y : popup.screenY };
									const rootRect = this.api.cfg.rootElement[0].getBoundingClientRect();
									intLeft = origPos.x + intRect.left - parentPagePos.x - rootRect.left;
									intTop = origPos.y + intRect.top - parentPagePos.y - rootRect.top;

									this.api.cfg.rootElement.append(ifw);
								}

								$(ifw).css({ "left": intLeft + 'px', "top": intTop + 'px' });
							}
						}
					}
				}
			}
		}

		canvasWindow.dockOwner = null;

		canvasWindow.preventDockClose = true;
		this.focusedUndockedWindow = null;
		popup.close();

		this.checkUndockedModalBlocked();
	}

	public visibilityChangeHandler() {
		if (!this.hasUndockedWindows()) {
			return;
		}

		const hidden = document.visibilityState === "hidden";
		for (const winId in this.windowImageHolders) {
			if (this.windowImageHolders.hasOwnProperty(winId)) {
				const popup = this.windowImageHolders[winId].dockOwner;
				if (popup != null) {
					if (hidden) {
						this.api.showOverlay(popup, this.api.dialogs.dockingVisibilityOverlay);
					} else {
						this.api.hideOverlay(popup, this.api.dialogs.dockingVisibilityOverlay);
					}
				}
			}
		}
	}

	public checkUndockedModalBlocked() {
		let anyUndocked = false;
		const blockedWindows = [];
		let modalWindow: Window | null = null;

		for (const winId in this.windowImageHolders) {
			if (this.windowImageHolders.hasOwnProperty(winId)) {
				const popup = this.windowImageHolders[winId].dockOwner;
				if (popup != null) {
					anyUndocked = true;

					if ($(popup.document).find(".suramadu-canvas:not(.modal-blocked)").length === 0) {
						this.api.showOverlay(popup, this.api.dialogs.dockingModalityOverlay);
						blockedWindows.push($(popup.document).find(".modality-overlay"));
					} else {
						this.api.hideOverlay(popup, this.api.dialogs.dockingModalityOverlay);
						modalWindow = popup;
					}
				}
			}
		}

		if (!anyUndocked) {
			this.api.hideOverlay(window, this.api.dialogs.dockingModalityOverlay);
			return;
		}

		if (this.api.cfg.rootElement.find(".suramadu-canvas:not([data-id=canvas])").length > 0
			&& this.api.cfg.rootElement.find(".suramadu-canvas:not(.modal-blocked):not([data-id=canvas])").length === 0) {
			this.api.showOverlay(window, this.api.dialogs.dockingModalityOverlay);
			blockedWindows.push($(document).find(".modality-overlay"));
		} else {
			this.api.hideOverlay(window, this.api.dialogs.dockingModalityOverlay);
		}

		if (blockedWindows.length > 0 && modalWindow != null) {
			for (const blockedWindow of blockedWindows) {
				blockedWindow.find("button").on("click", () => {
					// this focuses a window, do not use focusManager here
					modalWindow!.focus();
				});
			}
		}
	}

	public hasUndockedWindows() {
		for (const winId in this.windowImageHolders) {
			if (this.windowImageHolders[winId].dockOwner != null) {
				return true;
			}
		}
		return false;
	}

	public getFocusedWindow() {
		if (this.focusedUndockedWindow != null) {
			return this.focusedUndockedWindow!;
		}
		return window;
	}

	public getAllWindows() {
		const wins = Array<Window>();

		for (const winId in this.windowImageHolders) {
			if (this.windowImageHolders[winId].dockOwner != null) {
				wins.push(this.windowImageHolders[winId].dockOwner!);
			}
		}

		wins.push(window);

		return wins;
	}

	public getMainWindowVisibilityState() {
		return document.visibilityState;
	}

	public handleWindowModalityAndBounds(win: appFrameProtoOut.IWindowMsgOutProto, htmlOrCanvasWin : HtmlWindow|CanvasWindow, index: number, pngDraw: boolean) {
		const $element = $(htmlOrCanvasWin.element);

		if (htmlOrCanvasWin.htmlWindow && $element.is(".close-prevented")) {
			// call window listeners for close-prevented HtmlWindow
			this.windowOpening(htmlOrCanvasWin);
			$element.show().removeClass("close-prevented");
			this.windowOpened(htmlOrCanvasWin);
			if (!this.api.cfg.mirrorMode) {
				this.performActionInternal({ actionName: "", eventType: ActionEventType.init, data: "", windowId: win.id });
			}
		}

		if (pngDraw && win.type === WindowType.basic) {
			if (Math.abs($element.width()! - win.width!) >= 1 || Math.abs($element.height()! - win.height!) >= 1) {
				$element.css({ "width": win.width + 'px', "height": win.height + 'px' });
				$element.attr("width", win.width!).attr("height", win.height!);
			}
		}

		$element.css({ "z-index": (compositionBaseZIndex + index + 1), "width": win.width + 'px', "height": win.height + 'px' }).show();
		if ($element.is(".modal-blocked") !== win.modalBlocked) {
			$element.toggleClass("modal-blocked", win.modalBlocked!);
			this.windowModalBlockedChanged(htmlOrCanvasWin);
		}

		if (this.isVisible(htmlOrCanvasWin.element.parentNode as HTMLElement) && !htmlOrCanvasWin.ignoreNextPositioning) {
			$element.css({ "left": win.posX + 'px', "top": win.posY + 'px' });
			htmlOrCanvasWin.validatePositionAndSize(win.posX!, win.posY!);
		}

		htmlOrCanvasWin.ignoreNextPositioning = false;
	}

	public handleWindowState(state: number | null | undefined, htmlOrCanvasWin: HtmlWindow|CanvasWindow) {
		if (htmlOrCanvasWin.htmlWindow || typeof state === 'undefined') {
			return;
		}

		htmlOrCanvasWin.state = state!;

		if (state !== JFRAME_MAXIMIZED_STATE || this.api.cfg.mirrorMode || !htmlOrCanvasWin.element.parentNode) {
			return;
		}

		// window has been maximized, we need to set its bounds according to its parent node (could be detached)
		const rectP = (htmlOrCanvasWin.element.parentNode as HTMLElement).getBoundingClientRect();
		const rectC = htmlOrCanvasWin.element.getBoundingClientRect();
		if (rectC.width !== rectP.width || rectC.height !== rectP.height) {
			htmlOrCanvasWin.setBounds(0, 0, rectP.width, rectP.height);
		}
	}

	public handleWindowDockState(dockState:appFrameProtoOut.WindowMsgOutProto.DockStateProto| null | undefined, htmlOrCanvasWin: HtmlWindow|CanvasWindow) {
		if (this.api.cfg.mirrorMode || htmlOrCanvasWin.htmlWindow || typeof dockState === 'undefined') {
			return;
		}

		if (htmlOrCanvasWin.dockState === dockState) {
			return;
		}

		htmlOrCanvasWin.dockState = dockState;
		if (dockState === DockState.undocked) {
			const rect = htmlOrCanvasWin.element.getBoundingClientRect();
			this.undockWindow(htmlOrCanvasWin.id, htmlOrCanvasWin.title, htmlOrCanvasWin.element, rect.left, rect.top, rect.width, rect.height);
		} else {
			this.dockWindow(htmlOrCanvasWin);
		}
	}

	public drawInternalWindows(internalWindows: appFrameProtoOut.IWindowMsgOutProto[] | null | undefined) {
		if (!internalWindows || internalWindows == null || internalWindows.length === 0) {
			return;
		}

		const intWins = [];

		for (let i = internalWindows.length - 1; i >= 0; i--) {
			const intWin = internalWindows[i];

			if (intWin.type === WindowType.internalWrapper) {
				this.handleInternalWrapperWindow(intWin);
			} else if (intWin.type === WindowType.internal || intWin.type === WindowType.internalHtml) {
				intWins.push(intWin);
			}
		}

		const underlyingHtmlWindows = [];
		for (let i = 0; i < intWins.length; i++) {
			const intWin = intWins[i];

			if (intWin.type === WindowType.internal) {
				this.handleInternalWindow(intWin, i, underlyingHtmlWindows);
			} else if (intWin.type === WindowType.internalHtml) {
				this.handleInternalHtmlWindow(intWin, i);
				underlyingHtmlWindows.push(intWin);
			}
		}
	}

	public handleInternalWrapperWindow(win: appFrameProtoOut.IWindowMsgOutProto) {
		let wrapper = this.internalFrameWrapperHolders[win.id];
		if (!wrapper || wrapper == null) {
			wrapper = $("<div class='internal-frames-wrapper' id='wrapper-" + win.id + "' />")[0];
			if (win.ownerId && (this.windowImageHolders[win.ownerId] && this.windowImageHolders[win.ownerId] != null) && (this.windowImageHolders[win.ownerId] as CanvasWindow).isRelocated()) {
				this.windowImageHolders[win.ownerId].element.parentNode!.append(wrapper);
			} else {
				this.api.cfg.rootElement.append($(wrapper));
			}
			this.internalFrameWrapperHolders[win.id] = wrapper;
		}

		if (win.ownerId) {
			$(wrapper).attr("data-ownerid", win.ownerId);

			if (this.windowImageHolders[win.ownerId]) {
				const parent = $(this.windowImageHolders[win.ownerId].element);
				$(wrapper).css({
					"z-index": parent.css("z-index"),
					"left": win.posX + "px",
					"top": win.posY + "px",
					"width": win.width + "px",
					"height": win.height + "px"
				});
			}
		}
	}

	public handleInternalWindow(win: appFrameProtoOut.IWindowMsgOutProto, index: number, underlyingHtmlWindows: appFrameProtoOut.IWindowMsgOutProto[]) {
		const wrapper = this.internalFrameWrapperHolders[win.ownerId!];
		if (!wrapper || wrapper == null) {
			// wait for the parent wrapper to be attached first and render this window in next cycle
			return;
		}

		let canvas: HTMLCanvasElement;

		if (typeof this.windowImageHolders[win.id] === 'undefined' || this.windowImageHolders[win.id] == null) {
			canvas = document.createElement("canvas");
			canvas.classList.add("suramadu-canvas");
			canvas.classList.add("internal");

			this.windowImageHolders[win.id] = new CanvasWindow(win.id, win.ownerId, this.getOwnerParents(win.ownerId), canvas, true, win.name, win.title, win.classType, this);

			$(canvas).attr('data-id', win.id).css("position", "absolute");
			if (win.ownerId) {
				$(canvas).attr('data-ownerid', win.ownerId);
			}

			$(wrapper).append(canvas);
		} else {
			canvas = this.windowImageHolders[win.id].element as HTMLCanvasElement;
		}

		const parentPos = $(wrapper).position();
		$(canvas).css({
			"z-index": (compositionBaseZIndex + index + 1),
			"left": (win.posX! - parentPos.left) + "px",
			"top": (win.posY! - parentPos.top) + "px",
			"width": win.width + "px",
			"height": win.height + "px"
		});
		$(canvas).attr("width", win.width! * getDpr()).attr("height", win.height! * getDpr());

		if ($(canvas).is(".modal-blocked") !== win.modalBlocked) {
			$(canvas).toggleClass("modal-blocked", win.modalBlocked!);
		}

		if (underlyingHtmlWindows.length > 0) {
			const ownerCanvasId = $(wrapper).data("ownerid");
			if (ownerCanvasId && this.windowImageHolders[ownerCanvasId] && this.windowImageHolders[ownerCanvasId].element) {
				const src = this.windowImageHolders[ownerCanvasId].element as HTMLCanvasElement;
				const ctx = canvas.getContext("2d")!;
				const pos = $(src).position();

				for (const underlyingHtmlWindow of underlyingHtmlWindows) {
					const int = this.findWindowIntersection(win, underlyingHtmlWindow);
					if (int && (int.x2 - int.x1 > 0) && (int.y2 - int.y1 > 0)) {
						const width = (int.x2 - int.x1) * getDpr();
						const height = (int.y2 - int.y1) * getDpr();
						ctx.drawImage(src, (int.x1 - pos.left) * getDpr(), (int.y1 - pos.top) * getDpr(), width, height, (win.posX! < int.x1 ? (int.x1 - win.posX!) : 0) * getDpr(), (win.posY! < int.y1 ? (int.y1 - win.posY!) : 0) * getDpr(), width, height);
					}
				}
			}
		}
	}

	public handleInternalHtmlWindow(win: appFrameProtoOut.IWindowMsgOutProto, index: number) {
		const wrapper = this.internalFrameWrapperHolders[win.ownerId!];
		if (!wrapper || wrapper == null) {
			// wait for the parent wrapper to be attached first and render this window in next cycle
			return;
		}

		let htmlDiv;
		let newWindowOpened = false;

		if (typeof this.windowImageHolders[win.id] === 'undefined' || this.windowImageHolders[win.id] == null) {
			htmlDiv = document.createElement("div");
			htmlDiv.classList.add("suramadu-html-canvas");

			this.windowImageHolders[win.id] = new HtmlWindow(win.id, win.ownerId, this.getOwnerParents(win.ownerId), htmlDiv, win.name, this);
			newWindowOpened = true;
			$(htmlDiv).attr('data-id', win.id).css("position", "absolute");
			if (win.ownerId) {
				$(htmlDiv).attr('data-ownerid', win.ownerId);
			}

			this.windowOpening(this.windowImageHolders[win.id]);

			$(wrapper).append(htmlDiv);
		} else {
			htmlDiv = this.windowImageHolders[win.id].element;
			if ($(htmlDiv).is(".close-prevented")) {
				newWindowOpened = true;
				this.windowOpening(this.windowImageHolders[win.id]);
				$(htmlDiv).removeClass("close-prevented").show();
			}
		}

		if (newWindowOpened) {
			this.windowOpened(this.windowImageHolders[win.id]);
			if (!this.api.cfg.mirrorMode) {
				this.performActionInternal({ actionName: "", eventType: ActionEventType.init, data: "", windowId: win.id });
			}
		}

		const parentPos = $(wrapper).position();
		$(htmlDiv).css({
			"z-index": (compositionBaseZIndex + index + 1),
			"left": (win.posX! - parentPos.left) + "px",
			"top": (win.posY! - parentPos.top) + "px",
			"width": win.width + "px",
			"height": win.height + "px"
		});
		$(htmlDiv).attr("width", win.width!).attr("height", win.height!).show();

		if ($(htmlDiv).is(".modal-blocked") !== win.modalBlocked) {
			$(htmlDiv).toggleClass("modal-blocked", win.modalBlocked!);
		}
	}

	public findWindowIntersection(win1: appFrameProtoOut.IWindowMsgOutProto, win2: appFrameProtoOut.IWindowMsgOutProto) {
		const r1Temp = { x1: win1.posX, y1: win1.posY, x2: win1.posX! + win1.width!, y2: win1.posY! + win1.height! };
		const r2Temp = { x1: win2.posX, y1: win2.posY, x2: win2.posX! + win2.width!, y2: win2.posY! + win2.height! };

		const [r1, r2] = [r1Temp, r2Temp].map(r => {
			return { x: [r.x1!, r.x2!].sort(this.sortNumber), y: [r.y1!, r.y2!].sort(this.sortNumber) };
		});

		const noIntersect = r2.x[0] > r1.x[1] || r2.x[1] < r1.x[0] ||
			r2.y[0] > r1.y[1] || r2.y[1] < r1.y[0];

		return noIntersect ? false : {
			x1: Math.max(r1.x[0], r2.x[0]), // _[0] is the lesser,
			y1: Math.max(r1.y[0], r2.y[0]), // _[1] is the greater
			x2: Math.min(r1.x[1], r2.x[1]),
			y2: Math.min(r1.y[1], r2.y[1])
		};
	}

	public sortNumber(a: number, b: number) {
		return a - b;
	}

	public isVisible(element: HTMLElement) {
		if (!element || element == null) {
			return false;
		}

		return !!(element.offsetWidth || element.offsetHeight || element.getClientRects().length);
	}

	public findCanvasWindowsById(winId: string) {
		let wins = $();

		for (const id in this.windowImageHolders) {
			if (this.windowImageHolders.hasOwnProperty(id)) {
				const canvasWin = this.windowImageHolders[id];
				if (canvasWin.id === winId) {
					wins = wins.add($(canvasWin.element));
				}
			}
		}

		for (const id in this.internalFrameWrapperHolders) {
			if (this.internalFrameWrapperHolders.hasOwnProperty(id)) {
				const ifw = this.internalFrameWrapperHolders[id];
				if ($(ifw).data("ownerid") === winId) {
					wins = wins.add($(ifw));
				}
			}
		}

		return wins;
	}

	public validateAndPositionWindow(htmlOrCanvasWin: HtmlWindow | CanvasWindow, winPosX: number, winPosY: number) {
		if (htmlOrCanvasWin.htmlWindow || this.api.cfg.mirrorMode) {
			return;
		}

		const threshold = 40;
		let overrideLocation = false;
		const rect = (htmlOrCanvasWin.element.parentNode as HTMLElement).getBoundingClientRect();
		const rectE = htmlOrCanvasWin.element.getBoundingClientRect();

		if (winPosX > rect.width - threshold) {
			winPosX = Math.max(0, rect.width - threshold);
			overrideLocation = true;
		}
		if (winPosY > rect.height - threshold) {
			winPosY = Math.max(0, rect.height - threshold);
			overrideLocation = true;
		}
		if (winPosX < ((rectE.width - threshold) * (-1))) {
			winPosX = ((rectE.width - threshold) * (-1));
			overrideLocation = true;
		}
		if (winPosY < 0) {
			winPosY = 0;
			overrideLocation = true;
		}

		if (overrideLocation) {
			$(htmlOrCanvasWin.element).css({ "left": winPosX + 'px', "top": winPosY + 'px' });
			htmlOrCanvasWin.setLocation(winPosX, winPosY);
		}
	}

	public getOwnerParents(ownerId?: string | null) {
		if (!ownerId || ownerId == null) {
			return [];
		}

		const ownerWin = this.windowImageHolders[ownerId];

		if (!ownerWin || ownerWin == null) {
			return [];
		}

		if (!ownerId) {
			return [];
		}

		const parents = ownerWin.parents.slice();
		parents.push(ownerId);

		return parents;
	}

	public adjustCanvasSize(canvas: HTMLCanvasElement, width: number, height: number) {
		if (canvas.width !== width * getDpr() || canvas.height !== height * getDpr()) {
			const ctx : CanvasRenderingContext2D = canvas.getContext("2d")!;
			let snapshot;
			if (canvas.width !== 0 && canvas.height !== 0) {
				snapshot = ctx.getImageData(0, 0, canvas.width, canvas.height);
			}
			canvas.width = width * getDpr();
			canvas.height = height * getDpr();
			canvas.style.width = width + 'px';
			canvas.style.height = height + 'px';
			if (snapshot != null) {
				ctx.putImageData(snapshot, 0, 0);
			}
		}
	}

	public clear(x: number, y: number, w: number, h: number, context: CanvasRenderingContext2D) {
		const dpr = getDpr();
		context.clearRect(x * dpr, y * dpr, w * dpr, h * dpr);
	}

	public copy(sx: number, sy: number, dx: number, dy: number, w: number, h: number, context: CanvasRenderingContext2D) {
		const dpr = getDpr();
		const copy = context.getImageData(sx * dpr, sy * dpr, w * dpr, h * dpr);
		context.putImageData(copy, dx * dpr, dy * dpr);
	}

	public getCursorStyle(cursorMsg: appFrameProtoOut.ICursorChangeEventMsgOutProto) {
		if (cursorMsg.b64img == null) {
			return cursorMsg.cursor;
		} else {
			if (this.api.cfg.ieVersion) {
				return 'url(\'' + this.api.cfg.connectionUrl + 'file?id=' + cursorMsg.curFile + '\'), auto';
			} else {
				const data = getImageString(cursorMsg.b64img);
				return 'url(' + data + ') ' + cursorMsg.x + ' ' + cursorMsg.y + ' , auto';
			}
		}
	}

	public getHandShake(): commonProto.IConnectionHandshakeMsgInProto {
		if (window.name.substr(0, 4) !== 'tid_') {
			window.name = 'tid_' + GUID();
		}
		const handshake: commonProto.IConnectionHandshakeMsgInProto = {
			instanceId: this.api.getInstanceId(),
			browserId: this.api.cfg.browserId,
			viewId: this.api.cfg.viewId,
			tabId: window.name,
			locale: this.api.getLocale(),
			timeZone: getTimeZone(),
			mirrored: this.api.cfg.mirrorMode,
			directDrawSupported: this.api.cfg.typedArraysSupported && !(this.api.cfg.ieVersion && (this.api.cfg.ieVersion as number) <= 10),
			dockingSupported: !this.api.cfg.ieVersion,
			touchMode: this.api.cfg.touchMode,
			accessiblityEnabled: this.api.isAccessiblityEnabled(),
			url: window.location.href
		};

		if (!this.api.cfg.mirrorMode) {
			const desktopSize = this.api.getDesktopSize();
			handshake.documentBase = this.api.cfg.documentBase;
			handshake.params = this.api.cfg.params;
			handshake.desktopWidth = desktopSize.width;
			handshake.desktopHeight = desktopSize.height;
		}
		return handshake;
	}

	public getPixels(request: appFrameProtoOut.IPixelsAreaRequestMsgOutProto) {
		const result = document.createElement("canvas");
		result.width = request.w!;
		result.height = request.h!;
		const resctx = result.getContext("2d")!;

		if (this.compositingWM) {
			const wins = this.getWindows(true);
			wins.sort((a, b) => (a.element.style.zIndex > b.element.style.zIndex) ? 1 : ((a.element.style.zIndex < b.element.style.zIndex) ? -1 : 0));
			for (const win of wins) {
				if (!win.htmlWindow) {
					const rect = win.element.getBoundingClientRect();
					if ((rect.left > request.x! + request.w!) || (rect.top > request.y! + request.h!)
						|| (request.x! > rect.left + rect.width) || (request.y! > rect.top + rect.height)) {
						continue;
					}
					const left = Math.max(request.x!, rect.left);
					const top = Math.max(request.y!, rect.top);
					const width = Math.min(request.x! + request.w! - left, rect.left + rect.width - left);
					const height = Math.min(request.y! + request.h! - top, rect.top + rect.height - top);
					resctx.putImageData((win.element as HTMLCanvasElement).getContext("2d")!.getImageData(left - rect.left, top - rect.top, width, height), left - request.x!, top - request.y!);
				}
			}
		} else {
			resctx.putImageData(this.api.getCanvas().getContext("2d")!.getImageData(request.x!, request.y!, request.w!, request.h!), 0, 0);
		}

		const dataurl = result.toDataURL("image/png");
		return {
			pixelsResponse: {
				correlationId: request.correlationId,
				pixels: dataurl
			}
		};
	}

	public errorHandler(error: Error) {
		this.drawingLock = null;
		throw error;
	}

	public getWindows(canvasOnly: boolean) {
		if (!this.compositingWM) {
			// compositingWM only
			return [];
		}

		const wins = [];
		for (const id in this.windowImageHolders) {
			if (canvasOnly && this.windowImageHolders[id].htmlWindow) {
				continue;
			}
			wins.push(this.windowImageHolders[id]);
		}

		return wins;
	}

	public getWindowById(winId: string) {
		if (!this.compositingWM) {
			// compositingWM only
			return;
		}

		return this.windowImageHolders[winId];
	}

	public performAction(options: {actionName?: string, data?: string, binaryData?: Uint8Array, windowId?: string}) {
		this.performActionInternal($.extend({ "eventType": ActionEventType.user }, options));
	}

	public performActionInternal(options: {actionName?: string, eventType?: appFrameProtoIn.ActionEventMsgInProto.ActionEventTypeProto, data?: string, binaryData?: Uint8Array, windowId?: string}) {
		this.api.send({
			action: {
				actionName: options.actionName,
				eventType: options.eventType,
				data: options.data || "",
				binaryData: options.binaryData || null,
				windowId: options.windowId || ""
			}
		});
	}

	public windowOpening(htmlOrCanvasWindow: CanvasWindow|HtmlWindow) {
		try {
			if (this.api.cfg.compositingWindowsListener && this.api.cfg.compositingWindowsListener.windowOpening) {
				this.api.cfg.compositingWindowsListener.windowOpening(htmlOrCanvasWindow);
			}
		} catch (e) {
			console.error(e);
		}
	}

	public windowOpened(htmlOrCanvasWindow: CanvasWindow|HtmlWindow) {
		try {
			if (this.api.cfg.compositingWindowsListener && this.api.cfg.compositingWindowsListener.windowOpened) {
				this.api.cfg.compositingWindowsListener.windowOpened(htmlOrCanvasWindow);
			}
		} catch (e) {
			console.error(e);
		}
	}

	public windowClosing(htmlOrCanvasWindow: CanvasWindow|HtmlWindow, windowCloseEvent: WindowCloseEvent) {
		try {
			if (this.api.cfg.compositingWindowsListener && this.api.cfg.compositingWindowsListener.windowClosing) {
				this.api.cfg.compositingWindowsListener.windowClosing(htmlOrCanvasWindow, windowCloseEvent);
			}
		} catch (e) {
			console.error(e);
		}
	}

	public windowClosed(htmlOrCanvasWindow: CanvasWindow|HtmlWindow) {
		try {
			if (this.api.cfg.compositingWindowsListener && this.api.cfg.compositingWindowsListener.windowClosed) {
				this.api.cfg.compositingWindowsListener.windowClosed(htmlOrCanvasWindow);
			}
		} catch (e) {
			console.error(e);
		}
	}

	public windowModalBlockedChanged(htmlOrCanvasWindow: CanvasWindow|HtmlWindow) {
		try {
			if (this.api.cfg.compositingWindowsListener && this.api.cfg.compositingWindowsListener.windowModalBlockedChanged) {
				this.api.cfg.compositingWindowsListener.windowModalBlockedChanged(htmlOrCanvasWindow);
			}
		} catch (e) {
			console.error(e);
		}

		this.checkUndockedModalBlocked();
	}

	// tslint:disable-next-line: variable-name
	public handleActionEvent(_actionName?: string | null, _data?: string | null, _binaryData?: Uint8Array | null) {
		// to be customized
	}

	public getApi() {
		return this.api;
	}

}

// tslint:disable-next-line: max-classes-per-file
export class WindowCloseEvent {
	private defaultPrevented: boolean;

	constructor(
		public id: string
	) {
		this.defaultPrevented = false;
	}

	public preventDefault = () => {
		this.defaultPrevented = true;
	}

	public isDefaultPrevented = () => {
		return this.defaultPrevented;
	}
}

// tslint:disable-next-line: max-classes-per-file
class CanvasWindow {
	public readonly htmlWindow: boolean = false;
	public state: number = 0;
	public dockMode = DockMode.none;
	public dockState :appFrameProtoOut.WindowMsgOutProto.DockStateProto| null | undefined= DockState.docked;
	public dockOwner: IDockableWindow | null = null;
	public preventDockClose: boolean = false;
	public ignoreNextPositioning = false;
	public validatePositionAndSize: (x: number, y: number) => void;
	public webswingInstance: IInjected<{ start: "webswing.start"; disconnect: "webswing.disconnect"; configure: "webswing.configure"; kill: "base.kill"; setControl: "webswing.setControl"; repaint: "base.repaint"; instanceId: "socket.instanceId"; requestComponentTree: "base.requestComponentTree"; getWindows: "base.getWindows"; getWindowById: "base.getWindowById"; performAction: "base.performAction"; }>;

	constructor(
		public id: string,
		public ownerId: string | null | undefined,
		public parents: string[],
		public element: HTMLCanvasElement,
		public internal: boolean,
		public name: string | null | undefined,
		public title: string | null | undefined,
		public classType: (appFrameProtoOut.WindowMsgOutProto.WindowClassTypeProto | null | undefined),
		private baseModule: BaseModule,
	) {
		this.webswingInstance = baseModule.getApi().getExternalApi()
		this.validatePositionAndSize = (x: number, y: number) => {
			baseModule.validateAndPositionWindow(this, x, y);
		}
	}

	public isInParentHierarchy(winId: string) {
		if (!this.parents || this.parents.length === 0) {
			return false;
		}
		for (const parent of this.parents) {
			if (parent === winId) {
				return true;
			}
		}
		return false;
	}
	public isModalBlocked() {
		return this.element.classList.contains('modal-blocked');
	}

	public getDockMode() {
		return this.dockMode;
	};

	public getDockState() {
		return this.dockState;
	};

	public setBounds(x: number, y: number, width: number, height: number) {
		this.baseModule.getApi().send({ window: { id: this.id, x: Math.floor(x), y: Math.floor(y), width: Math.floor(width), height: Math.floor(height) } });
	};

	public setLocation(x: number, y: number) {
		const rect = this.element.getBoundingClientRect();
		this.baseModule.getApi().send({ window: { id: this.id, x: Math.floor(x), y: Math.floor(y), width: Math.floor(rect.width), height: Math.floor(rect.height) } });
	};

	public setSize(width: number, height: number) {
		const rect = this.element.getBoundingClientRect() as DOMRect;
		this.baseModule.getApi().send({ window: { id: this.id, x: Math.floor(rect.x + document.body.scrollLeft), y: Math.floor(rect.y + document.body.scrollTop), width: Math.floor(width), height: Math.floor(height) } });
	};

	public detach() {
		if (!this.element.parentNode) {
			console.error("Cannot detach window, it is not attached to any parent!");
			return;
		}

		return this.element.parentNode.removeChild(this.element);
	};

	public attach(parent: Element, pos: {x: number, y: number}) {
		if (this.element.parentNode) {
			console.error("Cannot attach window, it is still attached to another parent!");
			return;
		}

		if (parent) {
			parent.append(this.element);
			if (pos) {
				this.setLocation(pos.x, pos.y);
			}
		}
	};

	public undock() {
		if (this.dockMode === DockMode.none || this.dockState === DockState.undocked) {
			return;
		}

		this.baseModule.getApi().send({window: {id: this.id, eventType: WindowEventType.undock}});
	};

	public dock() {
		if (this.dockMode === DockMode.none || this.dockState === DockState.docked || this.dockOwner == null) {
			return;
		}

		this.baseModule.getApi().send({window: {id: this.id, eventType: WindowEventType.dock}});

	};

	public toggleDock() {
		if (this.dockState === DockState.docked) {
			this.undock();
		} else {
			this.dock();
		}
	};

	public maximize() {
		this.baseModule.getApi().send({ window: { id: this.id, eventType: WindowEventType.maximize } });
	}

	public setUndecorated(undecorated: boolean) {
		if (undecorated) {
			this.baseModule.getApi().send({window: {id: this.id, eventType: WindowEventType.undecorate}});
		} else {
			this.baseModule.getApi().send({window: {id: this.id, eventType: WindowEventType.decorate}});
		}	}

	public isRelocated() {
		return this.element.parentNode !== this.baseModule.getApi().cfg.rootElement[0];
	}

	public close() {
		this.baseModule.closeWindow(this.id);
	};

	public requestFocus() {
		this.baseModule.getApi().send({window: {id: this.id, eventType: WindowEventType.focus}});
	};

	public performAction(options: {actionName?: string, data?: string, binaryData?: Uint8Array}) {
		this.baseModule.performAction($.extend({ "windowId": this.id }, options));
	}

	// tslint:disable-next-line: variable-name
	public handleActionEvent(_actionName?: string | null, _data?: string | null, _binaryData?: Uint8Array | null) {
		// to be customized
	}

	// tslint:disable-next-line: variable-name
	public windowClosing(_windowCloseEvent: WindowCloseEvent) {
		// to be customized
	}

	public windowClosed() {
		// to be customized
	}
}

// tslint:disable-next-line: max-classes-per-file
class HtmlWindow {

	public readonly htmlWindow = true;
	public readonly internal = false;
	public readonly dockOwner = null;
	public dockMode = DockMode.none;
	public dockState :appFrameProtoOut.WindowMsgOutProto.DockStateProto| null | undefined= undefined;
	public ignoreNextPositioning = false;
	public webswingInstance: IInjected<{ start: "webswing.start"; disconnect: "webswing.disconnect"; configure: "webswing.configure"; kill: "base.kill"; setControl: "webswing.setControl"; repaint: "base.repaint"; instanceId: "socket.instanceId"; requestComponentTree: "base.requestComponentTree"; getWindows: "base.getWindows"; getWindowById: "base.getWindowById"; performAction: "base.performAction"; }>;

	constructor(
		public id: string,
		public ownerId: string | null | undefined,
		public parents: string[],
		public element: HTMLElement,
		public name: string | null | undefined,
		private baseModule: BaseModule) {
		this.webswingInstance = baseModule.getApi().getExternalApi();
	}

	public validatePositionAndSize(x: number, y: number) {
		this.baseModule.validateAndPositionWindow(this, x, y);
	}

	public isInParentHierarchy(winId: string) {
		if (!this.parents || this.parents.length === 0) {
			return false;
		}
		for (const parent of this.parents) {
			if (parent === winId) {
				return true;
			}
		}
		return false;
	}

	public isModalBlocked() {
		return this.element.classList.contains('modal-blocked');
	}

	public performAction(options: {actionName?: string, data?: string, binaryData?: Uint8Array, windowId?: string}) {
		this.baseModule.performAction($.extend({ "windowId": this.id }, options));
	}

	public dispose() {
		$(this.element).remove();
		delete this.baseModule.windowImageHolders[this.id];
		this.baseModule.repaint();
	}

	// tslint:disable-next-line: variable-name
	public handleActionEvent(_actionName?: string | null, _data?: string | null, _binaryData?: Uint8Array | null) {
		// to be customized
	}

	// tslint:disable-next-line: variable-name
	public windowClosing(_windowCloseEvent: WindowCloseEvent) {
		// to be customized
	}

	public windowClosed() {
		// to be customized
	}
}