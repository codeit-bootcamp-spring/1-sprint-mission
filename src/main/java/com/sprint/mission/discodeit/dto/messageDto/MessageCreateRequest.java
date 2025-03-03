package com.sprint.mission.discodeit.dto.messageDto;

import java.util.UUID;

public record MessageCreateRequest(
    String content,
    UUID channelId,
    UUID authorId
) {

}
