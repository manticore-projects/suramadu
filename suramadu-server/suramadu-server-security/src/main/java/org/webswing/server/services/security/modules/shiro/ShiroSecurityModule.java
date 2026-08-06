package org.webswing.server.services.security.modules.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.config.Ini;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.env.Environment;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.common.service.security.AuthenticatedWebswingUser;
import org.webswing.server.services.security.api.WebswingAuthenticationException;
import org.webswing.server.services.security.modules.AbstractUserPasswordSecurityModule;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;

public class ShiroSecurityModule
    extends AbstractUserPasswordSecurityModule<ShiroSecurityModuleConfig> {
  private final static Logger log = LoggerFactory.getLogger(ShiroSecurityModule.class);


  private volatile SecurityManager securityManager;
  private ShiroConfigMonitor configMonitor;

  // Reflection handle for the protected AuthorizingRealm.getAuthorizationInfo(PrincipalCollection).
  // Shiro exposes no public API to enumerate a principal's roles, so this is the supported route
  // to fully dynamic role discovery. Resolved once; null means discovery is unavailable and the
  // module fails CLOSED (grants no roles) rather than guessing.
  private static final Method GET_AUTHORIZATION_INFO;

  static {
    Method m = null;
    try {
      m = AuthorizingRealm.class.getDeclaredMethod("getAuthorizationInfo",
          PrincipalCollection.class);
      m.setAccessible(true);
    } catch (NoSuchMethodException | RuntimeException e) {
      log.error("Dynamic role discovery unavailable: cannot access "
          + "AuthorizingRealm.getAuthorizationInfo(PrincipalCollection). "
          + "All logins will resolve to zero roles (fail-closed) until this is corrected.", e);
    }
    GET_AUTHORIZATION_INFO = m;
  }

  public ShiroSecurityModule(ShiroSecurityModuleConfig config) {
    super(config);
  }

  @Override
  public void init() {
    super.init();
    String iniFileName = resolvePath(getConfig().getFile());

    internalInit(iniFileName);

    if (getConfig().isHotReload() && iniFileName != null) {
      // Monitor uses a pool of 4 threads for background watching and reload tasks
      this.configMonitor = new ShiroConfigMonitor(iniFileName, this::internalInit);
      this.configMonitor.startWatching();
    }
  }

  private void internalInit(String iniFileName) {
    try {
      // Shiro 3 Ini.loadFromPath() routes through URI.toURL() and rejects schemeless paths.
      // Load the InputStream directly to bypass Shiro's resource-prefix resolver.
      Ini ini = new Ini();
      try (InputStream is = Files.newInputStream(Path.of(iniFileName))) {
        ini.load(is);
      }

      // Modern Shiro Environment setup (Non-deprecated)
      Environment env = new BasicIniEnvironment(ini);
      SecurityManager newManager = env.getSecurityManager();


      // Atomic swap for thread safety
      this.securityManager = newManager;
      SecurityUtils.setSecurityManager(newManager);

      log.info("Shiro configuration (re)loaded from {}. Dynamic role discovery active.",
          iniFileName);
    } catch (Exception ex) {
      log.error("Failed to load Shiro configuration from {}. Previous state remains active.",
          iniFileName, ex);
    }
  }

  @Override
  public AuthenticatedWebswingUser verifyUserPassword(String user, String password)
      throws WebswingAuthenticationException {
    String originalPassword = deobfuscate(password);
    SecurityManager currentMgr = this.securityManager;

    if (currentMgr != null) {
      UsernamePasswordToken token = new UsernamePasswordToken(user, originalPassword);

      AuthenticationInfo authInfo;
      try {
        authInfo = currentMgr.authenticate(token);
      } catch (UnknownAccountException e) {
        log.warn("Authentication failed for user '{}': unknown account.", user);
        throw new WebswingAuthenticationException(
            "Unknown user '" + user + "'. Please check your username.",
            WebswingAuthenticationException.INVALID_USER_OR_PASSWORD);
      } catch (IncorrectCredentialsException e) {
        log.warn("Authentication failed for user '{}': incorrect credentials.", user);
        throw new WebswingAuthenticationException("Incorrect password for user '" + user + "'.",
            WebswingAuthenticationException.INVALID_USER_OR_PASSWORD);
      } catch (LockedAccountException e) {
        log.warn("Authentication failed for user '{}': account locked.", user);
        throw new WebswingAuthenticationException(
            "Account '" + user + "' is locked. Please contact your administrator.",
            WebswingAuthenticationException.NO_ACCESS);
      } catch (DisabledAccountException e) {
        log.warn("Authentication failed for user '{}': account disabled.", user);
        throw new WebswingAuthenticationException(
            "Account '" + user + "' is disabled. Please contact your administrator.",
            WebswingAuthenticationException.NO_ACCESS);
      } catch (ExcessiveAttemptsException e) {
        log.warn("Authentication failed for user '{}': too many attempts.", user);
        throw new WebswingAuthenticationException(
            "Too many failed login attempts for '" + user + "'. Please try again later.",
            WebswingAuthenticationException.FAILED_TO_AUTHENTICATE);
      } catch (AuthenticationException e) {
        String detail = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
        log.error("Authentication failed for user '{}': {}", user, detail);
        throw new WebswingAuthenticationException(
            "Authentication failed for '" + user + "': " + detail,
            WebswingAuthenticationException.FAILED_TO_AUTHENTICATE);
      }

      PrincipalCollection principals = authInfo.getPrincipals();

      // Fully dynamic discovery: collect exactly the roles the realm(s) assign this principal.
      // Fail-CLOSED by construction -- any failure yields an empty set, never a fallback grant.
      Set<String> authorizedRoles = discoverRoles(currentMgr, principals, user);

      if (authorizedRoles.isEmpty()) {
        log.warn("User '{}' authenticated but resolved to NO roles. Verify the [users]/[roles] "
            + "sections of the Shiro INI assign this account at least one role. Access denied "
            + "at the role level (no fallback grant).", user);
      }

      log.info("User {} authenticated. Resolved {} role(s): {}", user, authorizedRoles.size(),
          authorizedRoles);
      return new ShiroWebswingUser(user, password, new ArrayList<>(authorizedRoles));
    }

    throw new WebswingAuthenticationException("Security Manager not initialized.",
        WebswingAuthenticationException.CONFIG_ERROR);
  }

  /**
   * Enumerates every role assigned to the given principals by interrogating each
   * {@link AuthorizingRealm} managed by the {@link SecurityManager}. Returns whatever the realms
   * actually grant -- no hardcoded role universe. Any failure (missing reflection handle, wrong
   * SecurityManager type, no authorizing realm, or a realm throwing) is logged at ERROR and yields
   * no roles from the affected source. This method never escalates on error.
   */
  private Set<String> discoverRoles(SecurityManager mgr, PrincipalCollection principals,
      String user) {
    Set<String> roles = new LinkedHashSet<>();

    if (GET_AUTHORIZATION_INFO == null) {
      log.error("Cannot resolve roles for user '{}': role-discovery reflection handle is "
          + "unavailable. Granting no roles (fail-closed).", user);
      return roles;
    }

    if (!(mgr instanceof RealmSecurityManager rsm)) {
      log.error(
          "Cannot resolve roles for user '{}': SecurityManager is not a "
              + "RealmSecurityManager ({}). Granting no roles (fail-closed).",
          user, mgr.getClass().getName());
      return roles;
    }

    boolean queriedAnyRealm = false;
    for (Realm realm : rsm.getRealms()) {
      if (realm instanceof AuthorizingRealm) {
        queriedAnyRealm = true;
        try {
          Object result = GET_AUTHORIZATION_INFO.invoke(realm, principals);
          if (result instanceof AuthorizationInfo info && info.getRoles() != null) {
            roles.addAll(info.getRoles());
          }
        } catch (Exception e) {
          // Surface loudly; never escalate on failure.
          log.error("Role discovery failed on realm '{}' for user '{}'. "
              + "Granting no roles from this realm (fail-closed).", realm.getName(), user, e);
        }
      }
    }

    if (!queriedAnyRealm) {
      log.error("Cannot resolve roles for user '{}': no AuthorizingRealm present in the "
          + "SecurityManager. Granting no roles (fail-closed).", user);
    }

    return roles;
  }

  private String resolvePath(String path) {
    if (path == null) {
      return null;
    }
    String homePath = System.getProperty("user.home");
    String rootPath = System.getProperty(Constants.ROOT_DIR_PATH, "");
    return path.replaceFirst("^~", Matcher.quoteReplacement(homePath))
        .replaceFirst("\\$\\{" + Constants.ROOT_DIR_PATH + "}", Matcher.quoteReplacement(rootPath))
        .replaceFirst("\\$\\{user.home}", Matcher.quoteReplacement(homePath));
  }

  @Override
  public void destroy() {
    if (this.configMonitor != null) {
      this.configMonitor.shutdown();
    }
    super.destroy();
  }

  String deobfuscate(String encoded) {
    try {
      byte[] decodedBytes = Base64.getDecoder().decode(encoded);
      String decoded = new String(decodedBytes, StandardCharsets.UTF_8);
      if (decoded.length() < 8 || !decoded.matches(".*\\d{8}$")) {
        return encoded;
      }
      String digits = decoded.substring(decoded.length() - 8);
      String front = decoded.substring(0, decoded.length() - 8);
      int seed = Integer.parseInt(digits);
      Mulberry32 rng = new Mulberry32(seed);
      List<Integer> indices = new ArrayList<>();
      for (int i = 0; i < front.length(); i++) {
        indices.add(i);
      }
      for (int i = indices.size() - 1; i > 0; i--) {
        float rand = rng.next();
        int j = (int) Math.floor(rand * (i + 1));
        Collections.swap(indices, i, j);
      }
      char[] shuffled = front.toCharArray();
      char[] unshuffled = new char[shuffled.length];
      for (int i = 0; i < indices.size(); i++) {
        unshuffled[indices.get(i)] = shuffled[i];
      }
      return new String(unshuffled) + digits;
    } catch (IllegalArgumentException ex) {
      return encoded;
    }
  }

  static class Mulberry32 {
    private int state;

    public Mulberry32(int seed) {
      this.state = seed;
    }

    public float next() {
      int t = state += 0x6D2B79F5;
      t = (t ^ (t >>> 15)) * (t | 1);
      t ^= t + ((t ^ (t >>> 7)) * (t | 61));
      t = t ^ (t >>> 14);
      return ((long) t & 0xFFFFFFFFL) / (float) (1L << 32);
    }
  }
}
