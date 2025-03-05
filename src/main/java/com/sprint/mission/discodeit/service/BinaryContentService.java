package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    Long create(BinaryContentDTO.request binaryContentDto);

    BinaryContentDTO.response find(Long id);

    BinaryContentDTO.response find(UUID uuid);

    List<BinaryContentDTO.response> findAll();

    BinaryContentDTO.response delete(Long id);

    BinaryContentDTO.response delete(UUID uuid);

    BinaryContentDTO.convert findProfileByReferenceId(UUID referenceId);

    List<BinaryContentDTO.convert> findContentsByReferenceId(UUID referenceId);

    ResponseEntity<byte[]> getProfileImageAsResponse(UUID userId) throws IOException;

    ResponseEntity<byte[]> getMessageImageAsResponse(UUID userId) throws IOException;
}
