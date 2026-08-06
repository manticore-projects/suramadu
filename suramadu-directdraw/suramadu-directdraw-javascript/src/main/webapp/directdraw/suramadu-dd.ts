import {org} from "../proto/dd";
interface WebImageProto extends org.webswing.directdraw.proto.WebImageProto {}
interface IDrawConstantProto extends org.webswing.directdraw.proto.IDrawConstantProto {}
interface IImageProto extends org.webswing.directdraw.proto.IImageProto {}
interface IGlyphProto extends org.webswing.directdraw.proto.IGlyphProto {}
interface IFontFaceProto extends org.webswing.directdraw.proto.IFontFaceProto {}
interface IDrawInstructionProto extends org.webswing.directdraw.proto.IDrawInstructionProto {}
interface IRectangleProto extends org.webswing.directdraw.proto.IRectangleProto {}
interface ITransformProto extends org.webswing.directdraw.proto.ITransformProto {}
interface IRadialGradientProto extends org.webswing.directdraw.proto.IRadialGradientProto {}
interface ILinearGradientProto extends org.webswing.directdraw.proto.ILinearGradientProto {}
interface IEllipseProto extends org.webswing.directdraw.proto.IEllipseProto {}
interface IRoundRectangleProto extends org.webswing.directdraw.proto.IRoundRectangleProto {}
interface IArcProto extends org.webswing.directdraw.proto.IArcProto {}
const InstructionProto = org.webswing.directdraw.proto.DrawInstructionProto.InstructionProto;
const CompositeTypeProto = org.webswing.directdraw.proto.CompositeProto.CompositeTypeProto ;
const SegmentTypeProto = org.webswing.directdraw.proto.PathProto.SegmentTypeProto ;
const StyleProto = org.webswing.directdraw.proto.FontProto.StyleProto ;
const StrokeCapProto = org.webswing.directdraw.proto.StrokeProto.StrokeCapProto ;
const StrokeJoinProto = org.webswing.directdraw.proto.StrokeProto.StrokeJoinProto ;
const ArcTypeProto = org.webswing.directdraw.proto.ArcProto.ArcTypeProto ;
const CyclicMethodProto = org.webswing.directdraw.proto.CyclicMethodProto ;



interface IDirectDrawOptions {
    logDebug?: boolean,
    logTrace?: boolean,
    ieVersion?: boolean | number,
    onErrorMessage?: (message: Error) => void,
    dpr?: number,
    constantPoolCache?: IDrawConstantProto[]
}


interface IDirectDrawConfig {
    logDebug: boolean,
    logTrace: boolean,
    ieVersion: boolean | number,
    onErrorMessage: (message: Error) => void,
    dpr: number
}

export class DirectDraw {

    public static applyXorModeComposition(dest: ICanvasCtx, src: XorModeCanvasCtx, xor: IColor, bbox: null | IRectangle, logDebug: boolean) {
        if (bbox == null || bbox.w === 0 || bbox.h === 0) {
            return;
        }
        const start = new Date().getTime();
        const destData = dest.getImageData(bbox.x, bbox.y, bbox.w, bbox.h);
        const srcData = src.getImageData(bbox.x, bbox.y, bbox.w, bbox.h);
        for (let i = 0; i < destData.data.length / 4; i++) {
            if (srcData.data[4 * i + 3] > 0) {
                destData.data[4 * i] = srcData.data[4 * i] ^ xor.r ^ destData.data[4 * i];    // RED (0-255)
                destData.data[4 * i + 1] = srcData.data[4 * i + 1] ^ xor.g ^ destData.data[4 * i + 1];    // GREEN (0-255)
                destData.data[4 * i + 2] = srcData.data[4 * i + 2] ^ xor.b ^ destData.data[4 * i + 2];    // BLUE (0-255)
                destData.data[4 * i + 3] = destData.data[4 * i + 3];  // APLHA (0-255)
            }
        }
        dest.putImageData(destData, bbox.x, bbox.y);
        if (logDebug) {
            console.warn('DirectDraw DEBUG xormode - composition pixelsize:' + (bbox.w * bbox.h) + ' duration(ms): ' + (new Date().getTime() - start));
        }
    }

    public static toImageLoaded(image: Uint8Array | HTMLImageElement): HTMLImageElement {
        return image as HTMLImageElement;
    }

    public static toTx(t:ITransformProto):Tx{
        return [t.m00!, t.m10!, t.m01!, t.m11!, t.m02!, t.m12!]
    }

    // calculates how many times vector (dx, dy) will repeat from (x0, y0) until it touches a straight line
    // which goes through (x1, y1) and perpendicular to the vector
    public static calculateTimes(x0:number, y0:number, dx:number, dy:number, x1:number, y1:number) {
        return ((x1 - x0) * dx + (y1 - y0) * dy) / (dx * dx + dy * dy);
    }

    // fix gradient focus point as java does
    public static fixFocusPoint(gradient:IRadialGradientProto) {
        const dx = gradient.xFocus - gradient.xCenter;
        const dy = gradient.yFocus - gradient.yCenter;
        if (dx === 0 && dy === 0) {
            return;
        }
        const scaleBack = 0.99;
        const radiusSq = gradient.radius * gradient.radius;
        const distSq = (dx * dx) + (dy * dy);
        // test if distance from focus to center is greater than the radius
        if (distSq > radiusSq * scaleBack) {
            // clamp focus to radius
            const scale = Math.sqrt(radiusSq * scaleBack / distSq);
            // modify source object to skip fixes later
            gradient.xFocus = gradient.xCenter + dx * scale;
            gradient.yFocus = gradient.yCenter + dy * scale;
        }
    }

