package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void updateLastReadAt(Instant lastReadAt) {
        this.lastReadAt = lastReadAt;
    }
}
