package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentService {

  BinaryContent saveBinaryContent(BinaryContentDto request);

  BinaryContentDto find(UUID id);

  List<BinaryContentDto> findAllByIdIn(List<UUID> ids);

  void delete(UUID id);
}