    public static getDistance(x0:number, y0:number, x1:number, y1:number) {
        return Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));
    }

    public static getEllipseCoords(pts:number[], elli:IEllipseProto, bias:IBias) {
        pts[0] = elli.x + bias.x + pts[0] * elli.w;
        pts[1] = elli.y + bias.y + pts[1] * elli.h;
        pts[2] = elli.x + bias.x + pts[2] * elli.w;
        pts[3] = elli.y + bias.y + pts[3] * elli.h;
        pts[4] = elli.x + bias.x + pts[4] * elli.w;
        pts[5] = elli.y + bias.y + pts[5] * elli.h;
        return pts;
    }

    public static getRRCoords(pts:number[], rr:IRoundRectangleProto, bias:IBias) {
        const coords = [];
        let nc = 0;
        for (let i = 0; i < pts.length; i += 4) {
            coords[nc++] = rr.x + bias.x + pts[i + 0] * rr.w + pts[i + 1] * Math.abs(rr.arcW!);
            coords[nc++] = rr.y + bias.y + pts[i + 2] * rr.h + pts[i + 3] * Math.abs(rr.arcH!);
        }
        return coords;
    }

    public static calculateBias(lineWidth: number, dpr: number, biased?: boolean, tx?: Tx) {
        if (!biased) {
            return {x: 0, y: 0};
        } else {
            const transform = tx ? tx : DirectDraw.identityTransform(dpr);
            const dx = lineWidth * transform[0] / dpr % 2;
            const dy = lineWidth * transform[3] / dpr % 2;
            const xbias = (dx > 0.0001 && dx < 1.9999) ? (0.5 / transform[0] * dpr) : 0
            const ybias = (dy > 0.0001 && dy < 1.9999) ? (0.5 / transform[3] * dpr) : 0
            return {
                x: xbias,
                y: ybias
            }
        }
    }

    public static parseColor(rgba: number) {
        const samples = DirectDraw.parseColorSamples(rgba);
        return 'rgba(' + samples.r + ',' + samples.g + ',' + samples.b + ',' + (samples.a / 255) + ')';
    }

    public static parseColorSamples(rgba: number): IColor {
        const mask = 0x000000FF;
        return {
            r: ((rgba >>> 24) & mask),
            g: ((rgba >>> 16) & mask),
            b: ((rgba) >>> 8 & mask),
            a: (rgba & mask)
        };
    }

    public static isFontAvailable(fontName:string) {
        const canvas = document.createElement("canvas");
        const context = canvas.getContext("2d")!;
        const text = "abcdefghijklmnopqrstuvwxyz0123456789";
        context.font = "72px monospace";
        const baselineSize = context.measureText(text).width;
        context.font = "72px '" + fontName + "', monospace";
        const newSize = context.measureText(text).width;
        if (newSize === baselineSize) {
            return false;
        } else {
            return true;
        }
    }

    public static getFontFaceData(name:string, font:Uint8Array, style:string) {
        let fontFaceCss = "@font-face {";
        fontFaceCss += "font-family: '" + name + "';";
        fontFaceCss += "src: url(data:font/truetype;base64," + DirectDraw.toBase64(font) + ");";
        if (style != null) {
            fontFaceCss += "font-style: " + style + ";";
        }
        fontFaceCss += "}";
        return fontFaceCss
    }

    public static toBase64(data:Uint8Array) {
        let binary = '';
        const bytes = data;

        for (let i = 0, l = bytes.byteLength; i < l; i++) {
            binary += String.fromCharCode(bytes[i]);
        }
        return window.btoa(binary);
    }

    public static getImageData(data: Uint8Array) {
        const b64 = DirectDraw.toBase64(data);
        return b64 ? "data:image/png;base64," + b64 : null;
    }

    public static fillRule(constant: IDrawConstantProto) {
        if (constant.path != null) {
            return constant.path.windingOdd ? 'evenodd' : 'nonzero';
        }
        return 'nonzero';
    }

    public static resolveArgs(args: number[], cache: IDrawConstantProto[]) {
        const result = [];
        for (let i = 0; i < args.length; i++) {
            result[i] = cache[args[i]];
        }
        return result;
    }

    public static concatTransform(m: Tx, t: Tx, dpr: number): Tx {
        if (m == null) {
            m = DirectDraw.identityTransform(dpr);
        }
        const r0 = m[0] * t[0] + m[2] * t[1];
        const r1 = m[1] * t[0] + m[3] * t[1];
        const r2 = m[0] * t[2] + m[2] * t[3];
        const r3 = m[1] * t[2] + m[3] * t[3];
        const r4 = m[0] * t[4] + m[2] * t[5] + m[4];
        const r5 = m[1] * t[4] + m[3] * t[5] + m[5];
        return [r0, r1, r2, r3, r4, r5];
    }

    public static withDpr(m: Tx, dpr: number) {
        return DirectDraw.concatTransform(DirectDraw.identityTransform(dpr), m, dpr);
    }

    public static identityTransform(dpr: number): Tx {
        return [dpr, 0, 0, dpr, 0, 0]
    }

    public static transformPoint(x: number, y: number, t: Tx) {
        const xt = t[0] * x + t[2] * y + t[4];
        const yt = t[1] * x + t[3] * y + t[5];
        return {x: xt, y: yt};
    }

     public static toCanvasCtx(ctx2d:ICanvasCtx):ICanvasCtx{
        return ctx2d as ICanvasCtx;
     }
    private ctxId = Math.floor(Math.random() * 0x10000).toString(16)

    private config: IDirectDrawConfig = {
        logDebug: false,
        logTrace: false,
        ieVersion: false,
        onErrorMessage: (message) => console.error(message.stack),
        dpr: 1
    }

    private constantPoolCache: IDrawConstantProto[] = [];
    private fontsArray: string[] = [];
    private canvasBuffer:HTMLCanvasElement[] = [];
    private xorLayer:HTMLCanvasElement=document.createElement("canvas");

    constructor(c?: IDirectDrawOptions) {
        if (c) {
            this.config = {...this.config, ...c}

            if (c.constantPoolCache) {
                this.constantPoolCache = c.constantPoolCache
            }
        }
    }

    public draw64(value: string, targetCanvas?: HTMLCanvasElement): PromiseLike<HTMLCanvasElement> {
        const bin = Uint8Array.from(atob(value), c => c.charCodeAt(0));
        return this.drawBin(bin, targetCanvas);
    }

    public drawBin(data: Uint8Array, targetCanvas?: HTMLCanvasElement): PromiseLike<HTMLCanvasElement> {
        return this.drawWebImage(org.webswing.directdraw.proto.WebImageProto.decode(data), targetCanvas);
    }

    public drawProto(data: WebImageProto, targetCanvas?: HTMLCanvasElement): PromiseLike<HTMLCanvasElement> {
        return this.drawWebImage(data, targetCanvas);
    }

    public iprtGraphicsDispose(id: number, imageContext: IImageContext) {
        delete imageContext.graphicsStates[id];
    }

    public iprtGraphicsSwitch(ctx: ICanvasCtx, id: number, imageContext: IImageContext) {
        const graphicsStates = imageContext.graphicsStates;
        if (graphicsStates[id] != null) {
            this.setCtxState(graphicsStates[id], ctx);
        } else {
            console.warn("Graphics with id " + id + " not initialized!");
        }
        imageContext.currentStateId = id;
    }

    public setCtxState(graphicsState: IGraphicsState, ctx: ICanvasCtx) {
        if (graphicsState != null) {
            if (graphicsState.strokeArgs != null) {
                this.iprtSetStroke(ctx, graphicsState.strokeArgs);
            }
            if (graphicsState.paintArgs != null) {
                this.iprtSetPaint(ctx, graphicsState.paintArgs);
            }
            if (graphicsState.compositeArgs != null) {
                this.iprtSetComposite(ctx, graphicsState.compositeArgs);
            }
            if (graphicsState.fontArgs != null) {
                this.iprtSetFont(ctx, graphicsState.fontArgs);
            }
            if (graphicsState.transform != null) {
                const t = graphicsState.transform;
                ctx.setTransform(t[0], t[1], t[2], t[3], t[4], t[5]);
            } else {
                const iT = DirectDraw.identityTransform(this.config.dpr);
                ctx.setTransform(iT[0], iT[1], iT[2], iT[3], iT[4], iT[5]);
            }
        }
    }

    public iprtGraphicsCreate(ctx: ICanvasCtx, id: number, args: IDrawConstantProto[], imageContext: IImageContext) {
        const graphicsStates = imageContext.graphicsStates;
        if (graphicsStates[id] == null) {
            imageContext.currentStateId = id;
            args.shift();
            const transform = this.iprtSetTransform(ctx, args);
            args.shift();
            this.iprtSetStroke(ctx, args);
            const strokeArgs = args.slice(0, 1);
            args.shift();
            this.iprtSetComposite(ctx, args);
            const compositeArgs = args.slice(0, 1);
            args.shift();
            this.iprtSetPaint(ctx, args);
            const paintArgs = args.slice(0, 1);
            args.shift();
            const fontArgs = args;
            const fontTransform = this.iprtSetFont(ctx, args);

            graphicsStates[id] = {
                transform, strokeArgs, compositeArgs, paintArgs, fontArgs, fontTransform
            };
        } else {
            console.warn("Graphics with id " + id + " already exist!");
        }
    }

    public iprtDraw(ctx: ICanvasCtx, args: IDrawConstantProto[], transform: Tx) {
        ctx.save();
        if (this.path(ctx, args[1])) {
            ctx.clip(DirectDraw.fillRule(args[1]));
        }
        this.path(ctx, args[0], true, transform);
        ctx.stroke();
        ctx.restore();
    }

    public iprtFill(ctx: ICanvasCtx, args: IDrawConstantProto[]) {
        ctx.save();
        if (this.path(ctx, args[1])) {
            ctx.clip(DirectDraw.fillRule(args[1]));
        }
        this.path(ctx, args[0]);
        ctx.fill(DirectDraw.fillRule(args[0]));
        ctx.restore();
    }

    public iprtDrawImage(ctx: ICanvasCtx, args: IDrawConstantProto[]) {
        ctx.save();
        const image = DirectDraw.toImageLoaded(args[0].image!.data);
        const transform = args[1];
        const crop = args[2];
        const bgcolor = args[3];
        const clip = args[4];

        if (this.path(ctx, clip)) {
            ctx.clip(DirectDraw.fillRule(clip));
        }
        if (transform != null) {
            this.iprtTransform(ctx, [transform]);
        }
        if (bgcolor) {
            ctx.fillStyle = DirectDraw.parseColor(bgcolor.color && bgcolor.color.rgba ? bgcolor.color.rgba : 0);
            ctx.beginPath();
            if (crop == null) {
                ctx.rect(0, 0, image.width, image.height);
            } else {
                ctx.rect(0, 0, crop.rectangle!.w, crop.rectangle!.h);
            }
            ctx.fill();
        }
        if (crop == null) {
            ctx.drawImage(image, 0, 0);
        } else {
            const rect = crop.rectangle!;
            ctx.drawImage(image, rect.x, rect.y, rect.w, rect.h, 0, 0, rect.w, rect.h);
        }
        ctx.restore();
    }

    public iprtDrawWebImage(ctx:ICanvasCtx, args:IDrawConstantProto[], webImageData:Uint8Array) {
        const transform = args[0];
        const crop = args[1];
        const bgcolor = args[2];
        const clip = args[3];

        const buffer = this.canvasBuffer.pop();
        const webImage= org.webswing.directdraw.proto.WebImageProto.decode(webImageData);
        const dpr = this.config.dpr;
        if (buffer!==undefined && (buffer.width !== webImage.width * dpr || buffer.height !== webImage.height * dpr)) {
            buffer.width = webImage.width * dpr;
            buffer.height = webImage.height * dpr;
        }
        return this.drawWebImage(webImage, buffer!).then((imageCanvas)=> {
            ctx.save();
            if (this.path(ctx, clip)) {
                ctx.clip(DirectDraw.fillRule(clip));
            }
            if (transform != null) {
                this.iprtTransform(ctx, [transform]);
            }
            if (bgcolor != null) {
                ctx.fillStyle = DirectDraw.parseColor(bgcolor.color!.rgba);
                ctx.beginPath();
                if (crop == null) {
                    ctx.rect(0, 0, imageCanvas.width / dpr, imageCanvas.height / dpr);
                } else {
                    ctx.rect(0, 0, crop.rectangle!.w, crop.rectangle!.h);
                }
                ctx.fill();
            }
            if (crop == null) {
                ctx.drawImage(imageCanvas, 0, 0, imageCanvas.width, imageCanvas.height, 0, 0, imageCanvas.width / dpr, imageCanvas.height / dpr);
            } else {
                const rect = crop.rectangle!;
                ctx.drawImage(imageCanvas, rect.x * dpr, rect.y * dpr, rect.w * dpr, rect.h * dpr, 0, 0, rect.w, rect.h);
            }
            ctx.restore();

            imageCanvas.width = 0;// clear the buffer image for future reuse
            this.canvasBuffer.push(imageCanvas);
        });
    }

    public iprtDrawGlyphList(ctx:ICanvasCtx, args:IDrawConstantProto[]) {
        const combinedArgs = DirectDraw.resolveArgs(args[0].combined!.ids!, this.constantPoolCache);
        const size = combinedArgs[0].points!;
        const points = combinedArgs[1].points;
        const glyphs = combinedArgs.slice(2);
        const clip = args[1];
        ctx.save();
        if (this.path(ctx, clip)) {
            ctx.clip(DirectDraw.fillRule(clip));
        }
        if (glyphs.length > 0) {
            const buffer = document.createElement("canvas");
            buffer.width = size.points![2];
            buffer.height = size.points![3];
            const bufctx = buffer.getContext("2d")!;
            for (let i = 0; i < glyphs.length; i++) {
                if (glyphs[i].glyph!.data != null) {
                    const img = DirectDraw.toImageLoaded(glyphs[i].glyph!.data!);
                    const x = points!.points![i * 2];
                    const y = points!.points![i * 2 + 1];
                    bufctx.drawImage(img, 0, 0, img.width, img.height, x, y, img.width, img.height);
                }
            }
            bufctx.fillStyle = ctx.fillStyle;
            bufctx.globalCompositeOperation = 'source-in';
            bufctx.fillRect(0, 0, buffer.width, buffer.height);
            ctx.drawImage(buffer, 0, 0, buffer.width, buffer.height, size.points![0], size.points![1], buffer.width, buffer.height);
        }
        ctx.restore();
    }

    public iprtDrawString(ctx:ICanvasCtx, args:IDrawConstantProto[], fontTransform:ITransformProto|null) {
        const value = args[0].string!;
        const points = args[1].points!.points!;
        const x=points[0];
        const y=points[1];
        const clip = args[2];
        ctx.save();
        if (this.path(ctx, clip)) {
            ctx.clip(DirectDraw.fillRule(clip));
        }
        if (fontTransform != null) {
            const t = fontTransform;
            ctx.transform(t.m00!, t.m10!, t.m01!, t.m11!, t.m02! + x, t.m12! + y);
            ctx.fillText(value, 0, 0);
        } else {
            let currentX=x;
            for (let i = 0;i<value.length;i++){
                if(points[i+2]===0){
                    continue;
                }
                const c = this.getCharGroup(i,value,points);
                const canvasWidth = ctx.measureText(c).width;
                const scaleX = points[i+2] / canvasWidth;
                ctx.save();
                if(scaleX<=1) {
                    ctx.scale(scaleX, 1);
                    ctx.fillText(c, currentX / scaleX, y);
                }else{
                    ctx.fillText(c, currentX+((points[i+2] - canvasWidth)/2), y);
                }
                ctx.restore();
                currentX+=points[i+2];
            }
        }
        ctx.restore();
    }

    public getCharGroup(i:number, value:string, points:number[]){
        let c = value.charAt(i);
        let currentIndex = i+1;
        while(value.length>currentIndex && points[currentIndex+2]===0){
            c+=value.charAt(currentIndex);
            currentIndex++
        }
        return c;
    }


    public iprtSetFont(ctx:ICanvasCtx, args:IDrawConstantProto[]):ITransformProto|null {
        if (args[0] == null) {
            return null;
        }
        const font = args[0].font!;
        let style = '';
        switch (font.style) {
            case StyleProto.NORMAL:
                style = '';
                break;
            case StyleProto.OBLIQUE:
                style = 'bold';
                break;
            case StyleProto.ITALIC:
                style = 'italic';
                break;
            case StyleProto.BOLDANDITALIC:
                style = 'bold italic';
                break;
        }
        let fontFamily = font.family;
        if (font.family !== 'sans-serif' && font.family !== 'serif' && font.family !== 'monospace') {
            fontFamily = "\"" + (font.fontProvided ? '' : this.ctxId) + font.family + "\"";
        }
        ctx.font = style + " " + font.size + "px " + fontFamily;
        return font.transform!;
    }

    public iprtCopyArea(ctx:ICanvasCtx, args:IDrawConstantProto[]) {
        const p = args[0].points!.points!;
        const clip = args[1];
        const dpr = this.config.dpr;
        ctx.save();

        if (this.path(ctx, clip)) {
            ctx.clip(DirectDraw.fillRule(clip));
        }
        ctx.beginPath();
        ctx.setTransform(1, 0, 0, 1, 0, 0);
        ctx.rect(p[0] * dpr, p[1] * dpr, p[2] * dpr, p[3] * dpr);
        ctx.clip();
        ctx.translate(p[4] * dpr, p[5] * dpr);
        ctx.drawImage(ctx.canvas, 0, 0);
        ctx.restore();
    }

    public iprtTransform(ctx:ICanvasCtx, args:IDrawConstantProto[]): Tx {
        const t = args[0].transform!;
        ctx.transform(t.m00!, t.m10!, t.m01!, t.m11!, t.m02!, t.m12!);
        return [t.m00!, t.m10!, t.m01!, t.m11!, t.m02!, t.m12!];
    }

    public iprtSetTransform(ctx:ICanvasCtx, args:IDrawConstantProto[]):Tx {
        const t = args[0].transform!;
        const tx = DirectDraw.withDpr(DirectDraw.toTx(t), this.config.dpr);
        ctx.setTransform(tx[0], tx[1], tx[2], tx[3], tx[4], tx[5]);
        return tx;
    }

    public iprtSetStroke(ctx:ICanvasCtx, args:IDrawConstantProto[]) {
        const stroke = args[0].stroke!;
        ctx.lineWidth = stroke.width;
        ctx.miterLimit = stroke.miterLimit!;
        switch (stroke.cap) {
            case StrokeCapProto.CAP_BUTT:
                ctx.lineCap = "butt";
                break;
            case StrokeCapProto.CAP_ROUND:
                ctx.lineCap = "round";
                break;
            case StrokeCapProto.CAP_SQUARE:
                ctx.lineCap = "square";
                break;
        }
        switch (stroke.join) {
            case StrokeJoinProto.JOIN_MITER:
                ctx.lineJoin = "miter";
                break;
            case StrokeJoinProto.JOIN_ROUND:
                ctx.lineJoin = "round";
                break;
            case StrokeJoinProto.JOIN_BEVEL:
                ctx.lineJoin = "bevel";
                break;
        }
        if (stroke.dash != null) {
            if (ctx.setLineDash != null) {// ie10 does fails on dash
                ctx.setLineDash(stroke.dash);
                ctx.lineDashOffset = stroke.dashOffset!;
            }
        }
    }

    public iprtSetPaint(ctx:ICanvasCtx, args:IDrawConstantProto[]) {
        const constant = args[0];
        if (constant.color != null) {
            const color = DirectDraw.parseColor(constant.color.rgba);
            ctx.fillStyle = color;
            ctx.strokeStyle = color;
        } else if (constant.texture != null) {
            const anchor = constant.texture.anchor;
            const preloadedImage = DirectDraw.toImageLoaded(constant.texture.image.data);
            let ptrn;
            if (anchor.x === 0 && anchor.y === 0 && anchor.w === preloadedImage.width && anchor.h === preloadedImage.height) {
                ptrn = ctx.createPattern(preloadedImage, 'repeat');
            } else {
                const ptrnCanvas = document.createElement('canvas');
                const ax = anchor.x < 0 ? ((anchor.x % anchor.w) + anchor.w) : (anchor.x % anchor.w);
                const ay = anchor.y < 0 ? ((anchor.y % anchor.h) + anchor.h) : (anchor.y % anchor.h);
                ptrnCanvas.width = anchor.w;
                ptrnCanvas.height = anchor.h;
                const ptrnContext = ptrnCanvas.getContext("2d")!;
                ptrnContext.clearRect(0, 0, anchor.w, anchor.h);
                ptrnContext.fillStyle = ptrnContext.createPattern(preloadedImage, 'repeat')!;
                ptrnContext.setTransform(anchor.w / preloadedImage.width, 0, 0, anchor.h / preloadedImage.height, ax, ay);
                ptrnContext.fillRect(-ax * preloadedImage.width / anchor.w, -ay * preloadedImage.height / anchor.h, preloadedImage.width,
                    preloadedImage.height);
                ptrn = ctx.createPattern(ptrnCanvas, 'repeat');
            }
            ctx.fillStyle = ptrn!;
            ctx.strokeStyle = ptrn!;
        } else if (constant.linearGrad != null) {
            const gradient = this.iprtLinearGradient(ctx, constant.linearGrad);
            ctx.fillStyle = gradient;
            ctx.strokeStyle = gradient;
        } else if (constant.radialGrad != null) {
            const gradient = this.iprtRadialGradient(ctx, constant.radialGrad);
            ctx.fillStyle = gradient;
            ctx.strokeStyle = gradient;
        }
    }

    public iprtLinearGradient(ctx:ICanvasCtx, g:ILinearGradientProto) {
        const x0 = g.xStart;
        const y0 = g.yStart;
        const dx = g.xEnd - x0;
        const dy = g.yEnd - y0;
        // in case of cyclic gradient calculate repeat counts
        let repeatCount = 1;
        let increaseCount = repeatCount;
        let  decreaseCount = 0;
        if (g.repeat !== CyclicMethodProto.NO_CYCLE && (dx !== 0 || dy !== 0)) {
            // calculate how many times gradient will completely repeat in both directions until it touches canvas corners
            const c = ctx.canvas;
            const times = [DirectDraw.calculateTimes(x0, y0, dx, dy, 0, 0),
                DirectDraw.calculateTimes(x0, y0, dx, dy, c.width, 0),
                DirectDraw.calculateTimes(x0, y0, dx, dy, c.width, c.height),
                DirectDraw.calculateTimes(x0, y0, dx, dy, 0, c.height)];
            // increase count is maximum of all positive times rounded up
            increaseCount = Math.ceil(Math.max.apply(Math, times));
            // decrease count is maximum of all negative times rounded up (with inverted sign)
            decreaseCount = Math.ceil(-Math.min.apply(Math, times));
            repeatCount = increaseCount + decreaseCount;
        }
        const gradient = ctx.createLinearGradient(x0 - dx * decreaseCount, y0 - dy * decreaseCount, x0 + dx * increaseCount, y0 + dy * increaseCount);
        for (let rep = -decreaseCount, offset = 0; rep < increaseCount; rep++, offset++) {
            if (g.repeat !== CyclicMethodProto.REFLECT || rep % 2 === 0) {
                for (let i = 0; i < g.colors!.length; i++) {
                    gradient.addColorStop((offset + g.fractions![i]) / repeatCount, DirectDraw.parseColor(g.colors![i]));
                }
            } else {
                // reflect colors
                for (let i = g.colors!.length - 1; i >= 0; i--) {
                    gradient.addColorStop((offset + (1 - g.fractions![i])) / repeatCount, DirectDraw.parseColor(g.colors![i]));
                }
            }
        }
        return gradient;
    }

    public iprtRadialGradient(ctx:ICanvasCtx, g:IRadialGradientProto) {
        DirectDraw.fixFocusPoint(g);
        const fX = g.xFocus;
        const fY = g.yFocus;
        const dx = g.xCenter - fX;
        const dy = g.yCenter - fY;
        const r = g.radius;
        // in case of cyclic gradient calculate repeat counts
        let repeatCount = 1;
        if (g.repeat !== CyclicMethodProto.NO_CYCLE) {
            if (dx === 0 && dy === 0) {
                // calculate how many times gradient will completely repeat in both directions until it touches canvas corners
                const c = ctx.canvas;
                const times = [DirectDraw.getDistance(fX, fY, 0, 0) / r,
                    DirectDraw. getDistance(fX, fY, c.width, 0) / r,
                    DirectDraw. getDistance(fX, fY, c.width, c.height) / r,
                    DirectDraw.  getDistance(fX, fY, 0, c.height) / r];
                repeatCount = Math.ceil(Math.max.apply(Math, times));
            } else {
                const distance = Math.sqrt(dx * dx + dy * dy);
                // calculate vector which goes from focus point through center to the circle bound
                const vdX = dx + r * dx / distance;
                const vdY = dy + r * dy / distance;
                // and in opposite direction
                const ovdX = dx - r * dx / distance;
                const ovdY = dy - r * dy / distance;
                // calculate how many times gradient will completely repeat in both directions until it touches canvas corners
                const c = ctx.canvas;
                const times = [DirectDraw.calculateTimes(fX, fY, vdX, vdY, 0, 0),
                    DirectDraw. calculateTimes(fX, fY, vdX, vdY, c.width, 0),
                    DirectDraw.calculateTimes(fX, fY, vdX, vdY, c.width, c.height),
                    DirectDraw.calculateTimes(fX, fY, vdX, vdY, 0, c.height),
                    DirectDraw.calculateTimes(fX, fY, ovdX, ovdY, 0, 0),
                    DirectDraw.calculateTimes(fX, fY, ovdX, ovdY, c.width, 0),
                    DirectDraw. calculateTimes(fX, fY, ovdX, ovdY, c.width, c.height),
                    DirectDraw. calculateTimes(fX, fY, ovdX, ovdY, 0, c.height)];
                repeatCount = Math.ceil(Math.max.apply(Math, times));
            }
        }
        // in case of repeat focus stays in the same place, radius and distance between focus and center are multiplied
        const gradient = ctx.createRadialGradient(fX, fY, 0, fX + repeatCount * dx, fY + repeatCount * dy, r * repeatCount);
        for (let rep = 0; rep < repeatCount; rep++) {
            if (g.repeat !== CyclicMethodProto.REFLECT || rep % 2 === 0) {
                for (let i = 0; i < g.colors!.length; i++) {
                    gradient.addColorStop((rep + g.fractions![i]) / repeatCount, DirectDraw.parseColor(g.colors![i]));
                }
            } else {
                // reflect colors
                for (let i = g.colors!.length - 1; i >= 0; i--) {
                    gradient.addColorStop((rep + (1 - g.fractions![i])) / repeatCount,  DirectDraw.parseColor(g.colors![i]));
                }
            }
        }
        return gradient;
    }

    public iprtSetComposite(ctx:ICanvasCtx, args:IDrawConstantProto[]) {
        const composite = args[0].composite;
        if (composite != null) {
            ctx.globalAlpha = composite.alpha!;
            switch (composite.type) {
                case CompositeTypeProto.CLEAR:
                    ctx.globalCompositeOperation = "destination-out";
                    ctx.globalAlpha = 1;
                    break;
                case CompositeTypeProto.SRC:
                    ctx.globalCompositeOperation = "source-over";
                    break;
                case CompositeTypeProto.DST:
                    ctx.globalCompositeOperation = "destination-over";
                    ctx.globalAlpha = 0;
                    break;
                case CompositeTypeProto.SRC_OVER:
                    ctx.globalCompositeOperation = "source-over";
                    break;
                case CompositeTypeProto.DST_OVER:
                    ctx.globalCompositeOperation = "destination-over";
                    break;
                case CompositeTypeProto.SRC_IN:
                    ctx.globalCompositeOperation = "source-in";
                    break;
                case CompositeTypeProto.DST_IN:
                    ctx.globalCompositeOperation = "destination-in";
                    break;
                case CompositeTypeProto.SRC_OUT:
                    ctx.globalCompositeOperation = "source-out";
                    break;
                case CompositeTypeProto.DST_OUT:
                    ctx.globalCompositeOperation = "destination-out";
                    break;
                case CompositeTypeProto.SRC_ATOP:
                    ctx.globalCompositeOperation = "source-atop";
                    break;
                case CompositeTypeProto.DST_ATOP:
                    ctx.globalCompositeOperation = "destination-atop";
                    break;
                case CompositeTypeProto.XOR:
                    ctx.globalCompositeOperation = "xor";
                    break;
                case CompositeTypeProto.XOR_MODE:// handled with custom pixel processing (no effect)
                    ctx.globalCompositeOperation = "source-over";
                    break;
            }
        }
    }

    public path(ctx: ICanvasCtx, arg: IDrawConstantProto, biased?: boolean, transform?: Tx) {
        if (arg == null) {
            return false;
        }

        const bias = DirectDraw.calculateBias(ctx.lineWidth, this.config.dpr, biased, transform);

        if (arg.rectangle != null) {
            ctx.beginPath();
            this.pathRectangle(ctx, arg.rectangle, bias);
            return true;
        }

        if (arg.roundRectangle != null) {
            ctx.beginPath();
            this.pathRoundRectangle(ctx, arg.roundRectangle, bias);
            return true;
        }

        if (arg.ellipse != null) {
            ctx.beginPath();
            this.pathEllipse(ctx, arg.ellipse, bias);
            return true;
        }

        if (arg.arc != null) {
            ctx.beginPath();
            this.pathArc(ctx, arg.arc, bias);
            return true;
        }

        // generic path
        if (arg.path != null) {
            ctx.beginPath();
            const path = arg.path;
            let off = 0;
            if (path.type) {
                path.type.forEach((type) => {
                    switch (type) {
                        case SegmentTypeProto.MOVE:
                            ctx.moveTo(path.points![off + 0] + bias.x, path.points![off + 1] + bias.y);
                            off += 2;
                            break;
                        case SegmentTypeProto.LINE:
                            ctx.lineTo(path.points![off + 0] + bias.x, path.points![off + 1] + bias.y);
                            off += 2;
                            break;
                        case SegmentTypeProto.QUAD:
                            ctx.quadraticCurveTo(path.points![off + 0] + bias.x, path.points![off + 1] + bias.y,
                                path.points![off + 2] + bias.x, path.points![off + 3] + bias.y);
                            off += 4;
                            break;
                        case SegmentTypeProto.CUBIC:
                            ctx.bezierCurveTo(path.points![off + 0] + bias.x, path.points![off + 1] + bias.y,
                                path.points![off + 2] + bias.x, path.points![off + 3] + bias.y,
                                path.points![off + 4] + bias.x, path.points![off + 5] + bias.y);
                            off += 6;
                            break;
                        case SegmentTypeProto.CLOSE:
                            ctx.closePath();
                            break;
                        default:
                            console.warn("segment.type:" + path.type + " not recognized");
                    }
                });
            }
            return true;
        }
        return false;
    }

    public pathRectangle(ctx: ICanvasCtx, rect: IRectangleProto, bias: IBias) {
        ctx.rect(rect.x + bias.x, rect.y + bias.y, rect.w, rect.h);
    }

    public pathEllipse(ctx:ICanvasCtx, elli:IEllipseProto, bias:IBias) {
        const kappa = 0.5522847498307933;
        const pcv = 0.5 + kappa * 0.5;
        const ncv = 0.5 - kappa * 0.5;

        ctx.moveTo(elli.x + bias.x + elli.w, elli.y + bias.y + 0.5 * elli.h);
        let pts = DirectDraw.getEllipseCoords([1.0, pcv, pcv, 1.0, 0.5, 1.0], elli, bias);
        ctx.bezierCurveTo(pts[0], pts[1], pts[2], pts[3], pts[4], pts[5]);
        pts = DirectDraw.getEllipseCoords([ncv, 1.0, 0.0, pcv, 0.0, 0.5], elli, bias);
        ctx.bezierCurveTo(pts[0], pts[1], pts[2], pts[3], pts[4], pts[5]);
        pts = DirectDraw.getEllipseCoords([0.0, ncv, ncv, 0.0, 0.5, 0.0], elli, bias);
        ctx.bezierCurveTo(pts[0], pts[1], pts[2], pts[3], pts[4], pts[5]);
        pts = DirectDraw.getEllipseCoords([pcv, 0.0, 1.0, ncv, 1.0, 0.5], elli, bias);
        ctx.bezierCurveTo(pts[0], pts[1], pts[2], pts[3], pts[4], pts[5]);
        ctx.closePath();
    }

    public pathRoundRectangle(ctx:ICanvasCtx, rr:IRoundRectangleProto, bias:IBias) {
        const acv = 0.22385762508460333;

        let pts = DirectDraw.getRRCoords([0, 0, 0, 0.5], rr, bias);
        ctx.moveTo(pts[0], pts[1]);

        pts = DirectDraw.getRRCoords([0, 0, 1, -0.5], rr, bias);
        ctx.lineTo(pts[0], pts[1]);
        pts = DirectDraw.getRRCoords([0, 0, 1, -acv, 0, acv, 1, 0, 0, 0.5, 1, 0], rr, bias);
        ctx.bezierCurveTo(pts[0], pts[1], pts[2], pts[3], pts[4], pts[5]);

        pts =DirectDraw. getRRCoords([1, -0.5, 1, 0], rr, bias);
        ctx.lineTo(pts[0], pts[1]);
        pts =DirectDraw. getRRCoords([1, -acv, 1, 0, 1, 0, 1, -acv, 1, 0, 1, -0.5], rr, bias);
        ctx.bezierCurveTo(pts[0], pts[1], pts[2], pts[3], pts[4], pts[5]);

        pts =DirectDraw. getRRCoords([1, 0, 0, 0.5], rr, bias);
        ctx.lineTo(pts[0], pts[1]);
        pts =DirectDraw. getRRCoords([1, 0, 0, acv, 1, -acv, 0, 0, 1, -0.5, 0, 0], rr, bias);
        ctx.bezierCurveTo(pts[0], pts[1], pts[2], pts[3], pts[4], pts[5]);

        pts =DirectDraw. getRRCoords([0, 0.5, 0, 0], rr, bias);
        ctx.lineTo(pts[0], pts[1]);
        pts = DirectDraw.getRRCoords([0, acv, 0, 0, 0, 0, 0, acv, 0, 0, 0, 0.5], rr, bias);
        ctx.bezierCurveTo(pts[0], pts[1], pts[2], pts[3], pts[4], pts[5]);

        ctx.closePath();
    }

    public pathArc(ctx:ICanvasCtx, arc:IArcProto, bias:IBias) {
        const w = arc.w / 2;
        const h = arc.h / 2;
        const  x = arc.x + bias.x + w;
        const   y = arc.y + bias.y + h;
        const angStRad = -(arc.start! * Math.PI / 180);
        const ext = -arc.extent!;
        let arcSegs = 4;
        let increment = ext < 0 ? Math.PI / 2 : -Math.PI / 2;
        let cv = ext < 0 ? 0.5522847498307933 : -0.5522847498307933;
        if (ext > -360 && ext < 360) {
            arcSegs = Math.ceil(Math.abs(ext) / 90);
            increment = (ext / arcSegs) * Math.PI / 180;
            cv = 4.0 / 3.0 * Math.sin(increment / 2) / (1.0 + Math.cos(increment / 2));
            arcSegs = cv === 0 ? 0 : arcSegs;
        }
        ctx.moveTo(x + Math.cos(angStRad) * w, y + Math.sin(angStRad) * h);
        for (let i = 0; i < arcSegs; i++) {
            let angle = angStRad + increment * i;
            let relx = Math.cos(angle);
            let rely = Math.sin(angle);
            const pts = [];
            pts[0] = (x + (relx - cv * rely) * w);
            pts[1] = (y + (rely + cv * relx) * h);
            angle += increment;
            relx = Math.cos(angle);
            rely = Math.sin(angle);
            pts[2] = (x + (relx + cv * rely) * w);
            pts[3] = (y + (rely - cv * relx) * h);
            pts[4] = (x + relx * w);
            pts[5] = (y + rely * h);
            ctx.bezierCurveTo(pts[0], pts[1], pts[2], pts[3], pts[4], pts[5]);
        }
        switch (arc.type) {
            case ArcTypeProto.OPEN:
                break;
            case ArcTypeProto.CHORD:
                ctx.closePath();
                break;
            case ArcTypeProto.PIE:
                ctx.lineTo(x, y);
                ctx.closePath();
                break;
        }
    }

    public prepareImages(images: DecodedProtoImage[]) {
        return new Promise<void>((resolve, reject) => {
            try {
                if (images.length > 0) {
                    const loadedImages = images.map((image) => {
                        return new Promise<void>((resolveImg) => {
                            const img = new Image();
                            const dataurl=DirectDraw.getImageData(image.data as Uint8Array);
                            if(dataurl) {
                                img.onload = () => {
                                    image.data = img;
                                    resolveImg();
                                };
                                img.src = dataurl
                            }else{
                                image.data = img;
                                resolveImg();
                            }
                        });
                    });
                    Promise.all(loadedImages).then(() => resolve());
                } else {
                    resolve();
                }
            } catch (e) {
                this.config.onErrorMessage(e as Error);
                reject(e);
            }
        });
    }

    public initializeFontFaces(fontFaces: IFontFaceProto[]) {
        return new Promise<void>((resolve, reject) => {
            try {
                if (fontFaces.length > 0) {
                    const loadedFonts = fontFaces.map((fontFace) => {
                        return new Promise<void>((resolveFont) => {
                            if (this.fontsArray.indexOf(fontFace.name) >= 0) {
                                resolveFont();
                            } else {
                                this.fontsArray.push(fontFace.name);
                                const fontCss = document.createElement("style");
                                fontCss.type = "text/css";
                                fontCss.setAttribute("data-dd-ctx", this.ctxId);
                                const fontName = this.ctxId + fontFace.name;
                                fontCss.innerHTML = DirectDraw.getFontFaceData(fontName, fontFace.font, fontFace.style!);
                                document.body.appendChild(fontCss);
                                this.waitForFont(fontName,resolveFont);
                            }
                        });
                    });
                    Promise.all(loadedFonts).then(() => resolve());
                } else {
                    resolve();
                }
            } catch (e) {
                this.config.onErrorMessage(e as Error);
                reject(e);
            }
        });
    }

    public dispose() {
        const styles = Array.from(document.body.getElementsByTagName("style"));
        const toRemove = [];
        for (const style of styles) {
            if (style.getAttribute("data-dd-ctx") === this.ctxId) {
                toRemove.push(style);
            }
        }
        toRemove.forEach( (element) =>{
            document.body.removeChild(element);
        });
    }

    public logRenderTime(startTime:number, webImage:WebImageProto) {
        if (this.config.logDebug && webImage != null) {
            const time = new Date().getTime() - startTime;
            const instLength = webImage.instructions == null ? 0 : webImage.instructions.length;
            const constLength = webImage.constants == null ? 0 : webImage.constants.length;
            const fontsLength = webImage.fontFaces == null ? 0 : webImage.fontFaces.length;
            console.warn("DirectDraw DEBUG render time " + time + "ms (insts:" + instLength + ", consts:" + constLength + ", fonts:" + fontsLength + ")");
        }
    }

    public getConstantPoolCache() {
        return this.constantPoolCache;
    }

    private waitForFont(fontName:string,resolve: ()=>void){
        const timeout = (ms:number)=> new Promise((r)=>setTimeout(r,ms));
        const start = new Date().getTime();
        const pollFont = () => {
            if (DirectDraw.isFontAvailable(fontName) || new Date().getTime()-start > 100) {
                resolve();
            } else {
                timeout(5).then(pollFont);
            }
        }
        pollFont();
    }

    private drawWebImage(image: WebImageProto, targetCanvas?: HTMLCanvasElement): PromiseLike<HTMLCanvasElement> {
        return new Promise<HTMLCanvasElement>((resolve, reject) => {
            try {
                this.drawWebImageInternal(image, resolve, reject,targetCanvas);
            } catch (e) {
                this.config.onErrorMessage(e as Error);
                reject(e);
            }
        });
    }

    private drawWebImageInternal(image: WebImageProto, resolve: (value: HTMLCanvasElement) => void, reject: (value?: Error) => void,targetCanvas?: HTMLCanvasElement) {
        let newCanvas: HTMLCanvasElement;
        const renderStart = new Date().getTime();
        const dpr = this.config.dpr;
        if (targetCanvas != null) {
            if (targetCanvas.width !== image.width * dpr || targetCanvas.height !== image.height * dpr) {
                let buffer = this.canvasBuffer.pop();
                if(buffer === undefined){
                    buffer = document.createElement("canvas");
                }
                buffer.width = targetCanvas.width;
                buffer.height = targetCanvas.height;
                buffer.getContext("2d")!.drawImage(targetCanvas,0,0);

                targetCanvas.width = image.width * dpr;
                targetCanvas.height = image.height * dpr;
                if(buffer.width>0 && buffer.height>0){
                    targetCanvas.getContext("2d")!.drawImage(buffer,0,0);
                }
                this.canvasBuffer.push(buffer);
            }
            newCanvas = targetCanvas;
        } else {
            newCanvas = document.createElement("canvas");
            newCanvas.classList.add("suramadu-canvas");
            newCanvas.getContext("2d")!.scale(dpr, dpr);
            newCanvas.width = image.width * dpr;
            newCanvas.height = image.height * dpr;
        }
        newCanvas.style.width = image.width+"px";
        newCanvas.style.height = image.height+"px";


        const imageContext: IImageContext = {
            canvas: newCanvas,
            graphicsStates: [],
            currentStateId: -1
        };

        const images = this.populateConstantsPool(image.constants);
        this.prepareImages(images)
            .then(() =>
                this.initializeFontFaces(image.fontFaces)
            )
            .then(() => {
                let ctx = DirectDraw.toCanvasCtx(imageContext.canvas.getContext("2d")!);
                if (ctx != null) {
                    if (this.config.logTrace) {
                        ctx = this.wrap2TraceLoggerCtx(ctx);
                    }
                    if (image.instructions != null) {
                        ctx.save();
                        image.instructions.reduce((seq, instruction) => {
                            return seq.then(() => {
                                return this.interpretInstruction(ctx!, instruction, imageContext);
                            });
                        }, Promise.resolve()).then(() => {
                            ctx!.restore();
                            this.logRenderTime(renderStart, image);
                            resolve(imageContext.canvas);
                        }).catch((error) => {
                            ctx!.restore();
                            reject(error);
                            this.config.onErrorMessage(error);
                        });
                    }
                } else {
                    reject(new Error("ctx is null"));
                }
            }).catch((error) => {
            this.config.onErrorMessage(error);
        });
    }

    private populateConstantsPool(constants: IDrawConstantProto[]) {
        const images: Array<IImageProto | IGlyphProto> = [];
        constants.forEach((constant) => {
            this.constantPoolCache[constant.id] = constant;
            if (constant.image != null) {
                images.push(constant.image);
            } else if (constant.texture != null) {
                images.push(constant.texture.image);
            } else if (constant.glyph != null && constant.glyph.data != null) {
                images.push(constant.glyph);
            }
        });
        return images;
    }

    private interpretInstruction(ctx: ICanvasCtx, instruction: IDrawInstructionProto, imageContext: IImageContext) {
        const ctxOriginal = ctx;
        const args = instruction.args ? DirectDraw.resolveArgs(instruction.args, this.constantPoolCache) : [];
        const graphicsState = imageContext.graphicsStates[imageContext.currentStateId];
        const xorMode = this.isXorMode(graphicsState);
        if (xorMode) {
            ctx = this.initXorModeCtx(graphicsState, ctxOriginal);
        }
        switch (instruction.inst) {
            case InstructionProto.GRAPHICS_CREATE:
                this.iprtGraphicsCreate(ctxOriginal, instruction.args ? instruction.args[0] : 0, args, imageContext);
                break;
            case InstructionProto.GRAPHICS_SWITCH:
                this.iprtGraphicsSwitch(ctxOriginal, instruction.args ? instruction.args[0] : 0, imageContext);
                break;
            case InstructionProto.GRAPHICS_DISPOSE:
                this.iprtGraphicsDispose(instruction.args ? instruction.args[0] : 0, imageContext);
                break;
            case InstructionProto.DRAW:
                this.iprtDraw(ctx, args, graphicsState.transform);
                break;
            case InstructionProto.FILL:
                this.iprtFill(ctx, args);
                break;
            case InstructionProto.DRAW_IMAGE:
                this.iprtDrawImage(ctx, args);
                break;
            case InstructionProto.DRAW_WEBIMAGE:
                return this.iprtDrawWebImage(ctx, args, instruction.webImage!);
                break;
            case InstructionProto.DRAW_STRING:
                this.iprtDrawString(ctx, args, graphicsState.fontTransform);
                break;
            case InstructionProto.COPY_AREA:
                this.iprtCopyArea(ctx, args);
                break;
            case InstructionProto.SET_STROKE:
                graphicsState.strokeArgs = args;
                this.iprtSetStroke(ctxOriginal, args);
                break;
            case InstructionProto.SET_PAINT:
                graphicsState.paintArgs = args;
                this.iprtSetPaint(ctxOriginal, args);
                break;
            case InstructionProto.SET_COMPOSITE:
                graphicsState.compositeArgs = args;
                this.iprtSetComposite(ctxOriginal, args);
                break;
            case InstructionProto.SET_FONT:
                graphicsState.fontArgs = args;
                graphicsState.fontTransform = this.iprtSetFont(ctxOriginal, args);
                break;
            case InstructionProto.TRANSFORM:
                graphicsState.transform = DirectDraw.concatTransform(graphicsState.transform, this.iprtTransform(ctxOriginal, args), this.config.dpr);
                break;
            case InstructionProto.DRAW_GLYPH_LIST:
                this.iprtDrawGlyphList(ctx, args);
                break;
            default:
                console.warn("instruction code: " + instruction.inst + " not recognized");
        }

        if (xorMode) {
            const xorctx = ctx as XorModeCanvasCtx;
            const bbox = xorctx.popBoundingBox();

            const xorModeColor = DirectDraw.parseColorSamples(graphicsState.compositeArgs[0].composite!.color!);
            DirectDraw.applyXorModeComposition(ctxOriginal, xorctx, xorModeColor, bbox, this.config.logDebug);
        }
        return Promise.resolve();
    }

    private isXorMode(graphicsState: IGraphicsState) {
        return graphicsState != null && graphicsState.compositeArgs != null && graphicsState.compositeArgs[0].composite!.type === CompositeTypeProto.XOR_MODE;
    }

    private initXorModeCtx(graphicsState: IGraphicsState, original: ICanvasCtx) {
        this.xorLayer.width = original.canvas.width;
        this.xorLayer.height = original.canvas.height;
        const ctx = DirectDraw.toCanvasCtx(this.xorLayer.getContext("2d")!);
        const xorCtx = this.wrapContext(ctx, this.config.dpr);
        this.setCtxState(graphicsState, xorCtx);
        return xorCtx;
    }

    private wrapContext(ctxOriginal: ICanvasCtx, dpr: number) {
        const ctx = ctxOriginal as XorModeCanvasCtx;
        if (!ctx.wrapped) {

            ctx.wrapped = true;
            ctx.boundingBox = null;
            const emptyBBox = () => {
                return {minX: 99999999999, minY: 99999999999, maxX: -99999999999, maxY: -99999999999}
            }
            ctx.pathBBox = emptyBBox();
            // track bounding boxes of changed areas:
            const beginPathOriginal = ctx.beginPath;
            ctx.beginPath = function () {
                this.pathBBox = emptyBBox();
                return beginPathOriginal.call(this);
            };
            const setTransformOriginal = ctx.setTransform;
            ctx.setTransform = function (m11: number, m12: number, m21: number, m22: number, dx: number, dy: number): void {
                this.transfomMatrix = [m11, m12, m21, m22, dx, dy];
                setTransformOriginal.call(this, m11, m12, m21, m22, dx, dy);
            };

            const transformOriginal = ctx.transform;
            ctx.transform = function (m11: number, m12: number, m21: number, m22: number, dx: number, dy: number) {
                this.transfomMatrix = DirectDraw.concatTransform(this.transfomMatrix, [m11, m12, m21, m22, dx, dy], dpr);
                return transformOriginal.call(this, m11, m12, m21, m22, dx, dy);
            };

            ctx.updateMinMax = function (x: number, y: number) {
                const tp = DirectDraw.transformPoint(x, y, this.transfomMatrix);
                if (tp.x < this.pathBBox.minX) { this.pathBBox.minX = tp.x; }
                if (tp.x > this.pathBBox.maxX) { this.pathBBox.maxX = tp.x; }
                if (tp.y < this.pathBBox.minY) { this.pathBBox.minY = tp.y; }
                if (tp.y > this.pathBBox.maxY) { this.pathBBox.maxY = tp.y; }
            };

            const fillTextOriginal = ctx.fillText;
            ctx.fillText = function (text: string, x: number, y: number, maxWidth?: number) {
                this.pathBBox = emptyBBox();
                const width = maxWidth || this.measureText(text).width;
                const height = this.measureText("M").width * 2;// approximation
                this.updateMinMax(x - 3, y - height * 0.7);
                this.updateMinMax(x - 3, y + height * 0.3);
                this.updateMinMax(x + width * 1.2, y - height * 0.7);
                this.updateMinMax(x + width * 1.2, y + height * 0.3);
                this.setBoundingBox();
                if (maxWidth === undefined) { // IE can not handle maxWidth==undefined
                    return fillTextOriginal.call(this, text, x, y);
                } else {
                    return fillTextOriginal.call(this, text, x, y, maxWidth);
                }
            };

            const moveToOriginal = ctx.moveTo;
            ctx.moveTo = function (x: number, y: number) {
                this.updateMinMax(x, y);
                return moveToOriginal.call(this, x, y);
            };

            const lineToOriginal = ctx.lineTo
            ctx.lineTo = function (x: number, y: number) {
                this.updateMinMax(x, y);
                return lineToOriginal.call(this, x, y);
            };

            const quadraticCurveToOriginal = ctx.quadraticCurveTo
            ctx.quadraticCurveTo = function (cpx: number, cpy: number, x: number, y: number) {
                this.updateMinMax(x, y);
                this.updateMinMax(cpx, cpy);
                return quadraticCurveToOriginal.call(this, cpx, cpy, x, y);
            };

            const bezierCurveToOriginal = ctx.bezierCurveTo
            ctx.bezierCurveTo = function (cp1x: number, cp1y: number, cp2x: number, cp2y: number, x: number, y: number) {
                this.updateMinMax(x, y);
                this.updateMinMax(cp1x, cp1y);
                this.updateMinMax(cp2x, cp2y);
                return bezierCurveToOriginal.call(this, cp1x, cp1y, cp2x, cp2y, x, y);
            };

            const rectOriginal = ctx.rect
            ctx.rect = function (x: number, y: number, w: number, h: number) {
                this.updateMinMax(x, y);
                this.updateMinMax(x, y + h);
                this.updateMinMax(x + w, y);
                this.updateMinMax(x + w, y + h);
                return rectOriginal.call(this, x, y, w, h);
            };

            const fillOriginal = ctx.fill;
            ctx.fill = function () {
                this.setBoundingBox();
                return fillOriginal.call(this);
            };

            const drawImageOriginal = ctx.drawImage;
            ctx.drawImage = function (image: HTMLImageElement | HTMLCanvasElement, dx: number, dy: number, dw?: number, dh?: number, sx?: number, sy?: number, sw?: number, sh?: number) {
                this.pathBBox = emptyBBox();
                this.setBoundingBox();
                return drawImageOriginal.call(this, image, dx, dy, dw, dh, sx, sy, sw, sh);
            };

            const strokeOriginal = ctx.stroke;
            ctx.stroke = function () {
                this.setBoundingBox(this.lineWidth / 2 + 3);
                return strokeOriginal.call(this);
            }

            ctx.setBoundingBox = function (excess?: number) {
                const excessX = (excess || 0) * this.transfomMatrix[0];
                const excessY = (excess || 0) * this.transfomMatrix[3];
                const x = Math.min(Math.max(0, this.pathBBox.minX - excessX), this.canvas.width);
                const y = Math.min(Math.max(0, this.pathBBox.minY - excessY), this.canvas.height);
                const mx = Math.min(Math.max(0, this.pathBBox.maxX + excessX), this.canvas.width);
                const my = Math.min(Math.max(0, this.pathBBox.maxY + excessY), this.canvas.height);
                this.boundingBox = {x, y, w: mx - x, h: my - y};
            };

            ctx.popBoundingBox = function () {
                const result = this.boundingBox;
                this.boundingBox = null;
                if (result != null) {
                    result.x = Math.floor(result.x);
                    result.y = Math.floor(result.y);
                    result.w = Math.ceil(result.w);
                    result.h = Math.ceil(result.h);
                }
                return result;
            }
        }
        return ctx;
    }

    private wrap2TraceLoggerCtx(ctx: LoggingCanvasCtx|ICanvasCtx): LoggingCanvasCtx {
        if ((ctx as LoggingCanvasCtx).traceLoggerWrapped) {
            return ctx
        }

        const tracerFactory = (original: any, name: string) => {
            // tslint:disable-next-line:only-arrow-functions
            return function () {
                console.warn("ctx." + name + "(" + printArguments(arguments) + ")")
                original.apply(ctx, Array.from(arguments));
            }
        }

        const printArguments = (as: IArguments) => {
            let str = '';
            for (const a of as) {
                str += "," + JSON.stringify(a);
            }
            return str.length > 0 ? str.substring(1) : str;
        }
        ['fill', 'stroke', 'beginPath', 'save', 'restore',
            'setTransform', 'clip', 'rect', 'drawImage', 'fillRect', 'transform',
            'fillText', 'scale', 'setLineDash', 'moveTo', 'lineTo', 'quadraticCurveTo',
            'bezierCurveTo', 'closePath'].reduce( (c: any, name)=> {
            c[name] = tracerFactory(c[name], name);
            return c;
        }, ctx);
        (ctx as LoggingCanvasCtx).traceLoggerWrapped = true;
        return ctx;
    }
}


