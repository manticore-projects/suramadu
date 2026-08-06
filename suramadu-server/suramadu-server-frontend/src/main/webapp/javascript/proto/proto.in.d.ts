import * as $protobuf from "protobufjs";
import Long = require("long");
/** Namespace appFrameProtoIn. */
export namespace appFrameProtoIn {

    /** Properties of an AppFrameMsgInProto. */
    interface IAppFrameMsgInProto {

        /** AppFrameMsgInProto events */
        events?: (appFrameProtoIn.IInputEventMsgInProto[]|null);

        /** AppFrameMsgInProto paste */
        paste?: (appFrameProtoIn.IPasteEventMsgInProto|null);

        /** AppFrameMsgInProto copy */
        copy?: (appFrameProtoIn.ICopyEventMsgInProto|null);

        /** AppFrameMsgInProto upload */
        upload?: (appFrameProtoIn.IUploadEventMsgInProto|null);

        /** AppFrameMsgInProto selected */
        selected?: (appFrameProtoIn.IFilesSelectedEventMsgInProto|null);

        /** AppFrameMsgInProto jsResponse */
        jsResponse?: (appFrameProtoIn.IJsResultMsgInProto|null);

        /** AppFrameMsgInProto javaRequest */
        javaRequest?: (appFrameProtoIn.IJavaEvalRequestMsgInProto|null);

        /** AppFrameMsgInProto pixelsResponse */
        pixelsResponse?: (appFrameProtoIn.IPixelsAreaResponseMsgInProto|null);

        /** AppFrameMsgInProto window */
        window?: (appFrameProtoIn.IWindowEventMsgInProto|null);

        /** AppFrameMsgInProto action */
        action?: (appFrameProtoIn.IActionEventMsgInProto|null);

        /** AppFrameMsgInProto audio */
        audio?: (appFrameProtoIn.IAudioEventMsgInProto|null);
    }

    /** Represents an AppFrameMsgInProto. */
    class AppFrameMsgInProto implements IAppFrameMsgInProto {

        /**
         * Constructs a new AppFrameMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IAppFrameMsgInProto);

        /** AppFrameMsgInProto events. */
        public events: appFrameProtoIn.IInputEventMsgInProto[];

        /** AppFrameMsgInProto paste. */
        public paste?: (appFrameProtoIn.IPasteEventMsgInProto|null);

        /** AppFrameMsgInProto copy. */
        public copy?: (appFrameProtoIn.ICopyEventMsgInProto|null);

        /** AppFrameMsgInProto upload. */
        public upload?: (appFrameProtoIn.IUploadEventMsgInProto|null);

        /** AppFrameMsgInProto selected. */
        public selected?: (appFrameProtoIn.IFilesSelectedEventMsgInProto|null);

        /** AppFrameMsgInProto jsResponse. */
        public jsResponse?: (appFrameProtoIn.IJsResultMsgInProto|null);

        /** AppFrameMsgInProto javaRequest. */
        public javaRequest?: (appFrameProtoIn.IJavaEvalRequestMsgInProto|null);

        /** AppFrameMsgInProto pixelsResponse. */
        public pixelsResponse?: (appFrameProtoIn.IPixelsAreaResponseMsgInProto|null);

        /** AppFrameMsgInProto window. */
        public window?: (appFrameProtoIn.IWindowEventMsgInProto|null);

        /** AppFrameMsgInProto action. */
        public action?: (appFrameProtoIn.IActionEventMsgInProto|null);

        /** AppFrameMsgInProto audio. */
        public audio?: (appFrameProtoIn.IAudioEventMsgInProto|null);

        /**
         * Creates a new AppFrameMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns AppFrameMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IAppFrameMsgInProto): appFrameProtoIn.AppFrameMsgInProto;

        /**
         * Encodes the specified AppFrameMsgInProto message. Does not implicitly {@link appFrameProtoIn.AppFrameMsgInProto.verify|verify} messages.
         * @param message AppFrameMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IAppFrameMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates an AppFrameMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns AppFrameMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.AppFrameMsgInProto;

        /**
         * Creates a plain object from an AppFrameMsgInProto message. Also converts values to other types if specified.
         * @param message AppFrameMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.AppFrameMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this AppFrameMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for AppFrameMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an InputEventMsgInProto. */
    interface IInputEventMsgInProto {

