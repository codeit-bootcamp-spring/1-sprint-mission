package com.sprint.mission.entity.addOn;

import com.sprint.mission.config.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "메시지 읽음 상태 정보")
@Getter
public class ReadStatus extends BaseTimeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
//    private Instant createdAt;
//    private Instant updatedAt;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void update(Instant newLastReadAt) {
        if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
            this.lastReadAt = newLastReadAt;
        }
    }
}
