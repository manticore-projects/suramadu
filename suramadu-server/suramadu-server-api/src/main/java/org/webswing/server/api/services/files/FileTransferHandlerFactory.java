package org.webswing.server.api.services.files;

import org.webswing.server.api.services.application.AppPathHandler;

public interface FileTransferHandlerFactory {

  FileTransferHandler create(AppPathHandler manager);
}
