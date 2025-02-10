package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
  void save(BinaryContent binaryContent);
  
  Optional<BinaryContent> findById(UUID binaryContentId);
  
  List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);
  
  void remove(UUID binaryContentId);
}
