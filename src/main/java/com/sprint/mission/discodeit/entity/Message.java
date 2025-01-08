package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private long createdAt;
    private long updatedAt;
    private Channel channel;
    private User writer;
    private String content;

    public Message(User writer, String content) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.writer = writer;
        this.content = content;
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

    public Channel getChannel() {
        return channel;
    }

    public User getWriter() {
        return writer;
    }

    public String getContent() {
        return content;
    }

    // update
    public void updateContent(String updateContent){
        this.content = updateContent;
    }

}
