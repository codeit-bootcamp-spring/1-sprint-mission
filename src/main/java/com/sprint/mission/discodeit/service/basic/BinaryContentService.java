package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  public BinaryContentDto create(BinaryContentCreateRequest request) {
    String fileName = request.fileName();
    byte[] bytes = request.bytes();
    String contentType = request.contentType();
    log.debug("파일 생성 서비스 진입 - fileName: {}, contentType: {}", fileName, contentType);
    try {
      BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
      binaryContentRepository.save(binaryContent);
      binaryContentStorage.put(binaryContent.getId(), bytes);

      log.info("파일 저장 성공 - binaryContentId: {}, fileName: {}, size: {} bytes",
          binaryContent.getId(), fileName, bytes.length);

      return binaryContentMapper.toDto(binaryContent);

    } catch (Exception e) {
      log.error("파일 저장 중 예외 발생 - fileName: {}, 원인: {}",
          fileName, e.getMessage(), e);
      throw e;
    }
  }

  public BinaryContentDto find(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .map(binaryContentMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException(
            "BinaryContent with id " + binaryContentId + " not found"));
  }

  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllById(binaryContentIds).stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Transactional
  public void delete(UUID binaryContentId) {
    if (!binaryContentRepository.existsById(binaryContentId)) {
      log.warn("파일 삭제 실패 - 존재하지 않는 파일 - binaryContentId: {}", binaryContentId);
      throw new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
    }
    binaryContentRepository.deleteById(binaryContentId);
    log.info("파일 삭제 완료 - binaryContentId: {}", binaryContentId);
  }
}
