import * as $protobuf from "protobufjs";
import Long = require("long");
/** Namespace org. */
export namespace org {

    /** Namespace webswing. */
    namespace webswing {

        /** Namespace directdraw. */
        namespace directdraw {

            /** Namespace proto. */
            namespace proto {

                /** Properties of a WebImageProto. */
                interface IWebImageProto {

                    /** WebImageProto width */
                    width: number;

                    /** WebImageProto height */
                    height: number;

                    /** WebImageProto instructions */
                    instructions?: (org.webswing.directdraw.proto.IDrawInstructionProto[]|null);

                    /** WebImageProto constants */
                    constants?: (org.webswing.directdraw.proto.IDrawConstantProto[]|null);

                    /** WebImageProto fontFaces */
                    fontFaces?: (org.webswing.directdraw.proto.IFontFaceProto[]|null);
                }

                /** Represents a WebImageProto. */
                class WebImageProto implements IWebImageProto {

                    /**
                     * Constructs a new WebImageProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IWebImageProto);

                    /** WebImageProto width. */
                    public width: number;

                    /** WebImageProto height. */
                    public height: number;

                    /** WebImageProto instructions. */
                    public instructions: org.webswing.directdraw.proto.IDrawInstructionProto[];

                    /** WebImageProto constants. */
                    public constants: org.webswing.directdraw.proto.IDrawConstantProto[];

                    /** WebImageProto fontFaces. */
                    public fontFaces: org.webswing.directdraw.proto.IFontFaceProto[];

                    /**
                     * Decodes a WebImageProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns WebImageProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.WebImageProto;

                    /**
                     * Creates a WebImageProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns WebImageProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.WebImageProto;

                    /**
                     * Creates a plain object from a WebImageProto message. Also converts values to other types if specified.
                     * @param message WebImageProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.WebImageProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this WebImageProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for WebImageProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** Properties of a DrawInstructionProto. */
                interface IDrawInstructionProto {

                    /** DrawInstructionProto inst */
                    inst: org.webswing.directdraw.proto.DrawInstructionProto.InstructionProto;

                    /** DrawInstructionProto args */
                    args?: (number[]|null);

                    /** DrawInstructionProto webImage */
                    webImage?: (Uint8Array|null);
                }

                /** Represents a DrawInstructionProto. */
                class DrawInstructionProto implements IDrawInstructionProto {

                    /**
                     * Constructs a new DrawInstructionProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IDrawInstructionProto);

                    /** DrawInstructionProto inst. */
                    public inst: org.webswing.directdraw.proto.DrawInstructionProto.InstructionProto;

                    /** DrawInstructionProto args. */
                    public args: number[];

                    /** DrawInstructionProto webImage. */
                    public webImage: Uint8Array;

                    /**
                     * Decodes a DrawInstructionProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns DrawInstructionProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.DrawInstructionProto;

                    /**
                     * Creates a DrawInstructionProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns DrawInstructionProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.DrawInstructionProto;

                    /**
                     * Creates a plain object from a DrawInstructionProto message. Also converts values to other types if specified.
                     * @param message DrawInstructionProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.DrawInstructionProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this DrawInstructionProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for DrawInstructionProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                namespace DrawInstructionProto {

                    /** InstructionProto enum. */
                    enum InstructionProto {
                        DRAW = 0,
                        FILL = 1,
                        DRAW_IMAGE = 2,
                        DRAW_WEBIMAGE = 3,
                        DRAW_STRING = 4,
                        COPY_AREA = 5,
                        GRAPHICS_DISPOSE = 6,
                        GRAPHICS_SWITCH = 7,
                        GRAPHICS_CREATE = 8,
                        TRANSFORM = 9,
                        SET_PAINT = 10,
                        SET_FONT = 11,
                        SET_STROKE = 12,
                        SET_COMPOSITE = 13,
                        DRAW_GLYPH_LIST = 14
                    }
                }

                /** Properties of a DrawConstantProto. */
                interface IDrawConstantProto {

                    /** DrawConstantProto id */
                    id: number;

