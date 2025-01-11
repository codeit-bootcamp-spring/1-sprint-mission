package com.sprint.mission.discodeit.entity.channel.dto;

import java.util.UUID;

public record CreateNewChannelRequest(UUID userId, String channelName) {
}
