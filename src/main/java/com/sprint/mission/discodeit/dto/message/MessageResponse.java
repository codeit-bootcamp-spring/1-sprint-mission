package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageResponse(UUID id, String text, UUID authorId, UUID channelId) {
}
