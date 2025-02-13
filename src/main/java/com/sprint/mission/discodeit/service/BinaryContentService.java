package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

public interface BinaryContentService {
    BinaryContent create(BinaryContentRequestDto binaryContentRequestDto);
}
