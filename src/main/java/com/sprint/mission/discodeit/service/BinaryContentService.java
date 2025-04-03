package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContentDto create(BinaryContentRequest binaryContentRequest);

  BinaryContentDto findById(UUID uuid);

  List<BinaryContentDto> findAllByIdIn(List<UUID> uuidList);

  void delete(UUID uuid);
}
