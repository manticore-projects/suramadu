interface GlobalEventHandlersEventMap {// extend lib.dom.d.ts
    'compositionstart': CompositionEvent
    'compositionupdate': CompositionEvent
    'compositionend': CompositionEvent
    'touchleave': TouchEvent
    "copy": ClipboardEvent;
    "cut": ClipboardEvent;
    "paste": ClipboardEvent;
    "wheel": WheelEvent;
}

declare module '*.html' {
    const content: string;
    export default content;
}

declare class ClipboardItem {
    constructor(data: { [mimeType: string]: Blob });
}
