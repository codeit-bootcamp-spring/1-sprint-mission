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
    private boolean isOnline;

    private UserStatus(UUID userId){
        this.id=UUID.randomUUID();
        this.createdAt=Instant.now();
        this.updatedAt=null;
        this.userId =userId;
        this.isOnline=false;

    }
    private UserStatus(UUID id, Instant createdAt, Instant updatedAt, UUID userId,boolean isOnline){
        this.id=id;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;

        this.userId = userId;
        this.isOnline=isOnline;
    }
    public static UserStatus setUpUserStatus(UUID userId){
        return new UserStatus(userId);
    }
    public static UserStatus makeAllUserStatus(UUID id,Instant createdAt,Instant updatedAt,UUID userId,boolean isOnline){
        return new UserStatus(id,createdAt,updatedAt,userId, isOnline);
    }

    public boolean isUserOnline() {
        if (updatedAt == null) {
            return false;
        }
        return Duration.between(updatedAt, Instant.now()).getSeconds() <= 300;
    }

    public void updateLastSeen() {
        this.updatedAt = Instant.now();
    }

    public void updateIsOnlien(){
        this.isOnline=!this.isOnline;
    }



}
