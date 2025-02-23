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
    BinaryContent binaryContent = new BinaryContent(binaryContentDto.uploadedById(),
        binaryContentDto.fileName(),
        binaryContentDto.size(),
        binaryContentDto.contentType(),
        binaryContentDto.bytes());
    binaryContentRepository.save(binaryContent);
  }
  
  @Override
  public BinaryContentDto findById(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new NoSuchElementException("binary content not found with id: " + binaryContentId));
    return new BinaryContentDto(binaryContent.getUploadedById(),
        binaryContent.getFileName(),
        binaryContent.getSize(),
        binaryContent.getContentType(),
        binaryContent.getBytes());
    
  }
  
  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentRepository.findAllByIdIn(binaryContentIds);
    return binaryContents.stream()
        .map(b -> new BinaryContentDto(b.getUploadedById(),
            b.getFileName(),
            b.getSize(),
            b.getContentType(),
            b.getBytes()))
        .toList();
  }
  
  @Override
  public void remove(UUID binaryContentId) {
    binaryContentRepository.remove(binaryContentId);
  }
}