        /** InputEventMsgInProto key */
        key?: (appFrameProtoIn.IKeyboardEventMsgInProto|null);

        /** InputEventMsgInProto mouse */
        mouse?: (appFrameProtoIn.IMouseEventMsgInProto|null);

        /** InputEventMsgInProto focus */
        focus?: (appFrameProtoIn.IWindowFocusMsgInProto|null);
    }

    /** Represents an InputEventMsgInProto. */
    class InputEventMsgInProto implements IInputEventMsgInProto {

        /**
         * Constructs a new InputEventMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IInputEventMsgInProto);

        /** InputEventMsgInProto key. */
        public key?: (appFrameProtoIn.IKeyboardEventMsgInProto|null);

        /** InputEventMsgInProto mouse. */
        public mouse?: (appFrameProtoIn.IMouseEventMsgInProto|null);

        /** InputEventMsgInProto focus. */
        public focus?: (appFrameProtoIn.IWindowFocusMsgInProto|null);

        /**
         * Creates a new InputEventMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns InputEventMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IInputEventMsgInProto): appFrameProtoIn.InputEventMsgInProto;

        /**
         * Encodes the specified InputEventMsgInProto message. Does not implicitly {@link appFrameProtoIn.InputEventMsgInProto.verify|verify} messages.
         * @param message InputEventMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IInputEventMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates an InputEventMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns InputEventMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.InputEventMsgInProto;

        /**
         * Creates a plain object from an InputEventMsgInProto message. Also converts values to other types if specified.
         * @param message InputEventMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.InputEventMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this InputEventMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for InputEventMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a WindowFocusMsgInProto. */
    interface IWindowFocusMsgInProto {

        /** WindowFocusMsgInProto windowId */
        windowId?: (string|null);

        /** WindowFocusMsgInProto htmlPanelId */
        htmlPanelId?: (string|null);
    }

    /** Represents a WindowFocusMsgInProto. */
    class WindowFocusMsgInProto implements IWindowFocusMsgInProto {

        /**
         * Constructs a new WindowFocusMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IWindowFocusMsgInProto);

        /** WindowFocusMsgInProto windowId. */
        public windowId: string;

        /** WindowFocusMsgInProto htmlPanelId. */
        public htmlPanelId: string;

        /**
         * Creates a new WindowFocusMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns WindowFocusMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IWindowFocusMsgInProto): appFrameProtoIn.WindowFocusMsgInProto;

        /**
         * Encodes the specified WindowFocusMsgInProto message. Does not implicitly {@link appFrameProtoIn.WindowFocusMsgInProto.verify|verify} messages.
         * @param message WindowFocusMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IWindowFocusMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates a WindowFocusMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns WindowFocusMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.WindowFocusMsgInProto;

        /**
         * Creates a plain object from a WindowFocusMsgInProto message. Also converts values to other types if specified.
         * @param message WindowFocusMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.WindowFocusMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this WindowFocusMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for WindowFocusMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a KeyboardEventMsgInProto. */
    interface IKeyboardEventMsgInProto {

        /** KeyboardEventMsgInProto type */
        type?: (appFrameProtoIn.KeyboardEventMsgInProto.KeyEventTypeProto|null);

        /** KeyboardEventMsgInProto character */
        character?: (number|null);

        /** KeyboardEventMsgInProto keycode */
        keycode?: (number|null);

        /** KeyboardEventMsgInProto alt */
        alt?: (boolean|null);

        /** KeyboardEventMsgInProto ctrl */
        ctrl?: (boolean|null);

        /** KeyboardEventMsgInProto shift */
        shift?: (boolean|null);

        /** KeyboardEventMsgInProto meta */
        meta?: (boolean|null);
    }

    /** Represents a KeyboardEventMsgInProto. */
    class KeyboardEventMsgInProto implements IKeyboardEventMsgInProto {

        /**
         * Constructs a new KeyboardEventMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IKeyboardEventMsgInProto);

        /** KeyboardEventMsgInProto type. */
        public type: appFrameProtoIn.KeyboardEventMsgInProto.KeyEventTypeProto;

