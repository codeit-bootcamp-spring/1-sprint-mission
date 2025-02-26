package com.sprint.mission.discodeit.dto.userstatus;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatusCreateRequestDto {
    private UUID userId;
    private Instant createdAt;

    public UserStatusCreateRequestDto(UUID userId, Instant createdAt) {
        this.userId = userId;
        this.createdAt = createdAt;
    }
}
