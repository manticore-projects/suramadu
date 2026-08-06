import { detectIE, detectFF, detectMac } from './suramadu-util'
import { ModuleDef } from './suramadu-inject';
import { IDockableWindow } from './suramadu-base';
import { appFrameProtoIn } from './proto/proto.in';
import { commonProto } from "./proto/proto.common";

export const inputInjectable = {
    cfg: 'webswing.config' as const,
    send: 'socket.send' as const,
    sendSimpleEvent: 'socket.sendSimpleEvent' as const,
    getCanvas: 'canvas.get' as const,
    cut: 'clipboard.cut' as const,
    copy: 'clipboard.copy' as const,
    paste: 'clipboard.paste' as const,
    isAccessibilityEnabled: 'accessible.isEnabled' as const,
    toggleAccessibility: 'accessible.toggle' as const,
    focusInput: 'focusManager.focusInput' as const,
    inputConfig: 'input.config' as const
}

export interface IInputService {
    'input.register': () => void,
    'input.sendInput': (message?: appFrameProtoIn.IInputEventMsgInProto) => void,
    'input.dispose': () => void,
    'input.registerUndockedCanvas': (win: IDockableWindow) => void,
    'input.updateFocusedHtmlCanvas': (focusedElement: Element | null) => void,
    'input.config': IInputConfig
}

interface IRegisteredListener {
    target: GlobalEventHandlers
    name: keyof GlobalEventHandlersEventMap
    listener: (this: GlobalEventHandlers, ev: any) => any
    useCapture?: boolean
}

interface IInputConfig {
    preventWheelEvent: boolean
}

const KeyEventType = appFrameProtoIn.KeyboardEventMsgInProto.KeyEventTypeProto;
const MouseEventType = appFrameProtoIn.MouseEventMsgInProto.MouseEventTypeProto;
const SimpleEventType = commonProto.SimpleEventMsgInProto.SimpleEventTypeProto;
const DEFAULT_FONT = '14px sans-serif';

export class InputModule extends ModuleDef<typeof inputInjectable, IInputService> {

    private registered = false;
    private registeredListeners: IRegisteredListener[] = [];
    private latestMouseMoveEvent?: appFrameProtoIn.IInputEventMsgInProto;
    private latestMouseWheelEvent?: appFrameProtoIn.IInputEventMsgInProto;
    private latestWindowResizeEvent?: appFrameProtoIn.IInputEventMsgInProto;
    private latestKeyDownEvent?: appFrameProtoIn.IInputEventMsgInProto;
    private latestKeyDownEventSendTimeout: number | null = null;
    private mouseDown = 0;
    private mouseDownButton = 0;
    private mouseDownCanvas?: EventTarget | null;
    private inputEvtQueue: appFrameProtoIn.IInputEventMsgInProto[] = [];
    private pixelsScrolled = 0;
    private compositionInput = false;
    private focusedHtmlCanvas?: Element;
    private ctrlCounter = 0;
    private lastCtrlKey = 0;
    private ctrl3Timeout?: number;

    private caretControlKeys = [33/*pgup*/, 34/*pgdown*/, 35/*end*/, 36/*home*/, 37/*left*/, 38/*up*/, 39/*right*/, 40/*down*/];

    public provides() {
        return {
            'input.register': this.register,
            'input.sendInput': this.sendInput,
            'input.dispose': this.dispose,
            'input.registerUndockedCanvas': this.registerUndockedCanvas,
            'input.updateFocusedHtmlCanvas': this.updateFocusedHtmlCanvas,
            'input.config': this.config()
        }
    }

    private config(): IInputConfig {
        return {
            preventWheelEvent: true
        };
    }

    private sendInput(message?: appFrameProtoIn.IInputEventMsgInProto) {
        this.enqueueInputEvent();
        const evts = this.inputEvtQueue;
        this.inputEvtQueue = [];
        if (message != null) {
            evts.push(message);
        }
        if (evts.length > 0) {
            this.api.send({
                events: evts
            });
        }
    }

