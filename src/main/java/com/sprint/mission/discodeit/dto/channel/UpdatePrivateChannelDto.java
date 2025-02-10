package com.sprint.mission.discodeit.dto.channel;

import java.util.List;
import java.util.UUID;

public record UpdatePrivateChannelDto(
    UUID channelId,
    List<UUID> memberIds
) {
}
