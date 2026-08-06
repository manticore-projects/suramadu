import * as $protobuf from "protobufjs";
import Long = require("long");
/** Namespace appFrameProtoOut. */
export namespace appFrameProtoOut {

    /** Properties of an AppFrameMsgOutProto. */
    interface IAppFrameMsgOutProto {

        /** AppFrameMsgOutProto startApplication */
        startApplication?: (appFrameProtoOut.IStartApplicationMsgOutProto|null);

        /** AppFrameMsgOutProto linkAction */
        linkAction?: (appFrameProtoOut.ILinkActionMsgOutProto|null);

        /** AppFrameMsgOutProto moveAction */
        moveAction?: (appFrameProtoOut.IWindowMoveActionMsgOutProto|null);

        /** AppFrameMsgOutProto copyEvent */
        copyEvent?: (appFrameProtoOut.ICopyEventMsgOutProto|null);

        /** AppFrameMsgOutProto pasteRequest */
        pasteRequest?: (appFrameProtoOut.IPasteRequestMsgOutProto|null);

        /** AppFrameMsgOutProto fileDialogEvent */
        fileDialogEvent?: (appFrameProtoOut.IFileDialogEventMsgOutProto|null);

        /** AppFrameMsgOutProto windows */
        windows?: (appFrameProtoOut.IWindowMsgOutProto[]|null);

        /** AppFrameMsgOutProto closedWindow */
        closedWindow?: (appFrameProtoOut.IWindowMsgOutProto|null);

        /** AppFrameMsgOutProto event */
        event?: (appFrameProtoOut.SimpleEventMsgOutProto|null);

        /** AppFrameMsgOutProto jsRequest */
        jsRequest?: (appFrameProtoOut.IJsEvalRequestMsgOutProto|null);

        /** AppFrameMsgOutProto javaResponse */
        javaResponse?: (appFrameProtoOut.IJsResultMsgOutProto|null);

        /** AppFrameMsgOutProto pixelsRequest */
        pixelsRequest?: (appFrameProtoOut.IPixelsAreaRequestMsgOutProto|null);

        /** AppFrameMsgOutProto playback */
        playback?: (appFrameProtoOut.IPlaybackInfoMsgOutProto|null);

        /** AppFrameMsgOutProto instanceId */
        instanceId?: (string|null);

        /** AppFrameMsgOutProto startTimestamp */
        startTimestamp?: (string|null);

        /** AppFrameMsgOutProto sendTimestamp */
        sendTimestamp?: (string|null);

        /** AppFrameMsgOutProto focusEvent */
        focusEvent?: (appFrameProtoOut.IFocusEventMsgOutProto|null);

        /** AppFrameMsgOutProto componentTree */
        componentTree?: (appFrameProtoOut.IComponentTreeMsgOutProto[]|null);

        /** AppFrameMsgOutProto directDraw */
        directDraw?: (boolean|null);

        /** AppFrameMsgOutProto actionEvent */
        actionEvent?: (appFrameProtoOut.IActionEventMsgOutProto|null);

        /** AppFrameMsgOutProto compositingWM */
        compositingWM?: (boolean|null);

        /** AppFrameMsgOutProto audioEvent */
        audioEvent?: (appFrameProtoOut.IAudioEventMsgOutProto|null);

        /** AppFrameMsgOutProto accessible */
        accessible?: (appFrameProtoOut.IAccessibilityMsgOutProto|null);

        /** AppFrameMsgOutProto windowSwitchList */
        windowSwitchList?: (appFrameProtoOut.IWindowSwitchMsgOutProto[]|null);

        /** AppFrameMsgOutProto cursorChangeEvent */
        cursorChangeEvent?: (appFrameProtoOut.ICursorChangeEventMsgOutProto|null);
    }

    /** Represents an AppFrameMsgOutProto. */
    class AppFrameMsgOutProto implements IAppFrameMsgOutProto {

        /**
         * Constructs a new AppFrameMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IAppFrameMsgOutProto);

        /** AppFrameMsgOutProto startApplication. */
        public startApplication?: (appFrameProtoOut.IStartApplicationMsgOutProto|null);

        /** AppFrameMsgOutProto linkAction. */
        public linkAction?: (appFrameProtoOut.ILinkActionMsgOutProto|null);

        /** AppFrameMsgOutProto moveAction. */
        public moveAction?: (appFrameProtoOut.IWindowMoveActionMsgOutProto|null);

        /** AppFrameMsgOutProto copyEvent. */
        public copyEvent?: (appFrameProtoOut.ICopyEventMsgOutProto|null);

        /** AppFrameMsgOutProto pasteRequest. */
        public pasteRequest?: (appFrameProtoOut.IPasteRequestMsgOutProto|null);

        /** AppFrameMsgOutProto fileDialogEvent. */
        public fileDialogEvent?: (appFrameProtoOut.IFileDialogEventMsgOutProto|null);

        /** AppFrameMsgOutProto windows. */
        public windows: appFrameProtoOut.IWindowMsgOutProto[];

        /** AppFrameMsgOutProto closedWindow. */
        public closedWindow?: (appFrameProtoOut.IWindowMsgOutProto|null);

        /** AppFrameMsgOutProto event. */
        public event: appFrameProtoOut.SimpleEventMsgOutProto;

        /** AppFrameMsgOutProto jsRequest. */
        public jsRequest?: (appFrameProtoOut.IJsEvalRequestMsgOutProto|null);

        /** AppFrameMsgOutProto javaResponse. */
        public javaResponse?: (appFrameProtoOut.IJsResultMsgOutProto|null);

        /** AppFrameMsgOutProto pixelsRequest. */
        public pixelsRequest?: (appFrameProtoOut.IPixelsAreaRequestMsgOutProto|null);

        /** AppFrameMsgOutProto playback. */
        public playback?: (appFrameProtoOut.IPlaybackInfoMsgOutProto|null);

        /** AppFrameMsgOutProto instanceId. */
        public instanceId: string;

        /** AppFrameMsgOutProto startTimestamp. */
        public startTimestamp: string;

        /** AppFrameMsgOutProto sendTimestamp. */
        public sendTimestamp: string;

        /** AppFrameMsgOutProto focusEvent. */
        public focusEvent?: (appFrameProtoOut.IFocusEventMsgOutProto|null);

        /** AppFrameMsgOutProto componentTree. */
        public componentTree: appFrameProtoOut.IComponentTreeMsgOutProto[];

        /** AppFrameMsgOutProto directDraw. */
        public directDraw: boolean;

        /** AppFrameMsgOutProto actionEvent. */
        public actionEvent?: (appFrameProtoOut.IActionEventMsgOutProto|null);

        /** AppFrameMsgOutProto compositingWM. */
        public compositingWM: boolean;

        /** AppFrameMsgOutProto audioEvent. */
        public audioEvent?: (appFrameProtoOut.IAudioEventMsgOutProto|null);

        /** AppFrameMsgOutProto accessible. */
        public accessible?: (appFrameProtoOut.IAccessibilityMsgOutProto|null);

        /** AppFrameMsgOutProto windowSwitchList. */
        public windowSwitchList: appFrameProtoOut.IWindowSwitchMsgOutProto[];

        /** AppFrameMsgOutProto cursorChangeEvent. */
        public cursorChangeEvent?: (appFrameProtoOut.ICursorChangeEventMsgOutProto|null);