    private enqueueInputEvent(message?: appFrameProtoIn.IInputEventMsgInProto) {
        if (this.api.cfg.hasControl) {
            if (this.latestMouseMoveEvent != null) {
                this.inputEvtQueue.push(this.latestMouseMoveEvent);
                this.latestMouseMoveEvent = undefined;
            }
            if (this.latestMouseWheelEvent != null) {
                this.inputEvtQueue.push(this.latestMouseWheelEvent);
                this.latestMouseWheelEvent = undefined;
            }
            if (this.latestWindowResizeEvent != null) {
                this.inputEvtQueue.push(this.latestWindowResizeEvent);
                this.latestWindowResizeEvent = undefined;
            }
            if (message != null) {
                if (JSON.stringify(this.inputEvtQueue[this.inputEvtQueue.length - 1]) !== JSON.stringify(message)) {
                    this.inputEvtQueue.push(message);
                }
            }
        }
    }

    private resetInput() {
        this.latestMouseMoveEvent = undefined;
        this.latestMouseWheelEvent = undefined;
        this.latestWindowResizeEvent = undefined;
        this.latestKeyDownEvent = undefined;
        this.latestKeyDownEventSendTimeout = null;
        this.mouseDown = 0;
        this.mouseDownButton = 0;
        this.resetMouseDownCanvas();
        this.inputEvtQueue = [];
    }

    private dispose() {
        this.registered = false;
        this.resetInput();

        for (const registeredListener of this.registeredListeners) {
            const l = registeredListener;
            if (l.useCapture) {
                l.target.removeEventListener(l.name, l.listener, l.useCapture);
            } else {
                l.target.removeEventListener(l.name, l.listener);
            }
        }
    }

    private registerUndockedCanvas(win: IDockableWindow) {
        const inputE = win.document.createElement("input");
        inputE.classList.add("ws-input-hidden");
        inputE.setAttribute("autocorrect", "off");
        inputE.setAttribute("autocthis.api.alize", "none");
        inputE.setAttribute("autocomplete", "off");
        inputE.setAttribute("data-id", "input-handler");
        win.document.querySelector('.suramadu-element .suramadu-element-content')?.appendChild(inputE);
        win.inputHandler = inputE;

        this.registerInternal(win.inputHandler, win.document, win);
    }

    private register() {
        if (this.registered) {
            return;
        }

        this.registerInternal(document.querySelector(".ws-input-hidden") as HTMLInputElement, document, window);

        this.registered = true;
    }

    private registerInternal(input: HTMLInputElement, doc: Document, win: Window) {
        this.resetInput();
        this.focusInput(input);

        this.registerGlobalListener(doc, 'mousedown', (evt) => this.handleMouseDown(evt, input), true);
        this.registerGlobalListener(doc, 'dblclick', (evt) => this.handleDblClick(evt, input));
        this.registerGlobalListener(doc, 'mousemove', (evt) => this.handleMouseMove(evt));
        this.registerGlobalListener(doc, 'mouseup', (evt) => this.handleMouseUp(evt, input));
        this.registerGlobalListener(doc.querySelector(".suramadu-element") as HTMLElement, 'wheel', (evt: WheelEvent) => this.handleWheel(evt));
        this.registerGlobalListener(doc, 'contextmenu', (evt) => this.handleContextMenu(evt));
        this.registerGlobalListener(doc, 'keydown', (evt) => this.handleKeyDown(evt));
        this.registerGlobalListener(doc, 'keypress', (evt) => this.handleKeyPress(evt));
        this.registerGlobalListener(doc, 'keyup', (evt) => this.handleKeyUp(evt));
        this.registerGlobalListener(doc, 'compositionstart', (evt) => this.handleCompositionStart(evt, input));
        this.registerGlobalListener(doc, 'compositionupdate', (evt) => this.handleCompositionUpdate(evt, input));
        this.registerGlobalListener(doc, 'compositionend', (evt) => this.handleCompositionEnd(evt, input));
        this.registerGlobalListener(doc, 'input', (evt) => this.handleInput(evt as InputEvent, input));
        this.registerGlobalListener(doc, 'cut', (evt) => this.handleCut(evt));
        this.registerGlobalListener(doc, 'copy', (evt) => this.handleCopy(evt));
        this.registerGlobalListener(doc, 'paste', (evt) => this.handlePaste(evt));
        //            registerGlobalListener(doc, 'mousedown', mouseDownEventHandler); // this should not be needed
        this.registerGlobalListener(doc, 'mouseout', (evt) => this.mouseOutEventHandler(evt, input));
        this.registerGlobalListener(doc, 'mouseover', (evt) => this.mouseOverEventHandler(evt));
        this.registerGlobalListener(win, 'blur', (evt) => { setTimeout(() => this.handleWindowBlur(evt.target as Window), 0) /* using the 'setTimout' to let the event pass the run loop*/ });
    }

