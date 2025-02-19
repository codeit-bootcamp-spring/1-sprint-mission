package com.sprint.mission.discodeit.entity.Dto;

import com.sprint.mission.discodeit.entity.Type.UserStatusType;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        UserStatusType statusType
) {
    public static UserStatusDto from(UserStatus userStatus) {
        return new UserStatusDto(userStatus.getUserId(), userStatus.getCreatedAt(), userStatus.getUpdatedAt(), userStatus.getStatusType());
    }
}
