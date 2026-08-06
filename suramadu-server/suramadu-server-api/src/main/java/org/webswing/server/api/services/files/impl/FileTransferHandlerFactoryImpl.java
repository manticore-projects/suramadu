package org.webswing.server.api.services.files.impl;

import com.google.inject.Singleton;
import org.webswing.server.api.services.application.AppPathHandler;
import org.webswing.server.api.services.files.FileTransferHandler;
import org.webswing.server.api.services.files.FileTransferHandlerFactory;

@Singleton
public class FileTransferHandlerFactoryImpl implements FileTransferHandlerFactory {

  public FileTransferHandler create(AppPathHandler manager) {
    return new FileTransferHandlerImpl(manager);
  }
}
