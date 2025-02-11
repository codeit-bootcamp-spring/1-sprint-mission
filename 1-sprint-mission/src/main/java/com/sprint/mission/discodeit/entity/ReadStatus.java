package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus {
    private final  UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private final UUID userId;
    private final UUID channelId;
    private boolean isRead;

    private ReadStatus(UUID userId,UUID channelId,boolean isRead){
        this.id=UUID.randomUUID();
        this.createdAt=Instant.now();
        this.updatedAt=null;
        this.userId =userId;
        this.channelId=channelId;
        this.isRead=isRead;
    }
    private ReadStatus(UUID id, Instant createdAt, Instant updatedAt, UUID userId, UUID channelId,boolean isRead){
        this.id=id;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
        this.userId =userId;

        this.channelId = channelId;
        this.isRead=isRead;
    }

    public static ReadStatus setUpReadStatus(UUID userId,UUID channelId,boolean isRead){
        return new ReadStatus(userId,channelId,isRead);
    }

    public static ReadStatus makeAllReadStatus(UUID id, Instant createdAt, Instant updatedAt, UUID userId, UUID channelId,boolean isRead){
        return new ReadStatus(id,createdAt,updatedAt,userId,channelId,isRead);
    }

    public void updateIsRead(){
        this.isRead=!this.isRead;
    }
}
