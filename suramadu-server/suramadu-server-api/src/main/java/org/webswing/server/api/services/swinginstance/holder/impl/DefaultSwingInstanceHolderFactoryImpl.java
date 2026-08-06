package org.webswing.server.api.services.swinginstance.holder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.webswing.server.api.services.swinginstance.holder.SwingInstanceHolder;
import org.webswing.server.api.services.swinginstance.holder.SwingInstanceHolderFactory;

@Singleton
public class DefaultSwingInstanceHolderFactoryImpl implements SwingInstanceHolderFactory {

  @Inject
  public DefaultSwingInstanceHolderFactoryImpl() {}

  @Override
  public SwingInstanceHolder createInstanceHolder() {
    return new DefaultSwingInstanceHolderImpl();
  }
}
