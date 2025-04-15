package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binaryContent.FileNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.io.File;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public BinaryContentDto createBinaryContent(BinaryContentCreateRequest request) {
    BinaryContent binaryContent = BinaryContent.builder()
        .fileName(request.fileName())
        .size(request.size())
        .contentType(request.contentType())
        .build();
    binaryContentRepository.save(binaryContent);

    binaryContentStorage.put(binaryContent.getId(), request.bytes());
    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  public BinaryContentDto findBinaryContentById(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new FileNotFoundException(Map.of("binaryContentId", binaryContentId)));
    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllById(ids).stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Override
  public ResponseEntity<?> downloadBinaryContent(UUID binaryContentId) {
    log.info("파일 다운로드 시도");
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> {
          log.error("파일 다운로드 단계에서 파일을 찾지 못함: binaryContentId={}", binaryContentId);
          return new FileNotFoundException(Map.of("binaryContentId", binaryContentId));
        });
    ResponseEntity<?> downloadFile = binaryContentStorage.download(
        binaryContentMapper.toDto(binaryContent));
    log.info("파일 다운로드 시도 성공");
    return downloadFile;
  }

  @Transactional
  @Override
  public void deleteBinaryContentById(UUID binaryContentId) {
    binaryContentRepository.deleteById(binaryContentId);
  }
}
