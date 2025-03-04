package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
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

  public BinaryContent create(BinaryContentCreateRequest binaryContentCreateRequest) {
    if (binaryContentCreateRequest.fileName() == null
        || binaryContentCreateRequest.contentType() == null) {
      throw new NoSuchElementException("BinaryContent is null");
    }

    BinaryContent binaryContent = new BinaryContent(
        binaryContentCreateRequest.fileName(),
        (long) binaryContentCreateRequest.bytes().length,
        binaryContentCreateRequest.contentType(),
        binaryContentCreateRequest.bytes()
    );

    return binaryContentRepository.save(binaryContent);
  }

  public BinaryContent findById(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new NoSuchElementException(
            "BinaryContent with id " + binaryContentId + " not found"));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
        .toList();
  }

  public void deleteById(UUID binaryContentId) {
    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
    }
    binaryContentRepository.deleteById(binaryContentId);
  }
}
