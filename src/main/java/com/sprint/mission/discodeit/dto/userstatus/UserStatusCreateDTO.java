package com.sprint.mission.discodeit.dto.userstatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateDTO
        (UUID userId,
         Instant lastAccessedAt
         )
{
}