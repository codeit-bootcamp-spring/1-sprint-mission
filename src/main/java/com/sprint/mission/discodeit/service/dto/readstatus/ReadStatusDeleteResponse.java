package com.sprint.mission.discodeit.service.dto.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusDeleteResponse(
    UUID userId,
    Instant createAt,
    Instant updateAt
) {

    public static ReadStatusDeleteResponse from(ReadStatus readStatus) {
        return new ReadStatusDeleteResponse(
            readStatus.userId(),
            readStatus.createAt(),
            readStatus.updateAt()
        );
    }
}
