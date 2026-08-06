package org.webswing.jslink.test;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.webswing.Constants;
import org.webswing.classloader.ClassModificationRegister;
import org.webswing.classloader.SwingClassloader;

import java.util.List;

public class ClassModificationBlacklistGenerator {
  public static void main(String[] args) {
    System.setProperty(Constants.SWING_START_SYS_PROP_CLASS_MODIFICATION_BLACKLIST,
        "test.unmodified_classes");
    ClassModificationRegister register = new ClassModificationRegister();
    List<String> classes = new FastClasspathScanner().scan().getNamesOfAllStandardClasses();
    for (String c : classes) {
      try {
        Class<?> clazz = Class.forName(c);
        boolean modified = SwingClassloader.isModified(clazz);
        register.setModificationState(c, modified);
        if (modified) {
          System.out.println(c + " will be modified.");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
