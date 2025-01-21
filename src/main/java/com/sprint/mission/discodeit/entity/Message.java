package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private String content;
    private User sender;
    private Channel channel;

    private long createdAt;
    private long updatedAt;

    public Message(UUID id, String content, Channel channel, User sender, long createdAt) {
        this.id = id;
        this.content = content;
        this.channel = channel;
        this.sender = sender;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }
    public String getContent() {
        return content;
    }
    public Channel getChannel() {
        return channel;
    }
    public User getSender() {
        return sender;
    }
    public long getCreatedAt() {
        return createdAt;
    }
    public long getUpdatedAt() {
        return updatedAt;
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }

    public String toString() {
        return "Message {" +
                "id = " + id +
                ", content = " + content +
                ", channel = " + channel.getChannelName() +
                ", sender = " + sender.getUsername() +
                ", createdAt = " + createdAt +
                ", updatedAt = " + updatedAt + "}";
    }

}