package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public BinaryContent create(BinaryContentCreateRequest dto) {

    BinaryContent binaryContent =
        new BinaryContent(dto.getBytes(),
            dto.getFileName(),
            dto.getContentType(),
            (long) dto.getBytes().length);
    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public BinaryContent find(UUID id) {
    BinaryContent findBinaryContent = binaryContentRepository.findOne(id);
    Optional.ofNullable(findBinaryContent)
        .orElseThrow(() -> new NotFoundException(ErrorCode.BINARY_CONTENT_NOT_FOUND));
    return findBinaryContent;
  }

  @Override
  public List<BinaryContent> findAll() {
    return binaryContentRepository.findAll();
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllByIdIn(ids);
  }

  @Override
  public UUID delete(UUID id) {
    return binaryContentRepository.delete(id);
  }
}
