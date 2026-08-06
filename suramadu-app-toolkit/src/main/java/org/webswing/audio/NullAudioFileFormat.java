package org.webswing.audio;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

public class NullAudioFileFormat extends AudioFileFormat {

  public NullAudioFileFormat() {
    super(null, new AudioFormat(AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED,
        AudioSystem.NOT_SPECIFIED, false, false), AudioSystem.NOT_SPECIFIED);
  }

}
