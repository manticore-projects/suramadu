package org.webswing.toolkit;

import java.awt.AWTEvent;
import java.awt.im.spi.InputMethod;
import java.awt.im.spi.InputMethodContext;
import java.lang.Character.Subset;
import java.util.Locale;

@SuppressWarnings("restriction")
public class WebInputMethod extends sun.awt.im.InputMethodAdapter implements InputMethod {

  public void activate() {}

  public void deactivate(boolean isTemporary) {}

  public void dispatchEvent(AWTEvent event) {}

  public void dispose() {}

  public void endComposition() {}

  public Object getControlObject() {
    return null;
  }

  public Locale getLocale() {
    return Locale.getDefault();
  }

  public void hideWindows() {}

  public boolean isCompositionEnabled() {
    return false;
  }

  public void removeNotify() {}

  public void setCharacterSubsets(Subset[] subsets) {}

  public void setCompositionEnabled(boolean enable) {}

  public void setInputMethodContext(InputMethodContext context) {}

  public boolean setLocale(Locale locale) {
    return false;
  }

  @Override
  public void disableInputMethod() {}

  @Override
  public String getNativeInputMethodInfo() {
    return "getNativeInputMethodInfo";
  }



}