    private registerGlobalListener<K extends keyof GlobalEventHandlersEventMap>(target: GlobalEventHandlers, name: K, listener: (this: GlobalEventHandlers, ev: GlobalEventHandlersEventMap[K]) => any, useCapture?: boolean) {
        target.addEventListener(name, listener, useCapture);
        this.registeredListeners.push({ target, name, listener, useCapture });
    }

    private updateFocusedHtmlCanvas(focusedElement: Element | null) {
        const htmlcanvas = isInsideWebswingHtmlCanvas(focusedElement);
        if (htmlcanvas) {
            if (this.focusedHtmlCanvas !== htmlcanvas) {
                this.sendInput({
                    focus: {
                        htmlPanelId: htmlcanvas.getAttribute("data-id")
                    }
                });
            }
            this.focusedHtmlCanvas = htmlcanvas;
        } else {
            this.focusedHtmlCanvas = undefined;
        }
    }

    private handleWindowBlur(targetWindow: Window) {
        if (targetWindow !== null) {
            this.updateFocusedHtmlCanvas(targetWindow.document.activeElement)
        }
    }

    private handleMouseDown(evt: MouseEvent, input: HTMLInputElement) {
        this.updateFocusedHtmlCanvas(evt.target as Element);

        if (isNotValidCanvasTarget(evt)) {
            return;
        }

        this.mouseDownEventHandler(evt);
        const mousePos = this.getMousePos(evt, MouseEventType.mousedown, evt.target as Element);
        this.latestMouseMoveEvent = undefined;
        this.enqueueInputEvent(mousePos);
        this.focusInput(input);
        this.sendInput();

        evt.preventDefault();
        evt.stopPropagation();

        return false;
    }

    private handleDblClick(evt: MouseEvent, input: HTMLInputElement) {
        if (isNotValidCanvasTarget(evt)) {
            return;
        }

        const mousePos = this.getMousePos(evt, MouseEventType.dblclick, evt.target as Element);
        this.latestMouseMoveEvent = undefined;
        this.enqueueInputEvent(mousePos);
        this.focusInput(input);
        this.sendInput();

        evt.preventDefault();
        evt.stopPropagation();

        return false;
    }

    private handleMouseMove(evt: MouseEvent) {
        if (isNotValidCanvasTarget(evt) && this.mouseDownCanvas == null) {
            return;
        }

        const mousePos = this.getMousePos(evt, MouseEventType.mousemove, (this.mouseDownCanvas != null ? this.mouseDownCanvas : evt.target) as Element);
        this.latestMouseMoveEvent = mousePos;

        if (isNotValidCanvasTarget(evt) && this.mouseDownCanvas != null) {
            // prevent firing mouse move events on underlying html components if dragging webswing component and mouse gets out of canvas bounds
            // this can happen when you quickly move a webswing dialog window over an html element (using compositing window manager)
            evt.preventDefault();
            evt.stopPropagation();
        }

        return false;
    }

    private handleMouseUp(evt: MouseEvent, input: HTMLInputElement) {
        // do this for the whole document, not only canvas
        const mousePos = this.getMousePos(evt, MouseEventType.mouseup, evt.target as Element);
        this.latestMouseMoveEvent = undefined;
        this.enqueueInputEvent(mousePos);

        const target = evt.target as Element;
        if (target && target.matches && target.matches("canvas.suramadu-canvas") && this.mouseDownCanvas != null) {
            // focus input only in case mouse was pressed and released over canvas
            this.focusInput(input);
        }

        this.sendInput();

        this.mouseDown = this.mouseDown & ~Math.pow(2, evt.which);
        this.mouseDownButton = 0;
        this.resetMouseDownCanvas();
        return false;
    }

