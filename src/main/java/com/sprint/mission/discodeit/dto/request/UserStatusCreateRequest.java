package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;

public record UserStatusCreateRequest(
    User user,
    Instant lastActiveAt
) {

}
