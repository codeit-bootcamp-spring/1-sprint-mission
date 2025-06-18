package com.sprint.mission.discodeit.dto.event;

import java.util.UUID;

public record MessageCreatedEvent(
    UUID channelId,
    String messageContent
) {

}
