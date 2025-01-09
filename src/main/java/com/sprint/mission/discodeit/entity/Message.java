package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    private String content;
    private User writer;
    private Channel channel;

    public Message(User writer, String content, Channel channel) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.writer = writer;
        this.content = content;
        this.channel = channel;
    }

    public void updateContent(String content) {
        this.content = content;
        updatedAt = System.currentTimeMillis();
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

    public User getWriter() {
        return writer;
    }

    public String getContent() {
        return content;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id.toString().substring(0, 8) +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", content='" + content + '\'' +
                ",\n        writer=" + writer +
                ",\n        channel=" + channel +
                '}';
    }
}
