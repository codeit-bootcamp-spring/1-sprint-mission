package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class UserStatus extends BaseEntity {
    private final UUID userId;
    private final Instant lastActiveAt;

    public UserStatus(UUID id, Instant lastActiveAt) {
        super();
        this.userId = id;
        this.lastActiveAt = lastActiveAt;
    }

    public boolean isUserOnlin() {
        return Instant.now().minusSeconds(300).isBefore(this.lastActiveAt);
    }
}