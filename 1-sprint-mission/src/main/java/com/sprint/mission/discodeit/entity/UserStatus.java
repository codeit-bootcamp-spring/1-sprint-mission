package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus {
    private UUID id;
    private User user;
    private Instant lastSeenAt;

    public UserStatus(User user) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.lastSeenAt = lastSeenAt;
    }

    public void updateLastSeenAt() {
        this.lastSeenAt = Instant.now();
    }

    public boolean isOnline() {
        return lastSeenAt.plusSeconds(300).isAfter(Instant.now());
    }

}
