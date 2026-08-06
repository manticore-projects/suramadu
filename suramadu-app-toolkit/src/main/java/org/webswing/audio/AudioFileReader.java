package org.webswing.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioFileReader extends javax.sound.sampled.spi.AudioFileReader {

  private AudioFileFormat fileFormat = new NullAudioFileFormat();

  @Override
  public AudioFileFormat getAudioFileFormat(InputStream stream)
      throws UnsupportedAudioFileException, IOException {
    return fileFormat;
  }

  @Override
  public AudioFileFormat getAudioFileFormat(URL url)
      throws UnsupportedAudioFileException, IOException {
    try (InputStream stream = url.openStream()) {
      return getAudioFileFormat(stream);
    }
  }

  @Override
  public AudioFileFormat getAudioFileFormat(File file)
      throws UnsupportedAudioFileException, IOException {
    try (InputStream stream = new FileInputStream(file)) {
      return getAudioFileFormat(stream);
    }
  }

  @Override
  public AudioInputStream getAudioInputStream(InputStream stream)
      throws UnsupportedAudioFileException, IOException {
    return new AudioInputStream(stream, fileFormat.getFormat(), AudioSystem.NOT_SPECIFIED);
  }

  @Override
  public AudioInputStream getAudioInputStream(URL url)
      throws UnsupportedAudioFileException, IOException {
    final InputStream urlStream = url.openStream();
    try {
      return getAudioInputStream(new BufferedInputStream(urlStream));
    } catch (final Throwable e) {
      try {
        urlStream.close();
      } catch (IOException e2) {
        // ignore
      }
      throw e;
    }
  }

  @Override
  public AudioInputStream getAudioInputStream(File file)
      throws UnsupportedAudioFileException, IOException {
    final InputStream fileStream = new FileInputStream(file);
    try {
      return getAudioInputStream(new BufferedInputStream(fileStream));
    } catch (final Throwable e) {
      try {
        fileStream.close();
      } catch (IOException e2) {
        // ignore
      }
      throw e;
    }
  }

}
