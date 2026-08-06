package org.webswing.server.api.services.rest;

import org.webswing.server.api.base.AbstractUrlHandler;
import org.webswing.server.api.base.UrlHandler;
import org.webswing.server.api.model.ApplicationInfoMsg;
import org.webswing.server.model.exception.WsException;

import java.util.List;

public abstract class AbstractGlobalRestHandler extends AbstractUrlHandler implements UrlHandler {

  public AbstractGlobalRestHandler(UrlHandler parent) {
    super(parent);
  }

  protected abstract List<ApplicationInfoMsg> getApps() throws WsException;

}
