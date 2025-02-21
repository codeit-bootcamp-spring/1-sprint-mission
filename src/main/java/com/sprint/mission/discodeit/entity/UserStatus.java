package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private Instant lastLoginTime;

    public UserStatus() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public UserStatus(UUID userId, Instant lastLoginTime) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.lastLoginTime = lastLoginTime;
    }

    public void update(Instant lastLoginTime){
        this.lastLoginTime = lastLoginTime;
        this.updatedAt = Instant.now();
    }

    public boolean isOnline(){
        Instant now = Instant.now();
        return lastLoginTime != null && Duration.between(lastLoginTime, now).toMinutes() <= 5;
    }
}