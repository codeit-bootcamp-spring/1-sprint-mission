package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private final String id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String content;
    private String userId;
    private String channelId;

    public Message(String content, String userId, String channelId) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;

        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
    }

    public void update(String content) {
        this.content = content != null ? content : this.content;
        this.updatedAt = Instant.now();
    }
}