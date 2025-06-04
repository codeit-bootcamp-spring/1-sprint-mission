package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserOnlineStatusService {

  private final SessionRegistry sessionRegistry;
  private final JwtSessionRepository jwtSessionRepository;

//  public boolean isUserOnline(UUID userId) {
//    return sessionRegistry.getAllPrincipals().stream()
//        .filter(DiscodeitUserDetails.class::isInstance)
//        .map(DiscodeitUserDetails.class::cast)
//        .map(DiscodeitUserDetails::getUser)
//        .anyMatch(user -> user.getId().equals(userId));
//  }

  public boolean isUserOnline(UUID userId) {
    return jwtSessionRepository.findByUser_Id(userId).isPresent(); // USER 가 unique 해야함
  }
}