        /** KeyboardEventMsgInProto character. */
        public character: number;

        /** KeyboardEventMsgInProto keycode. */
        public keycode: number;

        /** KeyboardEventMsgInProto alt. */
        public alt: boolean;

        /** KeyboardEventMsgInProto ctrl. */
        public ctrl: boolean;

        /** KeyboardEventMsgInProto shift. */
        public shift: boolean;

        /** KeyboardEventMsgInProto meta. */
        public meta: boolean;

        /**
         * Creates a new KeyboardEventMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns KeyboardEventMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IKeyboardEventMsgInProto): appFrameProtoIn.KeyboardEventMsgInProto;

        /**
         * Encodes the specified KeyboardEventMsgInProto message. Does not implicitly {@link appFrameProtoIn.KeyboardEventMsgInProto.verify|verify} messages.
         * @param message KeyboardEventMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IKeyboardEventMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates a KeyboardEventMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns KeyboardEventMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.KeyboardEventMsgInProto;

        /**
         * Creates a plain object from a KeyboardEventMsgInProto message. Also converts values to other types if specified.
         * @param message KeyboardEventMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.KeyboardEventMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this KeyboardEventMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for KeyboardEventMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace KeyboardEventMsgInProto {

        /** KeyEventTypeProto enum. */
        enum KeyEventTypeProto {
            keypress = 0,
            keydown = 1,
            keyup = 2
        }
    }

    /** Properties of a MouseEventMsgInProto. */
    interface IMouseEventMsgInProto {

        /** MouseEventMsgInProto type */
        type?: (appFrameProtoIn.MouseEventMsgInProto.MouseEventTypeProto|null);

        /** MouseEventMsgInProto x */
        x?: (number|null);

        /** MouseEventMsgInProto y */
        y?: (number|null);

        /** MouseEventMsgInProto wheelDelta */
        wheelDelta?: (number|null);

        /** MouseEventMsgInProto button */
        button?: (number|null);

        /** MouseEventMsgInProto ctrl */
        ctrl?: (boolean|null);

        /** MouseEventMsgInProto alt */
        alt?: (boolean|null);

        /** MouseEventMsgInProto shift */
        shift?: (boolean|null);

        /** MouseEventMsgInProto meta */
        meta?: (boolean|null);

        /** MouseEventMsgInProto buttons */
        buttons?: (number|null);

        /** MouseEventMsgInProto timeMilis */
        timeMilis?: (number|null);

        /** MouseEventMsgInProto winId */
        winId?: (string|null);
    }

    /** Represents a MouseEventMsgInProto. */
    class MouseEventMsgInProto implements IMouseEventMsgInProto {

        /**
         * Constructs a new MouseEventMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IMouseEventMsgInProto);

        /** MouseEventMsgInProto type. */
        public type: appFrameProtoIn.MouseEventMsgInProto.MouseEventTypeProto;

        /** MouseEventMsgInProto x. */
        public x: number;

        /** MouseEventMsgInProto y. */
        public y: number;

        /** MouseEventMsgInProto wheelDelta. */
        public wheelDelta: number;

        /** MouseEventMsgInProto button. */
        public button: number;

        /** MouseEventMsgInProto ctrl. */
        public ctrl: boolean;

        /** MouseEventMsgInProto alt. */
        public alt: boolean;

        /** MouseEventMsgInProto shift. */
        public shift: boolean;

        /** MouseEventMsgInProto meta. */
        public meta: boolean;

        /** MouseEventMsgInProto buttons. */
        public buttons: number;

        /** MouseEventMsgInProto timeMilis. */
        public timeMilis: number;

        /** MouseEventMsgInProto winId. */
        public winId: string;

        /**
         * Creates a new MouseEventMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns MouseEventMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IMouseEventMsgInProto): appFrameProtoIn.MouseEventMsgInProto;

        /**
         * Encodes the specified MouseEventMsgInProto message. Does not implicitly {@link appFrameProtoIn.MouseEventMsgInProto.verify|verify} messages.
         * @param message MouseEventMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IMouseEventMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates a MouseEventMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns MouseEventMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.MouseEventMsgInProto;

        /**
         * Creates a plain object from a MouseEventMsgInProto message. Also converts values to other types if specified.
         * @param message MouseEventMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.MouseEventMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this MouseEventMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for MouseEventMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace MouseEventMsgInProto {

        /** MouseEventTypeProto enum. */
        enum MouseEventTypeProto {
            mousemove = 0,
            mousedown = 1,
            mouseup = 2,
            mousewheel = 3,
            dblclick = 4
        }
    }

