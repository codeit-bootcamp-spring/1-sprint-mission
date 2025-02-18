package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
  void create(BinaryContentDto binaryContentDto);
  
  BinaryContent findById(UUID binaryContentId);
  
  List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);
  
  void remove(UUID binaryContentId);
}
