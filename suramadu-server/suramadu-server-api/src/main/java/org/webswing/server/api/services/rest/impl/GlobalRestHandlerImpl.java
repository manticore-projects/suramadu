package org.webswing.server.api.services.rest.impl;

import org.apache.commons.lang3.StringUtils;
import org.webswing.server.api.GlobalUrlHandler;
import org.webswing.server.api.model.ApplicationInfoMsg;
import org.webswing.server.api.services.application.AppPathHandler;
import org.webswing.server.api.services.rest.AbstractGlobalRestHandler;
import org.webswing.server.common.model.security.WebswingAction;
import org.webswing.server.common.util.ServerUtil;
import org.webswing.server.common.util.VariableSubstitutor;
import org.webswing.server.model.exception.WsException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class GlobalRestHandlerImpl extends AbstractGlobalRestHandler {

  private final GlobalUrlHandler handler;

  public GlobalRestHandlerImpl(GlobalUrlHandler parent) {
    super(parent);
    this.handler = parent;
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
      case "/apps": {
        try {
          List<ApplicationInfoMsg> apps = getApps();

          JsonMapper mapper = new JsonMapper();
          String appsJson = mapper.writeValueAsString(apps);

          res.setContentType("application/json");
          res.setCharacterEncoding("UTF-8");

          sendContent(res, appsJson);
          return true;
        } catch (JacksonException e) {
          throw new WsException(e);
        }
      }
      case "/refreshToken": {
        handler.refreshToken(req, res);
        return true;
      }
      case "/adminConsoleToken": {
        handler.issueAdminConsoleAccessToken(req, res);
        return true;
      }
      case "/adminConsoleAccess": {
        boolean hasAccess =
            getUser() != null && getUser().isPermitted(WebswingAction.rest_getConfig.name());
        sendContent(res, hasAccess + "");
        return true;
      }
      case "/adminConsoleUrl": {
        String url = VariableSubstitutor.basic().replace(handler.getConfig().getAdminConsoleUrl());
        if (StringUtils.isNotBlank(url)) {
          if (!url.endsWith("/")) {
            url += "/";
          }
          sendContent(res, url);
        }
        return true;
      }
    }

    return super.serve(req, res);
  }

  @Override
  protected boolean isOriginAllowed(String header) {
    if (super.isOriginAllowed(header)) {
      return true;
    }

    String url = VariableSubstitutor.basic().replace(handler.getConfig().getAdminConsoleUrl());
    if (StringUtils.isNotBlank(url) && url.toLowerCase().startsWith("http")
        && ServerUtil.domainFromUrl(url).equals(header)) {
      return true;
    }

    return false;
  }

  @Override
  protected List<ApplicationInfoMsg> getApps() throws WsException {
    handler.checkPermission(WebswingAction.rest_getApps);
    List<ApplicationInfoMsg> result = new ArrayList<>();
    for (AppPathHandler mgr : handler.getApplications()) {
      if (mgr.isEnabled() && mgr.isUserAuthorized()) {
        ApplicationInfoMsg applicationInfoMsg = mgr.getApplicationInfoMsg();
        if (applicationInfoMsg != null) {
          result.add(applicationInfoMsg);
        }
      }
    }
    return result;
  }

}
