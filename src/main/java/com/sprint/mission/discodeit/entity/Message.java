package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class Message implements Serializable {

    private static final Long serialVersionUID = 1L;
    private UUID id ;
    private String content;
    private final Long createdAt;
    private Long updatedAt;

    private UUID senderId;
    private UUID channelId;

    public Message(UUID userId, UUID channelId, String content){
        this.id = UUID.randomUUID();
        this.content = content;
        this.createdAt =  System.currentTimeMillis() / 1000; // 초 단위로 변환
        this.senderId = userId;
        this.channelId=channelId;
    }

    //update
    public void updateUpdatedAt(){
        this.updatedAt=System.currentTimeMillis() /1000; //업데이트 시간
    }
    public void updateContent(String content){
        this.content = content;
    }
}
