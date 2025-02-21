package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;

import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID userId,
        UUID channelId,
        String content,
        List<UUID> binaryContentIds //여러 개의 첨부파일을 등록
) {
}
