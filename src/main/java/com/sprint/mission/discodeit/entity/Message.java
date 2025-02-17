package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Builder.Default
    private Long createdAt = Instant.now().getEpochSecond();

    private Long updatedAt;
    private String content;
    private UUID channelId;
    private UUID authorId;

    public Message(String content, UUID channelId, UUID authorId) {
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.createdAt = Instant.now().getEpochSecond();
        this.updatedAt = this.createdAt;
    }

    public void update(String newContent) {
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            this.updatedAt = Instant.now().getEpochSecond();
        }
    }
}
