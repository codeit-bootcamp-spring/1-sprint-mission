package com.sprint.mission.discodeit.dto.MessageService;

import com.sprint.mission.discodeit.dto.binaryContentService.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public record MessageUpdateRequestDTO(
        String newContent
) {
}
