package com.sprint.mission.discodeit.dto.request.user;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDTO(UUID userId, Instant lastSeenAt, boolean isOnline) {

}
