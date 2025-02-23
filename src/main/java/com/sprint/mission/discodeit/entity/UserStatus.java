package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID userId;
    private Instant activeAt;

    public UserStatus(UUID userId, Instant activeAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.activeAt = activeAt;
    }

    public void update(Instant activeAt) {
        boolean flag = false;
        if (activeAt != null && !activeAt.equals(this.activeAt)) {
            this.activeAt = activeAt;
            flag = true;
        }

        if (flag) {
            this.updatedAt = Instant.now();
        }
    }

    public Boolean isOnline() {
        Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

        return activeAt.isAfter(instantFiveMinutesAgo);
    }
}
