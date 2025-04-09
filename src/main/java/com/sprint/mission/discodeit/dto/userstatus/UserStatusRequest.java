package com.sprint.mission.discodeit.dto.userstatus;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserStatusRequest
    (UUID userId,
     Instant lastAccessedAt
    ) {

}