        /**
         * Creates a new AppFrameMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns AppFrameMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IAppFrameMsgOutProto): appFrameProtoOut.AppFrameMsgOutProto;

        /**
         * Decodes an AppFrameMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns AppFrameMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.AppFrameMsgOutProto;

        /**
         * Creates an AppFrameMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns AppFrameMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.AppFrameMsgOutProto;

        /**
         * Creates a plain object from an AppFrameMsgOutProto message. Also converts values to other types if specified.
         * @param message AppFrameMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.AppFrameMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this AppFrameMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for AppFrameMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an AccessibilityMsgOutProto. */
    interface IAccessibilityMsgOutProto {

        /** AccessibilityMsgOutProto id */
        id?: (string|null);

        /** AccessibilityMsgOutProto role */
        role?: (string|null);

        /** AccessibilityMsgOutProto text */
        text?: (string|null);

        /** AccessibilityMsgOutProto tooltip */
        tooltip?: (string|null);

        /** AccessibilityMsgOutProto value */
        value?: (string|null);

        /** AccessibilityMsgOutProto description */
        description?: (string|null);

        /** AccessibilityMsgOutProto columnheader */
        columnheader?: (string|null);

        /** AccessibilityMsgOutProto password */
        password?: (boolean|null);

        /** AccessibilityMsgOutProto toggle */
        toggle?: (boolean|null);

        /** AccessibilityMsgOutProto selstart */
        selstart?: (number|null);

        /** AccessibilityMsgOutProto selend */
        selend?: (number|null);

        /** AccessibilityMsgOutProto rowheight */
        rowheight?: (number|null);

        /** AccessibilityMsgOutProto rows */
        rows?: (number|null);

        /** AccessibilityMsgOutProto size */
        size?: (number|null);

        /** AccessibilityMsgOutProto position */
        position?: (number|null);

        /** AccessibilityMsgOutProto level */
        level?: (number|null);

        /** AccessibilityMsgOutProto colindex */
        colindex?: (number|null);

        /** AccessibilityMsgOutProto rowindex */
        rowindex?: (number|null);

        /** AccessibilityMsgOutProto colcount */
        colcount?: (number|null);

        /** AccessibilityMsgOutProto rowcount */
        rowcount?: (number|null);

        /** AccessibilityMsgOutProto states */
        states?: (string[]|null);

        /** AccessibilityMsgOutProto min */
        min?: (number|null);

        /** AccessibilityMsgOutProto max */
        max?: (number|null);

        /** AccessibilityMsgOutProto val */
        val?: (number|null);

        /** AccessibilityMsgOutProto screenX */
        screenX?: (number|null);

        /** AccessibilityMsgOutProto screenY */
        screenY?: (number|null);

        /** AccessibilityMsgOutProto width */
        width?: (number|null);

        /** AccessibilityMsgOutProto height */
        height?: (number|null);

        /** AccessibilityMsgOutProto hierarchy */
        hierarchy?: (appFrameProtoOut.IAccessibilityHierarchyMsgOutProto[]|null);
    }

    /** Represents an AccessibilityMsgOutProto. */
    class AccessibilityMsgOutProto implements IAccessibilityMsgOutProto {

        /**
         * Constructs a new AccessibilityMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IAccessibilityMsgOutProto);

        /** AccessibilityMsgOutProto id. */
        public id: string;

        /** AccessibilityMsgOutProto role. */
        public role: string;

        /** AccessibilityMsgOutProto text. */
        public text: string;

        /** AccessibilityMsgOutProto tooltip. */
        public tooltip: string;

        /** AccessibilityMsgOutProto value. */
        public value: string;

        /** AccessibilityMsgOutProto description. */
        public description: string;

        /** AccessibilityMsgOutProto columnheader. */
        public columnheader: string;

        /** AccessibilityMsgOutProto password. */
        public password: boolean;

        /** AccessibilityMsgOutProto toggle. */
        public toggle: boolean;

        /** AccessibilityMsgOutProto selstart. */
        public selstart: number;

        /** AccessibilityMsgOutProto selend. */
        public selend: number;

        /** AccessibilityMsgOutProto rowheight. */
        public rowheight: number;

        /** AccessibilityMsgOutProto rows. */
        public rows: number;

        /** AccessibilityMsgOutProto size. */
        public size: number;

        /** AccessibilityMsgOutProto position. */
        public position: number;

        /** AccessibilityMsgOutProto level. */
        public level: number;

        /** AccessibilityMsgOutProto colindex. */
        public colindex: number;

        /** AccessibilityMsgOutProto rowindex. */
        public rowindex: number;

        /** AccessibilityMsgOutProto colcount. */
        public colcount: number;

        /** AccessibilityMsgOutProto rowcount. */
        public rowcount: number;

        /** AccessibilityMsgOutProto states. */
        public states: string[];

        /** AccessibilityMsgOutProto min. */
        public min: number;

        /** AccessibilityMsgOutProto max. */
        public max: number;

        /** AccessibilityMsgOutProto val. */
        public val: number;

        /** AccessibilityMsgOutProto screenX. */
        public screenX: number;

        /** AccessibilityMsgOutProto screenY. */
        public screenY: number;

        /** AccessibilityMsgOutProto width. */
        public width: number;

        /** AccessibilityMsgOutProto height. */
        public height: number;

        /** AccessibilityMsgOutProto hierarchy. */
        public hierarchy: appFrameProtoOut.IAccessibilityHierarchyMsgOutProto[];

        /**
         * Creates a new AccessibilityMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns AccessibilityMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IAccessibilityMsgOutProto): appFrameProtoOut.AccessibilityMsgOutProto;

        /**
         * Decodes an AccessibilityMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns AccessibilityMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.AccessibilityMsgOutProto;

        /**
         * Creates an AccessibilityMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns AccessibilityMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.AccessibilityMsgOutProto;

        /**
         * Creates a plain object from an AccessibilityMsgOutProto message. Also converts values to other types if specified.
         * @param message AccessibilityMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.AccessibilityMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this AccessibilityMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for AccessibilityMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an AccessibilityHierarchyMsgOutProto. */
    interface IAccessibilityHierarchyMsgOutProto {

        /** AccessibilityHierarchyMsgOutProto id */
        id?: (string|null);

        /** AccessibilityHierarchyMsgOutProto role */
        role?: (string|null);

        /** AccessibilityHierarchyMsgOutProto text */
        text?: (string|null);

        /** AccessibilityHierarchyMsgOutProto position */
        position?: (number|null);

        /** AccessibilityHierarchyMsgOutProto size */
        size?: (number|null);
    }

    /** Represents an AccessibilityHierarchyMsgOutProto. */
    class AccessibilityHierarchyMsgOutProto implements IAccessibilityHierarchyMsgOutProto {

        /**
         * Constructs a new AccessibilityHierarchyMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IAccessibilityHierarchyMsgOutProto);

        /** AccessibilityHierarchyMsgOutProto id. */
        public id: string;

        /** AccessibilityHierarchyMsgOutProto role. */
        public role: string;

        /** AccessibilityHierarchyMsgOutProto text. */
        public text: string;

        /** AccessibilityHierarchyMsgOutProto position. */
        public position: number;