    private handleWheel(evt: WheelEvent) {
        if (isNotValidCanvasTarget(evt)) {
            return;
        }

        this.pixelsScrolled += detectFF() ? evt.deltaY * 100 : evt.deltaY
        if (this.pixelsScrolled >= 100 || this.pixelsScrolled <= -100) {
            this.pixelsScrolled = 0;
            const mousePos = this.getMousePos(evt, MouseEventType.mousewheel, evt.target as Element);
            this.latestMouseMoveEvent = undefined;
            if (this.latestMouseWheelEvent != null) {
                mousePos.mouse!.wheelDelta! += this.latestMouseWheelEvent?.mouse?.wheelDelta!;
            }
            this.latestMouseWheelEvent = mousePos;
        }
        if (this.api.inputConfig.preventWheelEvent) {
            evt.preventDefault();
        }
        evt.stopPropagation();
        return false;
    }

    private handleContextMenu(evt: MouseEvent) {
        if (isNotValidCanvasTarget(evt)) {
            return;
        }

        evt.preventDefault();
        evt.stopPropagation();
        return false;
    }

    private handleKeyDown(evt: KeyboardEvent) {
        if (isNotValidCanvasTarget(evt) && isNotValidInputHandlerTarget(evt)) {
            return;
        }

        this.keyDownHandler(evt);
    }

    private handleKeyPress(evt: KeyboardEvent) {
        if (isNotValidCanvasTarget(evt) && isNotValidInputHandlerTarget(evt)) {
            return;
        }

        this.keyPressHandler(evt);
    }

    private handleKeyUp(evt: KeyboardEvent) {
        this.handleAccessibilityAccessKeys(evt);

        if (isNotValidCanvasTarget(evt) && isNotValidInputHandlerTarget(evt)) {
            return;
        }

        this.keyUpHandler(evt);
    }

    private handleCompositionStart(event: CompositionEvent, input: HTMLInputElement) {
        if (isNotValidInputHandlerTarget(event)) {
            return;
        }

        if (this.api.cfg.touchMode) {
            return;
        }

        this.compositionInput = true;
        $(input).addClass('ws-input-ime');
        $(input).removeClass('ws-input-hidden');
        input.style.font = DEFAULT_FONT;
    }

    private handleCompositionUpdate(event: CompositionEvent, input: HTMLInputElement) {
        if (isNotValidInputHandlerTarget(event)) {
            return;
        }

        if (this.api.cfg.touchMode) {
            return;
        }

        const text = event.data;
        input.style.width = this.getTextWidth(text, DEFAULT_FONT) + 'px';
    }

    private handleCompositionEnd(event: CompositionEvent, input: HTMLInputElement) {
        if (isNotValidInputHandlerTarget(event)) {
            return;
        }

        if (this.api.cfg.touchMode) {
            return;
        }

        $(input).addClass('ws-input-hidden');
        $(input).removeClass('ws-input-ime');
        input.style.width = '1px';
        const isIE = this.api.cfg.ieVersion && (this.api.cfg.ieVersion as number) <= 11;
        const target = event.target as HTMLInputElement
        this.sentWordsUsingKeypressEvent(isIE ? target.value : event.data);
        this.compositionInput = false;
        this.focusInput(input);
    }

    private handleInput(event: InputEvent, input: HTMLInputElement) {
        if (isNotValidInputHandlerTarget(event)) {
            return;
        }

        if (this.api.cfg.touchMode) {
            return;
        }

        if (this.compositionInput || !input.value) {
            return;
        }
        if (((!event.isComposing && event.inputType === 'insertText' && event.data != null)
            || (this.api.cfg.ieVersion && event.type === "input"))
            && input.value !== " ") {
            this.sentWordsUsingKeypressEvent(input.value);
            this.focusInput(input);
        }
    }

