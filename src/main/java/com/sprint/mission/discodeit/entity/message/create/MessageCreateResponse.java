package com.sprint.mission.discodeit.entity.message.create;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentResponse;

import java.util.List;
import java.util.UUID;

public record MessageCreateResponse(
    UUID messageId,
    String messageTitle,
    String messageContent,
    UUID senderId,
    UUID receiverId,
    List<BinaryContentResponse> files
) {

}
