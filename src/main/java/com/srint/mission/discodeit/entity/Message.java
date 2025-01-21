package com.srint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class Message{

    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    private String content;
    private User user;
    private Channel channel;

    public Message(String content, User user, Channel channel) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();

        this.content = content;
        this.user = user;
        this.channel = channel;

        //여기서 연관관계를 맺는 것이 맞을까?
        channel.getMessages().add(this);
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getContent() {
        return content;
    }
    public User getUser() {
        return user;
    }

    public Channel getChannel(){
        return channel;
    }

    public void setMessageChannel(){
        this.channel.getMessages().add(this);
    }


    public void setUpdatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
    }

    public void setContent(String content) {
        this.content = content;
        setUpdatedAt();
    }

}
