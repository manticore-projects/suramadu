import html from './templates/touch.html';
import { isIOS, getImageString, getDpr, isTouchDevice } from './suramadu-util';
import { ModuleDef } from './suramadu-inject';
import { appFrameProtoIn } from './proto/proto.in';
import { appFrameProtoOut } from './proto/proto.out';

const KeyEventType = appFrameProtoIn.KeyboardEventMsgInProto.KeyEventTypeProto;
const MouseEventType = appFrameProtoIn.MouseEventMsgInProto.MouseEventTypeProto;
const CopyEventMsgType = appFrameProtoIn.CopyEventMsgInProto.CopyEventMsgTypeProto;

export const touchInjectable = {
	cfg: 'webswing.config' as const,
	send: 'socket.send' as const,
	sendHandshake: 'base.handshake' as const,
	getCanvas: 'canvas.get' as const,
	getRootWidth: 'canvas.width' as const,
	getRootHeight: 'canvas.height' as const,
	displayPasteDialog: 'clipboard.displayPasteDialog' as const,
	translate: 'translate.translate' as const,
	repaint: 'base.repaint' as const,
	showBar: 'dialog.showBar' as const,
	dialogs: 'dialog.content' as const,
	hasUndockedWindows: 'base.hasUndockedWindows' as const,
	focusDefault: 'focusManager.focusDefault' as const
}

export interface ITouchService {
	'touch.register': (config: ITouchBarConfig) => void,
	'touch.cursorChanged': (cursorMsg: appFrameProtoOut.ICursorChangeEventMsgOutProto) => void,
	'touch.inputFocusLost': () => void,
	'touch.touchBarConfig': ITouchBarConfig,
	'touch.touchBarEnabled': () => boolean,
	'touch.switchMode': (doSwitch: boolean, dontAsk: boolean) => void,
	'touch.dispose': () => void,
	'touch.correctPosition': () => void,
	'touch.refreshScale': () => void
}

interface ITouchBarConfig {
	scalingEnabled: boolean,
	staticScaleEnabled: boolean,
	buttons: {
		left: IButton[]
		center: IButton[]
		right: IButton[]
		defaultButtons: {
			toggleMode: IButton
			switchToDesktop: IButton
			copy: IButton
			cut: IButton
			paste: IButton
			keyboard: IButton
			fullscreen: IButton
		}
	}
}

interface IButton {
	enabled: boolean
	label: string
	title: string
	hidden: boolean
	action: (button: JQuery<HTMLElement>) => void
}
export class TouchModule extends ModuleDef<typeof touchInjectable, ITouchService> {

	private canvas?: HTMLCanvasElement;
	private input?: JQuery<HTMLInputElement>;
	private cursor?: JQuery<HTMLElement>;
	private touchBar?: JQuery<HTMLElement>;
	private registered = false;
	private tapStarted = 0;
	private dragStarted = 0;
	private dragging = false;
	private scaling = false;
	private lastScaleDist = 0;
	private touchStartY = 0;
	private touchStartX = 0;
	private inputEvtQueue: appFrameProtoIn.IInputEventMsgInProto[] = [];
	private latestMouseWheelEvent?: appFrameProtoIn.IInputEventMsgInProto;
	private latestMouseMoveEvent?: appFrameProtoIn.IInputEventMsgInProto;
	private sendInterval?: number;
	private canvasResizeInterval?: number;
	private scaleHideTimer?: number;
	private toastTimer?: number;
	private tapDelayThreshold = 750;
	private doubleTapDelayThreshold = 500;
	private lastTap = 0;
	private tapMoveThreshold = 10;
	private longPressTimeout?: number;
	private longPressStartTimeout?: number;
	private lastRepaint = 0;
	private repaintTimeout?: number;
	private isKeyboardProbablyShowing = false;
	private focusCounter = 0;
	private scaleStartScrollLeft = 0;
	private scaleStartScrollTop = 0;
	private scaleStartPoints: Array<{ x: number, y: number }> = [];
	private scaleStartDim: { width?: number, height?: number } = {};
	private maxScale = 3;
	private scaleSpeed = 10;
	private compositionText = "";
	private composing = false;
	private backspaceKeyDown = false;
	private scrollMode: 'drag' | 'swipe' = 'drag';
	private touchpadMode = false;
	private fullscreenButton?: JQuery<HTMLElement>;
	private scalingEnabled = true;
	private staticScaleEnabled = false;
	private switchModeRequested = false;
	private switchModeDontAsk = false;
	private switchModeAlreadySwitched = false;
	private isProbablyTouchBook = false;
	private currentMode: "mouse" | "touch" = "mouse";
	private currentConfig: ITouchBarConfig | null = null;
	private lastRootWidth = 0;
	private lastRootHeight = 0;
	private lastWinWidth = 0;
	private lastWinHeight = 0;

	public provides() {
		return {
			'touch.register': this.register,
			'touch.cursorChanged': this.cursorChanged,
			'touch.inputFocusLost': this.inputFocusLost,
			'touch.touchBarConfig': this.touchBarConfig(),
			'touch.touchBarEnabled': () => this.touchBar != null,
			'touch.switchMode': this.switchMode,
			'touch.dispose': this.dispose,
			'touch.correctPosition': this.correctPosition,
			'touch.refreshScale': this.refreshScale
		}
	}

	private touchBarConfig() {
		return {
			buttons: {
				defaultButtons: {
					toggleMode: {
						label: '<span class="ws-button-toggle-mode ws-icon-mouse-pointer" role="button"></span>',
						title: '${touch.controlMode}',
						enabled: true,
						hidden: false,
						action: (button: JQuery<HTMLElement>) => {
							this.buttonToggleMode(button);
						}
					},
					switchToDesktop: {
						label: '<span class="ws-button-switch-to-desktop ws-icon-desktop" role="button"></span>',
						title: '${touch.desktopMode}',
						enabled: true,
						hidden: true,
						action: (button: JQuery<HTMLElement>) => {
							this.buttonSwitchToDesktop(button);
						}
					},
					copy: {
						label: '<span class="ws-icon-docs" role="button"></span>',
						title: '${touch.copy}',
						enabled: true,
						hidden: false,
						action: (button: JQuery<HTMLElement>) => {
							this.buttonCopy(button);
						}
					},
					cut: {
						label: '<span class="ws-icon-scissors" role="button"></span>',
						title: '${touch.cut}',
						enabled: true,
						hidden: false,
						action: (button: JQuery<HTMLElement>) => {
							this.buttonCut(button);
						}
					},
					paste: {
						label: '<span class="ws-icon-paste" role="button"></span>',
						title: '${touch.paste}',
						enabled: true,
						hidden: false,
						action: (button: JQuery<HTMLElement>) => {
							this.buttonPaste(button);
						}
					},
					keyboard: {
						label: '<span class="ws-icon-keyboard" role="button"></span>',
						title: '${touch.keyboard}',
						enabled: true,
						hidden: true,
						action: (button: JQuery<HTMLElement>) => {
							this.buttonOpenKeyboard(button);
						}
					},
					fullscreen: {
						label: '<span class="ws-button-toggle-fullscreen ws-icon-resize-full" role="button"></span>',
						title: '${touch.fullscreen}',
						enabled: !isIOS(),
						hidden: false,
						action: (button: JQuery<HTMLElement>) => {
							this.buttonToggleFullscreen(button);
						}
					}
				},
				left: [],
				center: [],
				right: []
			},
			scalingEnabled: false,
			staticScaleEnabled: false
		};
	}

