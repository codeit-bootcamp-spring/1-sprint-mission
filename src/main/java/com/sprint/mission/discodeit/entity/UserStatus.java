package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID userId;
    private Instant lastAccessedAt;

    public UserStatus(UUID userId, Instant lastAccessedAt){
        super();
        this.userId = userId;
        this.lastAccessedAt = lastAccessedAt;
    }

    public boolean isUserOnline() {
        return Instant.now().minusSeconds(300).isBefore(this.lastAccessedAt);
    }
}
