package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus {
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private final UUID userId;

    private UserStatus(UUID userId){
        this.id=UUID.randomUUID();
        this.createdAt=Instant.now();
        this.updatedAt=null;
        this.userId =userId;

    }
    private UserStatus(UUID id, Instant createdAt, Instant updatedAt, UUID userId){
        this.id=id;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;

        this.userId = userId;
    }
    public static UserStatus setUpUserStatus(UUID userId){
        return new UserStatus(userId);
    }
    public static UserStatus makeAllUserStatus(UUID id,Instant createdAt,Instant updatedAt,UUID userId){
        return new UserStatus(id,createdAt,updatedAt,userId);
    }

    public boolean isUserOnline() {
        if (updatedAt == null) {
            return false;
        }
        return Duration.between(updatedAt, Instant.now()).getSeconds() <= 300;
    }

    // 사용자의 마지막 접속 시간 업데이트
    public void updateLastSeen() {
        this.updatedAt = Instant.now();
    }




}
