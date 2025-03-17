package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.NoArgsConstructor;

//@Repository
@NoArgsConstructor
//@JCFRepositoryCondition
public class JCFBinaryContentRepository implements BinaryContentRepository {

  private final Map<UUID, BinaryContent> binaryContentData = new HashMap<>();

  @Override
  public BinaryContent saveBinaryContent(BinaryContent binaryContent) {
    binaryContentData.put(binaryContent.getId(), binaryContent);
    return binaryContent;
  }

  @Override
  public Optional<BinaryContent> findBinaryContentById(UUID binaryContentId) {
    return Optional.ofNullable(binaryContentData.get(binaryContentId));
  }

  @Override
  public List<BinaryContent> findAllBinaryContentById(List<UUID> binaryContentIdList) {
    List<BinaryContent> binaryContentList = new ArrayList<>();

    for (UUID binaryContentId : binaryContentIdList) {
      binaryContentList.add(binaryContentData.get(binaryContentId));
    }
    return binaryContentList;
  }

  @Override
  public void removeBinaryContent(UUID binaryContentId) {
    binaryContentData.remove(binaryContentId);
  }
}
