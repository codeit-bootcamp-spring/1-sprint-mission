package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    User user,
    Instant lastConnectAt
) {

}
