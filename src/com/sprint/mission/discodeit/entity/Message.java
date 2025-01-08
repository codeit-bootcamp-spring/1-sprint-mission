package com.sprint.mission.discodeit.entity;
import java.util.UUID;

public class Message {
    private String id ;
    private String content;
    private long createdAt;
    private long updatedAt;

    public Message(String msgMame){
        this.id = UUID.randomUUID().toString();
        this.content = msgMame;
        this.createdAt =  System.currentTimeMillis() / 1000; // 초 단위로 변환
    }

    //get
    public String getMsgId(){
        return this.id;
    }
    public String getcontent(){
        return this.content;
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