    private handleCut(event: ClipboardEvent) {
        event.preventDefault();
        event.stopPropagation();
        this.api.cut(event);
        return false;
    }

    private handleCopy(event: ClipboardEvent) {
        event.preventDefault();
        event.stopPropagation();
        this.api.copy(event);
        return false;
    }

    private handlePaste(event: ClipboardEvent) {
        event.preventDefault();
        event.stopPropagation();
        this.api.paste(event, false);
        return false;
    }

    private keyDownHandler(event: KeyboardEvent) {
        if (this.api.cfg.touchMode) {
            return;
        }

        const functionKeys = [9/*tab*/, 12/*Numpad5*/, 16/*Shift*/, 17/*ctrl*/, 18/*alt*/, 19/*pause*/, 20/*CapsLock*/, 27/*esc*/,
            32/*space*/, 33/*pgup*/, 34/*pgdown*/, 35/*end*/, 36/*home*/, 37/*left*/, 38/*up*/, 39/*right*/, 40/*down*/, 44/*prtscr*/,
            45/*insert*/, 46/*Delete*/, 91/*OSLeft*/, 92/*OSRight*/, 93/*Context*/, 145/*scrlck*/, 225/*altGraph(Linux)*/,
            112/*F1*/, 113/*F2*/, 114/*F3*/, 115/*F4*/, 116/*F5*/, 117/*F6*/, 118/*F7*/, 119/*F8*/, 120/*F9*/,
            121/*F10*/, 122/*F11*/, 123/*F12*/, 124/*F13*/, 125/*F14*/, 126/*F15*/, 127/*F16*/, 128/*F17*/, 129/*F18*/, 130/*F19*/, 131/*F20*/,
            132/*F21*/, 133/*F22*/, 134/*F23*/, 135/*F24*/];

        const kc = event.keyCode;
        if (functionKeys.indexOf(kc) !== -1) {
            if (!this.api.cfg.virtualKB && !this.isInputCaretControl(event.keyCode, event.target as Element)) {
                event.preventDefault();
                event.stopPropagation();
            }
        }
        let keyevt = this.getKBKey(KeyEventType.keydown, event);
        if (!this.isClipboardEvent(keyevt)) { // cut copy paste handled separately
            // default action prevented
            if (this.isCtrlCmd(keyevt) && !keyevt.key?.alt) {
                event.preventDefault();
            }
            if (this.latestKeyDownEvent != null) {
                this.enqueueInputEvent(this.latestKeyDownEvent);
                this.latestKeyDownEvent = undefined;
                clearTimeout(this.latestKeyDownEventSendTimeout!);
                this.latestKeyDownEventSendTimeout = null
            }
            this.latestKeyDownEvent = keyevt;
            this.latestKeyDownEventSendTimeout = setTimeout(() => {
                this.enqueueInputEvent(this.latestKeyDownEvent);
                this.latestKeyDownEvent = undefined;
            }, 100);

            // generate keypress event for alt+key events
            if (!detectMac() && !keyevt.key?.ctrl && keyevt.key?.alt && functionKeys.indexOf(kc) === -1) {
                event.preventDefault();
                event.stopPropagation();
                keyevt = this.getKBKey(KeyEventType.keypress, event);
                if (keyevt.key) {
                    if (event.key.length === 1) {
                        keyevt.key.character = event.key.charCodeAt(0);
                    } else {
                        const key = keyevt.key?.keycode!;
                        keyevt.key.character = (!keyevt.key?.shift && (key >= 65 && key <= 90)) ? key + 32 : key;
                    }
                }
                this.enqueueInputEvent(keyevt);
            }

            // prevent firefox password manager
            if(event.keyCode==8){
                event.stopImmediatePropagation();
                event.preventDefault();
            }
        }
        return false;
    }

    private isInputCaretControl(kc: number, input: Element) {
        if ($(input).is("[aria-hidden=true]")) {
            return false;
        }
        return this.caretControlKeys.indexOf(kc) !== -1;
    }

