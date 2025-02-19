package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.MIN;
    }

    //lastReadAt 시간 수정.
    public void updateReadStatus(Instant time) {
        lastReadAt = time;
        setUpdatedAt();
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "ReadStatus{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userId=" + userId +
                ", channelId=" + channelId +
                ", lastReadAt=" + lastReadAt +
                '}';
    }
}