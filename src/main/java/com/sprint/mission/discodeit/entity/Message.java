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
    private final UUID channelId;
    private UUID senderId;
    private String content;
    private final Instant createdAt;
    private Instant updatedAt;
    private List<UUID> attachmentIds;


    public Message(UUID senderId, UUID channelId, String content) {
        this.id = UUID.randomUUID();
        this.senderId = senderId;
        this.channelId = channelId;
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.attachmentIds = attachmentIds;

    }

    public void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = Instant.now();
    }
}
