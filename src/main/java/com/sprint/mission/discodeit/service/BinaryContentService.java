package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(BinaryContentRequestDto binaryContentRequestDto);
    BinaryContent findByUserId(UUID userId);
}