                    /** DrawConstantProto color */
                    color?: (org.webswing.directdraw.proto.IColorProto|null);

                    /** DrawConstantProto image */
                    image?: (org.webswing.directdraw.proto.IImageProto|null);

                    /** DrawConstantProto transform */
                    transform?: (org.webswing.directdraw.proto.ITransformProto|null);

                    /** DrawConstantProto string */
                    string?: (string|null);

                    /** DrawConstantProto path */
                    path?: (org.webswing.directdraw.proto.IPathProto|null);

                    /** DrawConstantProto font */
                    font?: (org.webswing.directdraw.proto.IFontProto|null);

                    /** DrawConstantProto linearGrad */
                    linearGrad?: (org.webswing.directdraw.proto.ILinearGradientProto|null);

                    /** DrawConstantProto radialGrad */
                    radialGrad?: (org.webswing.directdraw.proto.IRadialGradientProto|null);

                    /** DrawConstantProto points */
                    points?: (org.webswing.directdraw.proto.IPointsProto|null);

                    /** DrawConstantProto rectangle */
                    rectangle?: (org.webswing.directdraw.proto.IRectangleProto|null);

                    /** DrawConstantProto ellipse */
                    ellipse?: (org.webswing.directdraw.proto.IEllipseProto|null);

                    /** DrawConstantProto roundRectangle */
                    roundRectangle?: (org.webswing.directdraw.proto.IRoundRectangleProto|null);

                    /** DrawConstantProto arc */
                    arc?: (org.webswing.directdraw.proto.IArcProto|null);

                    /** DrawConstantProto stroke */
                    stroke?: (org.webswing.directdraw.proto.IStrokeProto|null);

                    /** DrawConstantProto composite */
                    composite?: (org.webswing.directdraw.proto.ICompositeProto|null);

                    /** DrawConstantProto texture */
                    texture?: (org.webswing.directdraw.proto.ITextureProto|null);

                    /** DrawConstantProto glyph */
                    glyph?: (org.webswing.directdraw.proto.IGlyphProto|null);

                    /** DrawConstantProto combined */
                    combined?: (org.webswing.directdraw.proto.ICombinedProto|null);
                }

                /** Represents a DrawConstantProto. */
                class DrawConstantProto implements IDrawConstantProto {

                    /**
                     * Constructs a new DrawConstantProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IDrawConstantProto);

                    /** DrawConstantProto id. */
                    public id: number;

                    /** DrawConstantProto color. */
                    public color?: (org.webswing.directdraw.proto.IColorProto|null);

                    /** DrawConstantProto image. */
                    public image?: (org.webswing.directdraw.proto.IImageProto|null);

                    /** DrawConstantProto transform. */
                    public transform?: (org.webswing.directdraw.proto.ITransformProto|null);

                    /** DrawConstantProto string. */
                    public string: string;

                    /** DrawConstantProto path. */
                    public path?: (org.webswing.directdraw.proto.IPathProto|null);

                    /** DrawConstantProto font. */
                    public font?: (org.webswing.directdraw.proto.IFontProto|null);

                    /** DrawConstantProto linearGrad. */
                    public linearGrad?: (org.webswing.directdraw.proto.ILinearGradientProto|null);

                    /** DrawConstantProto radialGrad. */
                    public radialGrad?: (org.webswing.directdraw.proto.IRadialGradientProto|null);

                    /** DrawConstantProto points. */
                    public points?: (org.webswing.directdraw.proto.IPointsProto|null);

                    /** DrawConstantProto rectangle. */
                    public rectangle?: (org.webswing.directdraw.proto.IRectangleProto|null);

                    /** DrawConstantProto ellipse. */
                    public ellipse?: (org.webswing.directdraw.proto.IEllipseProto|null);

                    /** DrawConstantProto roundRectangle. */
                    public roundRectangle?: (org.webswing.directdraw.proto.IRoundRectangleProto|null);

                    /** DrawConstantProto arc. */
                    public arc?: (org.webswing.directdraw.proto.IArcProto|null);

                    /** DrawConstantProto stroke. */
                    public stroke?: (org.webswing.directdraw.proto.IStrokeProto|null);

