package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {

  private final Map<UUID, ReadStatus> data = new HashMap<>();

  @Override
  public ReadStatus save(ReadStatus readStatuses) {
    this.data.put(readStatuses.getId(), readStatuses);
    return readStatuses;
  }

  @Override
  public Optional<ReadStatus> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return this.data.values().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId))
        .toList();
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    List<ReadStatus> result = new ArrayList<>();
    for (ReadStatus readStatus : data.values()) {
      if (readStatus.getChannelId().equals(channelId)) {
        result.add(readStatus);
      }
    }
    return result;
  }

  @Override
  public void deleteById(UUID id) {
    data.remove(id);
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }

  @Override
  public List<UUID> findMembersByChannelId(UUID id) {
    List<UUID> members = new ArrayList<>();
    for (ReadStatus readStatus : data.values()) {
      if (readStatus.getChannelId().equals(id)) {
        members.add(readStatus.getUserId());
      }
    }
    return members;
  }

  @Override
  public void deleteByChannelId(UUID id) {
    data.values().removeIf(readStatus -> readStatus.getChannelId().equals(id));
  }

  @Override
  public boolean isUserMemberOfChannel(UUID userId, UUID channelId) {
    return data.values().stream()
        .anyMatch(readStatus -> readStatus.getUserId().equals(userId) && readStatus.getChannelId()
            .equals(channelId));
  }
}
