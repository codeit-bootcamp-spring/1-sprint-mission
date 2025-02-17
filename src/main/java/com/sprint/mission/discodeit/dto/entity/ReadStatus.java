package com.sprint.mission.discodeit.dto.entity;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ReadStatus extends BaseEntity{
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();
    }
    public void updateRead(Instant newLastReadAt) {
        this.lastReadAt = newLastReadAt;
        setUpdatedAt();
    }
}
