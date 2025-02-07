package com.sprint.mission.discodeit.dto.request.message;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;

import java.util.List;
import java.util.UUID;

public record MessageUpdateDTO(
        UUID messageId,
        String newContent,
        List<BinaryContentDTO> attachments
) {
}
