package com.sprint.mission.discodeit.entity;
import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable, Entity {
    private final long createdAt;
    private long updatedAt;
    private UUID id;
    private User fromUser;
    private Channel channel;
    private String content;


    public Message(User fromUser, Channel channel, String content){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.fromUser = fromUser;
    }

    //생성시간 리턴
    public long getCreatedAt(){return this.createdAt;}
    //업데이트시간 리턴
    public long getUpdatedAt(){return this.updatedAt;}
    //업데이트시간 변경
    public void setUpdatedAt(){this.updatedAt = System.currentTimeMillis();}
    //id 리턴
    public UUID getId(){return this.id;}
    //메세지 보낸이 리턴
    public User getFromUser(){return this.fromUser;}
    //메세지가 보여질 채널 리턴
    public Channel getChannel(){return this.channel;}
    //메세지 내용 리턴
    public String getContent(){return this.content;}
    //메세지 내용 변경 후 업데이트시간 변경
    public void setContent(String content){
        this.content = content;
        this.setUpdatedAt();
    }







}
