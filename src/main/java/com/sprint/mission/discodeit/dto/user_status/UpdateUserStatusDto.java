package com.sprint.mission.discodeit.dto.user_status;

import com.sprint.mission.discodeit.util.UserStatusType;


public record UpdateUserStatusDto(
    UserStatusType status
) {
}
