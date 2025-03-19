package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(CreateBinaryContentRequestDto createBinaryContentRequestDto) throws IOException;
    BinaryContentDto find(UUID id);
    List<BinaryContentDto> findAll();
    List<UUID> findAllByIdIn();
    void delete(UUID id);
}
