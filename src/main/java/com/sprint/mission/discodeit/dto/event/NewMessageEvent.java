package com.sprint.mission.discodeit.dto.event;

import java.util.UUID;

public record NewMessageEvent(
    UUID channelId,
    UUID authorId,
    String content
) {

}
