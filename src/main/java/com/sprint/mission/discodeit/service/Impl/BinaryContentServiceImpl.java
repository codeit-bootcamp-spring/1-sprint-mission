package com.sprint.mission.discodeit.service.Impl;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BinaryContentServiceImpl implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @Transactional
  @Override
  public BinaryContentDto create(String fileName, Long size, String contentType, byte[] data) {
    UUID id = UUID.randomUUID();

    try {
      binaryContentStorage.put(id, data);

      BinaryContent binaryContent = BinaryContent.builder()
          .fileName(fileName)
          .size(size)
          .contentType(contentType)
          .build();

      BinaryContent savedContent = binaryContentRepository.save(binaryContent);
      log.info("바이너리 콘텐츠 저장 완료: {}", savedContent.getId());
      return binaryContentMapper.toDto(savedContent);
    } catch (Exception e) {
      log.error("파일 저장 실패: {}", e.getMessage());
      throw new RestApiException(DomainErrorCode.FILE_STORAGE_FAILED, "파일 저장 실패");
    }
  }

  @Override
  public BinaryContentDto find(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new RestApiException(DomainErrorCode.FILE_NOT_FOUND, "파일을 찾을 수 없습니다"));
    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
        .map(binaryContentMapper::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public void delete(UUID binaryContentId) {
    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new RestApiException(DomainErrorCode.FILE_NOT_FOUND, "파일을 찾을 수 없습니다");
    }
    binaryContentRepository.deleteById(binaryContentId);
  }
}
