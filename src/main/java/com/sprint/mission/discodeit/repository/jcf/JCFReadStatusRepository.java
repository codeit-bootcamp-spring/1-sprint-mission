package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Qualifier("jcf")
public class JCFReadStatusRepository implements ReadStatusRepository {
  private final List<ReadStatus> data = new ArrayList<>();
  
  @Override
  public void save(ReadStatus readStatus) {
    data.add(readStatus);
  }
  
  @Override
  public Optional<ReadStatus> findById(UUID readStatusId) {
    return data.stream()
        .filter(r -> r.getId().equals(readStatusId))
        .findFirst();
  }
  
  @Override
  public List<ReadStatus> findAllByUser(UUID userId) {
    List<ReadStatus> readStatuses = data.stream()
        .filter(r -> r.getUserid().equals(userId))
        .toList();
    return new ArrayList<>(readStatuses);
  }
  
  @Override
  public void remove(UUID readStatusId) {
    findById(readStatusId).ifPresent(data::remove);
  }
}