	private register(config: ITouchBarConfig) {
		this.canvas = this.api.getCanvas();

		this.doRegister(config, false);

		this.currentConfig = config;

		$(document).on("touchstart", (event) => {
			if (isNotValidCanvasTarget(event.target) || this.api.hasUndockedWindows()) {
				return;
			}

			if (!this.registered && !this.switchModeDontAsk && !this.switchModeRequested) {
				// ask to switch to touch
				this.switchModeRequested = true;

				this.api.showBar(this.api.dialogs.touchSwitchModeTouchDialog);

				event.preventDefault();
			}
		});

		document.addEventListener('mousemove', (event) => {
			if (isNotValidCanvasTarget(event.target)) {
				return;
			}

			if (this.registered && !this.switchModeDontAsk && !this.switchModeRequested && !this.isProbablyTouchBook) {
				// ask to switch to mouse
				this.switchModeRequested = true;

				this.api.showBar(this.api.dialogs.touchSwitchModeMouseDialog);

				event.preventDefault();
				event.stopPropagation();
			}
		}, false);
	}

	private doRegister(config: ITouchBarConfig | null, force: boolean) {
		if (this.registered || (!isTouchDevice() && !force)) {
			return;
		}

		this.input = $(document.querySelector(".ws-input-hidden")!) as JQuery<HTMLInputElement>;

		document.addEventListener("touchstart", this.handleStart, { passive: false });
		document.addEventListener("touchend", this.handleEnd, { passive: false });
		document.addEventListener("touchcancel", this.handleCancel, { passive: false });
		document.addEventListener("touchleave", this.handleEnd, { passive: false });
		document.addEventListener("touchmove", this.handleMove, { passive: false });

		this.api.cfg.rootElementWrapper.addClass("touch");

		this.input[0].addEventListener("focus", this.handleInputFocus);
		this.input[0].addEventListener("blur", this.handleInputBlur);

		this.input[0].addEventListener('keyup', this.handleKeyup);
		this.input[0].addEventListener('keydown', this.handleKeydown);
		this.input[0].addEventListener('compositionstart', this.handleCompositionStart);
		this.input[0].addEventListener('compositionupdate', this.handleCompositionUpdate);
		this.input[0].addEventListener('compositionend', this.handleCompositionEnd);
		this.input[0].addEventListener('input', this.handleInputEvent as (e: Event) => void);

		document.addEventListener('webkitfullscreenchange', this.handleFullScreenChange);
		document.addEventListener('mozfullscreenchange', this.handleFullScreenChange);
		document.addEventListener('fullscreenchange', this.handleFullScreenChange);
		document.addEventListener('MSFullscreenChange', this.handleFullScreenChange);

		if (isIOS()) {
			document.addEventListener('gesturestart', this.handleGestureStart);
		}

		this.initIntervals();

		this.api.cfg.rootElement[0].onselectstart = (_: any) => {
			// prevent browser text selection
			return false;
		};

		this.registered = true;
		this.api.cfg.touchMode = true;
		this.currentMode = "touch";
		this.touchpadMode = false;

		this.input.blur();
		this.api.focusDefault();

		this.lastRootWidth = this.api.cfg.rootElement.width()!;
		this.lastRootHeight = this.api.cfg.rootElement.height()!;
		this.lastWinWidth = Math.round(document.body.clientWidth);
		this.lastWinHeight = Math.round(document.body.clientHeight);

		this.api.cfg.rootElement.parent().append(html);
		if (!this.api.cfg.hideTouchBar) {
			this.initTouchBar(config);
		}
		this.initMisc(config);
		this.initScale();
	}

	private dispose() {
		if (!this.registered) {
			return;
		}

		document.removeEventListener("touchstart", this.handleStart);
		document.removeEventListener("touchend", this.handleEnd);
		document.removeEventListener("touchcancel", this.handleCancel);
		document.removeEventListener("touchleave", this.handleEnd);
		document.removeEventListener("touchmove", this.handleMove);

		this.api.cfg.rootElementWrapper.removeClass("touch");

		if (this.input) {
			this.input[0].removeEventListener("focus", this.handleInputFocus);
			this.input[0].removeEventListener("blur", this.handleInputBlur);

			this.input[0].removeEventListener('keyup', this.handleKeyup);
			this.input[0].removeEventListener('keydown', this.handleKeydown);
			this.input[0].removeEventListener('compositionstart', this.handleCompositionStart);
			this.input[0].removeEventListener('compositionupdate', this.handleCompositionUpdate);
			this.input[0].removeEventListener('compositionend', this.handleCompositionEnd);
			this.input[0].removeEventListener('input', this.handleInputEvent as (e: Event) => void);
		}
		document.removeEventListener('webkitfullscreenchange', this.handleFullScreenChange);
		document.removeEventListener('mozfullscreenchange', this.handleFullScreenChange);
		document.removeEventListener('fullscreenchange', this.handleFullScreenChange);
		document.removeEventListener('MSFullscreenChange', this.handleFullScreenChange);

		if (isIOS()) {
			document.removeEventListener('gesturestart', this.handleGestureStart);
		}

		this.api.cfg.rootElement[0].onselectstart = () => {
			return true;
		};

		this.api.sendHandshake();

		clearInterval(this.sendInterval);
		clearInterval(this.canvasResizeInterval);

		$(".touch-control, #ws-cursor").remove();

		this.touchBar = undefined;
		this.currentMode = "mouse";
		this.registered = false;
	}

	// handler functions
	private handleInputFocus = (e: FocusEvent) => {
		if (this.input?.is(".focused-with-caret.editable")) {
			$("#fake-input").remove();
			if (!this.input.is(':focus')) {
				if (this.focusCounter === 0) {
					this.api.focusDefault();
					this.focusCounter++;
				}
			} else {
				if (this.input!.val() === "") {
					this.input![0].value = " ";
					this.input![0].selectionStart = this.input![0].selectionEnd = this.input![0].value.length;
				}
			}
			// this.correctInputPosition();
		} else {
			// prevent focusing input without caret
			e.preventDefault();
			this.input?.blur();
		}
		return false;
	}

	private handleInputBlur = (_: FocusEvent) => {
		this.focusCounter = 0;
		this.input?.val('');
		$("#fake-input").remove();
		this.api.focusDefault();
		// force canvas repaint
		this.repaint();
	}

	private handleKeyup = (event: KeyboardEvent) => {
		if (event.keyCode === 8 || event.keyCode === 13) {
			this.inputEvtQueue.push(this.getKBKey(KeyEventType.keyup, event));
		}
		this.backspaceKeyDown = false;
	}

	private handleKeydown = (event: KeyboardEvent) => {
		if (event.keyCode === 8 || event.keyCode === 13) {
			this.inputEvtQueue.push(this.getKBKey(KeyEventType.keydown, event));
		}

		this.backspaceKeyDown = (event.keyCode === 8);

		if (event.keyCode === 8 && this.input![0].value.length === 1) {
			// ignore this event when the last character is going to be deleted
			// instead set the value of the input to default " "
			// this fixes problems with different keyboard implementations that do not send any event when backspace is pressed in empty input
			this.input![0].value = " ";
			this.input![0].selectionStart = this.input![0].selectionEnd = this.input![0].value.length;
			event.preventDefault();
  			event.stopPropagation();
			return false;
		}
		return true;
	}

	private handleCompositionStart = (event: CompositionEvent) => {
		this.compositionText = event.data;
		this.composing = true;
	}

	private handleCompositionUpdate = (event: CompositionEvent) => {
		for (const _ of this.compositionText) {
			this.sendBackspace();
		}

		this.compositionText = event.data;
	}

