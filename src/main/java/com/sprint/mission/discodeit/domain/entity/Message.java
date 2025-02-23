package com.sprint.mission.discodeit.domain.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Message implements Serializable {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private String content;

    private UUID senderId;

    private UUID channelId;
    private List<UUID> messageFiles;

    public Message(String content, UUID senderId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    public Message(UUID id,String content, UUID senderId, UUID channelId, List<UUID> messageFiles) {
        this.id = id;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
        this.messageFiles = messageFiles;
    }

    public void updateMessage(String content) {
        this.content = content;
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                '}';
    }
}
