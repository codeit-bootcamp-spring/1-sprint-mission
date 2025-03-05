package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record CreateMessageRequest(UUID authorID, UUID channelID, String text) {
}