	private handleCompositionEnd = (event: CompositionEvent) => {
		this.compositionText = event.data;

		if (this.compositionText.length === 0) {
			this.composing = false;
		}
	}

	private handleInputEvent = (event: InputEvent) => {
		if (event.inputType === 'deleteContentBackward') {
			if (this.backspaceKeyDown) {
				// handled by keydown-keyup handlers
				return;
			}
			if (this.composing) {
				for (const _ of this.compositionText) {
					this.sendBackspace();
				}
			} else {
				this.sendBackspace();

				if (this.input![0].value.length === 0) {
					// prevent this event when the last character has been deleted
					// set the value of the input to default " "
					// this fixes problems with different keyboard implementations that do not send any event when backspace is pressed in empty input
					this.input![0].value = " ";
					setTimeout(() => {
						this.input![0].selectionStart = this.input![0].selectionEnd = this.input![0].value.length;
					}, 1);
					event.preventDefault();
					event.stopPropagation();
					return;
				}
			}
		}

		if (event.inputType === 'insertCompositionText') {
			if (event.data != null && event.data.length) {
				this.sendWordsUsingKeypressEvent(event.data);
				this.compositionText = event.data;
			}
			return;
		}

		if (!event.isComposing && event.inputType === 'insertText' && event.data != null) {
			this.sendWordsUsingKeypressEvent(event.data);
			this.compositionText = "";
			this.composing = false;
		}
	}

	private handleFullScreenChange = () => {
		// force canvas repaint
		this.repaint();

		const doc = window.document;

		const isFullscreen = doc.fullscreenElement || (doc as any).mozFullScreenElement || (doc as any).webkitFullscreenElement || (doc as any).msFullscreenElement;
		this.toast("Fullscreen " + (isFullscreen ? "ON" : "OFF"));

		if (this.fullscreenButton != null) {
			this.fullscreenButton.find(".ws-button-toggle-fullscreen")
				.toggleClass("ws-icon-resize-small", isFullscreen)
				.toggleClass("ws-icon-resize-full", !isFullscreen);
		}
	}

	private handleGestureStart = (event: Event) => {
		// prevent pinch-zoom
		event.preventDefault();
	}

	private initIntervals() {
		this.sendInterval = setInterval(() => this.sendInput(), 100);
		setTimeout(() => {
			// defer resize check a few seconds to let the app resize successfully after touch bar makes the canvas smaller
			this.canvasResizeInterval = setInterval(() => this.resizeCheck(), 500);
		}, 3000);
	}

	private initTouchBar(config: ITouchBarConfig | null) {
		if (this.touchBar != null) {
			return;
		}

		const root = this.api.cfg.rootElement.parent();
		this.touchBar = root.find('div[data-id="touchBar"]');
		if (config) {
			if (config.buttons) {
				const buttonContainer = this.touchBar!.find(".ws-toolbar-container");
				const defButtons = config.buttons.defaultButtons;

				if (defButtons) {
					this.appendButtonsToBar([defButtons.toggleMode], buttonContainer.find(".align-left"));
					this.appendButtonsToBar([defButtons.switchToDesktop], buttonContainer.find(".align-left"));
					this.appendButtonsToBar([defButtons.copy], buttonContainer.find(".align-center"));
					this.appendButtonsToBar([defButtons.cut], buttonContainer.find(".align-center"));
					this.appendButtonsToBar([defButtons.paste], buttonContainer.find(".align-center"));
					this.appendButtonsToBar([defButtons.keyboard], buttonContainer.find(".align-right"));
					this.appendButtonsToBar([defButtons.fullscreen], buttonContainer.find(".align-right"));
				}

				this.appendButtonsToBar(config.buttons.left, buttonContainer.find(".align-left"));
				this.appendButtonsToBar(config.buttons.center, buttonContainer.find(".align-center"));
				this.appendButtonsToBar(config.buttons.right, buttonContainer.find(".align-right"));
			}
		}

		this.cursor = $("#ws-cursor");
		this.touchBar!.show();
	}

	private initMisc(config: ITouchBarConfig | null) {
		if (config) {
			// this.scalingEnabled = config.scalingEnabled;
			this.scalingEnabled = false;
			this.staticScaleEnabled = config.staticScaleEnabled;
		}

		$("#ws-canvas-scale .scale-restore").click(() => {
			this.doScaleCanvas(1.0);
		});
	}

	private initScale() {
		if (this.scalingEnabled) {
			let touchBarHeight = 0;
			if (this.touchBar && this.touchBar != null) {
				touchBarHeight = this.touchBar.height()!;
			}
			const initScale = Math.min(document.body.offsetWidth / this.api.getRootWidth(), (document.body.offsetHeight - touchBarHeight) / this.api.getRootHeight());
			$(this.canvas!).data("scale", initScale);
			$(this.canvas!).data("minscale", initScale);
			this.doScaleCanvas(initScale);
		} else if (this.staticScaleEnabled) {
			const parentHeight = this.api.cfg.rootElement[0].clientHeight;
			const parentWidth = this.api.cfg.rootElement[0].clientWidth;
			const initScale = Math.min(parentWidth / this.api.getRootWidth(), parentHeight / this.api.getRootHeight());
			// init static zoom scale on root element
			$(this.canvas!).data("scale", initScale);

			this.refreshScale();

			// const vp = document.querySelector("meta[name='viewport']");
			// vp!.setAttribute("content", "width=device-width, user-scalable=no, initial-scale=" + initScale + ", maximum-scale=" + initScale);
		}
	}

	private refreshScale() {
		if (this.staticScaleEnabled) {
			const scale = $(this.canvas!).data("scale") || 1.0;

			const zoomEls = this.api.cfg.rootElement[0].querySelectorAll(".suramadu-canvas:not([data-id=canvas]), .ws-input-hidden");
			zoomEls.forEach((zoomEl: any) => {
				(zoomEl as HTMLElement).style.zoom = scale + "";
			});
		}
	}

	private disposeScale() {
		if (this.scalingEnabled) {
			$(this.canvas!).data("scale", 1);
			$(this.canvas!).data("minscale", 1);
			this.doScaleCanvas(1.0);
		} else if (this.staticScaleEnabled) {
			// init static zoom scale on root element
			$(this.canvas!).data("scale", 1);

			const zoomEls = this.api.cfg.rootElement[0].querySelectorAll(".suramadu-canvas:not([data-id=canvas]), .ws-input-hidden");
			zoomEls.forEach((zoomEl: any) => {
				(zoomEl as HTMLElement).style.zoom = "1";
			});
			
			// const vp = document.querySelector("meta[name='viewport']");
			// vp!.setAttribute("content", "width=device-width, user-scalable=no, initial-scale=" + "1" + ", maximum-scale=" + "1");
		}
	}

	private appendButtonsToBar(buttons: IButton[], container: JQuery<HTMLElement>) {
		if (buttons) {
			for (const button of buttons) {
				this.appendButtonToBar(button, container);
			}
		}
	}

	private appendButtonToBar(btn: IButton, container: JQuery<HTMLElement>) {
		if (!btn || !btn.enabled) {
			return;
		}
		const button = $('<button class="ws-touchbar-btn">' + this.api.translate(btn.label) + '</button>');
		button.attr("title", this.api.translate(btn.title));
		button.on("click", () => {
			if (btn.action) {
				btn.action(button);
			}
		});
		button.toggle(!btn.hidden);
		container.append(button);
	}

	// this.api.functions

