package com.sprint.mission.discodeit.dto.status;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(UUID id, Instant lastActiveAt) {

}