                    /** DrawConstantProto composite. */
                    public composite?: (org.webswing.directdraw.proto.ICompositeProto|null);

                    /** DrawConstantProto texture. */
                    public texture?: (org.webswing.directdraw.proto.ITextureProto|null);

                    /** DrawConstantProto glyph. */
                    public glyph?: (org.webswing.directdraw.proto.IGlyphProto|null);

                    /** DrawConstantProto combined. */
                    public combined?: (org.webswing.directdraw.proto.ICombinedProto|null);

                    /**
                     * Decodes a DrawConstantProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns DrawConstantProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.DrawConstantProto;

                    /**
                     * Creates a DrawConstantProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns DrawConstantProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.DrawConstantProto;

                    /**
                     * Creates a plain object from a DrawConstantProto message. Also converts values to other types if specified.
                     * @param message DrawConstantProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.DrawConstantProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this DrawConstantProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for DrawConstantProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** Properties of a FontFaceProto. */
                interface IFontFaceProto {

                    /** FontFaceProto name */
                    name: string;

                    /** FontFaceProto font */
                    font: Uint8Array;

                    /** FontFaceProto style */
                    style?: (string|null);
                }

                /** Represents a FontFaceProto. */
                class FontFaceProto implements IFontFaceProto {

                    /**
                     * Constructs a new FontFaceProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IFontFaceProto);

                    /** FontFaceProto name. */
                    public name: string;

                    /** FontFaceProto font. */
                    public font: Uint8Array;

                    /** FontFaceProto style. */
                    public style: string;

                    /**
                     * Decodes a FontFaceProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns FontFaceProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.FontFaceProto;

                    /**
                     * Creates a FontFaceProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns FontFaceProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.FontFaceProto;

                    /**
                     * Creates a plain object from a FontFaceProto message. Also converts values to other types if specified.
                     * @param message FontFaceProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.FontFaceProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this FontFaceProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for FontFaceProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** Properties of a ColorProto. */
                interface IColorProto {

                    /** ColorProto rgba */
                    rgba: number;
                }

                /** Represents a ColorProto. */
                class ColorProto implements IColorProto {

                    /**
                     * Constructs a new ColorProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IColorProto);

                    /** ColorProto rgba. */
                    public rgba: number;

                    /**
                     * Decodes a ColorProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns ColorProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.ColorProto;

                    /**
                     * Creates a ColorProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns ColorProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.ColorProto;

                    /**
                     * Creates a plain object from a ColorProto message. Also converts values to other types if specified.
                     * @param message ColorProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.ColorProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this ColorProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for ColorProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** Properties of an ImageProto. */
                interface IImageProto {

                    /** ImageProto data */
                    data: Uint8Array;
                }

                /** Represents an ImageProto. */
                class ImageProto implements IImageProto {

                    /**
                     * Constructs a new ImageProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IImageProto);

                    /** ImageProto data. */
                    public data: Uint8Array;

                    /**
                     * Decodes an ImageProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns ImageProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.ImageProto;

                    /**
                     * Creates an ImageProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns ImageProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.ImageProto;

                    /**
                     * Creates a plain object from an ImageProto message. Also converts values to other types if specified.
                     * @param message ImageProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.ImageProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this ImageProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for ImageProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** Properties of a TransformProto. */
                interface ITransformProto {

                    /** TransformProto m00 */
                    m00?: (number|null);

                    /** TransformProto m10 */
                    m10?: (number|null);

                    /** TransformProto m01 */
                    m01?: (number|null);

                    /** TransformProto m11 */
                    m11?: (number|null);

                    /** TransformProto m02 */
                    m02?: (number|null);

                    /** TransformProto m12 */
                    m12?: (number|null);
                }

                /** Represents a TransformProto. */
                class TransformProto implements ITransformProto {

                    /**
                     * Constructs a new TransformProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.ITransformProto);

                    /** TransformProto m00. */
                    public m00: number;

                    /** TransformProto m10. */
                    public m10: number;

                    /** TransformProto m01. */
                    public m01: number;

                    /** TransformProto m11. */
                    public m11: number;

