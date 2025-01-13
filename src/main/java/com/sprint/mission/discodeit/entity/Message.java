package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message{
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String content;

    public Message(UUID id, Long createdAt, Long updatedAt, String content) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.content = content;
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

    public void update(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
