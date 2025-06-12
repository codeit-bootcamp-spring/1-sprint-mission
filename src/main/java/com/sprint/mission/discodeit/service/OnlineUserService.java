package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnlineUserService {

  private final JwtSessionRepository sessionRepository;

  public boolean isUserOnline(User user) {
    if (user == null) {
      return false;
    }

    List<JwtSession> jwtSessions = sessionRepository.findByUser(user);

    return !jwtSessions.isEmpty(); // 해당 사용자의 세션이 없음 = 오프라인
  }
}