    private keyPressHandler(event: KeyboardEvent) {
        if (this.api.cfg.touchMode) {
            return;
        }

        const keyevt = this.getKBKey(KeyEventType.keypress, event);
        if (!this.isClipboardEvent(keyevt)) { // cut copy paste handled separately
            event.preventDefault();
            event.stopPropagation();
            if (this.latestKeyDownEvent?.key) {
                this.latestKeyDownEvent.key.character = keyevt.key?.character;
                this.enqueueInputEvent(this.latestKeyDownEvent);
                this.latestKeyDownEvent = undefined;
                clearTimeout(this.latestKeyDownEventSendTimeout!);
                this.latestKeyDownEventSendTimeout = null
            }
            this.enqueueInputEvent(keyevt);
        }

        if (detectMac() && keyevt.key) {
            keyevt.key.alt = false
        }

        return false;
    }

    private keyUpHandler(event: KeyboardEvent) {
        if (this.api.cfg.touchMode) {
            return;
        }

        const keyevt = this.getKBKey(KeyEventType.keyup, event);

        if (this.latestKeyDownEvent != null && this.latestKeyDownEvent.key?.keycode === keyevt.key?.keycode) {
            this.enqueueInputEvent(this.latestKeyDownEvent);
            this.latestKeyDownEvent = undefined;
            clearTimeout(this.latestKeyDownEventSendTimeout!);
            this.latestKeyDownEventSendTimeout = null
        }

        if (!this.isClipboardEvent(keyevt)) { // cut copy paste handled separately
            if (!this.api.cfg.virtualKB && !this.isInputCaretControl(event.keyCode, event.target as Element)) {
                event.preventDefault();
                event.stopPropagation();
            }
            this.enqueueInputEvent(keyevt);
            this.sendInput();
        }
        return false;
    }

    private isClipboardEvent(evt: appFrameProtoIn.IInputEventMsgInProto) {
        const ctrlCmd = this.isCtrlCmd(evt);

        const isCutCopyKey = KeyEventType.keypress === evt.key?.type ? (evt.key.character === 120 || evt.key.character === 24 || evt.key.character === 99) : (evt.key?.character === 88 || evt.key?.character === 67);
        const isCutCopyEvt = ctrlCmd && !evt.key?.alt && !evt.key?.shift && isCutCopyKey;

        const isPasteKey = KeyEventType.keypress === evt.key?.type ? (evt.key.character === 118 || evt.key.character === 22 || evt.key.character === 86) : (evt.key?.character === 86);
        const isPasteEvt = ctrlCmd && !evt.key?.alt && !evt.key?.shift && isPasteKey;

        return isPasteEvt || isCutCopyEvt;
    }

    private isCtrlCmd(evt: appFrameProtoIn.IInputEventMsgInProto) {
        return this.api.cfg.isMac ? evt.key?.meta : evt.key?.ctrl;
    }

    private getTextWidth(text: string, font: string) {
        const canvas = this.api.getCanvas();
        const ctx = canvas.getContext("2d")!;
        ctx.save();
        ctx.font = font;
        const metrics = ctx.measureText(text);
        ctx.restore();
        return Math.ceil(metrics.width) + 5;
    }


    private handleAccessibilityAccessKeys(event: KeyboardEvent) {
        if (this.ctrl3Timeout != null) {
            clearTimeout(this.ctrl3Timeout);
            this.ctrl3Timeout = undefined;
        }

        const now = new Date().getTime();
        if (event.keyCode === 17) {
            // accessibility CTRL access key
            if (now - this.lastCtrlKey < 1000) {
                this.ctrlCounter++;
            } else {
                this.ctrlCounter = 0;
            }

            this.lastCtrlKey = now;
        } else {
            this.ctrlCounter = 0;
        }

        if ((this.ctrlCounter + 1) === 3 && this.api.isAccessibilityEnabled()) {
            this.ctrl3Timeout = setTimeout(() => {
                this.ctrlCounter = 0;
                this.api.sendSimpleEvent({
                    type: SimpleEventType.requestWindowSwitchList
                });
            }, 1000);
        }

        if ((this.ctrlCounter + 1) === 5) {
            this.api.toggleAccessibility();
            this.ctrlCounter = 0;
        }
    }

