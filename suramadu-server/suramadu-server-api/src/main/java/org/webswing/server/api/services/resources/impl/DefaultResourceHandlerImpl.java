package org.webswing.server.api.services.resources.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.api.base.PrimaryUrlHandler;
import org.webswing.server.api.model.Manifest;
import org.webswing.server.api.model.ManifestIcons;
import org.webswing.server.api.services.resources.AbstractResourceHandler;
import org.webswing.server.api.services.resources.WebResourceProvider;
import org.webswing.server.common.model.SecuredPathConfig;
import org.webswing.server.common.model.security.WebswingAction;
import org.webswing.server.model.exception.WsException;
import tools.jackson.databind.json.JsonMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class DefaultResourceHandlerImpl extends AbstractResourceHandler {

  private static final Logger log = LoggerFactory.getLogger(DefaultResourceHandlerImpl.class);

  private final PrimaryUrlHandler parent;

  public DefaultResourceHandlerImpl(PrimaryUrlHandler parent,
      WebResourceProvider webResourceProvider) {
    super(parent, webResourceProvider);
    this.parent = parent;
  }

  @Override
  protected LookupResult lookupNoCache(HttpServletRequest req, String path) {
    if ("/manifest.json".equals(path)) {
      return new ManifestResult(parent);
    }

    if (isRestrictedAccess(path)) {
      return new ErrorResult(HttpServletResponse.SC_UNAUTHORIZED, "Access restricted.");
    }

    return super.lookupNoCache(req, path);
  }

  private boolean isRestrictedAccess(String path) {
    String lpath = path.toLowerCase();
    for (String restricted : this.parent.getConfig().getRestrictedResources()) {
      if (!restricted.trim().isEmpty() && lpath.startsWith(restricted.toLowerCase())) {
        try {
          checkPermission(WebswingAction.master_basic_access);
        } catch (WsException e) {
          log.warn(
              "Accessing restricted resource path. Path '{}' requires authentication. (matches restricted prefix '{}')",
              path, restricted);
          return true;
        }
      }
    }
    return false;
  }

  private static class ManifestResult implements LookupResult {

    private final PrimaryUrlHandler handler;

    public ManifestResult(PrimaryUrlHandler handler) {
      this.handler = handler;
    }

    @Override
    public boolean respondGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      Manifest manifest = getManifest(handler);

      JsonMapper mapper = new JsonMapper();
      String manifestJson = mapper.writeValueAsString(manifest);

      resp.setContentType("application/json");
      resp.setCharacterEncoding("UTF-8");

      sendContent(resp, manifestJson);

      return true;
    }

    @Override
    public boolean respondHead(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
      return false;
    }

    @Override
    public long getLastModified() {
      return System.currentTimeMillis();
    }

    private void sendContent(HttpServletResponse res, String content) throws IOException {
      try (PrintWriter writer = res.getWriter()) {
        writer.write(content);
        writer.flush();
      }
    }

    private Manifest getManifest(PrimaryUrlHandler handler) {
      // must be accessible without login
      SecuredPathConfig config = handler.getConfig();

      String color = "#FFFFFF";

      Manifest manifest = new Manifest();
      manifest.setName(config.getName());
      manifest.setShortName(manifest.getName());
      ManifestIcons icon = new ManifestIcons();
      icon.setSrc(handler.getFullPathMapping() + "/rest/appicon");
      icon.setSizes("256x256"); // this is just a hardcoded value, not real size
      manifest.setIcons(List.of(icon));
      manifest.setStartUrl(handler.getFullPathMapping());
      manifest.setScope(handler.getFullPathMapping());
      manifest.setBackgroundColor(color);
      manifest.setDisplay("fullscreen");
      manifest.setThemeColor(color);

      return manifest;
    }

  }

}
