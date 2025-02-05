package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String content;

    private final User sender;
    private final UUID channelId;

    public Message(String content, User sender, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.content = content;
        this.sender = sender;
        this.channelId = channelId;
    }

    public void updateUpdatedAt() {
        updatedAt = Instant.now();
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
