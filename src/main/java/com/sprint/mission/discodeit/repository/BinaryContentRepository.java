package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
  BinaryContent save(BinaryContent paramBinaryContent);

  Optional<BinaryContent> findById(UUID paramUUID);

  List<BinaryContent> findAll();

  List<BinaryContent> findAllByIdIn(List<UUID> paramList);

  void deleteById(UUID paramUUID);
}
