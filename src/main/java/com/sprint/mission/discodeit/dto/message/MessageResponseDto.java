package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageResponseDto(
        UUID id,
        String content,
        UUID authorId,
        UUID channelId,
        List<byte[]> binaryContentData
) {
    public static MessageResponseDto from(UUID id, String content, UUID authorId, UUID channelId, List<byte[]> binaryContentData) {
        return new MessageResponseDto(id, content, authorId, channelId, binaryContentData);
    }
}
