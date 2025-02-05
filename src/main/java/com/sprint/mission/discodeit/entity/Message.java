package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private long createdAt;
    private long updatedAt;
    private String content;
    private Channel channel;
    private User writer;

    public static Message createMessage(Channel channel, User writer, String content) {
        return new Message(channel, writer, content);
    }

    private Message(Channel channel, User writer, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
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

    public void update(String newContent) {
        boolean isChanged = false;
        if (!newContent.equals(this.content)) {
            this.content = newContent;
            isChanged = true;
        }

        if (isChanged) {
            updatedAt = System.currentTimeMillis();
        }
    }

    @Override
    public String toString() {
        return "Message{id:" + id + ",channel:" + channel.getTitle() + ",writer:" + writer.getName() + ",content:" + content + ",createdAt:" + createdAt + ",updatedAt:" + updatedAt + "}";
    }
}
