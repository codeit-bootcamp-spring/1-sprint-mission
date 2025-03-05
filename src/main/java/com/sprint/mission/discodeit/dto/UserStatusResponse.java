package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserStatusResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    Instant lastActiveAt,
    Boolean online
) {

  public static UserStatusResponse entityToDto(UserStatus userStatus) {
    return UserStatusResponse.builder()
        .id(userStatus.getId())
        .createdAt(userStatus.getCreatedAt())
        .updatedAt(userStatus.getUpdatedAt())
        .userId(userStatus.getUserId())
        .lastActiveAt(userStatus.getLastActiveAt())
        .online(userStatus.getIsOnline())
        .build();
  }

}
