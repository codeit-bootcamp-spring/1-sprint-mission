package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = 3735382067812923186L;

    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    //
    private final UUID userId;
    private final UUID channelId;
    private final Instant lastReadAt;

    public static ReadStatus createReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        return new ReadStatus(UUID.randomUUID(), Instant.now(), null, userId, channelId,
            lastReadAt);
    }

    public ReadStatus update(Instant lastReadAt) {
        if (lastReadAt == null) {
            throw new IllegalArgumentException();
        }

        if (lastReadAt.equals(this.lastReadAt)) {
            return this;
        }

        return new ReadStatus(
            this.id,
            this.createdAt,
            Instant.now(),
            this.userId,
            this.channelId,
            lastReadAt
        );
    }
}
