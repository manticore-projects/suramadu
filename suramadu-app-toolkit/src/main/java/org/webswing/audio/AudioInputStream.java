package org.webswing.audio;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;

public class AudioInputStream extends javax.sound.sampled.AudioInputStream {

  private InputStream stream;

  public AudioInputStream(InputStream stream, AudioFormat format, long length) {
    super(stream, format, length);
    this.stream = stream;
  }

  @Override
  public int read() throws IOException {
    return stream.read();
  }

  @Override
  public int read(byte[] b) throws IOException {
    return stream.read(b);
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return stream.read(b, off, len);
  }

  @Override
  public long skip(long n) throws IOException {
    return stream.skip(n);
  }

  @Override
  public int available() throws IOException {
    return stream.available();
  }

  @Override
  public void close() throws IOException {
    stream.close();
  }

  @Override
  public void mark(int readlimit) {
    stream.mark(readlimit);
  }

  @Override
  public void reset() throws IOException {
    stream.reset();
  }

}
