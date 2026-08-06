import * as $protobuf from "protobufjs";
import Long = require("long");
/** Namespace serverBrowserFrameProto. */
export namespace serverBrowserFrameProto {

    /** Properties of a BrowserToServerFrameMsgInProto. */
    interface IBrowserToServerFrameMsgInProto {

        /** BrowserToServerFrameMsgInProto appFrameMsgIn */
        appFrameMsgIn?: (Uint8Array|null);

        /** BrowserToServerFrameMsgInProto handshake */
        handshake?: (commonProto.IConnectionHandshakeMsgInProto|null);

        /** BrowserToServerFrameMsgInProto timestamps */
        timestamps?: (commonProto.ITimestampsMsgInProto[]|null);

        /** BrowserToServerFrameMsgInProto events */
        events?: (commonProto.ISimpleEventMsgInProto[]|null);

        /** BrowserToServerFrameMsgInProto playback */
        playback?: (serverBrowserFrameProto.IPlaybackCommandMsgInProto|null);
    }

    /** Represents a BrowserToServerFrameMsgInProto. */
    class BrowserToServerFrameMsgInProto implements IBrowserToServerFrameMsgInProto {

        /**
         * Constructs a new BrowserToServerFrameMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: serverBrowserFrameProto.IBrowserToServerFrameMsgInProto);

        /** BrowserToServerFrameMsgInProto appFrameMsgIn. */
        public appFrameMsgIn: Uint8Array;

        /** BrowserToServerFrameMsgInProto handshake. */
        public handshake?: (commonProto.IConnectionHandshakeMsgInProto|null);

        /** BrowserToServerFrameMsgInProto timestamps. */
        public timestamps: commonProto.ITimestampsMsgInProto[];

        /** BrowserToServerFrameMsgInProto events. */
        public events: commonProto.ISimpleEventMsgInProto[];

        /** BrowserToServerFrameMsgInProto playback. */
        public playback?: (serverBrowserFrameProto.IPlaybackCommandMsgInProto|null);

        /**
         * Creates a new BrowserToServerFrameMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns BrowserToServerFrameMsgInProto instance
         */
        public static create(properties?: serverBrowserFrameProto.IBrowserToServerFrameMsgInProto): serverBrowserFrameProto.BrowserToServerFrameMsgInProto;

        /**
         * Encodes the specified BrowserToServerFrameMsgInProto message. Does not implicitly {@link serverBrowserFrameProto.BrowserToServerFrameMsgInProto.verify|verify} messages.
         * @param message BrowserToServerFrameMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: serverBrowserFrameProto.IBrowserToServerFrameMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a BrowserToServerFrameMsgInProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns BrowserToServerFrameMsgInProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): serverBrowserFrameProto.BrowserToServerFrameMsgInProto;

        /**
         * Creates a BrowserToServerFrameMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns BrowserToServerFrameMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): serverBrowserFrameProto.BrowserToServerFrameMsgInProto;

        /**
         * Creates a plain object from a BrowserToServerFrameMsgInProto message. Also converts values to other types if specified.
         * @param message BrowserToServerFrameMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: serverBrowserFrameProto.BrowserToServerFrameMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this BrowserToServerFrameMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for BrowserToServerFrameMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a PlaybackCommandMsgInProto. */
    interface IPlaybackCommandMsgInProto {

        /** PlaybackCommandMsgInProto command */
        command?: (serverBrowserFrameProto.PlaybackCommandMsgInProto.PlaybackCommandProto|null);
    }

    /** Represents a PlaybackCommandMsgInProto. */
    class PlaybackCommandMsgInProto implements IPlaybackCommandMsgInProto {

        /**
         * Constructs a new PlaybackCommandMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: serverBrowserFrameProto.IPlaybackCommandMsgInProto);

        /** PlaybackCommandMsgInProto command. */
        public command: serverBrowserFrameProto.PlaybackCommandMsgInProto.PlaybackCommandProto;

        /**
         * Creates a new PlaybackCommandMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns PlaybackCommandMsgInProto instance
         */
        public static create(properties?: serverBrowserFrameProto.IPlaybackCommandMsgInProto): serverBrowserFrameProto.PlaybackCommandMsgInProto;