                    /** TransformProto m02. */
                    public m02: number;

                    /** TransformProto m12. */
                    public m12: number;

                    /**
                     * Decodes a TransformProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns TransformProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.TransformProto;

                    /**
                     * Creates a TransformProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns TransformProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.TransformProto;

                    /**
                     * Creates a plain object from a TransformProto message. Also converts values to other types if specified.
                     * @param message TransformProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.TransformProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this TransformProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for TransformProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** Properties of a CombinedProto. */
                interface ICombinedProto {

                    /** CombinedProto ids */
                    ids?: (number[]|null);
                }

                /** Represents a CombinedProto. */
                class CombinedProto implements ICombinedProto {

                    /**
                     * Constructs a new CombinedProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.ICombinedProto);

                    /** CombinedProto ids. */
                    public ids: number[];

                    /**
                     * Decodes a CombinedProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns CombinedProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.CombinedProto;

                    /**
                     * Creates a CombinedProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns CombinedProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.CombinedProto;

                    /**
                     * Creates a plain object from a CombinedProto message. Also converts values to other types if specified.
                     * @param message CombinedProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.CombinedProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this CombinedProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for CombinedProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** Properties of a GlyphProto. */
                interface IGlyphProto {

                    /** GlyphProto data */
                    data?: (Uint8Array|null);
                }

                /** Represents a GlyphProto. */
                class GlyphProto implements IGlyphProto {

                    /**
                     * Constructs a new GlyphProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IGlyphProto);

                    /** GlyphProto data. */
                    public data: Uint8Array;

                    /**
                     * Decodes a GlyphProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns GlyphProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.GlyphProto;

                    /**
                     * Creates a GlyphProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns GlyphProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.GlyphProto;

                    /**
                     * Creates a plain object from a GlyphProto message. Also converts values to other types if specified.
                     * @param message GlyphProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.GlyphProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this GlyphProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for GlyphProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** Properties of a RectangleProto. */
                interface IRectangleProto {

                    /** RectangleProto x */
                    x: number;

                    /** RectangleProto y */
                    y: number;

                    /** RectangleProto w */
                    w: number;

                    /** RectangleProto h */
                    h: number;
                }

                /** Represents a RectangleProto. */
                class RectangleProto implements IRectangleProto {

                    /**
                     * Constructs a new RectangleProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IRectangleProto);

                    /** RectangleProto x. */
                    public x: number;

                    /** RectangleProto y. */
                    public y: number;

                    /** RectangleProto w. */
                    public w: number;

                    /** RectangleProto h. */
                    public h: number;

                    /**
                     * Decodes a RectangleProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns RectangleProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.RectangleProto;

                    /**
                     * Creates a RectangleProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns RectangleProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.RectangleProto;

                    /**
                     * Creates a plain object from a RectangleProto message. Also converts values to other types if specified.
                     * @param message RectangleProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.RectangleProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this RectangleProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for RectangleProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** Properties of an EllipseProto. */
                interface IEllipseProto {

                    /** EllipseProto x */
                    x: number;

                    /** EllipseProto y */
                    y: number;

                    /** EllipseProto w */
                    w: number;

                    /** EllipseProto h */
                    h: number;
                }

                /** Represents an EllipseProto. */
                class EllipseProto implements IEllipseProto {

                    /**
                     * Constructs a new EllipseProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IEllipseProto);

                    /** EllipseProto x. */
                    public x: number;

                    /** EllipseProto y. */
                    public y: number;

                    /** EllipseProto w. */
                    public w: number;

                    /** EllipseProto h. */
                    public h: number;

                    /**
                     * Decodes an EllipseProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns EllipseProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.EllipseProto;

                    /**
                     * Creates an EllipseProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns EllipseProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.EllipseProto;

                    /**
                     * Creates a plain object from an EllipseProto message. Also converts values to other types if specified.
                     * @param message EllipseProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.EllipseProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this EllipseProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for EllipseProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** Properties of a RoundRectangleProto. */
                interface IRoundRectangleProto {

