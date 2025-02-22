package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContent create(BinaryContentCreateRequest request);

  BinaryContentDto findById(UUID binaryContentId);

  List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds);

  void delete(UUID binaryContentId);
}
