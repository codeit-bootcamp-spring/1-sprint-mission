package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.auth.UserLoginDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;

  @Override
  public UserDto login(UserLoginDto userLoginDto) throws CustomException {
    if (userLoginDto == null || userLoginDto.username() == null
        || userLoginDto.password() == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA);
    }
    User user = userRepository.findByUsername(userLoginDto.username()).orElse(null);
    if (user == null || !user.getPassword().equals(userLoginDto.password())) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    // 이것도 마찬가지로 어할 수 없는 값이라 이 방식을 쓰면 안되는지?
    UserStatusDto userStatusDto = userStatusService.updateByUserId(user.getId().toString(),
        new UpdateUserStatusDto(Instant.now()));

    return UserDto.from(user, user.getUserStatus().isActive());
  }
}
