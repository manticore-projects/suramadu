/*eslint-disable block-scoped-var, id-length, no-control-regex, no-magic-numbers, no-prototype-builtins, no-redeclare, no-shadow, no-var, sort-vars*/
"use strict";

var $protobuf = require("protobufjs/minimal");

// Common aliases
var $Reader = $protobuf.Reader, $Writer = $protobuf.Writer, $util = $protobuf.util;

// Exported root namespace
var $root = $protobuf.roots["default"] || ($protobuf.roots["default"] = {});

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
