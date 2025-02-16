package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record UpdateMessageRequest(UUID uuid, String text) {
}
