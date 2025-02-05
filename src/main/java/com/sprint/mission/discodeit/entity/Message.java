package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID id;
    private long createdAt;
    private long updatedAt;
    private String content;
    private User sender;
    private UUID channelId;

    public Message(String content, User sender, UUID channelId) {
        long currentUnixTime = System.currentTimeMillis() / 1000;
        this.id = UUID.randomUUID();
        this.createdAt = currentUnixTime;
        this.updatedAt = currentUnixTime;

        this.content = content;
        this.sender = sender;
        this.channelId = channelId;
    }

    public void updateUpdatedAt() {
        updatedAt = System.currentTimeMillis() / 1000;
    }

    public void updateContent(String content) {
        if (!this.content.equals(content)) {
            this.content = content;
            updateUpdatedAt();
        }
    }

    @Override
    public String toString() {
        return String.format(sender.getName() + ": " + content);
    }
}
