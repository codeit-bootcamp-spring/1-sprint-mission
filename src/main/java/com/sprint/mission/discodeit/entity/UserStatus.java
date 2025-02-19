package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class UserStatus extends BaseEntity{
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private Instant lastSeenAt;


    public UserStatus(UUID userId,Instant lastSeenAt) {
        super();
        this.userId = userId;
        this.lastSeenAt = lastSeenAt;
    }

    public  boolean isOnline() {
        return Duration.between(lastSeenAt, Instant.now()).toMinutes() < 5;
    }
    public void updateLastSeenAt() {
        lastSeenAt = super.getCurrentTime();
    }
}
