package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest (
        UUID userId,
        Instant lastConnectAt
){
}
