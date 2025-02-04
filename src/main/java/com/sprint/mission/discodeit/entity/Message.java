package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

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

    //get
    public UUID getMsgId(){
        return this.id;
    }
    public String getContent(){
        return this.content;
    }
    public long getCreatedAt(){
        return this.createdAt;
    }
    public long getUpdatedAt(){
        return this.updatedAt;
    }
    public UUID getUserID(){
        return this.senderId;
    }

    //update
    public void updateUpdatedAt(){
        this.updatedAt=System.currentTimeMillis() /1000; //업데이트 시간
    }
    public void updateContent(String content){
        this.content = content;
    }
}
