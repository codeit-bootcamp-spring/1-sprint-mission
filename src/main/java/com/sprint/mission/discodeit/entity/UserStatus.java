package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.Status.CONNECTED;

@Getter
public class UserStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastActiveAt;
    private UUID userId;
    private Status status;


    public UserStatus(UUID userId) {
        this.userId = userId;
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.lastActiveAt = updatedAt;
        this.status = CONNECTED;
    }

    public UserStatus(UUID userId, Instant instant) {
        this.userId = userId;
        this.id = UUID.randomUUID();
        this.createdAt = instant;
        this.updatedAt = createdAt;
        this.lastActiveAt = updatedAt;
        this.status = CONNECTED;
    }

    public void updateLastActiveAt(Instant instant) {
        this.lastActiveAt = instant;
    }

    public void updateStatus() {
        Instant now = Instant.now();
        if (Duration.between(lastActiveAt, now).getSeconds() <= 300) {
            this.status = Status.CONNECTED;
        } else {
            this.status = Status.DISCONNECTED;
        }
    }
}

