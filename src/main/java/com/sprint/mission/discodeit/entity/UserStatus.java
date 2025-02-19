package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastActivityAt;
    private final UUID userId;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.lastActivityAt = Instant.now();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void updateLastActivityAt(Instant lastActivityAt) {
        this.lastActivityAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public boolean isCurrentlyLoggedIn() {
        return lastActivityAt.isAfter(Instant.now().minus(Duration.ofMinutes(5)));
    }

}
