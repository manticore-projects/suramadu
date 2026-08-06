package org.webswing.audio;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

public class AudioMixer implements Mixer {

  private static class Info extends Mixer.Info {
    Info() {
      super(INFO_NAME, INFO_VENDOR, INFO_DESCRIPTION, INFO_VERSION);
    }
  }

  private static final String INFO_NAME = "Webswing Sound Mixer";
  private static final String INFO_VENDOR = "Webswing";
  private static final String INFO_DESCRIPTION = "Webswing Sound Mixer";
  private static final String INFO_VERSION = "1.0";
  private static final Mixer.Info info = new Info();

  private final javax.sound.sampled.Line.Info sourceLineInfo;
  final Object control_mutex = this;
  private AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
  private final float controlrate = 147f;
  private final long latency = 100000;

  public AudioMixer() {
    ArrayList<AudioFormat> formats = new ArrayList<>();

    for (int channels = 1; channels <= 2; channels++) {
      formats.add(new AudioFormat(Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 8, channels,
          channels, AudioSystem.NOT_SPECIFIED, false));
      formats.add(new AudioFormat(Encoding.PCM_UNSIGNED, AudioSystem.NOT_SPECIFIED, 8, channels,
          channels, AudioSystem.NOT_SPECIFIED, false));
      for (int bits = 16; bits < 32; bits += 8) {
        formats.add(new AudioFormat(Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, bits, channels,
            channels * bits / 8, AudioSystem.NOT_SPECIFIED, false));
        formats.add(new AudioFormat(Encoding.PCM_UNSIGNED, AudioSystem.NOT_SPECIFIED, bits,
            channels, channels * bits / 8, AudioSystem.NOT_SPECIFIED, false));
        formats.add(new AudioFormat(Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, bits, channels,
            channels * bits / 8, AudioSystem.NOT_SPECIFIED, true));
        formats.add(new AudioFormat(Encoding.PCM_UNSIGNED, AudioSystem.NOT_SPECIFIED, bits,
            channels, channels * bits / 8, AudioSystem.NOT_SPECIFIED, true));
      }
      formats.add(new AudioFormat(Encoding.PCM_FLOAT, AudioSystem.NOT_SPECIFIED, 32, channels,
          channels * 4, AudioSystem.NOT_SPECIFIED, false));
      formats.add(new AudioFormat(Encoding.PCM_FLOAT, AudioSystem.NOT_SPECIFIED, 32, channels,
          channels * 4, AudioSystem.NOT_SPECIFIED, true));
      formats.add(new AudioFormat(Encoding.PCM_FLOAT, AudioSystem.NOT_SPECIFIED, 64, channels,
          channels * 8, AudioSystem.NOT_SPECIFIED, false));
      formats.add(new AudioFormat(Encoding.PCM_FLOAT, AudioSystem.NOT_SPECIFIED, 64, channels,
          channels * 8, AudioSystem.NOT_SPECIFIED, true));
    }
    AudioFormat[] formats_array = formats.toArray(new AudioFormat[formats.size()]);

    sourceLineInfo = new DataLine.Info(Clip.class, formats_array, AudioSystem.NOT_SPECIFIED,
        AudioSystem.NOT_SPECIFIED);
  }

  @Override
  public void open() throws LineUnavailableException {}

  public void open(SourceDataLine line) throws LineUnavailableException {}

  @Override
  public void close() {}

  @Override
  public boolean isOpen() {
    return false;
  }

  @Override
  public Control getControl(Type control) {
    throw new IllegalArgumentException("Unsupported control type : " + control);
  }

  @Override
  public Control[] getControls() {
    return new Control[0];
  }

  @Override
  public javax.sound.sampled.Line.Info getLineInfo() {
    return new Line.Info(Mixer.class);
  }

  @Override
  public boolean isControlSupported(Type control) {
    return false;
  }

  @Override
  public void addLineListener(LineListener listener) {}

  @Override
  public void removeLineListener(LineListener listener) {}

  @Override
  public Mixer.Info getMixerInfo() {
    return info;
  }

  @Override
  public javax.sound.sampled.Line.Info[] getSourceLineInfo() {
    return new Line.Info[] {sourceLineInfo};
  }

  @Override
  public javax.sound.sampled.Line.Info[] getSourceLineInfo(javax.sound.sampled.Line.Info info) {
    ArrayList<javax.sound.sampled.Line.Info> infos = new ArrayList<>();
    if (info.matches(sourceLineInfo)) {
      infos.add(sourceLineInfo);
    }
    return infos.toArray(new Line.Info[infos.size()]);
  }

  @Override
  public Line[] getSourceLines() {
    return new Line[0];
  }

  @Override
  public javax.sound.sampled.Line.Info[] getTargetLineInfo() {
    return new javax.sound.sampled.Line.Info[0];
  }

  @Override
  public javax.sound.sampled.Line.Info[] getTargetLineInfo(javax.sound.sampled.Line.Info info) {
    return new javax.sound.sampled.Line.Info[0];
  }

  @Override
  public Line[] getTargetLines() {
    return new Line[0];
  }

  @Override
  public boolean isLineSupported(javax.sound.sampled.Line.Info info) {
    if (info != null) {
      return info.matches(sourceLineInfo);
    }
    return false;
  }

  @Override
  public Line getLine(Line.Info info) throws LineUnavailableException {
    if (!isLineSupported(info))
      throw new IllegalArgumentException("Line unsupported: " + info);

    if ((info.getLineClass() == Clip.class)) {
      return new AudioClip(this, (DataLine.Info) info);
    }

    throw new IllegalArgumentException("Line unsupported: " + info);
  }

  @Override
  public int getMaxLines(Line.Info info) {
    if (info.getLineClass() == Clip.class) {
      return AudioSystem.NOT_SPECIFIED;
    }
    return 0;
  }

  @Override
  public boolean isSynchronizationSupported(Line[] lines, boolean maintainSync) {
    return false;
  }

  @Override
  public void synchronize(Line[] lines, boolean maintainSync) {
    throw new IllegalArgumentException("Synchronization not supported by this mixer.");
  }

  @Override
  public void unsynchronize(Line[] lines) {
    throw new IllegalArgumentException("Synchronization not supported by this mixer.");
  }

  public long getLatency() {
    synchronized (control_mutex) {
      return latency;
    }
  }

  public AudioFormat getFormat() {
    synchronized (control_mutex) {
      return format;
    }
  }

  float getControlRate() {
    return controlrate;
  }

}
