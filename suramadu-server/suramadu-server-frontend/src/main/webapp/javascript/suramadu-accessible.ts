import switcherTemplate from './templates/windowSwitcher.html'
import { ModuleDef } from './suramadu-inject';
import { appFrameProtoOut } from "./proto/proto.out";

export const accessibleInjectable = {
	cfg: 'webswing.config' as const,
	send: 'socket.send' as const,
	translate: 'translate.translate' as const,
	sendHandshake: 'base.handshake' as const,
	getFocusedWindow: 'base.getFocusedWindow' as const,
	getAllWindows: 'base.getAllWindows' as const,
	closeWindow: 'base.closeWindow' as const,
	focusDefault: 'focusManager.focusDefault' as const
}

export interface IAccessibleService {
	'accessible.dispose': () => void,
	'accessible.toggle': (silent?: boolean) => void,
	'accessible.isEnabled': () => boolean,
	'accessible.handleAccessible': (accessibleMsg: appFrameProtoOut.IAccessibilityMsgOutProto, rootElement: JQuery<HTMLElement>) => void,
	'accessible.showWindowSwitcher': (rootElement: JQuery<HTMLElement>, windowSwitchList: appFrameProtoOut.IWindowSwitchMsgOutProto[]) => void
}

export type AccessibleMsg = appFrameProtoOut.IAccessibilityMsgOutProto & {
	rootElement: JQuery<HTMLElement>
}

export class AccessibleModule extends ModuleDef<typeof accessibleInjectable, IAccessibleService> {
	public switcherHtml?: string;
	public enabled = false;
	public ariaBusyTimeout?: number;
	public windowSwitcher?: JQuery<HTMLElement>;

	public ready = () => {
		if (window.localStorage) {
			const previousState = localStorage.getItem('accessibilityEnabled');
			if (previousState === 'true') {
				this.toggle(true);
			}
		}
		this.switcherHtml = this.api.translate(switcherTemplate);
	};

	public provides() {
		return {
			'accessible.dispose': this.dispose,
			'accessible.toggle': this.toggle,
			'accessible.isEnabled': this.isEnabled,
			'accessible.handleAccessible': this.handleAccessible,
			'accessible.showWindowSwitcher': this.showWindowSwitcher
		}
	}

	public toggle(silent?: boolean) {
		this.enabled = !this.enabled;
		this.api.sendHandshake();

		if (this.enabled) {
			if (!silent) {
				localStorage.setItem('accessibilityEnabled', 'true');
				this.init();
			}
		} else {
			localStorage.setItem('accessibilityEnabled', 'false');
			this.destroy();
		}
	}

	public init() {
		const rootElement = $(this.api.getFocusedWindow().document).find(".suramadu-element-content");
		setTimeout(() => {
			rootElement.find("#aria-live-log").text(this.api.translate('accessibility.turnedOn'));
		}, 1);
	}

	public dispose() {
		this.removeAllAriaElements();
	}

	public destroy() {
		const rootElement = ($(this.api.getFocusedWindow().document).find(".suramadu-element-content") as unknown) as JQuery<HTMLElement>;
		getLiveLog(rootElement).text(this.api.translate('accessibility.turnedOff'));

		this.removeAllAriaElements();

		this.api.focusDefault();
	}

	public removeAllAriaElements() {
		const wins = this.api.getAllWindows();
		for (const win of wins) {
			$(win.document).find(".aria-element, .aria-parent").remove();
		}
	}

	public isEnabled() {
		return this.enabled;
	}

