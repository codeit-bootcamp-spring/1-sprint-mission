package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.userstatus.UserStatusType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class UserStatusResponse {
    private UUID id;
    private UUID userId;
    private UserStatusType status;
    private Instant updatedAt;
}