    /** Properties of a CopyEventMsgInProto. */
    interface ICopyEventMsgInProto {

        /** CopyEventMsgInProto type */
        type?: (appFrameProtoIn.CopyEventMsgInProto.CopyEventMsgTypeProto|null);

        /** CopyEventMsgInProto file */
        file?: (string|null);
    }

    /** Represents a CopyEventMsgInProto. */
    class CopyEventMsgInProto implements ICopyEventMsgInProto {

        /**
         * Constructs a new CopyEventMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.ICopyEventMsgInProto);

        /** CopyEventMsgInProto type. */
        public type: appFrameProtoIn.CopyEventMsgInProto.CopyEventMsgTypeProto;

        /** CopyEventMsgInProto file. */
        public file: string;

        /**
         * Creates a new CopyEventMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns CopyEventMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.ICopyEventMsgInProto): appFrameProtoIn.CopyEventMsgInProto;

        /**
         * Encodes the specified CopyEventMsgInProto message. Does not implicitly {@link appFrameProtoIn.CopyEventMsgInProto.verify|verify} messages.
         * @param message CopyEventMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.ICopyEventMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates a CopyEventMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns CopyEventMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.CopyEventMsgInProto;

        /**
         * Creates a plain object from a CopyEventMsgInProto message. Also converts values to other types if specified.
         * @param message CopyEventMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.CopyEventMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this CopyEventMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for CopyEventMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace CopyEventMsgInProto {

        /** CopyEventMsgTypeProto enum. */
        enum CopyEventMsgTypeProto {
            copy = 0,
            cut = 1,
            getFileFromClipboard = 2
        }
    }

    /** Properties of a PasteEventMsgInProto. */
    interface IPasteEventMsgInProto {

        /** PasteEventMsgInProto text */
        text?: (string|null);

        /** PasteEventMsgInProto html */
        html?: (string|null);

        /** PasteEventMsgInProto img */
        img?: (string|null);

        /** PasteEventMsgInProto special */
        special?: (boolean|null);
    }

    /** Represents a PasteEventMsgInProto. */
    class PasteEventMsgInProto implements IPasteEventMsgInProto {

        /**
         * Constructs a new PasteEventMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IPasteEventMsgInProto);

        /** PasteEventMsgInProto text. */
        public text: string;

        /** PasteEventMsgInProto html. */
        public html: string;

        /** PasteEventMsgInProto img. */
        public img: string;

        /** PasteEventMsgInProto special. */
        public special: boolean;

        /**
         * Creates a new PasteEventMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns PasteEventMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IPasteEventMsgInProto): appFrameProtoIn.PasteEventMsgInProto;

        /**
         * Encodes the specified PasteEventMsgInProto message. Does not implicitly {@link appFrameProtoIn.PasteEventMsgInProto.verify|verify} messages.
         * @param message PasteEventMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IPasteEventMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates a PasteEventMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns PasteEventMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.PasteEventMsgInProto;

        /**
         * Creates a plain object from a PasteEventMsgInProto message. Also converts values to other types if specified.
         * @param message PasteEventMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.PasteEventMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this PasteEventMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for PasteEventMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a FilesSelectedEventMsgInProto. */
    interface IFilesSelectedEventMsgInProto {

        /** FilesSelectedEventMsgInProto files */
        files?: (string[]|null);
    }

    /** Represents a FilesSelectedEventMsgInProto. */
    class FilesSelectedEventMsgInProto implements IFilesSelectedEventMsgInProto {

        /**
         * Constructs a new FilesSelectedEventMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IFilesSelectedEventMsgInProto);

        /** FilesSelectedEventMsgInProto files. */
        public files: string[];