                    /** RoundRectangleProto x */
                    x: number;

                    /** RoundRectangleProto y */
                    y: number;

                    /** RoundRectangleProto w */
                    w: number;

                    /** RoundRectangleProto h */
                    h: number;

                    /** RoundRectangleProto arcW */
                    arcW?: (number|null);

                    /** RoundRectangleProto arcH */
                    arcH?: (number|null);
                }

                /** Represents a RoundRectangleProto. */
                class RoundRectangleProto implements IRoundRectangleProto {

                    /**
                     * Constructs a new RoundRectangleProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IRoundRectangleProto);

                    /** RoundRectangleProto x. */
                    public x: number;

                    /** RoundRectangleProto y. */
                    public y: number;

                    /** RoundRectangleProto w. */
                    public w: number;

                    /** RoundRectangleProto h. */
                    public h: number;

                    /** RoundRectangleProto arcW. */
                    public arcW: number;

                    /** RoundRectangleProto arcH. */
                    public arcH: number;

                    /**
                     * Decodes a RoundRectangleProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns RoundRectangleProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.RoundRectangleProto;

                    /**
                     * Creates a RoundRectangleProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns RoundRectangleProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.RoundRectangleProto;

                    /**
                     * Creates a plain object from a RoundRectangleProto message. Also converts values to other types if specified.
                     * @param message RoundRectangleProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.RoundRectangleProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this RoundRectangleProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for RoundRectangleProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** Properties of an ArcProto. */
                interface IArcProto {

                    /** ArcProto x */
                    x: number;

                    /** ArcProto y */
                    y: number;

                    /** ArcProto w */
                    w: number;

                    /** ArcProto h */
                    h: number;

                    /** ArcProto start */
                    start?: (number|null);

                    /** ArcProto extent */
                    extent?: (number|null);

                    /** ArcProto type */
                    type?: (org.webswing.directdraw.proto.ArcProto.ArcTypeProto|null);
                }

                /** Represents an ArcProto. */
                class ArcProto implements IArcProto {

                    /**
                     * Constructs a new ArcProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IArcProto);

                    /** ArcProto x. */
                    public x: number;

                    /** ArcProto y. */
                    public y: number;

                    /** ArcProto w. */
                    public w: number;

                    /** ArcProto h. */
                    public h: number;

                    /** ArcProto start. */
                    public start: number;

                    /** ArcProto extent. */
                    public extent: number;

                    /** ArcProto type. */
                    public type: org.webswing.directdraw.proto.ArcProto.ArcTypeProto;

                    /**
                     * Decodes an ArcProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns ArcProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.ArcProto;

                    /**
                     * Creates an ArcProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns ArcProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.ArcProto;

                    /**
                     * Creates a plain object from an ArcProto message. Also converts values to other types if specified.
                     * @param message ArcProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.ArcProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this ArcProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for ArcProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                namespace ArcProto {

                    /** ArcTypeProto enum. */
                    enum ArcTypeProto {
                        OPEN = 0,
                        CHORD = 1,
                        PIE = 2
                    }
                }

                /** Properties of a PathProto. */
                interface IPathProto {

                    /** PathProto windingOdd */
                    windingOdd: boolean;

                    /** PathProto type */
                    type?: (org.webswing.directdraw.proto.PathProto.SegmentTypeProto[]|null);

                    /** PathProto points */
                    points?: (number[]|null);
                }

                /** Represents a PathProto. */
                class PathProto implements IPathProto {

                    /**
                     * Constructs a new PathProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IPathProto);

                    /** PathProto windingOdd. */
                    public windingOdd: boolean;

                    /** PathProto type. */
                    public type: org.webswing.directdraw.proto.PathProto.SegmentTypeProto[];

                    /** PathProto points. */
                    public points: number[];

                    /**
                     * Decodes a PathProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns PathProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.PathProto;

                    /**
                     * Creates a PathProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns PathProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.PathProto;

                    /**
                     * Creates a plain object from a PathProto message. Also converts values to other types if specified.
                     * @param message PathProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.PathProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this PathProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for PathProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                namespace PathProto {

                    /** SegmentTypeProto enum. */
                    enum SegmentTypeProto {
                        MOVE = 0,
                        LINE = 1,
                        QUAD = 2,
                        CUBIC = 3,
                        CLOSE = 4
                    }
                }