	private cursorChanged(cursorMsg: appFrameProtoOut.ICursorChangeEventMsgOutProto) {
		if (!this.cursor || !this.registered || !this.api.cfg.touchMode || !this.touchpadMode) {
			return;
		}

		this.cursor.removeClass();
		this.cursor.css("content", "");

		if (cursorMsg.b64img == null) {
			const cur = cursorMsg.cursor;
			if (cur && cur != null) {
				this.cursor.addClass(cur);
			}
		} else {
			this.cursor.addClass("custom");
			if (this.api.cfg.ieVersion) {
				this.cursor.css("content", 'url(\'' + this.api.cfg.connectionUrl + 'file?id=' + cursorMsg.curFile + '\')');
			} else {
				const data = getImageString(cursorMsg.b64img);
				this.cursor.css("content", 'url(' + data + ')');
			}
		}
	}

	private inputFocusLost() {
		if (!this.input || !this.registered || !this.api.cfg.touchMode) {
			return;
		}
	
		if (this.input.is(".focused-with-caret.editable")) {
			return;
		}

		this.hideKeyboard();
		// this.focusFakeInput();
	}

	private focusFakeInput() {
		// focus fake input
		// iOS: need to focus this input to be able to focus on the real keyboard input not from user-initiated event
		// https://stackoverflow.com/questions/12204571/mobile-safari-javascript-focus-method-on-inputfield-only-works-with-click
		if (!$("#fake-input").length) {
			const fakeInput = $('<input type="text" id="fake-input" style="position: absolute; opacity: 0; height: 0; font-size: 16px;" readonly="true">');
			$("body").prepend(fakeInput);

			fakeInput.on("focus", () => {
				setTimeout(() => {
					$("#fake-input").remove();
				}, 1000);
			});
		}
		$("#fake-input")[0].focus({ preventScroll: true });
	}
	
	private switchMode(doSwitch: boolean, dontAsk: boolean) {
		this.switchModeRequested = false;

		if (dontAsk) {
			this.switchModeDontAsk = true;
		}

		if (!doSwitch) {
			return;
		}

		if (this.currentMode === "mouse") {
			// switch to touch
			this.doRegister(this.currentConfig, true);
			this.api.sendHandshake();

			// if first time switched => mouse mode was original mode => device is probably a notebook with touch screen
			if (!this.switchModeAlreadySwitched || this.isProbablyTouchBook) {
				$(".ws-button-switch-to-desktop").parent().show();
				$(".ws-button-toggle-mode").parent().hide(); // we don't need to switch to pointer mode anymore
				this.isProbablyTouchBook = true;
			}
		} else {
			// switch to mouse
			this.disposeScale(); // make sure there is no touch scale applied
			this.dispose();
			this.api.cfg.touchMode = false;
		}

		this.switchModeAlreadySwitched = true;
	}

	// button actions

	private buttonToggleMode(button: JQuery<HTMLElement>) {
		this.touchpadMode = !this.touchpadMode;

		button.find(".ws-button-toggle-mode")
			.toggleClass("ws-icon-hand-paper-o", this.touchpadMode)
			.toggleClass("ws-icon-mouse-pointer", !this.touchpadMode);

		if (this.cursor) {
			this.cursor?.toggle(this.touchpadMode).css({ "top": this.cursor.parent().height()! / 2, "left": this.cursor.parent().width()! / 2 });
		}

		button.blur();

		this.toast(this.touchpadMode ? "Pointer Mode" : "Touch Mode");
	}

	private buttonSwitchToDesktop(_: JQuery<HTMLElement>) {
		this.disposeScale(); // make sure there is no touch scale applied
		this.dispose();
		this.api.cfg.touchMode = false;
	}

	private buttonCopy(button: JQuery<HTMLElement>) {
		button.blur();
		this.api.send({ copy: { type: CopyEventMsgType.copy } });
		this.toast("Copy");
	}

	private buttonCut(button: JQuery<HTMLElement>) {
		button.blur();
		this.api.send({ copy: { type: CopyEventMsgType.cut } });
		this.toast("Cut");
	}

	private buttonPaste(button: JQuery<HTMLElement>) {
		button.blur();
		this.api.displayPasteDialog({ "title": "${clipboard.paste.title.touch}", "message": "${clipboard.paste.message.touch}" })
		this.toast("Paste");
	}

	private buttonOpenKeyboard(button: JQuery<HTMLElement>) {
		button.blur();
		this.showKeyboard();
		this.toast("Keyboard ON");
	}

	private buttonToggleFullscreen(button: JQuery<HTMLElement>) {
		this.fullscreenButton = button;

		button.blur();

		const doc = window.document;
		const docEl = doc.documentElement;

		const requestFullScreen = docEl.requestFullscreen || (docEl as any).mozRequestFullScreen || (docEl as any).webkitRequestFullScreen || (docEl as any).msRequestFullscreen;
		const cancelFullScreen = doc.exitFullscreen || (doc as any).mozCancelFullScreen || (doc as any).webkitExitFullscreen || (doc as any).msExitFullscreen;

		if (!doc.fullscreenElement && !(doc as any).mozFullScreenElement && !(doc as any).webkitFullscreenElement && !(doc as any).msFullscreenElement) {
			requestFullScreen.call(docEl)
		} else {
			cancelFullScreen.call(doc);
		}
	}

	// touch handling

	private handleStart = (evt: TouchEvent) => {
		if (isNotValidCanvasTarget(evt.target)) {
			return;
		}
		if (this.touchpadMode) {
			this.handleStartPointer(evt);
		} else if (this.scrollMode === "drag") {
			this.handleStartScrollDrag(evt);
		} else if (this.scrollMode === "swipe") {
			this.handleStartScrollSwipe(evt);
		}
	}

	private handleMove = (evt: TouchEvent) => {
		if (isNotValidCanvasTarget(evt.target)) {
			if (evt.cancelable) {
				evt.preventDefault();
			}
			return;
		}
		if (this.touchpadMode) {
			this.handleMovePointer(evt);
		} else if (this.scrollMode === "drag") {
			this.handleMoveScrollDrag(evt);
		} else if (this.scrollMode === "swipe") {
			this.handleMoveScrollSwipe(evt);
		}
	}

	private handleEnd = (evt: TouchEvent) => {
		if (isNotValidCanvasTarget(evt.target)) {
			return;
		}
		if (this.touchpadMode) {
			this.handleEndPointer(evt);
		} else if (this.scrollMode === "drag") {
			this.handleEndScrollDrag(evt);
		} else if (this.scrollMode === "swipe") {
			this.handleEndScrollSwipe(evt);
		}
	}

	private handleCancel = (evt: TouchEvent) => {
		if (isNotValidCanvasTarget(evt.target)) {
			return;
		}
		if (this.touchpadMode) {
			this.handleCancelDefault(evt);
		} else if (this.scrollMode === "drag") {
			this.handleCancelDefault(evt);
		} else if (this.scrollMode === "swipe") {
			this.handleCancelDefault(evt);
		}
	}

