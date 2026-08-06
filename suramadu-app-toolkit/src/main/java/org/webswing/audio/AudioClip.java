package org.webswing.audio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

import org.webswing.toolkit.util.Util;

public class AudioClip implements Clip, DataLine {

  private final String id = System.identityHashCode(this) + "";

  private boolean open;
  private byte[] data;
  private int loopCount;
  private long lastPlaybackTimestamp;

  private float timePosition = 0;

  private List<LineListener> listeners = new ArrayList<>();

  public AudioClip(AudioMixer mixer, DataLine.Info info) {}

  @Override
  public void open(AudioInputStream stream) throws LineUnavailableException, IOException {
    if (isOpen()) {
      throw new IllegalStateException("Clip is already open!");
    }

    byte[] data = toByteArray(stream);

    if (data != null) {
      this.data = Arrays.copyOf(data, data.length);
    }

    open = true;

    if (!listeners.isEmpty()) {
      LineEvent event = new LineEvent(this, LineEvent.Type.OPEN, getLongFramePosition());
      for (LineListener listener : listeners) {
        listener.update(event);
      }
    }
  }

  @Override
  public void open(AudioFormat format, byte[] audioData, int offset, int bufferSize)
      throws LineUnavailableException {
    if (isOpen()) {
      throw new IllegalStateException("Clip is already open!");
    }

    this.data = Arrays.copyOf(audioData, audioData.length);

    open = true;

    if (!listeners.isEmpty()) {
      LineEvent event = new LineEvent(this, LineEvent.Type.OPEN, getLongFramePosition());
      for (LineListener listener : listeners) {
        listener.update(event);
      }
    }
  }

  @Override
  public void open() throws LineUnavailableException {
    if (isOpen()) {
      throw new IllegalStateException("Clip is already open!");
    }
    open = true;

    if (!listeners.isEmpty()) {
      LineEvent event = new LineEvent(this, LineEvent.Type.OPEN, getLongFramePosition());
      for (LineListener listener : listeners) {
        listener.update(event);
      }
    }
  }

  @Override
  public void setMicrosecondPosition(long microseconds) {
    timePosition = microseconds / 1_000_000.0f;
    Util.getWebToolkit().getPaintDispatcher().notifyAudioEventUpdate(this, timePosition, loopCount);
  }

  @Override
  public long getMicrosecondPosition() {
    return (long) timePosition * 1_000_000;
  }

  @Override
  public boolean isOpen() {
    return open;
  }

  @Override
  public void start() {
    if (!isOpen()) {
      return;
    }

    Util.getWebToolkit().getPaintDispatcher().notifyAudioEventPlay(this, data, timePosition, 0);

    lastPlaybackTimestamp = -1; // initialize, next values will come from browser

    if (!listeners.isEmpty()) {
      LineEvent event = new LineEvent(this, LineEvent.Type.START, getLongFramePosition());
      for (LineListener listener : listeners) {
        listener.update(event);
      }
    }
  }

  @Override
  public void loop(int count) {
    if (!isOpen()) {
      return;
    }

    loopCount = count;

    Util.getWebToolkit().getPaintDispatcher().notifyAudioEventPlay(this, data, timePosition,
        loopCount);

    lastPlaybackTimestamp = -1; // initialize, next values will come from browser

    if (!listeners.isEmpty()) {
      LineEvent event = new LineEvent(this, LineEvent.Type.START, getLongFramePosition());
      for (LineListener listener : listeners) {
        listener.update(event);
      }
    }
  }

  @Override
  public void stop() {
    if (!isOpen()) {
      return;
    }

    Util.getWebToolkit().getPaintDispatcher().notifyAudioEventStop(this);

    notifyPlaybackStopped();
  }

  @Override
  public void close() {
    if (!isOpen()) {
      return;
    }

    stop();

    open = false;

    Util.getWebToolkit().getPaintDispatcher().notifyAudioEventDispose(this);

    if (!listeners.isEmpty()) {
      LineEvent event = new LineEvent(this, LineEvent.Type.CLOSE, getLongFramePosition());
      for (LineListener listener : listeners) {
        listener.update(event);
      }
    }
  }

  @Override
  public void addLineListener(LineListener listener) {
    listeners.add(listener);
  }

  @Override
  public void removeLineListener(LineListener listener) {
    listeners.remove(listener);
  }

  public String getId() {
    return id;
  }

  public void notifyPlaybackStopped() {
    lastPlaybackTimestamp = -1; // flag this as stopped

    if (!listeners.isEmpty()) {
      LineEvent event = new LineEvent(this, LineEvent.Type.STOP, getLongFramePosition());
      for (LineListener listener : listeners) {
        listener.update(event);
      }
    }
  }

  public void playbackPing() {
    lastPlaybackTimestamp = System.currentTimeMillis();
  }

  public long getLastPlaybackTimestamp() {
    return lastPlaybackTimestamp;
  }

  @Override
  public boolean isActive() {
    return false;
  }

  @Override
  public boolean isRunning() {
    return false;
  }

  @Override
  public int getFrameLength() {
    return AudioSystem.NOT_SPECIFIED;
  }

  @Override
  public long getMicrosecondLength() {
    return AudioSystem.NOT_SPECIFIED;
  }

  @Override
  public void setFramePosition(int frames) {}

  @Override
  public void setLoopPoints(int start, int end) {}

  @Override
  public int available() {
    return AudioSystem.NOT_SPECIFIED;
  }

  @Override
  public void drain() {}

  @Override
  public void flush() {}

  @Override
  public int getBufferSize() {
    return AudioSystem.NOT_SPECIFIED;
  }

  @Override
  public AudioFormat getFormat() {
    return null;
  }

  @Override
  public int getFramePosition() {
    return AudioSystem.NOT_SPECIFIED;
  }

  @Override
  public float getLevel() {
    return AudioSystem.NOT_SPECIFIED;
  }

  @Override
  public long getLongFramePosition() {
    return AudioSystem.NOT_SPECIFIED;
  }

  @Override
  public javax.sound.sampled.Line.Info getLineInfo() {
    return null;
  }

  @Override
  public Control[] getControls() {
    return new Control[0];
  }

  @Override
  public boolean isControlSupported(Type control) {
    return false;
  }

  @Override
  public Control getControl(Type control) {
    return null;
  }

  private static byte[] toByteArray(AudioInputStream is) throws IOException {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
      int reads = is.read();

      while (reads != -1) {
        baos.write(reads);
        reads = is.read();
      }

      is.close();

      return baos.toByteArray();
    }
  }

}
