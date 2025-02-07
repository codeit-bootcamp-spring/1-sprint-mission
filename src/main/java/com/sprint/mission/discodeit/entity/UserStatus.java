package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private final long ADDITIONAL_TIME_SECONDS = 60 * 5;
    private final UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID userId;
    private Status status;

    public enum Status {
        ONLINE,
        OFFLINE,
    }

    public UserStatus createUserStatus(UUID userId) {
        return createUserStatus(userId);
    }

    private UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.status = Status.ONLINE;
    }

    public Status getStatus() {
        updateStatus();
        return this.status;
    }

    // 레파지토리에서 해야할 거 같은데,,,? 시간을 다루는 거라 여기서 하나?
    private void updateStatus() {
        Instant ValidTime = this.updatedAt.plusSeconds(ADDITIONAL_TIME_SECONDS);
        if (ValidTime.compareTo(Instant.now()) > 0) {
            this.status = Status.ONLINE;
        } else {
            this.status = Status.OFFLINE;
        }
    }
}