	private handleStartDefault = (evt: TouchEvent) => {
		if (this.longPressTimeout != null) {
			clearTimeout(this.longPressTimeout);
			this.longPressTimeout = undefined;
		}
		if (this.longPressStartTimeout != null) {
			clearTimeout(this.longPressStartTimeout);
			this.longPressStartTimeout = undefined;
		}

		const touches = evt.changedTouches;

		if (evt.touches.length === 1) {
			this.dragStarted = evt.timeStamp;
			this.tapStarted = evt.timeStamp;
			const tx = touches[0].clientX;
			const ty = touches[0].clientY;
			this.touchStartX = tx;
			this.touchStartY = ty;

			this.animateDrag(touches[0].clientX, touches[0].clientY);
			this.longPressStartTimeout = setTimeout(() => {
				this.animateLongPress(touches[0].clientX, touches[0].clientY);
			}, 50);

			this.longPressTimeout = setTimeout(() => {
				// handle long press
				this.longPressTimeout = undefined;
				this.tapStarted = 0;

				const eventMsg = [];
				eventMsg.push(this.createMouseEvent(evt.target as Element, MouseEventType.mousedown, tx, ty, 3));
				eventMsg.push(this.createMouseEvent(evt.target as Element, MouseEventType.mouseup, tx, ty, 3));

				this.api.send({ events: eventMsg });
				this.api.focusDefault();

				this.cancelAnimateDrag();
				this.animateLongPressOut(tx, ty);
			}, this.tapDelayThreshold);
		} else {
			// cancel tap and long press
			this.tapStarted = 0;
			this.dragStarted = 0;
			this.touchStartY = 0;
			this.touchStartX = 0;
			this.cancelAnimateLongPress();
			this.cancelAnimateDrag();

			if (this.longPressTimeout != null) {
				clearTimeout(this.longPressTimeout);
				this.longPressTimeout = undefined;
			}
		}

		this.handleStartScaling(evt);

		if (evt.cancelable) {
			// prevent browser simulating mouse events
			evt.preventDefault();
		}
	}

	private handleStartScrollDrag = (evt: TouchEvent) => {
		this.handleStartDefault(evt);

		this.dragging = false;
	}

	private handleStartScrollSwipe = (evt: TouchEvent) => {
		this.handleStartDefault(evt);

		this.cancelAnimateDrag();
	}

	private handleStartPointer = (evt: TouchEvent) => {
		if (this.longPressTimeout != null) {
			clearTimeout(this.longPressTimeout);
			this.longPressTimeout = undefined;
		}
		if (this.longPressStartTimeout != null) {
			clearTimeout(this.longPressStartTimeout);
			this.longPressStartTimeout = undefined;
		}

		const touches = evt.changedTouches;

		if (evt.touches.length === 1) {
			this.tapStarted = evt.timeStamp;
			this.dragStarted = evt.timeStamp;
			this.touchStartX = touches[0].clientX;
			this.touchStartY = touches[0].clientY;
			if (this.cursor) {
				const cursorX = parseInt(this.cursor.css("left"), 10);
				const cursorY = parseInt(this.cursor.css("top"), 10);
				this.cursor.data("originX", cursorX);
				this.cursor.data("originY", cursorY);

				const relatedCanvas = this.getPointerRelatedCanvas(cursorX, cursorY);

				this.longPressStartTimeout = setTimeout(() => {
					this.animateLongPress(cursorX, cursorY);
				}, 50);

				this.longPressTimeout = setTimeout(() => {
					// handle long press
					this.longPressTimeout = undefined;
					this.tapStarted = 0;

					const eventMsg = [];
					eventMsg.push(this.createMouseEvent(relatedCanvas, MouseEventType.mousedown, cursorX, cursorY, 3));
					eventMsg.push(this.createMouseEvent(relatedCanvas, MouseEventType.mouseup, cursorX, cursorY, 3));

					this.api.send({ events: eventMsg });
					this.api.focusDefault();

					this.animateLongPressOut(cursorX, cursorY);
				}, this.tapDelayThreshold);
			}
		} else {
			this.tapStarted = 0;
			this.touchStartY = 0;
			this.touchStartX = 0;
			this.cancelAnimateLongPress();

			if (this.longPressTimeout != null) {
				clearTimeout(this.longPressTimeout);
				this.longPressTimeout = undefined;
			}
		}

		this.handleStartScaling(evt);

		this.dragging = false;

		if (evt.cancelable) {
			// prevent browser simulating mouse events
			evt.preventDefault();
		}
	}

	private handleMoveDefault = (evt: TouchEvent) => {

		if (this.scalingEnabled && this.scaling && evt.touches.length === 2) {
			const dist = Math.hypot(evt.touches[0].clientX - evt.touches[1].clientX, evt.touches[0].clientY - evt.touches[1].clientY);
			const diff = (dist - this.lastScaleDist) * this.scaleSpeed;

			this.lastScaleDist = dist;

			const canvasDiameter = Math.hypot(this.canvas?.width!, this.canvas?.height!);
			let scale = diff / canvasDiameter;
			const lastScale = $(this.canvas!).data("scale") || 1.0;
			const minScale = $(this.canvas!).data("minscale") || 1.0

			scale = Math.min(Math.max(minScale, lastScale + scale), this.maxScale);

			this.doScaleCanvas(scale);

			const tx1 = evt.touches[0].clientX;
			const ty1 = evt.touches[0].clientY;
			const tx2 = evt.touches[1].clientX;
			const ty2 = evt.touches[1].clientY;

			const sx1 = this.scaleStartPoints[0].x;
			const sy1 = this.scaleStartPoints[0].y;
			const sx2 = this.scaleStartPoints[1].x;
			const sy2 = this.scaleStartPoints[1].y;

			const widthChange = ($(this.canvas!).width()! * scale) - this.scaleStartDim.width!;
			const heightChange = ($(this.canvas!).height()! * scale) - this.scaleStartDim.height!;

			const sCenter = { "x": ((sx1 + sx2) / 2), "y": ((sy1 + sy2) / 2) };
			const tCenter = { "x": ((tx1 + tx2) / 2), "y": ((ty1 + ty2) / 2) };

			// scroll change is relative to scale start point
			// (center = 0,0 => no scroll change)
			// (center = width,height => full scroll change)
			let scrollLeft = this.scaleStartScrollLeft + (widthChange * (sCenter.x / $(this.canvas!).width()!));
			let scrollTop = this.scaleStartScrollTop + (heightChange * (sCenter.y / $(this.canvas!).height()!));

			// additional scroll due to center change
			scrollLeft += sCenter.x - tCenter.x;
			scrollTop += sCenter.y - tCenter.y;

			const parent = this.api.cfg.rootElement;

			parent[0].scrollLeft = scrollLeft;
			parent[0].scrollTop = scrollTop;
		} else {
			this.scaling = false;
		}
	}

	private handleMoveScrollDrag = (evt: TouchEvent) => {
		this.handleMoveDefault(evt);

		const touches = evt.changedTouches;

		if (evt.touches.length === 1) {
			this.handleDragMove(evt.target as Element, evt.touches[0].clientX, evt.touches[0].clientY, touches[0].clientX, touches[0].clientY, this.touchStartX, this.touchStartY, true, () => true);
		} else {
			// cancel tap
			this.tapStarted = 0;

			// cancel drag
			this.dragStarted = 0;
			this.dragging = false;
		}

		if (evt.cancelable) {
			evt.preventDefault();
			evt.stopPropagation();
		}
	}