        /** AccessibilityHierarchyMsgOutProto size. */
        public size: number;

        /**
         * Creates a new AccessibilityHierarchyMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns AccessibilityHierarchyMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IAccessibilityHierarchyMsgOutProto): appFrameProtoOut.AccessibilityHierarchyMsgOutProto;

        /**
         * Decodes an AccessibilityHierarchyMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns AccessibilityHierarchyMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.AccessibilityHierarchyMsgOutProto;

        /**
         * Creates an AccessibilityHierarchyMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns AccessibilityHierarchyMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.AccessibilityHierarchyMsgOutProto;

        /**
         * Creates a plain object from an AccessibilityHierarchyMsgOutProto message. Also converts values to other types if specified.
         * @param message AccessibilityHierarchyMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.AccessibilityHierarchyMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this AccessibilityHierarchyMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for AccessibilityHierarchyMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a FocusEventMsgOutProto. */
    interface IFocusEventMsgOutProto {

        /** FocusEventMsgOutProto type */
        type: appFrameProtoOut.FocusEventMsgOutProto.FocusEventTypeProto;

        /** FocusEventMsgOutProto x */
        x?: (number|null);

        /** FocusEventMsgOutProto y */
        y?: (number|null);

        /** FocusEventMsgOutProto w */
        w?: (number|null);

        /** FocusEventMsgOutProto h */
        h?: (number|null);

        /** FocusEventMsgOutProto caretX */
        caretX?: (number|null);

        /** FocusEventMsgOutProto caretY */
        caretY?: (number|null);

        /** FocusEventMsgOutProto caretH */
        caretH?: (number|null);

        /** FocusEventMsgOutProto editable */
        editable?: (boolean|null);
    }

    /** Represents a FocusEventMsgOutProto. */
    class FocusEventMsgOutProto implements IFocusEventMsgOutProto {

        /**
         * Constructs a new FocusEventMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IFocusEventMsgOutProto);

        /** FocusEventMsgOutProto type. */
        public type: appFrameProtoOut.FocusEventMsgOutProto.FocusEventTypeProto;

        /** FocusEventMsgOutProto x. */
        public x: number;

        /** FocusEventMsgOutProto y. */
        public y: number;

        /** FocusEventMsgOutProto w. */
        public w: number;

        /** FocusEventMsgOutProto h. */
        public h: number;

        /** FocusEventMsgOutProto caretX. */
        public caretX: number;

        /** FocusEventMsgOutProto caretY. */
        public caretY: number;

        /** FocusEventMsgOutProto caretH. */
        public caretH: number;

        /** FocusEventMsgOutProto editable. */
        public editable: boolean;

        /**
         * Creates a new FocusEventMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns FocusEventMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IFocusEventMsgOutProto): appFrameProtoOut.FocusEventMsgOutProto;

        /**
         * Decodes a FocusEventMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns FocusEventMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.FocusEventMsgOutProto;

        /**
         * Creates a FocusEventMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns FocusEventMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.FocusEventMsgOutProto;

        /**
         * Creates a plain object from a FocusEventMsgOutProto message. Also converts values to other types if specified.
         * @param message FocusEventMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.FocusEventMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this FocusEventMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for FocusEventMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace FocusEventMsgOutProto {

        /** FocusEventTypeProto enum. */
        enum FocusEventTypeProto {
            focusLost = 1,
            focusGained = 2,
            focusWithCarretGained = 3,
            focusPasswordGained = 4
        }
    }

    /** Properties of a StartApplicationMsgOutProto. */
    interface IStartApplicationMsgOutProto {
    }

    /** Represents a StartApplicationMsgOutProto. */
    class StartApplicationMsgOutProto implements IStartApplicationMsgOutProto {

        /**
         * Constructs a new StartApplicationMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IStartApplicationMsgOutProto);

        /**
         * Creates a new StartApplicationMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns StartApplicationMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IStartApplicationMsgOutProto): appFrameProtoOut.StartApplicationMsgOutProto;

        /**
         * Decodes a StartApplicationMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns StartApplicationMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.StartApplicationMsgOutProto;

        /**
         * Creates a StartApplicationMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns StartApplicationMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.StartApplicationMsgOutProto;

        /**
         * Creates a plain object from a StartApplicationMsgOutProto message. Also converts values to other types if specified.
         * @param message StartApplicationMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.StartApplicationMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this StartApplicationMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for StartApplicationMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a LinkActionMsgOutProto. */
    interface ILinkActionMsgOutProto {

        /** LinkActionMsgOutProto action */
        action: appFrameProtoOut.LinkActionMsgOutProto.LinkActionTypeProto;

        /** LinkActionMsgOutProto src */
        src: string;
    }

    /** Represents a LinkActionMsgOutProto. */
    class LinkActionMsgOutProto implements ILinkActionMsgOutProto {

        /**
         * Constructs a new LinkActionMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.ILinkActionMsgOutProto);

        /** LinkActionMsgOutProto action. */
        public action: appFrameProtoOut.LinkActionMsgOutProto.LinkActionTypeProto;

        /** LinkActionMsgOutProto src. */
        public src: string;

        /**
         * Creates a new LinkActionMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns LinkActionMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.ILinkActionMsgOutProto): appFrameProtoOut.LinkActionMsgOutProto;

        /**
         * Decodes a LinkActionMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns LinkActionMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.LinkActionMsgOutProto;

        /**
         * Creates a LinkActionMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns LinkActionMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.LinkActionMsgOutProto;

        /**
         * Creates a plain object from a LinkActionMsgOutProto message. Also converts values to other types if specified.
         * @param message LinkActionMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.LinkActionMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this LinkActionMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for LinkActionMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace LinkActionMsgOutProto {

        /** LinkActionTypeProto enum. */
        enum LinkActionTypeProto {
            file = 0,
            url = 1,
            print = 2,
            redirect = 3
        }
    }

    /** Properties of a WindowMoveActionMsgOutProto. */
    interface IWindowMoveActionMsgOutProto {

        /** WindowMoveActionMsgOutProto sx */
        sx?: (number|null);

        /** WindowMoveActionMsgOutProto sy */
        sy?: (number|null);

        /** WindowMoveActionMsgOutProto dx */
        dx?: (number|null);

        /** WindowMoveActionMsgOutProto dy */
        dy?: (number|null);

        /** WindowMoveActionMsgOutProto width */
        width?: (number|null);

        /** WindowMoveActionMsgOutProto height */
        height?: (number|null);
    }

    /** Represents a WindowMoveActionMsgOutProto. */
    class WindowMoveActionMsgOutProto implements IWindowMoveActionMsgOutProto {

        /**
         * Constructs a new WindowMoveActionMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IWindowMoveActionMsgOutProto);

        /** WindowMoveActionMsgOutProto sx. */
        public sx: number;

        /** WindowMoveActionMsgOutProto sy. */
        public sy: number;

        /** WindowMoveActionMsgOutProto dx. */
        public dx: number;

        /** WindowMoveActionMsgOutProto dy. */
        public dy: number;

        /** WindowMoveActionMsgOutProto width. */
        public width: number;

        /** WindowMoveActionMsgOutProto height. */
        public height: number;

