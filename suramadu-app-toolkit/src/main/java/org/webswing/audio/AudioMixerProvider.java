package org.webswing.audio;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.spi.MixerProvider;

public class AudioMixerProvider extends MixerProvider {

  private static AudioMixer globalmixer;
  private static Info globalmixerInfo;

  static Thread lockthread;

  static final Object mutex = new Object();

  @Override
  public Mixer getMixer(Info info) {
    initGlobalMixer();

    if (!(info == null || info == globalmixerInfo)) {
      throw new IllegalArgumentException(
          "Mixer " + info.toString() + " not supported by this provider.");
    }

    synchronized (mutex) {
      if (lockthread != null) {
        if (Thread.currentThread() == lockthread) {
          throw new IllegalArgumentException(
              "Mixer " + info.toString() + " not supported by this provider.");
        }
      }
      return globalmixer;
    }
  }

  @Override
  public Info[] getMixerInfo() {
    initGlobalMixer();
    return new Info[] {globalmixerInfo};
  }

  private void initGlobalMixer() {
    if (globalmixer == null) {
      globalmixer = new AudioMixer();
      globalmixerInfo = globalmixer.getMixerInfo();
    }
  }

}