                /** Properties of a FontProto. */
                interface IFontProto {

                    /** FontProto family */
                    family: string;

                    /** FontProto style */
                    style?: (org.webswing.directdraw.proto.FontProto.StyleProto|null);

                    /** FontProto size */
                    size?: (number|null);

                    /** FontProto transform */
                    transform?: (org.webswing.directdraw.proto.ITransformProto|null);

                    /** FontProto fontProvided */
                    fontProvided?: (boolean|null);
                }

                /** Represents a FontProto. */
                class FontProto implements IFontProto {

                    /**
                     * Constructs a new FontProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IFontProto);

                    /** FontProto family. */
                    public family: string;

                    /** FontProto style. */
                    public style: org.webswing.directdraw.proto.FontProto.StyleProto;

                    /** FontProto size. */
                    public size: number;

                    /** FontProto transform. */
                    public transform?: (org.webswing.directdraw.proto.ITransformProto|null);

                    /** FontProto fontProvided. */
                    public fontProvided: boolean;

                    /**
                     * Decodes a FontProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns FontProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.FontProto;

                    /**
                     * Creates a FontProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns FontProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.FontProto;

                    /**
                     * Creates a plain object from a FontProto message. Also converts values to other types if specified.
                     * @param message FontProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.FontProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this FontProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for FontProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                namespace FontProto {

                    /** StyleProto enum. */
                    enum StyleProto {
                        NORMAL = 0,
                        OBLIQUE = 1,
                        ITALIC = 2,
                        BOLDANDITALIC = 3
                    }
                }

                /** Properties of a StrokeProto. */
                interface IStrokeProto {

                    /** StrokeProto width */
                    width: number;

                    /** StrokeProto miterLimit */
                    miterLimit?: (number|null);

                    /** StrokeProto join */
                    join?: (org.webswing.directdraw.proto.StrokeProto.StrokeJoinProto|null);

                    /** StrokeProto cap */
                    cap?: (org.webswing.directdraw.proto.StrokeProto.StrokeCapProto|null);

                    /** StrokeProto dash */
                    dash?: (number[]|null);

                    /** StrokeProto dashOffset */
                    dashOffset?: (number|null);
                }

                /** Represents a StrokeProto. */
                class StrokeProto implements IStrokeProto {

                    /**
                     * Constructs a new StrokeProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IStrokeProto);

                    /** StrokeProto width. */
                    public width: number;

                    /** StrokeProto miterLimit. */
                    public miterLimit: number;

                    /** StrokeProto join. */
                    public join: org.webswing.directdraw.proto.StrokeProto.StrokeJoinProto;

                    /** StrokeProto cap. */
                    public cap: org.webswing.directdraw.proto.StrokeProto.StrokeCapProto;

                    /** StrokeProto dash. */
                    public dash: number[];

                    /** StrokeProto dashOffset. */
                    public dashOffset: number;

                    /**
                     * Decodes a StrokeProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns StrokeProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.StrokeProto;

                    /**
                     * Creates a StrokeProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns StrokeProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.StrokeProto;

                    /**
                     * Creates a plain object from a StrokeProto message. Also converts values to other types if specified.
                     * @param message StrokeProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.StrokeProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this StrokeProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for StrokeProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                namespace StrokeProto {

                    /** StrokeJoinProto enum. */
                    enum StrokeJoinProto {
                        JOIN_MITER = 0,
                        JOIN_ROUND = 1,
                        JOIN_BEVEL = 2
                    }

                    /** StrokeCapProto enum. */
                    enum StrokeCapProto {
                        CAP_BUTT = 0,
                        CAP_ROUND = 1,
                        CAP_SQUARE = 2
                    }
                }

                /** Properties of a LinearGradientProto. */
                interface ILinearGradientProto {

                    /** LinearGradientProto xStart */
                    xStart: number;

