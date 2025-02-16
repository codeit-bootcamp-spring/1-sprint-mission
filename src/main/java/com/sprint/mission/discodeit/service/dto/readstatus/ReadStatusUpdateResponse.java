package com.sprint.mission.discodeit.service.dto.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateResponse(
    UUID userId,
    Instant createAt,
    Instant updateAt
) {

    public static ReadStatusUpdateResponse from(ReadStatus readStatus) {
        return new ReadStatusUpdateResponse(
            readStatus.userId(),
            readStatus.createAt(),
            readStatus.updateAt()
        );
    }
}
