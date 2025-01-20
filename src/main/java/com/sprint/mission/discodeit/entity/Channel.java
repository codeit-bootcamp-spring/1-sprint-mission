package com.sprint.mission.discodeit.entity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {

    private UUID id ;
    private String channelName;
    private final Long createdAt;
    private Long updatedAt;

    public Channel(String channelName){
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.createdAt =  System.currentTimeMillis() / 1000; // 초 단위로 변환
    }

    //get
    public UUID getChId(){
        return this.id;
    }
    public String getChannelName(){
        return this.channelName;
    }
    public long getCreatedAt(){
        return this.createdAt;
    }
    public long getUpdatedAt(){
        return this.updatedAt;
    }

    //update
    public void updateName(String name){
        this.channelName=name;
    }
    public void updateUpdatedAt(){
        this.updatedAt=System.currentTimeMillis() /1000; //업데이트 시간
    }


}