                    /** LinearGradientProto yStart */
                    yStart: number;

                    /** LinearGradientProto xEnd */
                    xEnd: number;

                    /** LinearGradientProto yEnd */
                    yEnd: number;

                    /** LinearGradientProto colors */
                    colors?: (number[]|null);

                    /** LinearGradientProto fractions */
                    fractions?: (number[]|null);

                    /** LinearGradientProto repeat */
                    repeat: org.webswing.directdraw.proto.CyclicMethodProto;
                }

                /** Represents a LinearGradientProto. */
                class LinearGradientProto implements ILinearGradientProto {

                    /**
                     * Constructs a new LinearGradientProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.ILinearGradientProto);

                    /** LinearGradientProto xStart. */
                    public xStart: number;

                    /** LinearGradientProto yStart. */
                    public yStart: number;

                    /** LinearGradientProto xEnd. */
                    public xEnd: number;

                    /** LinearGradientProto yEnd. */
                    public yEnd: number;

                    /** LinearGradientProto colors. */
                    public colors: number[];

                    /** LinearGradientProto fractions. */
                    public fractions: number[];

                    /** LinearGradientProto repeat. */
                    public repeat: org.webswing.directdraw.proto.CyclicMethodProto;

                    /**
                     * Decodes a LinearGradientProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns LinearGradientProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.LinearGradientProto;

                    /**
                     * Creates a LinearGradientProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns LinearGradientProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.LinearGradientProto;

                    /**
                     * Creates a plain object from a LinearGradientProto message. Also converts values to other types if specified.
                     * @param message LinearGradientProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.LinearGradientProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this LinearGradientProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for LinearGradientProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** Properties of a RadialGradientProto. */
                interface IRadialGradientProto {

                    /** RadialGradientProto xCenter */
                    xCenter: number;

                    /** RadialGradientProto yCenter */
                    yCenter: number;

                    /** RadialGradientProto xFocus */
                    xFocus: number;

                    /** RadialGradientProto yFocus */
                    yFocus: number;

                    /** RadialGradientProto radius */
                    radius: number;

                    /** RadialGradientProto colors */
                    colors?: (number[]|null);

                    /** RadialGradientProto fractions */
                    fractions?: (number[]|null);

                    /** RadialGradientProto repeat */
                    repeat: org.webswing.directdraw.proto.CyclicMethodProto;
                }

                /** Represents a RadialGradientProto. */
                class RadialGradientProto implements IRadialGradientProto {

                    /**
                     * Constructs a new RadialGradientProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IRadialGradientProto);

                    /** RadialGradientProto xCenter. */
                    public xCenter: number;

                    /** RadialGradientProto yCenter. */
                    public yCenter: number;

                    /** RadialGradientProto xFocus. */
                    public xFocus: number;

                    /** RadialGradientProto yFocus. */
                    public yFocus: number;

                    /** RadialGradientProto radius. */
                    public radius: number;

                    /** RadialGradientProto colors. */
                    public colors: number[];

                    /** RadialGradientProto fractions. */
                    public fractions: number[];

                    /** RadialGradientProto repeat. */
                    public repeat: org.webswing.directdraw.proto.CyclicMethodProto;

                    /**
                     * Decodes a RadialGradientProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns RadialGradientProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.RadialGradientProto;

                    /**
                     * Creates a RadialGradientProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns RadialGradientProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.RadialGradientProto;

                    /**
                     * Creates a plain object from a RadialGradientProto message. Also converts values to other types if specified.
                     * @param message RadialGradientProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.RadialGradientProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this RadialGradientProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for RadialGradientProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** CyclicMethodProto enum. */
                enum CyclicMethodProto {
                    NO_CYCLE = 0,
                    REFLECT = 1,
                    REPEAT = 2
                }

                /** Properties of a PointsProto. */
                interface IPointsProto {

                    /** PointsProto points */
                    points?: (number[]|null);
                }

                /** Represents a PointsProto. */
                class PointsProto implements IPointsProto {

                    /**
                     * Constructs a new PointsProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.IPointsProto);

                    /** PointsProto points. */
                    public points: number[];