        /**
         * Creates a new WindowMoveActionMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns WindowMoveActionMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IWindowMoveActionMsgOutProto): appFrameProtoOut.WindowMoveActionMsgOutProto;

        /**
         * Decodes a WindowMoveActionMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns WindowMoveActionMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.WindowMoveActionMsgOutProto;

        /**
         * Creates a WindowMoveActionMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns WindowMoveActionMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.WindowMoveActionMsgOutProto;

        /**
         * Creates a plain object from a WindowMoveActionMsgOutProto message. Also converts values to other types if specified.
         * @param message WindowMoveActionMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.WindowMoveActionMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this WindowMoveActionMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for WindowMoveActionMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a CopyEventMsgOutProto. */
    interface ICopyEventMsgOutProto {

        /** CopyEventMsgOutProto text */
        text?: (string|null);

        /** CopyEventMsgOutProto html */
        html?: (string|null);

        /** CopyEventMsgOutProto img */
        img?: (Uint8Array|null);

        /** CopyEventMsgOutProto files */
        files?: (string[]|null);

        /** CopyEventMsgOutProto other */
        other?: (boolean|null);
    }

    /** Represents a CopyEventMsgOutProto. */
    class CopyEventMsgOutProto implements ICopyEventMsgOutProto {

        /**
         * Constructs a new CopyEventMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.ICopyEventMsgOutProto);

        /** CopyEventMsgOutProto text. */
        public text: string;

        /** CopyEventMsgOutProto html. */
        public html: string;

        /** CopyEventMsgOutProto img. */
        public img: Uint8Array;

        /** CopyEventMsgOutProto files. */
        public files: string[];

        /** CopyEventMsgOutProto other. */
        public other: boolean;

        /**
         * Creates a new CopyEventMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns CopyEventMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.ICopyEventMsgOutProto): appFrameProtoOut.CopyEventMsgOutProto;

        /**
         * Decodes a CopyEventMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns CopyEventMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.CopyEventMsgOutProto;

        /**
         * Creates a CopyEventMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns CopyEventMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.CopyEventMsgOutProto;

        /**
         * Creates a plain object from a CopyEventMsgOutProto message. Also converts values to other types if specified.
         * @param message CopyEventMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.CopyEventMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this CopyEventMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for CopyEventMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a PasteRequestMsgOutProto. */
    interface IPasteRequestMsgOutProto {

        /** PasteRequestMsgOutProto title */
        title?: (string|null);

        /** PasteRequestMsgOutProto message */
        message?: (string|null);
    }

    /** Represents a PasteRequestMsgOutProto. */
    class PasteRequestMsgOutProto implements IPasteRequestMsgOutProto {

        /**
         * Constructs a new PasteRequestMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IPasteRequestMsgOutProto);

        /** PasteRequestMsgOutProto title. */
        public title: string;

        /** PasteRequestMsgOutProto message. */
        public message: string;

        /**
         * Creates a new PasteRequestMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns PasteRequestMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IPasteRequestMsgOutProto): appFrameProtoOut.PasteRequestMsgOutProto;

        /**
         * Decodes a PasteRequestMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns PasteRequestMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.PasteRequestMsgOutProto;

        /**
         * Creates a PasteRequestMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns PasteRequestMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.PasteRequestMsgOutProto;

        /**
         * Creates a plain object from a PasteRequestMsgOutProto message. Also converts values to other types if specified.
         * @param message PasteRequestMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.PasteRequestMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this PasteRequestMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for PasteRequestMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a FileDialogEventMsgOutProto. */
    interface IFileDialogEventMsgOutProto {

        /** FileDialogEventMsgOutProto eventType */
        eventType: appFrameProtoOut.FileDialogEventMsgOutProto.FileDialogEventTypeProto;

        /** FileDialogEventMsgOutProto allowDownload */
        allowDownload?: (boolean|null);

        /** FileDialogEventMsgOutProto allowUpload */
        allowUpload?: (boolean|null);

        /** FileDialogEventMsgOutProto allowDelete */
        allowDelete?: (boolean|null);

        /** FileDialogEventMsgOutProto filter */
        filter?: (string|null);

        /** FileDialogEventMsgOutProto isMultiSelection */
        isMultiSelection?: (boolean|null);

        /** FileDialogEventMsgOutProto selection */
        selection?: (string|null);

        /** FileDialogEventMsgOutProto customDialog */
        customDialog?: (boolean|null);
    }

    /** Represents a FileDialogEventMsgOutProto. */
    class FileDialogEventMsgOutProto implements IFileDialogEventMsgOutProto {

        /**
         * Constructs a new FileDialogEventMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IFileDialogEventMsgOutProto);

        /** FileDialogEventMsgOutProto eventType. */
        public eventType: appFrameProtoOut.FileDialogEventMsgOutProto.FileDialogEventTypeProto;

        /** FileDialogEventMsgOutProto allowDownload. */
        public allowDownload: boolean;

        /** FileDialogEventMsgOutProto allowUpload. */
        public allowUpload: boolean;

        /** FileDialogEventMsgOutProto allowDelete. */
        public allowDelete: boolean;

        /** FileDialogEventMsgOutProto filter. */
        public filter: string;

        /** FileDialogEventMsgOutProto isMultiSelection. */
        public isMultiSelection: boolean;

        /** FileDialogEventMsgOutProto selection. */
        public selection: string;

        /** FileDialogEventMsgOutProto customDialog. */
        public customDialog: boolean;

        /**
         * Creates a new FileDialogEventMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns FileDialogEventMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IFileDialogEventMsgOutProto): appFrameProtoOut.FileDialogEventMsgOutProto;

        /**
         * Decodes a FileDialogEventMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns FileDialogEventMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.FileDialogEventMsgOutProto;

        /**
         * Creates a FileDialogEventMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns FileDialogEventMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.FileDialogEventMsgOutProto;

        /**
         * Creates a plain object from a FileDialogEventMsgOutProto message. Also converts values to other types if specified.
         * @param message FileDialogEventMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.FileDialogEventMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this FileDialogEventMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for FileDialogEventMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace FileDialogEventMsgOutProto {

        /** FileDialogEventTypeProto enum. */
        enum FileDialogEventTypeProto {
            Open = 0,
            Close = 1,
            AutoUpload = 2,
            AutoSave = 3
        }
    }

    /** Properties of a WindowSwitchMsg. */
    interface IWindowSwitchMsg {

        /** WindowSwitchMsg id */
        id: string;

        /** WindowSwitchMsg title */
        title?: (string|null);

        /** WindowSwitchMsg modalBlocked */
        modalBlocked?: (boolean|null);
    }

    /** Represents a WindowSwitchMsg. */
    class WindowSwitchMsg implements IWindowSwitchMsg {

        /**
         * Constructs a new WindowSwitchMsg.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IWindowSwitchMsg);

        /** WindowSwitchMsg id. */
        public id: string;

        /** WindowSwitchMsg title. */
        public title: string;

        /** WindowSwitchMsg modalBlocked. */
        public modalBlocked: boolean;

        /**
         * Creates a new WindowSwitchMsg instance using the specified properties.
         * @param [properties] Properties to set
         * @returns WindowSwitchMsg instance
         */
        public static create(properties?: appFrameProtoOut.IWindowSwitchMsg): appFrameProtoOut.WindowSwitchMsg;

