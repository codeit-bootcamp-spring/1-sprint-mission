package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDto.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.binaryContentDto.FindBinaryContentResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    UUID create(CreateBinaryContentRequestDto createBinaryContentRequestDto) throws IOException;
    FindBinaryContentResponseDto find(UUID id);
    List<FindBinaryContentResponseDto> findAll();
    List<UUID> findAllByIdIn();
    void delete(UUID id);
}
