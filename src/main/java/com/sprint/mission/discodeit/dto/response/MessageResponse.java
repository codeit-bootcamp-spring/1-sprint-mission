package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public record MessageResponse(
    UUID id,
    String content,
    UUID senderId,
//    UUID recipientId,
    UUID channelId
) { }
