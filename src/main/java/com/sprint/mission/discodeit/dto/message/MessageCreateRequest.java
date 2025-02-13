package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public record MessageCreateRequest(
        UUID channelId,
        UUID authorId,
        String messageText,
        int binaryContentNum
){
}
