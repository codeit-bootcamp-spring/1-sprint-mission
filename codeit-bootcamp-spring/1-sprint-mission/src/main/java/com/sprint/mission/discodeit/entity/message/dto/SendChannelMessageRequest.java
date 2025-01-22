package com.sprint.mission.discodeit.entity.message.dto;

import java.util.UUID;

public record SendChannelMessageRequest(UUID sendUserId, UUID receiveChannelId, String message) {

}
