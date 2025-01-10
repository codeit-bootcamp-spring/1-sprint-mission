package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private User sender;
    private String content;
    private MessageType type;

    public Message(User sender, String content, MessageType type) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = null;
        this.sender = sender;
        this.content = content;
        this.type = type;
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

    public User getSender() {
        return sender;
    }

    public void updateSender(User sender) {
        this.sender = sender;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }
}
