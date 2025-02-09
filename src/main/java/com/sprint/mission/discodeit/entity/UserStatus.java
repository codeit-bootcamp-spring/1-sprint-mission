package com.sprint.mission.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class UserStatus {

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private UserStatusType type;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        this.userId = userId;
        type = UserStatusType.ONLINE;

    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public UserStatusType getType() {
        return type;
    }


    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }


    //업데이트 > UserType
    public void updateUserStatus(UserStatusType type){
        this.type = type;
        setUpdatedAt();
    }

    //마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주
    public boolean isOnline() {
        if(type != UserStatusType.OFFLINE && Duration.between(updatedAt, Instant.now()).toMinutes() < 5){
            return true;
        }else{
            type = UserStatusType.OFFLINE;
            return false;
        }

    }
}