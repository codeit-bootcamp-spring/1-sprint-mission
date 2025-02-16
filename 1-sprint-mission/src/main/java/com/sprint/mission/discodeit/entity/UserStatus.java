package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus {
    private UUID id;
    private User user;
    private Instant lastSeenAt = Instant.EPOCH;

    public UserStatus(User user, Instant lastSeenAt) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.lastSeenAt = (lastSeenAt != null) ? lastSeenAt : Instant.EPOCH;
    }

    public void updateLastSeenAt(Instant newLastActiveAt) {
        if(newLastActiveAt.isAfter(this.lastSeenAt)) {
            this.lastSeenAt = newLastActiveAt;
        }
    }

    public boolean isOnline() {
        if(lastSeenAt == null) {
            return false;
        }
        return lastSeenAt.plusSeconds(300).isAfter(Instant.now());
    }

}
