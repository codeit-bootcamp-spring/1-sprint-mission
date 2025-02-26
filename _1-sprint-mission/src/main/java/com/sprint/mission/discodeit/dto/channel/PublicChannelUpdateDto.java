package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record PublicChannelUpdateDto(
        String newChannelName,
        String newDescription
) {
}
