package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID userId;
    private final Instant createdAt;
    private Instant updateAt;

    private UserStatus(UUID userId) {
        this.createdAt = Instant.now();
        this.updateAt = createdAt;
        this.userId = userId;
    }

    public static UserStatus from(UUID userId) {
        return new UserStatus(userId);
    }

    public boolean isOnline() {
        long minutes = Duration.between(updateAt, Instant.now()).toMinutes();
        return minutes <= 5;
    }

    public void setUpdateAt() {
        updateAt = Instant.now();
    }
}
