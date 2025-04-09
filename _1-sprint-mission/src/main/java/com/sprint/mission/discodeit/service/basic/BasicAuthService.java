package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.login.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserDuplicatedUsernameException;
import com.sprint.mission.discodeit.exception.user.UserPasswordIncorrect;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Transactional(readOnly = true)
  @Override
  public UserDto login(LoginRequest loginRequest) {
    User user = userRepository.findByUsername(loginRequest.username())
        .orElseThrow(
            () -> new UserDuplicatedUsernameException(ErrorCode.USER_DUPLICATE_USERNAME,
                Map.of("username", loginRequest.username())));
    if (!user.getPassword().equals(loginRequest.password())) {
      throw new UserPasswordIncorrect(ErrorCode.USER_PASSWORD_INCORRECT,
          Map.of("password", loginRequest.password()));
    }
    return userMapper.toDto(user);
  }
}
