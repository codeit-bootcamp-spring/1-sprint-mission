package com.sprint.mission.discodeit.dto.user.userStatus;

import com.sprint.mission.discodeit.entity.UserStatusType;

import java.time.Instant;

public record UserStatusUpdate (UserStatusType type, Instant lastActiveAt) {
}
