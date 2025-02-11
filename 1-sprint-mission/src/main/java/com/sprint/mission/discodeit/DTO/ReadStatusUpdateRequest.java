package com.sprint.mission.discodeit.DTO;

import java.util.UUID;

public record ReadStatusUpdateRequest(UUID id,Boolean isRead) {
}