        /**
         * Encodes the specified PlaybackCommandMsgInProto message. Does not implicitly {@link serverBrowserFrameProto.PlaybackCommandMsgInProto.verify|verify} messages.
         * @param message PlaybackCommandMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: serverBrowserFrameProto.IPlaybackCommandMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a PlaybackCommandMsgInProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns PlaybackCommandMsgInProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): serverBrowserFrameProto.PlaybackCommandMsgInProto;

        /**
         * Creates a PlaybackCommandMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns PlaybackCommandMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): serverBrowserFrameProto.PlaybackCommandMsgInProto;

        /**
         * Creates a plain object from a PlaybackCommandMsgInProto message. Also converts values to other types if specified.
         * @param message PlaybackCommandMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: serverBrowserFrameProto.PlaybackCommandMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this PlaybackCommandMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for PlaybackCommandMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace PlaybackCommandMsgInProto {

        /** PlaybackCommandProto enum. */
        enum PlaybackCommandProto {
            reset = 0,
            play = 1,
            stop = 2,
            step = 3,
            step10 = 4,
            step100 = 5
        }
    }

    /** Properties of a ServerToBrowserFrameMsgOutProto. */
    interface IServerToBrowserFrameMsgOutProto {

        /** ServerToBrowserFrameMsgOutProto appFrameMsgOut */
        appFrameMsgOut?: (Uint8Array|null);

        /** ServerToBrowserFrameMsgOutProto connectionInfo */
        connectionInfo?: (serverBrowserFrameProto.IConnectionInfoMsgOutProto|null);
    }

    /** Represents a ServerToBrowserFrameMsgOutProto. */
    class ServerToBrowserFrameMsgOutProto implements IServerToBrowserFrameMsgOutProto {

        /**
         * Constructs a new ServerToBrowserFrameMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: serverBrowserFrameProto.IServerToBrowserFrameMsgOutProto);

        /** ServerToBrowserFrameMsgOutProto appFrameMsgOut. */
        public appFrameMsgOut: Uint8Array;

        /** ServerToBrowserFrameMsgOutProto connectionInfo. */
        public connectionInfo?: (serverBrowserFrameProto.IConnectionInfoMsgOutProto|null);

        /**
         * Creates a new ServerToBrowserFrameMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ServerToBrowserFrameMsgOutProto instance
         */
        public static create(properties?: serverBrowserFrameProto.IServerToBrowserFrameMsgOutProto): serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto;

        /**
         * Encodes the specified ServerToBrowserFrameMsgOutProto message. Does not implicitly {@link serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto.verify|verify} messages.
         * @param message ServerToBrowserFrameMsgOutProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: serverBrowserFrameProto.IServerToBrowserFrameMsgOutProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ServerToBrowserFrameMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ServerToBrowserFrameMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto;

        /**
         * Creates a ServerToBrowserFrameMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ServerToBrowserFrameMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto;

        /**
         * Creates a plain object from a ServerToBrowserFrameMsgOutProto message. Also converts values to other types if specified.
         * @param message ServerToBrowserFrameMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ServerToBrowserFrameMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ServerToBrowserFrameMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a ConnectionInfoMsgOutProto. */
    interface IConnectionInfoMsgOutProto {

        /** ConnectionInfoMsgOutProto serverId */
        serverId?: (string|null);

        /** ConnectionInfoMsgOutProto sessionPoolId */
        sessionPoolId?: (string|null);

        /** ConnectionInfoMsgOutProto autoLogout */
        autoLogout?: (boolean|null);
    }

    /** Represents a ConnectionInfoMsgOutProto. */
    class ConnectionInfoMsgOutProto implements IConnectionInfoMsgOutProto {

        /**
         * Constructs a new ConnectionInfoMsgOutProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: serverBrowserFrameProto.IConnectionInfoMsgOutProto);

        /** ConnectionInfoMsgOutProto serverId. */
        public serverId: string;

        /** ConnectionInfoMsgOutProto sessionPoolId. */
        public sessionPoolId: string;

