package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class UserStatus extends BaseEntity{
    private UUID userId;
    private static Instant lastSeenAt;
    private String userStatus;

    public UserStatus(UUID userId,String userStatus) {
        super();
        this.userId = userId;
        lastSeenAt = super.getCurrentTime();
        this.userStatus = userStatus;
    }

    public static boolean isOnline() {
        return Duration.between(lastSeenAt, Instant.now()).toMinutes() < 5;
    }
    public void updateLastSeenAt() {
        lastSeenAt = super.getCurrentTime();
    }
}