	public handleAccessible(accessibleMsg: appFrameProtoOut.IAccessibilityMsgOutProto, rootElement: JQuery<HTMLElement>) {
		if (!this.enabled) {
			return;
		}

		if (this.ariaBusyTimeout != null) {
			clearTimeout(this.ariaBusyTimeout);
		}

		let delay = 250;
		if (accessibleMsg.role === "textbox") {
			delay = 1;
		}

		this.ariaBusyTimeout = setTimeout(() => {
			const accessible: AccessibleMsg = { ...accessibleMsg, rootElement };
			this.ariaBusyTimeout = undefined;
			switch (accessible.role) {
				case "button":
					handleButton(accessible);
					break;
				case "decorationbutton":
					handleDecorationbutton(accessible, this.api.translate(accessible.text || ''));
					break;
				case "checkbox":
					handleCheckbox(accessible);
					break;
				case "menuitemcheckbox":
					handleMenuitemcheckbox(accessible);
					break;
				case "combobox":
					handleCombobox(accessible);
					break;
				case "progressbar":
					handleProgressbar(accessible);
					break;
				case "radio":
					handleRadiobutton(accessible);
					break;
				case "menuitemradio":
					handleMenuitemradio(accessible);
					break;
				case "scrollbar":
					handleScrollbar(accessible);
					break;
				case "slider":
					handleSlider(accessible);
					break;
				case "label":
					handleLabel(accessible);
					break;
				case "tooltip":
					handleTooltip(accessible);
					break;
				case "tab":
				case "tablist":
					handleTab(accessible);
					break;
				case "list":
					handleList(accessible);
					break;
				case "listitem":
					handleListitem(accessible);
					break;
				case "treeitem":
					handleTreeitem(accessible);
					break;
				case "gridcell":
					handleGridcell(accessible);
					break;
				case "textbox":
					handleTextbox(accessible);
					break;
				case "img":
					handleImage(accessible);
					break;
				case "menuitem":
					handleMenuitem(accessible);
					break;
				case "menu":
				case "tree":
				case "menubar":
				case "table":
				case "panel":
				case "alertdialog":
				case "dialog":
					handleEmptyElement(accessible);
					break;
				default:
					handleEmptyElement(accessible);
					break;
			}

			if (rootElement.find(".aria-element.aria-id.new").length > 0) {
				//        			// remove old aria-element if there is a new one
				rootElement.find(".aria-element.aria-id:not(.new)").remove();
			}
			rootElement.find(".aria-element.aria-id.new").removeClass("new");

			handleHierarchy(accessible);

			this.api.focusDefault();
		}, delay);
	}


	public showWindowSwitcher(rootElement: JQuery<HTMLElement>, windowSwitchList: appFrameProtoOut.IWindowSwitchMsgOutProto[]) {
		if (this.windowSwitcher == null) {
			rootElement.append(this.switcherHtml || '');
			this.windowSwitcher = rootElement.find('div[data-id="windowSwitcher"]');
		}

		const content = this.windowSwitcher.find(".ws-window-switcher-content");
		content.empty();

		let firstItem;

		for (let i = 0; i < windowSwitchList.length; i++) {
			const win = windowSwitchList[i];

			const wrapper = jQuery("<div />");

			const item = jQuery("<input type='radio' name='windowSwitcher' />");
			item.attr("id", "windowSwitcher-" + i);
			item.data("winid", win.id);

			let modalNote;

			if (win.modalBlocked) {
				item.attr("aria-describedby", "switcher-modal-blocked-description");
				modalNote = jQuery("<span aria-hidden='true' />").text(this.api.translate("accessibility.switcher.modalBlocked"));
			} else {
				item.on("keyup", this.switcherRadioKeyHandler);
			}

			if (i === 0) {
				firstItem = item;
				item.prop("checked", true);
			}

			const label = jQuery("<label />");
			label.attr("for", "windowSwitcher-" + i);
			if (win.title) {
				label.text(win.title);
			} else {
				label.text(this.api.translate("accessibility.switcher.untitled"));
			}

			wrapper.append(item);
			wrapper.append(label);
			if (modalNote) {
				wrapper.append(modalNote);
			}

			content.append(wrapper);
		}

		if (firstItem) {
			// this focuses first item in the dialog
			// do not go through focusManager, this is inside a .catch-focus element
			firstItem[0].focus({ preventScroll: true });
		}

		const closeBtn = this.windowSwitcher.find('button[data-id="closeBtn"]');
		closeBtn.on('click', () => {
			this.hideWindowSwitcher();
		});
	}

	public switcherRadioKeyHandler(evt: JQuery.KeyUpEvent) {
		if (evt.keyCode === 13) {
			// enter
			const winId = this.windowSwitcher?.find("input[type=radio]:checked").data("winid");
			this.switchWindow(winId);
			this.hideWindowSwitcher();
		} else if (evt.keyCode === 88) {
			// X
			const winId = this.windowSwitcher?.find("input[type=radio]:checked").data("winid");
			this.closeWindow(winId);
			this.hideWindowSwitcher();
		}
	}