        /** ConnectionInfoMsgOutProto autoLogout. */
        public autoLogout: boolean;

        /**
         * Creates a new ConnectionInfoMsgOutProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ConnectionInfoMsgOutProto instance
         */
        public static create(properties?: serverBrowserFrameProto.IConnectionInfoMsgOutProto): serverBrowserFrameProto.ConnectionInfoMsgOutProto;

        /**
         * Encodes the specified ConnectionInfoMsgOutProto message. Does not implicitly {@link serverBrowserFrameProto.ConnectionInfoMsgOutProto.verify|verify} messages.
         * @param message ConnectionInfoMsgOutProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: serverBrowserFrameProto.IConnectionInfoMsgOutProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ConnectionInfoMsgOutProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ConnectionInfoMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): serverBrowserFrameProto.ConnectionInfoMsgOutProto;

        /**
         * Creates a ConnectionInfoMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ConnectionInfoMsgOutProto
         */
        public static fromObject(object: { [k: string]: any }): serverBrowserFrameProto.ConnectionInfoMsgOutProto;

        /**
         * Creates a plain object from a ConnectionInfoMsgOutProto message. Also converts values to other types if specified.
         * @param message ConnectionInfoMsgOutProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: serverBrowserFrameProto.ConnectionInfoMsgOutProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ConnectionInfoMsgOutProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ConnectionInfoMsgOutProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}

/** Namespace commonProto. */
export namespace commonProto {

    /** Properties of a ParamMsgInProto. */
    interface IParamMsgInProto {

        /** ParamMsgInProto name */
        name?: (string|null);

        /** ParamMsgInProto value */
        value?: (string|null);
    }

    /** Represents a ParamMsgInProto. */
    class ParamMsgInProto implements IParamMsgInProto {

        /**
         * Constructs a new ParamMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: commonProto.IParamMsgInProto);

        /** ParamMsgInProto name. */
        public name: string;

        /** ParamMsgInProto value. */
        public value: string;

        /**
         * Creates a new ParamMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ParamMsgInProto instance
         */
        public static create(properties?: commonProto.IParamMsgInProto): commonProto.ParamMsgInProto;

        /**
         * Encodes the specified ParamMsgInProto message. Does not implicitly {@link commonProto.ParamMsgInProto.verify|verify} messages.
         * @param message ParamMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: commonProto.IParamMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ParamMsgInProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ParamMsgInProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): commonProto.ParamMsgInProto;

        /**
         * Creates a ParamMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ParamMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): commonProto.ParamMsgInProto;

        /**
         * Creates a plain object from a ParamMsgInProto message. Also converts values to other types if specified.
         * @param message ParamMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: commonProto.ParamMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ParamMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ParamMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** RecordingStatusEnumProto enum. */
    enum RecordingStatusEnumProto {
        NOT_RECORDING = 1,
        WAITING_FOR_RECORDING_APPROVAL = 2,
        DENIED_RECORDING_BY_USER = 3,
        RECORDING = 4
    }

    /** MirroringStatusEnumProto enum. */
    enum MirroringStatusEnumProto {
        NOT_MIRRORING = 1,
        WAITING_FOR_MIRRORING_APPROVAL = 2,
        DENIED_MIRRORING_BY_USER = 3,
        MIRRORING = 4
    }

    /** Properties of a SimpleEventMsgInProto. */
    interface ISimpleEventMsgInProto {

        /** SimpleEventMsgInProto type */
        type?: (commonProto.SimpleEventMsgInProto.SimpleEventTypeProto|null);
    }

    /** Represents a SimpleEventMsgInProto. */
    class SimpleEventMsgInProto implements ISimpleEventMsgInProto {

        /**
         * Constructs a new SimpleEventMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: commonProto.ISimpleEventMsgInProto);

        /** SimpleEventMsgInProto type. */
        public type: commonProto.SimpleEventMsgInProto.SimpleEventTypeProto;

        /**
         * Creates a new SimpleEventMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns SimpleEventMsgInProto instance
         */
        public static create(properties?: commonProto.ISimpleEventMsgInProto): commonProto.SimpleEventMsgInProto;

