package com.sprint.mission.discodeit.dto.request.channel;

import java.util.UUID;

public record ChannelUpdateDTO(
        UUID channelId,
        String newName,
        String newDescription
) {
}
