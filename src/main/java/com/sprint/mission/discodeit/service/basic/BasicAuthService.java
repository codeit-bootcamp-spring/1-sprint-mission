package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.InvalidCredentialsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional(readOnly = true)
  public UserDto login(LoginRequest request) {

    log.debug("로그인 시도: username={}", request.username());

    String username = request.username();
    String password = request.password();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> UserNotFoundException.withUsername(username));

    if (!user.getPassword().equals(password)) {
      throw InvalidCredentialsException.wrongPassword();
    }

    log.info("로그인 성공: userId={}, username={}", user.getId(), user.getUsername());

    return userMapper.toDto(user);
  }
}