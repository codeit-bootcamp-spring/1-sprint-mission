package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CustomUserDetails;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnlineUserService {

  private final SessionRegistry sessionRegistry;

  public boolean isUserOnline(User user) {
    if (user == null) {
      return false;
    }

    // SessionRegistry에서 모든 Principal 조회
    List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

    // 해당 사용자의 Principal 찾기
    for (Object principal : allPrincipals) {
      if (principal instanceof CustomUserDetails) {
        CustomUserDetails userDetails = (CustomUserDetails) principal;
        if (user.getUsername().equals(userDetails.getUsername())) {
          // 해당 사용자의 활성 세션이 있는지 확인
          List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
          return !sessions.isEmpty();
        }
      }
    }

    return false; // 해당 사용자의 세션이 없음 = 오프라인
  }
}
