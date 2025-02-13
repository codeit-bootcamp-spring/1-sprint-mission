package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final UUID id;
    private final UUID userId;
    private final Instant createdAt;

    private Instant updatedAt;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.createdAt = Instant.now();

        this.updatedAt = Instant.now();
    }

    public void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
