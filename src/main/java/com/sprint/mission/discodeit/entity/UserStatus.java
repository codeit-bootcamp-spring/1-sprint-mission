package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Getter
@NoArgsConstructor
public class UserStatus implements Serializable {
    private String userId;
    private Instant lastSeen;

    public UserStatus(String userId, Instant lastSeen) {
        this.userId = userId;
        this.lastSeen = lastSeen;
    }

    public Instant getLastSeen() {
        return lastSeen;
    }
}