package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.CustomUserDetails;
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

    if (principal instanceof CustomUserDetails) {
      CustomUserDetails userDetails = (CustomUserDetails) principal;
      usernameToPrincipal.put(userDetails.getUsername(), principal);
    }
  }

  public Optional<Object> findPrincipalByUsername(String username) {
    return Optional.ofNullable(usernameToPrincipal.get(username));
  }
}