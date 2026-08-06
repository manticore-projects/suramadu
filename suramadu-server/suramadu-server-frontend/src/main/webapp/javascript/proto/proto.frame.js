/*eslint-disable block-scoped-var, id-length, no-control-regex, no-magic-numbers, no-prototype-builtins, no-redeclare, no-shadow, no-var, sort-vars*/
"use strict";

var $protobuf = require("protobufjs/minimal");

// Common aliases
var $Reader = $protobuf.Reader, $Writer = $protobuf.Writer, $util = $protobuf.util;

// Exported root namespace
var $root = $protobuf.roots["default"] || ($protobuf.roots["default"] = {});

$root.serverBrowserFrameProto = (function() {

    /**
     * Namespace serverBrowserFrameProto.
     * @exports serverBrowserFrameProto
     * @namespace
     */
    var serverBrowserFrameProto = {};

    serverBrowserFrameProto.BrowserToServerFrameMsgInProto = (function() {

        /**
         * Properties of a BrowserToServerFrameMsgInProto.
         * @memberof serverBrowserFrameProto
         * @interface IBrowserToServerFrameMsgInProto
         * @property {Uint8Array|null} [appFrameMsgIn] BrowserToServerFrameMsgInProto appFrameMsgIn
         * @property {commonProto.IConnectionHandshakeMsgInProto|null} [handshake] BrowserToServerFrameMsgInProto handshake
         * @property {Array.<commonProto.ITimestampsMsgInProto>|null} [timestamps] BrowserToServerFrameMsgInProto timestamps
         * @property {Array.<commonProto.ISimpleEventMsgInProto>|null} [events] BrowserToServerFrameMsgInProto events
         * @property {serverBrowserFrameProto.IPlaybackCommandMsgInProto|null} [playback] BrowserToServerFrameMsgInProto playback
         */

        /**
         * Constructs a new BrowserToServerFrameMsgInProto.
         * @memberof serverBrowserFrameProto
         * @classdesc Represents a BrowserToServerFrameMsgInProto.
         * @implements IBrowserToServerFrameMsgInProto
         * @constructor
         * @param {serverBrowserFrameProto.IBrowserToServerFrameMsgInProto=} [properties] Properties to set
         */
        function BrowserToServerFrameMsgInProto(properties) {
            this.timestamps = [];
            this.events = [];
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * BrowserToServerFrameMsgInProto appFrameMsgIn.
         * @member {Uint8Array} appFrameMsgIn
         * @memberof serverBrowserFrameProto.BrowserToServerFrameMsgInProto
         * @instance
         */
        BrowserToServerFrameMsgInProto.prototype.appFrameMsgIn = $util.newBuffer([]);

        /**
         * BrowserToServerFrameMsgInProto handshake.
         * @member {commonProto.IConnectionHandshakeMsgInProto|null|undefined} handshake
         * @memberof serverBrowserFrameProto.BrowserToServerFrameMsgInProto
         * @instance
         */
        BrowserToServerFrameMsgInProto.prototype.handshake = null;

        /**
         * BrowserToServerFrameMsgInProto timestamps.
         * @member {Array.<commonProto.ITimestampsMsgInProto>} timestamps
         * @memberof serverBrowserFrameProto.BrowserToServerFrameMsgInProto
         * @instance
         */
        BrowserToServerFrameMsgInProto.prototype.timestamps = $util.emptyArray;

        /**
         * BrowserToServerFrameMsgInProto events.
         * @member {Array.<commonProto.ISimpleEventMsgInProto>} events
         * @memberof serverBrowserFrameProto.BrowserToServerFrameMsgInProto
         * @instance
         */
        BrowserToServerFrameMsgInProto.prototype.events = $util.emptyArray;

        /**
         * BrowserToServerFrameMsgInProto playback.
         * @member {serverBrowserFrameProto.IPlaybackCommandMsgInProto|null|undefined} playback
         * @memberof serverBrowserFrameProto.BrowserToServerFrameMsgInProto
         * @instance
         */
        BrowserToServerFrameMsgInProto.prototype.playback = null;

        /**
         * Creates a new BrowserToServerFrameMsgInProto instance using the specified properties.
         * @function create
         * @memberof serverBrowserFrameProto.BrowserToServerFrameMsgInProto
         * @static
         * @param {serverBrowserFrameProto.IBrowserToServerFrameMsgInProto=} [properties] Properties to set
         * @returns {serverBrowserFrameProto.BrowserToServerFrameMsgInProto} BrowserToServerFrameMsgInProto instance
         */
        BrowserToServerFrameMsgInProto.create = function create(properties) {
            return new BrowserToServerFrameMsgInProto(properties);
        };

        /**
         * Encodes the specified BrowserToServerFrameMsgInProto message. Does not implicitly {@link serverBrowserFrameProto.BrowserToServerFrameMsgInProto.verify|verify} messages.
         * @function encode
         * @memberof serverBrowserFrameProto.BrowserToServerFrameMsgInProto
         * @static
         * @param {serverBrowserFrameProto.IBrowserToServerFrameMsgInProto} message BrowserToServerFrameMsgInProto message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        BrowserToServerFrameMsgInProto.encode = function encode(message, writer, q) {
            if (!writer)
                writer = $Writer.create();
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            if (message.appFrameMsgIn != null && Object.hasOwnProperty.call(message, "appFrameMsgIn"))
                writer.uint32(/* id 1, wireType 2 =*/10).bytes(message.appFrameMsgIn);
            if (message.handshake != null && Object.hasOwnProperty.call(message, "handshake"))
                $root.commonProto.ConnectionHandshakeMsgInProto.encode(message.handshake, writer.uint32(/* id 2, wireType 2 =*/18).fork(), q + 1).ldelim();
            if (message.timestamps != null && message.timestamps.length)
                for (var i = 0; i < message.timestamps.length; ++i)
                    $root.commonProto.TimestampsMsgInProto.encode(message.timestamps[i], writer.uint32(/* id 3, wireType 2 =*/26).fork(), q + 1).ldelim();
            if (message.events != null && message.events.length)
                for (var i = 0; i < message.events.length; ++i)
                    $root.commonProto.SimpleEventMsgInProto.encode(message.events[i], writer.uint32(/* id 4, wireType 2 =*/34).fork(), q + 1).ldelim();
            if (message.playback != null && Object.hasOwnProperty.call(message, "playback"))
                $root.serverBrowserFrameProto.PlaybackCommandMsgInProto.encode(message.playback, writer.uint32(/* id 5, wireType 2 =*/42).fork(), q + 1).ldelim();
            return writer;
        };

        /**
         * Decodes a BrowserToServerFrameMsgInProto message from the specified reader or buffer.
         * @function decode
         * @memberof serverBrowserFrameProto.BrowserToServerFrameMsgInProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {serverBrowserFrameProto.BrowserToServerFrameMsgInProto} BrowserToServerFrameMsgInProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        BrowserToServerFrameMsgInProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.serverBrowserFrameProto.BrowserToServerFrameMsgInProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.appFrameMsgIn = reader.bytes();
                        break;
                    }
                case 2: {
                        message.handshake = $root.commonProto.ConnectionHandshakeMsgInProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 3: {
                        if (!(message.timestamps && message.timestamps.length))
                            message.timestamps = [];
                        message.timestamps.push($root.commonProto.TimestampsMsgInProto.decode(reader, reader.uint32(), undefined, long + 1));
                        break;
                    }
                case 4: {
                        if (!(message.events && message.events.length))
                            message.events = [];
                        message.events.push($root.commonProto.SimpleEventMsgInProto.decode(reader, reader.uint32(), undefined, long + 1));
                        break;
                    }
                case 5: {
                        message.playback = $root.serverBrowserFrameProto.PlaybackCommandMsgInProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                default:
                    reader.skipType(tag & 7, long);
                    break;
                }
            }
            return message;
        };

        /**
         * Creates a BrowserToServerFrameMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof serverBrowserFrameProto.BrowserToServerFrameMsgInProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {serverBrowserFrameProto.BrowserToServerFrameMsgInProto} BrowserToServerFrameMsgInProto
         */
        BrowserToServerFrameMsgInProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.serverBrowserFrameProto.BrowserToServerFrameMsgInProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".serverBrowserFrameProto.BrowserToServerFrameMsgInProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.serverBrowserFrameProto.BrowserToServerFrameMsgInProto();
            if (object.appFrameMsgIn != null)
                if (typeof object.appFrameMsgIn === "string")
                    $util.base64.decode(object.appFrameMsgIn, message.appFrameMsgIn = $util.newBuffer($util.base64.length(object.appFrameMsgIn)), 0);
                else if (object.appFrameMsgIn.length >= 0)
                    message.appFrameMsgIn = object.appFrameMsgIn;
            if (object.handshake != null) {
                if (!$util.isObject(object.handshake))
                    throw TypeError(".serverBrowserFrameProto.BrowserToServerFrameMsgInProto.handshake: object expected");
                message.handshake = $root.commonProto.ConnectionHandshakeMsgInProto.fromObject(object.handshake, long + 1);
            }
            if (object.timestamps) {
                if (!Array.isArray(object.timestamps))
                    throw TypeError(".serverBrowserFrameProto.BrowserToServerFrameMsgInProto.timestamps: array expected");
                message.timestamps = [];
                for (var i = 0; i < object.timestamps.length; ++i) {
                    if (!$util.isObject(object.timestamps[i]))
                        throw TypeError(".serverBrowserFrameProto.BrowserToServerFrameMsgInProto.timestamps: object expected");
                    message.timestamps[i] = $root.commonProto.TimestampsMsgInProto.fromObject(object.timestamps[i], long + 1);
                }
            }
            if (object.events) {
                if (!Array.isArray(object.events))
                    throw TypeError(".serverBrowserFrameProto.BrowserToServerFrameMsgInProto.events: array expected");
                message.events = [];
                for (var i = 0; i < object.events.length; ++i) {
                    if (!$util.isObject(object.events[i]))
                        throw TypeError(".serverBrowserFrameProto.BrowserToServerFrameMsgInProto.events: object expected");
                    message.events[i] = $root.commonProto.SimpleEventMsgInProto.fromObject(object.events[i], long + 1);
                }
            }
            if (object.playback != null) {
                if (!$util.isObject(object.playback))
                    throw TypeError(".serverBrowserFrameProto.BrowserToServerFrameMsgInProto.playback: object expected");
                message.playback = $root.serverBrowserFrameProto.PlaybackCommandMsgInProto.fromObject(object.playback, long + 1);
            }
            return message;
        };

        /**
         * Creates a plain object from a BrowserToServerFrameMsgInProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof serverBrowserFrameProto.BrowserToServerFrameMsgInProto
         * @static
         * @param {serverBrowserFrameProto.BrowserToServerFrameMsgInProto} message BrowserToServerFrameMsgInProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        BrowserToServerFrameMsgInProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.arrays || options.defaults) {
                object.timestamps = [];
                object.events = [];
            }
            if (options.defaults) {
                if (options.bytes === String)
                    object.appFrameMsgIn = "";
                else {
                    object.appFrameMsgIn = [];
                    if (options.bytes !== Array)
                        object.appFrameMsgIn = $util.newBuffer(object.appFrameMsgIn);
                }
                object.handshake = null;
                object.playback = null;
            }
            if (message.appFrameMsgIn != null && Object.hasOwnProperty.call(message, "appFrameMsgIn"))
                object.appFrameMsgIn = options.bytes === String ? $util.base64.encode(message.appFrameMsgIn, 0, message.appFrameMsgIn.length) : options.bytes === Array ? Array.prototype.slice.call(message.appFrameMsgIn) : message.appFrameMsgIn;
            if (message.handshake != null && Object.hasOwnProperty.call(message, "handshake"))
                object.handshake = $root.commonProto.ConnectionHandshakeMsgInProto.toObject(message.handshake, options, q + 1);
            if (message.timestamps && message.timestamps.length) {
                object.timestamps = [];
                for (var j = 0; j < message.timestamps.length; ++j)
                    object.timestamps[j] = $root.commonProto.TimestampsMsgInProto.toObject(message.timestamps[j], options, q + 1);
            }
            if (message.events && message.events.length) {
                object.events = [];
                for (var j = 0; j < message.events.length; ++j)
                    object.events[j] = $root.commonProto.SimpleEventMsgInProto.toObject(message.events[j], options, q + 1);
            }
            if (message.playback != null && Object.hasOwnProperty.call(message, "playback"))
                object.playback = $root.serverBrowserFrameProto.PlaybackCommandMsgInProto.toObject(message.playback, options, q + 1);
            return object;
        };

        /**
         * Converts this BrowserToServerFrameMsgInProto to JSON.
         * @function toJSON
         * @memberof serverBrowserFrameProto.BrowserToServerFrameMsgInProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        BrowserToServerFrameMsgInProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for BrowserToServerFrameMsgInProto
         * @function getTypeUrl
         * @memberof serverBrowserFrameProto.BrowserToServerFrameMsgInProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        BrowserToServerFrameMsgInProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/serverBrowserFrameProto.BrowserToServerFrameMsgInProto";
        };

        return BrowserToServerFrameMsgInProto;
    })();

    serverBrowserFrameProto.PlaybackCommandMsgInProto = (function() {

        /**
         * Properties of a PlaybackCommandMsgInProto.
         * @memberof serverBrowserFrameProto
         * @interface IPlaybackCommandMsgInProto
         * @property {serverBrowserFrameProto.PlaybackCommandMsgInProto.PlaybackCommandProto|null} [command] PlaybackCommandMsgInProto command
         */

        /**
         * Constructs a new PlaybackCommandMsgInProto.
         * @memberof serverBrowserFrameProto
         * @classdesc Represents a PlaybackCommandMsgInProto.
         * @implements IPlaybackCommandMsgInProto
         * @constructor
         * @param {serverBrowserFrameProto.IPlaybackCommandMsgInProto=} [properties] Properties to set
         */
        function PlaybackCommandMsgInProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * PlaybackCommandMsgInProto command.
         * @member {serverBrowserFrameProto.PlaybackCommandMsgInProto.PlaybackCommandProto} command
         * @memberof serverBrowserFrameProto.PlaybackCommandMsgInProto
         * @instance
         */
        PlaybackCommandMsgInProto.prototype.command = 0;

        /**
         * Creates a new PlaybackCommandMsgInProto instance using the specified properties.
         * @function create
         * @memberof serverBrowserFrameProto.PlaybackCommandMsgInProto
         * @static
         * @param {serverBrowserFrameProto.IPlaybackCommandMsgInProto=} [properties] Properties to set
         * @returns {serverBrowserFrameProto.PlaybackCommandMsgInProto} PlaybackCommandMsgInProto instance
         */
        PlaybackCommandMsgInProto.create = function create(properties) {
            return new PlaybackCommandMsgInProto(properties);
        };

        /**
         * Encodes the specified PlaybackCommandMsgInProto message. Does not implicitly {@link serverBrowserFrameProto.PlaybackCommandMsgInProto.verify|verify} messages.
         * @function encode
         * @memberof serverBrowserFrameProto.PlaybackCommandMsgInProto
         * @static
         * @param {serverBrowserFrameProto.IPlaybackCommandMsgInProto} message PlaybackCommandMsgInProto message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        PlaybackCommandMsgInProto.encode = function encode(message, writer, q) {
            if (!writer)
                writer = $Writer.create();
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            if (message.command != null && Object.hasOwnProperty.call(message, "command"))
                writer.uint32(/* id 1, wireType 0 =*/8).int32(message.command);
            return writer;
        };

        /**
         * Decodes a PlaybackCommandMsgInProto message from the specified reader or buffer.
         * @function decode
         * @memberof serverBrowserFrameProto.PlaybackCommandMsgInProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {serverBrowserFrameProto.PlaybackCommandMsgInProto} PlaybackCommandMsgInProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        PlaybackCommandMsgInProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.serverBrowserFrameProto.PlaybackCommandMsgInProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.command = reader.int32();
                        break;
                    }
                default:
                    reader.skipType(tag & 7, long);
                    break;
                }
            }
            return message;
        };

        /**
         * Creates a PlaybackCommandMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof serverBrowserFrameProto.PlaybackCommandMsgInProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {serverBrowserFrameProto.PlaybackCommandMsgInProto} PlaybackCommandMsgInProto
         */
        PlaybackCommandMsgInProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.serverBrowserFrameProto.PlaybackCommandMsgInProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".serverBrowserFrameProto.PlaybackCommandMsgInProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.serverBrowserFrameProto.PlaybackCommandMsgInProto();
            switch (object.command) {
            default:
                if (typeof object.command === "number") {
                    message.command = object.command;
                    break;
                }
                break;
            case "reset":
            case 0:
                message.command = 0;
                break;
            case "play":
            case 1:
                message.command = 1;
                break;
            case "stop":
            case 2:
                message.command = 2;
                break;
            case "step":
            case 3:
                message.command = 3;
                break;
            case "step10":
            case 4:
                message.command = 4;
                break;
            case "step100":
            case 5:
                message.command = 5;
                break;
            }
            return message;
        };

        /**
         * Creates a plain object from a PlaybackCommandMsgInProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof serverBrowserFrameProto.PlaybackCommandMsgInProto
         * @static
         * @param {serverBrowserFrameProto.PlaybackCommandMsgInProto} message PlaybackCommandMsgInProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        PlaybackCommandMsgInProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults)
                object.command = options.enums === String ? "reset" : 0;
            if (message.command != null && Object.hasOwnProperty.call(message, "command"))
                object.command = options.enums === String ? $root.serverBrowserFrameProto.PlaybackCommandMsgInProto.PlaybackCommandProto[message.command] === undefined ? message.command : $root.serverBrowserFrameProto.PlaybackCommandMsgInProto.PlaybackCommandProto[message.command] : message.command;
            return object;
        };

        /**
         * Converts this PlaybackCommandMsgInProto to JSON.
         * @function toJSON
         * @memberof serverBrowserFrameProto.PlaybackCommandMsgInProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        PlaybackCommandMsgInProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for PlaybackCommandMsgInProto
         * @function getTypeUrl
         * @memberof serverBrowserFrameProto.PlaybackCommandMsgInProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        PlaybackCommandMsgInProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/serverBrowserFrameProto.PlaybackCommandMsgInProto";
        };

        /**
         * PlaybackCommandProto enum.
         * @name serverBrowserFrameProto.PlaybackCommandMsgInProto.PlaybackCommandProto
         * @enum {number}
         * @property {number} reset=0 reset value
         * @property {number} play=1 play value
         * @property {number} stop=2 stop value
         * @property {number} step=3 step value
         * @property {number} step10=4 step10 value
         * @property {number} step100=5 step100 value
         */
        PlaybackCommandMsgInProto.PlaybackCommandProto = (function() {
            var valuesById = {}, values = Object.create(valuesById);
            values[valuesById[0] = "reset"] = 0;
            values[valuesById[1] = "play"] = 1;
            values[valuesById[2] = "stop"] = 2;
            values[valuesById[3] = "step"] = 3;
            values[valuesById[4] = "step10"] = 4;
            values[valuesById[5] = "step100"] = 5;
            return values;
        })();

        return PlaybackCommandMsgInProto;
    })();

    serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto = (function() {

        /**
         * Properties of a ServerToBrowserFrameMsgOutProto.
         * @memberof serverBrowserFrameProto
         * @interface IServerToBrowserFrameMsgOutProto
         * @property {Uint8Array|null} [appFrameMsgOut] ServerToBrowserFrameMsgOutProto appFrameMsgOut
         * @property {serverBrowserFrameProto.IConnectionInfoMsgOutProto|null} [connectionInfo] ServerToBrowserFrameMsgOutProto connectionInfo
         */

        /**
         * Constructs a new ServerToBrowserFrameMsgOutProto.
         * @memberof serverBrowserFrameProto
         * @classdesc Represents a ServerToBrowserFrameMsgOutProto.
         * @implements IServerToBrowserFrameMsgOutProto
         * @constructor
         * @param {serverBrowserFrameProto.IServerToBrowserFrameMsgOutProto=} [properties] Properties to set
         */
        function ServerToBrowserFrameMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * ServerToBrowserFrameMsgOutProto appFrameMsgOut.
         * @member {Uint8Array} appFrameMsgOut
         * @memberof serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto
         * @instance
         */
        ServerToBrowserFrameMsgOutProto.prototype.appFrameMsgOut = $util.newBuffer([]);

        /**
         * ServerToBrowserFrameMsgOutProto connectionInfo.
         * @member {serverBrowserFrameProto.IConnectionInfoMsgOutProto|null|undefined} connectionInfo
         * @memberof serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto
         * @instance
         */
        ServerToBrowserFrameMsgOutProto.prototype.connectionInfo = null;

        /**
         * Creates a new ServerToBrowserFrameMsgOutProto instance using the specified properties.
         * @function create
         * @memberof serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto
         * @static
         * @param {serverBrowserFrameProto.IServerToBrowserFrameMsgOutProto=} [properties] Properties to set
         * @returns {serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto} ServerToBrowserFrameMsgOutProto instance
         */
        ServerToBrowserFrameMsgOutProto.create = function create(properties) {
            return new ServerToBrowserFrameMsgOutProto(properties);
        };

        /**
         * Encodes the specified ServerToBrowserFrameMsgOutProto message. Does not implicitly {@link serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto.verify|verify} messages.
         * @function encode
         * @memberof serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto
         * @static
         * @param {serverBrowserFrameProto.IServerToBrowserFrameMsgOutProto} message ServerToBrowserFrameMsgOutProto message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        ServerToBrowserFrameMsgOutProto.encode = function encode(message, writer, q) {
            if (!writer)
                writer = $Writer.create();
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            if (message.appFrameMsgOut != null && Object.hasOwnProperty.call(message, "appFrameMsgOut"))
                writer.uint32(/* id 1, wireType 2 =*/10).bytes(message.appFrameMsgOut);
            if (message.connectionInfo != null && Object.hasOwnProperty.call(message, "connectionInfo"))
                $root.serverBrowserFrameProto.ConnectionInfoMsgOutProto.encode(message.connectionInfo, writer.uint32(/* id 2, wireType 2 =*/18).fork(), q + 1).ldelim();
            return writer;
        };

        /**
         * Decodes a ServerToBrowserFrameMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto} ServerToBrowserFrameMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        ServerToBrowserFrameMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.appFrameMsgOut = reader.bytes();
                        break;
                    }
                case 2: {
                        message.connectionInfo = $root.serverBrowserFrameProto.ConnectionInfoMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                default:
                    reader.skipType(tag & 7, long);
                    break;
                }
            }
            return message;
        };

        /**
         * Creates a ServerToBrowserFrameMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto} ServerToBrowserFrameMsgOutProto
         */
        ServerToBrowserFrameMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto();
            if (object.appFrameMsgOut != null)
                if (typeof object.appFrameMsgOut === "string")
                    $util.base64.decode(object.appFrameMsgOut, message.appFrameMsgOut = $util.newBuffer($util.base64.length(object.appFrameMsgOut)), 0);
                else if (object.appFrameMsgOut.length >= 0)
                    message.appFrameMsgOut = object.appFrameMsgOut;
            if (object.connectionInfo != null) {
                if (!$util.isObject(object.connectionInfo))
                    throw TypeError(".serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto.connectionInfo: object expected");
                message.connectionInfo = $root.serverBrowserFrameProto.ConnectionInfoMsgOutProto.fromObject(object.connectionInfo, long + 1);
            }
            return message;
        };

        /**
         * Creates a plain object from a ServerToBrowserFrameMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto
         * @static
         * @param {serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto} message ServerToBrowserFrameMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        ServerToBrowserFrameMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                if (options.bytes === String)
                    object.appFrameMsgOut = "";
                else {
                    object.appFrameMsgOut = [];
                    if (options.bytes !== Array)
                        object.appFrameMsgOut = $util.newBuffer(object.appFrameMsgOut);
                }
                object.connectionInfo = null;
            }
            if (message.appFrameMsgOut != null && Object.hasOwnProperty.call(message, "appFrameMsgOut"))
                object.appFrameMsgOut = options.bytes === String ? $util.base64.encode(message.appFrameMsgOut, 0, message.appFrameMsgOut.length) : options.bytes === Array ? Array.prototype.slice.call(message.appFrameMsgOut) : message.appFrameMsgOut;
            if (message.connectionInfo != null && Object.hasOwnProperty.call(message, "connectionInfo"))
                object.connectionInfo = $root.serverBrowserFrameProto.ConnectionInfoMsgOutProto.toObject(message.connectionInfo, options, q + 1);
            return object;
        };

        /**
         * Converts this ServerToBrowserFrameMsgOutProto to JSON.
         * @function toJSON
         * @memberof serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        ServerToBrowserFrameMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for ServerToBrowserFrameMsgOutProto
         * @function getTypeUrl
         * @memberof serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        ServerToBrowserFrameMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/serverBrowserFrameProto.ServerToBrowserFrameMsgOutProto";
        };

        return ServerToBrowserFrameMsgOutProto;
    })();

    serverBrowserFrameProto.ConnectionInfoMsgOutProto = (function() {

        /**
         * Properties of a ConnectionInfoMsgOutProto.
         * @memberof serverBrowserFrameProto
         * @interface IConnectionInfoMsgOutProto
         * @property {string|null} [serverId] ConnectionInfoMsgOutProto serverId
         * @property {string|null} [sessionPoolId] ConnectionInfoMsgOutProto sessionPoolId
         * @property {boolean|null} [autoLogout] ConnectionInfoMsgOutProto autoLogout
         */

        /**
         * Constructs a new ConnectionInfoMsgOutProto.
         * @memberof serverBrowserFrameProto
         * @classdesc Represents a ConnectionInfoMsgOutProto.
         * @implements IConnectionInfoMsgOutProto
         * @constructor
         * @param {serverBrowserFrameProto.IConnectionInfoMsgOutProto=} [properties] Properties to set
         */
        function ConnectionInfoMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * ConnectionInfoMsgOutProto serverId.
         * @member {string} serverId
         * @memberof serverBrowserFrameProto.ConnectionInfoMsgOutProto
         * @instance
         */
        ConnectionInfoMsgOutProto.prototype.serverId = "";

        /**
         * ConnectionInfoMsgOutProto sessionPoolId.
         * @member {string} sessionPoolId
         * @memberof serverBrowserFrameProto.ConnectionInfoMsgOutProto
         * @instance
         */
        ConnectionInfoMsgOutProto.prototype.sessionPoolId = "";

        /**
         * ConnectionInfoMsgOutProto autoLogout.
         * @member {boolean} autoLogout
         * @memberof serverBrowserFrameProto.ConnectionInfoMsgOutProto
         * @instance
         */
        ConnectionInfoMsgOutProto.prototype.autoLogout = false;

        /**
         * Creates a new ConnectionInfoMsgOutProto instance using the specified properties.
         * @function create
         * @memberof serverBrowserFrameProto.ConnectionInfoMsgOutProto
         * @static
         * @param {serverBrowserFrameProto.IConnectionInfoMsgOutProto=} [properties] Properties to set
         * @returns {serverBrowserFrameProto.ConnectionInfoMsgOutProto} ConnectionInfoMsgOutProto instance
         */
        ConnectionInfoMsgOutProto.create = function create(properties) {
            return new ConnectionInfoMsgOutProto(properties);
        };

        /**
         * Encodes the specified ConnectionInfoMsgOutProto message. Does not implicitly {@link serverBrowserFrameProto.ConnectionInfoMsgOutProto.verify|verify} messages.
         * @function encode
         * @memberof serverBrowserFrameProto.ConnectionInfoMsgOutProto
         * @static
         * @param {serverBrowserFrameProto.IConnectionInfoMsgOutProto} message ConnectionInfoMsgOutProto message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        ConnectionInfoMsgOutProto.encode = function encode(message, writer, q) {
            if (!writer)
                writer = $Writer.create();
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            if (message.serverId != null && Object.hasOwnProperty.call(message, "serverId"))
                writer.uint32(/* id 1, wireType 2 =*/10).string(message.serverId);
            if (message.sessionPoolId != null && Object.hasOwnProperty.call(message, "sessionPoolId"))
                writer.uint32(/* id 2, wireType 2 =*/18).string(message.sessionPoolId);
            if (message.autoLogout != null && Object.hasOwnProperty.call(message, "autoLogout"))
                writer.uint32(/* id 3, wireType 0 =*/24).bool(message.autoLogout);
            return writer;
        };

        /**
         * Decodes a ConnectionInfoMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof serverBrowserFrameProto.ConnectionInfoMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {serverBrowserFrameProto.ConnectionInfoMsgOutProto} ConnectionInfoMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        ConnectionInfoMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.serverBrowserFrameProto.ConnectionInfoMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.serverId = reader.string();
                        break;
                    }
                case 2: {
                        message.sessionPoolId = reader.string();
                        break;
                    }
                case 3: {
                        message.autoLogout = reader.bool();
                        break;
                    }
                default:
                    reader.skipType(tag & 7, long);
                    break;
                }
            }
            return message;
        };

        /**
         * Creates a ConnectionInfoMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof serverBrowserFrameProto.ConnectionInfoMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {serverBrowserFrameProto.ConnectionInfoMsgOutProto} ConnectionInfoMsgOutProto
         */
        ConnectionInfoMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.serverBrowserFrameProto.ConnectionInfoMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".serverBrowserFrameProto.ConnectionInfoMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.serverBrowserFrameProto.ConnectionInfoMsgOutProto();
            if (object.serverId != null)
                message.serverId = String(object.serverId);
            if (object.sessionPoolId != null)
                message.sessionPoolId = String(object.sessionPoolId);
            if (object.autoLogout != null)
                message.autoLogout = Boolean(object.autoLogout);
            return message;
        };

        /**
         * Creates a plain object from a ConnectionInfoMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof serverBrowserFrameProto.ConnectionInfoMsgOutProto
         * @static
         * @param {serverBrowserFrameProto.ConnectionInfoMsgOutProto} message ConnectionInfoMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        ConnectionInfoMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.serverId = "";
                object.sessionPoolId = "";
                object.autoLogout = false;
            }
            if (message.serverId != null && Object.hasOwnProperty.call(message, "serverId"))
                object.serverId = message.serverId;
            if (message.sessionPoolId != null && Object.hasOwnProperty.call(message, "sessionPoolId"))
                object.sessionPoolId = message.sessionPoolId;
            if (message.autoLogout != null && Object.hasOwnProperty.call(message, "autoLogout"))
                object.autoLogout = message.autoLogout;
            return object;
        };

        /**
         * Converts this ConnectionInfoMsgOutProto to JSON.
         * @function toJSON
         * @memberof serverBrowserFrameProto.ConnectionInfoMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        ConnectionInfoMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for ConnectionInfoMsgOutProto
         * @function getTypeUrl
         * @memberof serverBrowserFrameProto.ConnectionInfoMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        ConnectionInfoMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/serverBrowserFrameProto.ConnectionInfoMsgOutProto";
        };

        return ConnectionInfoMsgOutProto;
    })();

    return serverBrowserFrameProto;
})();

$root.commonProto = (function() {

    /**
     * Namespace commonProto.
     * @exports commonProto
     * @namespace
     */
    var commonProto = {};

    commonProto.ParamMsgInProto = (function() {

        /**
         * Properties of a ParamMsgInProto.
         * @memberof commonProto
         * @interface IParamMsgInProto
         * @property {string|null} [name] ParamMsgInProto name
         * @property {string|null} [value] ParamMsgInProto value
         */

        /**
         * Constructs a new ParamMsgInProto.
         * @memberof commonProto
         * @classdesc Represents a ParamMsgInProto.
         * @implements IParamMsgInProto
         * @constructor
         * @param {commonProto.IParamMsgInProto=} [properties] Properties to set
         */
        function ParamMsgInProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * ParamMsgInProto name.
         * @member {string} name
         * @memberof commonProto.ParamMsgInProto
         * @instance
         */
        ParamMsgInProto.prototype.name = "";

        /**
         * ParamMsgInProto value.
         * @member {string} value
         * @memberof commonProto.ParamMsgInProto
         * @instance
         */
        ParamMsgInProto.prototype.value = "";

        /**
         * Creates a new ParamMsgInProto instance using the specified properties.
         * @function create
         * @memberof commonProto.ParamMsgInProto
         * @static
         * @param {commonProto.IParamMsgInProto=} [properties] Properties to set
         * @returns {commonProto.ParamMsgInProto} ParamMsgInProto instance
         */
        ParamMsgInProto.create = function create(properties) {
            return new ParamMsgInProto(properties);
        };

        /**
         * Encodes the specified ParamMsgInProto message. Does not implicitly {@link commonProto.ParamMsgInProto.verify|verify} messages.
         * @function encode
         * @memberof commonProto.ParamMsgInProto
         * @static
         * @param {commonProto.IParamMsgInProto} message ParamMsgInProto message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        ParamMsgInProto.encode = function encode(message, writer, q) {
            if (!writer)
                writer = $Writer.create();
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            if (message.name != null && Object.hasOwnProperty.call(message, "name"))
                writer.uint32(/* id 1, wireType 2 =*/10).string(message.name);
            if (message.value != null && Object.hasOwnProperty.call(message, "value"))
                writer.uint32(/* id 2, wireType 2 =*/18).string(message.value);
            return writer;
        };

        /**
         * Decodes a ParamMsgInProto message from the specified reader or buffer.
         * @function decode
         * @memberof commonProto.ParamMsgInProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {commonProto.ParamMsgInProto} ParamMsgInProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        ParamMsgInProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.commonProto.ParamMsgInProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.name = reader.string();
                        break;
                    }
                case 2: {
                        message.value = reader.string();
                        break;
                    }
                default:
                    reader.skipType(tag & 7, long);
                    break;
                }
            }
            return message;
        };

        /**
         * Creates a ParamMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof commonProto.ParamMsgInProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {commonProto.ParamMsgInProto} ParamMsgInProto
         */
        ParamMsgInProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.commonProto.ParamMsgInProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".commonProto.ParamMsgInProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.commonProto.ParamMsgInProto();
            if (object.name != null)
                message.name = String(object.name);
            if (object.value != null)
                message.value = String(object.value);
            return message;
        };

        /**
         * Creates a plain object from a ParamMsgInProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof commonProto.ParamMsgInProto
         * @static
         * @param {commonProto.ParamMsgInProto} message ParamMsgInProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        ParamMsgInProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.name = "";
                object.value = "";
            }
            if (message.name != null && Object.hasOwnProperty.call(message, "name"))
                object.name = message.name;
            if (message.value != null && Object.hasOwnProperty.call(message, "value"))
                object.value = message.value;
            return object;
        };

        /**
         * Converts this ParamMsgInProto to JSON.
         * @function toJSON
         * @memberof commonProto.ParamMsgInProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        ParamMsgInProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for ParamMsgInProto
         * @function getTypeUrl
         * @memberof commonProto.ParamMsgInProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        ParamMsgInProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/commonProto.ParamMsgInProto";
        };

        return ParamMsgInProto;
    })();

    /**
     * RecordingStatusEnumProto enum.
     * @name commonProto.RecordingStatusEnumProto
     * @enum {number}
     * @property {number} NOT_RECORDING=1 NOT_RECORDING value
     * @property {number} WAITING_FOR_RECORDING_APPROVAL=2 WAITING_FOR_RECORDING_APPROVAL value
     * @property {number} DENIED_RECORDING_BY_USER=3 DENIED_RECORDING_BY_USER value
     * @property {number} RECORDING=4 RECORDING value
     */
    commonProto.RecordingStatusEnumProto = (function() {
        var valuesById = {}, values = Object.create(valuesById);
        values[valuesById[1] = "NOT_RECORDING"] = 1;
        values[valuesById[2] = "WAITING_FOR_RECORDING_APPROVAL"] = 2;
        values[valuesById[3] = "DENIED_RECORDING_BY_USER"] = 3;
        values[valuesById[4] = "RECORDING"] = 4;
        return values;
    })();

    /**
     * MirroringStatusEnumProto enum.
     * @name commonProto.MirroringStatusEnumProto
     * @enum {number}
     * @property {number} NOT_MIRRORING=1 NOT_MIRRORING value
     * @property {number} WAITING_FOR_MIRRORING_APPROVAL=2 WAITING_FOR_MIRRORING_APPROVAL value
     * @property {number} DENIED_MIRRORING_BY_USER=3 DENIED_MIRRORING_BY_USER value
     * @property {number} MIRRORING=4 MIRRORING value
     */
    commonProto.MirroringStatusEnumProto = (function() {
        var valuesById = {}, values = Object.create(valuesById);
        values[valuesById[1] = "NOT_MIRRORING"] = 1;
        values[valuesById[2] = "WAITING_FOR_MIRRORING_APPROVAL"] = 2;
        values[valuesById[3] = "DENIED_MIRRORING_BY_USER"] = 3;
        values[valuesById[4] = "MIRRORING"] = 4;
        return values;
    })();

    commonProto.SimpleEventMsgInProto = (function() {

        /**
         * Properties of a SimpleEventMsgInProto.
         * @memberof commonProto
         * @interface ISimpleEventMsgInProto
         * @property {commonProto.SimpleEventMsgInProto.SimpleEventTypeProto|null} [type] SimpleEventMsgInProto type
         */

        /**
         * Constructs a new SimpleEventMsgInProto.
         * @memberof commonProto
         * @classdesc Represents a SimpleEventMsgInProto.
         * @implements ISimpleEventMsgInProto
         * @constructor
         * @param {commonProto.ISimpleEventMsgInProto=} [properties] Properties to set
         */
        function SimpleEventMsgInProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * SimpleEventMsgInProto type.
         * @member {commonProto.SimpleEventMsgInProto.SimpleEventTypeProto} type
         * @memberof commonProto.SimpleEventMsgInProto
         * @instance
         */
        SimpleEventMsgInProto.prototype.type = 0;

        /**
         * Creates a new SimpleEventMsgInProto instance using the specified properties.
         * @function create
         * @memberof commonProto.SimpleEventMsgInProto
         * @static
         * @param {commonProto.ISimpleEventMsgInProto=} [properties] Properties to set
         * @returns {commonProto.SimpleEventMsgInProto} SimpleEventMsgInProto instance
         */
        SimpleEventMsgInProto.create = function create(properties) {
            return new SimpleEventMsgInProto(properties);
        };

        /**
         * Encodes the specified SimpleEventMsgInProto message. Does not implicitly {@link commonProto.SimpleEventMsgInProto.verify|verify} messages.
         * @function encode
         * @memberof commonProto.SimpleEventMsgInProto
         * @static
         * @param {commonProto.ISimpleEventMsgInProto} message SimpleEventMsgInProto message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        SimpleEventMsgInProto.encode = function encode(message, writer, q) {
            if (!writer)
                writer = $Writer.create();
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            if (message.type != null && Object.hasOwnProperty.call(message, "type"))
                writer.uint32(/* id 1, wireType 0 =*/8).int32(message.type);
            return writer;
        };

        /**
         * Decodes a SimpleEventMsgInProto message from the specified reader or buffer.
         * @function decode
         * @memberof commonProto.SimpleEventMsgInProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {commonProto.SimpleEventMsgInProto} SimpleEventMsgInProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        SimpleEventMsgInProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.commonProto.SimpleEventMsgInProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.type = reader.int32();
                        break;
                    }
                default:
                    reader.skipType(tag & 7, long);
                    break;
                }
            }
            return message;
        };

        /**
         * Creates a SimpleEventMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof commonProto.SimpleEventMsgInProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {commonProto.SimpleEventMsgInProto} SimpleEventMsgInProto
         */
        SimpleEventMsgInProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.commonProto.SimpleEventMsgInProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".commonProto.SimpleEventMsgInProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.commonProto.SimpleEventMsgInProto();
            switch (object.type) {
            default:
                if (typeof object.type === "number") {
                    message.type = object.type;
                    break;
                }
                break;
            case "unload":
            case 0:
                message.type = 0;
                break;
            case "killSwing":
            case 1:
                message.type = 1;
                break;
            case "killSwingAdmin":
            case 2:
                message.type = 2;
                break;
            case "paintAck":
            case 3:
                message.type = 3;
                break;
            case "repaint":
            case 4:
                message.type = 4;
                break;
            case "downloadFile":
            case 5:
                message.type = 5;
                break;
            case "deleteFile":
            case 6:
                message.type = 6;
                break;
            case "cancelFileSelection":
            case 7:
                message.type = 7;
                break;
            case "requestComponentTree":
            case 8:
                message.type = 8;
                break;
            case "requestWindowSwitchList":
            case 9:
                message.type = 9;
                break;
            case "enableStatisticsLogging":
            case 10:
                message.type = 10;
                break;
            case "disableStatisticsLogging":
            case 11:
                message.type = 11;
                break;
            case "startRecording":
            case 12:
                message.type = 12;
                break;
            case "stopRecording":
            case 13:
                message.type = 13;
                break;
            case "startMirroring":
            case 14:
                message.type = 14;
                break;
            case "stopMirroring":
            case 15:
                message.type = 15;
                break;
            }
            return message;
        };

        /**
         * Creates a plain object from a SimpleEventMsgInProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof commonProto.SimpleEventMsgInProto
         * @static
         * @param {commonProto.SimpleEventMsgInProto} message SimpleEventMsgInProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        SimpleEventMsgInProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults)
                object.type = options.enums === String ? "unload" : 0;
            if (message.type != null && Object.hasOwnProperty.call(message, "type"))
                object.type = options.enums === String ? $root.commonProto.SimpleEventMsgInProto.SimpleEventTypeProto[message.type] === undefined ? message.type : $root.commonProto.SimpleEventMsgInProto.SimpleEventTypeProto[message.type] : message.type;
            return object;
        };

        /**
         * Converts this SimpleEventMsgInProto to JSON.
         * @function toJSON
         * @memberof commonProto.SimpleEventMsgInProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        SimpleEventMsgInProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for SimpleEventMsgInProto
         * @function getTypeUrl
         * @memberof commonProto.SimpleEventMsgInProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        SimpleEventMsgInProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/commonProto.SimpleEventMsgInProto";
        };

        /**
         * SimpleEventTypeProto enum.
         * @name commonProto.SimpleEventMsgInProto.SimpleEventTypeProto
         * @enum {number}
         * @property {number} unload=0 unload value
         * @property {number} killSwing=1 killSwing value
         * @property {number} killSwingAdmin=2 killSwingAdmin value
         * @property {number} paintAck=3 paintAck value
         * @property {number} repaint=4 repaint value
         * @property {number} downloadFile=5 downloadFile value
         * @property {number} deleteFile=6 deleteFile value
         * @property {number} cancelFileSelection=7 cancelFileSelection value
         * @property {number} requestComponentTree=8 requestComponentTree value
         * @property {number} requestWindowSwitchList=9 requestWindowSwitchList value
         * @property {number} enableStatisticsLogging=10 enableStatisticsLogging value
         * @property {number} disableStatisticsLogging=11 disableStatisticsLogging value
         * @property {number} startRecording=12 startRecording value
         * @property {number} stopRecording=13 stopRecording value
         * @property {number} startMirroring=14 startMirroring value
         * @property {number} stopMirroring=15 stopMirroring value
         */
        SimpleEventMsgInProto.SimpleEventTypeProto = (function() {
            var valuesById = {}, values = Object.create(valuesById);
            values[valuesById[0] = "unload"] = 0;
            values[valuesById[1] = "killSwing"] = 1;
            values[valuesById[2] = "killSwingAdmin"] = 2;
            values[valuesById[3] = "paintAck"] = 3;
            values[valuesById[4] = "repaint"] = 4;
            values[valuesById[5] = "downloadFile"] = 5;
            values[valuesById[6] = "deleteFile"] = 6;
            values[valuesById[7] = "cancelFileSelection"] = 7;
            values[valuesById[8] = "requestComponentTree"] = 8;
            values[valuesById[9] = "requestWindowSwitchList"] = 9;
            values[valuesById[10] = "enableStatisticsLogging"] = 10;
            values[valuesById[11] = "disableStatisticsLogging"] = 11;
            values[valuesById[12] = "startRecording"] = 12;
            values[valuesById[13] = "stopRecording"] = 13;
            values[valuesById[14] = "startMirroring"] = 14;
            values[valuesById[15] = "stopMirroring"] = 15;
            return values;
        })();

        return SimpleEventMsgInProto;
    })();

    commonProto.ConnectionHandshakeMsgInProto = (function() {

        /**
         * Properties of a ConnectionHandshakeMsgInProto.
         * @memberof commonProto
         * @interface IConnectionHandshakeMsgInProto
         * @property {string|null} [instanceId] ConnectionHandshakeMsgInProto instanceId
         * @property {string|null} [viewId] ConnectionHandshakeMsgInProto viewId
         * @property {string|null} [browserId] ConnectionHandshakeMsgInProto browserId
         * @property {number|null} [desktopWidth] ConnectionHandshakeMsgInProto desktopWidth
         * @property {number|null} [desktopHeight] ConnectionHandshakeMsgInProto desktopHeight
         * @property {string|null} [applicationName] ConnectionHandshakeMsgInProto applicationName
         * @property {boolean|null} [mirrored] ConnectionHandshakeMsgInProto mirrored
         * @property {boolean|null} [directDrawSupported] ConnectionHandshakeMsgInProto directDrawSupported
         * @property {string|null} [documentBase] ConnectionHandshakeMsgInProto documentBase
         * @property {Array.<commonProto.IParamMsgInProto>|null} [params] ConnectionHandshakeMsgInProto params
         * @property {string|null} [locale] ConnectionHandshakeMsgInProto locale
         * @property {string|null} [url] ConnectionHandshakeMsgInProto url
         * @property {string|null} [timeZone] ConnectionHandshakeMsgInProto timeZone
         * @property {boolean|null} [dockingSupported] ConnectionHandshakeMsgInProto dockingSupported
         * @property {boolean|null} [touchMode] ConnectionHandshakeMsgInProto touchMode
         * @property {boolean|null} [accessiblityEnabled] ConnectionHandshakeMsgInProto accessiblityEnabled
         * @property {string|null} [tabId] ConnectionHandshakeMsgInProto tabId
         */

        /**
         * Constructs a new ConnectionHandshakeMsgInProto.
         * @memberof commonProto
         * @classdesc Represents a ConnectionHandshakeMsgInProto.
         * @implements IConnectionHandshakeMsgInProto
         * @constructor
         * @param {commonProto.IConnectionHandshakeMsgInProto=} [properties] Properties to set
         */
        function ConnectionHandshakeMsgInProto(properties) {
            this.params = [];
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * ConnectionHandshakeMsgInProto instanceId.
         * @member {string} instanceId
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.instanceId = "";

        /**
         * ConnectionHandshakeMsgInProto viewId.
         * @member {string} viewId
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.viewId = "";

        /**
         * ConnectionHandshakeMsgInProto browserId.
         * @member {string} browserId
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.browserId = "";

        /**
         * ConnectionHandshakeMsgInProto desktopWidth.
         * @member {number} desktopWidth
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.desktopWidth = 0;

        /**
         * ConnectionHandshakeMsgInProto desktopHeight.
         * @member {number} desktopHeight
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.desktopHeight = 0;

        /**
         * ConnectionHandshakeMsgInProto applicationName.
         * @member {string} applicationName
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.applicationName = "";

        /**
         * ConnectionHandshakeMsgInProto mirrored.
         * @member {boolean} mirrored
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.mirrored = false;

        /**
         * ConnectionHandshakeMsgInProto directDrawSupported.
         * @member {boolean} directDrawSupported
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.directDrawSupported = false;

        /**
         * ConnectionHandshakeMsgInProto documentBase.
         * @member {string} documentBase
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.documentBase = "";

        /**
         * ConnectionHandshakeMsgInProto params.
         * @member {Array.<commonProto.IParamMsgInProto>} params
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.params = $util.emptyArray;

        /**
         * ConnectionHandshakeMsgInProto locale.
         * @member {string} locale
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.locale = "";

        /**
         * ConnectionHandshakeMsgInProto url.
         * @member {string} url
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.url = "";

        /**
         * ConnectionHandshakeMsgInProto timeZone.
         * @member {string} timeZone
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.timeZone = "";

        /**
         * ConnectionHandshakeMsgInProto dockingSupported.
         * @member {boolean} dockingSupported
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.dockingSupported = false;

        /**
         * ConnectionHandshakeMsgInProto touchMode.
         * @member {boolean} touchMode
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.touchMode = false;

        /**
         * ConnectionHandshakeMsgInProto accessiblityEnabled.
         * @member {boolean} accessiblityEnabled
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.accessiblityEnabled = false;

        /**
         * ConnectionHandshakeMsgInProto tabId.
         * @member {string} tabId
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         */
        ConnectionHandshakeMsgInProto.prototype.tabId = "";

        /**
         * Creates a new ConnectionHandshakeMsgInProto instance using the specified properties.
         * @function create
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @static
         * @param {commonProto.IConnectionHandshakeMsgInProto=} [properties] Properties to set
         * @returns {commonProto.ConnectionHandshakeMsgInProto} ConnectionHandshakeMsgInProto instance
         */
        ConnectionHandshakeMsgInProto.create = function create(properties) {
            return new ConnectionHandshakeMsgInProto(properties);
        };

        /**
         * Encodes the specified ConnectionHandshakeMsgInProto message. Does not implicitly {@link commonProto.ConnectionHandshakeMsgInProto.verify|verify} messages.
         * @function encode
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @static
         * @param {commonProto.IConnectionHandshakeMsgInProto} message ConnectionHandshakeMsgInProto message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        ConnectionHandshakeMsgInProto.encode = function encode(message, writer, q) {
            if (!writer)
                writer = $Writer.create();
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            if (message.instanceId != null && Object.hasOwnProperty.call(message, "instanceId"))
                writer.uint32(/* id 1, wireType 2 =*/10).string(message.instanceId);
            if (message.viewId != null && Object.hasOwnProperty.call(message, "viewId"))
                writer.uint32(/* id 2, wireType 2 =*/18).string(message.viewId);
            if (message.browserId != null && Object.hasOwnProperty.call(message, "browserId"))
                writer.uint32(/* id 3, wireType 2 =*/26).string(message.browserId);
            if (message.desktopWidth != null && Object.hasOwnProperty.call(message, "desktopWidth"))
                writer.uint32(/* id 4, wireType 0 =*/32).uint32(message.desktopWidth);
            if (message.desktopHeight != null && Object.hasOwnProperty.call(message, "desktopHeight"))
                writer.uint32(/* id 5, wireType 0 =*/40).uint32(message.desktopHeight);
            if (message.applicationName != null && Object.hasOwnProperty.call(message, "applicationName"))
                writer.uint32(/* id 6, wireType 2 =*/50).string(message.applicationName);
            if (message.mirrored != null && Object.hasOwnProperty.call(message, "mirrored"))
                writer.uint32(/* id 7, wireType 0 =*/56).bool(message.mirrored);
            if (message.directDrawSupported != null && Object.hasOwnProperty.call(message, "directDrawSupported"))
                writer.uint32(/* id 8, wireType 0 =*/64).bool(message.directDrawSupported);
            if (message.documentBase != null && Object.hasOwnProperty.call(message, "documentBase"))
                writer.uint32(/* id 9, wireType 2 =*/74).string(message.documentBase);
            if (message.params != null && message.params.length)
                for (var i = 0; i < message.params.length; ++i)
                    $root.commonProto.ParamMsgInProto.encode(message.params[i], writer.uint32(/* id 10, wireType 2 =*/82).fork(), q + 1).ldelim();
            if (message.locale != null && Object.hasOwnProperty.call(message, "locale"))
                writer.uint32(/* id 11, wireType 2 =*/90).string(message.locale);
            if (message.url != null && Object.hasOwnProperty.call(message, "url"))
                writer.uint32(/* id 12, wireType 2 =*/98).string(message.url);
            if (message.timeZone != null && Object.hasOwnProperty.call(message, "timeZone"))
                writer.uint32(/* id 13, wireType 2 =*/106).string(message.timeZone);
            if (message.dockingSupported != null && Object.hasOwnProperty.call(message, "dockingSupported"))
                writer.uint32(/* id 14, wireType 0 =*/112).bool(message.dockingSupported);
            if (message.touchMode != null && Object.hasOwnProperty.call(message, "touchMode"))
                writer.uint32(/* id 15, wireType 0 =*/120).bool(message.touchMode);
            if (message.accessiblityEnabled != null && Object.hasOwnProperty.call(message, "accessiblityEnabled"))
                writer.uint32(/* id 16, wireType 0 =*/128).bool(message.accessiblityEnabled);
            if (message.tabId != null && Object.hasOwnProperty.call(message, "tabId"))
                writer.uint32(/* id 17, wireType 2 =*/138).string(message.tabId);
            return writer;
        };

        /**
         * Decodes a ConnectionHandshakeMsgInProto message from the specified reader or buffer.
         * @function decode
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {commonProto.ConnectionHandshakeMsgInProto} ConnectionHandshakeMsgInProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        ConnectionHandshakeMsgInProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.commonProto.ConnectionHandshakeMsgInProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.instanceId = reader.string();
                        break;
                    }
                case 2: {
                        message.viewId = reader.string();
                        break;
                    }
                case 3: {
                        message.browserId = reader.string();
                        break;
                    }
                case 4: {
                        message.desktopWidth = reader.uint32();
                        break;
                    }
                case 5: {
                        message.desktopHeight = reader.uint32();
                        break;
                    }
                case 6: {
                        message.applicationName = reader.string();
                        break;
                    }
                case 7: {
                        message.mirrored = reader.bool();
                        break;
                    }
                case 8: {
                        message.directDrawSupported = reader.bool();
                        break;
                    }
                case 9: {
                        message.documentBase = reader.string();
                        break;
                    }
                case 10: {
                        if (!(message.params && message.params.length))
                            message.params = [];
                        message.params.push($root.commonProto.ParamMsgInProto.decode(reader, reader.uint32(), undefined, long + 1));
                        break;
                    }
                case 11: {
                        message.locale = reader.string();
                        break;
                    }
                case 12: {
                        message.url = reader.string();
                        break;
                    }
                case 13: {
                        message.timeZone = reader.string();
                        break;
                    }
                case 14: {
                        message.dockingSupported = reader.bool();
                        break;
                    }
                case 15: {
                        message.touchMode = reader.bool();
                        break;
                    }
                case 16: {
                        message.accessiblityEnabled = reader.bool();
                        break;
                    }
                case 17: {
                        message.tabId = reader.string();
                        break;
                    }
                default:
                    reader.skipType(tag & 7, long);
                    break;
                }
            }
            return message;
        };

        /**
         * Creates a ConnectionHandshakeMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {commonProto.ConnectionHandshakeMsgInProto} ConnectionHandshakeMsgInProto
         */
        ConnectionHandshakeMsgInProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.commonProto.ConnectionHandshakeMsgInProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".commonProto.ConnectionHandshakeMsgInProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.commonProto.ConnectionHandshakeMsgInProto();
            if (object.instanceId != null)
                message.instanceId = String(object.instanceId);
            if (object.viewId != null)
                message.viewId = String(object.viewId);
            if (object.browserId != null)
                message.browserId = String(object.browserId);
            if (object.desktopWidth != null)
                message.desktopWidth = object.desktopWidth >>> 0;
            if (object.desktopHeight != null)
                message.desktopHeight = object.desktopHeight >>> 0;
            if (object.applicationName != null)
                message.applicationName = String(object.applicationName);
            if (object.mirrored != null)
                message.mirrored = Boolean(object.mirrored);
            if (object.directDrawSupported != null)
                message.directDrawSupported = Boolean(object.directDrawSupported);
            if (object.documentBase != null)
                message.documentBase = String(object.documentBase);
            if (object.params) {
                if (!Array.isArray(object.params))
                    throw TypeError(".commonProto.ConnectionHandshakeMsgInProto.params: array expected");
                message.params = [];
                for (var i = 0; i < object.params.length; ++i) {
                    if (!$util.isObject(object.params[i]))
                        throw TypeError(".commonProto.ConnectionHandshakeMsgInProto.params: object expected");
                    message.params[i] = $root.commonProto.ParamMsgInProto.fromObject(object.params[i], long + 1);
                }
            }
            if (object.locale != null)
                message.locale = String(object.locale);
            if (object.url != null)
                message.url = String(object.url);
            if (object.timeZone != null)
                message.timeZone = String(object.timeZone);
            if (object.dockingSupported != null)
                message.dockingSupported = Boolean(object.dockingSupported);
            if (object.touchMode != null)
                message.touchMode = Boolean(object.touchMode);
            if (object.accessiblityEnabled != null)
                message.accessiblityEnabled = Boolean(object.accessiblityEnabled);
            if (object.tabId != null)
                message.tabId = String(object.tabId);
            return message;
        };

        /**
         * Creates a plain object from a ConnectionHandshakeMsgInProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @static
         * @param {commonProto.ConnectionHandshakeMsgInProto} message ConnectionHandshakeMsgInProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        ConnectionHandshakeMsgInProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.arrays || options.defaults)
                object.params = [];
            if (options.defaults) {
                object.instanceId = "";
                object.viewId = "";
                object.browserId = "";
                object.desktopWidth = 0;
                object.desktopHeight = 0;
                object.applicationName = "";
                object.mirrored = false;
                object.directDrawSupported = false;
                object.documentBase = "";
                object.locale = "";
                object.url = "";
                object.timeZone = "";
                object.dockingSupported = false;
                object.touchMode = false;
                object.accessiblityEnabled = false;
                object.tabId = "";
            }
            if (message.instanceId != null && Object.hasOwnProperty.call(message, "instanceId"))
                object.instanceId = message.instanceId;
            if (message.viewId != null && Object.hasOwnProperty.call(message, "viewId"))
                object.viewId = message.viewId;
            if (message.browserId != null && Object.hasOwnProperty.call(message, "browserId"))
                object.browserId = message.browserId;
            if (message.desktopWidth != null && Object.hasOwnProperty.call(message, "desktopWidth"))
                object.desktopWidth = message.desktopWidth;
            if (message.desktopHeight != null && Object.hasOwnProperty.call(message, "desktopHeight"))
                object.desktopHeight = message.desktopHeight;
            if (message.applicationName != null && Object.hasOwnProperty.call(message, "applicationName"))
                object.applicationName = message.applicationName;
            if (message.mirrored != null && Object.hasOwnProperty.call(message, "mirrored"))
                object.mirrored = message.mirrored;
            if (message.directDrawSupported != null && Object.hasOwnProperty.call(message, "directDrawSupported"))
                object.directDrawSupported = message.directDrawSupported;
            if (message.documentBase != null && Object.hasOwnProperty.call(message, "documentBase"))
                object.documentBase = message.documentBase;
            if (message.params && message.params.length) {
                object.params = [];
                for (var j = 0; j < message.params.length; ++j)
                    object.params[j] = $root.commonProto.ParamMsgInProto.toObject(message.params[j], options, q + 1);
            }
            if (message.locale != null && Object.hasOwnProperty.call(message, "locale"))
                object.locale = message.locale;
            if (message.url != null && Object.hasOwnProperty.call(message, "url"))
                object.url = message.url;
            if (message.timeZone != null && Object.hasOwnProperty.call(message, "timeZone"))
                object.timeZone = message.timeZone;
            if (message.dockingSupported != null && Object.hasOwnProperty.call(message, "dockingSupported"))
                object.dockingSupported = message.dockingSupported;
            if (message.touchMode != null && Object.hasOwnProperty.call(message, "touchMode"))
                object.touchMode = message.touchMode;
            if (message.accessiblityEnabled != null && Object.hasOwnProperty.call(message, "accessiblityEnabled"))
                object.accessiblityEnabled = message.accessiblityEnabled;
            if (message.tabId != null && Object.hasOwnProperty.call(message, "tabId"))
                object.tabId = message.tabId;
            return object;
        };

        /**
         * Converts this ConnectionHandshakeMsgInProto to JSON.
         * @function toJSON
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        ConnectionHandshakeMsgInProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for ConnectionHandshakeMsgInProto
         * @function getTypeUrl
         * @memberof commonProto.ConnectionHandshakeMsgInProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        ConnectionHandshakeMsgInProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/commonProto.ConnectionHandshakeMsgInProto";
        };

        return ConnectionHandshakeMsgInProto;
    })();

    commonProto.TimestampsMsgInProto = (function() {

        /**
         * Properties of a TimestampsMsgInProto.
         * @memberof commonProto
         * @interface ITimestampsMsgInProto
         * @property {string|null} [startTimestamp] TimestampsMsgInProto startTimestamp
         * @property {string|null} [sendTimestamp] TimestampsMsgInProto sendTimestamp
         * @property {string|null} [renderingTime] TimestampsMsgInProto renderingTime
         * @property {number|null} [ping] TimestampsMsgInProto ping
         */

        /**
         * Constructs a new TimestampsMsgInProto.
         * @memberof commonProto
         * @classdesc Represents a TimestampsMsgInProto.
         * @implements ITimestampsMsgInProto
         * @constructor
         * @param {commonProto.ITimestampsMsgInProto=} [properties] Properties to set
         */
        function TimestampsMsgInProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * TimestampsMsgInProto startTimestamp.
         * @member {string} startTimestamp
         * @memberof commonProto.TimestampsMsgInProto
         * @instance
         */
        TimestampsMsgInProto.prototype.startTimestamp = "";

        /**
         * TimestampsMsgInProto sendTimestamp.
         * @member {string} sendTimestamp
         * @memberof commonProto.TimestampsMsgInProto
         * @instance
         */
        TimestampsMsgInProto.prototype.sendTimestamp = "";

        /**
         * TimestampsMsgInProto renderingTime.
         * @member {string} renderingTime
         * @memberof commonProto.TimestampsMsgInProto
         * @instance
         */
        TimestampsMsgInProto.prototype.renderingTime = "";

        /**
         * TimestampsMsgInProto ping.
         * @member {number} ping
         * @memberof commonProto.TimestampsMsgInProto
         * @instance
         */
        TimestampsMsgInProto.prototype.ping = 0;

        /**
         * Creates a new TimestampsMsgInProto instance using the specified properties.
         * @function create
         * @memberof commonProto.TimestampsMsgInProto
         * @static
         * @param {commonProto.ITimestampsMsgInProto=} [properties] Properties to set
         * @returns {commonProto.TimestampsMsgInProto} TimestampsMsgInProto instance
         */
        TimestampsMsgInProto.create = function create(properties) {
            return new TimestampsMsgInProto(properties);
        };

        /**
         * Encodes the specified TimestampsMsgInProto message. Does not implicitly {@link commonProto.TimestampsMsgInProto.verify|verify} messages.
         * @function encode
         * @memberof commonProto.TimestampsMsgInProto
         * @static
         * @param {commonProto.ITimestampsMsgInProto} message TimestampsMsgInProto message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        TimestampsMsgInProto.encode = function encode(message, writer, q) {
            if (!writer)
                writer = $Writer.create();
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            if (message.startTimestamp != null && Object.hasOwnProperty.call(message, "startTimestamp"))
                writer.uint32(/* id 1, wireType 2 =*/10).string(message.startTimestamp);
            if (message.sendTimestamp != null && Object.hasOwnProperty.call(message, "sendTimestamp"))
                writer.uint32(/* id 2, wireType 2 =*/18).string(message.sendTimestamp);
            if (message.renderingTime != null && Object.hasOwnProperty.call(message, "renderingTime"))
                writer.uint32(/* id 3, wireType 2 =*/26).string(message.renderingTime);
            if (message.ping != null && Object.hasOwnProperty.call(message, "ping"))
                writer.uint32(/* id 4, wireType 0 =*/32).uint32(message.ping);
            return writer;
        };

        /**
         * Decodes a TimestampsMsgInProto message from the specified reader or buffer.
         * @function decode
         * @memberof commonProto.TimestampsMsgInProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {commonProto.TimestampsMsgInProto} TimestampsMsgInProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        TimestampsMsgInProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.commonProto.TimestampsMsgInProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.startTimestamp = reader.string();
                        break;
                    }
                case 2: {
                        message.sendTimestamp = reader.string();
                        break;
                    }
                case 3: {
                        message.renderingTime = reader.string();
                        break;
                    }
                case 4: {
                        message.ping = reader.uint32();
                        break;
                    }
                default:
                    reader.skipType(tag & 7, long);
                    break;
                }
            }
            return message;
        };

        /**
         * Creates a TimestampsMsgInProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof commonProto.TimestampsMsgInProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {commonProto.TimestampsMsgInProto} TimestampsMsgInProto
         */
        TimestampsMsgInProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.commonProto.TimestampsMsgInProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".commonProto.TimestampsMsgInProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.commonProto.TimestampsMsgInProto();
            if (object.startTimestamp != null)
                message.startTimestamp = String(object.startTimestamp);
            if (object.sendTimestamp != null)
                message.sendTimestamp = String(object.sendTimestamp);
            if (object.renderingTime != null)
                message.renderingTime = String(object.renderingTime);
            if (object.ping != null)
                message.ping = object.ping >>> 0;
            return message;
        };

        /**
         * Creates a plain object from a TimestampsMsgInProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof commonProto.TimestampsMsgInProto
         * @static
         * @param {commonProto.TimestampsMsgInProto} message TimestampsMsgInProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        TimestampsMsgInProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.startTimestamp = "";
                object.sendTimestamp = "";
                object.renderingTime = "";
                object.ping = 0;
            }
            if (message.startTimestamp != null && Object.hasOwnProperty.call(message, "startTimestamp"))
                object.startTimestamp = message.startTimestamp;
            if (message.sendTimestamp != null && Object.hasOwnProperty.call(message, "sendTimestamp"))
                object.sendTimestamp = message.sendTimestamp;
            if (message.renderingTime != null && Object.hasOwnProperty.call(message, "renderingTime"))
                object.renderingTime = message.renderingTime;
            if (message.ping != null && Object.hasOwnProperty.call(message, "ping"))
                object.ping = message.ping;
            return object;
        };

        /**
         * Converts this TimestampsMsgInProto to JSON.
         * @function toJSON
         * @memberof commonProto.TimestampsMsgInProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        TimestampsMsgInProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for TimestampsMsgInProto
         * @function getTypeUrl
         * @memberof commonProto.TimestampsMsgInProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        TimestampsMsgInProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/commonProto.TimestampsMsgInProto";
        };

        return TimestampsMsgInProto;
    })();

    return commonProto;
})();

module.exports = $root;
