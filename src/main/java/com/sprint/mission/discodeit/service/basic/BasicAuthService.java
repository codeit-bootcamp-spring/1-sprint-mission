package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.auth.AuthenticationFailedException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserResponse login(UserRequest.Login request) {
    User findUser = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND,
            Map.of("userName", request.getUsername())));

    if (!findUser.getPassword().equals(request.getPassword())) {
      throw new AuthenticationFailedException(ErrorCode.LOGIN_FAILED,
          Map.of("userName", request.getUsername()));
    }

    log.info("Login success - userId: {}", findUser.getId());
    return userMapper.entityToDto(findUser);
  }
}
