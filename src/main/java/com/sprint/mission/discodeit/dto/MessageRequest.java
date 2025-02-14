package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record MessageRequest(
        String content,
        UUID senderId,
        UUID recipientId,
        UUID channelId,
        UUID attachedFileId
) {


}