	public hideWindowSwitcher() {
		if (this.windowSwitcher != null) {
			this.windowSwitcher.hide("fast");
			this.windowSwitcher.remove();
			this.windowSwitcher = undefined;
		}
		this.api.focusDefault();
	}

	public switchWindow(winId?: string | null) {
		this.api.send({
			events: [{
				focus: {
					windowId: winId
				}
			}]
		});
	}

	public closeWindow(winId: string) {
		this.api.closeWindow(winId);
	}

}

function handleButton(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<button class='aria-element aria-id new focusable' tabindex='0' id='aria-element-" + id + "' />");

	el.attr("aria-label", accessible.text!).text(accessible.text!);
	if (accessible.toggle) {
		el.attr("aria-pressed", String(accessible.states != null && accessible.states.indexOf('CHECKED') !== -1));
	} else {
		el.removeAttr("aria-pressed");
	}

	processAccessibleElement(el, accessible);
}

function handleDecorationbutton(accessible: AccessibleMsg, text: string) {
	accessible.role = "button";
	accessible.text = text;

	handleButton(accessible);
}

function handleCheckbox(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new focusable' tabindex='0' id='aria-element-" + id + "' />");

	el.attr("aria-label", accessible.text!).text(accessible.text!);
	el.attr("aria-checked", String(accessible.states != null && accessible.states.indexOf('CHECKED') !== -1));

	processAccessibleElement(el, accessible);
}

function handleMenuitemcheckbox(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new' tabindex='0' id='aria-element-" + id + "' />");
	el.attr("role", "menu");
	el.attr("aria-owns", "aria-menuitemcheckbox");
	el.removeClass("focusable");

	const itemEl = $("<div class='aria-element focusable' tabindex='0' id='aria-menuitemcheckbox' />");
	itemEl.attr("role", "menuitemcheckbox");

	itemEl.attr("aria-label", accessible.text as string).text(accessible.text as string);
	itemEl.attr("aria-checked", String(accessible.states != null && accessible.states.indexOf('CHECKED') !== -1));
	itemEl.attr("aria-haspopup", String(false));

	itemEl.attr("aria-setsize", accessible.size!);
	itemEl.attr("aria-posinset", accessible.position!);

	// if enabled state is not present, it is disabled
	itemEl.attr("aria-disabled", String(accessible.states != null && accessible.states.indexOf('ENABLED') === -1));

	el.empty();
	el.append(itemEl);

	el.css({
		width: accessible.width!,
		height: accessible.height!,
		top: accessible.screenY + "px",
		left: accessible.screenX + "px"
	});

	handleElementDescription(accessible, itemEl);

	if (el.is(".new")) {
		appendAriaElement(el, accessible.rootElement);
	}
}

function handleCombobox(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new focusable' tabindex='0' id='aria-element-" + id + "' />");

	el.attr("aria-label", accessible.text!).text(accessible.text!);
	el.attr("aria-controls", "aria-listbox");
	el.attr("aria-haspopup", "listbox");
	el.attr("aria-expanded", String(accessible.states != null && accessible.states.indexOf('EXPANDED') !== -1));

	let listbox;
	if (el.find("#aria-listbox").length) {
		listbox = el.find("#aria-listbox");
		listbox.empty();
	} else {
		listbox = $("<ul role='listbox' id='aria-listbox' style='list-style-type: none;' />");
		el.append(listbox);
	}

	if (accessible.size != null && accessible.size > 0) {
		const item = $("<li id='aria-option' role='option' />");
		item.text(accessible.value!);
		item.attr("aria-selected", String(true));
		item.attr("aria-setsize", accessible.size);
		item.attr("aria-posinset", String(accessible.position));
		listbox.append(item);

		el.attr("aria-activedescendant", "aria-option");
	}

	processAccessibleElement(el, accessible);
}

function handleTab(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new focusable' tabindex='0' id='aria-element-" + id + "' />");

	el.attr("role", "tab");
	el.attr("aria-label", accessible.text!).text(accessible.text!);
	el.attr("aria-selected", String(accessible.states != null && accessible.states.indexOf('SELECTED') !== -1));

	if (accessible.size != null && accessible.size > 0) {
		el.attr("aria-setsize", accessible.size);
		el.attr("aria-posinset", accessible.position!);
	}

	// if enabled state is not present, it is disabled
	el.attr("aria-disabled", String(accessible.states != null && accessible.states.indexOf('ENABLED') === -1));

	el.css({
		width: accessible.width!,
		height: accessible.height!,
		top: accessible.screenY + "px",
		left: accessible.screenX + "px"
	});

	handleElementDescription(accessible, el);

	if (el.is(".new")) {
		appendAriaElement(el, accessible.rootElement);
	}
}

