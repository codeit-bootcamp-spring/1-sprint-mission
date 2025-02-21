package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
    private Instant lastReadTime;

    public ReadStatus() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public ReadStatus(UUID userId, UUID channelId){
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadTime = Instant.now();
    }

    public void update(Instant readTime){
        this.lastReadTime = readTime;
        this.updatedAt = Instant.now();
    }
}
