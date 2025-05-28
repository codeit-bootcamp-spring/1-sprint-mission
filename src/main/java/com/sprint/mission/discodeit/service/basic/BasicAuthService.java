package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.auth.LoginFailedException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserService userService;

  @Transactional
  @Override
  public UserResponseDto login(LoginRequest loginDTO) {
    if (userRepository.existsUserByUsernameAndPassword(loginDTO.username(), loginDTO.password())) {
      // 유저가 존재하는지 찾기
      User user = userRepository.findByUsername(loginDTO.username());
      // 로그인 했으니 시간 업데이트
      return userMapper.toResponseDto(user, userService.isUserOnline(user.getUsername()));
    }
    throw new LoginFailedException(loginDTO.username());
  }
}
