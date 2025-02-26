package com.sprint.mission.discodeit.entity.user;

import java.util.UUID;

public record UserChannelOwnerResponse (
        UUID id,
        String userName,
        UserRole userRole
) {
}
