package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.UserStatusType;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreate (UUID userId, UserStatusType type, Instant lastActiveAt){

}
