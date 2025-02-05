package com.sprint.mission.discodeit.entity.status;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private String id;
    private Instant createdAt;
    private Instant updatedAt;
    private static final int USER_ACTIVE_TIMEOUT_SECONDS = 5 * 60;

    public UserStatus() {
        id= UUID.randomUUID().toString();
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    public boolean isActive() {
        return Instant.now().minusSeconds(USER_ACTIVE_TIMEOUT_SECONDS).isBefore(updatedAt);
    }
}