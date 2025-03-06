package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {

  private final Map<UUID, BinaryContent> data;

  public JCFBinaryContentRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    this.data.put(binaryContent.getId(), binaryContent);
    return binaryContent;
  }

  @Override
  public Optional<BinaryContent> findById(UUID id) {
    return Optional.ofNullable(this.data.get(id));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
    return this.data.values().stream()
        .filter(content -> ids.contains(content.getId()))
        .toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }

  @Override
  public void deleteById(UUID id) {
    this.data.remove(id);
  }
}
