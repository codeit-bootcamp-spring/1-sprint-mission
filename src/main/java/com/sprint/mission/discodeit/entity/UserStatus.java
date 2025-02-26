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
    private boolean online;

    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.lastActivityAt = lastActiveAt != null ? lastActiveAt : Instant.now();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.online = true;
    }

    public void updateLastActivityAt(Instant lastActivityAt) {
        this.lastActivityAt = lastActivityAt != null ? lastActivityAt : Instant.now();
        this.updatedAt = Instant.now();
        this.online = true;
    }

    public boolean isCurrentlyLoggedIn() {
        return lastActivityAt.isAfter(Instant.now().minus(Duration.ofMinutes(5)));
    }

    public boolean isOnline() {
        return isCurrentlyLoggedIn(); // 기존 로직 활용
    }
}