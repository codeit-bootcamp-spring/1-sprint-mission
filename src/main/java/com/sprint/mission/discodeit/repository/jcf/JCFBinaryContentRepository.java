package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFBinaryContentRepository implements BinaryContentRepository {
  List<BinaryContent> data = new ArrayList<>();
  
  @Override
  public void save(BinaryContent binaryContent) {
    data.add(binaryContent);
  }
  
  @Override
  public Optional<BinaryContent> findById(UUID binaryContentId) {
    return data.stream()
        .filter(b -> b.getId().equals(binaryContentId))
        .findFirst();
  }
  
  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = new ArrayList<>();
    binaryContentIds.forEach(i -> {
      data.stream()
          .filter(b -> b.getId().equals(i))
          .findFirst()
          .ifPresent(binaryContents::add);
    });
    return binaryContents;
  }
  
  @Override
  public void remove(UUID binaryContentId) {
    findById(binaryContentId).ifPresent(data::remove);
  }
}
