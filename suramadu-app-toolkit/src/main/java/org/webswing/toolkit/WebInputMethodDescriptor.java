package org.webswing.toolkit;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.im.spi.InputMethod;
import java.awt.im.spi.InputMethodDescriptor;
import java.util.Locale;


public class WebInputMethodDescriptor implements InputMethodDescriptor {

  public InputMethod createInputMethod() throws Exception {
    return new WebInputMethod();
  }

  public Locale[] getAvailableLocales() throws AWTException {
    return Locale.getAvailableLocales();
  }

  public String getInputMethodDisplayName(Locale inputLocale, Locale displayLanguage) {
    return "input method display name";
  }

  public Image getInputMethodIcon(Locale inputLocale) {
    return null;
  }

  public boolean hasDynamicLocaleList() {
    return false;
  }

}
