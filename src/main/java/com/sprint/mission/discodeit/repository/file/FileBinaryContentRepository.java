package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
public class FileBinaryContentRepository implements BinaryContentRepository {

  private HashMap<UUID, BinaryContent> data;
  private SerializationUtil<UUID, BinaryContent> util;

  public FileBinaryContentRepository(SerializationUtil<UUID, BinaryContent> util) {
    this.util = util;
    this.data = util.loadData();
  }

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    this.data.put(binaryContent.getId(), binaryContent);
    util.saveData(data);
    return binaryContent;
  }

  @Override
  public Optional<BinaryContent> findById(UUID id) {
    return Optional.ofNullable(this.data.get(id));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> contentIds) {
    return this.data.values().stream()
        .filter(content -> contentIds.contains(content.getId()))
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

}
