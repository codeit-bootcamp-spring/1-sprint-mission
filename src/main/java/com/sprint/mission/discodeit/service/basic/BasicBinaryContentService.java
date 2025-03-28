package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Transactional
  @Override
  public BinaryContent create(BinaryContentCreateRequest request) {
    Long size = (request.file() != null) ? (long) request.file().length : 0;
    BinaryContent binaryContent = new BinaryContent(
        request.fileName(),
        request.contentType(),
        request.file(),
        size
    );
    return binaryContentRepository.save(binaryContent);
  }

  @Transactional(readOnly = true)
  @Override
  public BinaryContentDto find(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new NoSuchElementException("파일이 존재하지 않습니다."));

    return BinaryContentDto.fromEntity(binaryContent);
  }

  @Transactional(readOnly = true)
  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentRepository.findAllById(binaryContentIds);
    return binaryContents.stream()
        .map(BinaryContentDto::fromEntity)
        .toList();
  }

  @Transactional
  @Override
  public void delete(UUID binaryContentId) {
    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new NoSuchElementException("파일이 존재하지 않습니다.");
    }
    binaryContentRepository.deleteById(binaryContentId);
  }
}
