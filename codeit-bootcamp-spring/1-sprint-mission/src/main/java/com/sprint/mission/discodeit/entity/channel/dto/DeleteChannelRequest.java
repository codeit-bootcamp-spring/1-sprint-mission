package com.sprint.mission.discodeit.entity.channel.dto;

import java.util.UUID;

public record DeleteChannelRequest(UUID userId, UUID channelId) {
}
