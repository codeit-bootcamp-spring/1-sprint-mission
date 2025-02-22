package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jcf")
public class JCFReadStatusRepository implements ReadStatusRepository{

  private final Map<String, ReadStatus> data = new ConcurrentHashMap<>();

  @Override
  public ReadStatus save(ReadStatus status) {
    data.put(status.getId(), status);
    return status;
  }

  @Override
  public Optional<ReadStatus> findById(String id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<ReadStatus> findByUserId(String userId) {
    return data.values().stream()
        .filter(status -> status.getUserId().equals(userId))
        .toList();
  }

  @Override
  public List<ReadStatus> findByChannelId(String id) {
    return data.values().stream()
        .filter(status -> status.getChannelId().equals(id))
        .toList();
  }

  @Override
  public Optional<ReadStatus> findByChannelIdAndUserId(String channelId, String userId) {
    return data.values().stream()
        .filter(status ->
            status.getChannelId().equals(channelId)
                && status.getUserId().equals(userId))
        .findAny();
  }

  @Override
  public List<ReadStatus> findAll() {
    return data.values().stream().toList();
  }

  @Override
  public void deleteByUserId(String id) {
    data.values().removeIf(status -> status.getUserId().equals(id));
  }

  @Override
  public void deleteByChannelId(String channelId) {
    data.values().removeIf(status -> status.getChannelId().equals(channelId));
  }

  @Override
  public void clear() {
    data.clear();
  }
}
