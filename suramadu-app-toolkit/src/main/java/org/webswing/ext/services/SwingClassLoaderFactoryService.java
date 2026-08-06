package org.webswing.ext.services;

import java.net.URL;

public interface SwingClassLoaderFactoryService {

  ClassLoader createSwingClassLoader(URL[] toArray, ClassLoader parent);

}
