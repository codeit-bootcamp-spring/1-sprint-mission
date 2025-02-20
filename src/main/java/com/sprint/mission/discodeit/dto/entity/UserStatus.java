package com.sprint.mission.discodeit.dto.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity {
    private Instant lastAttendAt;
    private UUID userId;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
        this.lastAttendAt = Instant.now();
    }

    public void updateUserStatus(Instant newLastAttendAt) {
        this.lastAttendAt = newLastAttendAt;
        setUpdatedAt();
    }

    public Boolean isOnline() {
        Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

        return lastAttendAt.isAfter(instantFiveMinutesAgo);
    }
}
