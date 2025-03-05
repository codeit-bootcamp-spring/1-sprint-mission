package com.sprint.mission.discodeit.entity.message;

import java.util.UUID;

public record MessageAndFileCreateRequest(
    String content,
    UUID channelId,
    UUID authorId
) {

}
