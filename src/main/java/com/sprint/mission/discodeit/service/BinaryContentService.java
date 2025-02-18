package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    Long create(BinaryContentDTO.request binaryContentDto);

    BinaryContentDTO.response find(Long id);

    BinaryContentDTO.response find(UUID uuid);

    List<BinaryContentDTO.response> findAll();

    BinaryContentDTO.response delete(Long id);

    BinaryContentDTO.response delete(UUID uuid);
}
