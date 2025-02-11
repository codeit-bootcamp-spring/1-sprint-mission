package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity implements Serializable {
    private static final long getSerialVersionUID = 1L;
    private final UUID userid;
    private Instant lastSeenAt;

    public UserStatus(UUID userid) {
        super();
        this.userid = userid;
        this.lastSeenAt = Instant.ofEpochMilli(System.currentTimeMillis());
    }

    public void updateLastSeen(Instant timestamp) {
        this.lastSeenAt = timestamp;
        update();
    }

    public boolean isOnline() {
        return Duration.between(lastSeenAt, Instant.now()).toMinutes() < 5;
    }
}