        /**
         * Encodes the specified SimpleEventMsgInProto message. Does not implicitly {@link commonProto.SimpleEventMsgInProto.verify|verify} messages.
         * @param message SimpleEventMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: commonProto.ISimpleEventMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a SimpleEventMsgInProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns SimpleEventMsgInProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): commonProto.SimpleEventMsgInProto;

        /**
         * Creates a SimpleEventMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns SimpleEventMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): commonProto.SimpleEventMsgInProto;

        /**
         * Creates a plain object from a SimpleEventMsgInProto message. Also converts values to other types if specified.
         * @param message SimpleEventMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: commonProto.SimpleEventMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this SimpleEventMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for SimpleEventMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace SimpleEventMsgInProto {

        /** SimpleEventTypeProto enum. */
        enum SimpleEventTypeProto {
            unload = 0,
            killSwing = 1,
            killSwingAdmin = 2,
            paintAck = 3,
            repaint = 4,
            downloadFile = 5,
            deleteFile = 6,
            cancelFileSelection = 7,
            requestComponentTree = 8,
            requestWindowSwitchList = 9,
            enableStatisticsLogging = 10,
            disableStatisticsLogging = 11,
            startRecording = 12,
            stopRecording = 13,
            startMirroring = 14,
            stopMirroring = 15
        }
    }

    /** Properties of a ConnectionHandshakeMsgInProto. */
    interface IConnectionHandshakeMsgInProto {

        /** ConnectionHandshakeMsgInProto instanceId */
        instanceId?: (string|null);

        /** ConnectionHandshakeMsgInProto viewId */
        viewId?: (string|null);

        /** ConnectionHandshakeMsgInProto browserId */
        browserId?: (string|null);

        /** ConnectionHandshakeMsgInProto desktopWidth */
        desktopWidth?: (number|null);

        /** ConnectionHandshakeMsgInProto desktopHeight */
        desktopHeight?: (number|null);

        /** ConnectionHandshakeMsgInProto applicationName */
        applicationName?: (string|null);

        /** ConnectionHandshakeMsgInProto mirrored */
        mirrored?: (boolean|null);

        /** ConnectionHandshakeMsgInProto directDrawSupported */
        directDrawSupported?: (boolean|null);

        /** ConnectionHandshakeMsgInProto documentBase */
        documentBase?: (string|null);

        /** ConnectionHandshakeMsgInProto params */
        params?: (commonProto.IParamMsgInProto[]|null);

        /** ConnectionHandshakeMsgInProto locale */
        locale?: (string|null);

        /** ConnectionHandshakeMsgInProto url */
        url?: (string|null);

        /** ConnectionHandshakeMsgInProto timeZone */
        timeZone?: (string|null);

        /** ConnectionHandshakeMsgInProto dockingSupported */
        dockingSupported?: (boolean|null);

        /** ConnectionHandshakeMsgInProto touchMode */
        touchMode?: (boolean|null);

        /** ConnectionHandshakeMsgInProto accessiblityEnabled */
        accessiblityEnabled?: (boolean|null);

        /** ConnectionHandshakeMsgInProto tabId */
        tabId?: (string|null);
    }

    /** Represents a ConnectionHandshakeMsgInProto. */
    class ConnectionHandshakeMsgInProto implements IConnectionHandshakeMsgInProto {

        /**
         * Constructs a new ConnectionHandshakeMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: commonProto.IConnectionHandshakeMsgInProto);

        /** ConnectionHandshakeMsgInProto instanceId. */
        public instanceId: string;

        /** ConnectionHandshakeMsgInProto viewId. */
        public viewId: string;

        /** ConnectionHandshakeMsgInProto browserId. */
        public browserId: string;

        /** ConnectionHandshakeMsgInProto desktopWidth. */
        public desktopWidth: number;

        /** ConnectionHandshakeMsgInProto desktopHeight. */
        public desktopHeight: number;

        /** ConnectionHandshakeMsgInProto applicationName. */
        public applicationName: string;

        /** ConnectionHandshakeMsgInProto mirrored. */
        public mirrored: boolean;

        /** ConnectionHandshakeMsgInProto directDrawSupported. */
        public directDrawSupported: boolean;

