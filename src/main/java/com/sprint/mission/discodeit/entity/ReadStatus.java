package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant readAt;

    public ReadStatus(UUID userId, UUID channelId, Instant readAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.readAt = readAt;
    }

    @Override
    public String toString() {
        return "ReadStatus{" +
                "id=" + id +
                ", userId=" + userId +
                ", channelId=" + channelId +
                ", readAt=" + readAt +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
