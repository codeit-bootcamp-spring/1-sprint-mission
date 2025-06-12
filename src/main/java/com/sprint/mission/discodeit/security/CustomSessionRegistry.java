package com.sprint.mission.discodeit.security;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Component;

@Component
public class CustomSessionRegistry extends SessionRegistryImpl {

  private final Map<String, Object> usernameToPrincipal = new ConcurrentHashMap<>();

  @Override
  public void registerNewSession(String sessionId, Object principal) {
    super.registerNewSession(sessionId, principal);

    if (principal instanceof DiscodeitUserDetails userDetails) {
      usernameToPrincipal.put(userDetails.getUsername(), principal);
    }
  }

  public Optional<Object> findPrincipalByUsername(String username) {
    return Optional.ofNullable(usernameToPrincipal.get(username));
  }
}