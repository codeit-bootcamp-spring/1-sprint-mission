package com.sprint.mission.discodeit.dto.request.user;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateDTO(
    UUID userId,
    Instant lastActiveAt

) {

}
