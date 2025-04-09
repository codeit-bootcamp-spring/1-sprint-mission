package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final UserStatusRepository userStatusRepository;

  public UserDto toDto(User user) {
    return UserDto.builder()
        .id(user.getId())
        .name(user.getUsername())
        .email(user.getEmail())
        .profile(user.getProfile())
        .online(getOnline(user))
        .build();
  }

  private Boolean getOnline(User user) {
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId());
    if (userStatus == null) {
      // 아직 생성되지 않은 경우(최초 User생성)는 기본값 false로 간주
      return false;
    }
    return userStatus.isOnline();
  }
}
