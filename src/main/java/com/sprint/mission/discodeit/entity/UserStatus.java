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
    private final Instant createAt;
    private Instant updateAt;

    private final UUID userId;
    private Instant lastActive;

    private static final Duration ACTIVE_THRESHOLD = Duration.ofMinutes(5);

    public UserStatus(UUID userId, Instant lastSeenAt) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
        this.userId = userId;
        this.lastActive = lastActive != null ? lastActive : Instant.now(); //
    }



    public boolean isOnline() {
        return Duration.between(lastActive, Instant.now())
                .compareTo(ACTIVE_THRESHOLD) <= 0;
    }

    public void updateLastActive(Instant newLastActive) {
        if (newLastActive.isAfter(this.lastActive)) {
            this.lastActive = newLastActive;
            this.updateAt = Instant.now();
        }
    }

}
