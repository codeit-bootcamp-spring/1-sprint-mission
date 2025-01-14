package com.sprint.mission.discodeit.entity.channel.dto;

import java.util.UUID;

public record ChangeChannelNameRequest(UUID userId, UUID channelId, String newChannelName) {
}