        /**
         * Creates a new FilesSelectedEventMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns FilesSelectedEventMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IFilesSelectedEventMsgInProto): appFrameProtoIn.FilesSelectedEventMsgInProto;

        /**
         * Encodes the specified FilesSelectedEventMsgInProto message. Does not implicitly {@link appFrameProtoIn.FilesSelectedEventMsgInProto.verify|verify} messages.
         * @param message FilesSelectedEventMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IFilesSelectedEventMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates a FilesSelectedEventMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns FilesSelectedEventMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.FilesSelectedEventMsgInProto;

        /**
         * Creates a plain object from a FilesSelectedEventMsgInProto message. Also converts values to other types if specified.
         * @param message FilesSelectedEventMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.FilesSelectedEventMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this FilesSelectedEventMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for FilesSelectedEventMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UploadEventMsgInProto. */
    interface IUploadEventMsgInProto {

        /** UploadEventMsgInProto fileId */
        fileId?: (string|null);
    }

    /** Represents an UploadEventMsgInProto. */
    class UploadEventMsgInProto implements IUploadEventMsgInProto {

        /**
         * Constructs a new UploadEventMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IUploadEventMsgInProto);

        /** UploadEventMsgInProto fileId. */
        public fileId: string;

        /**
         * Creates a new UploadEventMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UploadEventMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IUploadEventMsgInProto): appFrameProtoIn.UploadEventMsgInProto;

        /**
         * Encodes the specified UploadEventMsgInProto message. Does not implicitly {@link appFrameProtoIn.UploadEventMsgInProto.verify|verify} messages.
         * @param message UploadEventMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IUploadEventMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates an UploadEventMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UploadEventMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.UploadEventMsgInProto;

        /**
         * Creates a plain object from an UploadEventMsgInProto message. Also converts values to other types if specified.
         * @param message UploadEventMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.UploadEventMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UploadEventMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UploadEventMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a JavaEvalRequestMsgInProto. */
    interface IJavaEvalRequestMsgInProto {

        /** JavaEvalRequestMsgInProto correlationId */
        correlationId?: (string|null);

        /** JavaEvalRequestMsgInProto objectId */
        objectId?: (string|null);

        /** JavaEvalRequestMsgInProto method */
        method?: (string|null);

        /** JavaEvalRequestMsgInProto params */
        params?: (appFrameProtoIn.IJsParamMsgInProto[]|null);
    }

    /** Represents a JavaEvalRequestMsgInProto. */
    class JavaEvalRequestMsgInProto implements IJavaEvalRequestMsgInProto {

        /**
         * Constructs a new JavaEvalRequestMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IJavaEvalRequestMsgInProto);

        /** JavaEvalRequestMsgInProto correlationId. */
        public correlationId: string;

        /** JavaEvalRequestMsgInProto objectId. */
        public objectId: string;

        /** JavaEvalRequestMsgInProto method. */
        public method: string;

        /** JavaEvalRequestMsgInProto params. */
        public params: appFrameProtoIn.IJsParamMsgInProto[];

        /**
         * Creates a new JavaEvalRequestMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns JavaEvalRequestMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IJavaEvalRequestMsgInProto): appFrameProtoIn.JavaEvalRequestMsgInProto;

        /**
         * Encodes the specified JavaEvalRequestMsgInProto message. Does not implicitly {@link appFrameProtoIn.JavaEvalRequestMsgInProto.verify|verify} messages.
         * @param message JavaEvalRequestMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IJavaEvalRequestMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates a JavaEvalRequestMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns JavaEvalRequestMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.JavaEvalRequestMsgInProto;

        /**
         * Creates a plain object from a JavaEvalRequestMsgInProto message. Also converts values to other types if specified.
         * @param message JavaEvalRequestMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.JavaEvalRequestMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this JavaEvalRequestMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for JavaEvalRequestMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a JsResultMsgInProto. */
    interface IJsResultMsgInProto {

        /** JsResultMsgInProto correlationId */
        correlationId?: (string|null);

        /** JsResultMsgInProto error */
        error?: (string|null);

        /** JsResultMsgInProto value */
        value?: (appFrameProtoIn.IJsParamMsgInProto|null);
    }

    /** Represents a JsResultMsgInProto. */
    class JsResultMsgInProto implements IJsResultMsgInProto {

        /**
         * Constructs a new JsResultMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IJsResultMsgInProto);

        /** JsResultMsgInProto correlationId. */
        public correlationId: string;

