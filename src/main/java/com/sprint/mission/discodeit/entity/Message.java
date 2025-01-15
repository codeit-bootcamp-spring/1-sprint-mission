package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final Long createdAt;
    private final User writer;
    private final Channel channel;
    private Long updatedAt;
    private String content;

    public Message(Channel channel, User writer, String content) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        updatedAt = null;
        this.channel = channel;
        this.writer = writer;
        this.content = content;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        updatedAt = System.currentTimeMillis();
    }

    public User getWriter() {
        return writer;
    }

    public Channel getChannel() {
        return channel;
    }

}