        /**
         * Decodes a WindowSwitchMsg message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns WindowSwitchMsg
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.WindowSwitchMsg;

        /**
         * Creates a WindowSwitchMsg message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns WindowSwitchMsg
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.WindowSwitchMsg;

        /**
         * Creates a plain object from a WindowSwitchMsg message. Also converts values to other types if specified.
         * @param message WindowSwitchMsg
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.WindowSwitchMsg, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this WindowSwitchMsg to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for WindowSwitchMsg
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a WindowMsgOutProto. */
    interface IWindowMsgOutProto {

        /** WindowMsgOutProto id */
        id: string;

        /** WindowMsgOutProto content */
        content?: (appFrameProtoOut.IWindowPartialContentMsgOutProto[]|null);

        /** WindowMsgOutProto directDraw */
        directDraw?: (Uint8Array|null);

        /** WindowMsgOutProto title */
        title?: (string|null);

        /** WindowMsgOutProto posX */
        posX?: (number|null);

        /** WindowMsgOutProto posY */
        posY?: (number|null);

        /** WindowMsgOutProto width */
        width?: (number|null);

        /** WindowMsgOutProto height */
        height?: (number|null);

        /** WindowMsgOutProto name */
        name?: (string|null);

        /** WindowMsgOutProto type */
        type?: (appFrameProtoOut.WindowMsgOutProto.WindowTypeProto|null);

        /** WindowMsgOutProto modalBlocked */
        modalBlocked?: (boolean|null);

        /** WindowMsgOutProto ownerId */
        ownerId?: (string|null);

        /** WindowMsgOutProto state */
        state?: (number|null);

        /** WindowMsgOutProto internalWindows */
        internalWindows?: (appFrameProtoOut.IWindowMsgOutProto[]|null);

        /** WindowMsgOutProto dockMode */
        dockMode?: (appFrameProtoOut.WindowMsgOutProto.DockModeProto|null);

        /** WindowMsgOutProto dockState */
        dockState?: (appFrameProtoOut.WindowMsgOutProto.DockStateProto|null);

        /** WindowMsgOutProto classType */
        classType?: (appFrameProtoOut.WindowMsgOutProto.WindowClassTypeProto|null);
    }

    /** Represents a WindowMsgOutProto. */
    class WindowMsgOutProto implements IWindowMsgOutProto {

        /**
         * Constructs a new WindowMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IWindowMsgOutProto);

        /** WindowMsgOutProto id. */
        public id: string;

        /** WindowMsgOutProto content. */
        public content: appFrameProtoOut.IWindowPartialContentMsgOutProto[];

        /** WindowMsgOutProto directDraw. */
        public directDraw: Uint8Array;

        /** WindowMsgOutProto title. */
        public title: string;

        /** WindowMsgOutProto posX. */
        public posX: number;

        /** WindowMsgOutProto posY. */
        public posY: number;

        /** WindowMsgOutProto width. */
        public width: number;

        /** WindowMsgOutProto height. */
        public height: number;

        /** WindowMsgOutProto name. */
        public name: string;

        /** WindowMsgOutProto type. */
        public type: appFrameProtoOut.WindowMsgOutProto.WindowTypeProto;

        /** WindowMsgOutProto modalBlocked. */
        public modalBlocked: boolean;

        /** WindowMsgOutProto ownerId. */
        public ownerId: string;

        /** WindowMsgOutProto state. */
        public state: number;

        /** WindowMsgOutProto internalWindows. */
        public internalWindows: appFrameProtoOut.IWindowMsgOutProto[];

        /** WindowMsgOutProto dockMode. */
        public dockMode: appFrameProtoOut.WindowMsgOutProto.DockModeProto;

        /** WindowMsgOutProto dockState. */
        public dockState: appFrameProtoOut.WindowMsgOutProto.DockStateProto;

        /** WindowMsgOutProto classType. */
        public classType: appFrameProtoOut.WindowMsgOutProto.WindowClassTypeProto;

        /**
         * Creates a new WindowMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns WindowMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IWindowMsgOutProto): appFrameProtoOut.WindowMsgOutProto;

        /**
         * Decodes a WindowMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns WindowMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.WindowMsgOutProto;

        /**
         * Creates a WindowMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns WindowMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.WindowMsgOutProto;

        /**
         * Creates a plain object from a WindowMsgOutProto message. Also converts values to other types if specified.
         * @param message WindowMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.WindowMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this WindowMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for WindowMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace WindowMsgOutProto {

        /** WindowTypeProto enum. */
        enum WindowTypeProto {
            basic = 1,
            html = 2,
            internal = 3,
            internalHtml = 4,
            internalWrapper = 5
        }

        /** WindowClassTypeProto enum. */
        enum WindowClassTypeProto {
            other = 1,
            Window = 2,
            JWindow = 3,
            Dialog = 4,
            JDialog = 5,
            Frame = 6,
            JFrame = 7
        }

        /** DockModeProto enum. */
        enum DockModeProto {
            none = 1,
            dockable = 2,
            autoUndock = 3
        }

        /** DockStateProto enum. */
        enum DockStateProto {
            docked = 1,
            undocked = 2
        }
    }

    /** Properties of a WindowSwitchMsgOutProto. */
    interface IWindowSwitchMsgOutProto {

        /** WindowSwitchMsgOutProto id */
        id: string;

        /** WindowSwitchMsgOutProto title */
        title?: (string|null);

        /** WindowSwitchMsgOutProto modalBlocked */
        modalBlocked?: (boolean|null);
    }

    /** Represents a WindowSwitchMsgOutProto. */
    class WindowSwitchMsgOutProto implements IWindowSwitchMsgOutProto {

        /**
         * Constructs a new WindowSwitchMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IWindowSwitchMsgOutProto);

        /** WindowSwitchMsgOutProto id. */
        public id: string;

        /** WindowSwitchMsgOutProto title. */
        public title: string;

        /** WindowSwitchMsgOutProto modalBlocked. */
        public modalBlocked: boolean;

        /**
         * Creates a new WindowSwitchMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns WindowSwitchMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IWindowSwitchMsgOutProto): appFrameProtoOut.WindowSwitchMsgOutProto;

        /**
         * Decodes a WindowSwitchMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns WindowSwitchMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.WindowSwitchMsgOutProto;

        /**
         * Creates a WindowSwitchMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns WindowSwitchMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.WindowSwitchMsgOutProto;

        /**
         * Creates a plain object from a WindowSwitchMsgOutProto message. Also converts values to other types if specified.
         * @param message WindowSwitchMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.WindowSwitchMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this WindowSwitchMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for WindowSwitchMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a WindowPartialContentMsgOutProto. */
    interface IWindowPartialContentMsgOutProto {

        /** WindowPartialContentMsgOutProto positionX */
        positionX?: (number|null);

        /** WindowPartialContentMsgOutProto positionY */
        positionY?: (number|null);

        /** WindowPartialContentMsgOutProto width */
        width?: (number|null);

        /** WindowPartialContentMsgOutProto height */
        height?: (number|null);

        /** WindowPartialContentMsgOutProto base64Content */
        base64Content?: (Uint8Array|null);
    }

    /** Represents a WindowPartialContentMsgOutProto. */
    class WindowPartialContentMsgOutProto implements IWindowPartialContentMsgOutProto {

