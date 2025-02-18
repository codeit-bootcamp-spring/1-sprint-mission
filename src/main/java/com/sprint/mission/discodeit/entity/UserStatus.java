package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final UUID userId;
    private Instant lastActive;

    public UserStatus(UUID userId, Instant lastActive) {
        this.userId = userId;
        this.lastActive = lastActive;
    }

    public void updateLastActive(Instant newTime) {
        this.lastActive = newTime;
    }
}
