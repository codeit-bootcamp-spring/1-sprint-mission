package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.auth.InvalidPasswordException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Transactional(readOnly = true)
  @Override
  public UserDto login(LoginRequest loginRequest) {
    String username = loginRequest.username();
    String password = loginRequest.password();

    log.info("[로그인 시도] User: {}", username);

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> {
          log.error("[로그인 실패] user를 찾을 수 없습니다. User: {}", username);
          return new UserNotFoundException();
        });

    if (!user.getPassword().equals(password)) {
      log.error("[로그인 실패] 잘못된 비밀번호입니다. User: {}", username);
      throw new InvalidPasswordException();
    }

    log.info("[로그인 성공] User: {}", username);
    return userMapper.toDto(user);
  }
}