	private handleMoveScrollSwipe = (evt: TouchEvent) => {
		this.handleMoveDefault(evt);

		const touches = evt.changedTouches;

		if (evt.touches.length === 1) {
			// if moved too much, cancel tap/long press event and handle wheel
			if (Math.abs(this.touchStartX - evt.touches[0].clientX) > this.tapMoveThreshold || Math.abs(this.touchStartY - evt.touches[0].clientY) > this.tapMoveThreshold) {
				// cancel tap
				this.tapStarted = 0;
				// cancel long press
				clearTimeout(this.longPressTimeout);
				this.longPressTimeout = undefined;
				this.cancelAnimateLongPress();

				// handle mouse wheel vertical scroll
				const wheelDelta = parseInt(String(this.touchStartY - evt.touches[0].clientY), 10);
				if (wheelDelta !== 0) {
					const me = this.createMouseEvent(evt.target as Element, MouseEventType.mousewheel, touches[0].clientX, touches[0].clientY, 0);
					me.mouse!.wheelDelta = wheelDelta;

					if (this.latestMouseWheelEvent != null) {
						this.latestMouseWheelEvent.mouse!.wheelDelta! += wheelDelta;
					} else {
						this.latestMouseWheelEvent = me;
					}

					this.touchStartY = evt.touches[0].clientY;
				}
			}
		} else {
			// cancel tap
			this.tapStarted = 0;
		}

		if (evt.cancelable) {
			evt.preventDefault();
			evt.stopPropagation();
		}
	}

	private handleMovePointer = (evt: TouchEvent) => {
		this.handleMoveDefault(evt);

		if (evt.touches.length === 1) {
			const coords = this.getPointerCoords(evt.touches[0].clientX, evt.touches[0].clientY);
			const relatedCanvas = this.getPointerRelatedCanvas(coords.x, coords.y);
			if (this.cursor) {
				this.cursor.css({ "left": coords.x, "top": coords.y });

				this.handleDragMove(relatedCanvas, evt.touches[0].clientX, evt.touches[0].clientY, coords.x, coords.y, this.cursor.data("originX"), this.cursor.data("originY"), false, () => {
					return evt.timeStamp - this.lastTap <= this.doubleTapDelayThreshold;
				});

				if (!this.dragging) {
					// mouse move only
					this.latestMouseMoveEvent = this.createMouseEvent(relatedCanvas, MouseEventType.mousemove, coords.x, coords.y, 0);
					this.cancelAnimateLongPress();
				}
			}
		} else {
			this.tapStarted = 0;
			this.dragStarted = 0;
			this.dragging = false;
			this.latestMouseMoveEvent = undefined;
		}

		if (evt.cancelable) {
			evt.preventDefault();
			evt.stopPropagation();
		}
	}

	private handleEndDefault = (evt: TouchEvent, relatedCanvas: Element | null, x: number, y: number) => {
		if (this.longPressTimeout != null) {
			clearTimeout(this.longPressTimeout);
			this.longPressTimeout = undefined;
		}

		if (this.tapStarted > 0) {
			const duration = evt.timeStamp - this.tapStarted;

			if (duration <= this.tapDelayThreshold) {
				// tap
				const eventMsg = [];
				eventMsg.push(this.createMouseEvent(relatedCanvas, MouseEventType.mousedown, x, y, 1));
				eventMsg.push(this.createMouseEvent(relatedCanvas, MouseEventType.mouseup, x, y, 1));

				if (relatedCanvas && $(relatedCanvas).closest(".suramadu-html-canvas").length) {
					// simulate browser mouse click on HtmlPanel (does not work for iframe)
					const mouseClick = new MouseEvent("click", {
						bubbles: true,
						cancelable: true,
						view: window,
						clientX: x,
						clientY: y,
					});
					relatedCanvas.dispatchEvent(mouseClick);
				}

				this.api.send({ events: eventMsg });
				
				if (isIOS()) {
					this.focusFakeInput();
				}

				this.cancelAnimateDrag();
				this.cancelAnimateLongPress();
				this.animateTap(x, y);

				if (this.tapStarted - this.lastTap <= this.doubleTapDelayThreshold) {
					// double tap
					const me = this.createMouseEvent(relatedCanvas, MouseEventType.dblclick, x, y, 1);

					this.api.send({ events: [me] });

					this.lastTap = 0;
				}

				this.lastTap = this.tapStarted;
			} else {
				// long press, already handled by timer
			}
		}

		this.scaling = false;
		this.lastScaleDist = 0;

		// try prevent browser simulated mouse events
		if (evt.cancelable) {
			evt.preventDefault();
			evt.stopPropagation();
		}
	}

	private handleEndScrollDrag = (evt: TouchEvent) => {
		const touches = evt.changedTouches;

		this.handleEndDefault(evt, evt.target as Element, touches[0].clientX, touches[0].clientY);
		this.handleDragMoveEnd(evt.target as Element, touches[0].clientX, touches[0].clientY);
	}

	private handleEndScrollSwipe = (evt: TouchEvent) => {
		const touches = evt.changedTouches;

		this.handleEndDefault(evt, evt.target as Element, touches[0].clientX, touches[0].clientY);
	}

	private handleEndPointer = (evt: TouchEvent) => {
		const touches = evt.changedTouches;
		const coords = this.getPointerCoords(touches[0].clientX, touches[0].clientY);
		const relatedCanvas = this.getPointerRelatedCanvas(coords.x, coords.y);

		this.handleEndDefault(evt, relatedCanvas, coords.x, coords.y);
		this.handleDragMoveEnd(relatedCanvas, coords.x, coords.y);
	}

	private handleCancelDefault = (evt: TouchEvent) => {
		if (this.longPressTimeout != null) {
			clearTimeout(this.longPressTimeout);
			this.longPressTimeout = undefined;
		}

		if (evt.cancelable) {
			evt.preventDefault();
			evt.stopPropagation();
		}
	}

	// [tx, ty] - point from evt.touches, [x, y] - point from evt.changedTouches or new pointer coords, [tsX, tsY] - point of touch/pointer start
	private handleDragMove = (relatedCanvas: Element | null, tx: number, ty: number, x: number, y: number, tsX: number, tsY: number, doAnimateDrag: boolean, startDragCondition: () => boolean) => {
		// if moved too much, cancel tap/long press event and handle wheel
		if (Math.abs(this.touchStartX - tx) > this.tapMoveThreshold || Math.abs(this.touchStartY - ty) > this.tapMoveThreshold) {
			// cancel tap
			this.tapStarted = 0;
			// cancel long press
			if (this.longPressTimeout != null) {
				clearTimeout(this.longPressTimeout);
				this.longPressTimeout = undefined;
			}
			this.cancelAnimateLongPress();

			// start drag if not started
			if (!this.dragging && this.dragStarted > 0 && startDragCondition()) {
				this.dragging = true;
				this.api.send({ events: [this.createMouseEvent(relatedCanvas, MouseEventType.mousedown, tsX, tsY, 1)] });
			}
		}

		// if moved just a little, do not start drag unless already started
		if (this.dragging) {
			this.api.send({ events: [this.createMouseEvent(relatedCanvas, MouseEventType.mousemove, x, y, 1)] });
			if (doAnimateDrag) {
				this.animateDrag(x, y);
			}
		}
	}

	private handleDragMoveEnd = (relatedCanvas: Element | null, x: number, y: number) => {
		if (this.dragging) {
			const me = this.createMouseEvent(relatedCanvas, MouseEventType.mouseup, x, y, 1);

			this.api.send({ events: [me] });
			this.api.focusDefault();

			this.dragging = false;
		}

		this.cancelAnimateDrag();
	}