function handleProgressbar(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new focusable' tabindex='0' id='aria-element-" + id + "' />");

	el.attr("aria-valuemin", accessible.min!)
		.attr("aria-valuemax", accessible.max!)
		.attr("aria-valuetext", accessible.val!)
		.attr("aria-valuenow", accessible.val!)
		.attr("aria-label", accessible.text!);

	processAccessibleElement(el, accessible);
}

function handleRadiobutton(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new focusable' tabindex='0' id='aria-element-" + id + "' />");

	el.attr("aria-setsize", accessible.size!);
	el.attr("aria-posinset", accessible.position!);
	el.attr("aria-label", accessible.text!).text(accessible.text!);
	el.attr("aria-checked", String(accessible.states != null && accessible.states.indexOf('CHECKED') !== -1));

	processAccessibleElement(el, accessible);
}

function handleMenuitemradio(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new' tabindex='0' id='aria-element-" + id + "' />");
	el.attr("role", "menu");
	el.attr("aria-owns", "aria-menuitemradio");
	el.removeClass("focusable");

	const itemEl = $("<div class='aria-element focusable' tabindex='0' id='aria-menuitemradio' />");
	itemEl.attr("role", "menuitemradio");

	itemEl.attr("aria-label", accessible.text!).text(accessible.text!);
	itemEl.attr("aria-checked", String(accessible.states != null && accessible.states.indexOf('CHECKED') !== -1));
	itemEl.attr("aria-haspopup", 'false');

	itemEl.attr("aria-setsize", accessible.size!);
	itemEl.attr("aria-posinset", accessible.position!);

	// if enabled state is not present, it is disabled
	itemEl.attr("aria-disabled", String(accessible.states != null && accessible.states.indexOf('ENABLED') === -1));

	el.empty();
	el.append(itemEl);

	el.css({
		width: accessible.width!,
		height: accessible.height!,
		top: accessible.screenY + "px",
		left: accessible.screenX + "px"
	});

	handleElementDescription(accessible, itemEl);

	if (el.is(".new")) {
		appendAriaElement(el, accessible.rootElement);
	}
}

function handleScrollbar(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new focusable' tabindex='0' id='aria-element-" + id + "' />");

	el.attr("aria-valuemin", accessible.min!)
		.attr("aria-valuemax", accessible.max!)
		.attr("aria-valuenow", accessible.val!);

	if (accessible.states != null && accessible.states.indexOf('VERTICAL') !== -1) {
		el.attr("aria-orientation", "vertical");
	} else if (accessible.states != null && accessible.states.indexOf('HORIZONTAL') !== -1) {
		el.attr("aria-orientation", "horizontal");
	}

	processAccessibleElement(el, accessible);
}

function handleSlider(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new focusable' tabindex='0' id='aria-element-" + id + "' />");

	el.attr("aria-valuemin", accessible.min!)
		.attr("aria-valuemax", accessible.max!)
		.attr("aria-valuetext", accessible.val!)
		.attr("aria-valuenow", accessible.val!)
		.attr("aria-label", accessible.text!);

	processAccessibleElement(el, accessible);
}

function handleLabel(accessible: AccessibleMsg) {
	// send text to aria live
	getLiveLog(accessible.rootElement).text(accessible.text!);
}

function handleTooltip(accessible: AccessibleMsg) {
	let text = accessible.text;
	if (!text || text.length === 0) {
		text = accessible.description;
	}
	if (text && text.length > 0) {
		// send text to aria live
		getLiveLog(accessible.rootElement).text(text);
	}
}

function handleImage(accessible: AccessibleMsg) {
	let text = accessible.text;
	if (!text || text.length === 0) {
		text = accessible.description;
	}

	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new focusable' tabindex='0' id='aria-element-" + id + "' />");

	el.attr("aria-label", text!);

	processAccessibleElement(el, accessible);
}