        /**
         * Constructs a new WindowPartialContentMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IWindowPartialContentMsgOutProto);

        /** WindowPartialContentMsgOutProto positionX. */
        public positionX: number;

        /** WindowPartialContentMsgOutProto positionY. */
        public positionY: number;

        /** WindowPartialContentMsgOutProto width. */
        public width: number;

        /** WindowPartialContentMsgOutProto height. */
        public height: number;

        /** WindowPartialContentMsgOutProto base64Content. */
        public base64Content: Uint8Array;

        /**
         * Creates a new WindowPartialContentMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns WindowPartialContentMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IWindowPartialContentMsgOutProto): appFrameProtoOut.WindowPartialContentMsgOutProto;

        /**
         * Decodes a WindowPartialContentMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns WindowPartialContentMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.WindowPartialContentMsgOutProto;

        /**
         * Creates a WindowPartialContentMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns WindowPartialContentMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.WindowPartialContentMsgOutProto;

        /**
         * Creates a plain object from a WindowPartialContentMsgOutProto message. Also converts values to other types if specified.
         * @param message WindowPartialContentMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.WindowPartialContentMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this WindowPartialContentMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for WindowPartialContentMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** SimpleEventMsgOutProto enum. */
    enum SimpleEventMsgOutProto {
        applicationAlreadyRunning = 0,
        shutDownNotification = 1,
        tooManyClientsNotification = 2,
        continueOldSession = 3,
        configurationError = 4,
        sessionStolenNotification = 5,
        unauthorizedAccess = 6,
        shutDownAutoLogoutNotification = 7,
        sessionTimeoutWarning = 8,
        sessionTimedOutNotification = 9,
        applicationBusy = 10,
        reconnectInstanceNotFound = 11
    }

    /** Properties of a JsEvalRequestMsgOutProto. */
    interface IJsEvalRequestMsgOutProto {

        /** JsEvalRequestMsgOutProto correlationId */
        correlationId?: (string|null);

        /** JsEvalRequestMsgOutProto thisObjectId */
        thisObjectId?: (string|null);

        /** JsEvalRequestMsgOutProto type */
        type?: (appFrameProtoOut.JsEvalRequestMsgOutProto.JsEvalRequestTypeProto|null);

        /** JsEvalRequestMsgOutProto evalString */
        evalString?: (string|null);

        /** JsEvalRequestMsgOutProto params */
        params?: (appFrameProtoOut.IJsParamMsgOutProto[]|null);

        /** JsEvalRequestMsgOutProto garbageIds */
        garbageIds?: (string[]|null);
    }

    /** Represents a JsEvalRequestMsgOutProto. */
    class JsEvalRequestMsgOutProto implements IJsEvalRequestMsgOutProto {

        /**
         * Constructs a new JsEvalRequestMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IJsEvalRequestMsgOutProto);

        /** JsEvalRequestMsgOutProto correlationId. */
        public correlationId: string;

        /** JsEvalRequestMsgOutProto thisObjectId. */
        public thisObjectId: string;

        /** JsEvalRequestMsgOutProto type. */
        public type: appFrameProtoOut.JsEvalRequestMsgOutProto.JsEvalRequestTypeProto;

        /** JsEvalRequestMsgOutProto evalString. */
        public evalString: string;

        /** JsEvalRequestMsgOutProto params. */
        public params: appFrameProtoOut.IJsParamMsgOutProto[];

        /** JsEvalRequestMsgOutProto garbageIds. */
        public garbageIds: string[];

        /**
         * Creates a new JsEvalRequestMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns JsEvalRequestMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IJsEvalRequestMsgOutProto): appFrameProtoOut.JsEvalRequestMsgOutProto;

        /**
         * Decodes a JsEvalRequestMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns JsEvalRequestMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.JsEvalRequestMsgOutProto;

        /**
         * Creates a JsEvalRequestMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns JsEvalRequestMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.JsEvalRequestMsgOutProto;

        /**
         * Creates a plain object from a JsEvalRequestMsgOutProto message. Also converts values to other types if specified.
         * @param message JsEvalRequestMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.JsEvalRequestMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this JsEvalRequestMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for JsEvalRequestMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace JsEvalRequestMsgOutProto {

        /** JsEvalRequestTypeProto enum. */
        enum JsEvalRequestTypeProto {
            eval = 0,
            call = 1,
            setMember = 2,
            getMember = 3,
            deleteMember = 4,
            setSlot = 5,
            getSlot = 6
        }
    }

    /** Properties of a JsParamMsgOutProto. */
    interface IJsParamMsgOutProto {

        /** JsParamMsgOutProto primitive */
        primitive?: (string|null);

        /** JsParamMsgOutProto jsObject */
        jsObject?: (appFrameProtoOut.IJSObjectMsgOutProto|null);

        /** JsParamMsgOutProto javaObject */
        javaObject?: (appFrameProtoOut.IJavaObjectRefMsgOutProto|null);

        /** JsParamMsgOutProto array */
        array?: (appFrameProtoOut.IJsParamMsgOutProto[]|null);
    }

    /** Represents a JsParamMsgOutProto. */
    class JsParamMsgOutProto implements IJsParamMsgOutProto {

        /**
         * Constructs a new JsParamMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IJsParamMsgOutProto);

        /** JsParamMsgOutProto primitive. */
        public primitive: string;

        /** JsParamMsgOutProto jsObject. */
        public jsObject?: (appFrameProtoOut.IJSObjectMsgOutProto|null);

        /** JsParamMsgOutProto javaObject. */
        public javaObject?: (appFrameProtoOut.IJavaObjectRefMsgOutProto|null);

        /** JsParamMsgOutProto array. */
        public array: appFrameProtoOut.IJsParamMsgOutProto[];

        /**
         * Creates a new JsParamMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns JsParamMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IJsParamMsgOutProto): appFrameProtoOut.JsParamMsgOutProto;

        /**
         * Decodes a JsParamMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns JsParamMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.JsParamMsgOutProto;

        /**
         * Creates a JsParamMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns JsParamMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.JsParamMsgOutProto;

        /**
         * Creates a plain object from a JsParamMsgOutProto message. Also converts values to other types if specified.
         * @param message JsParamMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.JsParamMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this JsParamMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for JsParamMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a JSObjectMsgOutProto. */
    interface IJSObjectMsgOutProto {

        /** JSObjectMsgOutProto id */
        id?: (string|null);
    }

    /** Represents a JSObjectMsgOutProto. */
    class JSObjectMsgOutProto implements IJSObjectMsgOutProto {

        /**
         * Constructs a new JSObjectMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IJSObjectMsgOutProto);

        /** JSObjectMsgOutProto id. */
        public id: string;

        /**
         * Creates a new JSObjectMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns JSObjectMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IJSObjectMsgOutProto): appFrameProtoOut.JSObjectMsgOutProto;

        /**
         * Decodes a JSObjectMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns JSObjectMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.JSObjectMsgOutProto;

        /**
         * Creates a JSObjectMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns JSObjectMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.JSObjectMsgOutProto;

        /**
         * Creates a plain object from a JSObjectMsgOutProto message. Also converts values to other types if specified.
         * @param message JSObjectMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.JSObjectMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this JSObjectMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for JSObjectMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a JavaObjectRefMsgOutProto. */
    interface IJavaObjectRefMsgOutProto {

