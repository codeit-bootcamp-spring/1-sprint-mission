package com.sprint.mission.discodeit.dto.userStatus.request;

import java.util.UUID;

public record UpdateUserStatusByIdRequest(
        UUID userStatusId,
        boolean isOnline
) {
}
