package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Long createdAt;
    @Setter
    private Long updatedAt;
    private final UUID ownerId;
    private final UUID channelId;

    public ReadStatus(UUID ownerId, UUID channelId) {
        this.ownerId = ownerId;
        this.channelId = channelId;
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().toEpochMilli();
        this.updatedAt = createdAt;
    }

}
