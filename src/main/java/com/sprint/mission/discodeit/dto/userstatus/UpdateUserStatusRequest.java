package com.sprint.mission.discodeit.dto.userstatus;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserStatusRequest(UUID id, Instant lastActiveAt) {
}
