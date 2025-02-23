package com.sprint.mission.discodeit.domain.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus{
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private Instant lastAttendAt;
    private UUID userId;
    private String status;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt =createdAt;
        this.userId = userId;
        this.lastAttendAt = Instant.now();
    }

    public void updateUserStatus(Instant newLastAttendAt) {
        this.lastAttendAt = newLastAttendAt;
        updatedAt = Instant.now();
    }

    public Boolean isOnline() {
        Instant now = Instant.now();
        Duration between = Duration.between(lastAttendAt, now);
        return between.toMinutes()<5;
    }
}
