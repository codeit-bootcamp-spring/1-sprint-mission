package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        String content,
        UUID authorId,
        UUID channelId,
        List<byte[]> binaryContentData
) {
    public static MessageResponse from(UUID id, String content, UUID authorId, UUID channelId, List<byte[]> binaryContentData) {
        return new MessageResponse(id, content, authorId, channelId, binaryContentData);
    }
}
