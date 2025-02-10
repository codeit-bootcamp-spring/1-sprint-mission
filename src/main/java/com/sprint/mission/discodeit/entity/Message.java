package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class Message implements Serializable {

    private static final Long serialVersionUID = 1L;
    private UUID id ;
    private final Instant createdAt;
    private Instant updatedAt;

    private UUID senderId;
    private UUID channelId;
    private String content;


    public Message(UUID userId, UUID channelId, String content){
        this.id = UUID.randomUUID();
        this.content = content;
        this.createdAt =  Instant.now(); // 초 단위로 변환
        this.senderId = userId;
        this.channelId=channelId;
    }

    //update
    public void updateUpdatedAt(){
        this.updatedAt=Instant.now(); //업데이트 시간
    }
    public void updateContent(String content){
        this.content = content;
    }
}
