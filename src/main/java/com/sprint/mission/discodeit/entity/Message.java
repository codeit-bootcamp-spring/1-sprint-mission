package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final Channel channel;
    private final User writer;
    private final long createdAt;
    private long updatedAt;
    private String content;

    public Message(Channel channel, User writer, String content) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.channel = channel;
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
    public void updateUpdatedAt() {
        updatedAt = System.currentTimeMillis();
    }

    public void updateContent(String updateContent){
        this.content = updateContent;
        updateUpdatedAt();
    }

    @Override
    public String toString() {
        return "Message{channel:" + channel.getTitle() + "writer:" + writer.getName() + ",content:" + content + ",createdAt:" + createdAt + ",updatedAt:" + updatedAt + "}";
    }
}
