package com.sprint.mission.discodeit.event;

import java.util.UUID;

public record UserDeletedEvent(UUID userId, UUID profileImageId, UUID userStatusId) {
}
