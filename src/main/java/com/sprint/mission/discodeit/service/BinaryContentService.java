package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
  void create(BinaryContentDto binaryContentDto);
  
  BinaryContentDto findById(UUID binaryContentId);
  
  List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds);
  
  void remove(UUID binaryContentId);
}
