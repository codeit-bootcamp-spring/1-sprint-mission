package com.sprint.mission.discodeit.dto.response.message;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDTO(
        UUID id,
        UUID userId,
        UUID channelId,
        String content,
        List<BinaryContentDTO>attachments, // 첨부 파일들
        Instant createdAt
) {
}
