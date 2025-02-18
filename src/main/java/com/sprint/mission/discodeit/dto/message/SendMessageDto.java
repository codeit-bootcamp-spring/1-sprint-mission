package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public record SendMessageDto(
    UUID channelId,
    UUID senderId,
    UUID replyToId,
    List<BinaryContent> binaryContent,
    String content
) {
}
