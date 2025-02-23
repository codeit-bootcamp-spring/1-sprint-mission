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
    private Instant lastActiveAt;

    public UserStatus(UUID userId, Instant lastActiveAt){
        super();
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }


    public void update(Instant lastActiveAt){
        boolean isUpdated = false;
        if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = lastActiveAt;
            isUpdated = true;
        }

        if (isUpdated) {
            updated();
        }
    }

    public boolean isUserOnline() {
        return Instant.now().minusSeconds(300).isBefore(this.lastActiveAt);
    }
}
