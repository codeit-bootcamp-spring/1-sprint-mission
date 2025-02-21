package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public record MessageCreateDTO(
        UUID userId,
        UUID channelId,
        String content,
        List<BinaryContent>binaryContentList
) {
}