        /** JsResultMsgInProto error. */
        public error: string;

        /** JsResultMsgInProto value. */
        public value?: (appFrameProtoIn.IJsParamMsgInProto|null);

        /**
         * Creates a new JsResultMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns JsResultMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IJsResultMsgInProto): appFrameProtoIn.JsResultMsgInProto;

        /**
         * Encodes the specified JsResultMsgInProto message. Does not implicitly {@link appFrameProtoIn.JsResultMsgInProto.verify|verify} messages.
         * @param message JsResultMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IJsResultMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates a JsResultMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns JsResultMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.JsResultMsgInProto;

        /**
         * Creates a plain object from a JsResultMsgInProto message. Also converts values to other types if specified.
         * @param message JsResultMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.JsResultMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this JsResultMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for JsResultMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a JsParamMsgInProto. */
    interface IJsParamMsgInProto {

        /** JsParamMsgInProto primitive */
        primitive?: (string|null);

        /** JsParamMsgInProto jsObject */
        jsObject?: (appFrameProtoIn.IJSObjectMsgInProto|null);

        /** JsParamMsgInProto javaObject */
        javaObject?: (appFrameProtoIn.IJavaObjectRefMsgInProto|null);

        /** JsParamMsgInProto array */
        array?: (appFrameProtoIn.IJsParamMsgInProto[]|null);
    }

    /** Represents a JsParamMsgInProto. */
    class JsParamMsgInProto implements IJsParamMsgInProto {

        /**
         * Constructs a new JsParamMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IJsParamMsgInProto);

        /** JsParamMsgInProto primitive. */
        public primitive: string;

        /** JsParamMsgInProto jsObject. */
        public jsObject?: (appFrameProtoIn.IJSObjectMsgInProto|null);

        /** JsParamMsgInProto javaObject. */
        public javaObject?: (appFrameProtoIn.IJavaObjectRefMsgInProto|null);

        /** JsParamMsgInProto array. */
        public array: appFrameProtoIn.IJsParamMsgInProto[];

        /**
         * Creates a new JsParamMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns JsParamMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IJsParamMsgInProto): appFrameProtoIn.JsParamMsgInProto;

        /**
         * Encodes the specified JsParamMsgInProto message. Does not implicitly {@link appFrameProtoIn.JsParamMsgInProto.verify|verify} messages.
         * @param message JsParamMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IJsParamMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates a JsParamMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns JsParamMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.JsParamMsgInProto;

        /**
         * Creates a plain object from a JsParamMsgInProto message. Also converts values to other types if specified.
         * @param message JsParamMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.JsParamMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this JsParamMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for JsParamMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a JavaObjectRefMsgInProto. */
    interface IJavaObjectRefMsgInProto {

        /** JavaObjectRefMsgInProto id */
        id?: (string|null);

        /** JavaObjectRefMsgInProto methods */
        methods?: (string[]|null);
    }

    /** Represents a JavaObjectRefMsgInProto. */
    class JavaObjectRefMsgInProto implements IJavaObjectRefMsgInProto {

        /**
         * Constructs a new JavaObjectRefMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IJavaObjectRefMsgInProto);

        /** JavaObjectRefMsgInProto id. */
        public id: string;

        /** JavaObjectRefMsgInProto methods. */
        public methods: string[];

        /**
         * Creates a new JavaObjectRefMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns JavaObjectRefMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IJavaObjectRefMsgInProto): appFrameProtoIn.JavaObjectRefMsgInProto;

        /**
         * Encodes the specified JavaObjectRefMsgInProto message. Does not implicitly {@link appFrameProtoIn.JavaObjectRefMsgInProto.verify|verify} messages.
         * @param message JavaObjectRefMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IJavaObjectRefMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates a JavaObjectRefMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns JavaObjectRefMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.JavaObjectRefMsgInProto;

        /**
         * Creates a plain object from a JavaObjectRefMsgInProto message. Also converts values to other types if specified.
         * @param message JavaObjectRefMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.JavaObjectRefMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this JavaObjectRefMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for JavaObjectRefMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a JSObjectMsgInProto. */
    interface IJSObjectMsgInProto {

