package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID channelId;
    private UUID userId;
    private Instant lastReadAt;

    public ReadStatus(UUID channelId, UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.channelId = channelId;
        this.userId = userId;
        this.lastReadAt = Instant.now();
    }

    public void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    public void update(Instant lastReadAt) {
        boolean updated = false;
        if (lastReadAt != null && !lastReadAt.equals(this.lastReadAt)) {
            this.lastReadAt = lastReadAt;
            updated = true;
        }

        if (updated) {
            updateUpdatedAt();
        }
    }

    public boolean isSameChannelId(UUID channelId) {
        return this.channelId.equals(channelId);
    }

    public boolean isSameUserId(UUID userId) {
        return this.userId.equals(userId);
    }
}
