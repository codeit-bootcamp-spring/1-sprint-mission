package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontetnt.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.binarycontetnt.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentService {

  BinaryContentResponse create(CreateBinaryContentRequest request);

  Optional<BinaryContentResponse> getBinaryContent(UUID id);

  BinaryContentResponse saveBinaryContent(BinaryContent binaryContent);

  void deleteBinaryContent(UUID id);

  List<BinaryContentResponse> getBinaryContentListByIds(List<UUID> ids);

  ResponseEntity<?> downloadBinaryContent(UUID id);
}
