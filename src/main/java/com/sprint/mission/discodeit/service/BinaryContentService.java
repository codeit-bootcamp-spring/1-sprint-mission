package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentService {

  @Transactional
  BinaryContent createBinaryContent(BinaryContentCreateRequest binaryContentCreateRequest);

  BinaryContentDto findBinaryContentById(UUID binaryContentId);

  List<BinaryContentDto> findAllByIdIn(List<UUID> ids);

  ResponseEntity<?> downloadBinaryContent(UUID binaryContentId);

  @Transactional
  void deleteBinaryContentById(UUID binaryContentId);
}
