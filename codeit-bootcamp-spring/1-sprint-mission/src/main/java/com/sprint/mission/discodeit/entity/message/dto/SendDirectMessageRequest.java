package com.sprint.mission.discodeit.entity.message.dto;

import java.util.UUID;

public record SendDirectMessageRequest(UUID sendUserId, UUID receiveUserId, String message) {
}
