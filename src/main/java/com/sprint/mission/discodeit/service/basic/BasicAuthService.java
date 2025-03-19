package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto login(LoginRequest request) {
    User user = userRepository.findByUsername(request.username())
        .orElseThrow(() -> new NoSuchElementException("[ERROR] 잘못된 정보입니다."));
    if (!user.isSamePassword(request.password())) {
      throw new NoSuchElementException("[ERROR] 잘못된 정보입니다.");
    }

    return userMapper.toDto(user);
  }
}