	private handleStartScaling = (evt: TouchEvent) => {
		if (!this.scalingEnabled) {
			return;
		}

		const parent = this.api.cfg.rootElement;

		this.scaleStartScrollLeft = parent.scrollLeft()!;
		this.scaleStartScrollTop = parent.scrollTop()!;

		if (evt.touches.length === 2) {
			this.scaling = true;
			const x1 = evt.touches[0].clientX;
			const y1 = evt.touches[0].clientY;
			const x2 = evt.touches[1].clientX;
			const y2 = evt.touches[1].clientY;
			this.lastScaleDist = Math.hypot(x1 - x2, y1 - y2);
			this.scaleStartPoints = [{ "x": x1, "y": y1 }, { "x": x2, "y": y2 }];

			const $canvas = $(this.canvas!);
			const scale = $canvas.data("scale") || $canvas.data("minscale") || 1.0;
			this.scaleStartDim = { "width": $canvas.width()! * scale, "height": $canvas.height()! * scale };
		} else {
			this.scaling = false;
			this.lastScaleDist = 0;
			this.scaleStartPoints = [];
		}
	}

	// animate functions

	private animateTap(x: number, y: number) {
		if (window.pageYOffset > 0) {
			y += window.pageYOffset;
		}

		const tap = $("#ws-touch-effect");
		tap.removeClass("animate animateLong animateLongOut").css({ top: (y - tap.height()! / 2) + 'px', left: (x - tap.width()! / 2) + 'px' }).addClass("animate");
	}

	private animateLongPress(x: number, y: number) {
		if (window.pageYOffset > 0) {
			y += window.pageYOffset;
		}

		const tap = $("#ws-touch-effect");
		tap.removeClass("animate animateLong animateLongOut").css({ top: (y - tap.height()! / 2) + 'px', left: (x - tap.width()! / 2) + 'px' }).addClass("animateLong");
	}

	private animateLongPressOut(x: number, y: number) {
		if (window.pageYOffset > 0) {
			y += window.pageYOffset;
		}

		const tap = $("#ws-touch-effect");
		tap.removeClass("animate animateLong animateLongOut").css({ top: (y - tap.height()! / 2) + 'px', left: (x - tap.width()! / 2) + 'px' }).addClass("animateLongOut");
	}

	private cancelAnimateLongPress() {
		if (this.longPressStartTimeout != null) {
			clearTimeout(this.longPressStartTimeout);
			this.longPressStartTimeout = undefined;
		}
		$("#ws-touch-effect").removeClass("animate animateLong animateLongOut");
	}

	private cancelAnimateDrag() {
		$('.ws-drag-effect').hide();
	}

	private animateDrag(x: number, y: number) {
		if (window.pageYOffset > 0) {
			y += window.pageYOffset;
		}

		const el = $('.ws-drag-effect');
		el.css({ 'top': (y - el.height()! / 2) + "px", 'left': (x - el.width()! / 2) + "px" }).show();
	}
	// utility functions

	private getPointerCoords(touchX: number, touchY: number) {
		const parent = this.api.cfg.rootElement;
		const pHeight = parent.height()!;
		const pWidth = parent.width()!;
		const xMove = touchX - this.touchStartX;
		const yMove = touchY - this.touchStartY;
		const originX = this.cursor?.data("originX");
		const originY = this.cursor?.data("originY");

		const left = Math.max(0, Math.min(originX + xMove, pWidth - 1));
		const top = Math.max(0, Math.min(originY + yMove, pHeight - 1));

		return { x: left, y: top };
	}

	private getPointerRelatedCanvas(px: number, py: number) {
		return document.elementFromPoint(px, py);
	}

	private doScaleCanvas(scale: number) {
		if (!this.scalingEnabled) {
			return;
		}

		if (this.scaleHideTimer != null) {
			clearTimeout(this.scaleHideTimer);
			this.scaleHideTimer = undefined;
		}

		if (scale === 1.0) {
			$(this.canvas!).css({ "transform": "scale(" + scale + ")", "transform-origin": "" });
		} else {
			$(this.canvas!).css({ "transform": "scale(" + scale + ")", "transform-origin": "top left" });
		}
		$(this.canvas!).data("scale", scale);

		if (scale !== 1.0) {
			$("#ws-canvas-scale").show();
			$("#ws-canvas-scale .scale-restore").show();
		} else {
			$("#ws-canvas-scale .scale-restore").hide();
		}
		$("#ws-canvas-scale .scale-value").text(Number(scale * 100).toFixed(0) + "%");

		this.scaleHideTimer = setTimeout(() => {
			$("#ws-canvas-scale").fadeOut("slow");
			this.scaleHideTimer = undefined;
		}, 3000);

		this.repaint();
	}

	private correctPosition() {
		if (this.api.cfg.rootElement[0].scrollTop > 0) {
			this.api.cfg.rootElement[0].scrollTo(0, 0);
		}

		if (!this.isKeyboardProbablyShowing) {
			if (this.staticScaleEnabled) {
				this.initScale();
			}
		}
	}

	private sendWordsUsingKeypressEvent(data: string) {
		for (let i = 0, length = data.length; i < length; i++) {
			this.inputEvtQueue.push(
				{ key: { type: KeyEventType.keydown, character: data.charCodeAt(i), keycode: 0 } },
				{ key: { type: KeyEventType.keypress, character: data.charCodeAt(i), keycode: 0 } },
				{ key: { type: KeyEventType.keyup, character: data.charCodeAt(i), keycode: 0 } }
			);
		}
	}

	private sendBackspace() {
		this.inputEvtQueue.push({ key: { type: KeyEventType.keydown, character: 8, keycode: 8, "alt": false, "ctrl": false, "shift": false, "meta": false } });
		this.inputEvtQueue.push({ key: { type: KeyEventType.keypress, character: 8, keycode: 8, "alt": false, "ctrl": false, "shift": false, "meta": false } });
		this.inputEvtQueue.push({ key: { type: KeyEventType.keyup, character: 8, keycode: 8, "alt": false, "ctrl": false, "shift": false, "meta": false } });
	}

	private getKBKey(type: appFrameProtoIn.KeyboardEventMsgInProto.KeyEventTypeProto, evt: KeyboardEvent): appFrameProtoIn.IInputEventMsgInProto {
		let char = evt.which;
		if (char === 0 && evt.key != null) {
			char = evt.key.charCodeAt(0);
		}
		let kk = evt.keyCode;
		if (kk === 0) {
			kk = char;
		}
		return {
			key: {
				type,
				character: char,
				keycode: kk,
				alt: evt.altKey,
				ctrl: evt.ctrlKey,
				shift: evt.shiftKey,
				meta: evt.metaKey
			}
		};
	}

