package com.sprint.mission.discodeit.entity.user.dto;

import java.util.UUID;

public record ExitChannelRequest(UUID userId, UUID channelId) {
}
