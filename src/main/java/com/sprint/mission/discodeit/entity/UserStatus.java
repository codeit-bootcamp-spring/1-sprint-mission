package com.sprint.mission.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class UserStatus {

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private UUID userId;

    private boolean

    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }


    //마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주합니다.
    public boolean isOnline() {
        return Duration.between(updatedAt, Instant.now()).toMinutes() < 5;
    }
}