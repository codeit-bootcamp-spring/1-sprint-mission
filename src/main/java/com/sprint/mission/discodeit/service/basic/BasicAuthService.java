package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.AuthUserDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.LoginFailedException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto isUserExist(AuthUserDTO authUserDTO) {

    User targetUser = userRepository.findAll()
        .stream()
        .filter(user -> authUserDTO.name().equals(user.getUsername())
            && authUserDTO.password().equals(user.getPassword()))
        .findFirst()
        .orElseThrow(() -> new LoginFailedException("로그인 실패"));

    return userMapper.toDto(targetUser);
  }


}
