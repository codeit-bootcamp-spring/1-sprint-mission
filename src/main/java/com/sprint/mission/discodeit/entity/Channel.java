package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String channelName;

    public Channel(String channelName){
        id = UUID.randomUUID();
        createdAt=System.currentTimeMillis();
        this.channelName = channelName;
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

    public String getChannelName(){
        return  channelName;
    }

    public void setChannelName(String channelName){
        this.channelName = channelName;
        updatedAt = System.currentTimeMillis();
    }



}
