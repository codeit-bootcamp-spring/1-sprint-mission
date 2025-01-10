package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String content;
    private final User writer;
    private final Channel channel;

    public Message(Channel channel, User writer, String content){
        id = UUID.randomUUID();
        createdAt=System.currentTimeMillis();
        updatedAt=null;
        this.channel = channel;
        this.writer = writer;
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

    public User getWriter(){
        return writer;
    }

    public Channel getChannel(){
        return channel;
    }

    public void displayMessageInfo(){
        System.out.println("채널 이름: " + channel + "\n작성자: " + writer + "\n" + content);
    }

    public void setContent(String content){
        this.content = content;
        updatedAt = System.currentTimeMillis();
    }

}
