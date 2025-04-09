package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class UserStatusCreateRequest {
    UUID userId;
    Instant lastActiveAt;
}
