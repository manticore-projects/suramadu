package org.webswing.server.api.services.rest.impl;

import org.apache.commons.lang3.StringUtils;
import org.webswing.server.api.GlobalUrlHandler;
import org.webswing.server.api.base.PrimaryUrlHandler;
import org.webswing.server.api.services.rest.AbstractAppRestHandler;
import org.webswing.server.common.util.ServerUtil;
import org.webswing.server.common.util.VariableSubstitutor;
import org.webswing.server.model.exception.WsException;
import org.webswing.util.GitRepositoryState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URISyntaxException;

public class AppRestHandlerImpl extends AbstractAppRestHandler {

  private static final String default_version = "unresolved";

  private final PrimaryUrlHandler parent;
  private final GlobalUrlHandler global;

  public AppRestHandlerImpl(PrimaryUrlHandler parent, GlobalUrlHandler global) {
    super(parent);
    this.parent = parent;
    this.global = global;
  }

  @Override
  protected String getPath() {
    return "rest";
  }

  @Override
  public boolean serve(HttpServletRequest req, HttpServletResponse res) throws WsException {
    handleCorsHeaders(req, res);

    String path = getPathInfo(req);

    switch (path) {
      case "/version": {
        sendContent(res, getVersion());
        return true;
      }
      case "/appicon": {
        res.setHeader("Cache-Control", "public, max-age=120");
        res.setContentType("image/png");
        sendFile(res, getAppIcon());
        return true;
      }
      case "/ping": {
        res.setStatus(HttpServletResponse.SC_OK);
        return true;
      }
      case "/refreshToken": {
        if ("OPTIONS".equals(req.getMethod())) {
          // cors preflight, don't refresh token
          return true;
        }

        parent.refreshToken(req, res);
        return true;
      }
    }

    return super.serve(req, res);
  }

  @Override
  protected String getVersion() throws WsException {
    String describe = GitRepositoryState.getInstance().getDescribe();
    if (describe == null) {
      return default_version;
    }
    return describe;
  }

  @Override
  protected File getAppIcon() throws WsException {
    File icon = parent.resolveFile(parent.getConfig().getIcon());
    if (icon == null) {
      try {
        icon = new File(
            AppRestHandlerImpl.class.getClassLoader().getResource("images/java.png").toURI());
      } catch (URISyntaxException e) {
        // ignore
      }
    }
    return icon;
  }

  @Override
  protected void ping() throws WsException {}

  @Override
  protected boolean isOriginAllowed(String header) {
    if (super.isOriginAllowed(header)) {
      return true;
    }

    String url = VariableSubstitutor.basic().replace(global.getConfig().getAdminConsoleUrl());
    if (StringUtils.isNotBlank(url) && url.toLowerCase().startsWith("http")
        && ServerUtil.domainFromUrl(url).equals(header)) {
      return true;
    }

    return false;
  }

}
