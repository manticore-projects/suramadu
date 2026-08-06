/*eslint-disable block-scoped-var, id-length, no-control-regex, no-magic-numbers, no-prototype-builtins, no-redeclare, no-shadow, no-var, sort-vars*/
"use strict";

var $protobuf = require("protobufjs/minimal");

// Common aliases
var $Reader = $protobuf.Reader, $util = $protobuf.util;

// Exported root namespace
var $root = $protobuf.roots["default"] || ($protobuf.roots["default"] = {});

$root.org = (function() {

    /**
     * Namespace org.
     * @exports org
     * @namespace
     */
    var org = {};

    org.webswing = (function() {

        /**
         * Namespace webswing.
         * @memberof org
         * @namespace
         */
        var webswing = {};

        webswing.directdraw = (function() {

            /**
             * Namespace directdraw.
             * @memberof org.webswing
             * @namespace
             */
            var directdraw = {};

            directdraw.proto = (function() {

                /**
                 * Namespace proto.
                 * @memberof org.webswing.directdraw
                 * @namespace
                 */
                var proto = {};

                proto.WebImageProto = (function() {

                    /**
                     * Properties of a WebImageProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IWebImageProto
                     * @property {number} width WebImageProto width
                     * @property {number} height WebImageProto height
                     * @property {Array.<org.webswing.directdraw.proto.IDrawInstructionProto>|null} [instructions] WebImageProto instructions
                     * @property {Array.<org.webswing.directdraw.proto.IDrawConstantProto>|null} [constants] WebImageProto constants
                     * @property {Array.<org.webswing.directdraw.proto.IFontFaceProto>|null} [fontFaces] WebImageProto fontFaces
                     */

                    /**
                     * Constructs a new WebImageProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a WebImageProto.
                     * @implements IWebImageProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IWebImageProto=} [properties] Properties to set
                     */
                    function WebImageProto(properties) {
                        this.instructions = [];
                        this.constants = [];
                        this.fontFaces = [];
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * WebImageProto width.
                     * @member {number} width
                     * @memberof org.webswing.directdraw.proto.WebImageProto
                     * @instance
                     */
                    WebImageProto.prototype.width = 0;

                    /**
                     * WebImageProto height.
                     * @member {number} height
                     * @memberof org.webswing.directdraw.proto.WebImageProto
                     * @instance
                     */
                    WebImageProto.prototype.height = 0;

                    /**
                     * WebImageProto instructions.
                     * @member {Array.<org.webswing.directdraw.proto.IDrawInstructionProto>} instructions
                     * @memberof org.webswing.directdraw.proto.WebImageProto
                     * @instance
                     */
                    WebImageProto.prototype.instructions = $util.emptyArray;

                    /**
                     * WebImageProto constants.
                     * @member {Array.<org.webswing.directdraw.proto.IDrawConstantProto>} constants
                     * @memberof org.webswing.directdraw.proto.WebImageProto
                     * @instance
                     */
                    WebImageProto.prototype.constants = $util.emptyArray;

                    /**
                     * WebImageProto fontFaces.
                     * @member {Array.<org.webswing.directdraw.proto.IFontFaceProto>} fontFaces
                     * @memberof org.webswing.directdraw.proto.WebImageProto
                     * @instance
                     */
                    WebImageProto.prototype.fontFaces = $util.emptyArray;

                    /**
                     * Decodes a WebImageProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.WebImageProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.WebImageProto} WebImageProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    WebImageProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.WebImageProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.width = reader.uint32();
                                    break;
                                }
                            case 2: {
                                    message.height = reader.uint32();
                                    break;
                                }
                            case 3: {
                                    if (!(message.instructions && message.instructions.length))
                                        message.instructions = [];
                                    message.instructions.push($root.org.webswing.directdraw.proto.DrawInstructionProto.decode(reader, reader.uint32(), undefined, long + 1));
                                    break;
                                }
                            case 4: {
                                    if (!(message.constants && message.constants.length))
                                        message.constants = [];
                                    message.constants.push($root.org.webswing.directdraw.proto.DrawConstantProto.decode(reader, reader.uint32(), undefined, long + 1));
                                    break;
                                }
                            case 5: {
                                    if (!(message.fontFaces && message.fontFaces.length))
                                        message.fontFaces = [];
                                    message.fontFaces.push($root.org.webswing.directdraw.proto.FontFaceProto.decode(reader, reader.uint32(), undefined, long + 1));
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "width"))
                            throw $util.ProtocolError("missing required 'width'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "height"))
                            throw $util.ProtocolError("missing required 'height'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a WebImageProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.WebImageProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.WebImageProto} WebImageProto
                     */
                    WebImageProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.WebImageProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.WebImageProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.WebImageProto();
                        if (object.width != null)
                            message.width = object.width >>> 0;
                        if (object.height != null)
                            message.height = object.height >>> 0;
                        if (object.instructions) {
                            if (!Array.isArray(object.instructions))
                                throw TypeError(".org.webswing.directdraw.proto.WebImageProto.instructions: array expected");
                            message.instructions = [];
                            for (var i = 0; i < object.instructions.length; ++i) {
                                if (!$util.isObject(object.instructions[i]))
                                    throw TypeError(".org.webswing.directdraw.proto.WebImageProto.instructions: object expected");
                                message.instructions[i] = $root.org.webswing.directdraw.proto.DrawInstructionProto.fromObject(object.instructions[i], long + 1);
                            }
                        }
                        if (object.constants) {
                            if (!Array.isArray(object.constants))
                                throw TypeError(".org.webswing.directdraw.proto.WebImageProto.constants: array expected");
                            message.constants = [];
                            for (var i = 0; i < object.constants.length; ++i) {
                                if (!$util.isObject(object.constants[i]))
                                    throw TypeError(".org.webswing.directdraw.proto.WebImageProto.constants: object expected");
                                message.constants[i] = $root.org.webswing.directdraw.proto.DrawConstantProto.fromObject(object.constants[i], long + 1);
                            }
                        }
                        if (object.fontFaces) {
                            if (!Array.isArray(object.fontFaces))
                                throw TypeError(".org.webswing.directdraw.proto.WebImageProto.fontFaces: array expected");
                            message.fontFaces = [];
                            for (var i = 0; i < object.fontFaces.length; ++i) {
                                if (!$util.isObject(object.fontFaces[i]))
                                    throw TypeError(".org.webswing.directdraw.proto.WebImageProto.fontFaces: object expected");
                                message.fontFaces[i] = $root.org.webswing.directdraw.proto.FontFaceProto.fromObject(object.fontFaces[i], long + 1);
                            }
                        }
                        return message;
                    };

                    /**
                     * Creates a plain object from a WebImageProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.WebImageProto
                     * @static
                     * @param {org.webswing.directdraw.proto.WebImageProto} message WebImageProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    WebImageProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.arrays || options.defaults) {
                            object.instructions = [];
                            object.constants = [];
                            object.fontFaces = [];
                        }
                        if (options.defaults) {
                            object.width = 0;
                            object.height = 0;
                        }
                        if (message.width != null && Object.hasOwnProperty.call(message, "width"))
                            object.width = message.width;
                        if (message.height != null && Object.hasOwnProperty.call(message, "height"))
                            object.height = message.height;
                        if (message.instructions && message.instructions.length) {
                            object.instructions = [];
                            for (var j = 0; j < message.instructions.length; ++j)
                                object.instructions[j] = $root.org.webswing.directdraw.proto.DrawInstructionProto.toObject(message.instructions[j], options, q + 1);
                        }
                        if (message.constants && message.constants.length) {
                            object.constants = [];
                            for (var j = 0; j < message.constants.length; ++j)
                                object.constants[j] = $root.org.webswing.directdraw.proto.DrawConstantProto.toObject(message.constants[j], options, q + 1);
                        }
                        if (message.fontFaces && message.fontFaces.length) {
                            object.fontFaces = [];
                            for (var j = 0; j < message.fontFaces.length; ++j)
                                object.fontFaces[j] = $root.org.webswing.directdraw.proto.FontFaceProto.toObject(message.fontFaces[j], options, q + 1);
                        }
                        return object;
                    };

                    /**
                     * Converts this WebImageProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.WebImageProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    WebImageProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for WebImageProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.WebImageProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    WebImageProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.WebImageProto";
                    };

                    return WebImageProto;
                })();

                proto.DrawInstructionProto = (function() {

                    /**
                     * Properties of a DrawInstructionProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IDrawInstructionProto
                     * @property {org.webswing.directdraw.proto.DrawInstructionProto.InstructionProto} inst DrawInstructionProto inst
                     * @property {Array.<number>|null} [args] DrawInstructionProto args
                     * @property {Uint8Array|null} [webImage] DrawInstructionProto webImage
                     */

                    /**
                     * Constructs a new DrawInstructionProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a DrawInstructionProto.
                     * @implements IDrawInstructionProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IDrawInstructionProto=} [properties] Properties to set
                     */
                    function DrawInstructionProto(properties) {
                        this.args = [];
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * DrawInstructionProto inst.
                     * @member {org.webswing.directdraw.proto.DrawInstructionProto.InstructionProto} inst
                     * @memberof org.webswing.directdraw.proto.DrawInstructionProto
                     * @instance
                     */
                    DrawInstructionProto.prototype.inst = 0;

                    /**
                     * DrawInstructionProto args.
                     * @member {Array.<number>} args
                     * @memberof org.webswing.directdraw.proto.DrawInstructionProto
                     * @instance
                     */
                    DrawInstructionProto.prototype.args = $util.emptyArray;

                    /**
                     * DrawInstructionProto webImage.
                     * @member {Uint8Array} webImage
                     * @memberof org.webswing.directdraw.proto.DrawInstructionProto
                     * @instance
                     */
                    DrawInstructionProto.prototype.webImage = $util.newBuffer([]);

                    /**
                     * Decodes a DrawInstructionProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.DrawInstructionProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.DrawInstructionProto} DrawInstructionProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    DrawInstructionProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.DrawInstructionProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.inst = reader.int32();
                                    break;
                                }
                            case 2: {
                                    if (!(message.args && message.args.length))
                                        message.args = [];
                                    if ((tag & 7) === 2) {
                                        var end2 = reader.uint32() + reader.pos;
                                        while (reader.pos < end2)
                                            message.args.push(reader.uint32());
                                    } else
                                        message.args.push(reader.uint32());
                                    break;
                                }
                            case 3: {
                                    message.webImage = reader.bytes();
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "inst"))
                            throw $util.ProtocolError("missing required 'inst'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a DrawInstructionProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.DrawInstructionProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.DrawInstructionProto} DrawInstructionProto
                     */
                    DrawInstructionProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.DrawInstructionProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.DrawInstructionProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.DrawInstructionProto();
                        switch (object.inst) {
                        default:
                            if (typeof object.inst === "number") {
                                message.inst = object.inst;
                                break;
                            }
                            break;
                        case "DRAW":
                        case 0:
                            message.inst = 0;
                            break;
                        case "FILL":
                        case 1:
                            message.inst = 1;
                            break;
                        case "DRAW_IMAGE":
                        case 2:
                            message.inst = 2;
                            break;
                        case "DRAW_WEBIMAGE":
                        case 3:
                            message.inst = 3;
                            break;
                        case "DRAW_STRING":
                        case 4:
                            message.inst = 4;
                            break;
                        case "COPY_AREA":
                        case 5:
                            message.inst = 5;
                            break;
                        case "GRAPHICS_DISPOSE":
                        case 6:
                            message.inst = 6;
                            break;
                        case "GRAPHICS_SWITCH":
                        case 7:
                            message.inst = 7;
                            break;
                        case "GRAPHICS_CREATE":
                        case 8:
                            message.inst = 8;
                            break;
                        case "TRANSFORM":
                        case 9:
                            message.inst = 9;
                            break;
                        case "SET_PAINT":
                        case 10:
                            message.inst = 10;
                            break;
                        case "SET_FONT":
                        case 11:
                            message.inst = 11;
                            break;
                        case "SET_STROKE":
                        case 12:
                            message.inst = 12;
                            break;
                        case "SET_COMPOSITE":
                        case 13:
                            message.inst = 13;
                            break;
                        case "DRAW_GLYPH_LIST":
                        case 14:
                            message.inst = 14;
                            break;
                        }
                        if (object.args) {
                            if (!Array.isArray(object.args))
                                throw TypeError(".org.webswing.directdraw.proto.DrawInstructionProto.args: array expected");
                            message.args = [];
                            for (var i = 0; i < object.args.length; ++i)
                                message.args[i] = object.args[i] >>> 0;
                        }
                        if (object.webImage != null)
                            if (typeof object.webImage === "string")
                                $util.base64.decode(object.webImage, message.webImage = $util.newBuffer($util.base64.length(object.webImage)), 0);
                            else if (object.webImage.length >= 0)
                                message.webImage = object.webImage;
                        return message;
                    };

                    /**
                     * Creates a plain object from a DrawInstructionProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.DrawInstructionProto
                     * @static
                     * @param {org.webswing.directdraw.proto.DrawInstructionProto} message DrawInstructionProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    DrawInstructionProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.arrays || options.defaults)
                            object.args = [];
                        if (options.defaults) {
                            object.inst = options.enums === String ? "DRAW" : 0;
                            if (options.bytes === String)
                                object.webImage = "";
                            else {
                                object.webImage = [];
                                if (options.bytes !== Array)
                                    object.webImage = $util.newBuffer(object.webImage);
                            }
                        }
                        if (message.inst != null && Object.hasOwnProperty.call(message, "inst"))
                            object.inst = options.enums === String ? $root.org.webswing.directdraw.proto.DrawInstructionProto.InstructionProto[message.inst] === undefined ? message.inst : $root.org.webswing.directdraw.proto.DrawInstructionProto.InstructionProto[message.inst] : message.inst;
                        if (message.args && message.args.length) {
                            object.args = [];
                            for (var j = 0; j < message.args.length; ++j)
                                object.args[j] = message.args[j];
                        }
                        if (message.webImage != null && Object.hasOwnProperty.call(message, "webImage"))
                            object.webImage = options.bytes === String ? $util.base64.encode(message.webImage, 0, message.webImage.length) : options.bytes === Array ? Array.prototype.slice.call(message.webImage) : message.webImage;
                        return object;
                    };

                    /**
                     * Converts this DrawInstructionProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.DrawInstructionProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    DrawInstructionProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for DrawInstructionProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.DrawInstructionProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    DrawInstructionProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.DrawInstructionProto";
                    };

                    /**
                     * InstructionProto enum.
                     * @name org.webswing.directdraw.proto.DrawInstructionProto.InstructionProto
                     * @enum {number}
                     * @property {number} DRAW=0 DRAW value
                     * @property {number} FILL=1 FILL value
                     * @property {number} DRAW_IMAGE=2 DRAW_IMAGE value
                     * @property {number} DRAW_WEBIMAGE=3 DRAW_WEBIMAGE value
                     * @property {number} DRAW_STRING=4 DRAW_STRING value
                     * @property {number} COPY_AREA=5 COPY_AREA value
                     * @property {number} GRAPHICS_DISPOSE=6 GRAPHICS_DISPOSE value
                     * @property {number} GRAPHICS_SWITCH=7 GRAPHICS_SWITCH value
                     * @property {number} GRAPHICS_CREATE=8 GRAPHICS_CREATE value
                     * @property {number} TRANSFORM=9 TRANSFORM value
                     * @property {number} SET_PAINT=10 SET_PAINT value
                     * @property {number} SET_FONT=11 SET_FONT value
                     * @property {number} SET_STROKE=12 SET_STROKE value
                     * @property {number} SET_COMPOSITE=13 SET_COMPOSITE value
                     * @property {number} DRAW_GLYPH_LIST=14 DRAW_GLYPH_LIST value
                     */
                    DrawInstructionProto.InstructionProto = (function() {
                        var valuesById = {}, values = Object.create(valuesById);
                        values[valuesById[0] = "DRAW"] = 0;
                        values[valuesById[1] = "FILL"] = 1;
                        values[valuesById[2] = "DRAW_IMAGE"] = 2;
                        values[valuesById[3] = "DRAW_WEBIMAGE"] = 3;
                        values[valuesById[4] = "DRAW_STRING"] = 4;
                        values[valuesById[5] = "COPY_AREA"] = 5;
                        values[valuesById[6] = "GRAPHICS_DISPOSE"] = 6;
                        values[valuesById[7] = "GRAPHICS_SWITCH"] = 7;
                        values[valuesById[8] = "GRAPHICS_CREATE"] = 8;
                        values[valuesById[9] = "TRANSFORM"] = 9;
                        values[valuesById[10] = "SET_PAINT"] = 10;
                        values[valuesById[11] = "SET_FONT"] = 11;
                        values[valuesById[12] = "SET_STROKE"] = 12;
                        values[valuesById[13] = "SET_COMPOSITE"] = 13;
                        values[valuesById[14] = "DRAW_GLYPH_LIST"] = 14;
                        return values;
                    })();

                    return DrawInstructionProto;
                })();

                proto.DrawConstantProto = (function() {

                    /**
                     * Properties of a DrawConstantProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IDrawConstantProto
                     * @property {number} id DrawConstantProto id
                     * @property {org.webswing.directdraw.proto.IColorProto|null} [color] DrawConstantProto color
                     * @property {org.webswing.directdraw.proto.IImageProto|null} [image] DrawConstantProto image
                     * @property {org.webswing.directdraw.proto.ITransformProto|null} [transform] DrawConstantProto transform
                     * @property {string|null} [string] DrawConstantProto string
                     * @property {org.webswing.directdraw.proto.IPathProto|null} [path] DrawConstantProto path
                     * @property {org.webswing.directdraw.proto.IFontProto|null} [font] DrawConstantProto font
                     * @property {org.webswing.directdraw.proto.ILinearGradientProto|null} [linearGrad] DrawConstantProto linearGrad
                     * @property {org.webswing.directdraw.proto.IRadialGradientProto|null} [radialGrad] DrawConstantProto radialGrad
                     * @property {org.webswing.directdraw.proto.IPointsProto|null} [points] DrawConstantProto points
                     * @property {org.webswing.directdraw.proto.IRectangleProto|null} [rectangle] DrawConstantProto rectangle
                     * @property {org.webswing.directdraw.proto.IEllipseProto|null} [ellipse] DrawConstantProto ellipse
                     * @property {org.webswing.directdraw.proto.IRoundRectangleProto|null} [roundRectangle] DrawConstantProto roundRectangle
                     * @property {org.webswing.directdraw.proto.IArcProto|null} [arc] DrawConstantProto arc
                     * @property {org.webswing.directdraw.proto.IStrokeProto|null} [stroke] DrawConstantProto stroke
                     * @property {org.webswing.directdraw.proto.ICompositeProto|null} [composite] DrawConstantProto composite
                     * @property {org.webswing.directdraw.proto.ITextureProto|null} [texture] DrawConstantProto texture
                     * @property {org.webswing.directdraw.proto.IGlyphProto|null} [glyph] DrawConstantProto glyph
                     * @property {org.webswing.directdraw.proto.ICombinedProto|null} [combined] DrawConstantProto combined
                     */

                    /**
                     * Constructs a new DrawConstantProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a DrawConstantProto.
                     * @implements IDrawConstantProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IDrawConstantProto=} [properties] Properties to set
                     */
                    function DrawConstantProto(properties) {
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * DrawConstantProto id.
                     * @member {number} id
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.id = 0;

                    /**
                     * DrawConstantProto color.
                     * @member {org.webswing.directdraw.proto.IColorProto|null|undefined} color
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.color = null;

                    /**
                     * DrawConstantProto image.
                     * @member {org.webswing.directdraw.proto.IImageProto|null|undefined} image
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.image = null;

                    /**
                     * DrawConstantProto transform.
                     * @member {org.webswing.directdraw.proto.ITransformProto|null|undefined} transform
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.transform = null;

                    /**
                     * DrawConstantProto string.
                     * @member {string} string
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.string = "";

                    /**
                     * DrawConstantProto path.
                     * @member {org.webswing.directdraw.proto.IPathProto|null|undefined} path
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.path = null;

                    /**
                     * DrawConstantProto font.
                     * @member {org.webswing.directdraw.proto.IFontProto|null|undefined} font
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.font = null;

                    /**
                     * DrawConstantProto linearGrad.
                     * @member {org.webswing.directdraw.proto.ILinearGradientProto|null|undefined} linearGrad
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.linearGrad = null;

                    /**
                     * DrawConstantProto radialGrad.
                     * @member {org.webswing.directdraw.proto.IRadialGradientProto|null|undefined} radialGrad
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.radialGrad = null;

                    /**
                     * DrawConstantProto points.
                     * @member {org.webswing.directdraw.proto.IPointsProto|null|undefined} points
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.points = null;

                    /**
                     * DrawConstantProto rectangle.
                     * @member {org.webswing.directdraw.proto.IRectangleProto|null|undefined} rectangle
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.rectangle = null;

                    /**
                     * DrawConstantProto ellipse.
                     * @member {org.webswing.directdraw.proto.IEllipseProto|null|undefined} ellipse
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.ellipse = null;

                    /**
                     * DrawConstantProto roundRectangle.
                     * @member {org.webswing.directdraw.proto.IRoundRectangleProto|null|undefined} roundRectangle
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.roundRectangle = null;

                    /**
                     * DrawConstantProto arc.
                     * @member {org.webswing.directdraw.proto.IArcProto|null|undefined} arc
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.arc = null;

                    /**
                     * DrawConstantProto stroke.
                     * @member {org.webswing.directdraw.proto.IStrokeProto|null|undefined} stroke
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.stroke = null;

                    /**
                     * DrawConstantProto composite.
                     * @member {org.webswing.directdraw.proto.ICompositeProto|null|undefined} composite
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.composite = null;

                    /**
                     * DrawConstantProto texture.
                     * @member {org.webswing.directdraw.proto.ITextureProto|null|undefined} texture
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.texture = null;

                    /**
                     * DrawConstantProto glyph.
                     * @member {org.webswing.directdraw.proto.IGlyphProto|null|undefined} glyph
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.glyph = null;

                    /**
                     * DrawConstantProto combined.
                     * @member {org.webswing.directdraw.proto.ICombinedProto|null|undefined} combined
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     */
                    DrawConstantProto.prototype.combined = null;

                    /**
                     * Decodes a DrawConstantProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.DrawConstantProto} DrawConstantProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    DrawConstantProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.DrawConstantProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.id = reader.uint32();
                                    break;
                                }
                            case 2: {
                                    message.color = $root.org.webswing.directdraw.proto.ColorProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 3: {
                                    message.image = $root.org.webswing.directdraw.proto.ImageProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 4: {
                                    message.transform = $root.org.webswing.directdraw.proto.TransformProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 5: {
                                    message.string = reader.string();
                                    break;
                                }
                            case 6: {
                                    message.path = $root.org.webswing.directdraw.proto.PathProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 7: {
                                    message.font = $root.org.webswing.directdraw.proto.FontProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 8: {
                                    message.linearGrad = $root.org.webswing.directdraw.proto.LinearGradientProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 9: {
                                    message.radialGrad = $root.org.webswing.directdraw.proto.RadialGradientProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 10: {
                                    message.points = $root.org.webswing.directdraw.proto.PointsProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 11: {
                                    message.rectangle = $root.org.webswing.directdraw.proto.RectangleProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 12: {
                                    message.ellipse = $root.org.webswing.directdraw.proto.EllipseProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 13: {
                                    message.roundRectangle = $root.org.webswing.directdraw.proto.RoundRectangleProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 14: {
                                    message.arc = $root.org.webswing.directdraw.proto.ArcProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 15: {
                                    message.stroke = $root.org.webswing.directdraw.proto.StrokeProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 16: {
                                    message.composite = $root.org.webswing.directdraw.proto.CompositeProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 17: {
                                    message.texture = $root.org.webswing.directdraw.proto.TextureProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 18: {
                                    message.glyph = $root.org.webswing.directdraw.proto.GlyphProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 19: {
                                    message.combined = $root.org.webswing.directdraw.proto.CombinedProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "id"))
                            throw $util.ProtocolError("missing required 'id'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a DrawConstantProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.DrawConstantProto} DrawConstantProto
                     */
                    DrawConstantProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.DrawConstantProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.DrawConstantProto();
                        if (object.id != null)
                            message.id = object.id >>> 0;
                        if (object.color != null) {
                            if (!$util.isObject(object.color))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.color: object expected");
                            message.color = $root.org.webswing.directdraw.proto.ColorProto.fromObject(object.color, long + 1);
                        }
                        if (object.image != null) {
                            if (!$util.isObject(object.image))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.image: object expected");
                            message.image = $root.org.webswing.directdraw.proto.ImageProto.fromObject(object.image, long + 1);
                        }
                        if (object.transform != null) {
                            if (!$util.isObject(object.transform))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.transform: object expected");
                            message.transform = $root.org.webswing.directdraw.proto.TransformProto.fromObject(object.transform, long + 1);
                        }
                        if (object.string != null)
                            message.string = String(object.string);
                        if (object.path != null) {
                            if (!$util.isObject(object.path))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.path: object expected");
                            message.path = $root.org.webswing.directdraw.proto.PathProto.fromObject(object.path, long + 1);
                        }
                        if (object.font != null) {
                            if (!$util.isObject(object.font))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.font: object expected");
                            message.font = $root.org.webswing.directdraw.proto.FontProto.fromObject(object.font, long + 1);
                        }
                        if (object.linearGrad != null) {
                            if (!$util.isObject(object.linearGrad))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.linearGrad: object expected");
                            message.linearGrad = $root.org.webswing.directdraw.proto.LinearGradientProto.fromObject(object.linearGrad, long + 1);
                        }
                        if (object.radialGrad != null) {
                            if (!$util.isObject(object.radialGrad))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.radialGrad: object expected");
                            message.radialGrad = $root.org.webswing.directdraw.proto.RadialGradientProto.fromObject(object.radialGrad, long + 1);
                        }
                        if (object.points != null) {
                            if (!$util.isObject(object.points))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.points: object expected");
                            message.points = $root.org.webswing.directdraw.proto.PointsProto.fromObject(object.points, long + 1);
                        }
                        if (object.rectangle != null) {
                            if (!$util.isObject(object.rectangle))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.rectangle: object expected");
                            message.rectangle = $root.org.webswing.directdraw.proto.RectangleProto.fromObject(object.rectangle, long + 1);
                        }
                        if (object.ellipse != null) {
                            if (!$util.isObject(object.ellipse))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.ellipse: object expected");
                            message.ellipse = $root.org.webswing.directdraw.proto.EllipseProto.fromObject(object.ellipse, long + 1);
                        }
                        if (object.roundRectangle != null) {
                            if (!$util.isObject(object.roundRectangle))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.roundRectangle: object expected");
                            message.roundRectangle = $root.org.webswing.directdraw.proto.RoundRectangleProto.fromObject(object.roundRectangle, long + 1);
                        }
                        if (object.arc != null) {
                            if (!$util.isObject(object.arc))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.arc: object expected");
                            message.arc = $root.org.webswing.directdraw.proto.ArcProto.fromObject(object.arc, long + 1);
                        }
                        if (object.stroke != null) {
                            if (!$util.isObject(object.stroke))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.stroke: object expected");
                            message.stroke = $root.org.webswing.directdraw.proto.StrokeProto.fromObject(object.stroke, long + 1);
                        }
                        if (object.composite != null) {
                            if (!$util.isObject(object.composite))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.composite: object expected");
                            message.composite = $root.org.webswing.directdraw.proto.CompositeProto.fromObject(object.composite, long + 1);
                        }
                        if (object.texture != null) {
                            if (!$util.isObject(object.texture))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.texture: object expected");
                            message.texture = $root.org.webswing.directdraw.proto.TextureProto.fromObject(object.texture, long + 1);
                        }
                        if (object.glyph != null) {
                            if (!$util.isObject(object.glyph))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.glyph: object expected");
                            message.glyph = $root.org.webswing.directdraw.proto.GlyphProto.fromObject(object.glyph, long + 1);
                        }
                        if (object.combined != null) {
                            if (!$util.isObject(object.combined))
                                throw TypeError(".org.webswing.directdraw.proto.DrawConstantProto.combined: object expected");
                            message.combined = $root.org.webswing.directdraw.proto.CombinedProto.fromObject(object.combined, long + 1);
                        }
                        return message;
                    };

                    /**
                     * Creates a plain object from a DrawConstantProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @static
                     * @param {org.webswing.directdraw.proto.DrawConstantProto} message DrawConstantProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    DrawConstantProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.defaults) {
                            object.id = 0;
                            object.color = null;
                            object.image = null;
                            object.transform = null;
                            object.string = "";
                            object.path = null;
                            object.font = null;
                            object.linearGrad = null;
                            object.radialGrad = null;
                            object.points = null;
                            object.rectangle = null;
                            object.ellipse = null;
                            object.roundRectangle = null;
                            object.arc = null;
                            object.stroke = null;
                            object.composite = null;
                            object.texture = null;
                            object.glyph = null;
                            object.combined = null;
                        }
                        if (message.id != null && Object.hasOwnProperty.call(message, "id"))
                            object.id = message.id;
                        if (message.color != null && Object.hasOwnProperty.call(message, "color"))
                            object.color = $root.org.webswing.directdraw.proto.ColorProto.toObject(message.color, options, q + 1);
                        if (message.image != null && Object.hasOwnProperty.call(message, "image"))
                            object.image = $root.org.webswing.directdraw.proto.ImageProto.toObject(message.image, options, q + 1);
                        if (message.transform != null && Object.hasOwnProperty.call(message, "transform"))
                            object.transform = $root.org.webswing.directdraw.proto.TransformProto.toObject(message.transform, options, q + 1);
                        if (message.string != null && Object.hasOwnProperty.call(message, "string"))
                            object.string = message.string;
                        if (message.path != null && Object.hasOwnProperty.call(message, "path"))
                            object.path = $root.org.webswing.directdraw.proto.PathProto.toObject(message.path, options, q + 1);
                        if (message.font != null && Object.hasOwnProperty.call(message, "font"))
                            object.font = $root.org.webswing.directdraw.proto.FontProto.toObject(message.font, options, q + 1);
                        if (message.linearGrad != null && Object.hasOwnProperty.call(message, "linearGrad"))
                            object.linearGrad = $root.org.webswing.directdraw.proto.LinearGradientProto.toObject(message.linearGrad, options, q + 1);
                        if (message.radialGrad != null && Object.hasOwnProperty.call(message, "radialGrad"))
                            object.radialGrad = $root.org.webswing.directdraw.proto.RadialGradientProto.toObject(message.radialGrad, options, q + 1);
                        if (message.points != null && Object.hasOwnProperty.call(message, "points"))
                            object.points = $root.org.webswing.directdraw.proto.PointsProto.toObject(message.points, options, q + 1);
                        if (message.rectangle != null && Object.hasOwnProperty.call(message, "rectangle"))
                            object.rectangle = $root.org.webswing.directdraw.proto.RectangleProto.toObject(message.rectangle, options, q + 1);
                        if (message.ellipse != null && Object.hasOwnProperty.call(message, "ellipse"))
                            object.ellipse = $root.org.webswing.directdraw.proto.EllipseProto.toObject(message.ellipse, options, q + 1);
                        if (message.roundRectangle != null && Object.hasOwnProperty.call(message, "roundRectangle"))
                            object.roundRectangle = $root.org.webswing.directdraw.proto.RoundRectangleProto.toObject(message.roundRectangle, options, q + 1);
                        if (message.arc != null && Object.hasOwnProperty.call(message, "arc"))
                            object.arc = $root.org.webswing.directdraw.proto.ArcProto.toObject(message.arc, options, q + 1);
                        if (message.stroke != null && Object.hasOwnProperty.call(message, "stroke"))
                            object.stroke = $root.org.webswing.directdraw.proto.StrokeProto.toObject(message.stroke, options, q + 1);
                        if (message.composite != null && Object.hasOwnProperty.call(message, "composite"))
                            object.composite = $root.org.webswing.directdraw.proto.CompositeProto.toObject(message.composite, options, q + 1);
                        if (message.texture != null && Object.hasOwnProperty.call(message, "texture"))
                            object.texture = $root.org.webswing.directdraw.proto.TextureProto.toObject(message.texture, options, q + 1);
                        if (message.glyph != null && Object.hasOwnProperty.call(message, "glyph"))
                            object.glyph = $root.org.webswing.directdraw.proto.GlyphProto.toObject(message.glyph, options, q + 1);
                        if (message.combined != null && Object.hasOwnProperty.call(message, "combined"))
                            object.combined = $root.org.webswing.directdraw.proto.CombinedProto.toObject(message.combined, options, q + 1);
                        return object;
                    };

                    /**
                     * Converts this DrawConstantProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    DrawConstantProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for DrawConstantProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.DrawConstantProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    DrawConstantProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.DrawConstantProto";
                    };

                    return DrawConstantProto;
                })();

                proto.FontFaceProto = (function() {

                    /**
                     * Properties of a FontFaceProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IFontFaceProto
                     * @property {string} name FontFaceProto name
                     * @property {Uint8Array} font FontFaceProto font
                     * @property {string|null} [style] FontFaceProto style
                     */

                    /**
                     * Constructs a new FontFaceProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a FontFaceProto.
                     * @implements IFontFaceProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IFontFaceProto=} [properties] Properties to set
                     */
                    function FontFaceProto(properties) {
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * FontFaceProto name.
                     * @member {string} name
                     * @memberof org.webswing.directdraw.proto.FontFaceProto
                     * @instance
                     */
                    FontFaceProto.prototype.name = "";

                    /**
                     * FontFaceProto font.
                     * @member {Uint8Array} font
                     * @memberof org.webswing.directdraw.proto.FontFaceProto
                     * @instance
                     */
                    FontFaceProto.prototype.font = $util.newBuffer([]);

                    /**
                     * FontFaceProto style.
                     * @member {string} style
                     * @memberof org.webswing.directdraw.proto.FontFaceProto
                     * @instance
                     */
                    FontFaceProto.prototype.style = "";

                    /**
                     * Decodes a FontFaceProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.FontFaceProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.FontFaceProto} FontFaceProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    FontFaceProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.FontFaceProto();
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
                                    message.font = reader.bytes();
                                    break;
                                }
                            case 3: {
                                    message.style = reader.string();
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "name"))
                            throw $util.ProtocolError("missing required 'name'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "font"))
                            throw $util.ProtocolError("missing required 'font'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a FontFaceProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.FontFaceProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.FontFaceProto} FontFaceProto
                     */
                    FontFaceProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.FontFaceProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.FontFaceProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.FontFaceProto();
                        if (object.name != null)
                            message.name = String(object.name);
                        if (object.font != null)
                            if (typeof object.font === "string")
                                $util.base64.decode(object.font, message.font = $util.newBuffer($util.base64.length(object.font)), 0);
                            else if (object.font.length >= 0)
                                message.font = object.font;
                        if (object.style != null)
                            message.style = String(object.style);
                        return message;
                    };

                    /**
                     * Creates a plain object from a FontFaceProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.FontFaceProto
                     * @static
                     * @param {org.webswing.directdraw.proto.FontFaceProto} message FontFaceProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    FontFaceProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.defaults) {
                            object.name = "";
                            if (options.bytes === String)
                                object.font = "";
                            else {
                                object.font = [];
                                if (options.bytes !== Array)
                                    object.font = $util.newBuffer(object.font);
                            }
                            object.style = "";
                        }
                        if (message.name != null && Object.hasOwnProperty.call(message, "name"))
                            object.name = message.name;
                        if (message.font != null && Object.hasOwnProperty.call(message, "font"))
                            object.font = options.bytes === String ? $util.base64.encode(message.font, 0, message.font.length) : options.bytes === Array ? Array.prototype.slice.call(message.font) : message.font;
                        if (message.style != null && Object.hasOwnProperty.call(message, "style"))
                            object.style = message.style;
                        return object;
                    };

                    /**
                     * Converts this FontFaceProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.FontFaceProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    FontFaceProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for FontFaceProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.FontFaceProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    FontFaceProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.FontFaceProto";
                    };

                    return FontFaceProto;
                })();

                proto.ColorProto = (function() {

                    /**
                     * Properties of a ColorProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IColorProto
                     * @property {number} rgba ColorProto rgba
                     */

                    /**
                     * Constructs a new ColorProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a ColorProto.
                     * @implements IColorProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IColorProto=} [properties] Properties to set
                     */
                    function ColorProto(properties) {
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * ColorProto rgba.
                     * @member {number} rgba
                     * @memberof org.webswing.directdraw.proto.ColorProto
                     * @instance
                     */
                    ColorProto.prototype.rgba = 0;

                    /**
                     * Decodes a ColorProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.ColorProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.ColorProto} ColorProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    ColorProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.ColorProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.rgba = reader.fixed32();
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "rgba"))
                            throw $util.ProtocolError("missing required 'rgba'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a ColorProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.ColorProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.ColorProto} ColorProto
                     */
                    ColorProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.ColorProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.ColorProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.ColorProto();
                        if (object.rgba != null)
                            message.rgba = object.rgba >>> 0;
                        return message;
                    };

                    /**
                     * Creates a plain object from a ColorProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.ColorProto
                     * @static
                     * @param {org.webswing.directdraw.proto.ColorProto} message ColorProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    ColorProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.defaults)
                            object.rgba = 0;
                        if (message.rgba != null && Object.hasOwnProperty.call(message, "rgba"))
                            object.rgba = message.rgba;
                        return object;
                    };

                    /**
                     * Converts this ColorProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.ColorProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    ColorProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for ColorProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.ColorProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    ColorProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.ColorProto";
                    };

                    return ColorProto;
                })();

                proto.ImageProto = (function() {

                    /**
                     * Properties of an ImageProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IImageProto
                     * @property {Uint8Array} data ImageProto data
                     */

                    /**
                     * Constructs a new ImageProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents an ImageProto.
                     * @implements IImageProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IImageProto=} [properties] Properties to set
                     */
                    function ImageProto(properties) {
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * ImageProto data.
                     * @member {Uint8Array} data
                     * @memberof org.webswing.directdraw.proto.ImageProto
                     * @instance
                     */
                    ImageProto.prototype.data = $util.newBuffer([]);

                    /**
                     * Decodes an ImageProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.ImageProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.ImageProto} ImageProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    ImageProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.ImageProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.data = reader.bytes();
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "data"))
                            throw $util.ProtocolError("missing required 'data'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates an ImageProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.ImageProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.ImageProto} ImageProto
                     */
                    ImageProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.ImageProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.ImageProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.ImageProto();
                        if (object.data != null)
                            if (typeof object.data === "string")
                                $util.base64.decode(object.data, message.data = $util.newBuffer($util.base64.length(object.data)), 0);
                            else if (object.data.length >= 0)
                                message.data = object.data;
                        return message;
                    };

                    /**
                     * Creates a plain object from an ImageProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.ImageProto
                     * @static
                     * @param {org.webswing.directdraw.proto.ImageProto} message ImageProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    ImageProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.defaults)
                            if (options.bytes === String)
                                object.data = "";
                            else {
                                object.data = [];
                                if (options.bytes !== Array)
                                    object.data = $util.newBuffer(object.data);
                            }
                        if (message.data != null && Object.hasOwnProperty.call(message, "data"))
                            object.data = options.bytes === String ? $util.base64.encode(message.data, 0, message.data.length) : options.bytes === Array ? Array.prototype.slice.call(message.data) : message.data;
                        return object;
                    };

                    /**
                     * Converts this ImageProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.ImageProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    ImageProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for ImageProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.ImageProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    ImageProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.ImageProto";
                    };

                    return ImageProto;
                })();

                proto.TransformProto = (function() {

                    /**
                     * Properties of a TransformProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface ITransformProto
                     * @property {number|null} [m00] TransformProto m00
                     * @property {number|null} [m10] TransformProto m10
                     * @property {number|null} [m01] TransformProto m01
                     * @property {number|null} [m11] TransformProto m11
                     * @property {number|null} [m02] TransformProto m02
                     * @property {number|null} [m12] TransformProto m12
                     */

                    /**
                     * Constructs a new TransformProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a TransformProto.
                     * @implements ITransformProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.ITransformProto=} [properties] Properties to set
                     */
                    function TransformProto(properties) {
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * TransformProto m00.
                     * @member {number} m00
                     * @memberof org.webswing.directdraw.proto.TransformProto
                     * @instance
                     */
                    TransformProto.prototype.m00 = 1;

                    /**
                     * TransformProto m10.
                     * @member {number} m10
                     * @memberof org.webswing.directdraw.proto.TransformProto
                     * @instance
                     */
                    TransformProto.prototype.m10 = 0;

                    /**
                     * TransformProto m01.
                     * @member {number} m01
                     * @memberof org.webswing.directdraw.proto.TransformProto
                     * @instance
                     */
                    TransformProto.prototype.m01 = 0;

                    /**
                     * TransformProto m11.
                     * @member {number} m11
                     * @memberof org.webswing.directdraw.proto.TransformProto
                     * @instance
                     */
                    TransformProto.prototype.m11 = 1;

                    /**
                     * TransformProto m02.
                     * @member {number} m02
                     * @memberof org.webswing.directdraw.proto.TransformProto
                     * @instance
                     */
                    TransformProto.prototype.m02 = 0;

                    /**
                     * TransformProto m12.
                     * @member {number} m12
                     * @memberof org.webswing.directdraw.proto.TransformProto
                     * @instance
                     */
                    TransformProto.prototype.m12 = 0;

                    /**
                     * Decodes a TransformProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.TransformProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.TransformProto} TransformProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    TransformProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.TransformProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.m00 = reader.float();
                                    break;
                                }
                            case 2: {
                                    message.m10 = reader.float();
                                    break;
                                }
                            case 3: {
                                    message.m01 = reader.float();
                                    break;
                                }
                            case 4: {
                                    message.m11 = reader.float();
                                    break;
                                }
                            case 5: {
                                    message.m02 = reader.float();
                                    break;
                                }
                            case 6: {
                                    message.m12 = reader.float();
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
                     * Creates a TransformProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.TransformProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.TransformProto} TransformProto
                     */
                    TransformProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.TransformProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.TransformProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.TransformProto();
                        if (object.m00 != null)
                            message.m00 = Number(object.m00);
                        if (object.m10 != null)
                            message.m10 = Number(object.m10);
                        if (object.m01 != null)
                            message.m01 = Number(object.m01);
                        if (object.m11 != null)
                            message.m11 = Number(object.m11);
                        if (object.m02 != null)
                            message.m02 = Number(object.m02);
                        if (object.m12 != null)
                            message.m12 = Number(object.m12);
                        return message;
                    };

                    /**
                     * Creates a plain object from a TransformProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.TransformProto
                     * @static
                     * @param {org.webswing.directdraw.proto.TransformProto} message TransformProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    TransformProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.defaults) {
                            object.m00 = 1;
                            object.m10 = 0;
                            object.m01 = 0;
                            object.m11 = 1;
                            object.m02 = 0;
                            object.m12 = 0;
                        }
                        if (message.m00 != null && Object.hasOwnProperty.call(message, "m00"))
                            object.m00 = options.json && !isFinite(message.m00) ? String(message.m00) : message.m00;
                        if (message.m10 != null && Object.hasOwnProperty.call(message, "m10"))
                            object.m10 = options.json && !isFinite(message.m10) ? String(message.m10) : message.m10;
                        if (message.m01 != null && Object.hasOwnProperty.call(message, "m01"))
                            object.m01 = options.json && !isFinite(message.m01) ? String(message.m01) : message.m01;
                        if (message.m11 != null && Object.hasOwnProperty.call(message, "m11"))
                            object.m11 = options.json && !isFinite(message.m11) ? String(message.m11) : message.m11;
                        if (message.m02 != null && Object.hasOwnProperty.call(message, "m02"))
                            object.m02 = options.json && !isFinite(message.m02) ? String(message.m02) : message.m02;
                        if (message.m12 != null && Object.hasOwnProperty.call(message, "m12"))
                            object.m12 = options.json && !isFinite(message.m12) ? String(message.m12) : message.m12;
                        return object;
                    };

                    /**
                     * Converts this TransformProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.TransformProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    TransformProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for TransformProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.TransformProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    TransformProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.TransformProto";
                    };

                    return TransformProto;
                })();

                proto.CombinedProto = (function() {

                    /**
                     * Properties of a CombinedProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface ICombinedProto
                     * @property {Array.<number>|null} [ids] CombinedProto ids
                     */

                    /**
                     * Constructs a new CombinedProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a CombinedProto.
                     * @implements ICombinedProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.ICombinedProto=} [properties] Properties to set
                     */
                    function CombinedProto(properties) {
                        this.ids = [];
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * CombinedProto ids.
                     * @member {Array.<number>} ids
                     * @memberof org.webswing.directdraw.proto.CombinedProto
                     * @instance
                     */
                    CombinedProto.prototype.ids = $util.emptyArray;

                    /**
                     * Decodes a CombinedProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.CombinedProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.CombinedProto} CombinedProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    CombinedProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.CombinedProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    if (!(message.ids && message.ids.length))
                                        message.ids = [];
                                    if ((tag & 7) === 2) {
                                        var end2 = reader.uint32() + reader.pos;
                                        while (reader.pos < end2)
                                            message.ids.push(reader.uint32());
                                    } else
                                        message.ids.push(reader.uint32());
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
                     * Creates a CombinedProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.CombinedProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.CombinedProto} CombinedProto
                     */
                    CombinedProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.CombinedProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.CombinedProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.CombinedProto();
                        if (object.ids) {
                            if (!Array.isArray(object.ids))
                                throw TypeError(".org.webswing.directdraw.proto.CombinedProto.ids: array expected");
                            message.ids = [];
                            for (var i = 0; i < object.ids.length; ++i)
                                message.ids[i] = object.ids[i] >>> 0;
                        }
                        return message;
                    };

                    /**
                     * Creates a plain object from a CombinedProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.CombinedProto
                     * @static
                     * @param {org.webswing.directdraw.proto.CombinedProto} message CombinedProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    CombinedProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.arrays || options.defaults)
                            object.ids = [];
                        if (message.ids && message.ids.length) {
                            object.ids = [];
                            for (var j = 0; j < message.ids.length; ++j)
                                object.ids[j] = message.ids[j];
                        }
                        return object;
                    };

                    /**
                     * Converts this CombinedProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.CombinedProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    CombinedProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for CombinedProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.CombinedProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    CombinedProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.CombinedProto";
                    };

                    return CombinedProto;
                })();

                proto.GlyphProto = (function() {

                    /**
                     * Properties of a GlyphProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IGlyphProto
                     * @property {Uint8Array|null} [data] GlyphProto data
                     */

                    /**
                     * Constructs a new GlyphProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a GlyphProto.
                     * @implements IGlyphProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IGlyphProto=} [properties] Properties to set
                     */
                    function GlyphProto(properties) {
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * GlyphProto data.
                     * @member {Uint8Array} data
                     * @memberof org.webswing.directdraw.proto.GlyphProto
                     * @instance
                     */
                    GlyphProto.prototype.data = $util.newBuffer([]);

                    /**
                     * Decodes a GlyphProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.GlyphProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.GlyphProto} GlyphProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    GlyphProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.GlyphProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.data = reader.bytes();
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
                     * Creates a GlyphProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.GlyphProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.GlyphProto} GlyphProto
                     */
                    GlyphProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.GlyphProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.GlyphProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.GlyphProto();
                        if (object.data != null)
                            if (typeof object.data === "string")
                                $util.base64.decode(object.data, message.data = $util.newBuffer($util.base64.length(object.data)), 0);
                            else if (object.data.length >= 0)
                                message.data = object.data;
                        return message;
                    };

                    /**
                     * Creates a plain object from a GlyphProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.GlyphProto
                     * @static
                     * @param {org.webswing.directdraw.proto.GlyphProto} message GlyphProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    GlyphProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.defaults)
                            if (options.bytes === String)
                                object.data = "";
                            else {
                                object.data = [];
                                if (options.bytes !== Array)
                                    object.data = $util.newBuffer(object.data);
                            }
                        if (message.data != null && Object.hasOwnProperty.call(message, "data"))
                            object.data = options.bytes === String ? $util.base64.encode(message.data, 0, message.data.length) : options.bytes === Array ? Array.prototype.slice.call(message.data) : message.data;
                        return object;
                    };

                    /**
                     * Converts this GlyphProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.GlyphProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    GlyphProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for GlyphProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.GlyphProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    GlyphProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.GlyphProto";
                    };

                    return GlyphProto;
                })();

                proto.RectangleProto = (function() {

                    /**
                     * Properties of a RectangleProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IRectangleProto
                     * @property {number} x RectangleProto x
                     * @property {number} y RectangleProto y
                     * @property {number} w RectangleProto w
                     * @property {number} h RectangleProto h
                     */

                    /**
                     * Constructs a new RectangleProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a RectangleProto.
                     * @implements IRectangleProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IRectangleProto=} [properties] Properties to set
                     */
                    function RectangleProto(properties) {
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * RectangleProto x.
                     * @member {number} x
                     * @memberof org.webswing.directdraw.proto.RectangleProto
                     * @instance
                     */
                    RectangleProto.prototype.x = 0;

                    /**
                     * RectangleProto y.
                     * @member {number} y
                     * @memberof org.webswing.directdraw.proto.RectangleProto
                     * @instance
                     */
                    RectangleProto.prototype.y = 0;

                    /**
                     * RectangleProto w.
                     * @member {number} w
                     * @memberof org.webswing.directdraw.proto.RectangleProto
                     * @instance
                     */
                    RectangleProto.prototype.w = 0;

                    /**
                     * RectangleProto h.
                     * @member {number} h
                     * @memberof org.webswing.directdraw.proto.RectangleProto
                     * @instance
                     */
                    RectangleProto.prototype.h = 0;

                    /**
                     * Decodes a RectangleProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.RectangleProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.RectangleProto} RectangleProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    RectangleProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.RectangleProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.x = reader.float();
                                    break;
                                }
                            case 2: {
                                    message.y = reader.float();
                                    break;
                                }
                            case 3: {
                                    message.w = reader.float();
                                    break;
                                }
                            case 4: {
                                    message.h = reader.float();
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "x"))
                            throw $util.ProtocolError("missing required 'x'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "y"))
                            throw $util.ProtocolError("missing required 'y'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "w"))
                            throw $util.ProtocolError("missing required 'w'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "h"))
                            throw $util.ProtocolError("missing required 'h'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a RectangleProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.RectangleProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.RectangleProto} RectangleProto
                     */
                    RectangleProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.RectangleProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.RectangleProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.RectangleProto();
                        if (object.x != null)
                            message.x = Number(object.x);
                        if (object.y != null)
                            message.y = Number(object.y);
                        if (object.w != null)
                            message.w = Number(object.w);
                        if (object.h != null)
                            message.h = Number(object.h);
                        return message;
                    };

                    /**
                     * Creates a plain object from a RectangleProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.RectangleProto
                     * @static
                     * @param {org.webswing.directdraw.proto.RectangleProto} message RectangleProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    RectangleProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.defaults) {
                            object.x = 0;
                            object.y = 0;
                            object.w = 0;
                            object.h = 0;
                        }
                        if (message.x != null && Object.hasOwnProperty.call(message, "x"))
                            object.x = options.json && !isFinite(message.x) ? String(message.x) : message.x;
                        if (message.y != null && Object.hasOwnProperty.call(message, "y"))
                            object.y = options.json && !isFinite(message.y) ? String(message.y) : message.y;
                        if (message.w != null && Object.hasOwnProperty.call(message, "w"))
                            object.w = options.json && !isFinite(message.w) ? String(message.w) : message.w;
                        if (message.h != null && Object.hasOwnProperty.call(message, "h"))
                            object.h = options.json && !isFinite(message.h) ? String(message.h) : message.h;
                        return object;
                    };

                    /**
                     * Converts this RectangleProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.RectangleProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    RectangleProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for RectangleProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.RectangleProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    RectangleProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.RectangleProto";
                    };

                    return RectangleProto;
                })();

                proto.EllipseProto = (function() {

                    /**
                     * Properties of an EllipseProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IEllipseProto
                     * @property {number} x EllipseProto x
                     * @property {number} y EllipseProto y
                     * @property {number} w EllipseProto w
                     * @property {number} h EllipseProto h
                     */

                    /**
                     * Constructs a new EllipseProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents an EllipseProto.
                     * @implements IEllipseProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IEllipseProto=} [properties] Properties to set
                     */
                    function EllipseProto(properties) {
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * EllipseProto x.
                     * @member {number} x
                     * @memberof org.webswing.directdraw.proto.EllipseProto
                     * @instance
                     */
                    EllipseProto.prototype.x = 0;

                    /**
                     * EllipseProto y.
                     * @member {number} y
                     * @memberof org.webswing.directdraw.proto.EllipseProto
                     * @instance
                     */
                    EllipseProto.prototype.y = 0;

                    /**
                     * EllipseProto w.
                     * @member {number} w
                     * @memberof org.webswing.directdraw.proto.EllipseProto
                     * @instance
                     */
                    EllipseProto.prototype.w = 0;

                    /**
                     * EllipseProto h.
                     * @member {number} h
                     * @memberof org.webswing.directdraw.proto.EllipseProto
                     * @instance
                     */
                    EllipseProto.prototype.h = 0;

                    /**
                     * Decodes an EllipseProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.EllipseProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.EllipseProto} EllipseProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    EllipseProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.EllipseProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.x = reader.float();
                                    break;
                                }
                            case 2: {
                                    message.y = reader.float();
                                    break;
                                }
                            case 3: {
                                    message.w = reader.float();
                                    break;
                                }
                            case 4: {
                                    message.h = reader.float();
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "x"))
                            throw $util.ProtocolError("missing required 'x'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "y"))
                            throw $util.ProtocolError("missing required 'y'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "w"))
                            throw $util.ProtocolError("missing required 'w'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "h"))
                            throw $util.ProtocolError("missing required 'h'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates an EllipseProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.EllipseProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.EllipseProto} EllipseProto
                     */
                    EllipseProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.EllipseProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.EllipseProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.EllipseProto();
                        if (object.x != null)
                            message.x = Number(object.x);
                        if (object.y != null)
                            message.y = Number(object.y);
                        if (object.w != null)
                            message.w = Number(object.w);
                        if (object.h != null)
                            message.h = Number(object.h);
                        return message;
                    };

                    /**
                     * Creates a plain object from an EllipseProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.EllipseProto
                     * @static
                     * @param {org.webswing.directdraw.proto.EllipseProto} message EllipseProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    EllipseProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.defaults) {
                            object.x = 0;
                            object.y = 0;
                            object.w = 0;
                            object.h = 0;
                        }
                        if (message.x != null && Object.hasOwnProperty.call(message, "x"))
                            object.x = options.json && !isFinite(message.x) ? String(message.x) : message.x;
                        if (message.y != null && Object.hasOwnProperty.call(message, "y"))
                            object.y = options.json && !isFinite(message.y) ? String(message.y) : message.y;
                        if (message.w != null && Object.hasOwnProperty.call(message, "w"))
                            object.w = options.json && !isFinite(message.w) ? String(message.w) : message.w;
                        if (message.h != null && Object.hasOwnProperty.call(message, "h"))
                            object.h = options.json && !isFinite(message.h) ? String(message.h) : message.h;
                        return object;
                    };

                    /**
                     * Converts this EllipseProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.EllipseProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    EllipseProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for EllipseProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.EllipseProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    EllipseProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.EllipseProto";
                    };

                    return EllipseProto;
                })();

                proto.RoundRectangleProto = (function() {

                    /**
                     * Properties of a RoundRectangleProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IRoundRectangleProto
                     * @property {number} x RoundRectangleProto x
                     * @property {number} y RoundRectangleProto y
                     * @property {number} w RoundRectangleProto w
                     * @property {number} h RoundRectangleProto h
                     * @property {number|null} [arcW] RoundRectangleProto arcW
                     * @property {number|null} [arcH] RoundRectangleProto arcH
                     */

                    /**
                     * Constructs a new RoundRectangleProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a RoundRectangleProto.
                     * @implements IRoundRectangleProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IRoundRectangleProto=} [properties] Properties to set
                     */
                    function RoundRectangleProto(properties) {
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * RoundRectangleProto x.
                     * @member {number} x
                     * @memberof org.webswing.directdraw.proto.RoundRectangleProto
                     * @instance
                     */
                    RoundRectangleProto.prototype.x = 0;

                    /**
                     * RoundRectangleProto y.
                     * @member {number} y
                     * @memberof org.webswing.directdraw.proto.RoundRectangleProto
                     * @instance
                     */
                    RoundRectangleProto.prototype.y = 0;

                    /**
                     * RoundRectangleProto w.
                     * @member {number} w
                     * @memberof org.webswing.directdraw.proto.RoundRectangleProto
                     * @instance
                     */
                    RoundRectangleProto.prototype.w = 0;

                    /**
                     * RoundRectangleProto h.
                     * @member {number} h
                     * @memberof org.webswing.directdraw.proto.RoundRectangleProto
                     * @instance
                     */
                    RoundRectangleProto.prototype.h = 0;

                    /**
                     * RoundRectangleProto arcW.
                     * @member {number} arcW
                     * @memberof org.webswing.directdraw.proto.RoundRectangleProto
                     * @instance
                     */
                    RoundRectangleProto.prototype.arcW = 0;

                    /**
                     * RoundRectangleProto arcH.
                     * @member {number} arcH
                     * @memberof org.webswing.directdraw.proto.RoundRectangleProto
                     * @instance
                     */
                    RoundRectangleProto.prototype.arcH = 0;

                    /**
                     * Decodes a RoundRectangleProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.RoundRectangleProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.RoundRectangleProto} RoundRectangleProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    RoundRectangleProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.RoundRectangleProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.x = reader.float();
                                    break;
                                }
                            case 2: {
                                    message.y = reader.float();
                                    break;
                                }
                            case 3: {
                                    message.w = reader.float();
                                    break;
                                }
                            case 4: {
                                    message.h = reader.float();
                                    break;
                                }
                            case 5: {
                                    message.arcW = reader.float();
                                    break;
                                }
                            case 6: {
                                    message.arcH = reader.float();
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "x"))
                            throw $util.ProtocolError("missing required 'x'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "y"))
                            throw $util.ProtocolError("missing required 'y'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "w"))
                            throw $util.ProtocolError("missing required 'w'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "h"))
                            throw $util.ProtocolError("missing required 'h'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a RoundRectangleProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.RoundRectangleProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.RoundRectangleProto} RoundRectangleProto
                     */
                    RoundRectangleProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.RoundRectangleProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.RoundRectangleProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.RoundRectangleProto();
                        if (object.x != null)
                            message.x = Number(object.x);
                        if (object.y != null)
                            message.y = Number(object.y);
                        if (object.w != null)
                            message.w = Number(object.w);
                        if (object.h != null)
                            message.h = Number(object.h);
                        if (object.arcW != null)
                            message.arcW = Number(object.arcW);
                        if (object.arcH != null)
                            message.arcH = Number(object.arcH);
                        return message;
                    };

                    /**
                     * Creates a plain object from a RoundRectangleProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.RoundRectangleProto
                     * @static
                     * @param {org.webswing.directdraw.proto.RoundRectangleProto} message RoundRectangleProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    RoundRectangleProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.defaults) {
                            object.x = 0;
                            object.y = 0;
                            object.w = 0;
                            object.h = 0;
                            object.arcW = 0;
                            object.arcH = 0;
                        }
                        if (message.x != null && Object.hasOwnProperty.call(message, "x"))
                            object.x = options.json && !isFinite(message.x) ? String(message.x) : message.x;
                        if (message.y != null && Object.hasOwnProperty.call(message, "y"))
                            object.y = options.json && !isFinite(message.y) ? String(message.y) : message.y;
                        if (message.w != null && Object.hasOwnProperty.call(message, "w"))
                            object.w = options.json && !isFinite(message.w) ? String(message.w) : message.w;
                        if (message.h != null && Object.hasOwnProperty.call(message, "h"))
                            object.h = options.json && !isFinite(message.h) ? String(message.h) : message.h;
                        if (message.arcW != null && Object.hasOwnProperty.call(message, "arcW"))
                            object.arcW = options.json && !isFinite(message.arcW) ? String(message.arcW) : message.arcW;
                        if (message.arcH != null && Object.hasOwnProperty.call(message, "arcH"))
                            object.arcH = options.json && !isFinite(message.arcH) ? String(message.arcH) : message.arcH;
                        return object;
                    };

                    /**
                     * Converts this RoundRectangleProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.RoundRectangleProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    RoundRectangleProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for RoundRectangleProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.RoundRectangleProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    RoundRectangleProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.RoundRectangleProto";
                    };

                    return RoundRectangleProto;
                })();

                proto.ArcProto = (function() {

                    /**
                     * Properties of an ArcProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IArcProto
                     * @property {number} x ArcProto x
                     * @property {number} y ArcProto y
                     * @property {number} w ArcProto w
                     * @property {number} h ArcProto h
                     * @property {number|null} [start] ArcProto start
                     * @property {number|null} [extent] ArcProto extent
                     * @property {org.webswing.directdraw.proto.ArcProto.ArcTypeProto|null} [type] ArcProto type
                     */

                    /**
                     * Constructs a new ArcProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents an ArcProto.
                     * @implements IArcProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IArcProto=} [properties] Properties to set
                     */
                    function ArcProto(properties) {
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * ArcProto x.
                     * @member {number} x
                     * @memberof org.webswing.directdraw.proto.ArcProto
                     * @instance
                     */
                    ArcProto.prototype.x = 0;

                    /**
                     * ArcProto y.
                     * @member {number} y
                     * @memberof org.webswing.directdraw.proto.ArcProto
                     * @instance
                     */
                    ArcProto.prototype.y = 0;

                    /**
                     * ArcProto w.
                     * @member {number} w
                     * @memberof org.webswing.directdraw.proto.ArcProto
                     * @instance
                     */
                    ArcProto.prototype.w = 0;

                    /**
                     * ArcProto h.
                     * @member {number} h
                     * @memberof org.webswing.directdraw.proto.ArcProto
                     * @instance
                     */
                    ArcProto.prototype.h = 0;

                    /**
                     * ArcProto start.
                     * @member {number} start
                     * @memberof org.webswing.directdraw.proto.ArcProto
                     * @instance
                     */
                    ArcProto.prototype.start = 0;

                    /**
                     * ArcProto extent.
                     * @member {number} extent
                     * @memberof org.webswing.directdraw.proto.ArcProto
                     * @instance
                     */
                    ArcProto.prototype.extent = 0;

                    /**
                     * ArcProto type.
                     * @member {org.webswing.directdraw.proto.ArcProto.ArcTypeProto} type
                     * @memberof org.webswing.directdraw.proto.ArcProto
                     * @instance
                     */
                    ArcProto.prototype.type = 0;

                    /**
                     * Decodes an ArcProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.ArcProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.ArcProto} ArcProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    ArcProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.ArcProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.x = reader.float();
                                    break;
                                }
                            case 2: {
                                    message.y = reader.float();
                                    break;
                                }
                            case 3: {
                                    message.w = reader.float();
                                    break;
                                }
                            case 4: {
                                    message.h = reader.float();
                                    break;
                                }
                            case 5: {
                                    message.start = reader.float();
                                    break;
                                }
                            case 6: {
                                    message.extent = reader.float();
                                    break;
                                }
                            case 7: {
                                    message.type = reader.int32();
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "x"))
                            throw $util.ProtocolError("missing required 'x'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "y"))
                            throw $util.ProtocolError("missing required 'y'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "w"))
                            throw $util.ProtocolError("missing required 'w'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "h"))
                            throw $util.ProtocolError("missing required 'h'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates an ArcProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.ArcProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.ArcProto} ArcProto
                     */
                    ArcProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.ArcProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.ArcProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.ArcProto();
                        if (object.x != null)
                            message.x = Number(object.x);
                        if (object.y != null)
                            message.y = Number(object.y);
                        if (object.w != null)
                            message.w = Number(object.w);
                        if (object.h != null)
                            message.h = Number(object.h);
                        if (object.start != null)
                            message.start = Number(object.start);
                        if (object.extent != null)
                            message.extent = Number(object.extent);
                        switch (object.type) {
                        default:
                            if (typeof object.type === "number") {
                                message.type = object.type;
                                break;
                            }
                            break;
                        case "OPEN":
                        case 0:
                            message.type = 0;
                            break;
                        case "CHORD":
                        case 1:
                            message.type = 1;
                            break;
                        case "PIE":
                        case 2:
                            message.type = 2;
                            break;
                        }
                        return message;
                    };

                    /**
                     * Creates a plain object from an ArcProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.ArcProto
                     * @static
                     * @param {org.webswing.directdraw.proto.ArcProto} message ArcProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    ArcProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.defaults) {
                            object.x = 0;
                            object.y = 0;
                            object.w = 0;
                            object.h = 0;
                            object.start = 0;
                            object.extent = 0;
                            object.type = options.enums === String ? "OPEN" : 0;
                        }
                        if (message.x != null && Object.hasOwnProperty.call(message, "x"))
                            object.x = options.json && !isFinite(message.x) ? String(message.x) : message.x;
                        if (message.y != null && Object.hasOwnProperty.call(message, "y"))
                            object.y = options.json && !isFinite(message.y) ? String(message.y) : message.y;
                        if (message.w != null && Object.hasOwnProperty.call(message, "w"))
                            object.w = options.json && !isFinite(message.w) ? String(message.w) : message.w;
                        if (message.h != null && Object.hasOwnProperty.call(message, "h"))
                            object.h = options.json && !isFinite(message.h) ? String(message.h) : message.h;
                        if (message.start != null && Object.hasOwnProperty.call(message, "start"))
                            object.start = options.json && !isFinite(message.start) ? String(message.start) : message.start;
                        if (message.extent != null && Object.hasOwnProperty.call(message, "extent"))
                            object.extent = options.json && !isFinite(message.extent) ? String(message.extent) : message.extent;
                        if (message.type != null && Object.hasOwnProperty.call(message, "type"))
                            object.type = options.enums === String ? $root.org.webswing.directdraw.proto.ArcProto.ArcTypeProto[message.type] === undefined ? message.type : $root.org.webswing.directdraw.proto.ArcProto.ArcTypeProto[message.type] : message.type;
                        return object;
                    };

                    /**
                     * Converts this ArcProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.ArcProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    ArcProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for ArcProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.ArcProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    ArcProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.ArcProto";
                    };

                    /**
                     * ArcTypeProto enum.
                     * @name org.webswing.directdraw.proto.ArcProto.ArcTypeProto
                     * @enum {number}
                     * @property {number} OPEN=0 OPEN value
                     * @property {number} CHORD=1 CHORD value
                     * @property {number} PIE=2 PIE value
                     */
                    ArcProto.ArcTypeProto = (function() {
                        var valuesById = {}, values = Object.create(valuesById);
                        values[valuesById[0] = "OPEN"] = 0;
                        values[valuesById[1] = "CHORD"] = 1;
                        values[valuesById[2] = "PIE"] = 2;
                        return values;
                    })();

                    return ArcProto;
                })();

                proto.PathProto = (function() {

                    /**
                     * Properties of a PathProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IPathProto
                     * @property {boolean} windingOdd PathProto windingOdd
                     * @property {Array.<org.webswing.directdraw.proto.PathProto.SegmentTypeProto>|null} [type] PathProto type
                     * @property {Array.<number>|null} [points] PathProto points
                     */

                    /**
                     * Constructs a new PathProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a PathProto.
                     * @implements IPathProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IPathProto=} [properties] Properties to set
                     */
                    function PathProto(properties) {
                        this.type = [];
                        this.points = [];
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * PathProto windingOdd.
                     * @member {boolean} windingOdd
                     * @memberof org.webswing.directdraw.proto.PathProto
                     * @instance
                     */
                    PathProto.prototype.windingOdd = false;

                    /**
                     * PathProto type.
                     * @member {Array.<org.webswing.directdraw.proto.PathProto.SegmentTypeProto>} type
                     * @memberof org.webswing.directdraw.proto.PathProto
                     * @instance
                     */
                    PathProto.prototype.type = $util.emptyArray;

                    /**
                     * PathProto points.
                     * @member {Array.<number>} points
                     * @memberof org.webswing.directdraw.proto.PathProto
                     * @instance
                     */
                    PathProto.prototype.points = $util.emptyArray;

                    /**
                     * Decodes a PathProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.PathProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.PathProto} PathProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    PathProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.PathProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.windingOdd = reader.bool();
                                    break;
                                }
                            case 2: {
                                    if (!(message.type && message.type.length))
                                        message.type = [];
                                    if ((tag & 7) === 2) {
                                        var end2 = reader.uint32() + reader.pos;
                                        while (reader.pos < end2)
                                            message.type.push(reader.int32());
                                    } else
                                        message.type.push(reader.int32());
                                    break;
                                }
                            case 3: {
                                    if (!(message.points && message.points.length))
                                        message.points = [];
                                    if ((tag & 7) === 2) {
                                        var end2 = reader.uint32() + reader.pos;
                                        while (reader.pos < end2)
                                            message.points.push(reader.float());
                                    } else
                                        message.points.push(reader.float());
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "windingOdd"))
                            throw $util.ProtocolError("missing required 'windingOdd'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a PathProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.PathProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.PathProto} PathProto
                     */
                    PathProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.PathProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.PathProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.PathProto();
                        if (object.windingOdd != null)
                            message.windingOdd = Boolean(object.windingOdd);
                        if (object.type) {
                            if (!Array.isArray(object.type))
                                throw TypeError(".org.webswing.directdraw.proto.PathProto.type: array expected");
                            message.type = [];
                            for (var i = 0; i < object.type.length; ++i)
                                switch (object.type[i]) {
                                default:
                                    if (typeof object.type[i] === "number") {
                                        message.type[i] = object.type[i];
                                        break;
                                    }
                                case "MOVE":
                                case 0:
                                    message.type[i] = 0;
                                    break;
                                case "LINE":
                                case 1:
                                    message.type[i] = 1;
                                    break;
                                case "QUAD":
                                case 2:
                                    message.type[i] = 2;
                                    break;
                                case "CUBIC":
                                case 3:
                                    message.type[i] = 3;
                                    break;
                                case "CLOSE":
                                case 4:
                                    message.type[i] = 4;
                                    break;
                                }
                        }
                        if (object.points) {
                            if (!Array.isArray(object.points))
                                throw TypeError(".org.webswing.directdraw.proto.PathProto.points: array expected");
                            message.points = [];
                            for (var i = 0; i < object.points.length; ++i)
                                message.points[i] = Number(object.points[i]);
                        }
                        return message;
                    };

                    /**
                     * Creates a plain object from a PathProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.PathProto
                     * @static
                     * @param {org.webswing.directdraw.proto.PathProto} message PathProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    PathProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.arrays || options.defaults) {
                            object.type = [];
                            object.points = [];
                        }
                        if (options.defaults)
                            object.windingOdd = false;
                        if (message.windingOdd != null && Object.hasOwnProperty.call(message, "windingOdd"))
                            object.windingOdd = message.windingOdd;
                        if (message.type && message.type.length) {
                            object.type = [];
                            for (var j = 0; j < message.type.length; ++j)
                                object.type[j] = options.enums === String ? $root.org.webswing.directdraw.proto.PathProto.SegmentTypeProto[message.type[j]] === undefined ? message.type[j] : $root.org.webswing.directdraw.proto.PathProto.SegmentTypeProto[message.type[j]] : message.type[j];
                        }
                        if (message.points && message.points.length) {
                            object.points = [];
                            for (var j = 0; j < message.points.length; ++j)
                                object.points[j] = options.json && !isFinite(message.points[j]) ? String(message.points[j]) : message.points[j];
                        }
                        return object;
                    };

                    /**
                     * Converts this PathProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.PathProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    PathProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for PathProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.PathProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    PathProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.PathProto";
                    };

                    /**
                     * SegmentTypeProto enum.
                     * @name org.webswing.directdraw.proto.PathProto.SegmentTypeProto
                     * @enum {number}
                     * @property {number} MOVE=0 MOVE value
                     * @property {number} LINE=1 LINE value
                     * @property {number} QUAD=2 QUAD value
                     * @property {number} CUBIC=3 CUBIC value
                     * @property {number} CLOSE=4 CLOSE value
                     */
                    PathProto.SegmentTypeProto = (function() {
                        var valuesById = {}, values = Object.create(valuesById);
                        values[valuesById[0] = "MOVE"] = 0;
                        values[valuesById[1] = "LINE"] = 1;
                        values[valuesById[2] = "QUAD"] = 2;
                        values[valuesById[3] = "CUBIC"] = 3;
                        values[valuesById[4] = "CLOSE"] = 4;
                        return values;
                    })();

                    return PathProto;
                })();

                proto.FontProto = (function() {

                    /**
                     * Properties of a FontProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IFontProto
                     * @property {string} family FontProto family
                     * @property {org.webswing.directdraw.proto.FontProto.StyleProto|null} [style] FontProto style
                     * @property {number|null} [size] FontProto size
                     * @property {org.webswing.directdraw.proto.ITransformProto|null} [transform] FontProto transform
                     * @property {boolean|null} [fontProvided] FontProto fontProvided
                     */

                    /**
                     * Constructs a new FontProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a FontProto.
                     * @implements IFontProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IFontProto=} [properties] Properties to set
                     */
                    function FontProto(properties) {
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * FontProto family.
                     * @member {string} family
                     * @memberof org.webswing.directdraw.proto.FontProto
                     * @instance
                     */
                    FontProto.prototype.family = "";

                    /**
                     * FontProto style.
                     * @member {org.webswing.directdraw.proto.FontProto.StyleProto} style
                     * @memberof org.webswing.directdraw.proto.FontProto
                     * @instance
                     */
                    FontProto.prototype.style = 0;

                    /**
                     * FontProto size.
                     * @member {number} size
                     * @memberof org.webswing.directdraw.proto.FontProto
                     * @instance
                     */
                    FontProto.prototype.size = 0;

                    /**
                     * FontProto transform.
                     * @member {org.webswing.directdraw.proto.ITransformProto|null|undefined} transform
                     * @memberof org.webswing.directdraw.proto.FontProto
                     * @instance
                     */
                    FontProto.prototype.transform = null;

                    /**
                     * FontProto fontProvided.
                     * @member {boolean} fontProvided
                     * @memberof org.webswing.directdraw.proto.FontProto
                     * @instance
                     */
                    FontProto.prototype.fontProvided = false;

                    /**
                     * Decodes a FontProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.FontProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.FontProto} FontProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    FontProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.FontProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.family = reader.string();
                                    break;
                                }
                            case 2: {
                                    message.style = reader.int32();
                                    break;
                                }
                            case 3: {
                                    message.size = reader.uint32();
                                    break;
                                }
                            case 4: {
                                    message.transform = $root.org.webswing.directdraw.proto.TransformProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 5: {
                                    message.fontProvided = reader.bool();
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "family"))
                            throw $util.ProtocolError("missing required 'family'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a FontProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.FontProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.FontProto} FontProto
                     */
                    FontProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.FontProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.FontProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.FontProto();
                        if (object.family != null)
                            message.family = String(object.family);
                        switch (object.style) {
                        default:
                            if (typeof object.style === "number") {
                                message.style = object.style;
                                break;
                            }
                            break;
                        case "NORMAL":
                        case 0:
                            message.style = 0;
                            break;
                        case "OBLIQUE":
                        case 1:
                            message.style = 1;
                            break;
                        case "ITALIC":
                        case 2:
                            message.style = 2;
                            break;
                        case "BOLDANDITALIC":
                        case 3:
                            message.style = 3;
                            break;
                        }
                        if (object.size != null)
                            message.size = object.size >>> 0;
                        if (object.transform != null) {
                            if (!$util.isObject(object.transform))
                                throw TypeError(".org.webswing.directdraw.proto.FontProto.transform: object expected");
                            message.transform = $root.org.webswing.directdraw.proto.TransformProto.fromObject(object.transform, long + 1);
                        }
                        if (object.fontProvided != null)
                            message.fontProvided = Boolean(object.fontProvided);
                        return message;
                    };

                    /**
                     * Creates a plain object from a FontProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.FontProto
                     * @static
                     * @param {org.webswing.directdraw.proto.FontProto} message FontProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    FontProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.defaults) {
                            object.family = "";
                            object.style = options.enums === String ? "NORMAL" : 0;
                            object.size = 0;
                            object.transform = null;
                            object.fontProvided = false;
                        }
                        if (message.family != null && Object.hasOwnProperty.call(message, "family"))
                            object.family = message.family;
                        if (message.style != null && Object.hasOwnProperty.call(message, "style"))
                            object.style = options.enums === String ? $root.org.webswing.directdraw.proto.FontProto.StyleProto[message.style] === undefined ? message.style : $root.org.webswing.directdraw.proto.FontProto.StyleProto[message.style] : message.style;
                        if (message.size != null && Object.hasOwnProperty.call(message, "size"))
                            object.size = message.size;
                        if (message.transform != null && Object.hasOwnProperty.call(message, "transform"))
                            object.transform = $root.org.webswing.directdraw.proto.TransformProto.toObject(message.transform, options, q + 1);
                        if (message.fontProvided != null && Object.hasOwnProperty.call(message, "fontProvided"))
                            object.fontProvided = message.fontProvided;
                        return object;
                    };

                    /**
                     * Converts this FontProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.FontProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    FontProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for FontProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.FontProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    FontProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.FontProto";
                    };

                    /**
                     * StyleProto enum.
                     * @name org.webswing.directdraw.proto.FontProto.StyleProto
                     * @enum {number}
                     * @property {number} NORMAL=0 NORMAL value
                     * @property {number} OBLIQUE=1 OBLIQUE value
                     * @property {number} ITALIC=2 ITALIC value
                     * @property {number} BOLDANDITALIC=3 BOLDANDITALIC value
                     */
                    FontProto.StyleProto = (function() {
                        var valuesById = {}, values = Object.create(valuesById);
                        values[valuesById[0] = "NORMAL"] = 0;
                        values[valuesById[1] = "OBLIQUE"] = 1;
                        values[valuesById[2] = "ITALIC"] = 2;
                        values[valuesById[3] = "BOLDANDITALIC"] = 3;
                        return values;
                    })();

                    return FontProto;
                })();

                proto.StrokeProto = (function() {

                    /**
                     * Properties of a StrokeProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IStrokeProto
                     * @property {number} width StrokeProto width
                     * @property {number|null} [miterLimit] StrokeProto miterLimit
                     * @property {org.webswing.directdraw.proto.StrokeProto.StrokeJoinProto|null} [join] StrokeProto join
                     * @property {org.webswing.directdraw.proto.StrokeProto.StrokeCapProto|null} [cap] StrokeProto cap
                     * @property {Array.<number>|null} [dash] StrokeProto dash
                     * @property {number|null} [dashOffset] StrokeProto dashOffset
                     */

                    /**
                     * Constructs a new StrokeProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a StrokeProto.
                     * @implements IStrokeProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IStrokeProto=} [properties] Properties to set
                     */
                    function StrokeProto(properties) {
                        this.dash = [];
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * StrokeProto width.
                     * @member {number} width
                     * @memberof org.webswing.directdraw.proto.StrokeProto
                     * @instance
                     */
                    StrokeProto.prototype.width = 0;

                    /**
                     * StrokeProto miterLimit.
                     * @member {number} miterLimit
                     * @memberof org.webswing.directdraw.proto.StrokeProto
                     * @instance
                     */
                    StrokeProto.prototype.miterLimit = 0;

                    /**
                     * StrokeProto join.
                     * @member {org.webswing.directdraw.proto.StrokeProto.StrokeJoinProto} join
                     * @memberof org.webswing.directdraw.proto.StrokeProto
                     * @instance
                     */
                    StrokeProto.prototype.join = 0;

                    /**
                     * StrokeProto cap.
                     * @member {org.webswing.directdraw.proto.StrokeProto.StrokeCapProto} cap
                     * @memberof org.webswing.directdraw.proto.StrokeProto
                     * @instance
                     */
                    StrokeProto.prototype.cap = 0;

                    /**
                     * StrokeProto dash.
                     * @member {Array.<number>} dash
                     * @memberof org.webswing.directdraw.proto.StrokeProto
                     * @instance
                     */
                    StrokeProto.prototype.dash = $util.emptyArray;

                    /**
                     * StrokeProto dashOffset.
                     * @member {number} dashOffset
                     * @memberof org.webswing.directdraw.proto.StrokeProto
                     * @instance
                     */
                    StrokeProto.prototype.dashOffset = 0;

                    /**
                     * Decodes a StrokeProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.StrokeProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.StrokeProto} StrokeProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    StrokeProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.StrokeProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.width = reader.float();
                                    break;
                                }
                            case 2: {
                                    message.miterLimit = reader.float();
                                    break;
                                }
                            case 3: {
                                    message.join = reader.int32();
                                    break;
                                }
                            case 4: {
                                    message.cap = reader.int32();
                                    break;
                                }
                            case 5: {
                                    if (!(message.dash && message.dash.length))
                                        message.dash = [];
                                    if ((tag & 7) === 2) {
                                        var end2 = reader.uint32() + reader.pos;
                                        while (reader.pos < end2)
                                            message.dash.push(reader.float());
                                    } else
                                        message.dash.push(reader.float());
                                    break;
                                }
                            case 6: {
                                    message.dashOffset = reader.float();
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "width"))
                            throw $util.ProtocolError("missing required 'width'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a StrokeProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.StrokeProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.StrokeProto} StrokeProto
                     */
                    StrokeProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.StrokeProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.StrokeProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.StrokeProto();
                        if (object.width != null)
                            message.width = Number(object.width);
                        if (object.miterLimit != null)
                            message.miterLimit = Number(object.miterLimit);
                        switch (object.join) {
                        default:
                            if (typeof object.join === "number") {
                                message.join = object.join;
                                break;
                            }
                            break;
                        case "JOIN_MITER":
                        case 0:
                            message.join = 0;
                            break;
                        case "JOIN_ROUND":
                        case 1:
                            message.join = 1;
                            break;
                        case "JOIN_BEVEL":
                        case 2:
                            message.join = 2;
                            break;
                        }
                        switch (object.cap) {
                        default:
                            if (typeof object.cap === "number") {
                                message.cap = object.cap;
                                break;
                            }
                            break;
                        case "CAP_BUTT":
                        case 0:
                            message.cap = 0;
                            break;
                        case "CAP_ROUND":
                        case 1:
                            message.cap = 1;
                            break;
                        case "CAP_SQUARE":
                        case 2:
                            message.cap = 2;
                            break;
                        }
                        if (object.dash) {
                            if (!Array.isArray(object.dash))
                                throw TypeError(".org.webswing.directdraw.proto.StrokeProto.dash: array expected");
                            message.dash = [];
                            for (var i = 0; i < object.dash.length; ++i)
                                message.dash[i] = Number(object.dash[i]);
                        }
                        if (object.dashOffset != null)
                            message.dashOffset = Number(object.dashOffset);
                        return message;
                    };

                    /**
                     * Creates a plain object from a StrokeProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.StrokeProto
                     * @static
                     * @param {org.webswing.directdraw.proto.StrokeProto} message StrokeProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    StrokeProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.arrays || options.defaults)
                            object.dash = [];
                        if (options.defaults) {
                            object.width = 0;
                            object.miterLimit = 0;
                            object.join = options.enums === String ? "JOIN_MITER" : 0;
                            object.cap = options.enums === String ? "CAP_BUTT" : 0;
                            object.dashOffset = 0;
                        }
                        if (message.width != null && Object.hasOwnProperty.call(message, "width"))
                            object.width = options.json && !isFinite(message.width) ? String(message.width) : message.width;
                        if (message.miterLimit != null && Object.hasOwnProperty.call(message, "miterLimit"))
                            object.miterLimit = options.json && !isFinite(message.miterLimit) ? String(message.miterLimit) : message.miterLimit;
                        if (message.join != null && Object.hasOwnProperty.call(message, "join"))
                            object.join = options.enums === String ? $root.org.webswing.directdraw.proto.StrokeProto.StrokeJoinProto[message.join] === undefined ? message.join : $root.org.webswing.directdraw.proto.StrokeProto.StrokeJoinProto[message.join] : message.join;
                        if (message.cap != null && Object.hasOwnProperty.call(message, "cap"))
                            object.cap = options.enums === String ? $root.org.webswing.directdraw.proto.StrokeProto.StrokeCapProto[message.cap] === undefined ? message.cap : $root.org.webswing.directdraw.proto.StrokeProto.StrokeCapProto[message.cap] : message.cap;
                        if (message.dash && message.dash.length) {
                            object.dash = [];
                            for (var j = 0; j < message.dash.length; ++j)
                                object.dash[j] = options.json && !isFinite(message.dash[j]) ? String(message.dash[j]) : message.dash[j];
                        }
                        if (message.dashOffset != null && Object.hasOwnProperty.call(message, "dashOffset"))
                            object.dashOffset = options.json && !isFinite(message.dashOffset) ? String(message.dashOffset) : message.dashOffset;
                        return object;
                    };

                    /**
                     * Converts this StrokeProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.StrokeProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    StrokeProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for StrokeProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.StrokeProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    StrokeProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.StrokeProto";
                    };

                    /**
                     * StrokeJoinProto enum.
                     * @name org.webswing.directdraw.proto.StrokeProto.StrokeJoinProto
                     * @enum {number}
                     * @property {number} JOIN_MITER=0 JOIN_MITER value
                     * @property {number} JOIN_ROUND=1 JOIN_ROUND value
                     * @property {number} JOIN_BEVEL=2 JOIN_BEVEL value
                     */
                    StrokeProto.StrokeJoinProto = (function() {
                        var valuesById = {}, values = Object.create(valuesById);
                        values[valuesById[0] = "JOIN_MITER"] = 0;
                        values[valuesById[1] = "JOIN_ROUND"] = 1;
                        values[valuesById[2] = "JOIN_BEVEL"] = 2;
                        return values;
                    })();

                    /**
                     * StrokeCapProto enum.
                     * @name org.webswing.directdraw.proto.StrokeProto.StrokeCapProto
                     * @enum {number}
                     * @property {number} CAP_BUTT=0 CAP_BUTT value
                     * @property {number} CAP_ROUND=1 CAP_ROUND value
                     * @property {number} CAP_SQUARE=2 CAP_SQUARE value
                     */
                    StrokeProto.StrokeCapProto = (function() {
                        var valuesById = {}, values = Object.create(valuesById);
                        values[valuesById[0] = "CAP_BUTT"] = 0;
                        values[valuesById[1] = "CAP_ROUND"] = 1;
                        values[valuesById[2] = "CAP_SQUARE"] = 2;
                        return values;
                    })();

                    return StrokeProto;
                })();

                proto.LinearGradientProto = (function() {

                    /**
                     * Properties of a LinearGradientProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface ILinearGradientProto
                     * @property {number} xStart LinearGradientProto xStart
                     * @property {number} yStart LinearGradientProto yStart
                     * @property {number} xEnd LinearGradientProto xEnd
                     * @property {number} yEnd LinearGradientProto yEnd
                     * @property {Array.<number>|null} [colors] LinearGradientProto colors
                     * @property {Array.<number>|null} [fractions] LinearGradientProto fractions
                     * @property {org.webswing.directdraw.proto.CyclicMethodProto} repeat LinearGradientProto repeat
                     */

                    /**
                     * Constructs a new LinearGradientProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a LinearGradientProto.
                     * @implements ILinearGradientProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.ILinearGradientProto=} [properties] Properties to set
                     */
                    function LinearGradientProto(properties) {
                        this.colors = [];
                        this.fractions = [];
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * LinearGradientProto xStart.
                     * @member {number} xStart
                     * @memberof org.webswing.directdraw.proto.LinearGradientProto
                     * @instance
                     */
                    LinearGradientProto.prototype.xStart = 0;

                    /**
                     * LinearGradientProto yStart.
                     * @member {number} yStart
                     * @memberof org.webswing.directdraw.proto.LinearGradientProto
                     * @instance
                     */
                    LinearGradientProto.prototype.yStart = 0;

                    /**
                     * LinearGradientProto xEnd.
                     * @member {number} xEnd
                     * @memberof org.webswing.directdraw.proto.LinearGradientProto
                     * @instance
                     */
                    LinearGradientProto.prototype.xEnd = 0;

                    /**
                     * LinearGradientProto yEnd.
                     * @member {number} yEnd
                     * @memberof org.webswing.directdraw.proto.LinearGradientProto
                     * @instance
                     */
                    LinearGradientProto.prototype.yEnd = 0;

                    /**
                     * LinearGradientProto colors.
                     * @member {Array.<number>} colors
                     * @memberof org.webswing.directdraw.proto.LinearGradientProto
                     * @instance
                     */
                    LinearGradientProto.prototype.colors = $util.emptyArray;

                    /**
                     * LinearGradientProto fractions.
                     * @member {Array.<number>} fractions
                     * @memberof org.webswing.directdraw.proto.LinearGradientProto
                     * @instance
                     */
                    LinearGradientProto.prototype.fractions = $util.emptyArray;

                    /**
                     * LinearGradientProto repeat.
                     * @member {org.webswing.directdraw.proto.CyclicMethodProto} repeat
                     * @memberof org.webswing.directdraw.proto.LinearGradientProto
                     * @instance
                     */
                    LinearGradientProto.prototype.repeat = 0;

                    /**
                     * Decodes a LinearGradientProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.LinearGradientProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.LinearGradientProto} LinearGradientProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    LinearGradientProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.LinearGradientProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.xStart = reader.sint32();
                                    break;
                                }
                            case 2: {
                                    message.yStart = reader.sint32();
                                    break;
                                }
                            case 3: {
                                    message.xEnd = reader.sint32();
                                    break;
                                }
                            case 4: {
                                    message.yEnd = reader.sint32();
                                    break;
                                }
                            case 5: {
                                    if (!(message.colors && message.colors.length))
                                        message.colors = [];
                                    if ((tag & 7) === 2) {
                                        var end2 = reader.uint32() + reader.pos;
                                        while (reader.pos < end2)
                                            message.colors.push(reader.fixed32());
                                    } else
                                        message.colors.push(reader.fixed32());
                                    break;
                                }
                            case 6: {
                                    if (!(message.fractions && message.fractions.length))
                                        message.fractions = [];
                                    if ((tag & 7) === 2) {
                                        var end2 = reader.uint32() + reader.pos;
                                        while (reader.pos < end2)
                                            message.fractions.push(reader.float());
                                    } else
                                        message.fractions.push(reader.float());
                                    break;
                                }
                            case 7: {
                                    message.repeat = reader.int32();
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "xStart"))
                            throw $util.ProtocolError("missing required 'xStart'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "yStart"))
                            throw $util.ProtocolError("missing required 'yStart'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "xEnd"))
                            throw $util.ProtocolError("missing required 'xEnd'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "yEnd"))
                            throw $util.ProtocolError("missing required 'yEnd'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "repeat"))
                            throw $util.ProtocolError("missing required 'repeat'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a LinearGradientProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.LinearGradientProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.LinearGradientProto} LinearGradientProto
                     */
                    LinearGradientProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.LinearGradientProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.LinearGradientProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.LinearGradientProto();
                        if (object.xStart != null)
                            message.xStart = object.xStart | 0;
                        if (object.yStart != null)
                            message.yStart = object.yStart | 0;
                        if (object.xEnd != null)
                            message.xEnd = object.xEnd | 0;
                        if (object.yEnd != null)
                            message.yEnd = object.yEnd | 0;
                        if (object.colors) {
                            if (!Array.isArray(object.colors))
                                throw TypeError(".org.webswing.directdraw.proto.LinearGradientProto.colors: array expected");
                            message.colors = [];
                            for (var i = 0; i < object.colors.length; ++i)
                                message.colors[i] = object.colors[i] >>> 0;
                        }
                        if (object.fractions) {
                            if (!Array.isArray(object.fractions))
                                throw TypeError(".org.webswing.directdraw.proto.LinearGradientProto.fractions: array expected");
                            message.fractions = [];
                            for (var i = 0; i < object.fractions.length; ++i)
                                message.fractions[i] = Number(object.fractions[i]);
                        }
                        switch (object.repeat) {
                        default:
                            if (typeof object.repeat === "number") {
                                message.repeat = object.repeat;
                                break;
                            }
                            break;
                        case "NO_CYCLE":
                        case 0:
                            message.repeat = 0;
                            break;
                        case "REFLECT":
                        case 1:
                            message.repeat = 1;
                            break;
                        case "REPEAT":
                        case 2:
                            message.repeat = 2;
                            break;
                        }
                        return message;
                    };

                    /**
                     * Creates a plain object from a LinearGradientProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.LinearGradientProto
                     * @static
                     * @param {org.webswing.directdraw.proto.LinearGradientProto} message LinearGradientProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    LinearGradientProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.arrays || options.defaults) {
                            object.colors = [];
                            object.fractions = [];
                        }
                        if (options.defaults) {
                            object.xStart = 0;
                            object.yStart = 0;
                            object.xEnd = 0;
                            object.yEnd = 0;
                            object.repeat = options.enums === String ? "NO_CYCLE" : 0;
                        }
                        if (message.xStart != null && Object.hasOwnProperty.call(message, "xStart"))
                            object.xStart = message.xStart;
                        if (message.yStart != null && Object.hasOwnProperty.call(message, "yStart"))
                            object.yStart = message.yStart;
                        if (message.xEnd != null && Object.hasOwnProperty.call(message, "xEnd"))
                            object.xEnd = message.xEnd;
                        if (message.yEnd != null && Object.hasOwnProperty.call(message, "yEnd"))
                            object.yEnd = message.yEnd;
                        if (message.colors && message.colors.length) {
                            object.colors = [];
                            for (var j = 0; j < message.colors.length; ++j)
                                object.colors[j] = message.colors[j];
                        }
                        if (message.fractions && message.fractions.length) {
                            object.fractions = [];
                            for (var j = 0; j < message.fractions.length; ++j)
                                object.fractions[j] = options.json && !isFinite(message.fractions[j]) ? String(message.fractions[j]) : message.fractions[j];
                        }
                        if (message.repeat != null && Object.hasOwnProperty.call(message, "repeat"))
                            object.repeat = options.enums === String ? $root.org.webswing.directdraw.proto.CyclicMethodProto[message.repeat] === undefined ? message.repeat : $root.org.webswing.directdraw.proto.CyclicMethodProto[message.repeat] : message.repeat;
                        return object;
                    };

                    /**
                     * Converts this LinearGradientProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.LinearGradientProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    LinearGradientProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for LinearGradientProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.LinearGradientProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    LinearGradientProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.LinearGradientProto";
                    };

                    return LinearGradientProto;
                })();

                proto.RadialGradientProto = (function() {

                    /**
                     * Properties of a RadialGradientProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IRadialGradientProto
                     * @property {number} xCenter RadialGradientProto xCenter
                     * @property {number} yCenter RadialGradientProto yCenter
                     * @property {number} xFocus RadialGradientProto xFocus
                     * @property {number} yFocus RadialGradientProto yFocus
                     * @property {number} radius RadialGradientProto radius
                     * @property {Array.<number>|null} [colors] RadialGradientProto colors
                     * @property {Array.<number>|null} [fractions] RadialGradientProto fractions
                     * @property {org.webswing.directdraw.proto.CyclicMethodProto} repeat RadialGradientProto repeat
                     */

                    /**
                     * Constructs a new RadialGradientProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a RadialGradientProto.
                     * @implements IRadialGradientProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IRadialGradientProto=} [properties] Properties to set
                     */
                    function RadialGradientProto(properties) {
                        this.colors = [];
                        this.fractions = [];
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * RadialGradientProto xCenter.
                     * @member {number} xCenter
                     * @memberof org.webswing.directdraw.proto.RadialGradientProto
                     * @instance
                     */
                    RadialGradientProto.prototype.xCenter = 0;

                    /**
                     * RadialGradientProto yCenter.
                     * @member {number} yCenter
                     * @memberof org.webswing.directdraw.proto.RadialGradientProto
                     * @instance
                     */
                    RadialGradientProto.prototype.yCenter = 0;

                    /**
                     * RadialGradientProto xFocus.
                     * @member {number} xFocus
                     * @memberof org.webswing.directdraw.proto.RadialGradientProto
                     * @instance
                     */
                    RadialGradientProto.prototype.xFocus = 0;

                    /**
                     * RadialGradientProto yFocus.
                     * @member {number} yFocus
                     * @memberof org.webswing.directdraw.proto.RadialGradientProto
                     * @instance
                     */
                    RadialGradientProto.prototype.yFocus = 0;

                    /**
                     * RadialGradientProto radius.
                     * @member {number} radius
                     * @memberof org.webswing.directdraw.proto.RadialGradientProto
                     * @instance
                     */
                    RadialGradientProto.prototype.radius = 0;

                    /**
                     * RadialGradientProto colors.
                     * @member {Array.<number>} colors
                     * @memberof org.webswing.directdraw.proto.RadialGradientProto
                     * @instance
                     */
                    RadialGradientProto.prototype.colors = $util.emptyArray;

                    /**
                     * RadialGradientProto fractions.
                     * @member {Array.<number>} fractions
                     * @memberof org.webswing.directdraw.proto.RadialGradientProto
                     * @instance
                     */
                    RadialGradientProto.prototype.fractions = $util.emptyArray;

                    /**
                     * RadialGradientProto repeat.
                     * @member {org.webswing.directdraw.proto.CyclicMethodProto} repeat
                     * @memberof org.webswing.directdraw.proto.RadialGradientProto
                     * @instance
                     */
                    RadialGradientProto.prototype.repeat = 0;

                    /**
                     * Decodes a RadialGradientProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.RadialGradientProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.RadialGradientProto} RadialGradientProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    RadialGradientProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.RadialGradientProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.xCenter = reader.sint32();
                                    break;
                                }
                            case 2: {
                                    message.yCenter = reader.sint32();
                                    break;
                                }
                            case 3: {
                                    message.xFocus = reader.sint32();
                                    break;
                                }
                            case 4: {
                                    message.yFocus = reader.sint32();
                                    break;
                                }
                            case 5: {
                                    message.radius = reader.sint32();
                                    break;
                                }
                            case 6: {
                                    if (!(message.colors && message.colors.length))
                                        message.colors = [];
                                    if ((tag & 7) === 2) {
                                        var end2 = reader.uint32() + reader.pos;
                                        while (reader.pos < end2)
                                            message.colors.push(reader.fixed32());
                                    } else
                                        message.colors.push(reader.fixed32());
                                    break;
                                }
                            case 7: {
                                    if (!(message.fractions && message.fractions.length))
                                        message.fractions = [];
                                    if ((tag & 7) === 2) {
                                        var end2 = reader.uint32() + reader.pos;
                                        while (reader.pos < end2)
                                            message.fractions.push(reader.float());
                                    } else
                                        message.fractions.push(reader.float());
                                    break;
                                }
                            case 8: {
                                    message.repeat = reader.int32();
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "xCenter"))
                            throw $util.ProtocolError("missing required 'xCenter'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "yCenter"))
                            throw $util.ProtocolError("missing required 'yCenter'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "xFocus"))
                            throw $util.ProtocolError("missing required 'xFocus'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "yFocus"))
                            throw $util.ProtocolError("missing required 'yFocus'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "radius"))
                            throw $util.ProtocolError("missing required 'radius'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "repeat"))
                            throw $util.ProtocolError("missing required 'repeat'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a RadialGradientProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.RadialGradientProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.RadialGradientProto} RadialGradientProto
                     */
                    RadialGradientProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.RadialGradientProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.RadialGradientProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.RadialGradientProto();
                        if (object.xCenter != null)
                            message.xCenter = object.xCenter | 0;
                        if (object.yCenter != null)
                            message.yCenter = object.yCenter | 0;
                        if (object.xFocus != null)
                            message.xFocus = object.xFocus | 0;
                        if (object.yFocus != null)
                            message.yFocus = object.yFocus | 0;
                        if (object.radius != null)
                            message.radius = object.radius | 0;
                        if (object.colors) {
                            if (!Array.isArray(object.colors))
                                throw TypeError(".org.webswing.directdraw.proto.RadialGradientProto.colors: array expected");
                            message.colors = [];
                            for (var i = 0; i < object.colors.length; ++i)
                                message.colors[i] = object.colors[i] >>> 0;
                        }
                        if (object.fractions) {
                            if (!Array.isArray(object.fractions))
                                throw TypeError(".org.webswing.directdraw.proto.RadialGradientProto.fractions: array expected");
                            message.fractions = [];
                            for (var i = 0; i < object.fractions.length; ++i)
                                message.fractions[i] = Number(object.fractions[i]);
                        }
                        switch (object.repeat) {
                        default:
                            if (typeof object.repeat === "number") {
                                message.repeat = object.repeat;
                                break;
                            }
                            break;
                        case "NO_CYCLE":
                        case 0:
                            message.repeat = 0;
                            break;
                        case "REFLECT":
                        case 1:
                            message.repeat = 1;
                            break;
                        case "REPEAT":
                        case 2:
                            message.repeat = 2;
                            break;
                        }
                        return message;
                    };

                    /**
                     * Creates a plain object from a RadialGradientProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.RadialGradientProto
                     * @static
                     * @param {org.webswing.directdraw.proto.RadialGradientProto} message RadialGradientProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    RadialGradientProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.arrays || options.defaults) {
                            object.colors = [];
                            object.fractions = [];
                        }
                        if (options.defaults) {
                            object.xCenter = 0;
                            object.yCenter = 0;
                            object.xFocus = 0;
                            object.yFocus = 0;
                            object.radius = 0;
                            object.repeat = options.enums === String ? "NO_CYCLE" : 0;
                        }
                        if (message.xCenter != null && Object.hasOwnProperty.call(message, "xCenter"))
                            object.xCenter = message.xCenter;
                        if (message.yCenter != null && Object.hasOwnProperty.call(message, "yCenter"))
                            object.yCenter = message.yCenter;
                        if (message.xFocus != null && Object.hasOwnProperty.call(message, "xFocus"))
                            object.xFocus = message.xFocus;
                        if (message.yFocus != null && Object.hasOwnProperty.call(message, "yFocus"))
                            object.yFocus = message.yFocus;
                        if (message.radius != null && Object.hasOwnProperty.call(message, "radius"))
                            object.radius = message.radius;
                        if (message.colors && message.colors.length) {
                            object.colors = [];
                            for (var j = 0; j < message.colors.length; ++j)
                                object.colors[j] = message.colors[j];
                        }
                        if (message.fractions && message.fractions.length) {
                            object.fractions = [];
                            for (var j = 0; j < message.fractions.length; ++j)
                                object.fractions[j] = options.json && !isFinite(message.fractions[j]) ? String(message.fractions[j]) : message.fractions[j];
                        }
                        if (message.repeat != null && Object.hasOwnProperty.call(message, "repeat"))
                            object.repeat = options.enums === String ? $root.org.webswing.directdraw.proto.CyclicMethodProto[message.repeat] === undefined ? message.repeat : $root.org.webswing.directdraw.proto.CyclicMethodProto[message.repeat] : message.repeat;
                        return object;
                    };

                    /**
                     * Converts this RadialGradientProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.RadialGradientProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    RadialGradientProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for RadialGradientProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.RadialGradientProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    RadialGradientProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.RadialGradientProto";
                    };

                    return RadialGradientProto;
                })();

                /**
                 * CyclicMethodProto enum.
                 * @name org.webswing.directdraw.proto.CyclicMethodProto
                 * @enum {number}
                 * @property {number} NO_CYCLE=0 NO_CYCLE value
                 * @property {number} REFLECT=1 REFLECT value
                 * @property {number} REPEAT=2 REPEAT value
                 */
                proto.CyclicMethodProto = (function() {
                    var valuesById = {}, values = Object.create(valuesById);
                    values[valuesById[0] = "NO_CYCLE"] = 0;
                    values[valuesById[1] = "REFLECT"] = 1;
                    values[valuesById[2] = "REPEAT"] = 2;
                    return values;
                })();

                proto.PointsProto = (function() {

                    /**
                     * Properties of a PointsProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface IPointsProto
                     * @property {Array.<number>|null} [points] PointsProto points
                     */

                    /**
                     * Constructs a new PointsProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a PointsProto.
                     * @implements IPointsProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.IPointsProto=} [properties] Properties to set
                     */
                    function PointsProto(properties) {
                        this.points = [];
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * PointsProto points.
                     * @member {Array.<number>} points
                     * @memberof org.webswing.directdraw.proto.PointsProto
                     * @instance
                     */
                    PointsProto.prototype.points = $util.emptyArray;

                    /**
                     * Decodes a PointsProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.PointsProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.PointsProto} PointsProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    PointsProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.PointsProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    if (!(message.points && message.points.length))
                                        message.points = [];
                                    if ((tag & 7) === 2) {
                                        var end2 = reader.uint32() + reader.pos;
                                        while (reader.pos < end2)
                                            message.points.push(reader.sint32());
                                    } else
                                        message.points.push(reader.sint32());
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
                     * Creates a PointsProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.PointsProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.PointsProto} PointsProto
                     */
                    PointsProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.PointsProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.PointsProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.PointsProto();
                        if (object.points) {
                            if (!Array.isArray(object.points))
                                throw TypeError(".org.webswing.directdraw.proto.PointsProto.points: array expected");
                            message.points = [];
                            for (var i = 0; i < object.points.length; ++i)
                                message.points[i] = object.points[i] | 0;
                        }
                        return message;
                    };

                    /**
                     * Creates a plain object from a PointsProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.PointsProto
                     * @static
                     * @param {org.webswing.directdraw.proto.PointsProto} message PointsProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    PointsProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.arrays || options.defaults)
                            object.points = [];
                        if (message.points && message.points.length) {
                            object.points = [];
                            for (var j = 0; j < message.points.length; ++j)
                                object.points[j] = message.points[j];
                        }
                        return object;
                    };

                    /**
                     * Converts this PointsProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.PointsProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    PointsProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for PointsProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.PointsProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    PointsProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.PointsProto";
                    };

                    return PointsProto;
                })();

                proto.CompositeProto = (function() {

                    /**
                     * Properties of a CompositeProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface ICompositeProto
                     * @property {org.webswing.directdraw.proto.CompositeProto.CompositeTypeProto} type CompositeProto type
                     * @property {number|null} [alpha] CompositeProto alpha
                     * @property {number|null} [color] CompositeProto color
                     */

                    /**
                     * Constructs a new CompositeProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a CompositeProto.
                     * @implements ICompositeProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.ICompositeProto=} [properties] Properties to set
                     */
                    function CompositeProto(properties) {
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * CompositeProto type.
                     * @member {org.webswing.directdraw.proto.CompositeProto.CompositeTypeProto} type
                     * @memberof org.webswing.directdraw.proto.CompositeProto
                     * @instance
                     */
                    CompositeProto.prototype.type = 1;

                    /**
                     * CompositeProto alpha.
                     * @member {number} alpha
                     * @memberof org.webswing.directdraw.proto.CompositeProto
                     * @instance
                     */
                    CompositeProto.prototype.alpha = 1;

                    /**
                     * CompositeProto color.
                     * @member {number} color
                     * @memberof org.webswing.directdraw.proto.CompositeProto
                     * @instance
                     */
                    CompositeProto.prototype.color = 0;

                    /**
                     * Decodes a CompositeProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.CompositeProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.CompositeProto} CompositeProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    CompositeProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.CompositeProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.type = reader.int32();
                                    break;
                                }
                            case 2: {
                                    message.alpha = reader.float();
                                    break;
                                }
                            case 3: {
                                    message.color = reader.fixed32();
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "type"))
                            throw $util.ProtocolError("missing required 'type'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a CompositeProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.CompositeProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.CompositeProto} CompositeProto
                     */
                    CompositeProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.CompositeProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.CompositeProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.CompositeProto();
                        switch (object.type) {
                        default:
                            if (typeof object.type === "number") {
                                message.type = object.type;
                                break;
                            }
                            break;
                        case "CLEAR":
                        case 1:
                            message.type = 1;
                            break;
                        case "SRC":
                        case 2:
                            message.type = 2;
                            break;
                        case "DST":
                        case 9:
                            message.type = 9;
                            break;
                        case "SRC_OVER":
                        case 3:
                            message.type = 3;
                            break;
                        case "DST_OVER":
                        case 4:
                            message.type = 4;
                            break;
                        case "SRC_IN":
                        case 5:
                            message.type = 5;
                            break;
                        case "DST_IN":
                        case 6:
                            message.type = 6;
                            break;
                        case "SRC_OUT":
                        case 7:
                            message.type = 7;
                            break;
                        case "DST_OUT":
                        case 8:
                            message.type = 8;
                            break;
                        case "SRC_ATOP":
                        case 10:
                            message.type = 10;
                            break;
                        case "DST_ATOP":
                        case 11:
                            message.type = 11;
                            break;
                        case "XOR":
                        case 12:
                            message.type = 12;
                            break;
                        case "XOR_MODE":
                        case 13:
                            message.type = 13;
                            break;
                        }
                        if (object.alpha != null)
                            message.alpha = Number(object.alpha);
                        if (object.color != null)
                            message.color = object.color >>> 0;
                        return message;
                    };

                    /**
                     * Creates a plain object from a CompositeProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.CompositeProto
                     * @static
                     * @param {org.webswing.directdraw.proto.CompositeProto} message CompositeProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    CompositeProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.defaults) {
                            object.type = options.enums === String ? "CLEAR" : 1;
                            object.alpha = 1;
                            object.color = 0;
                        }
                        if (message.type != null && Object.hasOwnProperty.call(message, "type"))
                            object.type = options.enums === String ? $root.org.webswing.directdraw.proto.CompositeProto.CompositeTypeProto[message.type] === undefined ? message.type : $root.org.webswing.directdraw.proto.CompositeProto.CompositeTypeProto[message.type] : message.type;
                        if (message.alpha != null && Object.hasOwnProperty.call(message, "alpha"))
                            object.alpha = options.json && !isFinite(message.alpha) ? String(message.alpha) : message.alpha;
                        if (message.color != null && Object.hasOwnProperty.call(message, "color"))
                            object.color = message.color;
                        return object;
                    };

                    /**
                     * Converts this CompositeProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.CompositeProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    CompositeProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for CompositeProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.CompositeProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    CompositeProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.CompositeProto";
                    };

                    /**
                     * CompositeTypeProto enum.
                     * @name org.webswing.directdraw.proto.CompositeProto.CompositeTypeProto
                     * @enum {number}
                     * @property {number} CLEAR=1 CLEAR value
                     * @property {number} SRC=2 SRC value
                     * @property {number} DST=9 DST value
                     * @property {number} SRC_OVER=3 SRC_OVER value
                     * @property {number} DST_OVER=4 DST_OVER value
                     * @property {number} SRC_IN=5 SRC_IN value
                     * @property {number} DST_IN=6 DST_IN value
                     * @property {number} SRC_OUT=7 SRC_OUT value
                     * @property {number} DST_OUT=8 DST_OUT value
                     * @property {number} SRC_ATOP=10 SRC_ATOP value
                     * @property {number} DST_ATOP=11 DST_ATOP value
                     * @property {number} XOR=12 XOR value
                     * @property {number} XOR_MODE=13 XOR_MODE value
                     */
                    CompositeProto.CompositeTypeProto = (function() {
                        var valuesById = {}, values = Object.create(valuesById);
                        values[valuesById[1] = "CLEAR"] = 1;
                        values[valuesById[2] = "SRC"] = 2;
                        values[valuesById[9] = "DST"] = 9;
                        values[valuesById[3] = "SRC_OVER"] = 3;
                        values[valuesById[4] = "DST_OVER"] = 4;
                        values[valuesById[5] = "SRC_IN"] = 5;
                        values[valuesById[6] = "DST_IN"] = 6;
                        values[valuesById[7] = "SRC_OUT"] = 7;
                        values[valuesById[8] = "DST_OUT"] = 8;
                        values[valuesById[10] = "SRC_ATOP"] = 10;
                        values[valuesById[11] = "DST_ATOP"] = 11;
                        values[valuesById[12] = "XOR"] = 12;
                        values[valuesById[13] = "XOR_MODE"] = 13;
                        return values;
                    })();

                    return CompositeProto;
                })();

                proto.TextureProto = (function() {

                    /**
                     * Properties of a TextureProto.
                     * @memberof org.webswing.directdraw.proto
                     * @interface ITextureProto
                     * @property {org.webswing.directdraw.proto.IImageProto} image TextureProto image
                     * @property {org.webswing.directdraw.proto.IRectangleProto} anchor TextureProto anchor
                     */

                    /**
                     * Constructs a new TextureProto.
                     * @memberof org.webswing.directdraw.proto
                     * @classdesc Represents a TextureProto.
                     * @implements ITextureProto
                     * @constructor
                     * @param {org.webswing.directdraw.proto.ITextureProto=} [properties] Properties to set
                     */
                    function TextureProto(properties) {
                        if (properties)
                            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                if (properties[keys[i]] != null && keys[i] !== "__proto__")
                                    this[keys[i]] = properties[keys[i]];
                    }

                    /**
                     * TextureProto image.
                     * @member {org.webswing.directdraw.proto.IImageProto} image
                     * @memberof org.webswing.directdraw.proto.TextureProto
                     * @instance
                     */
                    TextureProto.prototype.image = null;

                    /**
                     * TextureProto anchor.
                     * @member {org.webswing.directdraw.proto.IRectangleProto} anchor
                     * @memberof org.webswing.directdraw.proto.TextureProto
                     * @instance
                     */
                    TextureProto.prototype.anchor = null;

                    /**
                     * Decodes a TextureProto message from the specified reader or buffer.
                     * @function decode
                     * @memberof org.webswing.directdraw.proto.TextureProto
                     * @static
                     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                     * @param {number} [length] Message length if known beforehand
                     * @returns {org.webswing.directdraw.proto.TextureProto} TextureProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    TextureProto.decode = function decode(reader, length, error, long) {
                        if (!(reader instanceof $Reader))
                            reader = $Reader.create(reader);
                        if (long === undefined)
                            long = 0;
                        if (long > $Reader.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.org.webswing.directdraw.proto.TextureProto();
                        while (reader.pos < end) {
                            var tag = reader.uint32();
                            if (tag === error)
                                break;
                            switch (tag >>> 3) {
                            case 1: {
                                    message.image = $root.org.webswing.directdraw.proto.ImageProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            case 2: {
                                    message.anchor = $root.org.webswing.directdraw.proto.RectangleProto.decode(reader, reader.uint32(), undefined, long + 1);
                                    break;
                                }
                            default:
                                reader.skipType(tag & 7, long);
                                break;
                            }
                        }
                        if (!Object.hasOwnProperty.call(message, "image"))
                            throw $util.ProtocolError("missing required 'image'", { instance: message });
                        if (!Object.hasOwnProperty.call(message, "anchor"))
                            throw $util.ProtocolError("missing required 'anchor'", { instance: message });
                        return message;
                    };

                    /**
                     * Creates a TextureProto message from a plain object. Also converts values to their respective internal types.
                     * @function fromObject
                     * @memberof org.webswing.directdraw.proto.TextureProto
                     * @static
                     * @param {Object.<string,*>} object Plain object
                     * @returns {org.webswing.directdraw.proto.TextureProto} TextureProto
                     */
                    TextureProto.fromObject = function fromObject(object, long) {
                        if (object instanceof $root.org.webswing.directdraw.proto.TextureProto)
                            return object;
                        if (!$util.isObject(object))
                            throw TypeError(".org.webswing.directdraw.proto.TextureProto: object expected");
                        if (long === undefined)
                            long = 0;
                        if (long > $util.recursionLimit)
                            throw Error("maximum nesting depth exceeded");
                        var message = new $root.org.webswing.directdraw.proto.TextureProto();
                        if (object.image != null) {
                            if (!$util.isObject(object.image))
                                throw TypeError(".org.webswing.directdraw.proto.TextureProto.image: object expected");
                            message.image = $root.org.webswing.directdraw.proto.ImageProto.fromObject(object.image, long + 1);
                        }
                        if (object.anchor != null) {
                            if (!$util.isObject(object.anchor))
                                throw TypeError(".org.webswing.directdraw.proto.TextureProto.anchor: object expected");
                            message.anchor = $root.org.webswing.directdraw.proto.RectangleProto.fromObject(object.anchor, long + 1);
                        }
                        return message;
                    };

                    /**
                     * Creates a plain object from a TextureProto message. Also converts values to other types if specified.
                     * @function toObject
                     * @memberof org.webswing.directdraw.proto.TextureProto
                     * @static
                     * @param {org.webswing.directdraw.proto.TextureProto} message TextureProto
                     * @param {$protobuf.IConversionOptions} [options] Conversion options
                     * @returns {Object.<string,*>} Plain object
                     */
                    TextureProto.toObject = function toObject(message, options, q) {
                        if (!options)
                            options = {};
                        if (q === undefined)
                            q = 0;
                        if (q > $util.recursionLimit)
                            throw Error("max depth exceeded");
                        var object = {};
                        if (options.defaults) {
                            object.image = null;
                            object.anchor = null;
                        }
                        if (message.image != null && Object.hasOwnProperty.call(message, "image"))
                            object.image = $root.org.webswing.directdraw.proto.ImageProto.toObject(message.image, options, q + 1);
                        if (message.anchor != null && Object.hasOwnProperty.call(message, "anchor"))
                            object.anchor = $root.org.webswing.directdraw.proto.RectangleProto.toObject(message.anchor, options, q + 1);
                        return object;
                    };

                    /**
                     * Converts this TextureProto to JSON.
                     * @function toJSON
                     * @memberof org.webswing.directdraw.proto.TextureProto
                     * @instance
                     * @returns {Object.<string,*>} JSON object
                     */
                    TextureProto.prototype.toJSON = function toJSON() {
                        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                    };

                    /**
                     * Gets the default type url for TextureProto
                     * @function getTypeUrl
                     * @memberof org.webswing.directdraw.proto.TextureProto
                     * @static
                     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns {string} The default type url
                     */
                    TextureProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
                        if (typeUrlPrefix === undefined) {
                            typeUrlPrefix = "type.googleapis.com";
                        }
                        return typeUrlPrefix + "/org.webswing.directdraw.proto.TextureProto";
                    };

                    return TextureProto;
                })();

                return proto;
            })();

            return directdraw;
        })();

        return webswing;
    })();

    return org;
})();

module.exports = $root;
