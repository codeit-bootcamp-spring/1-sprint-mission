package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.BinaryContentRequestDto;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequestDto(
        String content,
        UUID authorId,
        UUID channelId,
        List<BinaryContentRequestDto> binaryContentData
) {
}
