package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;


@Getter
@RequiredArgsConstructor
public class UserStatus implements Serializable {
    private String userId;
    private Instant lastSeen;
    private Instant createdAt;

    public UserStatus(String userId, Instant lastSeen) {
        this.userId = userId;
        this.lastSeen = lastSeen;
        this.createdAt = Instant.now();
    }

    public Instant getLastSeen() {
        return lastSeen;
    }

}