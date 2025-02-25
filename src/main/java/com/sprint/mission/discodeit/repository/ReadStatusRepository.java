package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
  void save(ReadStatus readStatus);
  
  Optional<ReadStatus> findById(UUID readStatusId);
  
  List<ReadStatus> findAllByUser(UUID userId);
  
  void remove(UUID channelId);
}
