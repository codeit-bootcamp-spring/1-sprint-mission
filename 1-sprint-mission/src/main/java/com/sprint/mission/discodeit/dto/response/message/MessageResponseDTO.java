package com.sprint.mission.discodeit.dto.response.message;

import com.sprint.mission.discodeit.dto.response.binary.BinaryContentDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDTO(
    UUID messageId,
    UUID userId,
    UUID channelId,
    String content,
    List<BinaryContentDTO> attachments, // 첨부 파일들
    Instant createdAt
) {

}
