package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.security.auth.DiscodeitUserDetails;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserOnlineStatusService {

  private final SessionRegistry sessionRegistry;

  public boolean isUserOnline(UUID userId) {
    return sessionRegistry.getAllPrincipals().stream()
        .filter(DiscodeitUserDetails.class::isInstance)
        .map(DiscodeitUserDetails.class::cast)
        .map(DiscodeitUserDetails::getUser)
        .anyMatch(user -> user.getId().equals(userId));
  }
}
