package com.sprint.mission.discodeit.dto.message.request;

import java.util.UUID;

public record CreateMessageRequest(
    String content,
    UUID channelId,
    UUID authorId
) {

}
