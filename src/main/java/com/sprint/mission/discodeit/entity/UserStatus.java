package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus {
    private final UUID id;
    private final Instant createdAt;
    private Instant updateAt;

    private UUID userId;

    private UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        updateAt = createdAt;
        this.userId = userId;
    }

    public static UserStatus from(UUID userId) {
        return new UserStatus(userId);
    }

    public boolean isOnline() {
        long minutes = Duration.between(Instant.now(), updateAt).toMinutes();
        return minutes <= 5;
    }

}
