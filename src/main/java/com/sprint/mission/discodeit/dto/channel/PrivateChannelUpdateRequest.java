package com.sprint.mission.discodeit.dto.channel;

import java.util.List;
import java.util.UUID;

public record PrivateChannelUpdateRequest(
    UUID channelId,
    List<UUID> memberIds
) {
}
