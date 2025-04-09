package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.AuthUserDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.LoginFailedException;
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
  public UserDto isUserExist(AuthUserDTO authUserDTO) {
    try {
      User targetUser = userRepository.findAll()
          .stream()
          .filter(user -> authUserDTO.name().equals(user.getUsername())
              && authUserDTO.password().equals(user.getPassword()))
          .findFirst()
          .orElseThrow(() -> new LoginFailedException(
              Map.of("Login 시도 유저 이름: ", authUserDTO.name())));

      return userMapper.toDto(targetUser);

    } catch (LoginFailedException e) {
      // 예외 처리 로직 (로깅이나 다른 예외로 감싸기 등)
      throw new RuntimeException(e); // 또는 다른 사용자 정의 예외로 감싸서 던질 수도 있어
    }
  }
}
