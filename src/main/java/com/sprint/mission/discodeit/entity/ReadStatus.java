package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now();
    // TODO : 롬복 생성자 대상이 아닌데 = null 초기화 안해줘도 되나?
    private Instant updatedAt;

    private final UUID userId;
    private final UUID channelId;
    @NonNull
    private Instant lastReadAt;

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

    @Override
    // TODO : toString()
    public String toString() {
        return "ReadStatus{" +
                "updatedAt=" + updatedAt +
                "}";
    }
}
