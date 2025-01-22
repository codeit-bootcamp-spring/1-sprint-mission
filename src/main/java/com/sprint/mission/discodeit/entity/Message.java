package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message{
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String content;
    private final User user;
    private final Channel channel;

    public Message(UUID id, Long createdAt, Long updatedAt, String content, User user, Channel channel) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.content = content;
        this.user = user;
        this.channel = channel;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = updatedAt;
    }
}
