package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * - 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현
 * - 사용자 별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용
 */
@Getter
public class ReadStatus implements Serializable {
    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;
    private Instant createdAt;
    private Instant updatedAt;

    public ReadStatus(ReadStatusDTO.request readStatusReqDTO) {
        this.id = UUID.randomUUID();
        this.userId = readStatusReqDTO.userId();
        this.channelId = readStatusReqDTO.channelId();
        this.lastReadAt = readStatusReqDTO.lastReadAt();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void updateLastReadAt() {    // 읽을 시 시간 갱신을 위함
        lastReadAt = Instant.now();
        updatedAt = Instant.now();
    }

    public void updateLastReadAt(Instant time) {    // 읽을 시 시간 갱신을 위함
        lastReadAt = time;
        updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "ReadStatus{" +
                "id=" + id +
                ", userId=" + userId +
                ", channelId=" + channelId +
                ", lastReadAt=" + lastReadAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
