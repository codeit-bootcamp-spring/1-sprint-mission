package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String message;
    private UUID authorId;
    private UUID channelId;
    private List<byte[]> content;

    public Message() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public Message(String message, UUID authorId, UUID channelId, List<byte[]> content) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.message = message;
        this.authorId = authorId;
        this.channelId = channelId;
        this.content = content;
    }

    public void update(String message, List<byte[]> content) {
        this.message = message;
        this.updatedAt = Instant.now();
        this.content = content;
    }
}
