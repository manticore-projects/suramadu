package org.webswing.server.services.security.modules.embeded;

import org.apache.commons.lang3.StringUtils;
import org.webswing.server.common.service.security.AuthenticatedWebswingUser;
import org.webswing.server.services.security.api.WebswingAuthenticationException;
import org.webswing.server.services.security.modules.AbstractUserPasswordSecurityModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmbededSecurityModule
    extends AbstractUserPasswordSecurityModule<EmbededSecurityModuleConfig> {

  public Map<String, EmbededWebswingUser> userMap = new HashMap<>();

  public EmbededSecurityModule(EmbededSecurityModuleConfig config) {
    super(config);
  }

  @Override
  public void init() {
    super.init();
    for (EmbededUserEntry u : getConfig().getUsers()) {
      String user = getConfig().getContext().replaceVariables(u.getUsername());
      String password = getConfig().getContext().replaceVariables(u.getPassword());
      List<String> roles = new ArrayList<>();
      for (String r : u.getRoles()) {
        roles.add(getConfig().getContext().replaceVariables(r));
      }
      userMap.put(user, new EmbededWebswingUser(user, password, roles));
    }
  }

  @Override
  public AuthenticatedWebswingUser verifyUserPassword(String user, String password)
      throws WebswingAuthenticationException {
    if (userMap.containsKey(user)) {
      EmbededWebswingUser current = userMap.get(user);
      if (current.getPassword().startsWith(HashUtil.PREFIX)) {
        if (HashUtil.authenticate(password.toCharArray(), current.getPassword())) {
          return current;
        }
      } else {
        if (StringUtils.equals(password, current.getPassword())) {
          return current;
        }
      }
    }
    throw new WebswingAuthenticationException("Invalid Username or Password",
        WebswingAuthenticationException.INVALID_USER_OR_PASSWORD);

  }

}
