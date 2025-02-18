package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
  private final BinaryContentRepository binaryContentRepository;
  
  @Override
  public void create(BinaryContentDto binaryContentDto) {
    BinaryContent binaryContent = new BinaryContent(binaryContentDto.uploadedById(), binaryContentDto.type());
    binaryContentRepository.save(binaryContent);
  }
  
  @Override
  public BinaryContent findById(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new NoSuchElementException("binary content not found with id: " + binaryContentId));
  }
  
  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIdIn(binaryContentIds);
  }
  
  @Override
  public void remove(UUID binaryContentId) {
    binaryContentRepository.remove(binaryContentId);
  }
}
