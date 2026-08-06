package org.webswing.classloader;

import org.webswing.ext.services.SwingClassLoaderFactoryService;

import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

public class SwingClassLoaderFactory implements SwingClassLoaderFactoryService {

  private static SwingClassLoaderFactory impl;

  public static SwingClassLoaderFactory getInstance() {
    if (impl == null) {
      impl = new SwingClassLoaderFactory();
    }
    return impl;
  }

  private SwingClassLoaderFactory() {}

  @Override
  public ClassLoader createSwingClassLoader(final URL[] classpath, final ClassLoader parent) {
    return new SwingClassLoaderWithAccessControl(classpath, parent);
  }

  private class SwingClassLoaderWithAccessControl extends SwingClassloader {
    /* The context to be used when loading classes and resources */

    private final AccessControlContext acc;

    public SwingClassLoaderWithAccessControl(final URL[] classpath, final ClassLoader parent) {
      super(classpath, parent);
      this.acc = AccessController.getContext();
    }

    @Override
    protected synchronized Class<?> loadClass(final String class_name, final boolean resolve)
        throws ClassNotFoundException {
      try {
        return AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
          @Override
          public Class<?> run() throws ClassNotFoundException {
            return superLoadClass(class_name, resolve);
          }
        }, acc);
      } catch (final PrivilegedActionException e) {
        if (e.getException() instanceof ClassNotFoundException) {
          throw (ClassNotFoundException) e.getException();
        }
        throw new ClassNotFoundException(e.getMessage(), e);
      }
    }

    private synchronized Class<?> superLoadClass(final String class_name, final boolean resolve)
        throws ClassNotFoundException {
      return super.loadClass(class_name, resolve);
    }

  }

}
