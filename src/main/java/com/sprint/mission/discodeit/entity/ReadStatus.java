package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity{
    private UUID userId;
    private UUID channelId;
    private final Instant readAt;
    private Instant sendAt;

    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.readAt = Instant.now();
    }
}