        /** JavaObjectRefMsgOutProto id */
        id?: (string|null);

        /** JavaObjectRefMsgOutProto methods */
        methods?: (string[]|null);
    }

    /** Represents a JavaObjectRefMsgOutProto. */
    class JavaObjectRefMsgOutProto implements IJavaObjectRefMsgOutProto {

        /**
         * Constructs a new JavaObjectRefMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IJavaObjectRefMsgOutProto);

        /** JavaObjectRefMsgOutProto id. */
        public id: string;

        /** JavaObjectRefMsgOutProto methods. */
        public methods: string[];

        /**
         * Creates a new JavaObjectRefMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns JavaObjectRefMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IJavaObjectRefMsgOutProto): appFrameProtoOut.JavaObjectRefMsgOutProto;

        /**
         * Decodes a JavaObjectRefMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns JavaObjectRefMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.JavaObjectRefMsgOutProto;

        /**
         * Creates a JavaObjectRefMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns JavaObjectRefMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.JavaObjectRefMsgOutProto;

        /**
         * Creates a plain object from a JavaObjectRefMsgOutProto message. Also converts values to other types if specified.
         * @param message JavaObjectRefMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.JavaObjectRefMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this JavaObjectRefMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for JavaObjectRefMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a JsResultMsgOutProto. */
    interface IJsResultMsgOutProto {

        /** JsResultMsgOutProto correlationId */
        correlationId?: (string|null);

        /** JsResultMsgOutProto error */
        error?: (string|null);

        /** JsResultMsgOutProto value */
        value?: (appFrameProtoOut.IJsParamMsgOutProto|null);
    }

    /** Represents a JsResultMsgOutProto. */
    class JsResultMsgOutProto implements IJsResultMsgOutProto {

        /**
         * Constructs a new JsResultMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IJsResultMsgOutProto);

        /** JsResultMsgOutProto correlationId. */
        public correlationId: string;

        /** JsResultMsgOutProto error. */
        public error: string;

        /** JsResultMsgOutProto value. */
        public value?: (appFrameProtoOut.IJsParamMsgOutProto|null);

        /**
         * Creates a new JsResultMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns JsResultMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IJsResultMsgOutProto): appFrameProtoOut.JsResultMsgOutProto;

        /**
         * Decodes a JsResultMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns JsResultMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.JsResultMsgOutProto;

        /**
         * Creates a JsResultMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns JsResultMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.JsResultMsgOutProto;

        /**
         * Creates a plain object from a JsResultMsgOutProto message. Also converts values to other types if specified.
         * @param message JsResultMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.JsResultMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this JsResultMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for JsResultMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a PlaybackInfoMsgOutProto. */
    interface IPlaybackInfoMsgOutProto {

        /** PlaybackInfoMsgOutProto current */
        current?: (number|null);

        /** PlaybackInfoMsgOutProto total */
        total?: (number|null);
    }

    /** Represents a PlaybackInfoMsgOutProto. */
    class PlaybackInfoMsgOutProto implements IPlaybackInfoMsgOutProto {

        /**
         * Constructs a new PlaybackInfoMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IPlaybackInfoMsgOutProto);

        /** PlaybackInfoMsgOutProto current. */
        public current: number;

        /** PlaybackInfoMsgOutProto total. */
        public total: number;

        /**
         * Creates a new PlaybackInfoMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns PlaybackInfoMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IPlaybackInfoMsgOutProto): appFrameProtoOut.PlaybackInfoMsgOutProto;

        /**
         * Decodes a PlaybackInfoMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns PlaybackInfoMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.PlaybackInfoMsgOutProto;

        /**
         * Creates a PlaybackInfoMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns PlaybackInfoMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.PlaybackInfoMsgOutProto;

        /**
         * Creates a plain object from a PlaybackInfoMsgOutProto message. Also converts values to other types if specified.
         * @param message PlaybackInfoMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.PlaybackInfoMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this PlaybackInfoMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for PlaybackInfoMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a PixelsAreaRequestMsgOutProto. */
    interface IPixelsAreaRequestMsgOutProto {

        /** PixelsAreaRequestMsgOutProto correlationId */
        correlationId?: (string|null);

        /** PixelsAreaRequestMsgOutProto x */
        x?: (number|null);

        /** PixelsAreaRequestMsgOutProto y */
        y?: (number|null);

        /** PixelsAreaRequestMsgOutProto w */
        w?: (number|null);

        /** PixelsAreaRequestMsgOutProto h */
        h?: (number|null);
    }

    /** Represents a PixelsAreaRequestMsgOutProto. */
    class PixelsAreaRequestMsgOutProto implements IPixelsAreaRequestMsgOutProto {

        /**
         * Constructs a new PixelsAreaRequestMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IPixelsAreaRequestMsgOutProto);

        /** PixelsAreaRequestMsgOutProto correlationId. */
        public correlationId: string;

        /** PixelsAreaRequestMsgOutProto x. */
        public x: number;

        /** PixelsAreaRequestMsgOutProto y. */
        public y: number;

        /** PixelsAreaRequestMsgOutProto w. */
        public w: number;

        /** PixelsAreaRequestMsgOutProto h. */
        public h: number;

        /**
         * Creates a new PixelsAreaRequestMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns PixelsAreaRequestMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IPixelsAreaRequestMsgOutProto): appFrameProtoOut.PixelsAreaRequestMsgOutProto;

        /**
         * Decodes a PixelsAreaRequestMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns PixelsAreaRequestMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.PixelsAreaRequestMsgOutProto;

        /**
         * Creates a PixelsAreaRequestMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns PixelsAreaRequestMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.PixelsAreaRequestMsgOutProto;

        /**
         * Creates a plain object from a PixelsAreaRequestMsgOutProto message. Also converts values to other types if specified.
         * @param message PixelsAreaRequestMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.PixelsAreaRequestMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this PixelsAreaRequestMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for PixelsAreaRequestMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a ComponentTreeMsgOutProto. */
    interface IComponentTreeMsgOutProto {

        /** ComponentTreeMsgOutProto componentType */
        componentType?: (string|null);

        /** ComponentTreeMsgOutProto name */
        name?: (string|null);

        /** ComponentTreeMsgOutProto value */
        value?: (string|null);

        /** ComponentTreeMsgOutProto screenX */
        screenX?: (number|null);

        /** ComponentTreeMsgOutProto screenY */
        screenY?: (number|null);

        /** ComponentTreeMsgOutProto width */
        width?: (number|null);

        /** ComponentTreeMsgOutProto height */
        height?: (number|null);

        /** ComponentTreeMsgOutProto enabled */
        enabled?: (boolean|null);

        /** ComponentTreeMsgOutProto visible */
        visible?: (boolean|null);

        /** ComponentTreeMsgOutProto components */
        components?: (appFrameProtoOut.IComponentTreeMsgOutProto[]|null);

        /** ComponentTreeMsgOutProto hidden */
        hidden?: (boolean|null);

        /** ComponentTreeMsgOutProto selected */
        selected?: (boolean|null);
    }

    /** Represents a ComponentTreeMsgOutProto. */
    class ComponentTreeMsgOutProto implements IComponentTreeMsgOutProto {

