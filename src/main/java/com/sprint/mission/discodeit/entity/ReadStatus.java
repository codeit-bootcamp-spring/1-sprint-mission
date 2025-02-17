package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public ReadStatus() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        //
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }


    public void update(Instant newLastReadAt) {
        boolean anyValueUpdated = false;
        if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
            this.lastReadAt = newLastReadAt;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}
