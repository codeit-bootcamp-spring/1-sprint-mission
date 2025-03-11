package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

@Builder
public record UserStatusResponse(
    UUID id,
    UUID userId,
    Instant lastActiveAt
) {

//  public static UserStatusResponse entityToDto(UserStatus userStatus) {
//    return UserStatusResponse.builder()
//        .id(userStatus.getId())
//        .userId(userStatus.getUser().getId())
//        .lastActiveAt(userStatus.getLastActiveAt())
//        .build();
//  }
}