function handleTextbox(accessible: AccessibleMsg) {
	const id = accessible.id;
	let el;

	if (accessible.states != null && accessible.states.indexOf('SINGLE_LINE') === -1) {
		// multi line
		el = findAriaElementById(id, accessible.rootElement) || $("<textarea class='aria-element aria-id new focusable' tabindex='0' spellcheck='false' id='aria-element-" + id + "'></textarea>");
		el.attr("aria-multiline", 'true');
		(el[0] as HTMLTextAreaElement).value = accessible.text!;
	} else {
		// single line
		if (accessible.password) {
			el = findAriaElementById(id, accessible.rootElement) || $("<input type='password' class='aria-element aria-id new focusable' tabindex='0' spellcheck='false' autocomplete='off' id='aria-element-" + id + "'/>");
		} else {
			el = findAriaElementById(id, accessible.rootElement) || $("<input type='text' class='aria-element aria-id new focusable' tabindex='0' spellcheck='false' autocomplete='off' id='aria-element-" + id + "'/>");
		}

		el.attr("aria-multiline", 'false');
		let accessibleText = " ";
		if (accessible.text && accessible.text.length) {
			accessibleText = accessible.text;
		}
		(el[0] as HTMLInputElement).value = accessibleText;
	}

	if (accessible.rows) {
		el.attr("rows", accessible.rows);
		// TODO dpr ?
		el.css("line-height", accessible.rowheight + 'px');
	}

	if (accessible.selstart != null && accessible.selstart >= 0) {
		(el[0] as HTMLInputElement).setSelectionRange(accessible.selstart, accessible.selend!);
	}
	el.attr("aria-readonly", String(accessible.states != null && accessible.states.indexOf('EDITABLE') === -1));

	processAccessibleElement(el, accessible);
}

function handleMenuitem(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new' tabindex='0' id='aria-element-" + id + "' />");
	el.attr("role", "menu");
	el.attr("aria-owns", "aria-menuitem");
	el.removeClass("focusable");

	const itemEl = $("<div class='aria-element focusable' tabindex='0' id='aria-menuitem' />");
	itemEl.attr("role", "menuitem");

	itemEl.attr("aria-label", accessible.text!).text(accessible.text!);

	itemEl.attr("aria-setsize", accessible.size!);
	itemEl.attr("aria-posinset", accessible.position!);

	// if enabled state is not present, it is disabled
	itemEl.attr("aria-disabled", String(accessible.states != null && accessible.states.indexOf('ENABLED') === -1));

	el.empty();
	el.append(itemEl);

	el.css({
		width: accessible.width!,
		height: accessible.height!,
		top: accessible.screenY + "px",
		left: accessible.screenX + "px"
	});

	handleElementDescription(accessible, itemEl);

	if (el.is(".new")) {
		appendAriaElement(el, accessible.rootElement);
	}
}

function handleList(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new focusable' tabindex='0' id='aria-element-" + id + "' />");

	el.attr("aria-label", accessible.text!).text(accessible.text!);
	el.attr("aria-multiselectable", String(accessible.states != null && accessible.states.indexOf('MULTISELECTABLE') !== -1));

	processAccessibleElement(el, accessible);
}

function handleListitem(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new' tabindex='0' id='aria-element-" + id + "' />");
	el.attr("role", "listbox");
	el.attr("aria-owns", "aria-listitem");
	el.removeClass("focusable");
	el.attr("aria-multiselectable", String(accessible.states != null && accessible.states.indexOf('MULTISELECTABLE') !== -1));

	const itemEl = $("<div class='aria-element focusable' tabindex='0' id='aria-listitem' />");
	itemEl.attr("role", "option");

	itemEl.attr("aria-label", accessible.text!).text(accessible.text!);
	itemEl.attr("aria-selected", String(accessible.states != null && accessible.states.indexOf('SELECTED') !== -1));

	itemEl.attr("aria-setsize", accessible.size!);
	itemEl.attr("aria-posinset", accessible.position!);

	// if enabled state is not present, it is disabled
	itemEl.attr("aria-disabled", String(accessible.states != null && accessible.states.indexOf('ENABLED') === -1));

	el.empty();
	el.append(itemEl);

	el.css({
		width: accessible.width!,
		height: accessible.height!,
		top: accessible.screenY + "px",
		left: accessible.screenX + "px"
	});

	handleElementDescription(accessible, itemEl);

	if (el.is(".new")) {
		appendAriaElement(el, accessible.rootElement);
	}
}

