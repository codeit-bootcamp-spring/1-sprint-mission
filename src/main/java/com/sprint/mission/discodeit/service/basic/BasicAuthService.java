package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.AuthUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.login.LoginFailedException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto isUserExist(AuthUserRequest authUserRequest) {
    User targetUser = userRepository.findAll()
        .stream()
        .filter(user -> authUserRequest.name().equals(user.getUsername())
            && authUserRequest.password().equals(user.getPassword()))
        .findFirst()
        .orElseThrow(() -> new LoginFailedException(
            Map.of("Login 시도 유저 이름", authUserRequest.name())));

    return userMapper.toDto(targetUser);
  }
}