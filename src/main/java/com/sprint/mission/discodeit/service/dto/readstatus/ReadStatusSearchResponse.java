package com.sprint.mission.discodeit.service.dto.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusSearchResponse(
    UUID userId,
    Instant createAt,
    Instant updateAt
) {

    public static ReadStatusSearchResponse from(ReadStatus readStatus) {
        return new ReadStatusSearchResponse(
            readStatus.userId(),
            readStatus.createAt(),
            readStatus.updateAt()
        );
    }
}
