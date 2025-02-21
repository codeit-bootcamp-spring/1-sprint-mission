package com.sprint.mission.discodeit.service.dto.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusRegisterResponse(
    UUID userId,
    Instant createAt,
    Instant updateAt
) {

    public static ReadStatusRegisterResponse from(ReadStatus readStatus) {
        return new ReadStatusRegisterResponse(
            readStatus.userId(),
            readStatus.createAt(),
            readStatus.updateAt()
        );
    }
}