        /**
         * Constructs a new ComponentTreeMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IComponentTreeMsgOutProto);

        /** ComponentTreeMsgOutProto componentType. */
        public componentType: string;

        /** ComponentTreeMsgOutProto name. */
        public name: string;

        /** ComponentTreeMsgOutProto value. */
        public value: string;

        /** ComponentTreeMsgOutProto screenX. */
        public screenX: number;

        /** ComponentTreeMsgOutProto screenY. */
        public screenY: number;

        /** ComponentTreeMsgOutProto width. */
        public width: number;

        /** ComponentTreeMsgOutProto height. */
        public height: number;

        /** ComponentTreeMsgOutProto enabled. */
        public enabled: boolean;

        /** ComponentTreeMsgOutProto visible. */
        public visible: boolean;

        /** ComponentTreeMsgOutProto components. */
        public components: appFrameProtoOut.IComponentTreeMsgOutProto[];

        /** ComponentTreeMsgOutProto hidden. */
        public hidden: boolean;

        /** ComponentTreeMsgOutProto selected. */
        public selected: boolean;

        /**
         * Creates a new ComponentTreeMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ComponentTreeMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IComponentTreeMsgOutProto): appFrameProtoOut.ComponentTreeMsgOutProto;

        /**
         * Decodes a ComponentTreeMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ComponentTreeMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.ComponentTreeMsgOutProto;

        /**
         * Creates a ComponentTreeMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ComponentTreeMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.ComponentTreeMsgOutProto;

        /**
         * Creates a plain object from a ComponentTreeMsgOutProto message. Also converts values to other types if specified.
         * @param message ComponentTreeMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.ComponentTreeMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ComponentTreeMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ComponentTreeMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an ActionEventMsgOutProto. */
    interface IActionEventMsgOutProto {

        /** ActionEventMsgOutProto actionName */
        actionName?: (string|null);

        /** ActionEventMsgOutProto data */
        data?: (string|null);

        /** ActionEventMsgOutProto binaryData */
        binaryData?: (Uint8Array|null);

        /** ActionEventMsgOutProto windowId */
        windowId?: (string|null);
    }

    /** Represents an ActionEventMsgOutProto. */
    class ActionEventMsgOutProto implements IActionEventMsgOutProto {

        /**
         * Constructs a new ActionEventMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IActionEventMsgOutProto);

        /** ActionEventMsgOutProto actionName. */
        public actionName: string;

        /** ActionEventMsgOutProto data. */
        public data: string;

        /** ActionEventMsgOutProto binaryData. */
        public binaryData: Uint8Array;

        /** ActionEventMsgOutProto windowId. */
        public windowId: string;

        /**
         * Creates a new ActionEventMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ActionEventMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IActionEventMsgOutProto): appFrameProtoOut.ActionEventMsgOutProto;

        /**
         * Decodes an ActionEventMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ActionEventMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.ActionEventMsgOutProto;

        /**
         * Creates an ActionEventMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ActionEventMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.ActionEventMsgOutProto;

        /**
         * Creates a plain object from an ActionEventMsgOutProto message. Also converts values to other types if specified.
         * @param message ActionEventMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.ActionEventMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ActionEventMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ActionEventMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an AudioEventMsgOutProto. */
    interface IAudioEventMsgOutProto {

        /** AudioEventMsgOutProto id */
        id?: (string|null);

        /** AudioEventMsgOutProto eventType */
        eventType?: (appFrameProtoOut.AudioEventMsgOutProto.AudioEventTypeProto|null);

        /** AudioEventMsgOutProto data */
        data?: (Uint8Array|null);

        /** AudioEventMsgOutProto time */
        time?: (number|null);

        /** AudioEventMsgOutProto loop */
        loop?: (number|null);
    }

    /** Represents an AudioEventMsgOutProto. */
    class AudioEventMsgOutProto implements IAudioEventMsgOutProto {

        /**
         * Constructs a new AudioEventMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.IAudioEventMsgOutProto);

        /** AudioEventMsgOutProto id. */
        public id: string;

        /** AudioEventMsgOutProto eventType. */
        public eventType: appFrameProtoOut.AudioEventMsgOutProto.AudioEventTypeProto;

        /** AudioEventMsgOutProto data. */
        public data: Uint8Array;

        /** AudioEventMsgOutProto time. */
        public time: number;

        /** AudioEventMsgOutProto loop. */
        public loop: number;

        /**
         * Creates a new AudioEventMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns AudioEventMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.IAudioEventMsgOutProto): appFrameProtoOut.AudioEventMsgOutProto;

        /**
         * Decodes an AudioEventMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns AudioEventMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.AudioEventMsgOutProto;

        /**
         * Creates an AudioEventMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns AudioEventMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.AudioEventMsgOutProto;

        /**
         * Creates a plain object from an AudioEventMsgOutProto message. Also converts values to other types if specified.
         * @param message AudioEventMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.AudioEventMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this AudioEventMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for AudioEventMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace AudioEventMsgOutProto {

        /** AudioEventTypeProto enum. */
        enum AudioEventTypeProto {
            play = 0,
            stop = 1,
            update = 2,
            dispose = 3
        }
    }

    /** Properties of a CursorChangeEventMsgOutProto. */
    interface ICursorChangeEventMsgOutProto {

        /** CursorChangeEventMsgOutProto cursor */
        cursor?: (string|null);

        /** CursorChangeEventMsgOutProto b64img */
        b64img?: (Uint8Array|null);

        /** CursorChangeEventMsgOutProto curFile */
        curFile?: (string|null);

        /** CursorChangeEventMsgOutProto x */
        x?: (number|null);

        /** CursorChangeEventMsgOutProto y */
        y?: (number|null);

        /** CursorChangeEventMsgOutProto winId */
        winId?: (string|null);
    }

    /** Represents a CursorChangeEventMsgOutProto. */
    class CursorChangeEventMsgOutProto implements ICursorChangeEventMsgOutProto {

        /**
         * Constructs a new CursorChangeEventMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoOut.ICursorChangeEventMsgOutProto);

        /** CursorChangeEventMsgOutProto cursor. */
        public cursor: string;

        /** CursorChangeEventMsgOutProto b64img. */
        public b64img: Uint8Array;

        /** CursorChangeEventMsgOutProto curFile. */
        public curFile: string;

        /** CursorChangeEventMsgOutProto x. */
        public x: number;

        /** CursorChangeEventMsgOutProto y. */
        public y: number;

        /** CursorChangeEventMsgOutProto winId. */
        public winId: string;

        /**
         * Creates a new CursorChangeEventMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns CursorChangeEventMsgOutProto instance
         */
        public static create(properties?: appFrameProtoOut.ICursorChangeEventMsgOutProto): appFrameProtoOut.CursorChangeEventMsgOutProto;

        /**
         * Decodes a CursorChangeEventMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns CursorChangeEventMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): appFrameProtoOut.CursorChangeEventMsgOutProto;

        /**
         * Creates a CursorChangeEventMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns CursorChangeEventMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoOut.CursorChangeEventMsgOutProto;

        /**
         * Creates a plain object from a CursorChangeEventMsgOutProto message. Also converts values to other types if specified.
         * @param message CursorChangeEventMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoOut.CursorChangeEventMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this CursorChangeEventMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for CursorChangeEventMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}