    private setMouseDownCanvas(evt: MouseEvent) {
        this.mouseDownCanvas = isWebswingCanvas(evt.target as Element) ? evt.target : null;
        if (isWebswingCanvas(evt.target as Element)) {
            $('.suramadu-html-canvas iframe').addClass('suramadu-iframe-muted-while-dragging');
        }
    }

    private resetMouseDownCanvas() {
        this.mouseDownCanvas = undefined;
        $('.suramadu-html-canvas iframe').removeClass('suramadu-iframe-muted-while-dragging');
    }


    private mouseDownEventHandler(evt: MouseEvent) {
        this.mouseDown = this.mouseDown | Math.pow(2, evt.which);
        this.mouseDownButton = evt.which;
        this.setMouseDownCanvas(evt);
    }

    private mouseOutEventHandler(evt: MouseEvent, input: HTMLInputElement) {
        if (isWebswingCanvas(evt.target as Element) || isWebswingCanvas(evt.relatedTarget as Element) || this.mouseDownCanvas != null) {
            return;
        }

        this.mouseOutEventHandlerImpl(evt, input);
    }

    private mouseOutEventHandlerImpl(evt: MouseEvent, input: HTMLInputElement) {
        if (this.mouseDown !== 0 && this.api.cfg.hasControl && this.api.cfg.canPaint && !this.api.cfg.mirrorMode && !this.api.cfg.touchMode && !this.compositionInput) {
            const mousePos = this.getMousePos(evt, MouseEventType.mouseup, evt.target as Element);
            // when a new web page pops after user click, mouseup will send twice
            const canvasSize = this.api.getCanvas().getBoundingClientRect();

            if (mousePos.mouse?.x! < 0 || mousePos.mouse?.x! > canvasSize.width) {
                mousePos.mouse!.x = -1;
            }
            if (mousePos.mouse?.y! < 0 || mousePos.mouse?.y! > canvasSize.height) {
                mousePos.mouse!.y = -1;
            }
            this.latestMouseMoveEvent = undefined;
            this.enqueueInputEvent(mousePos);
            this.focusInput(input);
            this.sendInput();
        }
        this.mouseDown = 0;
        this.mouseDownButton = 0;
        this.resetMouseDownCanvas();
    }

    private mouseOverEventHandler(evt: MouseEvent) {
    	const target = evt.target as Element;
        if (target && target.matches && (target.matches("#canvas-overlay") || target.matches(".canvas-component"))) {
        	// ignore for canvas-overlay shown in test tool
        	return;
        }

        const newMouseDown = Math.pow(2, evt.which);
        if (this.mouseDownButton !== 0 && this.mouseDownButton !== evt.which && this.mouseDown !== newMouseDown) {
            // mouse has been released outside window (iframe)
            const mousePos = this.getMousePos(evt, MouseEventType.mouseup, evt.target as Element);
            // simulate release of previously pressed mouse button
            mousePos.mouse!.buttons = this.mouseDown;
            mousePos.mouse!.button = this.mouseDownButton;

            this.latestMouseMoveEvent = undefined;
            this.enqueueInputEvent(mousePos);

            this.sendInput();

            this.mouseDown = 0;
            this.mouseDownButton = 0;
            this.resetMouseDownCanvas();
        }
    }

    private focusInput(input: HTMLInputElement) {
        this.api.focusInput(input);
    }

    private sentWordsUsingKeypressEvent(data: string) {
        for (let i = 0, length = data.length; i < length; i++) {
            this.inputEvtQueue.push({
                key: {
                    type: KeyEventType.keypress,
                    character: data.charCodeAt(i),
                    keycode: 0,
                }
            });
        }
    }

