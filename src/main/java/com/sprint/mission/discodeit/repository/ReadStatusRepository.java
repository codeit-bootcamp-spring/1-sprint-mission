package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
  ReadStatus save(ReadStatus paramReadStatus);

  Optional<ReadStatus> findById(UUID paramUUID);

  List<ReadStatus> findAll();

  List<ReadStatus> findAllByUserId(UUID paramUUID);

  void deleteById(UUID paramUUID);
}
