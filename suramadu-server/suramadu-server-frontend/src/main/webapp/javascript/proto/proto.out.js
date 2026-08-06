/*eslint-disable block-scoped-var, id-length, no-control-regex, no-magic-numbers, no-prototype-builtins, no-redeclare, no-shadow, no-var, sort-vars*/
"use strict";

var $protobuf = require("protobufjs/minimal");

// Common aliases
var $Reader = $protobuf.Reader, $util = $protobuf.util;

// Exported root namespace
var $root = $protobuf.roots["default"] || ($protobuf.roots["default"] = {});

$root.appFrameProtoOut = (function() {

    /**
     * Namespace appFrameProtoOut.
     * @exports appFrameProtoOut
     * @namespace
     */
    var appFrameProtoOut = {};

    appFrameProtoOut.AppFrameMsgOutProto = (function() {

        /**
         * Properties of an AppFrameMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IAppFrameMsgOutProto
         * @property {appFrameProtoOut.IStartApplicationMsgOutProto|null} [startApplication] AppFrameMsgOutProto startApplication
         * @property {appFrameProtoOut.ILinkActionMsgOutProto|null} [linkAction] AppFrameMsgOutProto linkAction
         * @property {appFrameProtoOut.IWindowMoveActionMsgOutProto|null} [moveAction] AppFrameMsgOutProto moveAction
         * @property {appFrameProtoOut.ICopyEventMsgOutProto|null} [copyEvent] AppFrameMsgOutProto copyEvent
         * @property {appFrameProtoOut.IPasteRequestMsgOutProto|null} [pasteRequest] AppFrameMsgOutProto pasteRequest
         * @property {appFrameProtoOut.IFileDialogEventMsgOutProto|null} [fileDialogEvent] AppFrameMsgOutProto fileDialogEvent
         * @property {Array.<appFrameProtoOut.IWindowMsgOutProto>|null} [windows] AppFrameMsgOutProto windows
         * @property {appFrameProtoOut.IWindowMsgOutProto|null} [closedWindow] AppFrameMsgOutProto closedWindow
         * @property {appFrameProtoOut.SimpleEventMsgOutProto|null} [event] AppFrameMsgOutProto event
         * @property {appFrameProtoOut.IJsEvalRequestMsgOutProto|null} [jsRequest] AppFrameMsgOutProto jsRequest
         * @property {appFrameProtoOut.IJsResultMsgOutProto|null} [javaResponse] AppFrameMsgOutProto javaResponse
         * @property {appFrameProtoOut.IPixelsAreaRequestMsgOutProto|null} [pixelsRequest] AppFrameMsgOutProto pixelsRequest
         * @property {appFrameProtoOut.IPlaybackInfoMsgOutProto|null} [playback] AppFrameMsgOutProto playback
         * @property {string|null} [instanceId] AppFrameMsgOutProto instanceId
         * @property {string|null} [startTimestamp] AppFrameMsgOutProto startTimestamp
         * @property {string|null} [sendTimestamp] AppFrameMsgOutProto sendTimestamp
         * @property {appFrameProtoOut.IFocusEventMsgOutProto|null} [focusEvent] AppFrameMsgOutProto focusEvent
         * @property {Array.<appFrameProtoOut.IComponentTreeMsgOutProto>|null} [componentTree] AppFrameMsgOutProto componentTree
         * @property {boolean|null} [directDraw] AppFrameMsgOutProto directDraw
         * @property {appFrameProtoOut.IActionEventMsgOutProto|null} [actionEvent] AppFrameMsgOutProto actionEvent
         * @property {boolean|null} [compositingWM] AppFrameMsgOutProto compositingWM
         * @property {appFrameProtoOut.IAudioEventMsgOutProto|null} [audioEvent] AppFrameMsgOutProto audioEvent
         * @property {appFrameProtoOut.IAccessibilityMsgOutProto|null} [accessible] AppFrameMsgOutProto accessible
         * @property {Array.<appFrameProtoOut.IWindowSwitchMsgOutProto>|null} [windowSwitchList] AppFrameMsgOutProto windowSwitchList
         * @property {appFrameProtoOut.ICursorChangeEventMsgOutProto|null} [cursorChangeEvent] AppFrameMsgOutProto cursorChangeEvent
         */

        /**
         * Constructs a new AppFrameMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents an AppFrameMsgOutProto.
         * @implements IAppFrameMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IAppFrameMsgOutProto=} [properties] Properties to set
         */
        function AppFrameMsgOutProto(properties) {
            this.windows = [];
            this.componentTree = [];
            this.windowSwitchList = [];
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * AppFrameMsgOutProto startApplication.
         * @member {appFrameProtoOut.IStartApplicationMsgOutProto|null|undefined} startApplication
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.startApplication = null;

        /**
         * AppFrameMsgOutProto linkAction.
         * @member {appFrameProtoOut.ILinkActionMsgOutProto|null|undefined} linkAction
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.linkAction = null;

        /**
         * AppFrameMsgOutProto moveAction.
         * @member {appFrameProtoOut.IWindowMoveActionMsgOutProto|null|undefined} moveAction
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.moveAction = null;

        /**
         * AppFrameMsgOutProto copyEvent.
         * @member {appFrameProtoOut.ICopyEventMsgOutProto|null|undefined} copyEvent
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.copyEvent = null;

        /**
         * AppFrameMsgOutProto pasteRequest.
         * @member {appFrameProtoOut.IPasteRequestMsgOutProto|null|undefined} pasteRequest
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.pasteRequest = null;

        /**
         * AppFrameMsgOutProto fileDialogEvent.
         * @member {appFrameProtoOut.IFileDialogEventMsgOutProto|null|undefined} fileDialogEvent
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.fileDialogEvent = null;

        /**
         * AppFrameMsgOutProto windows.
         * @member {Array.<appFrameProtoOut.IWindowMsgOutProto>} windows
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.windows = $util.emptyArray;

        /**
         * AppFrameMsgOutProto closedWindow.
         * @member {appFrameProtoOut.IWindowMsgOutProto|null|undefined} closedWindow
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.closedWindow = null;

        /**
         * AppFrameMsgOutProto event.
         * @member {appFrameProtoOut.SimpleEventMsgOutProto} event
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.event = 0;

        /**
         * AppFrameMsgOutProto jsRequest.
         * @member {appFrameProtoOut.IJsEvalRequestMsgOutProto|null|undefined} jsRequest
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.jsRequest = null;

        /**
         * AppFrameMsgOutProto javaResponse.
         * @member {appFrameProtoOut.IJsResultMsgOutProto|null|undefined} javaResponse
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.javaResponse = null;

        /**
         * AppFrameMsgOutProto pixelsRequest.
         * @member {appFrameProtoOut.IPixelsAreaRequestMsgOutProto|null|undefined} pixelsRequest
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.pixelsRequest = null;

        /**
         * AppFrameMsgOutProto playback.
         * @member {appFrameProtoOut.IPlaybackInfoMsgOutProto|null|undefined} playback
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.playback = null;

        /**
         * AppFrameMsgOutProto instanceId.
         * @member {string} instanceId
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.instanceId = "";

        /**
         * AppFrameMsgOutProto startTimestamp.
         * @member {string} startTimestamp
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.startTimestamp = "";

        /**
         * AppFrameMsgOutProto sendTimestamp.
         * @member {string} sendTimestamp
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.sendTimestamp = "";

        /**
         * AppFrameMsgOutProto focusEvent.
         * @member {appFrameProtoOut.IFocusEventMsgOutProto|null|undefined} focusEvent
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.focusEvent = null;

        /**
         * AppFrameMsgOutProto componentTree.
         * @member {Array.<appFrameProtoOut.IComponentTreeMsgOutProto>} componentTree
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.componentTree = $util.emptyArray;

        /**
         * AppFrameMsgOutProto directDraw.
         * @member {boolean} directDraw
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.directDraw = false;

        /**
         * AppFrameMsgOutProto actionEvent.
         * @member {appFrameProtoOut.IActionEventMsgOutProto|null|undefined} actionEvent
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.actionEvent = null;

        /**
         * AppFrameMsgOutProto compositingWM.
         * @member {boolean} compositingWM
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.compositingWM = false;

        /**
         * AppFrameMsgOutProto audioEvent.
         * @member {appFrameProtoOut.IAudioEventMsgOutProto|null|undefined} audioEvent
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.audioEvent = null;

        /**
         * AppFrameMsgOutProto accessible.
         * @member {appFrameProtoOut.IAccessibilityMsgOutProto|null|undefined} accessible
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.accessible = null;

        /**
         * AppFrameMsgOutProto windowSwitchList.
         * @member {Array.<appFrameProtoOut.IWindowSwitchMsgOutProto>} windowSwitchList
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.windowSwitchList = $util.emptyArray;

        /**
         * AppFrameMsgOutProto cursorChangeEvent.
         * @member {appFrameProtoOut.ICursorChangeEventMsgOutProto|null|undefined} cursorChangeEvent
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         */
        AppFrameMsgOutProto.prototype.cursorChangeEvent = null;

        /**
         * Creates a new AppFrameMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @static
         * @param {appFrameProtoOut.IAppFrameMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.AppFrameMsgOutProto} AppFrameMsgOutProto instance
         */
        AppFrameMsgOutProto.create = function create(properties) {
            return new AppFrameMsgOutProto(properties);
        };

        /**
         * Decodes an AppFrameMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.AppFrameMsgOutProto} AppFrameMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        AppFrameMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.AppFrameMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.startApplication = $root.appFrameProtoOut.StartApplicationMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 2: {
                        message.linkAction = $root.appFrameProtoOut.LinkActionMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 3: {
                        message.moveAction = $root.appFrameProtoOut.WindowMoveActionMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 4: {
                        message.copyEvent = $root.appFrameProtoOut.CopyEventMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 5: {
                        message.pasteRequest = $root.appFrameProtoOut.PasteRequestMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 6: {
                        message.fileDialogEvent = $root.appFrameProtoOut.FileDialogEventMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 7: {
                        if (!(message.windows && message.windows.length))
                            message.windows = [];
                        message.windows.push($root.appFrameProtoOut.WindowMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1));
                        break;
                    }
                case 8: {
                        message.closedWindow = $root.appFrameProtoOut.WindowMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 9: {
                        message.event = reader.int32();
                        break;
                    }
                case 10: {
                        message.jsRequest = $root.appFrameProtoOut.JsEvalRequestMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 11: {
                        message.javaResponse = $root.appFrameProtoOut.JsResultMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 12: {
                        message.pixelsRequest = $root.appFrameProtoOut.PixelsAreaRequestMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 13: {
                        message.playback = $root.appFrameProtoOut.PlaybackInfoMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 14: {
                        message.instanceId = reader.string();
                        break;
                    }
                case 15: {
                        message.startTimestamp = reader.string();
                        break;
                    }
                case 16: {
                        message.sendTimestamp = reader.string();
                        break;
                    }
                case 17: {
                        message.focusEvent = $root.appFrameProtoOut.FocusEventMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 18: {
                        if (!(message.componentTree && message.componentTree.length))
                            message.componentTree = [];
                        message.componentTree.push($root.appFrameProtoOut.ComponentTreeMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1));
                        break;
                    }
                case 19: {
                        message.directDraw = reader.bool();
                        break;
                    }
                case 20: {
                        message.actionEvent = $root.appFrameProtoOut.ActionEventMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 21: {
                        message.compositingWM = reader.bool();
                        break;
                    }
                case 22: {
                        message.audioEvent = $root.appFrameProtoOut.AudioEventMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 23: {
                        message.accessible = $root.appFrameProtoOut.AccessibilityMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 24: {
                        if (!(message.windowSwitchList && message.windowSwitchList.length))
                            message.windowSwitchList = [];
                        message.windowSwitchList.push($root.appFrameProtoOut.WindowSwitchMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1));
                        break;
                    }
                case 25: {
                        message.cursorChangeEvent = $root.appFrameProtoOut.CursorChangeEventMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
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
         * Creates an AppFrameMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.AppFrameMsgOutProto} AppFrameMsgOutProto
         */
        AppFrameMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.AppFrameMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.AppFrameMsgOutProto();
            if (object.startApplication != null) {
                if (!$util.isObject(object.startApplication))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.startApplication: object expected");
                message.startApplication = $root.appFrameProtoOut.StartApplicationMsgOutProto.fromObject(object.startApplication, long + 1);
            }
            if (object.linkAction != null) {
                if (!$util.isObject(object.linkAction))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.linkAction: object expected");
                message.linkAction = $root.appFrameProtoOut.LinkActionMsgOutProto.fromObject(object.linkAction, long + 1);
            }
            if (object.moveAction != null) {
                if (!$util.isObject(object.moveAction))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.moveAction: object expected");
                message.moveAction = $root.appFrameProtoOut.WindowMoveActionMsgOutProto.fromObject(object.moveAction, long + 1);
            }
            if (object.copyEvent != null) {
                if (!$util.isObject(object.copyEvent))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.copyEvent: object expected");
                message.copyEvent = $root.appFrameProtoOut.CopyEventMsgOutProto.fromObject(object.copyEvent, long + 1);
            }
            if (object.pasteRequest != null) {
                if (!$util.isObject(object.pasteRequest))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.pasteRequest: object expected");
                message.pasteRequest = $root.appFrameProtoOut.PasteRequestMsgOutProto.fromObject(object.pasteRequest, long + 1);
            }
            if (object.fileDialogEvent != null) {
                if (!$util.isObject(object.fileDialogEvent))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.fileDialogEvent: object expected");
                message.fileDialogEvent = $root.appFrameProtoOut.FileDialogEventMsgOutProto.fromObject(object.fileDialogEvent, long + 1);
            }
            if (object.windows) {
                if (!Array.isArray(object.windows))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.windows: array expected");
                message.windows = [];
                for (var i = 0; i < object.windows.length; ++i) {
                    if (!$util.isObject(object.windows[i]))
                        throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.windows: object expected");
                    message.windows[i] = $root.appFrameProtoOut.WindowMsgOutProto.fromObject(object.windows[i], long + 1);
                }
            }
            if (object.closedWindow != null) {
                if (!$util.isObject(object.closedWindow))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.closedWindow: object expected");
                message.closedWindow = $root.appFrameProtoOut.WindowMsgOutProto.fromObject(object.closedWindow, long + 1);
            }
            switch (object.event) {
            default:
                if (typeof object.event === "number") {
                    message.event = object.event;
                    break;
                }
                break;
            case "applicationAlreadyRunning":
            case 0:
                message.event = 0;
                break;
            case "shutDownNotification":
            case 1:
                message.event = 1;
                break;
            case "tooManyClientsNotification":
            case 2:
                message.event = 2;
                break;
            case "continueOldSession":
            case 3:
                message.event = 3;
                break;
            case "configurationError":
            case 4:
                message.event = 4;
                break;
            case "sessionStolenNotification":
            case 5:
                message.event = 5;
                break;
            case "unauthorizedAccess":
            case 6:
                message.event = 6;
                break;
            case "shutDownAutoLogoutNotification":
            case 7:
                message.event = 7;
                break;
            case "sessionTimeoutWarning":
            case 8:
                message.event = 8;
                break;
            case "sessionTimedOutNotification":
            case 9:
                message.event = 9;
                break;
            case "applicationBusy":
            case 10:
                message.event = 10;
                break;
            case "reconnectInstanceNotFound":
            case 11:
                message.event = 11;
                break;
            }
            if (object.jsRequest != null) {
                if (!$util.isObject(object.jsRequest))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.jsRequest: object expected");
                message.jsRequest = $root.appFrameProtoOut.JsEvalRequestMsgOutProto.fromObject(object.jsRequest, long + 1);
            }
            if (object.javaResponse != null) {
                if (!$util.isObject(object.javaResponse))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.javaResponse: object expected");
                message.javaResponse = $root.appFrameProtoOut.JsResultMsgOutProto.fromObject(object.javaResponse, long + 1);
            }
            if (object.pixelsRequest != null) {
                if (!$util.isObject(object.pixelsRequest))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.pixelsRequest: object expected");
                message.pixelsRequest = $root.appFrameProtoOut.PixelsAreaRequestMsgOutProto.fromObject(object.pixelsRequest, long + 1);
            }
            if (object.playback != null) {
                if (!$util.isObject(object.playback))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.playback: object expected");
                message.playback = $root.appFrameProtoOut.PlaybackInfoMsgOutProto.fromObject(object.playback, long + 1);
            }
            if (object.instanceId != null)
                message.instanceId = String(object.instanceId);
            if (object.startTimestamp != null)
                message.startTimestamp = String(object.startTimestamp);
            if (object.sendTimestamp != null)
                message.sendTimestamp = String(object.sendTimestamp);
            if (object.focusEvent != null) {
                if (!$util.isObject(object.focusEvent))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.focusEvent: object expected");
                message.focusEvent = $root.appFrameProtoOut.FocusEventMsgOutProto.fromObject(object.focusEvent, long + 1);
            }
            if (object.componentTree) {
                if (!Array.isArray(object.componentTree))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.componentTree: array expected");
                message.componentTree = [];
                for (var i = 0; i < object.componentTree.length; ++i) {
                    if (!$util.isObject(object.componentTree[i]))
                        throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.componentTree: object expected");
                    message.componentTree[i] = $root.appFrameProtoOut.ComponentTreeMsgOutProto.fromObject(object.componentTree[i], long + 1);
                }
            }
            if (object.directDraw != null)
                message.directDraw = Boolean(object.directDraw);
            if (object.actionEvent != null) {
                if (!$util.isObject(object.actionEvent))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.actionEvent: object expected");
                message.actionEvent = $root.appFrameProtoOut.ActionEventMsgOutProto.fromObject(object.actionEvent, long + 1);
            }
            if (object.compositingWM != null)
                message.compositingWM = Boolean(object.compositingWM);
            if (object.audioEvent != null) {
                if (!$util.isObject(object.audioEvent))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.audioEvent: object expected");
                message.audioEvent = $root.appFrameProtoOut.AudioEventMsgOutProto.fromObject(object.audioEvent, long + 1);
            }
            if (object.accessible != null) {
                if (!$util.isObject(object.accessible))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.accessible: object expected");
                message.accessible = $root.appFrameProtoOut.AccessibilityMsgOutProto.fromObject(object.accessible, long + 1);
            }
            if (object.windowSwitchList) {
                if (!Array.isArray(object.windowSwitchList))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.windowSwitchList: array expected");
                message.windowSwitchList = [];
                for (var i = 0; i < object.windowSwitchList.length; ++i) {
                    if (!$util.isObject(object.windowSwitchList[i]))
                        throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.windowSwitchList: object expected");
                    message.windowSwitchList[i] = $root.appFrameProtoOut.WindowSwitchMsgOutProto.fromObject(object.windowSwitchList[i], long + 1);
                }
            }
            if (object.cursorChangeEvent != null) {
                if (!$util.isObject(object.cursorChangeEvent))
                    throw TypeError(".appFrameProtoOut.AppFrameMsgOutProto.cursorChangeEvent: object expected");
                message.cursorChangeEvent = $root.appFrameProtoOut.CursorChangeEventMsgOutProto.fromObject(object.cursorChangeEvent, long + 1);
            }
            return message;
        };

        /**
         * Creates a plain object from an AppFrameMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @static
         * @param {appFrameProtoOut.AppFrameMsgOutProto} message AppFrameMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        AppFrameMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.arrays || options.defaults) {
                object.windows = [];
                object.componentTree = [];
                object.windowSwitchList = [];
            }
            if (options.defaults) {
                object.startApplication = null;
                object.linkAction = null;
                object.moveAction = null;
                object.copyEvent = null;
                object.pasteRequest = null;
                object.fileDialogEvent = null;
                object.closedWindow = null;
                object.event = options.enums === String ? "applicationAlreadyRunning" : 0;
                object.jsRequest = null;
                object.javaResponse = null;
                object.pixelsRequest = null;
                object.playback = null;
                object.instanceId = "";
                object.startTimestamp = "";
                object.sendTimestamp = "";
                object.focusEvent = null;
                object.directDraw = false;
                object.actionEvent = null;
                object.compositingWM = false;
                object.audioEvent = null;
                object.accessible = null;
                object.cursorChangeEvent = null;
            }
            if (message.startApplication != null && Object.hasOwnProperty.call(message, "startApplication"))
                object.startApplication = $root.appFrameProtoOut.StartApplicationMsgOutProto.toObject(message.startApplication, options, q + 1);
            if (message.linkAction != null && Object.hasOwnProperty.call(message, "linkAction"))
                object.linkAction = $root.appFrameProtoOut.LinkActionMsgOutProto.toObject(message.linkAction, options, q + 1);
            if (message.moveAction != null && Object.hasOwnProperty.call(message, "moveAction"))
                object.moveAction = $root.appFrameProtoOut.WindowMoveActionMsgOutProto.toObject(message.moveAction, options, q + 1);
            if (message.copyEvent != null && Object.hasOwnProperty.call(message, "copyEvent"))
                object.copyEvent = $root.appFrameProtoOut.CopyEventMsgOutProto.toObject(message.copyEvent, options, q + 1);
            if (message.pasteRequest != null && Object.hasOwnProperty.call(message, "pasteRequest"))
                object.pasteRequest = $root.appFrameProtoOut.PasteRequestMsgOutProto.toObject(message.pasteRequest, options, q + 1);
            if (message.fileDialogEvent != null && Object.hasOwnProperty.call(message, "fileDialogEvent"))
                object.fileDialogEvent = $root.appFrameProtoOut.FileDialogEventMsgOutProto.toObject(message.fileDialogEvent, options, q + 1);
            if (message.windows && message.windows.length) {
                object.windows = [];
                for (var j = 0; j < message.windows.length; ++j)
                    object.windows[j] = $root.appFrameProtoOut.WindowMsgOutProto.toObject(message.windows[j], options, q + 1);
            }
            if (message.closedWindow != null && Object.hasOwnProperty.call(message, "closedWindow"))
                object.closedWindow = $root.appFrameProtoOut.WindowMsgOutProto.toObject(message.closedWindow, options, q + 1);
            if (message.event != null && Object.hasOwnProperty.call(message, "event"))
                object.event = options.enums === String ? $root.appFrameProtoOut.SimpleEventMsgOutProto[message.event] === undefined ? message.event : $root.appFrameProtoOut.SimpleEventMsgOutProto[message.event] : message.event;
            if (message.jsRequest != null && Object.hasOwnProperty.call(message, "jsRequest"))
                object.jsRequest = $root.appFrameProtoOut.JsEvalRequestMsgOutProto.toObject(message.jsRequest, options, q + 1);
            if (message.javaResponse != null && Object.hasOwnProperty.call(message, "javaResponse"))
                object.javaResponse = $root.appFrameProtoOut.JsResultMsgOutProto.toObject(message.javaResponse, options, q + 1);
            if (message.pixelsRequest != null && Object.hasOwnProperty.call(message, "pixelsRequest"))
                object.pixelsRequest = $root.appFrameProtoOut.PixelsAreaRequestMsgOutProto.toObject(message.pixelsRequest, options, q + 1);
            if (message.playback != null && Object.hasOwnProperty.call(message, "playback"))
                object.playback = $root.appFrameProtoOut.PlaybackInfoMsgOutProto.toObject(message.playback, options, q + 1);
            if (message.instanceId != null && Object.hasOwnProperty.call(message, "instanceId"))
                object.instanceId = message.instanceId;
            if (message.startTimestamp != null && Object.hasOwnProperty.call(message, "startTimestamp"))
                object.startTimestamp = message.startTimestamp;
            if (message.sendTimestamp != null && Object.hasOwnProperty.call(message, "sendTimestamp"))
                object.sendTimestamp = message.sendTimestamp;
            if (message.focusEvent != null && Object.hasOwnProperty.call(message, "focusEvent"))
                object.focusEvent = $root.appFrameProtoOut.FocusEventMsgOutProto.toObject(message.focusEvent, options, q + 1);
            if (message.componentTree && message.componentTree.length) {
                object.componentTree = [];
                for (var j = 0; j < message.componentTree.length; ++j)
                    object.componentTree[j] = $root.appFrameProtoOut.ComponentTreeMsgOutProto.toObject(message.componentTree[j], options, q + 1);
            }
            if (message.directDraw != null && Object.hasOwnProperty.call(message, "directDraw"))
                object.directDraw = message.directDraw;
            if (message.actionEvent != null && Object.hasOwnProperty.call(message, "actionEvent"))
                object.actionEvent = $root.appFrameProtoOut.ActionEventMsgOutProto.toObject(message.actionEvent, options, q + 1);
            if (message.compositingWM != null && Object.hasOwnProperty.call(message, "compositingWM"))
                object.compositingWM = message.compositingWM;
            if (message.audioEvent != null && Object.hasOwnProperty.call(message, "audioEvent"))
                object.audioEvent = $root.appFrameProtoOut.AudioEventMsgOutProto.toObject(message.audioEvent, options, q + 1);
            if (message.accessible != null && Object.hasOwnProperty.call(message, "accessible"))
                object.accessible = $root.appFrameProtoOut.AccessibilityMsgOutProto.toObject(message.accessible, options, q + 1);
            if (message.windowSwitchList && message.windowSwitchList.length) {
                object.windowSwitchList = [];
                for (var j = 0; j < message.windowSwitchList.length; ++j)
                    object.windowSwitchList[j] = $root.appFrameProtoOut.WindowSwitchMsgOutProto.toObject(message.windowSwitchList[j], options, q + 1);
            }
            if (message.cursorChangeEvent != null && Object.hasOwnProperty.call(message, "cursorChangeEvent"))
                object.cursorChangeEvent = $root.appFrameProtoOut.CursorChangeEventMsgOutProto.toObject(message.cursorChangeEvent, options, q + 1);
            return object;
        };

        /**
         * Converts this AppFrameMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        AppFrameMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for AppFrameMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.AppFrameMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        AppFrameMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.AppFrameMsgOutProto";
        };

        return AppFrameMsgOutProto;
    })();

    appFrameProtoOut.AccessibilityMsgOutProto = (function() {

        /**
         * Properties of an AccessibilityMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IAccessibilityMsgOutProto
         * @property {string|null} [id] AccessibilityMsgOutProto id
         * @property {string|null} [role] AccessibilityMsgOutProto role
         * @property {string|null} [text] AccessibilityMsgOutProto text
         * @property {string|null} [tooltip] AccessibilityMsgOutProto tooltip
         * @property {string|null} [value] AccessibilityMsgOutProto value
         * @property {string|null} [description] AccessibilityMsgOutProto description
         * @property {string|null} [columnheader] AccessibilityMsgOutProto columnheader
         * @property {boolean|null} [password] AccessibilityMsgOutProto password
         * @property {boolean|null} [toggle] AccessibilityMsgOutProto toggle
         * @property {number|null} [selstart] AccessibilityMsgOutProto selstart
         * @property {number|null} [selend] AccessibilityMsgOutProto selend
         * @property {number|null} [rowheight] AccessibilityMsgOutProto rowheight
         * @property {number|null} [rows] AccessibilityMsgOutProto rows
         * @property {number|null} [size] AccessibilityMsgOutProto size
         * @property {number|null} [position] AccessibilityMsgOutProto position
         * @property {number|null} [level] AccessibilityMsgOutProto level
         * @property {number|null} [colindex] AccessibilityMsgOutProto colindex
         * @property {number|null} [rowindex] AccessibilityMsgOutProto rowindex
         * @property {number|null} [colcount] AccessibilityMsgOutProto colcount
         * @property {number|null} [rowcount] AccessibilityMsgOutProto rowcount
         * @property {Array.<string>|null} [states] AccessibilityMsgOutProto states
         * @property {number|null} [min] AccessibilityMsgOutProto min
         * @property {number|null} [max] AccessibilityMsgOutProto max
         * @property {number|null} [val] AccessibilityMsgOutProto val
         * @property {number|null} [screenX] AccessibilityMsgOutProto screenX
         * @property {number|null} [screenY] AccessibilityMsgOutProto screenY
         * @property {number|null} [width] AccessibilityMsgOutProto width
         * @property {number|null} [height] AccessibilityMsgOutProto height
         * @property {Array.<appFrameProtoOut.IAccessibilityHierarchyMsgOutProto>|null} [hierarchy] AccessibilityMsgOutProto hierarchy
         */

        /**
         * Constructs a new AccessibilityMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents an AccessibilityMsgOutProto.
         * @implements IAccessibilityMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IAccessibilityMsgOutProto=} [properties] Properties to set
         */
        function AccessibilityMsgOutProto(properties) {
            this.states = [];
            this.hierarchy = [];
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * AccessibilityMsgOutProto id.
         * @member {string} id
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.id = "";

        /**
         * AccessibilityMsgOutProto role.
         * @member {string} role
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.role = "";

        /**
         * AccessibilityMsgOutProto text.
         * @member {string} text
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.text = "";

        /**
         * AccessibilityMsgOutProto tooltip.
         * @member {string} tooltip
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.tooltip = "";

        /**
         * AccessibilityMsgOutProto value.
         * @member {string} value
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.value = "";

        /**
         * AccessibilityMsgOutProto description.
         * @member {string} description
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.description = "";

        /**
         * AccessibilityMsgOutProto columnheader.
         * @member {string} columnheader
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.columnheader = "";

        /**
         * AccessibilityMsgOutProto password.
         * @member {boolean} password
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.password = false;

        /**
         * AccessibilityMsgOutProto toggle.
         * @member {boolean} toggle
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.toggle = false;

        /**
         * AccessibilityMsgOutProto selstart.
         * @member {number} selstart
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.selstart = 0;

        /**
         * AccessibilityMsgOutProto selend.
         * @member {number} selend
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.selend = 0;

        /**
         * AccessibilityMsgOutProto rowheight.
         * @member {number} rowheight
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.rowheight = 0;

        /**
         * AccessibilityMsgOutProto rows.
         * @member {number} rows
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.rows = 0;

        /**
         * AccessibilityMsgOutProto size.
         * @member {number} size
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.size = 0;

        /**
         * AccessibilityMsgOutProto position.
         * @member {number} position
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.position = 0;

        /**
         * AccessibilityMsgOutProto level.
         * @member {number} level
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.level = 0;

        /**
         * AccessibilityMsgOutProto colindex.
         * @member {number} colindex
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.colindex = 0;

        /**
         * AccessibilityMsgOutProto rowindex.
         * @member {number} rowindex
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.rowindex = 0;

        /**
         * AccessibilityMsgOutProto colcount.
         * @member {number} colcount
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.colcount = 0;

        /**
         * AccessibilityMsgOutProto rowcount.
         * @member {number} rowcount
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.rowcount = 0;

        /**
         * AccessibilityMsgOutProto states.
         * @member {Array.<string>} states
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.states = $util.emptyArray;

        /**
         * AccessibilityMsgOutProto min.
         * @member {number} min
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.min = 0;

        /**
         * AccessibilityMsgOutProto max.
         * @member {number} max
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.max = 0;

        /**
         * AccessibilityMsgOutProto val.
         * @member {number} val
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.val = 0;

        /**
         * AccessibilityMsgOutProto screenX.
         * @member {number} screenX
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.screenX = 0;

        /**
         * AccessibilityMsgOutProto screenY.
         * @member {number} screenY
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.screenY = 0;

        /**
         * AccessibilityMsgOutProto width.
         * @member {number} width
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.width = 0;

        /**
         * AccessibilityMsgOutProto height.
         * @member {number} height
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.height = 0;

        /**
         * AccessibilityMsgOutProto hierarchy.
         * @member {Array.<appFrameProtoOut.IAccessibilityHierarchyMsgOutProto>} hierarchy
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         */
        AccessibilityMsgOutProto.prototype.hierarchy = $util.emptyArray;

        /**
         * Creates a new AccessibilityMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @static
         * @param {appFrameProtoOut.IAccessibilityMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.AccessibilityMsgOutProto} AccessibilityMsgOutProto instance
         */
        AccessibilityMsgOutProto.create = function create(properties) {
            return new AccessibilityMsgOutProto(properties);
        };

        /**
         * Decodes an AccessibilityMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.AccessibilityMsgOutProto} AccessibilityMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        AccessibilityMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.AccessibilityMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.id = reader.string();
                        break;
                    }
                case 2: {
                        message.role = reader.string();
                        break;
                    }
                case 3: {
                        message.text = reader.string();
                        break;
                    }
                case 4: {
                        message.tooltip = reader.string();
                        break;
                    }
                case 5: {
                        message.value = reader.string();
                        break;
                    }
                case 6: {
                        message.description = reader.string();
                        break;
                    }
                case 7: {
                        message.columnheader = reader.string();
                        break;
                    }
                case 8: {
                        message.password = reader.bool();
                        break;
                    }
                case 9: {
                        message.toggle = reader.bool();
                        break;
                    }
                case 10: {
                        message.selstart = reader.sint32();
                        break;
                    }
                case 11: {
                        message.selend = reader.sint32();
                        break;
                    }
                case 12: {
                        message.rowheight = reader.sint32();
                        break;
                    }
                case 13: {
                        message.rows = reader.sint32();
                        break;
                    }
                case 14: {
                        message.size = reader.sint32();
                        break;
                    }
                case 15: {
                        message.position = reader.sint32();
                        break;
                    }
                case 16: {
                        message.level = reader.sint32();
                        break;
                    }
                case 17: {
                        message.colindex = reader.sint32();
                        break;
                    }
                case 18: {
                        message.rowindex = reader.sint32();
                        break;
                    }
                case 19: {
                        message.colcount = reader.sint32();
                        break;
                    }
                case 20: {
                        message.rowcount = reader.sint32();
                        break;
                    }
                case 21: {
                        if (!(message.states && message.states.length))
                            message.states = [];
                        message.states.push(reader.string());
                        break;
                    }
                case 22: {
                        message.min = reader.sint32();
                        break;
                    }
                case 23: {
                        message.max = reader.sint32();
                        break;
                    }
                case 24: {
                        message.val = reader.sint32();
                        break;
                    }
                case 25: {
                        message.screenX = reader.sint32();
                        break;
                    }
                case 26: {
                        message.screenY = reader.sint32();
                        break;
                    }
                case 27: {
                        message.width = reader.sint32();
                        break;
                    }
                case 28: {
                        message.height = reader.sint32();
                        break;
                    }
                case 29: {
                        if (!(message.hierarchy && message.hierarchy.length))
                            message.hierarchy = [];
                        message.hierarchy.push($root.appFrameProtoOut.AccessibilityHierarchyMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1));
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
         * Creates an AccessibilityMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.AccessibilityMsgOutProto} AccessibilityMsgOutProto
         */
        AccessibilityMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.AccessibilityMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.AccessibilityMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.AccessibilityMsgOutProto();
            if (object.id != null)
                message.id = String(object.id);
            if (object.role != null)
                message.role = String(object.role);
            if (object.text != null)
                message.text = String(object.text);
            if (object.tooltip != null)
                message.tooltip = String(object.tooltip);
            if (object.value != null)
                message.value = String(object.value);
            if (object.description != null)
                message.description = String(object.description);
            if (object.columnheader != null)
                message.columnheader = String(object.columnheader);
            if (object.password != null)
                message.password = Boolean(object.password);
            if (object.toggle != null)
                message.toggle = Boolean(object.toggle);
            if (object.selstart != null)
                message.selstart = object.selstart | 0;
            if (object.selend != null)
                message.selend = object.selend | 0;
            if (object.rowheight != null)
                message.rowheight = object.rowheight | 0;
            if (object.rows != null)
                message.rows = object.rows | 0;
            if (object.size != null)
                message.size = object.size | 0;
            if (object.position != null)
                message.position = object.position | 0;
            if (object.level != null)
                message.level = object.level | 0;
            if (object.colindex != null)
                message.colindex = object.colindex | 0;
            if (object.rowindex != null)
                message.rowindex = object.rowindex | 0;
            if (object.colcount != null)
                message.colcount = object.colcount | 0;
            if (object.rowcount != null)
                message.rowcount = object.rowcount | 0;
            if (object.states) {
                if (!Array.isArray(object.states))
                    throw TypeError(".appFrameProtoOut.AccessibilityMsgOutProto.states: array expected");
                message.states = [];
                for (var i = 0; i < object.states.length; ++i)
                    message.states[i] = String(object.states[i]);
            }
            if (object.min != null)
                message.min = object.min | 0;
            if (object.max != null)
                message.max = object.max | 0;
            if (object.val != null)
                message.val = object.val | 0;
            if (object.screenX != null)
                message.screenX = object.screenX | 0;
            if (object.screenY != null)
                message.screenY = object.screenY | 0;
            if (object.width != null)
                message.width = object.width | 0;
            if (object.height != null)
                message.height = object.height | 0;
            if (object.hierarchy) {
                if (!Array.isArray(object.hierarchy))
                    throw TypeError(".appFrameProtoOut.AccessibilityMsgOutProto.hierarchy: array expected");
                message.hierarchy = [];
                for (var i = 0; i < object.hierarchy.length; ++i) {
                    if (!$util.isObject(object.hierarchy[i]))
                        throw TypeError(".appFrameProtoOut.AccessibilityMsgOutProto.hierarchy: object expected");
                    message.hierarchy[i] = $root.appFrameProtoOut.AccessibilityHierarchyMsgOutProto.fromObject(object.hierarchy[i], long + 1);
                }
            }
            return message;
        };

        /**
         * Creates a plain object from an AccessibilityMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @static
         * @param {appFrameProtoOut.AccessibilityMsgOutProto} message AccessibilityMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        AccessibilityMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.arrays || options.defaults) {
                object.states = [];
                object.hierarchy = [];
            }
            if (options.defaults) {
                object.id = "";
                object.role = "";
                object.text = "";
                object.tooltip = "";
                object.value = "";
                object.description = "";
                object.columnheader = "";
                object.password = false;
                object.toggle = false;
                object.selstart = 0;
                object.selend = 0;
                object.rowheight = 0;
                object.rows = 0;
                object.size = 0;
                object.position = 0;
                object.level = 0;
                object.colindex = 0;
                object.rowindex = 0;
                object.colcount = 0;
                object.rowcount = 0;
                object.min = 0;
                object.max = 0;
                object.val = 0;
                object.screenX = 0;
                object.screenY = 0;
                object.width = 0;
                object.height = 0;
            }
            if (message.id != null && Object.hasOwnProperty.call(message, "id"))
                object.id = message.id;
            if (message.role != null && Object.hasOwnProperty.call(message, "role"))
                object.role = message.role;
            if (message.text != null && Object.hasOwnProperty.call(message, "text"))
                object.text = message.text;
            if (message.tooltip != null && Object.hasOwnProperty.call(message, "tooltip"))
                object.tooltip = message.tooltip;
            if (message.value != null && Object.hasOwnProperty.call(message, "value"))
                object.value = message.value;
            if (message.description != null && Object.hasOwnProperty.call(message, "description"))
                object.description = message.description;
            if (message.columnheader != null && Object.hasOwnProperty.call(message, "columnheader"))
                object.columnheader = message.columnheader;
            if (message.password != null && Object.hasOwnProperty.call(message, "password"))
                object.password = message.password;
            if (message.toggle != null && Object.hasOwnProperty.call(message, "toggle"))
                object.toggle = message.toggle;
            if (message.selstart != null && Object.hasOwnProperty.call(message, "selstart"))
                object.selstart = message.selstart;
            if (message.selend != null && Object.hasOwnProperty.call(message, "selend"))
                object.selend = message.selend;
            if (message.rowheight != null && Object.hasOwnProperty.call(message, "rowheight"))
                object.rowheight = message.rowheight;
            if (message.rows != null && Object.hasOwnProperty.call(message, "rows"))
                object.rows = message.rows;
            if (message.size != null && Object.hasOwnProperty.call(message, "size"))
                object.size = message.size;
            if (message.position != null && Object.hasOwnProperty.call(message, "position"))
                object.position = message.position;
            if (message.level != null && Object.hasOwnProperty.call(message, "level"))
                object.level = message.level;
            if (message.colindex != null && Object.hasOwnProperty.call(message, "colindex"))
                object.colindex = message.colindex;
            if (message.rowindex != null && Object.hasOwnProperty.call(message, "rowindex"))
                object.rowindex = message.rowindex;
            if (message.colcount != null && Object.hasOwnProperty.call(message, "colcount"))
                object.colcount = message.colcount;
            if (message.rowcount != null && Object.hasOwnProperty.call(message, "rowcount"))
                object.rowcount = message.rowcount;
            if (message.states && message.states.length) {
                object.states = [];
                for (var j = 0; j < message.states.length; ++j)
                    object.states[j] = message.states[j];
            }
            if (message.min != null && Object.hasOwnProperty.call(message, "min"))
                object.min = message.min;
            if (message.max != null && Object.hasOwnProperty.call(message, "max"))
                object.max = message.max;
            if (message.val != null && Object.hasOwnProperty.call(message, "val"))
                object.val = message.val;
            if (message.screenX != null && Object.hasOwnProperty.call(message, "screenX"))
                object.screenX = message.screenX;
            if (message.screenY != null && Object.hasOwnProperty.call(message, "screenY"))
                object.screenY = message.screenY;
            if (message.width != null && Object.hasOwnProperty.call(message, "width"))
                object.width = message.width;
            if (message.height != null && Object.hasOwnProperty.call(message, "height"))
                object.height = message.height;
            if (message.hierarchy && message.hierarchy.length) {
                object.hierarchy = [];
                for (var j = 0; j < message.hierarchy.length; ++j)
                    object.hierarchy[j] = $root.appFrameProtoOut.AccessibilityHierarchyMsgOutProto.toObject(message.hierarchy[j], options, q + 1);
            }
            return object;
        };

        /**
         * Converts this AccessibilityMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        AccessibilityMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for AccessibilityMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.AccessibilityMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        AccessibilityMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.AccessibilityMsgOutProto";
        };

        return AccessibilityMsgOutProto;
    })();

    appFrameProtoOut.AccessibilityHierarchyMsgOutProto = (function() {

        /**
         * Properties of an AccessibilityHierarchyMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IAccessibilityHierarchyMsgOutProto
         * @property {string|null} [id] AccessibilityHierarchyMsgOutProto id
         * @property {string|null} [role] AccessibilityHierarchyMsgOutProto role
         * @property {string|null} [text] AccessibilityHierarchyMsgOutProto text
         * @property {number|null} [position] AccessibilityHierarchyMsgOutProto position
         * @property {number|null} [size] AccessibilityHierarchyMsgOutProto size
         */

        /**
         * Constructs a new AccessibilityHierarchyMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents an AccessibilityHierarchyMsgOutProto.
         * @implements IAccessibilityHierarchyMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IAccessibilityHierarchyMsgOutProto=} [properties] Properties to set
         */
        function AccessibilityHierarchyMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * AccessibilityHierarchyMsgOutProto id.
         * @member {string} id
         * @memberof appFrameProtoOut.AccessibilityHierarchyMsgOutProto
         * @instance
         */
        AccessibilityHierarchyMsgOutProto.prototype.id = "";

        /**
         * AccessibilityHierarchyMsgOutProto role.
         * @member {string} role
         * @memberof appFrameProtoOut.AccessibilityHierarchyMsgOutProto
         * @instance
         */
        AccessibilityHierarchyMsgOutProto.prototype.role = "";

        /**
         * AccessibilityHierarchyMsgOutProto text.
         * @member {string} text
         * @memberof appFrameProtoOut.AccessibilityHierarchyMsgOutProto
         * @instance
         */
        AccessibilityHierarchyMsgOutProto.prototype.text = "";

        /**
         * AccessibilityHierarchyMsgOutProto position.
         * @member {number} position
         * @memberof appFrameProtoOut.AccessibilityHierarchyMsgOutProto
         * @instance
         */
        AccessibilityHierarchyMsgOutProto.prototype.position = 0;

        /**
         * AccessibilityHierarchyMsgOutProto size.
         * @member {number} size
         * @memberof appFrameProtoOut.AccessibilityHierarchyMsgOutProto
         * @instance
         */
        AccessibilityHierarchyMsgOutProto.prototype.size = 0;

        /**
         * Creates a new AccessibilityHierarchyMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.AccessibilityHierarchyMsgOutProto
         * @static
         * @param {appFrameProtoOut.IAccessibilityHierarchyMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.AccessibilityHierarchyMsgOutProto} AccessibilityHierarchyMsgOutProto instance
         */
        AccessibilityHierarchyMsgOutProto.create = function create(properties) {
            return new AccessibilityHierarchyMsgOutProto(properties);
        };

        /**
         * Decodes an AccessibilityHierarchyMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.AccessibilityHierarchyMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.AccessibilityHierarchyMsgOutProto} AccessibilityHierarchyMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        AccessibilityHierarchyMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.AccessibilityHierarchyMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.id = reader.string();
                        break;
                    }
                case 2: {
                        message.role = reader.string();
                        break;
                    }
                case 3: {
                        message.text = reader.string();
                        break;
                    }
                case 4: {
                        message.position = reader.sint32();
                        break;
                    }
                case 5: {
                        message.size = reader.sint32();
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
         * Creates an AccessibilityHierarchyMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.AccessibilityHierarchyMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.AccessibilityHierarchyMsgOutProto} AccessibilityHierarchyMsgOutProto
         */
        AccessibilityHierarchyMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.AccessibilityHierarchyMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.AccessibilityHierarchyMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.AccessibilityHierarchyMsgOutProto();
            if (object.id != null)
                message.id = String(object.id);
            if (object.role != null)
                message.role = String(object.role);
            if (object.text != null)
                message.text = String(object.text);
            if (object.position != null)
                message.position = object.position | 0;
            if (object.size != null)
                message.size = object.size | 0;
            return message;
        };

        /**
         * Creates a plain object from an AccessibilityHierarchyMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.AccessibilityHierarchyMsgOutProto
         * @static
         * @param {appFrameProtoOut.AccessibilityHierarchyMsgOutProto} message AccessibilityHierarchyMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        AccessibilityHierarchyMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.id = "";
                object.role = "";
                object.text = "";
                object.position = 0;
                object.size = 0;
            }
            if (message.id != null && Object.hasOwnProperty.call(message, "id"))
                object.id = message.id;
            if (message.role != null && Object.hasOwnProperty.call(message, "role"))
                object.role = message.role;
            if (message.text != null && Object.hasOwnProperty.call(message, "text"))
                object.text = message.text;
            if (message.position != null && Object.hasOwnProperty.call(message, "position"))
                object.position = message.position;
            if (message.size != null && Object.hasOwnProperty.call(message, "size"))
                object.size = message.size;
            return object;
        };

        /**
         * Converts this AccessibilityHierarchyMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.AccessibilityHierarchyMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        AccessibilityHierarchyMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for AccessibilityHierarchyMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.AccessibilityHierarchyMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        AccessibilityHierarchyMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.AccessibilityHierarchyMsgOutProto";
        };

        return AccessibilityHierarchyMsgOutProto;
    })();

    appFrameProtoOut.FocusEventMsgOutProto = (function() {

        /**
         * Properties of a FocusEventMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IFocusEventMsgOutProto
         * @property {appFrameProtoOut.FocusEventMsgOutProto.FocusEventTypeProto} type FocusEventMsgOutProto type
         * @property {number|null} [x] FocusEventMsgOutProto x
         * @property {number|null} [y] FocusEventMsgOutProto y
         * @property {number|null} [w] FocusEventMsgOutProto w
         * @property {number|null} [h] FocusEventMsgOutProto h
         * @property {number|null} [caretX] FocusEventMsgOutProto caretX
         * @property {number|null} [caretY] FocusEventMsgOutProto caretY
         * @property {number|null} [caretH] FocusEventMsgOutProto caretH
         * @property {boolean|null} [editable] FocusEventMsgOutProto editable
         */

        /**
         * Constructs a new FocusEventMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a FocusEventMsgOutProto.
         * @implements IFocusEventMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IFocusEventMsgOutProto=} [properties] Properties to set
         */
        function FocusEventMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * FocusEventMsgOutProto type.
         * @member {appFrameProtoOut.FocusEventMsgOutProto.FocusEventTypeProto} type
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @instance
         */
        FocusEventMsgOutProto.prototype.type = 1;

        /**
         * FocusEventMsgOutProto x.
         * @member {number} x
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @instance
         */
        FocusEventMsgOutProto.prototype.x = 0;

        /**
         * FocusEventMsgOutProto y.
         * @member {number} y
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @instance
         */
        FocusEventMsgOutProto.prototype.y = 0;

        /**
         * FocusEventMsgOutProto w.
         * @member {number} w
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @instance
         */
        FocusEventMsgOutProto.prototype.w = 0;

        /**
         * FocusEventMsgOutProto h.
         * @member {number} h
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @instance
         */
        FocusEventMsgOutProto.prototype.h = 0;

        /**
         * FocusEventMsgOutProto caretX.
         * @member {number} caretX
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @instance
         */
        FocusEventMsgOutProto.prototype.caretX = 0;

        /**
         * FocusEventMsgOutProto caretY.
         * @member {number} caretY
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @instance
         */
        FocusEventMsgOutProto.prototype.caretY = 0;

        /**
         * FocusEventMsgOutProto caretH.
         * @member {number} caretH
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @instance
         */
        FocusEventMsgOutProto.prototype.caretH = 0;

        /**
         * FocusEventMsgOutProto editable.
         * @member {boolean} editable
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @instance
         */
        FocusEventMsgOutProto.prototype.editable = false;

        /**
         * Creates a new FocusEventMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @static
         * @param {appFrameProtoOut.IFocusEventMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.FocusEventMsgOutProto} FocusEventMsgOutProto instance
         */
        FocusEventMsgOutProto.create = function create(properties) {
            return new FocusEventMsgOutProto(properties);
        };

        /**
         * Decodes a FocusEventMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.FocusEventMsgOutProto} FocusEventMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        FocusEventMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.FocusEventMsgOutProto();
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
                        message.x = reader.sint32();
                        break;
                    }
                case 3: {
                        message.y = reader.sint32();
                        break;
                    }
                case 4: {
                        message.w = reader.uint32();
                        break;
                    }
                case 5: {
                        message.h = reader.uint32();
                        break;
                    }
                case 6: {
                        message.caretX = reader.sint32();
                        break;
                    }
                case 7: {
                        message.caretY = reader.sint32();
                        break;
                    }
                case 8: {
                        message.caretH = reader.sint32();
                        break;
                    }
                case 9: {
                        message.editable = reader.bool();
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
         * Creates a FocusEventMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.FocusEventMsgOutProto} FocusEventMsgOutProto
         */
        FocusEventMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.FocusEventMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.FocusEventMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.FocusEventMsgOutProto();
            switch (object.type) {
            default:
                if (typeof object.type === "number") {
                    message.type = object.type;
                    break;
                }
                break;
            case "focusLost":
            case 1:
                message.type = 1;
                break;
            case "focusGained":
            case 2:
                message.type = 2;
                break;
            case "focusWithCarretGained":
            case 3:
                message.type = 3;
                break;
            case "focusPasswordGained":
            case 4:
                message.type = 4;
                break;
            }
            if (object.x != null)
                message.x = object.x | 0;
            if (object.y != null)
                message.y = object.y | 0;
            if (object.w != null)
                message.w = object.w >>> 0;
            if (object.h != null)
                message.h = object.h >>> 0;
            if (object.caretX != null)
                message.caretX = object.caretX | 0;
            if (object.caretY != null)
                message.caretY = object.caretY | 0;
            if (object.caretH != null)
                message.caretH = object.caretH | 0;
            if (object.editable != null)
                message.editable = Boolean(object.editable);
            return message;
        };

        /**
         * Creates a plain object from a FocusEventMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @static
         * @param {appFrameProtoOut.FocusEventMsgOutProto} message FocusEventMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        FocusEventMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.type = options.enums === String ? "focusLost" : 1;
                object.x = 0;
                object.y = 0;
                object.w = 0;
                object.h = 0;
                object.caretX = 0;
                object.caretY = 0;
                object.caretH = 0;
                object.editable = false;
            }
            if (message.type != null && Object.hasOwnProperty.call(message, "type"))
                object.type = options.enums === String ? $root.appFrameProtoOut.FocusEventMsgOutProto.FocusEventTypeProto[message.type] === undefined ? message.type : $root.appFrameProtoOut.FocusEventMsgOutProto.FocusEventTypeProto[message.type] : message.type;
            if (message.x != null && Object.hasOwnProperty.call(message, "x"))
                object.x = message.x;
            if (message.y != null && Object.hasOwnProperty.call(message, "y"))
                object.y = message.y;
            if (message.w != null && Object.hasOwnProperty.call(message, "w"))
                object.w = message.w;
            if (message.h != null && Object.hasOwnProperty.call(message, "h"))
                object.h = message.h;
            if (message.caretX != null && Object.hasOwnProperty.call(message, "caretX"))
                object.caretX = message.caretX;
            if (message.caretY != null && Object.hasOwnProperty.call(message, "caretY"))
                object.caretY = message.caretY;
            if (message.caretH != null && Object.hasOwnProperty.call(message, "caretH"))
                object.caretH = message.caretH;
            if (message.editable != null && Object.hasOwnProperty.call(message, "editable"))
                object.editable = message.editable;
            return object;
        };

        /**
         * Converts this FocusEventMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        FocusEventMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for FocusEventMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.FocusEventMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        FocusEventMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.FocusEventMsgOutProto";
        };

        /**
         * FocusEventTypeProto enum.
         * @name appFrameProtoOut.FocusEventMsgOutProto.FocusEventTypeProto
         * @enum {number}
         * @property {number} focusLost=1 focusLost value
         * @property {number} focusGained=2 focusGained value
         * @property {number} focusWithCarretGained=3 focusWithCarretGained value
         * @property {number} focusPasswordGained=4 focusPasswordGained value
         */
        FocusEventMsgOutProto.FocusEventTypeProto = (function() {
            var valuesById = {}, values = Object.create(valuesById);
            values[valuesById[1] = "focusLost"] = 1;
            values[valuesById[2] = "focusGained"] = 2;
            values[valuesById[3] = "focusWithCarretGained"] = 3;
            values[valuesById[4] = "focusPasswordGained"] = 4;
            return values;
        })();

        return FocusEventMsgOutProto;
    })();

    appFrameProtoOut.StartApplicationMsgOutProto = (function() {

        /**
         * Properties of a StartApplicationMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IStartApplicationMsgOutProto
         */

        /**
         * Constructs a new StartApplicationMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a StartApplicationMsgOutProto.
         * @implements IStartApplicationMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IStartApplicationMsgOutProto=} [properties] Properties to set
         */
        function StartApplicationMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * Creates a new StartApplicationMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.StartApplicationMsgOutProto
         * @static
         * @param {appFrameProtoOut.IStartApplicationMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.StartApplicationMsgOutProto} StartApplicationMsgOutProto instance
         */
        StartApplicationMsgOutProto.create = function create(properties) {
            return new StartApplicationMsgOutProto(properties);
        };

        /**
         * Decodes a StartApplicationMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.StartApplicationMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.StartApplicationMsgOutProto} StartApplicationMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        StartApplicationMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.StartApplicationMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                default:
                    reader.skipType(tag & 7, long);
                    break;
                }
            }
            return message;
        };

        /**
         * Creates a StartApplicationMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.StartApplicationMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.StartApplicationMsgOutProto} StartApplicationMsgOutProto
         */
        StartApplicationMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.StartApplicationMsgOutProto)
                return object;
            return new $root.appFrameProtoOut.StartApplicationMsgOutProto();
        };

        /**
         * Creates a plain object from a StartApplicationMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.StartApplicationMsgOutProto
         * @static
         * @param {appFrameProtoOut.StartApplicationMsgOutProto} message StartApplicationMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        StartApplicationMsgOutProto.toObject = function toObject() {
            return {};
        };

        /**
         * Converts this StartApplicationMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.StartApplicationMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        StartApplicationMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for StartApplicationMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.StartApplicationMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        StartApplicationMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.StartApplicationMsgOutProto";
        };

        return StartApplicationMsgOutProto;
    })();

    appFrameProtoOut.LinkActionMsgOutProto = (function() {

        /**
         * Properties of a LinkActionMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface ILinkActionMsgOutProto
         * @property {appFrameProtoOut.LinkActionMsgOutProto.LinkActionTypeProto} action LinkActionMsgOutProto action
         * @property {string} src LinkActionMsgOutProto src
         */

        /**
         * Constructs a new LinkActionMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a LinkActionMsgOutProto.
         * @implements ILinkActionMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.ILinkActionMsgOutProto=} [properties] Properties to set
         */
        function LinkActionMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * LinkActionMsgOutProto action.
         * @member {appFrameProtoOut.LinkActionMsgOutProto.LinkActionTypeProto} action
         * @memberof appFrameProtoOut.LinkActionMsgOutProto
         * @instance
         */
        LinkActionMsgOutProto.prototype.action = 0;

        /**
         * LinkActionMsgOutProto src.
         * @member {string} src
         * @memberof appFrameProtoOut.LinkActionMsgOutProto
         * @instance
         */
        LinkActionMsgOutProto.prototype.src = "";

        /**
         * Creates a new LinkActionMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.LinkActionMsgOutProto
         * @static
         * @param {appFrameProtoOut.ILinkActionMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.LinkActionMsgOutProto} LinkActionMsgOutProto instance
         */
        LinkActionMsgOutProto.create = function create(properties) {
            return new LinkActionMsgOutProto(properties);
        };

        /**
         * Decodes a LinkActionMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.LinkActionMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.LinkActionMsgOutProto} LinkActionMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        LinkActionMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.LinkActionMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.action = reader.int32();
                        break;
                    }
                case 2: {
                        message.src = reader.string();
                        break;
                    }
                default:
                    reader.skipType(tag & 7, long);
                    break;
                }
            }
            if (!Object.hasOwnProperty.call(message, "action"))
                throw $util.ProtocolError("missing required 'action'", { instance: message });
            if (!Object.hasOwnProperty.call(message, "src"))
                throw $util.ProtocolError("missing required 'src'", { instance: message });
            return message;
        };

        /**
         * Creates a LinkActionMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.LinkActionMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.LinkActionMsgOutProto} LinkActionMsgOutProto
         */
        LinkActionMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.LinkActionMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.LinkActionMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.LinkActionMsgOutProto();
            switch (object.action) {
            default:
                if (typeof object.action === "number") {
                    message.action = object.action;
                    break;
                }
                break;
            case "file":
            case 0:
                message.action = 0;
                break;
            case "url":
            case 1:
                message.action = 1;
                break;
            case "print":
            case 2:
                message.action = 2;
                break;
            case "redirect":
            case 3:
                message.action = 3;
                break;
            }
            if (object.src != null)
                message.src = String(object.src);
            return message;
        };

        /**
         * Creates a plain object from a LinkActionMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.LinkActionMsgOutProto
         * @static
         * @param {appFrameProtoOut.LinkActionMsgOutProto} message LinkActionMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        LinkActionMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.action = options.enums === String ? "file" : 0;
                object.src = "";
            }
            if (message.action != null && Object.hasOwnProperty.call(message, "action"))
                object.action = options.enums === String ? $root.appFrameProtoOut.LinkActionMsgOutProto.LinkActionTypeProto[message.action] === undefined ? message.action : $root.appFrameProtoOut.LinkActionMsgOutProto.LinkActionTypeProto[message.action] : message.action;
            if (message.src != null && Object.hasOwnProperty.call(message, "src"))
                object.src = message.src;
            return object;
        };

        /**
         * Converts this LinkActionMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.LinkActionMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        LinkActionMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for LinkActionMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.LinkActionMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        LinkActionMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.LinkActionMsgOutProto";
        };

        /**
         * LinkActionTypeProto enum.
         * @name appFrameProtoOut.LinkActionMsgOutProto.LinkActionTypeProto
         * @enum {number}
         * @property {number} file=0 file value
         * @property {number} url=1 url value
         * @property {number} print=2 print value
         * @property {number} redirect=3 redirect value
         */
        LinkActionMsgOutProto.LinkActionTypeProto = (function() {
            var valuesById = {}, values = Object.create(valuesById);
            values[valuesById[0] = "file"] = 0;
            values[valuesById[1] = "url"] = 1;
            values[valuesById[2] = "print"] = 2;
            values[valuesById[3] = "redirect"] = 3;
            return values;
        })();

        return LinkActionMsgOutProto;
    })();

    appFrameProtoOut.WindowMoveActionMsgOutProto = (function() {

        /**
         * Properties of a WindowMoveActionMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IWindowMoveActionMsgOutProto
         * @property {number|null} [sx] WindowMoveActionMsgOutProto sx
         * @property {number|null} [sy] WindowMoveActionMsgOutProto sy
         * @property {number|null} [dx] WindowMoveActionMsgOutProto dx
         * @property {number|null} [dy] WindowMoveActionMsgOutProto dy
         * @property {number|null} [width] WindowMoveActionMsgOutProto width
         * @property {number|null} [height] WindowMoveActionMsgOutProto height
         */

        /**
         * Constructs a new WindowMoveActionMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a WindowMoveActionMsgOutProto.
         * @implements IWindowMoveActionMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IWindowMoveActionMsgOutProto=} [properties] Properties to set
         */
        function WindowMoveActionMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * WindowMoveActionMsgOutProto sx.
         * @member {number} sx
         * @memberof appFrameProtoOut.WindowMoveActionMsgOutProto
         * @instance
         */
        WindowMoveActionMsgOutProto.prototype.sx = 0;

        /**
         * WindowMoveActionMsgOutProto sy.
         * @member {number} sy
         * @memberof appFrameProtoOut.WindowMoveActionMsgOutProto
         * @instance
         */
        WindowMoveActionMsgOutProto.prototype.sy = 0;

        /**
         * WindowMoveActionMsgOutProto dx.
         * @member {number} dx
         * @memberof appFrameProtoOut.WindowMoveActionMsgOutProto
         * @instance
         */
        WindowMoveActionMsgOutProto.prototype.dx = 0;

        /**
         * WindowMoveActionMsgOutProto dy.
         * @member {number} dy
         * @memberof appFrameProtoOut.WindowMoveActionMsgOutProto
         * @instance
         */
        WindowMoveActionMsgOutProto.prototype.dy = 0;

        /**
         * WindowMoveActionMsgOutProto width.
         * @member {number} width
         * @memberof appFrameProtoOut.WindowMoveActionMsgOutProto
         * @instance
         */
        WindowMoveActionMsgOutProto.prototype.width = 0;

        /**
         * WindowMoveActionMsgOutProto height.
         * @member {number} height
         * @memberof appFrameProtoOut.WindowMoveActionMsgOutProto
         * @instance
         */
        WindowMoveActionMsgOutProto.prototype.height = 0;

        /**
         * Creates a new WindowMoveActionMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.WindowMoveActionMsgOutProto
         * @static
         * @param {appFrameProtoOut.IWindowMoveActionMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.WindowMoveActionMsgOutProto} WindowMoveActionMsgOutProto instance
         */
        WindowMoveActionMsgOutProto.create = function create(properties) {
            return new WindowMoveActionMsgOutProto(properties);
        };

        /**
         * Decodes a WindowMoveActionMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.WindowMoveActionMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.WindowMoveActionMsgOutProto} WindowMoveActionMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        WindowMoveActionMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.WindowMoveActionMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.sx = reader.sint32();
                        break;
                    }
                case 2: {
                        message.sy = reader.sint32();
                        break;
                    }
                case 3: {
                        message.dx = reader.sint32();
                        break;
                    }
                case 4: {
                        message.dy = reader.sint32();
                        break;
                    }
                case 5: {
                        message.width = reader.uint32();
                        break;
                    }
                case 6: {
                        message.height = reader.uint32();
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
         * Creates a WindowMoveActionMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.WindowMoveActionMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.WindowMoveActionMsgOutProto} WindowMoveActionMsgOutProto
         */
        WindowMoveActionMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.WindowMoveActionMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.WindowMoveActionMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.WindowMoveActionMsgOutProto();
            if (object.sx != null)
                message.sx = object.sx | 0;
            if (object.sy != null)
                message.sy = object.sy | 0;
            if (object.dx != null)
                message.dx = object.dx | 0;
            if (object.dy != null)
                message.dy = object.dy | 0;
            if (object.width != null)
                message.width = object.width >>> 0;
            if (object.height != null)
                message.height = object.height >>> 0;
            return message;
        };

        /**
         * Creates a plain object from a WindowMoveActionMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.WindowMoveActionMsgOutProto
         * @static
         * @param {appFrameProtoOut.WindowMoveActionMsgOutProto} message WindowMoveActionMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        WindowMoveActionMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.sx = 0;
                object.sy = 0;
                object.dx = 0;
                object.dy = 0;
                object.width = 0;
                object.height = 0;
            }
            if (message.sx != null && Object.hasOwnProperty.call(message, "sx"))
                object.sx = message.sx;
            if (message.sy != null && Object.hasOwnProperty.call(message, "sy"))
                object.sy = message.sy;
            if (message.dx != null && Object.hasOwnProperty.call(message, "dx"))
                object.dx = message.dx;
            if (message.dy != null && Object.hasOwnProperty.call(message, "dy"))
                object.dy = message.dy;
            if (message.width != null && Object.hasOwnProperty.call(message, "width"))
                object.width = message.width;
            if (message.height != null && Object.hasOwnProperty.call(message, "height"))
                object.height = message.height;
            return object;
        };

        /**
         * Converts this WindowMoveActionMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.WindowMoveActionMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        WindowMoveActionMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for WindowMoveActionMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.WindowMoveActionMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        WindowMoveActionMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.WindowMoveActionMsgOutProto";
        };

        return WindowMoveActionMsgOutProto;
    })();

    appFrameProtoOut.CopyEventMsgOutProto = (function() {

        /**
         * Properties of a CopyEventMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface ICopyEventMsgOutProto
         * @property {string|null} [text] CopyEventMsgOutProto text
         * @property {string|null} [html] CopyEventMsgOutProto html
         * @property {Uint8Array|null} [img] CopyEventMsgOutProto img
         * @property {Array.<string>|null} [files] CopyEventMsgOutProto files
         * @property {boolean|null} [other] CopyEventMsgOutProto other
         */

        /**
         * Constructs a new CopyEventMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a CopyEventMsgOutProto.
         * @implements ICopyEventMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.ICopyEventMsgOutProto=} [properties] Properties to set
         */
        function CopyEventMsgOutProto(properties) {
            this.files = [];
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * CopyEventMsgOutProto text.
         * @member {string} text
         * @memberof appFrameProtoOut.CopyEventMsgOutProto
         * @instance
         */
        CopyEventMsgOutProto.prototype.text = "";

        /**
         * CopyEventMsgOutProto html.
         * @member {string} html
         * @memberof appFrameProtoOut.CopyEventMsgOutProto
         * @instance
         */
        CopyEventMsgOutProto.prototype.html = "";

        /**
         * CopyEventMsgOutProto img.
         * @member {Uint8Array} img
         * @memberof appFrameProtoOut.CopyEventMsgOutProto
         * @instance
         */
        CopyEventMsgOutProto.prototype.img = $util.newBuffer([]);

        /**
         * CopyEventMsgOutProto files.
         * @member {Array.<string>} files
         * @memberof appFrameProtoOut.CopyEventMsgOutProto
         * @instance
         */
        CopyEventMsgOutProto.prototype.files = $util.emptyArray;

        /**
         * CopyEventMsgOutProto other.
         * @member {boolean} other
         * @memberof appFrameProtoOut.CopyEventMsgOutProto
         * @instance
         */
        CopyEventMsgOutProto.prototype.other = false;

        /**
         * Creates a new CopyEventMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.CopyEventMsgOutProto
         * @static
         * @param {appFrameProtoOut.ICopyEventMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.CopyEventMsgOutProto} CopyEventMsgOutProto instance
         */
        CopyEventMsgOutProto.create = function create(properties) {
            return new CopyEventMsgOutProto(properties);
        };

        /**
         * Decodes a CopyEventMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.CopyEventMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.CopyEventMsgOutProto} CopyEventMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        CopyEventMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.CopyEventMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.text = reader.string();
                        break;
                    }
                case 2: {
                        message.html = reader.string();
                        break;
                    }
                case 3: {
                        message.img = reader.bytes();
                        break;
                    }
                case 4: {
                        if (!(message.files && message.files.length))
                            message.files = [];
                        message.files.push(reader.string());
                        break;
                    }
                case 5: {
                        message.other = reader.bool();
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
         * Creates a CopyEventMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.CopyEventMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.CopyEventMsgOutProto} CopyEventMsgOutProto
         */
        CopyEventMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.CopyEventMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.CopyEventMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.CopyEventMsgOutProto();
            if (object.text != null)
                message.text = String(object.text);
            if (object.html != null)
                message.html = String(object.html);
            if (object.img != null)
                if (typeof object.img === "string")
                    $util.base64.decode(object.img, message.img = $util.newBuffer($util.base64.length(object.img)), 0);
                else if (object.img.length >= 0)
                    message.img = object.img;
            if (object.files) {
                if (!Array.isArray(object.files))
                    throw TypeError(".appFrameProtoOut.CopyEventMsgOutProto.files: array expected");
                message.files = [];
                for (var i = 0; i < object.files.length; ++i)
                    message.files[i] = String(object.files[i]);
            }
            if (object.other != null)
                message.other = Boolean(object.other);
            return message;
        };

        /**
         * Creates a plain object from a CopyEventMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.CopyEventMsgOutProto
         * @static
         * @param {appFrameProtoOut.CopyEventMsgOutProto} message CopyEventMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        CopyEventMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.arrays || options.defaults)
                object.files = [];
            if (options.defaults) {
                object.text = "";
                object.html = "";
                if (options.bytes === String)
                    object.img = "";
                else {
                    object.img = [];
                    if (options.bytes !== Array)
                        object.img = $util.newBuffer(object.img);
                }
                object.other = false;
            }
            if (message.text != null && Object.hasOwnProperty.call(message, "text"))
                object.text = message.text;
            if (message.html != null && Object.hasOwnProperty.call(message, "html"))
                object.html = message.html;
            if (message.img != null && Object.hasOwnProperty.call(message, "img"))
                object.img = options.bytes === String ? $util.base64.encode(message.img, 0, message.img.length) : options.bytes === Array ? Array.prototype.slice.call(message.img) : message.img;
            if (message.files && message.files.length) {
                object.files = [];
                for (var j = 0; j < message.files.length; ++j)
                    object.files[j] = message.files[j];
            }
            if (message.other != null && Object.hasOwnProperty.call(message, "other"))
                object.other = message.other;
            return object;
        };

        /**
         * Converts this CopyEventMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.CopyEventMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        CopyEventMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for CopyEventMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.CopyEventMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        CopyEventMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.CopyEventMsgOutProto";
        };

        return CopyEventMsgOutProto;
    })();

    appFrameProtoOut.PasteRequestMsgOutProto = (function() {

        /**
         * Properties of a PasteRequestMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IPasteRequestMsgOutProto
         * @property {string|null} [title] PasteRequestMsgOutProto title
         * @property {string|null} [message] PasteRequestMsgOutProto message
         */

        /**
         * Constructs a new PasteRequestMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a PasteRequestMsgOutProto.
         * @implements IPasteRequestMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IPasteRequestMsgOutProto=} [properties] Properties to set
         */
        function PasteRequestMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * PasteRequestMsgOutProto title.
         * @member {string} title
         * @memberof appFrameProtoOut.PasteRequestMsgOutProto
         * @instance
         */
        PasteRequestMsgOutProto.prototype.title = "";

        /**
         * PasteRequestMsgOutProto message.
         * @member {string} message
         * @memberof appFrameProtoOut.PasteRequestMsgOutProto
         * @instance
         */
        PasteRequestMsgOutProto.prototype.message = "";

        /**
         * Creates a new PasteRequestMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.PasteRequestMsgOutProto
         * @static
         * @param {appFrameProtoOut.IPasteRequestMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.PasteRequestMsgOutProto} PasteRequestMsgOutProto instance
         */
        PasteRequestMsgOutProto.create = function create(properties) {
            return new PasteRequestMsgOutProto(properties);
        };

        /**
         * Decodes a PasteRequestMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.PasteRequestMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.PasteRequestMsgOutProto} PasteRequestMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        PasteRequestMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.PasteRequestMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.title = reader.string();
                        break;
                    }
                case 2: {
                        message.message = reader.string();
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
         * Creates a PasteRequestMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.PasteRequestMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.PasteRequestMsgOutProto} PasteRequestMsgOutProto
         */
        PasteRequestMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.PasteRequestMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.PasteRequestMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.PasteRequestMsgOutProto();
            if (object.title != null)
                message.title = String(object.title);
            if (object.message != null)
                message.message = String(object.message);
            return message;
        };

        /**
         * Creates a plain object from a PasteRequestMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.PasteRequestMsgOutProto
         * @static
         * @param {appFrameProtoOut.PasteRequestMsgOutProto} message PasteRequestMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        PasteRequestMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.title = "";
                object.message = "";
            }
            if (message.title != null && Object.hasOwnProperty.call(message, "title"))
                object.title = message.title;
            if (message.message != null && Object.hasOwnProperty.call(message, "message"))
                object.message = message.message;
            return object;
        };

        /**
         * Converts this PasteRequestMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.PasteRequestMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        PasteRequestMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for PasteRequestMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.PasteRequestMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        PasteRequestMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.PasteRequestMsgOutProto";
        };

        return PasteRequestMsgOutProto;
    })();

    appFrameProtoOut.FileDialogEventMsgOutProto = (function() {

        /**
         * Properties of a FileDialogEventMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IFileDialogEventMsgOutProto
         * @property {appFrameProtoOut.FileDialogEventMsgOutProto.FileDialogEventTypeProto} eventType FileDialogEventMsgOutProto eventType
         * @property {boolean|null} [allowDownload] FileDialogEventMsgOutProto allowDownload
         * @property {boolean|null} [allowUpload] FileDialogEventMsgOutProto allowUpload
         * @property {boolean|null} [allowDelete] FileDialogEventMsgOutProto allowDelete
         * @property {string|null} [filter] FileDialogEventMsgOutProto filter
         * @property {boolean|null} [isMultiSelection] FileDialogEventMsgOutProto isMultiSelection
         * @property {string|null} [selection] FileDialogEventMsgOutProto selection
         * @property {boolean|null} [customDialog] FileDialogEventMsgOutProto customDialog
         */

        /**
         * Constructs a new FileDialogEventMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a FileDialogEventMsgOutProto.
         * @implements IFileDialogEventMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IFileDialogEventMsgOutProto=} [properties] Properties to set
         */
        function FileDialogEventMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * FileDialogEventMsgOutProto eventType.
         * @member {appFrameProtoOut.FileDialogEventMsgOutProto.FileDialogEventTypeProto} eventType
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @instance
         */
        FileDialogEventMsgOutProto.prototype.eventType = 0;

        /**
         * FileDialogEventMsgOutProto allowDownload.
         * @member {boolean} allowDownload
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @instance
         */
        FileDialogEventMsgOutProto.prototype.allowDownload = false;

        /**
         * FileDialogEventMsgOutProto allowUpload.
         * @member {boolean} allowUpload
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @instance
         */
        FileDialogEventMsgOutProto.prototype.allowUpload = false;

        /**
         * FileDialogEventMsgOutProto allowDelete.
         * @member {boolean} allowDelete
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @instance
         */
        FileDialogEventMsgOutProto.prototype.allowDelete = false;

        /**
         * FileDialogEventMsgOutProto filter.
         * @member {string} filter
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @instance
         */
        FileDialogEventMsgOutProto.prototype.filter = "";

        /**
         * FileDialogEventMsgOutProto isMultiSelection.
         * @member {boolean} isMultiSelection
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @instance
         */
        FileDialogEventMsgOutProto.prototype.isMultiSelection = false;

        /**
         * FileDialogEventMsgOutProto selection.
         * @member {string} selection
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @instance
         */
        FileDialogEventMsgOutProto.prototype.selection = "";

        /**
         * FileDialogEventMsgOutProto customDialog.
         * @member {boolean} customDialog
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @instance
         */
        FileDialogEventMsgOutProto.prototype.customDialog = false;

        /**
         * Creates a new FileDialogEventMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @static
         * @param {appFrameProtoOut.IFileDialogEventMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.FileDialogEventMsgOutProto} FileDialogEventMsgOutProto instance
         */
        FileDialogEventMsgOutProto.create = function create(properties) {
            return new FileDialogEventMsgOutProto(properties);
        };

        /**
         * Decodes a FileDialogEventMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.FileDialogEventMsgOutProto} FileDialogEventMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        FileDialogEventMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.FileDialogEventMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.eventType = reader.int32();
                        break;
                    }
                case 2: {
                        message.allowDownload = reader.bool();
                        break;
                    }
                case 3: {
                        message.allowUpload = reader.bool();
                        break;
                    }
                case 4: {
                        message.allowDelete = reader.bool();
                        break;
                    }
                case 5: {
                        message.filter = reader.string();
                        break;
                    }
                case 6: {
                        message.isMultiSelection = reader.bool();
                        break;
                    }
                case 7: {
                        message.selection = reader.string();
                        break;
                    }
                case 8: {
                        message.customDialog = reader.bool();
                        break;
                    }
                default:
                    reader.skipType(tag & 7, long);
                    break;
                }
            }
            if (!Object.hasOwnProperty.call(message, "eventType"))
                throw $util.ProtocolError("missing required 'eventType'", { instance: message });
            return message;
        };

        /**
         * Creates a FileDialogEventMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.FileDialogEventMsgOutProto} FileDialogEventMsgOutProto
         */
        FileDialogEventMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.FileDialogEventMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.FileDialogEventMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.FileDialogEventMsgOutProto();
            switch (object.eventType) {
            default:
                if (typeof object.eventType === "number") {
                    message.eventType = object.eventType;
                    break;
                }
                break;
            case "Open":
            case 0:
                message.eventType = 0;
                break;
            case "Close":
            case 1:
                message.eventType = 1;
                break;
            case "AutoUpload":
            case 2:
                message.eventType = 2;
                break;
            case "AutoSave":
            case 3:
                message.eventType = 3;
                break;
            }
            if (object.allowDownload != null)
                message.allowDownload = Boolean(object.allowDownload);
            if (object.allowUpload != null)
                message.allowUpload = Boolean(object.allowUpload);
            if (object.allowDelete != null)
                message.allowDelete = Boolean(object.allowDelete);
            if (object.filter != null)
                message.filter = String(object.filter);
            if (object.isMultiSelection != null)
                message.isMultiSelection = Boolean(object.isMultiSelection);
            if (object.selection != null)
                message.selection = String(object.selection);
            if (object.customDialog != null)
                message.customDialog = Boolean(object.customDialog);
            return message;
        };

        /**
         * Creates a plain object from a FileDialogEventMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @static
         * @param {appFrameProtoOut.FileDialogEventMsgOutProto} message FileDialogEventMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        FileDialogEventMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.eventType = options.enums === String ? "Open" : 0;
                object.allowDownload = false;
                object.allowUpload = false;
                object.allowDelete = false;
                object.filter = "";
                object.isMultiSelection = false;
                object.selection = "";
                object.customDialog = false;
            }
            if (message.eventType != null && Object.hasOwnProperty.call(message, "eventType"))
                object.eventType = options.enums === String ? $root.appFrameProtoOut.FileDialogEventMsgOutProto.FileDialogEventTypeProto[message.eventType] === undefined ? message.eventType : $root.appFrameProtoOut.FileDialogEventMsgOutProto.FileDialogEventTypeProto[message.eventType] : message.eventType;
            if (message.allowDownload != null && Object.hasOwnProperty.call(message, "allowDownload"))
                object.allowDownload = message.allowDownload;
            if (message.allowUpload != null && Object.hasOwnProperty.call(message, "allowUpload"))
                object.allowUpload = message.allowUpload;
            if (message.allowDelete != null && Object.hasOwnProperty.call(message, "allowDelete"))
                object.allowDelete = message.allowDelete;
            if (message.filter != null && Object.hasOwnProperty.call(message, "filter"))
                object.filter = message.filter;
            if (message.isMultiSelection != null && Object.hasOwnProperty.call(message, "isMultiSelection"))
                object.isMultiSelection = message.isMultiSelection;
            if (message.selection != null && Object.hasOwnProperty.call(message, "selection"))
                object.selection = message.selection;
            if (message.customDialog != null && Object.hasOwnProperty.call(message, "customDialog"))
                object.customDialog = message.customDialog;
            return object;
        };

        /**
         * Converts this FileDialogEventMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        FileDialogEventMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for FileDialogEventMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.FileDialogEventMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        FileDialogEventMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.FileDialogEventMsgOutProto";
        };

        /**
         * FileDialogEventTypeProto enum.
         * @name appFrameProtoOut.FileDialogEventMsgOutProto.FileDialogEventTypeProto
         * @enum {number}
         * @property {number} Open=0 Open value
         * @property {number} Close=1 Close value
         * @property {number} AutoUpload=2 AutoUpload value
         * @property {number} AutoSave=3 AutoSave value
         */
        FileDialogEventMsgOutProto.FileDialogEventTypeProto = (function() {
            var valuesById = {}, values = Object.create(valuesById);
            values[valuesById[0] = "Open"] = 0;
            values[valuesById[1] = "Close"] = 1;
            values[valuesById[2] = "AutoUpload"] = 2;
            values[valuesById[3] = "AutoSave"] = 3;
            return values;
        })();

        return FileDialogEventMsgOutProto;
    })();

    appFrameProtoOut.WindowSwitchMsg = (function() {

        /**
         * Properties of a WindowSwitchMsg.
         * @memberof appFrameProtoOut
         * @interface IWindowSwitchMsg
         * @property {string} id WindowSwitchMsg id
         * @property {string|null} [title] WindowSwitchMsg title
         * @property {boolean|null} [modalBlocked] WindowSwitchMsg modalBlocked
         */

        /**
         * Constructs a new WindowSwitchMsg.
         * @memberof appFrameProtoOut
         * @classdesc Represents a WindowSwitchMsg.
         * @implements IWindowSwitchMsg
         * @constructor
         * @param {appFrameProtoOut.IWindowSwitchMsg=} [properties] Properties to set
         */
        function WindowSwitchMsg(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * WindowSwitchMsg id.
         * @member {string} id
         * @memberof appFrameProtoOut.WindowSwitchMsg
         * @instance
         */
        WindowSwitchMsg.prototype.id = "";

        /**
         * WindowSwitchMsg title.
         * @member {string} title
         * @memberof appFrameProtoOut.WindowSwitchMsg
         * @instance
         */
        WindowSwitchMsg.prototype.title = "";

        /**
         * WindowSwitchMsg modalBlocked.
         * @member {boolean} modalBlocked
         * @memberof appFrameProtoOut.WindowSwitchMsg
         * @instance
         */
        WindowSwitchMsg.prototype.modalBlocked = false;

        /**
         * Creates a new WindowSwitchMsg instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.WindowSwitchMsg
         * @static
         * @param {appFrameProtoOut.IWindowSwitchMsg=} [properties] Properties to set
         * @returns {appFrameProtoOut.WindowSwitchMsg} WindowSwitchMsg instance
         */
        WindowSwitchMsg.create = function create(properties) {
            return new WindowSwitchMsg(properties);
        };

        /**
         * Decodes a WindowSwitchMsg message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.WindowSwitchMsg
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.WindowSwitchMsg} WindowSwitchMsg
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        WindowSwitchMsg.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.WindowSwitchMsg();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.id = reader.string();
                        break;
                    }
                case 2: {
                        message.title = reader.string();
                        break;
                    }
                case 3: {
                        message.modalBlocked = reader.bool();
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
         * Creates a WindowSwitchMsg message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.WindowSwitchMsg
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.WindowSwitchMsg} WindowSwitchMsg
         */
        WindowSwitchMsg.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.WindowSwitchMsg)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.WindowSwitchMsg: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.WindowSwitchMsg();
            if (object.id != null)
                message.id = String(object.id);
            if (object.title != null)
                message.title = String(object.title);
            if (object.modalBlocked != null)
                message.modalBlocked = Boolean(object.modalBlocked);
            return message;
        };

        /**
         * Creates a plain object from a WindowSwitchMsg message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.WindowSwitchMsg
         * @static
         * @param {appFrameProtoOut.WindowSwitchMsg} message WindowSwitchMsg
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        WindowSwitchMsg.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.id = "";
                object.title = "";
                object.modalBlocked = false;
            }
            if (message.id != null && Object.hasOwnProperty.call(message, "id"))
                object.id = message.id;
            if (message.title != null && Object.hasOwnProperty.call(message, "title"))
                object.title = message.title;
            if (message.modalBlocked != null && Object.hasOwnProperty.call(message, "modalBlocked"))
                object.modalBlocked = message.modalBlocked;
            return object;
        };

        /**
         * Converts this WindowSwitchMsg to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.WindowSwitchMsg
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        WindowSwitchMsg.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for WindowSwitchMsg
         * @function getTypeUrl
         * @memberof appFrameProtoOut.WindowSwitchMsg
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        WindowSwitchMsg.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.WindowSwitchMsg";
        };

        return WindowSwitchMsg;
    })();

    appFrameProtoOut.WindowMsgOutProto = (function() {

        /**
         * Properties of a WindowMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IWindowMsgOutProto
         * @property {string} id WindowMsgOutProto id
         * @property {Array.<appFrameProtoOut.IWindowPartialContentMsgOutProto>|null} [content] WindowMsgOutProto content
         * @property {Uint8Array|null} [directDraw] WindowMsgOutProto directDraw
         * @property {string|null} [title] WindowMsgOutProto title
         * @property {number|null} [posX] WindowMsgOutProto posX
         * @property {number|null} [posY] WindowMsgOutProto posY
         * @property {number|null} [width] WindowMsgOutProto width
         * @property {number|null} [height] WindowMsgOutProto height
         * @property {string|null} [name] WindowMsgOutProto name
         * @property {appFrameProtoOut.WindowMsgOutProto.WindowTypeProto|null} [type] WindowMsgOutProto type
         * @property {boolean|null} [modalBlocked] WindowMsgOutProto modalBlocked
         * @property {string|null} [ownerId] WindowMsgOutProto ownerId
         * @property {number|null} [state] WindowMsgOutProto state
         * @property {Array.<appFrameProtoOut.IWindowMsgOutProto>|null} [internalWindows] WindowMsgOutProto internalWindows
         * @property {appFrameProtoOut.WindowMsgOutProto.DockModeProto|null} [dockMode] WindowMsgOutProto dockMode
         * @property {appFrameProtoOut.WindowMsgOutProto.DockStateProto|null} [dockState] WindowMsgOutProto dockState
         * @property {appFrameProtoOut.WindowMsgOutProto.WindowClassTypeProto|null} [classType] WindowMsgOutProto classType
         */

        /**
         * Constructs a new WindowMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a WindowMsgOutProto.
         * @implements IWindowMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IWindowMsgOutProto=} [properties] Properties to set
         */
        function WindowMsgOutProto(properties) {
            this.content = [];
            this.internalWindows = [];
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * WindowMsgOutProto id.
         * @member {string} id
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.id = "";

        /**
         * WindowMsgOutProto content.
         * @member {Array.<appFrameProtoOut.IWindowPartialContentMsgOutProto>} content
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.content = $util.emptyArray;

        /**
         * WindowMsgOutProto directDraw.
         * @member {Uint8Array} directDraw
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.directDraw = $util.newBuffer([]);

        /**
         * WindowMsgOutProto title.
         * @member {string} title
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.title = "";

        /**
         * WindowMsgOutProto posX.
         * @member {number} posX
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.posX = 0;

        /**
         * WindowMsgOutProto posY.
         * @member {number} posY
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.posY = 0;

        /**
         * WindowMsgOutProto width.
         * @member {number} width
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.width = 0;

        /**
         * WindowMsgOutProto height.
         * @member {number} height
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.height = 0;

        /**
         * WindowMsgOutProto name.
         * @member {string} name
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.name = "";

        /**
         * WindowMsgOutProto type.
         * @member {appFrameProtoOut.WindowMsgOutProto.WindowTypeProto} type
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.type = 1;

        /**
         * WindowMsgOutProto modalBlocked.
         * @member {boolean} modalBlocked
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.modalBlocked = false;

        /**
         * WindowMsgOutProto ownerId.
         * @member {string} ownerId
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.ownerId = "";

        /**
         * WindowMsgOutProto state.
         * @member {number} state
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.state = 0;

        /**
         * WindowMsgOutProto internalWindows.
         * @member {Array.<appFrameProtoOut.IWindowMsgOutProto>} internalWindows
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.internalWindows = $util.emptyArray;

        /**
         * WindowMsgOutProto dockMode.
         * @member {appFrameProtoOut.WindowMsgOutProto.DockModeProto} dockMode
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.dockMode = 1;

        /**
         * WindowMsgOutProto dockState.
         * @member {appFrameProtoOut.WindowMsgOutProto.DockStateProto} dockState
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.dockState = 1;

        /**
         * WindowMsgOutProto classType.
         * @member {appFrameProtoOut.WindowMsgOutProto.WindowClassTypeProto} classType
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         */
        WindowMsgOutProto.prototype.classType = 1;

        /**
         * Creates a new WindowMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @static
         * @param {appFrameProtoOut.IWindowMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.WindowMsgOutProto} WindowMsgOutProto instance
         */
        WindowMsgOutProto.create = function create(properties) {
            return new WindowMsgOutProto(properties);
        };

        /**
         * Decodes a WindowMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.WindowMsgOutProto} WindowMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        WindowMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.WindowMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.id = reader.string();
                        break;
                    }
                case 2: {
                        if (!(message.content && message.content.length))
                            message.content = [];
                        message.content.push($root.appFrameProtoOut.WindowPartialContentMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1));
                        break;
                    }
                case 3: {
                        message.directDraw = reader.bytes();
                        break;
                    }
                case 4: {
                        message.title = reader.string();
                        break;
                    }
                case 5: {
                        message.posX = reader.sint32();
                        break;
                    }
                case 6: {
                        message.posY = reader.sint32();
                        break;
                    }
                case 7: {
                        message.width = reader.uint32();
                        break;
                    }
                case 8: {
                        message.height = reader.uint32();
                        break;
                    }
                case 9: {
                        message.name = reader.string();
                        break;
                    }
                case 10: {
                        message.type = reader.int32();
                        break;
                    }
                case 11: {
                        message.modalBlocked = reader.bool();
                        break;
                    }
                case 12: {
                        message.ownerId = reader.string();
                        break;
                    }
                case 13: {
                        message.state = reader.uint32();
                        break;
                    }
                case 14: {
                        if (!(message.internalWindows && message.internalWindows.length))
                            message.internalWindows = [];
                        message.internalWindows.push($root.appFrameProtoOut.WindowMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1));
                        break;
                    }
                case 15: {
                        message.dockMode = reader.int32();
                        break;
                    }
                case 16: {
                        message.dockState = reader.int32();
                        break;
                    }
                case 17: {
                        message.classType = reader.int32();
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
         * Creates a WindowMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.WindowMsgOutProto} WindowMsgOutProto
         */
        WindowMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.WindowMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.WindowMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.WindowMsgOutProto();
            if (object.id != null)
                message.id = String(object.id);
            if (object.content) {
                if (!Array.isArray(object.content))
                    throw TypeError(".appFrameProtoOut.WindowMsgOutProto.content: array expected");
                message.content = [];
                for (var i = 0; i < object.content.length; ++i) {
                    if (!$util.isObject(object.content[i]))
                        throw TypeError(".appFrameProtoOut.WindowMsgOutProto.content: object expected");
                    message.content[i] = $root.appFrameProtoOut.WindowPartialContentMsgOutProto.fromObject(object.content[i], long + 1);
                }
            }
            if (object.directDraw != null)
                if (typeof object.directDraw === "string")
                    $util.base64.decode(object.directDraw, message.directDraw = $util.newBuffer($util.base64.length(object.directDraw)), 0);
                else if (object.directDraw.length >= 0)
                    message.directDraw = object.directDraw;
            if (object.title != null)
                message.title = String(object.title);
            if (object.posX != null)
                message.posX = object.posX | 0;
            if (object.posY != null)
                message.posY = object.posY | 0;
            if (object.width != null)
                message.width = object.width >>> 0;
            if (object.height != null)
                message.height = object.height >>> 0;
            if (object.name != null)
                message.name = String(object.name);
            switch (object.type) {
            default:
                if (typeof object.type === "number") {
                    message.type = object.type;
                    break;
                }
                break;
            case "basic":
            case 1:
                message.type = 1;
                break;
            case "html":
            case 2:
                message.type = 2;
                break;
            case "internal":
            case 3:
                message.type = 3;
                break;
            case "internalHtml":
            case 4:
                message.type = 4;
                break;
            case "internalWrapper":
            case 5:
                message.type = 5;
                break;
            }
            if (object.modalBlocked != null)
                message.modalBlocked = Boolean(object.modalBlocked);
            if (object.ownerId != null)
                message.ownerId = String(object.ownerId);
            if (object.state != null)
                message.state = object.state >>> 0;
            if (object.internalWindows) {
                if (!Array.isArray(object.internalWindows))
                    throw TypeError(".appFrameProtoOut.WindowMsgOutProto.internalWindows: array expected");
                message.internalWindows = [];
                for (var i = 0; i < object.internalWindows.length; ++i) {
                    if (!$util.isObject(object.internalWindows[i]))
                        throw TypeError(".appFrameProtoOut.WindowMsgOutProto.internalWindows: object expected");
                    message.internalWindows[i] = $root.appFrameProtoOut.WindowMsgOutProto.fromObject(object.internalWindows[i], long + 1);
                }
            }
            switch (object.dockMode) {
            default:
                if (typeof object.dockMode === "number") {
                    message.dockMode = object.dockMode;
                    break;
                }
                break;
            case "none":
            case 1:
                message.dockMode = 1;
                break;
            case "dockable":
            case 2:
                message.dockMode = 2;
                break;
            case "autoUndock":
            case 3:
                message.dockMode = 3;
                break;
            }
            switch (object.dockState) {
            default:
                if (typeof object.dockState === "number") {
                    message.dockState = object.dockState;
                    break;
                }
                break;
            case "docked":
            case 1:
                message.dockState = 1;
                break;
            case "undocked":
            case 2:
                message.dockState = 2;
                break;
            }
            switch (object.classType) {
            default:
                if (typeof object.classType === "number") {
                    message.classType = object.classType;
                    break;
                }
                break;
            case "other":
            case 1:
                message.classType = 1;
                break;
            case "Window":
            case 2:
                message.classType = 2;
                break;
            case "JWindow":
            case 3:
                message.classType = 3;
                break;
            case "Dialog":
            case 4:
                message.classType = 4;
                break;
            case "JDialog":
            case 5:
                message.classType = 5;
                break;
            case "Frame":
            case 6:
                message.classType = 6;
                break;
            case "JFrame":
            case 7:
                message.classType = 7;
                break;
            }
            return message;
        };

        /**
         * Creates a plain object from a WindowMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @static
         * @param {appFrameProtoOut.WindowMsgOutProto} message WindowMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        WindowMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.arrays || options.defaults) {
                object.content = [];
                object.internalWindows = [];
            }
            if (options.defaults) {
                object.id = "";
                if (options.bytes === String)
                    object.directDraw = "";
                else {
                    object.directDraw = [];
                    if (options.bytes !== Array)
                        object.directDraw = $util.newBuffer(object.directDraw);
                }
                object.title = "";
                object.posX = 0;
                object.posY = 0;
                object.width = 0;
                object.height = 0;
                object.name = "";
                object.type = options.enums === String ? "basic" : 1;
                object.modalBlocked = false;
                object.ownerId = "";
                object.state = 0;
                object.dockMode = options.enums === String ? "none" : 1;
                object.dockState = options.enums === String ? "docked" : 1;
                object.classType = options.enums === String ? "other" : 1;
            }
            if (message.id != null && Object.hasOwnProperty.call(message, "id"))
                object.id = message.id;
            if (message.content && message.content.length) {
                object.content = [];
                for (var j = 0; j < message.content.length; ++j)
                    object.content[j] = $root.appFrameProtoOut.WindowPartialContentMsgOutProto.toObject(message.content[j], options, q + 1);
            }
            if (message.directDraw != null && Object.hasOwnProperty.call(message, "directDraw"))
                object.directDraw = options.bytes === String ? $util.base64.encode(message.directDraw, 0, message.directDraw.length) : options.bytes === Array ? Array.prototype.slice.call(message.directDraw) : message.directDraw;
            if (message.title != null && Object.hasOwnProperty.call(message, "title"))
                object.title = message.title;
            if (message.posX != null && Object.hasOwnProperty.call(message, "posX"))
                object.posX = message.posX;
            if (message.posY != null && Object.hasOwnProperty.call(message, "posY"))
                object.posY = message.posY;
            if (message.width != null && Object.hasOwnProperty.call(message, "width"))
                object.width = message.width;
            if (message.height != null && Object.hasOwnProperty.call(message, "height"))
                object.height = message.height;
            if (message.name != null && Object.hasOwnProperty.call(message, "name"))
                object.name = message.name;
            if (message.type != null && Object.hasOwnProperty.call(message, "type"))
                object.type = options.enums === String ? $root.appFrameProtoOut.WindowMsgOutProto.WindowTypeProto[message.type] === undefined ? message.type : $root.appFrameProtoOut.WindowMsgOutProto.WindowTypeProto[message.type] : message.type;
            if (message.modalBlocked != null && Object.hasOwnProperty.call(message, "modalBlocked"))
                object.modalBlocked = message.modalBlocked;
            if (message.ownerId != null && Object.hasOwnProperty.call(message, "ownerId"))
                object.ownerId = message.ownerId;
            if (message.state != null && Object.hasOwnProperty.call(message, "state"))
                object.state = message.state;
            if (message.internalWindows && message.internalWindows.length) {
                object.internalWindows = [];
                for (var j = 0; j < message.internalWindows.length; ++j)
                    object.internalWindows[j] = $root.appFrameProtoOut.WindowMsgOutProto.toObject(message.internalWindows[j], options, q + 1);
            }
            if (message.dockMode != null && Object.hasOwnProperty.call(message, "dockMode"))
                object.dockMode = options.enums === String ? $root.appFrameProtoOut.WindowMsgOutProto.DockModeProto[message.dockMode] === undefined ? message.dockMode : $root.appFrameProtoOut.WindowMsgOutProto.DockModeProto[message.dockMode] : message.dockMode;
            if (message.dockState != null && Object.hasOwnProperty.call(message, "dockState"))
                object.dockState = options.enums === String ? $root.appFrameProtoOut.WindowMsgOutProto.DockStateProto[message.dockState] === undefined ? message.dockState : $root.appFrameProtoOut.WindowMsgOutProto.DockStateProto[message.dockState] : message.dockState;
            if (message.classType != null && Object.hasOwnProperty.call(message, "classType"))
                object.classType = options.enums === String ? $root.appFrameProtoOut.WindowMsgOutProto.WindowClassTypeProto[message.classType] === undefined ? message.classType : $root.appFrameProtoOut.WindowMsgOutProto.WindowClassTypeProto[message.classType] : message.classType;
            return object;
        };

        /**
         * Converts this WindowMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        WindowMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for WindowMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.WindowMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        WindowMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.WindowMsgOutProto";
        };

        /**
         * WindowTypeProto enum.
         * @name appFrameProtoOut.WindowMsgOutProto.WindowTypeProto
         * @enum {number}
         * @property {number} basic=1 basic value
         * @property {number} html=2 html value
         * @property {number} internal=3 internal value
         * @property {number} internalHtml=4 internalHtml value
         * @property {number} internalWrapper=5 internalWrapper value
         */
        WindowMsgOutProto.WindowTypeProto = (function() {
            var valuesById = {}, values = Object.create(valuesById);
            values[valuesById[1] = "basic"] = 1;
            values[valuesById[2] = "html"] = 2;
            values[valuesById[3] = "internal"] = 3;
            values[valuesById[4] = "internalHtml"] = 4;
            values[valuesById[5] = "internalWrapper"] = 5;
            return values;
        })();

        /**
         * WindowClassTypeProto enum.
         * @name appFrameProtoOut.WindowMsgOutProto.WindowClassTypeProto
         * @enum {number}
         * @property {number} other=1 other value
         * @property {number} Window=2 Window value
         * @property {number} JWindow=3 JWindow value
         * @property {number} Dialog=4 Dialog value
         * @property {number} JDialog=5 JDialog value
         * @property {number} Frame=6 Frame value
         * @property {number} JFrame=7 JFrame value
         */
        WindowMsgOutProto.WindowClassTypeProto = (function() {
            var valuesById = {}, values = Object.create(valuesById);
            values[valuesById[1] = "other"] = 1;
            values[valuesById[2] = "Window"] = 2;
            values[valuesById[3] = "JWindow"] = 3;
            values[valuesById[4] = "Dialog"] = 4;
            values[valuesById[5] = "JDialog"] = 5;
            values[valuesById[6] = "Frame"] = 6;
            values[valuesById[7] = "JFrame"] = 7;
            return values;
        })();

        /**
         * DockModeProto enum.
         * @name appFrameProtoOut.WindowMsgOutProto.DockModeProto
         * @enum {number}
         * @property {number} none=1 none value
         * @property {number} dockable=2 dockable value
         * @property {number} autoUndock=3 autoUndock value
         */
        WindowMsgOutProto.DockModeProto = (function() {
            var valuesById = {}, values = Object.create(valuesById);
            values[valuesById[1] = "none"] = 1;
            values[valuesById[2] = "dockable"] = 2;
            values[valuesById[3] = "autoUndock"] = 3;
            return values;
        })();

        /**
         * DockStateProto enum.
         * @name appFrameProtoOut.WindowMsgOutProto.DockStateProto
         * @enum {number}
         * @property {number} docked=1 docked value
         * @property {number} undocked=2 undocked value
         */
        WindowMsgOutProto.DockStateProto = (function() {
            var valuesById = {}, values = Object.create(valuesById);
            values[valuesById[1] = "docked"] = 1;
            values[valuesById[2] = "undocked"] = 2;
            return values;
        })();

        return WindowMsgOutProto;
    })();

    appFrameProtoOut.WindowSwitchMsgOutProto = (function() {

        /**
         * Properties of a WindowSwitchMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IWindowSwitchMsgOutProto
         * @property {string} id WindowSwitchMsgOutProto id
         * @property {string|null} [title] WindowSwitchMsgOutProto title
         * @property {boolean|null} [modalBlocked] WindowSwitchMsgOutProto modalBlocked
         */

        /**
         * Constructs a new WindowSwitchMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a WindowSwitchMsgOutProto.
         * @implements IWindowSwitchMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IWindowSwitchMsgOutProto=} [properties] Properties to set
         */
        function WindowSwitchMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * WindowSwitchMsgOutProto id.
         * @member {string} id
         * @memberof appFrameProtoOut.WindowSwitchMsgOutProto
         * @instance
         */
        WindowSwitchMsgOutProto.prototype.id = "";

        /**
         * WindowSwitchMsgOutProto title.
         * @member {string} title
         * @memberof appFrameProtoOut.WindowSwitchMsgOutProto
         * @instance
         */
        WindowSwitchMsgOutProto.prototype.title = "";

        /**
         * WindowSwitchMsgOutProto modalBlocked.
         * @member {boolean} modalBlocked
         * @memberof appFrameProtoOut.WindowSwitchMsgOutProto
         * @instance
         */
        WindowSwitchMsgOutProto.prototype.modalBlocked = false;

        /**
         * Creates a new WindowSwitchMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.WindowSwitchMsgOutProto
         * @static
         * @param {appFrameProtoOut.IWindowSwitchMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.WindowSwitchMsgOutProto} WindowSwitchMsgOutProto instance
         */
        WindowSwitchMsgOutProto.create = function create(properties) {
            return new WindowSwitchMsgOutProto(properties);
        };

        /**
         * Decodes a WindowSwitchMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.WindowSwitchMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.WindowSwitchMsgOutProto} WindowSwitchMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        WindowSwitchMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.WindowSwitchMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.id = reader.string();
                        break;
                    }
                case 2: {
                        message.title = reader.string();
                        break;
                    }
                case 3: {
                        message.modalBlocked = reader.bool();
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
         * Creates a WindowSwitchMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.WindowSwitchMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.WindowSwitchMsgOutProto} WindowSwitchMsgOutProto
         */
        WindowSwitchMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.WindowSwitchMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.WindowSwitchMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.WindowSwitchMsgOutProto();
            if (object.id != null)
                message.id = String(object.id);
            if (object.title != null)
                message.title = String(object.title);
            if (object.modalBlocked != null)
                message.modalBlocked = Boolean(object.modalBlocked);
            return message;
        };

        /**
         * Creates a plain object from a WindowSwitchMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.WindowSwitchMsgOutProto
         * @static
         * @param {appFrameProtoOut.WindowSwitchMsgOutProto} message WindowSwitchMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        WindowSwitchMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.id = "";
                object.title = "";
                object.modalBlocked = false;
            }
            if (message.id != null && Object.hasOwnProperty.call(message, "id"))
                object.id = message.id;
            if (message.title != null && Object.hasOwnProperty.call(message, "title"))
                object.title = message.title;
            if (message.modalBlocked != null && Object.hasOwnProperty.call(message, "modalBlocked"))
                object.modalBlocked = message.modalBlocked;
            return object;
        };

        /**
         * Converts this WindowSwitchMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.WindowSwitchMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        WindowSwitchMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for WindowSwitchMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.WindowSwitchMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        WindowSwitchMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.WindowSwitchMsgOutProto";
        };

        return WindowSwitchMsgOutProto;
    })();

    appFrameProtoOut.WindowPartialContentMsgOutProto = (function() {

        /**
         * Properties of a WindowPartialContentMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IWindowPartialContentMsgOutProto
         * @property {number|null} [positionX] WindowPartialContentMsgOutProto positionX
         * @property {number|null} [positionY] WindowPartialContentMsgOutProto positionY
         * @property {number|null} [width] WindowPartialContentMsgOutProto width
         * @property {number|null} [height] WindowPartialContentMsgOutProto height
         * @property {Uint8Array|null} [base64Content] WindowPartialContentMsgOutProto base64Content
         */

        /**
         * Constructs a new WindowPartialContentMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a WindowPartialContentMsgOutProto.
         * @implements IWindowPartialContentMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IWindowPartialContentMsgOutProto=} [properties] Properties to set
         */
        function WindowPartialContentMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * WindowPartialContentMsgOutProto positionX.
         * @member {number} positionX
         * @memberof appFrameProtoOut.WindowPartialContentMsgOutProto
         * @instance
         */
        WindowPartialContentMsgOutProto.prototype.positionX = 0;

        /**
         * WindowPartialContentMsgOutProto positionY.
         * @member {number} positionY
         * @memberof appFrameProtoOut.WindowPartialContentMsgOutProto
         * @instance
         */
        WindowPartialContentMsgOutProto.prototype.positionY = 0;

        /**
         * WindowPartialContentMsgOutProto width.
         * @member {number} width
         * @memberof appFrameProtoOut.WindowPartialContentMsgOutProto
         * @instance
         */
        WindowPartialContentMsgOutProto.prototype.width = 0;

        /**
         * WindowPartialContentMsgOutProto height.
         * @member {number} height
         * @memberof appFrameProtoOut.WindowPartialContentMsgOutProto
         * @instance
         */
        WindowPartialContentMsgOutProto.prototype.height = 0;

        /**
         * WindowPartialContentMsgOutProto base64Content.
         * @member {Uint8Array} base64Content
         * @memberof appFrameProtoOut.WindowPartialContentMsgOutProto
         * @instance
         */
        WindowPartialContentMsgOutProto.prototype.base64Content = $util.newBuffer([]);

        /**
         * Creates a new WindowPartialContentMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.WindowPartialContentMsgOutProto
         * @static
         * @param {appFrameProtoOut.IWindowPartialContentMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.WindowPartialContentMsgOutProto} WindowPartialContentMsgOutProto instance
         */
        WindowPartialContentMsgOutProto.create = function create(properties) {
            return new WindowPartialContentMsgOutProto(properties);
        };

        /**
         * Decodes a WindowPartialContentMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.WindowPartialContentMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.WindowPartialContentMsgOutProto} WindowPartialContentMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        WindowPartialContentMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.WindowPartialContentMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.positionX = reader.sint32();
                        break;
                    }
                case 2: {
                        message.positionY = reader.sint32();
                        break;
                    }
                case 3: {
                        message.width = reader.uint32();
                        break;
                    }
                case 4: {
                        message.height = reader.uint32();
                        break;
                    }
                case 5: {
                        message.base64Content = reader.bytes();
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
         * Creates a WindowPartialContentMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.WindowPartialContentMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.WindowPartialContentMsgOutProto} WindowPartialContentMsgOutProto
         */
        WindowPartialContentMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.WindowPartialContentMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.WindowPartialContentMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.WindowPartialContentMsgOutProto();
            if (object.positionX != null)
                message.positionX = object.positionX | 0;
            if (object.positionY != null)
                message.positionY = object.positionY | 0;
            if (object.width != null)
                message.width = object.width >>> 0;
            if (object.height != null)
                message.height = object.height >>> 0;
            if (object.base64Content != null)
                if (typeof object.base64Content === "string")
                    $util.base64.decode(object.base64Content, message.base64Content = $util.newBuffer($util.base64.length(object.base64Content)), 0);
                else if (object.base64Content.length >= 0)
                    message.base64Content = object.base64Content;
            return message;
        };

        /**
         * Creates a plain object from a WindowPartialContentMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.WindowPartialContentMsgOutProto
         * @static
         * @param {appFrameProtoOut.WindowPartialContentMsgOutProto} message WindowPartialContentMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        WindowPartialContentMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.positionX = 0;
                object.positionY = 0;
                object.width = 0;
                object.height = 0;
                if (options.bytes === String)
                    object.base64Content = "";
                else {
                    object.base64Content = [];
                    if (options.bytes !== Array)
                        object.base64Content = $util.newBuffer(object.base64Content);
                }
            }
            if (message.positionX != null && Object.hasOwnProperty.call(message, "positionX"))
                object.positionX = message.positionX;
            if (message.positionY != null && Object.hasOwnProperty.call(message, "positionY"))
                object.positionY = message.positionY;
            if (message.width != null && Object.hasOwnProperty.call(message, "width"))
                object.width = message.width;
            if (message.height != null && Object.hasOwnProperty.call(message, "height"))
                object.height = message.height;
            if (message.base64Content != null && Object.hasOwnProperty.call(message, "base64Content"))
                object.base64Content = options.bytes === String ? $util.base64.encode(message.base64Content, 0, message.base64Content.length) : options.bytes === Array ? Array.prototype.slice.call(message.base64Content) : message.base64Content;
            return object;
        };

        /**
         * Converts this WindowPartialContentMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.WindowPartialContentMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        WindowPartialContentMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for WindowPartialContentMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.WindowPartialContentMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        WindowPartialContentMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.WindowPartialContentMsgOutProto";
        };

        return WindowPartialContentMsgOutProto;
    })();

    /**
     * SimpleEventMsgOutProto enum.
     * @name appFrameProtoOut.SimpleEventMsgOutProto
     * @enum {number}
     * @property {number} applicationAlreadyRunning=0 applicationAlreadyRunning value
     * @property {number} shutDownNotification=1 shutDownNotification value
     * @property {number} tooManyClientsNotification=2 tooManyClientsNotification value
     * @property {number} continueOldSession=3 continueOldSession value
     * @property {number} configurationError=4 configurationError value
     * @property {number} sessionStolenNotification=5 sessionStolenNotification value
     * @property {number} unauthorizedAccess=6 unauthorizedAccess value
     * @property {number} shutDownAutoLogoutNotification=7 shutDownAutoLogoutNotification value
     * @property {number} sessionTimeoutWarning=8 sessionTimeoutWarning value
     * @property {number} sessionTimedOutNotification=9 sessionTimedOutNotification value
     * @property {number} applicationBusy=10 applicationBusy value
     * @property {number} reconnectInstanceNotFound=11 reconnectInstanceNotFound value
     */
    appFrameProtoOut.SimpleEventMsgOutProto = (function() {
        var valuesById = {}, values = Object.create(valuesById);
        values[valuesById[0] = "applicationAlreadyRunning"] = 0;
        values[valuesById[1] = "shutDownNotification"] = 1;
        values[valuesById[2] = "tooManyClientsNotification"] = 2;
        values[valuesById[3] = "continueOldSession"] = 3;
        values[valuesById[4] = "configurationError"] = 4;
        values[valuesById[5] = "sessionStolenNotification"] = 5;
        values[valuesById[6] = "unauthorizedAccess"] = 6;
        values[valuesById[7] = "shutDownAutoLogoutNotification"] = 7;
        values[valuesById[8] = "sessionTimeoutWarning"] = 8;
        values[valuesById[9] = "sessionTimedOutNotification"] = 9;
        values[valuesById[10] = "applicationBusy"] = 10;
        values[valuesById[11] = "reconnectInstanceNotFound"] = 11;
        return values;
    })();

    appFrameProtoOut.JsEvalRequestMsgOutProto = (function() {

        /**
         * Properties of a JsEvalRequestMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IJsEvalRequestMsgOutProto
         * @property {string|null} [correlationId] JsEvalRequestMsgOutProto correlationId
         * @property {string|null} [thisObjectId] JsEvalRequestMsgOutProto thisObjectId
         * @property {appFrameProtoOut.JsEvalRequestMsgOutProto.JsEvalRequestTypeProto|null} [type] JsEvalRequestMsgOutProto type
         * @property {string|null} [evalString] JsEvalRequestMsgOutProto evalString
         * @property {Array.<appFrameProtoOut.IJsParamMsgOutProto>|null} [params] JsEvalRequestMsgOutProto params
         * @property {Array.<string>|null} [garbageIds] JsEvalRequestMsgOutProto garbageIds
         */

        /**
         * Constructs a new JsEvalRequestMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a JsEvalRequestMsgOutProto.
         * @implements IJsEvalRequestMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IJsEvalRequestMsgOutProto=} [properties] Properties to set
         */
        function JsEvalRequestMsgOutProto(properties) {
            this.params = [];
            this.garbageIds = [];
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * JsEvalRequestMsgOutProto correlationId.
         * @member {string} correlationId
         * @memberof appFrameProtoOut.JsEvalRequestMsgOutProto
         * @instance
         */
        JsEvalRequestMsgOutProto.prototype.correlationId = "";

        /**
         * JsEvalRequestMsgOutProto thisObjectId.
         * @member {string} thisObjectId
         * @memberof appFrameProtoOut.JsEvalRequestMsgOutProto
         * @instance
         */
        JsEvalRequestMsgOutProto.prototype.thisObjectId = "";

        /**
         * JsEvalRequestMsgOutProto type.
         * @member {appFrameProtoOut.JsEvalRequestMsgOutProto.JsEvalRequestTypeProto} type
         * @memberof appFrameProtoOut.JsEvalRequestMsgOutProto
         * @instance
         */
        JsEvalRequestMsgOutProto.prototype.type = 0;

        /**
         * JsEvalRequestMsgOutProto evalString.
         * @member {string} evalString
         * @memberof appFrameProtoOut.JsEvalRequestMsgOutProto
         * @instance
         */
        JsEvalRequestMsgOutProto.prototype.evalString = "";

        /**
         * JsEvalRequestMsgOutProto params.
         * @member {Array.<appFrameProtoOut.IJsParamMsgOutProto>} params
         * @memberof appFrameProtoOut.JsEvalRequestMsgOutProto
         * @instance
         */
        JsEvalRequestMsgOutProto.prototype.params = $util.emptyArray;

        /**
         * JsEvalRequestMsgOutProto garbageIds.
         * @member {Array.<string>} garbageIds
         * @memberof appFrameProtoOut.JsEvalRequestMsgOutProto
         * @instance
         */
        JsEvalRequestMsgOutProto.prototype.garbageIds = $util.emptyArray;

        /**
         * Creates a new JsEvalRequestMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.JsEvalRequestMsgOutProto
         * @static
         * @param {appFrameProtoOut.IJsEvalRequestMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.JsEvalRequestMsgOutProto} JsEvalRequestMsgOutProto instance
         */
        JsEvalRequestMsgOutProto.create = function create(properties) {
            return new JsEvalRequestMsgOutProto(properties);
        };

        /**
         * Decodes a JsEvalRequestMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.JsEvalRequestMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.JsEvalRequestMsgOutProto} JsEvalRequestMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        JsEvalRequestMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.JsEvalRequestMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.correlationId = reader.string();
                        break;
                    }
                case 2: {
                        message.thisObjectId = reader.string();
                        break;
                    }
                case 3: {
                        message.type = reader.int32();
                        break;
                    }
                case 4: {
                        message.evalString = reader.string();
                        break;
                    }
                case 5: {
                        if (!(message.params && message.params.length))
                            message.params = [];
                        message.params.push($root.appFrameProtoOut.JsParamMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1));
                        break;
                    }
                case 6: {
                        if (!(message.garbageIds && message.garbageIds.length))
                            message.garbageIds = [];
                        message.garbageIds.push(reader.string());
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
         * Creates a JsEvalRequestMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.JsEvalRequestMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.JsEvalRequestMsgOutProto} JsEvalRequestMsgOutProto
         */
        JsEvalRequestMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.JsEvalRequestMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.JsEvalRequestMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.JsEvalRequestMsgOutProto();
            if (object.correlationId != null)
                message.correlationId = String(object.correlationId);
            if (object.thisObjectId != null)
                message.thisObjectId = String(object.thisObjectId);
            switch (object.type) {
            default:
                if (typeof object.type === "number") {
                    message.type = object.type;
                    break;
                }
                break;
            case "eval":
            case 0:
                message.type = 0;
                break;
            case "call":
            case 1:
                message.type = 1;
                break;
            case "setMember":
            case 2:
                message.type = 2;
                break;
            case "getMember":
            case 3:
                message.type = 3;
                break;
            case "deleteMember":
            case 4:
                message.type = 4;
                break;
            case "setSlot":
            case 5:
                message.type = 5;
                break;
            case "getSlot":
            case 6:
                message.type = 6;
                break;
            }
            if (object.evalString != null)
                message.evalString = String(object.evalString);
            if (object.params) {
                if (!Array.isArray(object.params))
                    throw TypeError(".appFrameProtoOut.JsEvalRequestMsgOutProto.params: array expected");
                message.params = [];
                for (var i = 0; i < object.params.length; ++i) {
                    if (!$util.isObject(object.params[i]))
                        throw TypeError(".appFrameProtoOut.JsEvalRequestMsgOutProto.params: object expected");
                    message.params[i] = $root.appFrameProtoOut.JsParamMsgOutProto.fromObject(object.params[i], long + 1);
                }
            }
            if (object.garbageIds) {
                if (!Array.isArray(object.garbageIds))
                    throw TypeError(".appFrameProtoOut.JsEvalRequestMsgOutProto.garbageIds: array expected");
                message.garbageIds = [];
                for (var i = 0; i < object.garbageIds.length; ++i)
                    message.garbageIds[i] = String(object.garbageIds[i]);
            }
            return message;
        };

        /**
         * Creates a plain object from a JsEvalRequestMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.JsEvalRequestMsgOutProto
         * @static
         * @param {appFrameProtoOut.JsEvalRequestMsgOutProto} message JsEvalRequestMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        JsEvalRequestMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.arrays || options.defaults) {
                object.params = [];
                object.garbageIds = [];
            }
            if (options.defaults) {
                object.correlationId = "";
                object.thisObjectId = "";
                object.type = options.enums === String ? "eval" : 0;
                object.evalString = "";
            }
            if (message.correlationId != null && Object.hasOwnProperty.call(message, "correlationId"))
                object.correlationId = message.correlationId;
            if (message.thisObjectId != null && Object.hasOwnProperty.call(message, "thisObjectId"))
                object.thisObjectId = message.thisObjectId;
            if (message.type != null && Object.hasOwnProperty.call(message, "type"))
                object.type = options.enums === String ? $root.appFrameProtoOut.JsEvalRequestMsgOutProto.JsEvalRequestTypeProto[message.type] === undefined ? message.type : $root.appFrameProtoOut.JsEvalRequestMsgOutProto.JsEvalRequestTypeProto[message.type] : message.type;
            if (message.evalString != null && Object.hasOwnProperty.call(message, "evalString"))
                object.evalString = message.evalString;
            if (message.params && message.params.length) {
                object.params = [];
                for (var j = 0; j < message.params.length; ++j)
                    object.params[j] = $root.appFrameProtoOut.JsParamMsgOutProto.toObject(message.params[j], options, q + 1);
            }
            if (message.garbageIds && message.garbageIds.length) {
                object.garbageIds = [];
                for (var j = 0; j < message.garbageIds.length; ++j)
                    object.garbageIds[j] = message.garbageIds[j];
            }
            return object;
        };

        /**
         * Converts this JsEvalRequestMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.JsEvalRequestMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        JsEvalRequestMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for JsEvalRequestMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.JsEvalRequestMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        JsEvalRequestMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.JsEvalRequestMsgOutProto";
        };

        /**
         * JsEvalRequestTypeProto enum.
         * @name appFrameProtoOut.JsEvalRequestMsgOutProto.JsEvalRequestTypeProto
         * @enum {number}
         * @property {number} eval=0 eval value
         * @property {number} call=1 call value
         * @property {number} setMember=2 setMember value
         * @property {number} getMember=3 getMember value
         * @property {number} deleteMember=4 deleteMember value
         * @property {number} setSlot=5 setSlot value
         * @property {number} getSlot=6 getSlot value
         */
        JsEvalRequestMsgOutProto.JsEvalRequestTypeProto = (function() {
            var valuesById = {}, values = Object.create(valuesById);
            values[valuesById[0] = "eval"] = 0;
            values[valuesById[1] = "call"] = 1;
            values[valuesById[2] = "setMember"] = 2;
            values[valuesById[3] = "getMember"] = 3;
            values[valuesById[4] = "deleteMember"] = 4;
            values[valuesById[5] = "setSlot"] = 5;
            values[valuesById[6] = "getSlot"] = 6;
            return values;
        })();

        return JsEvalRequestMsgOutProto;
    })();

    appFrameProtoOut.JsParamMsgOutProto = (function() {

        /**
         * Properties of a JsParamMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IJsParamMsgOutProto
         * @property {string|null} [primitive] JsParamMsgOutProto primitive
         * @property {appFrameProtoOut.IJSObjectMsgOutProto|null} [jsObject] JsParamMsgOutProto jsObject
         * @property {appFrameProtoOut.IJavaObjectRefMsgOutProto|null} [javaObject] JsParamMsgOutProto javaObject
         * @property {Array.<appFrameProtoOut.IJsParamMsgOutProto>|null} [array] JsParamMsgOutProto array
         */

        /**
         * Constructs a new JsParamMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a JsParamMsgOutProto.
         * @implements IJsParamMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IJsParamMsgOutProto=} [properties] Properties to set
         */
        function JsParamMsgOutProto(properties) {
            this.array = [];
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * JsParamMsgOutProto primitive.
         * @member {string} primitive
         * @memberof appFrameProtoOut.JsParamMsgOutProto
         * @instance
         */
        JsParamMsgOutProto.prototype.primitive = "";

        /**
         * JsParamMsgOutProto jsObject.
         * @member {appFrameProtoOut.IJSObjectMsgOutProto|null|undefined} jsObject
         * @memberof appFrameProtoOut.JsParamMsgOutProto
         * @instance
         */
        JsParamMsgOutProto.prototype.jsObject = null;

        /**
         * JsParamMsgOutProto javaObject.
         * @member {appFrameProtoOut.IJavaObjectRefMsgOutProto|null|undefined} javaObject
         * @memberof appFrameProtoOut.JsParamMsgOutProto
         * @instance
         */
        JsParamMsgOutProto.prototype.javaObject = null;

        /**
         * JsParamMsgOutProto array.
         * @member {Array.<appFrameProtoOut.IJsParamMsgOutProto>} array
         * @memberof appFrameProtoOut.JsParamMsgOutProto
         * @instance
         */
        JsParamMsgOutProto.prototype.array = $util.emptyArray;

        /**
         * Creates a new JsParamMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.JsParamMsgOutProto
         * @static
         * @param {appFrameProtoOut.IJsParamMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.JsParamMsgOutProto} JsParamMsgOutProto instance
         */
        JsParamMsgOutProto.create = function create(properties) {
            return new JsParamMsgOutProto(properties);
        };

        /**
         * Decodes a JsParamMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.JsParamMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.JsParamMsgOutProto} JsParamMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        JsParamMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.JsParamMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.primitive = reader.string();
                        break;
                    }
                case 2: {
                        message.jsObject = $root.appFrameProtoOut.JSObjectMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 3: {
                        message.javaObject = $root.appFrameProtoOut.JavaObjectRefMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
                        break;
                    }
                case 4: {
                        if (!(message.array && message.array.length))
                            message.array = [];
                        message.array.push($root.appFrameProtoOut.JsParamMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1));
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
         * Creates a JsParamMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.JsParamMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.JsParamMsgOutProto} JsParamMsgOutProto
         */
        JsParamMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.JsParamMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.JsParamMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.JsParamMsgOutProto();
            if (object.primitive != null)
                message.primitive = String(object.primitive);
            if (object.jsObject != null) {
                if (!$util.isObject(object.jsObject))
                    throw TypeError(".appFrameProtoOut.JsParamMsgOutProto.jsObject: object expected");
                message.jsObject = $root.appFrameProtoOut.JSObjectMsgOutProto.fromObject(object.jsObject, long + 1);
            }
            if (object.javaObject != null) {
                if (!$util.isObject(object.javaObject))
                    throw TypeError(".appFrameProtoOut.JsParamMsgOutProto.javaObject: object expected");
                message.javaObject = $root.appFrameProtoOut.JavaObjectRefMsgOutProto.fromObject(object.javaObject, long + 1);
            }
            if (object.array) {
                if (!Array.isArray(object.array))
                    throw TypeError(".appFrameProtoOut.JsParamMsgOutProto.array: array expected");
                message.array = [];
                for (var i = 0; i < object.array.length; ++i) {
                    if (!$util.isObject(object.array[i]))
                        throw TypeError(".appFrameProtoOut.JsParamMsgOutProto.array: object expected");
                    message.array[i] = $root.appFrameProtoOut.JsParamMsgOutProto.fromObject(object.array[i], long + 1);
                }
            }
            return message;
        };

        /**
         * Creates a plain object from a JsParamMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.JsParamMsgOutProto
         * @static
         * @param {appFrameProtoOut.JsParamMsgOutProto} message JsParamMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        JsParamMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.arrays || options.defaults)
                object.array = [];
            if (options.defaults) {
                object.primitive = "";
                object.jsObject = null;
                object.javaObject = null;
            }
            if (message.primitive != null && Object.hasOwnProperty.call(message, "primitive"))
                object.primitive = message.primitive;
            if (message.jsObject != null && Object.hasOwnProperty.call(message, "jsObject"))
                object.jsObject = $root.appFrameProtoOut.JSObjectMsgOutProto.toObject(message.jsObject, options, q + 1);
            if (message.javaObject != null && Object.hasOwnProperty.call(message, "javaObject"))
                object.javaObject = $root.appFrameProtoOut.JavaObjectRefMsgOutProto.toObject(message.javaObject, options, q + 1);
            if (message.array && message.array.length) {
                object.array = [];
                for (var j = 0; j < message.array.length; ++j)
                    object.array[j] = $root.appFrameProtoOut.JsParamMsgOutProto.toObject(message.array[j], options, q + 1);
            }
            return object;
        };

        /**
         * Converts this JsParamMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.JsParamMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        JsParamMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for JsParamMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.JsParamMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        JsParamMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.JsParamMsgOutProto";
        };

        return JsParamMsgOutProto;
    })();

    appFrameProtoOut.JSObjectMsgOutProto = (function() {

        /**
         * Properties of a JSObjectMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IJSObjectMsgOutProto
         * @property {string|null} [id] JSObjectMsgOutProto id
         */

        /**
         * Constructs a new JSObjectMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a JSObjectMsgOutProto.
         * @implements IJSObjectMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IJSObjectMsgOutProto=} [properties] Properties to set
         */
        function JSObjectMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * JSObjectMsgOutProto id.
         * @member {string} id
         * @memberof appFrameProtoOut.JSObjectMsgOutProto
         * @instance
         */
        JSObjectMsgOutProto.prototype.id = "";

        /**
         * Creates a new JSObjectMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.JSObjectMsgOutProto
         * @static
         * @param {appFrameProtoOut.IJSObjectMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.JSObjectMsgOutProto} JSObjectMsgOutProto instance
         */
        JSObjectMsgOutProto.create = function create(properties) {
            return new JSObjectMsgOutProto(properties);
        };

        /**
         * Decodes a JSObjectMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.JSObjectMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.JSObjectMsgOutProto} JSObjectMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        JSObjectMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.JSObjectMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.id = reader.string();
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
         * Creates a JSObjectMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.JSObjectMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.JSObjectMsgOutProto} JSObjectMsgOutProto
         */
        JSObjectMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.JSObjectMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.JSObjectMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.JSObjectMsgOutProto();
            if (object.id != null)
                message.id = String(object.id);
            return message;
        };

        /**
         * Creates a plain object from a JSObjectMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.JSObjectMsgOutProto
         * @static
         * @param {appFrameProtoOut.JSObjectMsgOutProto} message JSObjectMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        JSObjectMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults)
                object.id = "";
            if (message.id != null && Object.hasOwnProperty.call(message, "id"))
                object.id = message.id;
            return object;
        };

        /**
         * Converts this JSObjectMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.JSObjectMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        JSObjectMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for JSObjectMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.JSObjectMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        JSObjectMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.JSObjectMsgOutProto";
        };

        return JSObjectMsgOutProto;
    })();

    appFrameProtoOut.JavaObjectRefMsgOutProto = (function() {

        /**
         * Properties of a JavaObjectRefMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IJavaObjectRefMsgOutProto
         * @property {string|null} [id] JavaObjectRefMsgOutProto id
         * @property {Array.<string>|null} [methods] JavaObjectRefMsgOutProto methods
         */

        /**
         * Constructs a new JavaObjectRefMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a JavaObjectRefMsgOutProto.
         * @implements IJavaObjectRefMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IJavaObjectRefMsgOutProto=} [properties] Properties to set
         */
        function JavaObjectRefMsgOutProto(properties) {
            this.methods = [];
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * JavaObjectRefMsgOutProto id.
         * @member {string} id
         * @memberof appFrameProtoOut.JavaObjectRefMsgOutProto
         * @instance
         */
        JavaObjectRefMsgOutProto.prototype.id = "";

        /**
         * JavaObjectRefMsgOutProto methods.
         * @member {Array.<string>} methods
         * @memberof appFrameProtoOut.JavaObjectRefMsgOutProto
         * @instance
         */
        JavaObjectRefMsgOutProto.prototype.methods = $util.emptyArray;

        /**
         * Creates a new JavaObjectRefMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.JavaObjectRefMsgOutProto
         * @static
         * @param {appFrameProtoOut.IJavaObjectRefMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.JavaObjectRefMsgOutProto} JavaObjectRefMsgOutProto instance
         */
        JavaObjectRefMsgOutProto.create = function create(properties) {
            return new JavaObjectRefMsgOutProto(properties);
        };

        /**
         * Decodes a JavaObjectRefMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.JavaObjectRefMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.JavaObjectRefMsgOutProto} JavaObjectRefMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        JavaObjectRefMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.JavaObjectRefMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.id = reader.string();
                        break;
                    }
                case 2: {
                        if (!(message.methods && message.methods.length))
                            message.methods = [];
                        message.methods.push(reader.string());
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
         * Creates a JavaObjectRefMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.JavaObjectRefMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.JavaObjectRefMsgOutProto} JavaObjectRefMsgOutProto
         */
        JavaObjectRefMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.JavaObjectRefMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.JavaObjectRefMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.JavaObjectRefMsgOutProto();
            if (object.id != null)
                message.id = String(object.id);
            if (object.methods) {
                if (!Array.isArray(object.methods))
                    throw TypeError(".appFrameProtoOut.JavaObjectRefMsgOutProto.methods: array expected");
                message.methods = [];
                for (var i = 0; i < object.methods.length; ++i)
                    message.methods[i] = String(object.methods[i]);
            }
            return message;
        };

        /**
         * Creates a plain object from a JavaObjectRefMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.JavaObjectRefMsgOutProto
         * @static
         * @param {appFrameProtoOut.JavaObjectRefMsgOutProto} message JavaObjectRefMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        JavaObjectRefMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.arrays || options.defaults)
                object.methods = [];
            if (options.defaults)
                object.id = "";
            if (message.id != null && Object.hasOwnProperty.call(message, "id"))
                object.id = message.id;
            if (message.methods && message.methods.length) {
                object.methods = [];
                for (var j = 0; j < message.methods.length; ++j)
                    object.methods[j] = message.methods[j];
            }
            return object;
        };

        /**
         * Converts this JavaObjectRefMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.JavaObjectRefMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        JavaObjectRefMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for JavaObjectRefMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.JavaObjectRefMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        JavaObjectRefMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.JavaObjectRefMsgOutProto";
        };

        return JavaObjectRefMsgOutProto;
    })();

    appFrameProtoOut.JsResultMsgOutProto = (function() {

        /**
         * Properties of a JsResultMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IJsResultMsgOutProto
         * @property {string|null} [correlationId] JsResultMsgOutProto correlationId
         * @property {string|null} [error] JsResultMsgOutProto error
         * @property {appFrameProtoOut.IJsParamMsgOutProto|null} [value] JsResultMsgOutProto value
         */

        /**
         * Constructs a new JsResultMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a JsResultMsgOutProto.
         * @implements IJsResultMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IJsResultMsgOutProto=} [properties] Properties to set
         */
        function JsResultMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * JsResultMsgOutProto correlationId.
         * @member {string} correlationId
         * @memberof appFrameProtoOut.JsResultMsgOutProto
         * @instance
         */
        JsResultMsgOutProto.prototype.correlationId = "";

        /**
         * JsResultMsgOutProto error.
         * @member {string} error
         * @memberof appFrameProtoOut.JsResultMsgOutProto
         * @instance
         */
        JsResultMsgOutProto.prototype.error = "";

        /**
         * JsResultMsgOutProto value.
         * @member {appFrameProtoOut.IJsParamMsgOutProto|null|undefined} value
         * @memberof appFrameProtoOut.JsResultMsgOutProto
         * @instance
         */
        JsResultMsgOutProto.prototype.value = null;

        /**
         * Creates a new JsResultMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.JsResultMsgOutProto
         * @static
         * @param {appFrameProtoOut.IJsResultMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.JsResultMsgOutProto} JsResultMsgOutProto instance
         */
        JsResultMsgOutProto.create = function create(properties) {
            return new JsResultMsgOutProto(properties);
        };

        /**
         * Decodes a JsResultMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.JsResultMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.JsResultMsgOutProto} JsResultMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        JsResultMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.JsResultMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.correlationId = reader.string();
                        break;
                    }
                case 2: {
                        message.error = reader.string();
                        break;
                    }
                case 3: {
                        message.value = $root.appFrameProtoOut.JsParamMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1);
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
         * Creates a JsResultMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.JsResultMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.JsResultMsgOutProto} JsResultMsgOutProto
         */
        JsResultMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.JsResultMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.JsResultMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.JsResultMsgOutProto();
            if (object.correlationId != null)
                message.correlationId = String(object.correlationId);
            if (object.error != null)
                message.error = String(object.error);
            if (object.value != null) {
                if (!$util.isObject(object.value))
                    throw TypeError(".appFrameProtoOut.JsResultMsgOutProto.value: object expected");
                message.value = $root.appFrameProtoOut.JsParamMsgOutProto.fromObject(object.value, long + 1);
            }
            return message;
        };

        /**
         * Creates a plain object from a JsResultMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.JsResultMsgOutProto
         * @static
         * @param {appFrameProtoOut.JsResultMsgOutProto} message JsResultMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        JsResultMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.correlationId = "";
                object.error = "";
                object.value = null;
            }
            if (message.correlationId != null && Object.hasOwnProperty.call(message, "correlationId"))
                object.correlationId = message.correlationId;
            if (message.error != null && Object.hasOwnProperty.call(message, "error"))
                object.error = message.error;
            if (message.value != null && Object.hasOwnProperty.call(message, "value"))
                object.value = $root.appFrameProtoOut.JsParamMsgOutProto.toObject(message.value, options, q + 1);
            return object;
        };

        /**
         * Converts this JsResultMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.JsResultMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        JsResultMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for JsResultMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.JsResultMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        JsResultMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.JsResultMsgOutProto";
        };

        return JsResultMsgOutProto;
    })();

    appFrameProtoOut.PlaybackInfoMsgOutProto = (function() {

        /**
         * Properties of a PlaybackInfoMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IPlaybackInfoMsgOutProto
         * @property {number|null} [current] PlaybackInfoMsgOutProto current
         * @property {number|null} [total] PlaybackInfoMsgOutProto total
         */

        /**
         * Constructs a new PlaybackInfoMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a PlaybackInfoMsgOutProto.
         * @implements IPlaybackInfoMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IPlaybackInfoMsgOutProto=} [properties] Properties to set
         */
        function PlaybackInfoMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * PlaybackInfoMsgOutProto current.
         * @member {number} current
         * @memberof appFrameProtoOut.PlaybackInfoMsgOutProto
         * @instance
         */
        PlaybackInfoMsgOutProto.prototype.current = 0;

        /**
         * PlaybackInfoMsgOutProto total.
         * @member {number} total
         * @memberof appFrameProtoOut.PlaybackInfoMsgOutProto
         * @instance
         */
        PlaybackInfoMsgOutProto.prototype.total = 0;

        /**
         * Creates a new PlaybackInfoMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.PlaybackInfoMsgOutProto
         * @static
         * @param {appFrameProtoOut.IPlaybackInfoMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.PlaybackInfoMsgOutProto} PlaybackInfoMsgOutProto instance
         */
        PlaybackInfoMsgOutProto.create = function create(properties) {
            return new PlaybackInfoMsgOutProto(properties);
        };

        /**
         * Decodes a PlaybackInfoMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.PlaybackInfoMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.PlaybackInfoMsgOutProto} PlaybackInfoMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        PlaybackInfoMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.PlaybackInfoMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.current = reader.uint32();
                        break;
                    }
                case 2: {
                        message.total = reader.uint32();
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
         * Creates a PlaybackInfoMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.PlaybackInfoMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.PlaybackInfoMsgOutProto} PlaybackInfoMsgOutProto
         */
        PlaybackInfoMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.PlaybackInfoMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.PlaybackInfoMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.PlaybackInfoMsgOutProto();
            if (object.current != null)
                message.current = object.current >>> 0;
            if (object.total != null)
                message.total = object.total >>> 0;
            return message;
        };

        /**
         * Creates a plain object from a PlaybackInfoMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.PlaybackInfoMsgOutProto
         * @static
         * @param {appFrameProtoOut.PlaybackInfoMsgOutProto} message PlaybackInfoMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        PlaybackInfoMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.current = 0;
                object.total = 0;
            }
            if (message.current != null && Object.hasOwnProperty.call(message, "current"))
                object.current = message.current;
            if (message.total != null && Object.hasOwnProperty.call(message, "total"))
                object.total = message.total;
            return object;
        };

        /**
         * Converts this PlaybackInfoMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.PlaybackInfoMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        PlaybackInfoMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for PlaybackInfoMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.PlaybackInfoMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        PlaybackInfoMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.PlaybackInfoMsgOutProto";
        };

        return PlaybackInfoMsgOutProto;
    })();

    appFrameProtoOut.PixelsAreaRequestMsgOutProto = (function() {

        /**
         * Properties of a PixelsAreaRequestMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IPixelsAreaRequestMsgOutProto
         * @property {string|null} [correlationId] PixelsAreaRequestMsgOutProto correlationId
         * @property {number|null} [x] PixelsAreaRequestMsgOutProto x
         * @property {number|null} [y] PixelsAreaRequestMsgOutProto y
         * @property {number|null} [w] PixelsAreaRequestMsgOutProto w
         * @property {number|null} [h] PixelsAreaRequestMsgOutProto h
         */

        /**
         * Constructs a new PixelsAreaRequestMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a PixelsAreaRequestMsgOutProto.
         * @implements IPixelsAreaRequestMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IPixelsAreaRequestMsgOutProto=} [properties] Properties to set
         */
        function PixelsAreaRequestMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * PixelsAreaRequestMsgOutProto correlationId.
         * @member {string} correlationId
         * @memberof appFrameProtoOut.PixelsAreaRequestMsgOutProto
         * @instance
         */
        PixelsAreaRequestMsgOutProto.prototype.correlationId = "";

        /**
         * PixelsAreaRequestMsgOutProto x.
         * @member {number} x
         * @memberof appFrameProtoOut.PixelsAreaRequestMsgOutProto
         * @instance
         */
        PixelsAreaRequestMsgOutProto.prototype.x = 0;

        /**
         * PixelsAreaRequestMsgOutProto y.
         * @member {number} y
         * @memberof appFrameProtoOut.PixelsAreaRequestMsgOutProto
         * @instance
         */
        PixelsAreaRequestMsgOutProto.prototype.y = 0;

        /**
         * PixelsAreaRequestMsgOutProto w.
         * @member {number} w
         * @memberof appFrameProtoOut.PixelsAreaRequestMsgOutProto
         * @instance
         */
        PixelsAreaRequestMsgOutProto.prototype.w = 0;

        /**
         * PixelsAreaRequestMsgOutProto h.
         * @member {number} h
         * @memberof appFrameProtoOut.PixelsAreaRequestMsgOutProto
         * @instance
         */
        PixelsAreaRequestMsgOutProto.prototype.h = 0;

        /**
         * Creates a new PixelsAreaRequestMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.PixelsAreaRequestMsgOutProto
         * @static
         * @param {appFrameProtoOut.IPixelsAreaRequestMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.PixelsAreaRequestMsgOutProto} PixelsAreaRequestMsgOutProto instance
         */
        PixelsAreaRequestMsgOutProto.create = function create(properties) {
            return new PixelsAreaRequestMsgOutProto(properties);
        };

        /**
         * Decodes a PixelsAreaRequestMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.PixelsAreaRequestMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.PixelsAreaRequestMsgOutProto} PixelsAreaRequestMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        PixelsAreaRequestMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.PixelsAreaRequestMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.correlationId = reader.string();
                        break;
                    }
                case 2: {
                        message.x = reader.uint32();
                        break;
                    }
                case 3: {
                        message.y = reader.uint32();
                        break;
                    }
                case 4: {
                        message.w = reader.uint32();
                        break;
                    }
                case 5: {
                        message.h = reader.uint32();
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
         * Creates a PixelsAreaRequestMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.PixelsAreaRequestMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.PixelsAreaRequestMsgOutProto} PixelsAreaRequestMsgOutProto
         */
        PixelsAreaRequestMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.PixelsAreaRequestMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.PixelsAreaRequestMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.PixelsAreaRequestMsgOutProto();
            if (object.correlationId != null)
                message.correlationId = String(object.correlationId);
            if (object.x != null)
                message.x = object.x >>> 0;
            if (object.y != null)
                message.y = object.y >>> 0;
            if (object.w != null)
                message.w = object.w >>> 0;
            if (object.h != null)
                message.h = object.h >>> 0;
            return message;
        };

        /**
         * Creates a plain object from a PixelsAreaRequestMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.PixelsAreaRequestMsgOutProto
         * @static
         * @param {appFrameProtoOut.PixelsAreaRequestMsgOutProto} message PixelsAreaRequestMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        PixelsAreaRequestMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.correlationId = "";
                object.x = 0;
                object.y = 0;
                object.w = 0;
                object.h = 0;
            }
            if (message.correlationId != null && Object.hasOwnProperty.call(message, "correlationId"))
                object.correlationId = message.correlationId;
            if (message.x != null && Object.hasOwnProperty.call(message, "x"))
                object.x = message.x;
            if (message.y != null && Object.hasOwnProperty.call(message, "y"))
                object.y = message.y;
            if (message.w != null && Object.hasOwnProperty.call(message, "w"))
                object.w = message.w;
            if (message.h != null && Object.hasOwnProperty.call(message, "h"))
                object.h = message.h;
            return object;
        };

        /**
         * Converts this PixelsAreaRequestMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.PixelsAreaRequestMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        PixelsAreaRequestMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for PixelsAreaRequestMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.PixelsAreaRequestMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        PixelsAreaRequestMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.PixelsAreaRequestMsgOutProto";
        };

        return PixelsAreaRequestMsgOutProto;
    })();

    appFrameProtoOut.ComponentTreeMsgOutProto = (function() {

        /**
         * Properties of a ComponentTreeMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IComponentTreeMsgOutProto
         * @property {string|null} [componentType] ComponentTreeMsgOutProto componentType
         * @property {string|null} [name] ComponentTreeMsgOutProto name
         * @property {string|null} [value] ComponentTreeMsgOutProto value
         * @property {number|null} [screenX] ComponentTreeMsgOutProto screenX
         * @property {number|null} [screenY] ComponentTreeMsgOutProto screenY
         * @property {number|null} [width] ComponentTreeMsgOutProto width
         * @property {number|null} [height] ComponentTreeMsgOutProto height
         * @property {boolean|null} [enabled] ComponentTreeMsgOutProto enabled
         * @property {boolean|null} [visible] ComponentTreeMsgOutProto visible
         * @property {Array.<appFrameProtoOut.IComponentTreeMsgOutProto>|null} [components] ComponentTreeMsgOutProto components
         * @property {boolean|null} [hidden] ComponentTreeMsgOutProto hidden
         * @property {boolean|null} [selected] ComponentTreeMsgOutProto selected
         */

        /**
         * Constructs a new ComponentTreeMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a ComponentTreeMsgOutProto.
         * @implements IComponentTreeMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IComponentTreeMsgOutProto=} [properties] Properties to set
         */
        function ComponentTreeMsgOutProto(properties) {
            this.components = [];
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * ComponentTreeMsgOutProto componentType.
         * @member {string} componentType
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @instance
         */
        ComponentTreeMsgOutProto.prototype.componentType = "";

        /**
         * ComponentTreeMsgOutProto name.
         * @member {string} name
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @instance
         */
        ComponentTreeMsgOutProto.prototype.name = "";

        /**
         * ComponentTreeMsgOutProto value.
         * @member {string} value
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @instance
         */
        ComponentTreeMsgOutProto.prototype.value = "";

        /**
         * ComponentTreeMsgOutProto screenX.
         * @member {number} screenX
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @instance
         */
        ComponentTreeMsgOutProto.prototype.screenX = 0;

        /**
         * ComponentTreeMsgOutProto screenY.
         * @member {number} screenY
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @instance
         */
        ComponentTreeMsgOutProto.prototype.screenY = 0;

        /**
         * ComponentTreeMsgOutProto width.
         * @member {number} width
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @instance
         */
        ComponentTreeMsgOutProto.prototype.width = 0;

        /**
         * ComponentTreeMsgOutProto height.
         * @member {number} height
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @instance
         */
        ComponentTreeMsgOutProto.prototype.height = 0;

        /**
         * ComponentTreeMsgOutProto enabled.
         * @member {boolean} enabled
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @instance
         */
        ComponentTreeMsgOutProto.prototype.enabled = false;

        /**
         * ComponentTreeMsgOutProto visible.
         * @member {boolean} visible
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @instance
         */
        ComponentTreeMsgOutProto.prototype.visible = false;

        /**
         * ComponentTreeMsgOutProto components.
         * @member {Array.<appFrameProtoOut.IComponentTreeMsgOutProto>} components
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @instance
         */
        ComponentTreeMsgOutProto.prototype.components = $util.emptyArray;

        /**
         * ComponentTreeMsgOutProto hidden.
         * @member {boolean} hidden
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @instance
         */
        ComponentTreeMsgOutProto.prototype.hidden = false;

        /**
         * ComponentTreeMsgOutProto selected.
         * @member {boolean} selected
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @instance
         */
        ComponentTreeMsgOutProto.prototype.selected = false;

        /**
         * Creates a new ComponentTreeMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @static
         * @param {appFrameProtoOut.IComponentTreeMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.ComponentTreeMsgOutProto} ComponentTreeMsgOutProto instance
         */
        ComponentTreeMsgOutProto.create = function create(properties) {
            return new ComponentTreeMsgOutProto(properties);
        };

        /**
         * Decodes a ComponentTreeMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.ComponentTreeMsgOutProto} ComponentTreeMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        ComponentTreeMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.ComponentTreeMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.componentType = reader.string();
                        break;
                    }
                case 2: {
                        message.name = reader.string();
                        break;
                    }
                case 3: {
                        message.value = reader.string();
                        break;
                    }
                case 4: {
                        message.screenX = reader.sint32();
                        break;
                    }
                case 5: {
                        message.screenY = reader.sint32();
                        break;
                    }
                case 6: {
                        message.width = reader.sint32();
                        break;
                    }
                case 7: {
                        message.height = reader.sint32();
                        break;
                    }
                case 8: {
                        message.enabled = reader.bool();
                        break;
                    }
                case 9: {
                        message.visible = reader.bool();
                        break;
                    }
                case 10: {
                        if (!(message.components && message.components.length))
                            message.components = [];
                        message.components.push($root.appFrameProtoOut.ComponentTreeMsgOutProto.decode(reader, reader.uint32(), undefined, long + 1));
                        break;
                    }
                case 11: {
                        message.hidden = reader.bool();
                        break;
                    }
                case 12: {
                        message.selected = reader.bool();
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
         * Creates a ComponentTreeMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.ComponentTreeMsgOutProto} ComponentTreeMsgOutProto
         */
        ComponentTreeMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.ComponentTreeMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.ComponentTreeMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.ComponentTreeMsgOutProto();
            if (object.componentType != null)
                message.componentType = String(object.componentType);
            if (object.name != null)
                message.name = String(object.name);
            if (object.value != null)
                message.value = String(object.value);
            if (object.screenX != null)
                message.screenX = object.screenX | 0;
            if (object.screenY != null)
                message.screenY = object.screenY | 0;
            if (object.width != null)
                message.width = object.width | 0;
            if (object.height != null)
                message.height = object.height | 0;
            if (object.enabled != null)
                message.enabled = Boolean(object.enabled);
            if (object.visible != null)
                message.visible = Boolean(object.visible);
            if (object.components) {
                if (!Array.isArray(object.components))
                    throw TypeError(".appFrameProtoOut.ComponentTreeMsgOutProto.components: array expected");
                message.components = [];
                for (var i = 0; i < object.components.length; ++i) {
                    if (!$util.isObject(object.components[i]))
                        throw TypeError(".appFrameProtoOut.ComponentTreeMsgOutProto.components: object expected");
                    message.components[i] = $root.appFrameProtoOut.ComponentTreeMsgOutProto.fromObject(object.components[i], long + 1);
                }
            }
            if (object.hidden != null)
                message.hidden = Boolean(object.hidden);
            if (object.selected != null)
                message.selected = Boolean(object.selected);
            return message;
        };

        /**
         * Creates a plain object from a ComponentTreeMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @static
         * @param {appFrameProtoOut.ComponentTreeMsgOutProto} message ComponentTreeMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        ComponentTreeMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.arrays || options.defaults)
                object.components = [];
            if (options.defaults) {
                object.componentType = "";
                object.name = "";
                object.value = "";
                object.screenX = 0;
                object.screenY = 0;
                object.width = 0;
                object.height = 0;
                object.enabled = false;
                object.visible = false;
                object.hidden = false;
                object.selected = false;
            }
            if (message.componentType != null && Object.hasOwnProperty.call(message, "componentType"))
                object.componentType = message.componentType;
            if (message.name != null && Object.hasOwnProperty.call(message, "name"))
                object.name = message.name;
            if (message.value != null && Object.hasOwnProperty.call(message, "value"))
                object.value = message.value;
            if (message.screenX != null && Object.hasOwnProperty.call(message, "screenX"))
                object.screenX = message.screenX;
            if (message.screenY != null && Object.hasOwnProperty.call(message, "screenY"))
                object.screenY = message.screenY;
            if (message.width != null && Object.hasOwnProperty.call(message, "width"))
                object.width = message.width;
            if (message.height != null && Object.hasOwnProperty.call(message, "height"))
                object.height = message.height;
            if (message.enabled != null && Object.hasOwnProperty.call(message, "enabled"))
                object.enabled = message.enabled;
            if (message.visible != null && Object.hasOwnProperty.call(message, "visible"))
                object.visible = message.visible;
            if (message.components && message.components.length) {
                object.components = [];
                for (var j = 0; j < message.components.length; ++j)
                    object.components[j] = $root.appFrameProtoOut.ComponentTreeMsgOutProto.toObject(message.components[j], options, q + 1);
            }
            if (message.hidden != null && Object.hasOwnProperty.call(message, "hidden"))
                object.hidden = message.hidden;
            if (message.selected != null && Object.hasOwnProperty.call(message, "selected"))
                object.selected = message.selected;
            return object;
        };

        /**
         * Converts this ComponentTreeMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        ComponentTreeMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for ComponentTreeMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.ComponentTreeMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        ComponentTreeMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.ComponentTreeMsgOutProto";
        };

        return ComponentTreeMsgOutProto;
    })();

    appFrameProtoOut.ActionEventMsgOutProto = (function() {

        /**
         * Properties of an ActionEventMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IActionEventMsgOutProto
         * @property {string|null} [actionName] ActionEventMsgOutProto actionName
         * @property {string|null} [data] ActionEventMsgOutProto data
         * @property {Uint8Array|null} [binaryData] ActionEventMsgOutProto binaryData
         * @property {string|null} [windowId] ActionEventMsgOutProto windowId
         */

        /**
         * Constructs a new ActionEventMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents an ActionEventMsgOutProto.
         * @implements IActionEventMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IActionEventMsgOutProto=} [properties] Properties to set
         */
        function ActionEventMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * ActionEventMsgOutProto actionName.
         * @member {string} actionName
         * @memberof appFrameProtoOut.ActionEventMsgOutProto
         * @instance
         */
        ActionEventMsgOutProto.prototype.actionName = "";

        /**
         * ActionEventMsgOutProto data.
         * @member {string} data
         * @memberof appFrameProtoOut.ActionEventMsgOutProto
         * @instance
         */
        ActionEventMsgOutProto.prototype.data = "";

        /**
         * ActionEventMsgOutProto binaryData.
         * @member {Uint8Array} binaryData
         * @memberof appFrameProtoOut.ActionEventMsgOutProto
         * @instance
         */
        ActionEventMsgOutProto.prototype.binaryData = $util.newBuffer([]);

        /**
         * ActionEventMsgOutProto windowId.
         * @member {string} windowId
         * @memberof appFrameProtoOut.ActionEventMsgOutProto
         * @instance
         */
        ActionEventMsgOutProto.prototype.windowId = "";

        /**
         * Creates a new ActionEventMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.ActionEventMsgOutProto
         * @static
         * @param {appFrameProtoOut.IActionEventMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.ActionEventMsgOutProto} ActionEventMsgOutProto instance
         */
        ActionEventMsgOutProto.create = function create(properties) {
            return new ActionEventMsgOutProto(properties);
        };

        /**
         * Decodes an ActionEventMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.ActionEventMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.ActionEventMsgOutProto} ActionEventMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        ActionEventMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.ActionEventMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.actionName = reader.string();
                        break;
                    }
                case 2: {
                        message.data = reader.string();
                        break;
                    }
                case 3: {
                        message.binaryData = reader.bytes();
                        break;
                    }
                case 4: {
                        message.windowId = reader.string();
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
         * Creates an ActionEventMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.ActionEventMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.ActionEventMsgOutProto} ActionEventMsgOutProto
         */
        ActionEventMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.ActionEventMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.ActionEventMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.ActionEventMsgOutProto();
            if (object.actionName != null)
                message.actionName = String(object.actionName);
            if (object.data != null)
                message.data = String(object.data);
            if (object.binaryData != null)
                if (typeof object.binaryData === "string")
                    $util.base64.decode(object.binaryData, message.binaryData = $util.newBuffer($util.base64.length(object.binaryData)), 0);
                else if (object.binaryData.length >= 0)
                    message.binaryData = object.binaryData;
            if (object.windowId != null)
                message.windowId = String(object.windowId);
            return message;
        };

        /**
         * Creates a plain object from an ActionEventMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.ActionEventMsgOutProto
         * @static
         * @param {appFrameProtoOut.ActionEventMsgOutProto} message ActionEventMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        ActionEventMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.actionName = "";
                object.data = "";
                if (options.bytes === String)
                    object.binaryData = "";
                else {
                    object.binaryData = [];
                    if (options.bytes !== Array)
                        object.binaryData = $util.newBuffer(object.binaryData);
                }
                object.windowId = "";
            }
            if (message.actionName != null && Object.hasOwnProperty.call(message, "actionName"))
                object.actionName = message.actionName;
            if (message.data != null && Object.hasOwnProperty.call(message, "data"))
                object.data = message.data;
            if (message.binaryData != null && Object.hasOwnProperty.call(message, "binaryData"))
                object.binaryData = options.bytes === String ? $util.base64.encode(message.binaryData, 0, message.binaryData.length) : options.bytes === Array ? Array.prototype.slice.call(message.binaryData) : message.binaryData;
            if (message.windowId != null && Object.hasOwnProperty.call(message, "windowId"))
                object.windowId = message.windowId;
            return object;
        };

        /**
         * Converts this ActionEventMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.ActionEventMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        ActionEventMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for ActionEventMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.ActionEventMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        ActionEventMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.ActionEventMsgOutProto";
        };

        return ActionEventMsgOutProto;
    })();

    appFrameProtoOut.AudioEventMsgOutProto = (function() {

        /**
         * Properties of an AudioEventMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface IAudioEventMsgOutProto
         * @property {string|null} [id] AudioEventMsgOutProto id
         * @property {appFrameProtoOut.AudioEventMsgOutProto.AudioEventTypeProto|null} [eventType] AudioEventMsgOutProto eventType
         * @property {Uint8Array|null} [data] AudioEventMsgOutProto data
         * @property {number|null} [time] AudioEventMsgOutProto time
         * @property {number|null} [loop] AudioEventMsgOutProto loop
         */

        /**
         * Constructs a new AudioEventMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents an AudioEventMsgOutProto.
         * @implements IAudioEventMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.IAudioEventMsgOutProto=} [properties] Properties to set
         */
        function AudioEventMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * AudioEventMsgOutProto id.
         * @member {string} id
         * @memberof appFrameProtoOut.AudioEventMsgOutProto
         * @instance
         */
        AudioEventMsgOutProto.prototype.id = "";

        /**
         * AudioEventMsgOutProto eventType.
         * @member {appFrameProtoOut.AudioEventMsgOutProto.AudioEventTypeProto} eventType
         * @memberof appFrameProtoOut.AudioEventMsgOutProto
         * @instance
         */
        AudioEventMsgOutProto.prototype.eventType = 0;

        /**
         * AudioEventMsgOutProto data.
         * @member {Uint8Array} data
         * @memberof appFrameProtoOut.AudioEventMsgOutProto
         * @instance
         */
        AudioEventMsgOutProto.prototype.data = $util.newBuffer([]);

        /**
         * AudioEventMsgOutProto time.
         * @member {number} time
         * @memberof appFrameProtoOut.AudioEventMsgOutProto
         * @instance
         */
        AudioEventMsgOutProto.prototype.time = 0;

        /**
         * AudioEventMsgOutProto loop.
         * @member {number} loop
         * @memberof appFrameProtoOut.AudioEventMsgOutProto
         * @instance
         */
        AudioEventMsgOutProto.prototype.loop = 0;

        /**
         * Creates a new AudioEventMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.AudioEventMsgOutProto
         * @static
         * @param {appFrameProtoOut.IAudioEventMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.AudioEventMsgOutProto} AudioEventMsgOutProto instance
         */
        AudioEventMsgOutProto.create = function create(properties) {
            return new AudioEventMsgOutProto(properties);
        };

        /**
         * Decodes an AudioEventMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.AudioEventMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.AudioEventMsgOutProto} AudioEventMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        AudioEventMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.AudioEventMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.id = reader.string();
                        break;
                    }
                case 2: {
                        message.eventType = reader.int32();
                        break;
                    }
                case 3: {
                        message.data = reader.bytes();
                        break;
                    }
                case 4: {
                        message.time = reader.float();
                        break;
                    }
                case 5: {
                        message.loop = reader.sint32();
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
         * Creates an AudioEventMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.AudioEventMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.AudioEventMsgOutProto} AudioEventMsgOutProto
         */
        AudioEventMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.AudioEventMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.AudioEventMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.AudioEventMsgOutProto();
            if (object.id != null)
                message.id = String(object.id);
            switch (object.eventType) {
            default:
                if (typeof object.eventType === "number") {
                    message.eventType = object.eventType;
                    break;
                }
                break;
            case "play":
            case 0:
                message.eventType = 0;
                break;
            case "stop":
            case 1:
                message.eventType = 1;
                break;
            case "update":
            case 2:
                message.eventType = 2;
                break;
            case "dispose":
            case 3:
                message.eventType = 3;
                break;
            }
            if (object.data != null)
                if (typeof object.data === "string")
                    $util.base64.decode(object.data, message.data = $util.newBuffer($util.base64.length(object.data)), 0);
                else if (object.data.length >= 0)
                    message.data = object.data;
            if (object.time != null)
                message.time = Number(object.time);
            if (object.loop != null)
                message.loop = object.loop | 0;
            return message;
        };

        /**
         * Creates a plain object from an AudioEventMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.AudioEventMsgOutProto
         * @static
         * @param {appFrameProtoOut.AudioEventMsgOutProto} message AudioEventMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        AudioEventMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.id = "";
                object.eventType = options.enums === String ? "play" : 0;
                if (options.bytes === String)
                    object.data = "";
                else {
                    object.data = [];
                    if (options.bytes !== Array)
                        object.data = $util.newBuffer(object.data);
                }
                object.time = 0;
                object.loop = 0;
            }
            if (message.id != null && Object.hasOwnProperty.call(message, "id"))
                object.id = message.id;
            if (message.eventType != null && Object.hasOwnProperty.call(message, "eventType"))
                object.eventType = options.enums === String ? $root.appFrameProtoOut.AudioEventMsgOutProto.AudioEventTypeProto[message.eventType] === undefined ? message.eventType : $root.appFrameProtoOut.AudioEventMsgOutProto.AudioEventTypeProto[message.eventType] : message.eventType;
            if (message.data != null && Object.hasOwnProperty.call(message, "data"))
                object.data = options.bytes === String ? $util.base64.encode(message.data, 0, message.data.length) : options.bytes === Array ? Array.prototype.slice.call(message.data) : message.data;
            if (message.time != null && Object.hasOwnProperty.call(message, "time"))
                object.time = options.json && !isFinite(message.time) ? String(message.time) : message.time;
            if (message.loop != null && Object.hasOwnProperty.call(message, "loop"))
                object.loop = message.loop;
            return object;
        };

        /**
         * Converts this AudioEventMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.AudioEventMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        AudioEventMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for AudioEventMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.AudioEventMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        AudioEventMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.AudioEventMsgOutProto";
        };

        /**
         * AudioEventTypeProto enum.
         * @name appFrameProtoOut.AudioEventMsgOutProto.AudioEventTypeProto
         * @enum {number}
         * @property {number} play=0 play value
         * @property {number} stop=1 stop value
         * @property {number} update=2 update value
         * @property {number} dispose=3 dispose value
         */
        AudioEventMsgOutProto.AudioEventTypeProto = (function() {
            var valuesById = {}, values = Object.create(valuesById);
            values[valuesById[0] = "play"] = 0;
            values[valuesById[1] = "stop"] = 1;
            values[valuesById[2] = "update"] = 2;
            values[valuesById[3] = "dispose"] = 3;
            return values;
        })();

        return AudioEventMsgOutProto;
    })();

    appFrameProtoOut.CursorChangeEventMsgOutProto = (function() {

        /**
         * Properties of a CursorChangeEventMsgOutProto.
         * @memberof appFrameProtoOut
         * @interface ICursorChangeEventMsgOutProto
         * @property {string|null} [cursor] CursorChangeEventMsgOutProto cursor
         * @property {Uint8Array|null} [b64img] CursorChangeEventMsgOutProto b64img
         * @property {string|null} [curFile] CursorChangeEventMsgOutProto curFile
         * @property {number|null} [x] CursorChangeEventMsgOutProto x
         * @property {number|null} [y] CursorChangeEventMsgOutProto y
         * @property {string|null} [winId] CursorChangeEventMsgOutProto winId
         */

        /**
         * Constructs a new CursorChangeEventMsgOutProto.
         * @memberof appFrameProtoOut
         * @classdesc Represents a CursorChangeEventMsgOutProto.
         * @implements ICursorChangeEventMsgOutProto
         * @constructor
         * @param {appFrameProtoOut.ICursorChangeEventMsgOutProto=} [properties] Properties to set
         */
        function CursorChangeEventMsgOutProto(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null && keys[i] !== "__proto__")
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * CursorChangeEventMsgOutProto cursor.
         * @member {string} cursor
         * @memberof appFrameProtoOut.CursorChangeEventMsgOutProto
         * @instance
         */
        CursorChangeEventMsgOutProto.prototype.cursor = "";

        /**
         * CursorChangeEventMsgOutProto b64img.
         * @member {Uint8Array} b64img
         * @memberof appFrameProtoOut.CursorChangeEventMsgOutProto
         * @instance
         */
        CursorChangeEventMsgOutProto.prototype.b64img = $util.newBuffer([]);

        /**
         * CursorChangeEventMsgOutProto curFile.
         * @member {string} curFile
         * @memberof appFrameProtoOut.CursorChangeEventMsgOutProto
         * @instance
         */
        CursorChangeEventMsgOutProto.prototype.curFile = "";

        /**
         * CursorChangeEventMsgOutProto x.
         * @member {number} x
         * @memberof appFrameProtoOut.CursorChangeEventMsgOutProto
         * @instance
         */
        CursorChangeEventMsgOutProto.prototype.x = 0;

        /**
         * CursorChangeEventMsgOutProto y.
         * @member {number} y
         * @memberof appFrameProtoOut.CursorChangeEventMsgOutProto
         * @instance
         */
        CursorChangeEventMsgOutProto.prototype.y = 0;

        /**
         * CursorChangeEventMsgOutProto winId.
         * @member {string} winId
         * @memberof appFrameProtoOut.CursorChangeEventMsgOutProto
         * @instance
         */
        CursorChangeEventMsgOutProto.prototype.winId = "";

        /**
         * Creates a new CursorChangeEventMsgOutProto instance using the specified properties.
         * @function create
         * @memberof appFrameProtoOut.CursorChangeEventMsgOutProto
         * @static
         * @param {appFrameProtoOut.ICursorChangeEventMsgOutProto=} [properties] Properties to set
         * @returns {appFrameProtoOut.CursorChangeEventMsgOutProto} CursorChangeEventMsgOutProto instance
         */
        CursorChangeEventMsgOutProto.create = function create(properties) {
            return new CursorChangeEventMsgOutProto(properties);
        };

        /**
         * Decodes a CursorChangeEventMsgOutProto message from the specified reader or buffer.
         * @function decode
         * @memberof appFrameProtoOut.CursorChangeEventMsgOutProto
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {appFrameProtoOut.CursorChangeEventMsgOutProto} CursorChangeEventMsgOutProto
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        CursorChangeEventMsgOutProto.decode = function decode(reader, length, error, long) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            if (long === undefined)
                long = 0;
            if (long > $Reader.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.appFrameProtoOut.CursorChangeEventMsgOutProto();
            while (reader.pos < end) {
                var tag = reader.uint32();
                if (tag === error)
                    break;
                switch (tag >>> 3) {
                case 1: {
                        message.cursor = reader.string();
                        break;
                    }
                case 2: {
                        message.b64img = reader.bytes();
                        break;
                    }
                case 3: {
                        message.curFile = reader.string();
                        break;
                    }
                case 4: {
                        message.x = reader.sint32();
                        break;
                    }
                case 5: {
                        message.y = reader.sint32();
                        break;
                    }
                case 6: {
                        message.winId = reader.string();
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
         * Creates a CursorChangeEventMsgOutProto message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof appFrameProtoOut.CursorChangeEventMsgOutProto
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {appFrameProtoOut.CursorChangeEventMsgOutProto} CursorChangeEventMsgOutProto
         */
        CursorChangeEventMsgOutProto.fromObject = function fromObject(object, long) {
            if (object instanceof $root.appFrameProtoOut.CursorChangeEventMsgOutProto)
                return object;
            if (!$util.isObject(object))
                throw TypeError(".appFrameProtoOut.CursorChangeEventMsgOutProto: object expected");
            if (long === undefined)
                long = 0;
            if (long > $util.recursionLimit)
                throw Error("maximum nesting depth exceeded");
            var message = new $root.appFrameProtoOut.CursorChangeEventMsgOutProto();
            if (object.cursor != null)
                message.cursor = String(object.cursor);
            if (object.b64img != null)
                if (typeof object.b64img === "string")
                    $util.base64.decode(object.b64img, message.b64img = $util.newBuffer($util.base64.length(object.b64img)), 0);
                else if (object.b64img.length >= 0)
                    message.b64img = object.b64img;
            if (object.curFile != null)
                message.curFile = String(object.curFile);
            if (object.x != null)
                message.x = object.x | 0;
            if (object.y != null)
                message.y = object.y | 0;
            if (object.winId != null)
                message.winId = String(object.winId);
            return message;
        };

        /**
         * Creates a plain object from a CursorChangeEventMsgOutProto message. Also converts values to other types if specified.
         * @function toObject
         * @memberof appFrameProtoOut.CursorChangeEventMsgOutProto
         * @static
         * @param {appFrameProtoOut.CursorChangeEventMsgOutProto} message CursorChangeEventMsgOutProto
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        CursorChangeEventMsgOutProto.toObject = function toObject(message, options, q) {
            if (!options)
                options = {};
            if (q === undefined)
                q = 0;
            if (q > $util.recursionLimit)
                throw Error("max depth exceeded");
            var object = {};
            if (options.defaults) {
                object.cursor = "";
                if (options.bytes === String)
                    object.b64img = "";
                else {
                    object.b64img = [];
                    if (options.bytes !== Array)
                        object.b64img = $util.newBuffer(object.b64img);
                }
                object.curFile = "";
                object.x = 0;
                object.y = 0;
                object.winId = "";
            }
            if (message.cursor != null && Object.hasOwnProperty.call(message, "cursor"))
                object.cursor = message.cursor;
            if (message.b64img != null && Object.hasOwnProperty.call(message, "b64img"))
                object.b64img = options.bytes === String ? $util.base64.encode(message.b64img, 0, message.b64img.length) : options.bytes === Array ? Array.prototype.slice.call(message.b64img) : message.b64img;
            if (message.curFile != null && Object.hasOwnProperty.call(message, "curFile"))
                object.curFile = message.curFile;
            if (message.x != null && Object.hasOwnProperty.call(message, "x"))
                object.x = message.x;
            if (message.y != null && Object.hasOwnProperty.call(message, "y"))
                object.y = message.y;
            if (message.winId != null && Object.hasOwnProperty.call(message, "winId"))
                object.winId = message.winId;
            return object;
        };

        /**
         * Converts this CursorChangeEventMsgOutProto to JSON.
         * @function toJSON
         * @memberof appFrameProtoOut.CursorChangeEventMsgOutProto
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        CursorChangeEventMsgOutProto.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        /**
         * Gets the default type url for CursorChangeEventMsgOutProto
         * @function getTypeUrl
         * @memberof appFrameProtoOut.CursorChangeEventMsgOutProto
         * @static
         * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns {string} The default type url
         */
        CursorChangeEventMsgOutProto.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
            if (typeUrlPrefix === undefined) {
                typeUrlPrefix = "type.googleapis.com";
            }
            return typeUrlPrefix + "/appFrameProtoOut.CursorChangeEventMsgOutProto";
        };

        return CursorChangeEventMsgOutProto;
    })();

    return appFrameProtoOut;
})();

module.exports = $root;
