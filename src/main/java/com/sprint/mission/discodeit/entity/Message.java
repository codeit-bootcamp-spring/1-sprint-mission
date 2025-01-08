package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class Message {
    private final UUID id;
    private final User sendUser;
    private String content;

    private final Long createdAt;
    private Long updatedAt;

    public Message(User sendUser, String content) {
        this.id = UUID.randomUUID();
        this.sendUser = sendUser;
        this.content = content;
        this.createdAt = Instant.now().toEpochMilli();
        this.updatedAt = 0L;
    }

    public UUID getId() {
        return id;
    }

    public User getSendUser() {
        return sendUser;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void updateUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
