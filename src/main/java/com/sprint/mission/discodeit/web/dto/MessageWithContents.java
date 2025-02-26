package com.sprint.mission.discodeit.web.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class MessageWithContents {
    private UUID id;
    private Instant createdAt;

    private UUID senderId;
    private UUID channelId;
    private String content;

    private BinaryContent attachFile;

    public MessageWithContents(UUID senderId, UUID channelId, String content, BinaryContent attachFile) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.senderId = senderId;
        this.channelId = channelId;
        this.content = content;
        this.attachFile = attachFile;
    }
}
