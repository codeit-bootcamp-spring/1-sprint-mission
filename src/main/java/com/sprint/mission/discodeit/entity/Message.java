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
    private final UUID authorId;
    private final UUID channelId;

    private String content;
    private List<UUID> binaryContentData;

    public Message(String content, UUID authorId, UUID channelId, List<UUID> binaryContentData) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.authorId = authorId;
        this.channelId = channelId;

        this.content = content;
        this.binaryContentData = binaryContentData;
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

    public boolean isSameChannelId(UUID channelId) {
        return this.channelId.equals(channelId);
    }
}
