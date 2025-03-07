package com.sprint.mission.discodeit.entity;

import lombok.Getter;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ReadStatus{
    private final UUID id;
    private final UUID userId;
    private final UUID channelId;
    private final UUID messageId;
    private boolean isRead;
    private Instant readAt;

    public ReadStatus(UUID userId, UUID channelId, UUID messageId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.messageId  = messageId;
        this.isRead = false;
        this.readAt = null;
    }

    public void markAsRead() {
        this.isRead = true;
        this.readAt = Instant.ofEpochMilli(System.currentTimeMillis());
    }
}
