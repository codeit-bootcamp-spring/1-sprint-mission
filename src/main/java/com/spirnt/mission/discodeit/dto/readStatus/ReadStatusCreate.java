package com.spirnt.mission.discodeit.dto.readStatus;

import java.util.UUID;

public record ReadStatusCreate(
        UUID userId,
        UUID channelId
) {
}
