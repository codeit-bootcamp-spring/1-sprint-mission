package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.binarycontent.dto.BinaryContentResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentService {

  BinaryContentResponse find(UUID binaryContentId);

  List<BinaryContentResponse> findAll(List<String> binaryContentIds);

  ResponseEntity<?> downloadBinaryContent(UUID binaryContentId);
}
