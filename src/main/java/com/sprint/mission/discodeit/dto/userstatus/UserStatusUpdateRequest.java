package com.sprint.mission.discodeit.dto.userstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class UserStatusUpdateRequest {
    private Instant updateLastSeen;
}