                    /**
                     * Decodes a PointsProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns PointsProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.PointsProto;

                    /**
                     * Creates a PointsProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns PointsProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.PointsProto;

                    /**
                     * Creates a plain object from a PointsProto message. Also converts values to other types if specified.
                     * @param message PointsProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.PointsProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this PointsProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for PointsProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                /** Properties of a CompositeProto. */
                interface ICompositeProto {

                    /** CompositeProto type */
                    type: org.webswing.directdraw.proto.CompositeProto.CompositeTypeProto;

                    /** CompositeProto alpha */
                    alpha?: (number|null);

                    /** CompositeProto color */
                    color?: (number|null);
                }

                /** Represents a CompositeProto. */
                class CompositeProto implements ICompositeProto {

                    /**
                     * Constructs a new CompositeProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.ICompositeProto);

                    /** CompositeProto type. */
                    public type: org.webswing.directdraw.proto.CompositeProto.CompositeTypeProto;

                    /** CompositeProto alpha. */
                    public alpha: number;

                    /** CompositeProto color. */
                    public color: number;

                    /**
                     * Decodes a CompositeProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns CompositeProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.CompositeProto;

                    /**
                     * Creates a CompositeProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns CompositeProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.CompositeProto;

                    /**
                     * Creates a plain object from a CompositeProto message. Also converts values to other types if specified.
                     * @param message CompositeProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.CompositeProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this CompositeProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for CompositeProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }

                namespace CompositeProto {

                    /** CompositeTypeProto enum. */
                    enum CompositeTypeProto {
                        CLEAR = 1,
                        SRC = 2,
                        DST = 9,
                        SRC_OVER = 3,
                        DST_OVER = 4,
                        SRC_IN = 5,
                        DST_IN = 6,
                        SRC_OUT = 7,
                        DST_OUT = 8,
                        SRC_ATOP = 10,
                        DST_ATOP = 11,
                        XOR = 12,
                        XOR_MODE = 13
                    }
                }

                /** Properties of a TextureProto. */
                interface ITextureProto {

                    /** TextureProto image */
                    image: org.webswing.directdraw.proto.IImageProto;

                    /** TextureProto anchor */
                    anchor: org.webswing.directdraw.proto.IRectangleProto;
                }

                /** Represents a TextureProto. */
                class TextureProto implements ITextureProto {

                    /**
                     * Constructs a new TextureProto.
                     * @param [properties] Properties to set
                     */
                    constructor(properties?: org.webswing.directdraw.proto.ITextureProto);

                    /** TextureProto image. */
                    public image: org.webswing.directdraw.proto.IImageProto;

                    /** TextureProto anchor. */
                    public anchor: org.webswing.directdraw.proto.IRectangleProto;

                    /**
                     * Decodes a TextureProto message from the specified reader or buffer.
                     * @param reader Reader or buffer to decode from
                     * @param [length] Message length if known beforehand
                     * @returns TextureProto
                     * @throws {Error} If the payload is not a reader or valid buffer
                     * @throws {$protobuf.util.ProtocolError} If required fields are missing
                     */
                    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): org.webswing.directdraw.proto.TextureProto;

                    /**
                     * Creates a TextureProto message from a plain object. Also converts values to their respective internal types.
                     * @param object Plain object
                     * @returns TextureProto
                     */
                    public static fromObject(object: { [k: string]: any }): org.webswing.directdraw.proto.TextureProto;

                    /**
                     * Creates a plain object from a TextureProto message. Also converts values to other types if specified.
                     * @param message TextureProto
                     * @param [options] Conversion options
                     * @returns Plain object
                     */
                    public static toObject(message: org.webswing.directdraw.proto.TextureProto, options?: $protobuf.IConversionOptions): { [k: string]: any };

                    /**
                     * Converts this TextureProto to JSON.
                     * @returns JSON object
                     */
                    public toJSON(): { [k: string]: any };

                    /**
                     * Gets the default type url for TextureProto
                     * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                     * @returns The default type url
                     */
                    public static getTypeUrl(typeUrlPrefix?: string): string;
                }
            }
        }
    }
}
