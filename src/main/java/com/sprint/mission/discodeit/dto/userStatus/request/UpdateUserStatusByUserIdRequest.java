package com.sprint.mission.discodeit.dto.userStatus.request;

import java.util.UUID;

public record UpdateUserStatusByUserIdRequest(
        UUID userId,
        boolean isOnline
) {
}
