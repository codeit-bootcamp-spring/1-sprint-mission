package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentDto create(BinaryContentCreateRequest request);
    BinaryContentDto findById(UUID binaryContentId);
    List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds);
    void delete(UUID binaryContentId);

//    List<BinaryContentDto> findAllByUserId(UUID userId);
//    List<BinaryContentDto> findByAllMessageId(UUID messageId);
//    void deleteByContentId(UUID id);
//    void deleteByUserId(UUID userId);
//    void deleteByMessageId(UUID messageId);
}
