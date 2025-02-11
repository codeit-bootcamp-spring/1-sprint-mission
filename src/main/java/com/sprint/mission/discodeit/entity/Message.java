package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private String content;
    private UUID channelId;
    private UUID authorId;

    public Message(Instant updatedAt, String content, UUID channelId, UUID authorId) {
        this.updatedAt = updatedAt;
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
    }

    public void update(String newContent) {
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            this.updatedAt = Instant.now();
        }
    }
}
