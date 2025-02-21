package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.BinaryContentRequest;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
        String content,
        UUID authorId,
        UUID channelId,
        List<BinaryContentRequest> binaryContentData
) {
}
