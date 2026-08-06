package netscape.javascript;

import org.webswing.model.appframe.in.JSObjectMsgIn;
import org.webswing.toolkit.jslink.WebJSObject;

/**
 * Replacement for the standard netscape.javascript.JSObject API that was removed from the JDK after
 * Java 8.
 *
 * Deployed via bootclasspath so Swing apps can use Java ↔ JS bridging.
 */
public abstract class JSObject {
  public JSObject() {}

  public abstract Object call(String var1, Object[] var2) throws JSException;

  public abstract Object eval(String var1) throws JSException;

  public abstract Object getMember(String var1) throws JSException;

  public abstract void setMember(String var1, Object var2) throws JSException;

  public abstract void removeMember(String var1) throws JSException;

  public abstract Object getSlot(int var1) throws JSException;

  public abstract void setSlot(int var1, Object var2) throws JSException;

  /**
   * Returns a JSObject for the window containing the given object. The parameter type was Applet in
   * JDK 8; changed to Object since java.applet.Applet was removed in JDK 17.
   */
  @SuppressWarnings("unused")
  public static JSObject getWindow(Object applet) throws JSException {
    return new WebJSObject((JSObjectMsgIn) null);
  }
}
