package com.sprint.mission.discodeit.dto.channelService;

import java.util.UUID;

public record ChannelUpdateRequest(
        UUID channelId,
        String newName,
        String newDescription
) {
}
