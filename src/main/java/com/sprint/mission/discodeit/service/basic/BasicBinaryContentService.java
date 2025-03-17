package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public BinaryContentDto create(BinaryContentRequest request) {
    BinaryContent binaryContent = binaryContentRepository.save(new BinaryContent(
        request.fileName(),
        (long) request.bytes().length,
        request.contentType()
    ));
    binaryContentStorage.put(binaryContent.getId(), request.bytes());

    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  @Transactional(readOnly = true)
  public BinaryContentDto find(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .map(binaryContentMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 상태입니다."));
  }

  @Override
  @Transactional(readOnly = true)
  public List<BinaryContentDto> findAll() {
    return binaryContentRepository.findAll().stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllById(binaryContentIds).stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public void delete(UUID binaryContentId) {
    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new NoSuchElementException("[ERROR] 존재하지 않는 상태입니다.");
    }

    binaryContentRepository.deleteById(binaryContentId);
  }
}