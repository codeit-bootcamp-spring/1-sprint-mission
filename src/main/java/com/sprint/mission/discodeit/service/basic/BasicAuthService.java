package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.login.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.InvalidCredentialsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;
  private final UserMapper userMapper;

  @Transactional
  @Override
  public UserDto login(LoginRequest loginRequest) {
    log.debug("로그인 시도: username={}", loginRequest.username());

    String username = loginRequest.username();
    String password = loginRequest.password();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> UserNotFoundException.withUsername(username));

    if (!user.getPassword().equals(password)) {
      throw InvalidCredentialsException.wrongPassword();
    }

    userStatusService.updateByUserId(user.getId(), new UserStatusUpdateRequest(Instant.now()));
    log.info("로그인 성공: userId={}, username={}", user.getId(), username);
    return userMapper.toDto(user);
  }
}
