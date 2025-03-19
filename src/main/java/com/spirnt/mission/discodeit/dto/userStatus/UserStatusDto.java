package com.spirnt.mission.discodeit.dto.userStatus;

import com.spirnt.mission.discodeit.enity.UserStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserStatusDto {

  private UUID id;
  private UUID userId;
  private Instant lastActiveAt;

  @Builder
  public UserStatusDto(UUID id, UUID userId, Instant lastActiveAt) {
    this.id = id;
    this.userId = userId;
    this.lastActiveAt = lastActiveAt;
  }

  public static UserStatusDto from(UserStatus userStatus) {
    return UserStatusDto.builder()
        .id(userStatus.getId())
        .userId(userStatus.getUser().getId())
        .lastActiveAt(userStatus.getLastActiveAt())
        .build();
  }
}