    private getMousePos(evt: MouseEvent | WheelEvent, type: appFrameProtoIn.MouseEventMsgInProto.MouseEventTypeProto, targetElement: Element | null): appFrameProtoIn.IInputEventMsgInProto {
        let rect;
        if (targetElement && targetElement != null && targetElement.parentNode && targetElement.parentNode != null && (targetElement.parentNode as Element).getBoundingClientRect) {
            if ($(targetElement).is(".internal")) {
                rect = (targetElement.parentNode?.parentNode as Element).getBoundingClientRect();
            } else {
                rect = (targetElement.parentNode as Element).getBoundingClientRect();
            }
        } else {
            rect = this.api.getCanvas().getBoundingClientRect();
        }

        let winId;
        if (targetElement && targetElement.matches && targetElement.matches("canvas.suramadu-canvas") && $(targetElement).data("id") && $(targetElement).data("id") !== "canvas") {
            // for a composition canvas window send winId
            if ($(targetElement).is(".internal")) {
                // internal window must use its parent as mouse events target
                if (targetElement.parentNode && $(targetElement.parentNode).data("ownerid")) {
                    winId = $(targetElement.parentNode).data("ownerid");
                }
            } else {
                winId = $(targetElement).data("id");
                if (targetElement.ownerDocument?.defaultView) {
                    (targetElement.ownerDocument.defaultView as IDockableWindow).pagePosition = { x: evt.screenX - evt.clientX, y: evt.screenY - evt.clientY };
                }
            }
        }

        // return relative mouse position
        let mouseX = Math.round(evt.clientX - rect.left);
        let mouseY = Math.round(evt.clientY - rect.top);

        let delta = 0;
        if (evt.type === 'wheel' && type === MouseEventType.mousewheel) {
            if (detectFF()) {
                delta = -Math.max(-1, Math.min(1, (-(evt as WheelEvent).deltaY * 100)));
            } else if ((detectIE() as number) <= 11) {
                delta = -Math.max(-1, Math.min(1, (-(evt as WheelEvent).deltaY)));
            } else {
                delta = -Math.max(-1, Math.min(1, ((evt as any).wheelDelta || -evt.detail)));
            }
        }

        if (type === MouseEventType.mouseup && (!targetElement || !targetElement.matches || !targetElement.matches("canvas.suramadu-canvas"))) {
            // fix for detached composition canvas windows that might overlay same coordinates space, when clicked outside a canvas
            mouseX = -1;
            mouseY = -1;
        }

        if (type === MouseEventType.mouseup && targetElement && targetElement.matches && !targetElement.matches("canvas.suramadu-canvas") && targetElement.closest(".suramadu-html-canvas") != null) {
            // fix for mouseup over an HtmlWindow div content
            rect = (targetElement.closest(".suramadu-html-canvas")?.parentNode as Element)?.getBoundingClientRect();

            mouseX = Math.round(evt.clientX - rect.left);
            mouseY = Math.round(evt.clientY - rect.top);
        }

        return {
            mouse: {
                x: mouseX,
                y: mouseY,
                type,
                wheelDelta: delta,
                button: type === MouseEventType.mousemove ? 0 : evt.which,
                buttons: this.mouseDown,
                ctrl: evt.ctrlKey,
                alt: evt.altKey,
                shift: evt.shiftKey,
                meta: evt.metaKey,
                winId: winId || "",
                timeMilis: Date.now() % 86400000 // 86400000 day in milis
            }
        };
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

}


function isWebswingCanvas(e: Element | null) {
    return e && e.matches && e.matches("canvas.suramadu-canvas");
}

function isInputHandler(e: Element | null) {
    return e && e.matches && (e.matches("input.ws-input-hidden") || e.matches("input.ws-input-ime") || e.matches("input.aria-element") || e.matches("textarea.aria-element"));
}

function isInsideWebswingHtmlCanvas(e: Element | null) {
    while (e && e.matches) { // TODO && e !== window
        if (e.matches("div.suramadu-html-canvas")) {
            return e;
        }
        e = e.parentNode as Element;
    }
    return false;
}

function isAriaElement(e: Element | null) {
    return e && e.matches && e.matches(".aria-element");
}


function isNotValidCanvasTarget(evt: Event) {
    return !(isWebswingCanvas(evt.target as Element) || isAriaElement(evt.target as Element));
}

function isNotValidInputHandlerTarget(evt: Event) {
    return !isInputHandler(evt.target as Element);
}