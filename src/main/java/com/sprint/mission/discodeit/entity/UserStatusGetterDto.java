package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public record UserStatusGetterDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserStatusGetterDto from(UserStatus userStatus) {
        return new UserStatusGetterDto(userStatus.getId(), userStatus.getCreatedAt(), userStatus.getUpdatedAt());
    }
}
