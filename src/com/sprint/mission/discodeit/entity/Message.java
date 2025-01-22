package com.sprint.mission.discodeit.entity;
import java.util.UUID;

public class Message {
    private User user; //유저
    private String id ;
    private String content;
    private long createdAt;
    private long updatedAt;

    public Message(User user, String content){
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.createdAt =  System.currentTimeMillis() / 1000; // 초 단위로 변환
        this.user = user;
    }

    //get
    public String getMsgId(){
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
    public User getUser(){
        return this.user;
    }

    //update
    public void updateUpdatedAt(){
        this.updatedAt=System.currentTimeMillis() /1000; //업데이트 시간
    }
    public void updateContent(String content){
        this.content = content;
    }
}
