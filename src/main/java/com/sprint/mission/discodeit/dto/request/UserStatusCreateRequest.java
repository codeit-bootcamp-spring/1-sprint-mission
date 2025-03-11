package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    User user,
    Instant lastActiveAt
) {

}
