package com.sprint.mission.discodeit.dto.status;

import java.time.Instant;

public record UpdateUserStatusRequest(Instant lastActiveAt) {
}
