package com.sprint.mission.discodeit.dto.userstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusCreateRequest {
    private UUID userId;
    private Instant lastSeenAt;
}
