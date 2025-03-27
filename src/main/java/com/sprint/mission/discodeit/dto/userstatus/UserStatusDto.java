package com.sprint.mission.discodeit.dto.userstatus;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserStatusDto(
    UUID id,
    UUID userid,
    Instant lastActiveAt
) {

}
