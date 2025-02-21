package com.sprint.mission.discodeit.service.dto.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateRequest(
    UUID userId,
    Instant createAt,
    Instant updateAt
) {

    public static ReadStatusUpdateRequest from(ReadStatus readStatus) {
        return new ReadStatusUpdateRequest(
            readStatus.userId(),
            readStatus.createAt(),
            readStatus.updateAt()
        );
    }
}