interface ICanvasCtx {
    save: () => void,
    restore: () => void,
    beginPath: () => void,
    closePath: () => void,
    setTransform: (a: number, b: number, c: number, d: number, e: number, f: number) => void,
    transform: (a: number, b: number, c: number, d: number, e: number, f: number) => void,
    translate: (x: number, y: number) => void
    scale: (x: number, y: number) => void
    fillText: (text: string, x: number, y: number, maxWidth?: number) => void,
    moveTo: (x: number, y: number) => void,
    lineTo: (x: number, y: number) => void,
    quadraticCurveTo: (cpx: number, cpy: number, x: number, y: number) => void,
    bezierCurveTo: (cp1x: number, cp1y: number, cp2x: number, cp2y: number, x: number, y: number) => void,
    rect: (x: number, y: number, w: number, h: number) => void,
    fill: (fillRule?: "evenodd" | "nonzero" | undefined) => void,
    drawImage: (image: HTMLImageElement | HTMLCanvasElement, dx: number, dy: number, dw?: number, dh?: number, sx?: number, sy?: number, sw?: number, sh?: number) => void,
    createPattern: (image: HTMLImageElement | HTMLCanvasElement, repetition: string) => CanvasPattern|null,
    createLinearGradient: (x0:number, y0:number, x1:number, y1:number)=>CanvasGradient,
    createRadialGradient:(x0:number, y0:number, r0:number, x1:number, y1:number, r1:number)=>CanvasGradient,
    stroke: () => void,
    clip: (fillRule?: "evenodd" | "nonzero" | undefined) => void
    measureText: (text: string) => { width: number },
    getImageData: (x: number, y: number, w: number, h: number) => ImageData,
    putImageData: (data: ImageData, x: number, y: number) => void,
    lineWidth: number,
    fillStyle: string | CanvasPattern |CanvasGradient,
    strokeStyle: string | CanvasPattern|CanvasGradient,
    miterLimit: number,
    lineCap: string,
    lineJoin: string,
    setLineDash?: (segments: number[]) => void,
    lineDashOffset: number,
    font: string,
    globalAlpha:number,
    globalCompositeOperation:string,
    canvas: HTMLCanvasElement
}

type LoggingCanvasCtx = ICanvasCtx & {
    traceLoggerWrapped?: boolean
}

type XorModeCanvasCtx = ICanvasCtx & {
    updateMinMax: (x: number, y: number) => void,
    setBoundingBox: (excess?: number) => void,
    popBoundingBox: () => null | IRectangle,
    wrapped: boolean,
    boundingBox: null | IRectangle
    pathBBox: { minX: number, maxX: number, minY: number, maxY: number }
    transfomMatrix: [number, number, number, number, number, number]
}

type DecodedProtoImage = IImageProto | IGlyphProto | { data: HTMLImageElement }

interface IGraphicsState {
    transform: Tx;
    fontTransform: ITransformProto|null;
    fontArgs: IDrawConstantProto[];
    paintArgs: IDrawConstantProto[];
    strokeArgs: IDrawConstantProto[];
    compositeArgs: IDrawConstantProto[]
}

interface IImageContext { canvas: HTMLCanvasElement; graphicsStates: IGraphicsState[]; currentStateId: number; }

type Tx = [number, number, number, number, number, number]

interface IColor { r: number; g: number; b: number; a: number; }

interface IRectangle { x: number; y: number; w: number; h: number; }

interface IBias { x: number, y: number }
