package com.srint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Message implements Serializable {

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

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", content='" + content + '\'' +
                ", user=" + user +
                '}';
    }
}