function handleTreeitem(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new' tabindex='0' id='aria-element-" + id + "' />");
	el.attr("role", "tree");
	el.attr("aria-owns", "aria-treeitem");
	el.removeClass("focusable");
	el.attr("aria-multiselectable", String(accessible.states != null && accessible.states.indexOf('MULTISELECTABLE') !== -1));

	const itemEl = $("<div class='aria-element focusable' tabindex='0' id='aria-treeitem' />");
	itemEl.attr("role", "treeitem");

	itemEl.attr("aria-label", accessible.text!).text(accessible.text!);
	itemEl.attr("aria-selected", String(accessible.states != null && accessible.states.indexOf('SELECTED') !== -1));
	itemEl.attr("aria-expanded", String(accessible.states != null && accessible.states.indexOf('EXPANDED') !== -1));

	itemEl.attr("aria-level", accessible.level!);
	itemEl.attr("aria-setsize", accessible.size!);
	itemEl.attr("aria-posinset", accessible.position!);

	// if enabled state is not present, it is disabled
	itemEl.attr("aria-disabled", String(accessible.states != null && accessible.states.indexOf('ENABLED') === -1));

	el.empty();
	el.append(itemEl);

	el.css({
		width: accessible.width!,
		height: accessible.height!,
		top: accessible.screenY + "px",
		left: accessible.screenX + "px"
	});

	handleElementDescription(accessible, itemEl);

	if (el.is(".new")) {
		appendAriaElement(el, accessible.rootElement);
	}
}

function handleGridcell(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new' tabindex='-1' id='aria-element-" + id + "' />");
	el.attr("role", "grid");
	el.attr("aria-owns", "aria-chrow aria-row");
	el.removeClass("focusable");

	el.attr("aria-rowcount", accessible.rowcount!);
	el.attr("aria-colcount", accessible.colcount!);
	el.attr("aria-multiselectable", String(accessible.states != null && accessible.states.indexOf('MULTISELECTABLE') !== -1));

	const chRowEl = $("<div class='aria-element' tabindex='-1' id='aria-chrow' />");
	chRowEl.attr("role", "row");
	chRowEl.attr("aria-owns", "aria-columnheader");

	const chEl = $("<div class='aria-element' tabindex='-1' id='aria-columnheader' />");
	chEl.attr("role", "columnheader");

	chEl.attr("aria-label", accessible.columnheader!).text(accessible.columnheader!);
	chEl.attr("aria-colindex", accessible.colindex!);

	const rowEl = $("<div class='aria-element' tabindex='-1' id='aria-row' />");
	rowEl.attr("role", "row");
	rowEl.attr("aria-owns", "aria-cell");

	const cellEl = $("<div class='aria-element focusable' tabindex='0' id='aria-cell' />");
	cellEl.attr("role", "gridcell");

	cellEl.attr("aria-label", accessible.text!).text(accessible.text!);
	cellEl.attr("aria-selected", String(accessible.states != null && accessible.states.indexOf('SELECTED') !== -1));
	cellEl.attr("aria-readonly", String(accessible.states != null && accessible.states.indexOf('EDITABLE') === -1));

	cellEl.attr("aria-colindex", accessible.colindex!);
	cellEl.attr("aria-rowindex", accessible.rowindex!);

	// if enabled state is not present, it is disabled
	cellEl.attr("aria-disabled", String(accessible.states != null && accessible.states.indexOf('ENABLED') === -1));

	el.empty();
	chRowEl.append(chEl);
	el.append(chRowEl);
	rowEl.append(cellEl);
	el.append(rowEl);

	el.css({
		width: accessible.width!,
		height: accessible.height!,
		top: accessible.screenY + "px",
		left: accessible.screenX + "px"
	});

	handleElementDescription(accessible, cellEl);

	if (el.is(".new")) {
		appendAriaElement(el, accessible.rootElement);
	}
}

function handleEmptyElement(accessible: AccessibleMsg) {
	const id = accessible.id;
	const el = findAriaElementById(id, accessible.rootElement) || $("<div class='aria-element aria-id new focusable' tabindex='0' id='aria-element-" + id + "' />");

	if (accessible.text) {
		el.attr("aria-label", accessible.text).text(accessible.text);
	}

	processAccessibleElement(el, accessible);
}

