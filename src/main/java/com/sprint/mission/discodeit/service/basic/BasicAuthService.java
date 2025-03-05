package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.LoginRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByUserIdRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.util.TimeUtil;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserValidator userValidator;
  private final UserStatusService userStatusService;
  private final UserMapper userMapper;

  @Override
  public UserDto login(LoginRequest loginRequest) {
    User user = userValidator.validateUserExistsByNameAndPassword(loginRequest.username(),
        loginRequest.password());

    // UserStatus 로그인 여부 변경
    UpdateUserStatusByUserIdRequest updateUserStatusByUserIdRequest =
        new UpdateUserStatusByUserIdRequest(TimeUtil.getCurrentTime());
    userStatusService.updateUserStatusByUserId(user.getId(), updateUserStatusByUserIdRequest);

    UUID profileId = null;
    if (user.getProfileImage() != null) {
      profileId = user.getProfileImage().getId();
    }

    return userMapper.toUserDto(user, profileId);
  }
}
