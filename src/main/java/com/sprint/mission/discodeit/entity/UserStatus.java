package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private User user;
    private Instant lastSeenAt;

    public UserStatus(User user, Instant lastSeenAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.user = user;
        this.lastSeenAt = lastSeenAt;
    }

    public void updateLastSeen(Instant newLastSeen) {
        this.lastSeenAt = newLastSeen;
        this.updatedAt = Instant.now();
    }

    public boolean isOnline() {
        return lastSeenAt != null && lastSeenAt.isAfter(Instant.now().minusSeconds(300));
    }
}
