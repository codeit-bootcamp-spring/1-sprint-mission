package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.auth.AuthLoginDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.auth.LoginFailedException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto login(AuthLoginDTO dto) {
    User loginUser = userRepository.findAll().stream()
        .filter(user -> user.getUsername().equals(dto.getUsername())
            && user.getPassword().equals(dto
            .getPassword())).findFirst()
        .orElseThrow(() -> new LoginFailedException(dto.getUsername()));
    return userMapper.toDto(loginUser);
  }
}
