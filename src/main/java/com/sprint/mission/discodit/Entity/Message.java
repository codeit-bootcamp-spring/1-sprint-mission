package com.sprint.mission.discodit.Entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private UUID sender;
    private String content;

    public Message(UUID sender, String content) {
        this.id = UUID.randomUUID();
        this.sender = sender;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.content = content;
    }
    public Message() {
        this.id = null;
        this.createdAt = 0L;
    }

    public long setUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
        return updatedAt;
    }

    public void update(String content) {
        this.content = content;
        updatedAt = setUpdatedAt();
    }

    public UUID getMessageId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public UUID getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "Message{" + "id=" + id + ", sender=" + sender + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", content='" + content + '\'' + '}';
    }
}
