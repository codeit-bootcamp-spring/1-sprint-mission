package com.sprint.mission.discodeit.repository.jpa.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BinaryContentRepositoryImpl implements BinaryContentRepository {

  private final BinaryContentJpaRepository binaryContentRepository;

  @Override
  public BinaryContent saveBinaryContent(BinaryContent binaryContent) {
    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public Optional<BinaryContent> findBinaryContentById(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId);
  }

  @Override
  public List<BinaryContent> findAllBinaryContentById(List<UUID> binaryContentIdList) {
    return binaryContentRepository.findAllById(binaryContentIdList);
  }

  @Override
  public void removeBinaryContent(UUID binaryContentId) {
    binaryContentRepository.deleteById(binaryContentId);
  }
}
