package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.domain.Status;
import com.sprint.mission.discodeit.domain.UserStatus;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(UUID id, UUID userId, Instant createdAt,Instant updatedAt,Status status) {
    public UserStatusDto(UserStatus userStatus) {
        this(userStatus.getId(), userStatus.getUserId(), userStatus.getCreatedAt(), userStatus.getUpdatedAt(), userStatus.getStatus());
    }
}
