package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.security.auth.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final SessionRegistry sessionRegistry;

  @Override
  public void forceLogout(UUID userId) {
    List<Object> principals = sessionRegistry.getAllPrincipals();

    for (Object principal : principals) {
      if (principal instanceof DiscodeitUserDetails userDetails && userDetails.getUser().getId()
          .equals(userId)) {
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
        for (SessionInformation session : sessions) {
          session.expireNow();
        }
      }
    }
  }

  @Override
  public void printSessions() {
    List<Object> principals = sessionRegistry.getAllPrincipals();
    System.out.println("PRINTING SESSIONS: \n");
    for (Object principal : principals) {
      if (principal instanceof DiscodeitUserDetails userDetails) {
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
        boolean hasActiveSession = sessions.stream().anyMatch(s -> !s.isExpired());
        if (hasActiveSession) {
          System.out.println("User ID: " + userDetails.getUser().getId());
          System.out.println("Username: " + userDetails.getUsername());
          System.out.println("Active Sessions:");
          for (SessionInformation session : sessions) {
            System.out.println(" - Session ID: " + session.getSessionId());
            System.out.println("   Last Request: " + session.getLastRequest());
            System.out.println("   Expired: " + session.isExpired());
          }
        }
      }
    }
    System.out.println("FINISHED PRINTING SESSIONS: \n");

  }
}
