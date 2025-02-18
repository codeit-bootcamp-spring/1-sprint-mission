package com.sprint.mission.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class UserStatus {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private Instant lastOnlineAt;

    public UserStatus(UUID id, Instant lastOnlineAt) {
        this.id = id;
        this.lastOnlineAt = lastOnlineAt;

        id = UUID.randomUUID();
        createdAt = Instant.now();
    }

    public void update(Instant updateOnlineAt) {
        if (updateOnlineAt!= null && !updateOnlineAt.equals(lastOnlineAt)) {
            lastOnlineAt = updateOnlineAt;
            updatedAt = Instant.now();
        }
    }

    public boolean isOnline() {
        Instant minus = Instant.now().minus(Duration.ofMinutes(5));

        return lastOnlineAt.isAfter(minus);
    }
}
