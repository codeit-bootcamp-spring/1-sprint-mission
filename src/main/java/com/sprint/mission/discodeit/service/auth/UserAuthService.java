package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.entity.auth.RequestLogin;
import com.sprint.mission.discodeit.entity.auth.ResponseLogin;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserAuthException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.entitymapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthService implements AuthService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public ResponseLogin login(RequestLogin request) {
    User findUser = userRepository.findUserByUsername(request.username());

    if (findUser == null) {
      log.warn("잘못된 유저 로그인 시도: {}", request.username());
      throw new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
          Map.of(request.username(), ErrorCode.USER_NOT_FOUND.getMessage())
      );
    }

    if (!findUser.getUsername().equals(request.username()) || !findUser.getPassword()
        .equals(request.password())) {
      log.warn("로그인 실패: {}", request.username());
      throw new UserAuthException(Instant.now(), ErrorCode.USER_AUTH_FAIL,
          Map.of(request.username(), ErrorCode.USER_AUTH_FAIL.getMessage())
      );
    }

    log.info("로그인: {}", request.username());
    return ResponseLogin.builder()
        .id(findUser.getId())
        .username(findUser.getUsername())
        .email(findUser.getEmail())
        .profile(BinaryContentMapper.toDto(findUser.getProfile()))
        .online(true)
        .build();
  }
}
