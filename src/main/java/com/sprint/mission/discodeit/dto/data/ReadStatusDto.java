package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
    UUID id, UUID userUd, UUID channnelId, Instant lastReadAt
    ) {

}
