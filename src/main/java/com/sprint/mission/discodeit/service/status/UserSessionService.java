package com.sprint.mission.discodeit.service.status;

import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSessionService {

  private final UserRepository userRepository;
  private final JwtSessionRepository jwtSessionRepository;
  private final JwtService jwtService;

  public boolean isOnline(String username) {

    User user = userRepository.findUserByUsername(username);
    if (user == null) {
      throw new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, Map.of(
          ErrorCode.USER_NOT_FOUND.getCode(),
          ErrorCode.USER_NOT_FOUND.getMessage()
      ));
    }

    JwtSession jwtSession = jwtSessionRepository.findFirstByUser_Id(user.getId())
        .orElse(null);

    if (jwtSession == null) {
      return false;
    }

    String accessToken = jwtSession.getAccessToken();
    if (accessToken == null || !jwtService.validate(accessToken)) {
      return false;
    }

    return jwtSessionRepository.existsByAccessToken(accessToken);
  }
}
