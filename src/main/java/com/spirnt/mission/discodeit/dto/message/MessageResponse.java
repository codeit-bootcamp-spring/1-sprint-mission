package com.spirnt.mission.discodeit.dto.message;

import com.spirnt.mission.discodeit.enity.Message;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class MessageResponse {
    private String content;
    private UUID userId;
    private UUID channelId;
    private UUID messageId;
    private Instant createdAt;
    private Instant updatedAt;
    private List<UUID> attachedFiles;

    public MessageResponse(Message message) {
        this.content = message.getContent();
        this.userId = message.getSenderId();
        this.channelId = message.getChannelId();
        this.messageId = message.getId();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
        this.attachedFiles = message.getAttachedFiles();
    }

    @Override
    public String toString() {
        return "Message[" + this.getContent() +
                " ID: " + this.messageId + "]";
    }
}