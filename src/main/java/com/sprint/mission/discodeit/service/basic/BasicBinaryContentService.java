package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.BinaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public BinaryContentDto create(BinaryContentCreateRequest request) {
    log.debug("create 받는값: fileName={}, contentType={}, 크기={}바이트", request.fileName(), request.contentType(), request.bytes().length);


    String fileName = request.fileName();
    byte[] bytes = request.bytes();
    String contentType = request.contentType();
    BinaryContent binaryContent = new BinaryContent(
        fileName,
        (long) bytes.length,
        contentType
    );
    binaryContentRepository.save(binaryContent);
    log.debug("바이너리 콘텐츠 메타데이터 DB 저장 완료: ID={}", binaryContent.getId());
    binaryContentStorage.put(binaryContent.getId(), bytes);
    log.debug("바이너리 콘텐츠 실제 데이터 저장 완료: ID={}, 크기={}바이트", binaryContent.getId(), bytes.length);

    log.info("create 성공: ID={}, 파일명={}, 크기={}바이트", binaryContent.getId(), fileName, bytes.length);
    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  public BinaryContentDto find(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .map(binaryContentMapper::toDto)
        .orElseThrow(() -> {
          log.error("find 실패 컨텐츠를 찾을 수 없음: ID={}", binaryContentId);
          return new BinaryContentNotFoundException(Map.of("contentId", binaryContentId));
        });
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllById(binaryContentIds).stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public void delete(UUID binaryContentId) {
    log.debug("delete 받는값: ID={}", binaryContentId);

    if (!binaryContentRepository.existsById(binaryContentId)) {
      log.error("delete 실패: ID={}인 콘텐츠를 찾을 수 없음", binaryContentId);
      throw new BinaryContentNotFoundException(Map.of("contentId", binaryContentId));
    }

    log.info("delete 성공: ID={}", binaryContentId);
    binaryContentRepository.deleteById(binaryContentId);
  }
}
