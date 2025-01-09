package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String content;


    public Message(String content){
        id = UUID.randomUUID();
        createdAt=System.currentTimeMillis();
        this.content = content;

    }

    public Long getCreatedAt(){
        return createdAt;
    }

    public Long getUpdatedAt(){
        return updatedAt;
    }

    public UUID getId(){
        return id;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
        updatedAt = System.currentTimeMillis();
    }




}
