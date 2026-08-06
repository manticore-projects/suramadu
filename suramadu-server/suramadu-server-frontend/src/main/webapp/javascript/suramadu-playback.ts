import html from './templates/playback.html';
import { ModuleDef } from './suramadu-inject';
import { appFrameProtoOut } from './proto/proto.out';
import { serverBrowserFrameProto } from './proto/proto.frame';

export const playbackInjectable = {
    cfg: 'webswing.config' as const,
    sendPlaybackCommand: 'socket.sendPlaybackCommand' as const,
    getCanvas: 'canvas.get' as const
}

export interface IPlaybackService {
    'playback.playbackInfo': (data: any) => void,
    'playback.showControls': () => void
}

const Command = serverBrowserFrameProto.PlaybackCommandMsgInProto.PlaybackCommandProto

export class PlaybackModule extends ModuleDef<typeof playbackInjectable, IPlaybackService> {

    public playbackBar?: JQuery<HTMLElement>;
    public ready = () => {
        if (this.api.cfg.recordingPlayback) {
            this.showControls();
        }
    }

    public provides() {
        return {
            'playback.playbackInfo': this.playbackInfo,
            'playback.showControls': this.showControls
        }
    }

    public playbackInfo(data: appFrameProtoOut.IAppFrameMsgOutProto) {
        if (this.playbackBar != null && data.playback != null) {
            const playbackData = data.playback!;
            let progress
            if (playbackData.current && playbackData.total) {
                progress = parseInt("" + (playbackData.current / playbackData.total * 100), 10);
            } else {
                progress = 0;
            }
            this.playbackBar.find('.ws-progress-bar').css('width', progress + '%');
            this.playbackBar.find('span[data-id="current"]').html(playbackData.current ? (playbackData.current + "") : "0");
            this.playbackBar.find('span[data-id="total"]').html(playbackData.total ? (playbackData.total + "") : "0")
        }
    }

    public showControls() {
        this.api.cfg.rootElement.append(html);
        this.playbackBar = this.api.cfg.rootElement.find('div[data-id="playbackBar"]');
        this.playbackBar!.find('button[data-id="reset"]').on('click', () => {
            const ctx = this.api.getCanvas().getContext("2d")!;
            ctx.clearRect(0, 0, this.api.getCanvas().width, this.api.getCanvas().height);
            $("canvas.suramadu-canvas:not([data-id=canvas])").remove();

            this.sendCommand(Command.reset);
        });
        this.playbackBar!.find('button[data-id="play"]').on('click', () => {
            this.sendCommand(Command.play);
        })
        this.playbackBar!.find('button[data-id="stop"]').on('click', () => {
            this.sendCommand(Command.stop);
        })
        this.playbackBar!.find('button[data-id="step"]').on('click', () => {
            this.sendCommand(Command.step);
        })
        this.playbackBar!.find('button[data-id="10step"]').on('click', () => {
            this.sendCommand(Command.step10);
        });
        this.playbackBar!.find('button[data-id="100step"]').on('click', () => {
            this.sendCommand(Command.step100);
        });
        this.playbackBar!.show();
    }

    private sendCommand(cmd: serverBrowserFrameProto.PlaybackCommandMsgInProto.PlaybackCommandProto) {
        this.api.sendPlaybackCommand({
            command: cmd
        });
    }

}