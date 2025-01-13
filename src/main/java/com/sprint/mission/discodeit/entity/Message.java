package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final Long createdAt;
    private final User sender;
    private Long updatedAt;
    private String content;
    private MessageType type;

    private Message(User sender, String content, MessageType type) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = null;
        this.sender = sender;
        this.content = content;
        this.type = type;
    }

    public static Message ofCommon(User sender, String content) {
        return new Message(sender, content, MessageType.COMMON);
    }

    public static Message ofReply(User sender, String content) {
        return new Message(sender, content, MessageType.REPLY);
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

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }
}
