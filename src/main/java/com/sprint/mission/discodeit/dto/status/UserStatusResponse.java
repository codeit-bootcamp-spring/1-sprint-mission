package com.sprint.mission.discodeit.dto.status;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(UUID id, Instant lastActiveAt) {

}