        /** ConnectionHandshakeMsgInProto documentBase. */
        public documentBase: string;

        /** ConnectionHandshakeMsgInProto params. */
        public params: commonProto.IParamMsgInProto[];

        /** ConnectionHandshakeMsgInProto locale. */
        public locale: string;

        /** ConnectionHandshakeMsgInProto url. */
        public url: string;

        /** ConnectionHandshakeMsgInProto timeZone. */
        public timeZone: string;

        /** ConnectionHandshakeMsgInProto dockingSupported. */
        public dockingSupported: boolean;

        /** ConnectionHandshakeMsgInProto touchMode. */
        public touchMode: boolean;

        /** ConnectionHandshakeMsgInProto accessiblityEnabled. */
        public accessiblityEnabled: boolean;

        /** ConnectionHandshakeMsgInProto tabId. */
        public tabId: string;

        /**
         * Creates a new ConnectionHandshakeMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ConnectionHandshakeMsgInProto instance
         */
        public static create(properties?: commonProto.IConnectionHandshakeMsgInProto): commonProto.ConnectionHandshakeMsgInProto;

        /**
         * Encodes the specified ConnectionHandshakeMsgInProto message. Does not implicitly {@link commonProto.ConnectionHandshakeMsgInProto.verify|verify} messages.
         * @param message ConnectionHandshakeMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: commonProto.IConnectionHandshakeMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ConnectionHandshakeMsgInProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ConnectionHandshakeMsgInProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): commonProto.ConnectionHandshakeMsgInProto;

        /**
         * Creates a ConnectionHandshakeMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ConnectionHandshakeMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): commonProto.ConnectionHandshakeMsgInProto;

        /**
         * Creates a plain object from a ConnectionHandshakeMsgInProto message. Also converts values to other types if specified.
         * @param message ConnectionHandshakeMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: commonProto.ConnectionHandshakeMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ConnectionHandshakeMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ConnectionHandshakeMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a TimestampsMsgInProto. */
    interface ITimestampsMsgInProto {

        /** TimestampsMsgInProto startTimestamp */
        startTimestamp?: (string|null);

        /** TimestampsMsgInProto sendTimestamp */
        sendTimestamp?: (string|null);

        /** TimestampsMsgInProto renderingTime */
        renderingTime?: (string|null);

        /** TimestampsMsgInProto ping */
        ping?: (number|null);
    }

    /** Represents a TimestampsMsgInProto. */
    class TimestampsMsgInProto implements ITimestampsMsgInProto {

        /**
         * Constructs a new TimestampsMsgInProto.
         * @param [properties] Properties to set
         */
        constructor(properties?: commonProto.ITimestampsMsgInProto);

        /** TimestampsMsgInProto startTimestamp. */
        public startTimestamp: string;

        /** TimestampsMsgInProto sendTimestamp. */
        public sendTimestamp: string;

        /** TimestampsMsgInProto renderingTime. */
        public renderingTime: string;

        /** TimestampsMsgInProto ping. */
        public ping: number;

        /**
         * Creates a new TimestampsMsgInProto instance using the specified properties.
         * @param [properties] Properties to set
         * @returns TimestampsMsgInProto instance
         */
        public static create(properties?: commonProto.ITimestampsMsgInProto): commonProto.TimestampsMsgInProto;

        /**
         * Encodes the specified TimestampsMsgInProto message. Does not implicitly {@link commonProto.TimestampsMsgInProto.verify|verify} messages.
         * @param message TimestampsMsgInProto message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: commonProto.ITimestampsMsgInProto, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a TimestampsMsgInProto message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns TimestampsMsgInProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): commonProto.TimestampsMsgInProto;

        /**
         * Creates a TimestampsMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns TimestampsMsgInProto
         */
        public static fromObject(object: { [k: string]: any }): commonProto.TimestampsMsgInProto;

        /**
         * Creates a plain object from a TimestampsMsgInProto message. Also converts values to other types if specified.
         * @param message TimestampsMsgInProto
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: commonProto.TimestampsMsgInProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this TimestampsMsgInProto to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for TimestampsMsgInProto
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}
