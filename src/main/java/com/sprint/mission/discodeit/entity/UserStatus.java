package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID userId;
    private Instant lastActiveTime;
    private boolean active;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.lastActiveTime = Instant.now();
    }

    public void updateActiveTime(Instant time) {
        this.lastActiveTime = time;
    }

    // active 상태인지 마지막 접속 시간을 기준으로 판단
    public boolean isActiveNow() {
        return Duration.between(lastActiveTime, Instant.now()).toMinutes() <= 5;
    }
}