        /** JSObjectMsgInProto id */
        id?: (string|null);
    }

    /** Represents a JSObjectMsgInProto. */
    class JSObjectMsgInProto implements IJSObjectMsgInProto {

        /**
         * Constructs a new JSObjectMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IJSObjectMsgInProto);

        /** JSObjectMsgInProto id. */
        public id: string;

        /**
         * Creates a new JSObjectMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns JSObjectMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IJSObjectMsgInProto): appFrameProtoIn.JSObjectMsgInProto;

        /**
         * Encodes the specified JSObjectMsgInProto message. Does not implicitly {@link appFrameProtoIn.JSObjectMsgInProto.verify|verify} messages.
         * @param message JSObjectMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IJSObjectMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates a JSObjectMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns JSObjectMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.JSObjectMsgInProto;

        /**
         * Creates a plain object from a JSObjectMsgInProto message. Also converts values to other types if specified.
         * @param message JSObjectMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.JSObjectMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this JSObjectMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for JSObjectMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a PixelsAreaResponseMsgInProto. */
    interface IPixelsAreaResponseMsgInProto {

        /** PixelsAreaResponseMsgInProto correlationId */
        correlationId?: (string|null);

        /** PixelsAreaResponseMsgInProto pixels */
        pixels?: (string|null);
    }

    /** Represents a PixelsAreaResponseMsgInProto. */
    class PixelsAreaResponseMsgInProto implements IPixelsAreaResponseMsgInProto {

        /**
         * Constructs a new PixelsAreaResponseMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IPixelsAreaResponseMsgInProto);

        /** PixelsAreaResponseMsgInProto correlationId. */
        public correlationId: string;

        /** PixelsAreaResponseMsgInProto pixels. */
        public pixels: string;

        /**
         * Creates a new PixelsAreaResponseMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns PixelsAreaResponseMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IPixelsAreaResponseMsgInProto): appFrameProtoIn.PixelsAreaResponseMsgInProto;

        /**
         * Encodes the specified PixelsAreaResponseMsgInProto message. Does not implicitly {@link appFrameProtoIn.PixelsAreaResponseMsgInProto.verify|verify} messages.
         * @param message PixelsAreaResponseMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IPixelsAreaResponseMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates a PixelsAreaResponseMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns PixelsAreaResponseMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.PixelsAreaResponseMsgInProto;

        /**
         * Creates a plain object from a PixelsAreaResponseMsgInProto message. Also converts values to other types if specified.
         * @param message PixelsAreaResponseMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.PixelsAreaResponseMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this PixelsAreaResponseMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for PixelsAreaResponseMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a WindowEventMsgInProto. */
    interface IWindowEventMsgInProto {

        /** WindowEventMsgInProto id */
        id?: (string|null);

        /** WindowEventMsgInProto x */
        x?: (number|null);

        /** WindowEventMsgInProto y */
        y?: (number|null);

        /** WindowEventMsgInProto width */
        width?: (number|null);

        /** WindowEventMsgInProto height */
        height?: (number|null);

        /** WindowEventMsgInProto eventType */
        eventType?: (appFrameProtoIn.WindowEventMsgInProto.WindowEventTypeProto|null);
    }

    /** Represents a WindowEventMsgInProto. */
    class WindowEventMsgInProto implements IWindowEventMsgInProto {

        /**
         * Constructs a new WindowEventMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IWindowEventMsgInProto);

        /** WindowEventMsgInProto id. */
        public id: string;

        /** WindowEventMsgInProto x. */
        public x: number;

        /** WindowEventMsgInProto y. */
        public y: number;

        /** WindowEventMsgInProto width. */
        public width: number;

        /** WindowEventMsgInProto height. */
        public height: number;

        /** WindowEventMsgInProto eventType. */
        public eventType: appFrameProtoIn.WindowEventMsgInProto.WindowEventTypeProto;

