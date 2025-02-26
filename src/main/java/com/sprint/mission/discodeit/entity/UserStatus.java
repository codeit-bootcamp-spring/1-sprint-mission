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
    private final Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private Instant lastActiveAt;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        this.userId = userId;
        lastActiveAt = Instant.now();

    }

    public void updateLastActiveAt(Instant time){
        this.lastActiveAt = time;
        setUpdatedAt();
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    //마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주
    public boolean isOnline() {
        if(Duration.between(lastActiveAt, Instant.now()).toMinutes() < 5){
            return true;
        }else{
            return false;
        }

    }
}