function processAccessibleElement(el: JQuery<HTMLElement>, accessible: AccessibleMsg) {
	el.attr("role", accessible.role!);

	// if enabled state is not present, it is disabled
	el.attr("aria-disabled", String(accessible.states != null && accessible.states.indexOf('ENABLED') === -1));

	el.css({
		width: accessible.width!,
		height: accessible.height!,
		top: accessible.screenY + "px",
		left: accessible.screenX + "px"
	});

	handleElementDescription(accessible, el);

	if (el.is(".new")) {
		appendAriaElement(el, accessible.rootElement);
	}
}

function appendAriaElement(el: JQuery<HTMLElement>, rootElement: JQuery<HTMLElement>) {
	const lastParent = rootElement.find(".aria-parent:last");
	if (lastParent.length) {
		lastParent.append(el);
	} else {
		rootElement.append(el);
	}
}

function handleHierarchy(accessible: AccessibleMsg) {
	const hierarchy = accessible.hierarchy;
	const ariaId = accessible.rootElement.find(".aria-element.aria-id");

	if (!hierarchy || hierarchy.length === 0) {
		while (ariaId.parents(".aria-parent").length > 0) {
			ariaId.unwrap();
		}
		return;
	}

	const ariaParents = accessible.rootElement.find(".aria-parent");
	let i = 0;
	for (; i < hierarchy.length; i++) {
		const h = hierarchy[i];
		let child;

		if (ariaParents.length <= i) {
			child = ariaId;
			// there is no parent in previous hierarchy
			// next child that will be wrapped is the aria-element
		} else {
			const p = jQuery(ariaParents[i]);

			if (isHierarchyParentSame(p, h)) {
				// if parent is the same at this level, do not touch the element, otherwise it will be read again
				continue;
			}

			// if the parent changed, unwrap and wrap a new parent
			// if we just change the attributes, it will not be read
			child = p.children().first();
			child.unwrap();
		}

		child.wrap("<div class='aria-parent' id='aria-parent-" + h.id + "'></div>");
		const wrapper = accessible.rootElement.find("#aria-parent-" + h.id);
		wrapper.attr("role", h.role!).attr("aria-label", h.text!);
		if (h.size != null) {
			wrapper.attr("aria-setsize", h.size);
			wrapper.attr("aria-posinset", h.position!);
		}
	}

	if (ariaParents.length >= i) {
		for (; i < ariaParents.length; i++) {
			jQuery(ariaParents[i]).contents().unwrap();
		}
	}
}

function findAriaElementById(id: string | null | undefined, rootElement: JQuery<HTMLElement>) {
	if (id) {
		const ariaEl = rootElement.find("#aria-element-" + id);
		if (ariaEl.length) {
			return ariaEl;
		}
	}
	return null;
}

function getAriaDescribedByElement(rootElement: JQuery<HTMLElement>) {
	if (rootElement.find("#aria-describedby").length === 0) {
		rootElement.append($("<span id='aria-describedby' />"));
	}
	return rootElement.find("#aria-describedby");
}

function handleElementDescription(accessible: AccessibleMsg, el: JQuery<HTMLElement>) {
	const describedBy = getAriaDescribedByElement(accessible.rootElement);
	if (accessible.tooltip || accessible.description) {
		describedBy.text(accessible.description! || accessible.tooltip!);
		el.attr("aria-describedby", "aria-describedby");
	} else {
		el.removeAttr("aria-describedby");
		describedBy.text("");
	}
}

function isHierarchyParentSame(p: any, h: any) {
	if (p.attr("id") !== ("aria-parent-" + h.id) || p.attr("role") !== h.role || p.attr("aria-label") !== h.text) {
		return false;
	}

	if (h.size != null) {
		if (p.attr("aria-setsize") !== h.size || p.attr("aria-posinset") !== h.position) {
			return false;
		}
	}

	return true;
}


function getLiveLog(rootElement: JQuery<HTMLElement>) {
	if (rootElement.find("#aria-live-log").length === 0) {
		rootElement.append('<div id="aria-live-log" aria-live="assertive" role="log" data-id="access" tabindex="-1" />');
	}

	return rootElement.find("#aria-live-log");
}