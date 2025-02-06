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

    private String content;
    private UUID authorId;
    private UUID channelId;
    private List<UUID> attachmentIds;

    public Message() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public Message(String content, UUID authorId, UUID channelId, List<UUID> attachmentIds) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
        this.attachmentIds = attachmentIds;
    }

    public void update(String content, List<UUID> attachmentIds) {
        this.content = content;
        this.updatedAt = Instant.now();
        this.attachmentIds = attachmentIds;
    }
}
