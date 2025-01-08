package com.sprint.mission.discodeit.entity;
import java.util.UUID;

public class Channel {

    String id ;
    String chName;
    long createdAt;
    long updatedAt;

    public Channel(String chName){
        this.id = UUID.randomUUID().toString();
        this.chName = chName;
        this.createdAt =  System.currentTimeMillis() / 1000; // 초 단위로 변환
    }

    //get
    public String getUserId(){
        return this.id;
    }
    public String getchName(){
        return this.chName;
    }
    public long getCreatedAt(){
        return this.createdAt;
    }
    public long getUpdatedAt(){
        return this.updatedAt;
    }

    //update
    public void updateUpdatedAt(){
        this.updatedAt=System.currentTimeMillis() /1000; //업데이트 시간
    }

    
}
