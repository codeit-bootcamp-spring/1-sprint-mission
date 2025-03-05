package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import java.util.HashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

  private HashMap<UUID, ReadStatus> data;
  private SerializationUtil<UUID, ReadStatus> util;

  public FileReadStatusRepository(SerializationUtil<UUID, ReadStatus> util) {
    this.util = util;
    this.data = util.loadData();
  }

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    this.data.put(readStatus.getId(), readStatus);
    util.saveData(data);
    return readStatus;
  }

  @Override
  public Optional<ReadStatus> findById(UUID id) {
    return Optional.ofNullable(this.data.get(id));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return this.data.values().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId))
        .toList();
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return this.data.values().stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channelId))
        .toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }

  @Override
  public void deleteById(UUID id) {
    this.data.remove(id);
    util.saveData(data);
  }

  @Override
  public void deleteAllByChannelId(UUID channelId) {
    this.findAllByChannelId(channelId)
        .forEach(readStatus -> this.deleteById(readStatus.getId()));
    util.saveData(data);
  }
}