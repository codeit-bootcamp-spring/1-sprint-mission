package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.auth.LoginRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.AuthService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;

  private final UserStatusService userStatusService;

  @Override
  public UserDto login(LoginRequest loginRequest) {
    User user = userRepository.findByUsername(loginRequest.username())
        .orElseThrow(() -> new NoSuchElementException(
            "User with username " + loginRequest.username() + " not found"));
    if (!user.getPassword().equals(loginRequest.password())) {
      throw new IllegalArgumentException("Wrong password");
    }
    // 로그인 성공 시 온라인 상태로 업데이트
    userStatusService.updateByUserId(user.getId(), new UserStatusUpdateRequest(Instant.now()));
    return userMapper.toDto(user);
  }
}
