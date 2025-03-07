package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontetnt.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentService {

  BinaryContent create(CreateBinaryContentRequest request);

  Optional<BinaryContent> getBinaryContent(UUID id);

  BinaryContent saveBinaryContent(BinaryContent binaryContent);

  void deleteBinaryContent(UUID id);

  List<BinaryContent> getBinaryContentListByIds(List<UUID> ids);

  ResponseEntity<?> downloadBinaryContent(UUID id);
}