	private createMouseEvent(targetElement: Element | null, type: appFrameProtoIn.MouseEventMsgInProto.MouseEventTypeProto, x: number, y: number, button: number): appFrameProtoIn.IInputEventMsgInProto {
		let rectElement;
		const scale = $(this.canvas!).data("scale") || 1.0;
		
		if (targetElement && targetElement != null && targetElement.parentNode && targetElement.parentNode != null && (targetElement.parentNode as Element).getBoundingClientRect) {
			if (targetElement.matches(".internal")) {
				rectElement = targetElement.parentElement?.parentElement;
			} else {
				rectElement = targetElement.parentElement;
			}
		} else {
			rectElement = this.api.getCanvas();
		}

		let winId;
		if (targetElement && targetElement.matches("canvas.suramadu-canvas") && $(targetElement).data("id") && $(targetElement).data("id") !== "canvas") {
			// for a composition canvas window send winId
			if ($(targetElement).is(".internal")) {
				// internal window must use its parent as mouse events target
				if (targetElement.parentNode && $(targetElement.parentNode).data("ownerid")) {
					winId = $(targetElement.parentNode).data("ownerid");
				}
			} else {
				winId = $(targetElement).data("id");
			}
		}
		
		if (type === MouseEventType.mouseup && (!targetElement || !targetElement.matches || !targetElement.matches("canvas.suramadu-canvas"))) {
			// fix for detached composition canvas windows that might overlay same coordinates space, when clicked outside a canvas
			return {
				mouse: {
					x: -1,
					y: -1,
					type,
					button,
					buttons: button === 0 ? 0 : Math.pow(2, button),
					winId: winId || "",
					timeMilis: Date.now() % 86400000
				}
			}
		}
		
		if (type === MouseEventType.mouseup && targetElement && targetElement.matches && !targetElement.matches("canvas.suramadu-canvas") && targetElement.closest(".suramadu-html-canvas") != null) {
			// fix for mouseup over an HtmlWindow div content
			rectElement = targetElement?.closest(".suramadu-html-canvas")?.parentElement;
		}

		const pos = {
			left: rectElement!.getBoundingClientRect().left,
			top: rectElement!.getBoundingClientRect().top
		};

		// return relative mouse position
		let mouseX = Math.round((x - pos.left) / scale);
		let mouseY = Math.round((y - pos.top) / scale);

		if (targetElement && targetElement.parentElement) {
			mouseX += (targetElement.parentElement.scrollLeft / scale);
			mouseY += (targetElement.parentElement.scrollTop / scale);
		}

		return {
			mouse: {
				x: Math.round(mouseX),
				y: Math.round(mouseY),
				type,
				button,
				buttons: button === 0 ? 0 : Math.pow(2, button),
				winId: winId || "",
				timeMilis: Date.now() % 86400000
			}
		}
	}

	private sendInput() {
		this.enqueueInputEvent();
		const evts = this.inputEvtQueue;
		this.inputEvtQueue = [];
		if (evts.length > 0) {
			this.api.send({
				events: evts
			});
		}
	}

	private enqueueInputEvent() {
		if (this.api.cfg.hasControl) {
			if (this.latestMouseWheelEvent != null) {
				let wheelDelta = this.latestMouseWheelEvent.mouse?.wheelDelta;
				if (wheelDelta) {
					const sign = Math.sign(wheelDelta);

					if (Math.abs(wheelDelta) < 10) {
						wheelDelta = sign;
					} else {
						wheelDelta = parseInt(String(wheelDelta / 10), 10);
					}
				}
				this.inputEvtQueue.push(
					{
						mouse:
						{
							x: this.latestMouseWheelEvent.mouse?.x, "y": this.latestMouseWheelEvent.mouse?.y,
							type: MouseEventType.mousewheel,
							wheelDelta,
							button: 0, "buttons": 0,
							ctrl: false, "alt": false, "shift": false, "meta": false,
							timeMilis: this.latestMouseWheelEvent.mouse?.timeMilis
						}
					}
				);

				this.latestMouseWheelEvent = undefined;
			}
			if (this.latestMouseMoveEvent != null) {
				this.inputEvtQueue.push(this.latestMouseMoveEvent);
				this.latestMouseMoveEvent = undefined;
			}
		}
	}

	private showKeyboard() {
		// if (this.input) {
		// 	this.input.addClass('focused-with-caret editable');
		// 	this.api.focusInput(this.input[0]);
		// }
	}

	private hideKeyboard() {
		if (!this.input) {
			return;
		}

		// this is important, do not remove
		this.input[0].style.top = '';
		this.input[0].style.left = '';
		this.input[0].style.height = '';

		this.input.removeClass('focused-with-caret editable');
		this.input.blur();
		this.api.focusDefault();
	}

	private resizeCheck() {
		if (!this.api.cfg.mirror && this.canvas != null) {
			if (this.lastRootWidth !== this.api.cfg.rootElement.width() || this.lastRootHeight !== this.api.cfg.rootElement.height()) {
				if (this.scalingEnabled) {
					const minScale = Math.min(document.body.offsetWidth / this.api.getRootWidth(), (document.body.offsetHeight - this.touchBar?.height()!) / this.api.getRootHeight());
					$(this.canvas).data("minscale", minScale);
					if (minScale > $(this.canvas).data("scale")) {
						$(this.canvas).data("scale", minScale);
						this.doScaleCanvas(minScale);
					}
				}

				this.lastRootWidth = this.api.cfg.rootElement.width()!;
				this.lastRootHeight = this.api.cfg.rootElement.height()!;

				if (this.api.cfg.rootElement.is(".composition")) {
					// when using compositing window manager, the root canvas has 0 size
					// we need to do a handshake only if the root element has changed size
					this.api.sendHandshake();
				}
			}

			if (!this.api.cfg.rootElement.is(".composition")) {
				const w = this.api.getRootWidth();
				const h = this.api.getRootHeight();
				const widthChanged = Math.round($(this.canvas!).width()!) !== w;
				const heightChanged = Math.round($(this.canvas!).height()!) !== h;
				const sizeChanged = widthChanged || heightChanged;
				if (sizeChanged) {
					this.doResize();
				}
			}

			const winWidthChanged = this.lastWinWidth !== Math.round(document.body.clientWidth);
			const winHeightChanged = this.lastWinHeight !== Math.round(document.body.clientHeight);

			if (winWidthChanged && winHeightChanged) {
				this.correctPosition();
			}
			
			if ((winHeightChanged && !winWidthChanged && this.lastWinHeight - document.body.clientHeight > 100)) {
				if (this.input?.is(".focused-with-caret.editable")) {
					this.isKeyboardProbablyShowing = true;
				}
				return;
			} else {
				if (this.input?.is(".focused-with-caret.editable") && this.isKeyboardProbablyShowing) {
					// size has not changed but input is still focused and keyboard is probably showing = user probably closed keyboard with device back button
					// unfocus input
					this.isKeyboardProbablyShowing = false;
					this.hideKeyboard();
				}
			}
			
			this.lastWinWidth = Math.round(document.body.clientWidth);
			this.lastWinHeight = Math.round(document.body.clientHeight);

			this.isKeyboardProbablyShowing = false;
		}
	}

	private doResize() {
		if (this.canvas == null) {
			return
		}
		const w = this.api.getRootWidth();
		const h = this.api.getRootHeight();
		const dpr = getDpr();
		const snapshot = this.canvas.getContext("2d")!.getImageData(0, 0, this.canvas.width, this.canvas.height);

		this.canvas.width = w * dpr;
		this.canvas.height = h * dpr;
		this.canvas.style.width = w + 'px';
		this.canvas.style.height = h + 'px';
		this.canvas.getContext("2d")!.putImageData(snapshot, 0, 0);
		this.api.sendHandshake();
	}

	private repaint() {
		if (this.repaintTimeout != null) {
			clearTimeout(this.repaintTimeout);
			this.repaintTimeout = undefined;
		}
		if (new Date().getTime() - this.lastRepaint < 500) {
			this.repaintTimeout = setTimeout(() => {
				this.repaint();
			}, 250);
			return;
		}
		this.api.repaint();
		this.lastRepaint = new Date().getTime();
	}

	private toast(msg: string) {
		if (this.toastTimer != null) {
			clearTimeout(this.toastTimer);
			this.toastTimer = undefined;
		}

		$("#ws-toast").text(msg).show();
		this.toastTimer = setTimeout(() => {
			$("#ws-toast").text("").hide();
		}, 2000);
	}
}
function isNotValidCanvasTarget(target: Element | Document | null | EventTarget) {
	if (!target) {
		return true;
	}

	if (target instanceof Element && target.matches && (target.matches("canvas.suramadu-canvas") || target.matches(".suramadu-html-canvas"))) {
		return false;
	}

	if ($(target).closest(".suramadu-html-canvas").length) {
		return false;
	}

	return true;
}