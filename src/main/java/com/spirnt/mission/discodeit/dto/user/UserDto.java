package com.spirnt.mission.discodeit.dto.user;

import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
// 유저 정보를 읽기 위한 DTO
public class UserDto {

  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  private String username;
  private String email;
  private Boolean online;
  private UUID profileId;

  public UserDto(User user, UserStatus userStatus) {
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.id = user.getId();
    this.createdAt = user.getCreatedAt();
    this.updatedAt = user.getUpdatedAt();
    this.profileId = user.getProfileId();
    this.online = userStatus.isOnline() ? true : false;
  }
}
