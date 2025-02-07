package com.sprint.mission.discodeit.dto.ReadStatusService;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequestDTO(
        UUID userId,
        UUID channelId,
        UUID messageId,
        Instant lastReadAt
) {
    public ReadStatus from() {
        return new ReadStatus(
                userId,
                channelId,
                messageId,
                lastReadAt != null ? lastReadAt : Instant.now() // 기본값 설정
        );
    }
}
