package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID Id;
    private final Instant createAt;
    private Instant updateAt;

    private final UUID userId;
    private Instant lastSeenAt;

    private static final Duration ACTIVE_THRESHOLD = Duration.ofMinutes(5);

    public UserStatus(UUID userId, Instant lastSeenAt) {
        this.Id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
        this.userId = userId;
        this.lastSeenAt = lastSeenAt;
    }

    public void updateLastSeen() {
        this.lastSeenAt = Instant.now();
        this.updateAt = Instant.now();
    }

    public boolean isOnline() {
        return Duration.between(lastSeenAt, Instant.now())
                .compareTo(ACTIVE_THRESHOLD) <= 0;
    }


}