        /**
         * Creates a new WindowEventMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns WindowEventMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IWindowEventMsgInProto): appFrameProtoIn.WindowEventMsgInProto;

        /**
         * Encodes the specified WindowEventMsgInProto message. Does not implicitly {@link appFrameProtoIn.WindowEventMsgInProto.verify|verify} messages.
         * @param message WindowEventMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IWindowEventMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates a WindowEventMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns WindowEventMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.WindowEventMsgInProto;

        /**
         * Creates a plain object from a WindowEventMsgInProto message. Also converts values to other types if specified.
         * @param message WindowEventMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.WindowEventMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this WindowEventMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for WindowEventMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace WindowEventMsgInProto {

        /** WindowEventTypeProto enum. */
        enum WindowEventTypeProto {
            close = 0,
            focus = 1,
            maximize = 2,
            undecorate = 3,
            decorate = 4,
            undock = 5,
            dock = 6
        }
    }

    /** Properties of an AudioEventMsgInProto. */
    interface IAudioEventMsgInProto {

        /** AudioEventMsgInProto id */
        id?: (string|null);

        /** AudioEventMsgInProto stop */
        stop?: (boolean|null);

        /** AudioEventMsgInProto ping */
        ping?: (boolean|null);
    }

    /** Represents an AudioEventMsgInProto. */
    class AudioEventMsgInProto implements IAudioEventMsgInProto {

        /**
         * Constructs a new AudioEventMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IAudioEventMsgInProto);

        /** AudioEventMsgInProto id. */
        public id: string;

        /** AudioEventMsgInProto stop. */
        public stop: boolean;

        /** AudioEventMsgInProto ping. */
        public ping: boolean;

        /**
         * Creates a new AudioEventMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns AudioEventMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IAudioEventMsgInProto): appFrameProtoIn.AudioEventMsgInProto;

        /**
         * Encodes the specified AudioEventMsgInProto message. Does not implicitly {@link appFrameProtoIn.AudioEventMsgInProto.verify|verify} messages.
         * @param message AudioEventMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IAudioEventMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates an AudioEventMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns AudioEventMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.AudioEventMsgInProto;

        /**
         * Creates a plain object from an AudioEventMsgInProto message. Also converts values to other types if specified.
         * @param message AudioEventMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.AudioEventMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this AudioEventMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for AudioEventMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an ActionEventMsgInProto. */
    interface IActionEventMsgInProto {

        /** ActionEventMsgInProto actionName */
        actionName?: (string|null);

        /** ActionEventMsgInProto data */
        data?: (string|null);

        /** ActionEventMsgInProto binaryData */
        binaryData?: (Uint8Array|null);

        /** ActionEventMsgInProto windowId */
        windowId?: (string|null);

        /** ActionEventMsgInProto eventType */
        eventType?: (appFrameProtoIn.ActionEventMsgInProto.ActionEventTypeProto|null);
    }

    /** Represents an ActionEventMsgInProto. */
    class ActionEventMsgInProto implements IActionEventMsgInProto {

        /**
         * Constructs a new ActionEventMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: appFrameProtoIn.IActionEventMsgInProto);

        /** ActionEventMsgInProto actionName. */
        public actionName: string;

        /** ActionEventMsgInProto data. */
        public data: string;

        /** ActionEventMsgInProto binaryData. */
        public binaryData: Uint8Array;

        /** ActionEventMsgInProto windowId. */
        public windowId: string;

        /** ActionEventMsgInProto eventType. */
        public eventType: appFrameProtoIn.ActionEventMsgInProto.ActionEventTypeProto;

        /**
         * Creates a new ActionEventMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ActionEventMsgInProto instance
         */
        public static create(properties?: appFrameProtoIn.IActionEventMsgInProto): appFrameProtoIn.ActionEventMsgInProto;

        /**
         * Encodes the specified ActionEventMsgInProto message. Does not implicitly {@link appFrameProtoIn.ActionEventMsgInProto.verify|verify} messages.
         * @param message ActionEventMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: appFrameProtoIn.IActionEventMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Creates an ActionEventMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ActionEventMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): appFrameProtoIn.ActionEventMsgInProto;

        /**
         * Creates a plain object from an ActionEventMsgInProto message. Also converts values to other types if specified.
         * @param message ActionEventMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: appFrameProtoIn.ActionEventMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ActionEventMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ActionEventMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace ActionEventMsgInProto {

        /** ActionEventTypeProto enum. */
        enum ActionEventTypeProto {
            init = 0,
            user = 1
        }
    }
}
