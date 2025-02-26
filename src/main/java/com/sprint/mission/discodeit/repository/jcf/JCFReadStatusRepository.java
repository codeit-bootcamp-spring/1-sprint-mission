package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.global.config.JCFRepositoryCondition;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@NoArgsConstructor
@JCFRepositoryCondition
public class JCFReadStatusRepository implements ReadStatusRepository {

  private final Map<UUID, ReadStatus> readStatusData = new HashMap<>();

  @Override
  public ReadStatus saveReadStatus(ReadStatus readStatus) {
    readStatusData.put(readStatus.getId(), readStatus);
    return readStatus;
  }

  @Override
  public ReadStatus findReadStatusById(UUID readStatusId) {
    return readStatusData.get(readStatusId);
  }

  @Override
  public Optional<ReadStatus> findReadStatusByUserId(UUID userId) {
    return readStatusData.values().stream()
        .filter(readStatus -> readStatus.getUser().getId().equals(userId))
        .findFirst();
  }

  @Override
  public List<ReadStatus> findAllReadStatusByUserId(UUID userId) {
    return readStatusData.values().stream()
        .filter(readStatus -> readStatus.getUser().getId().equals(userId))
        .toList();
  }

  @Override
  public void removeReadStatus(UUID readStatusId) {
    readStatusData.remove(readStatusId);
  }
}
