package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public BinaryContentDto create(CreateBinaryContentRequest request) {

    log.debug("BinaryContent 생성 시작: fileName={}, size={}, contentType={}",
        request.fileName(), request.bytes().length, request.contentType());

    String fileName = request.fileName();
    byte[] bytes = request.bytes();
    String contentType = request.contentType();

    BinaryContent binaryContent = new BinaryContent(
        fileName,
        (long) bytes.length,
        contentType
    );

    binaryContentRepository.save(binaryContent);
    binaryContentStorage.put(binaryContent.getId(), bytes);

    BinaryContentDto binaryContentDto = binaryContentMapper.toDto(binaryContent);

    log.info("BinaryContent 생성 완료: id={}, fileName={}, size={}",
        binaryContent.getId(), fileName, bytes.length);

    return binaryContentDto;
  }

  @Override
  public BinaryContentDto find(UUID binaryContentId) {
    log.debug("BinaryContent 조회 시작: id={}", binaryContentId);

    BinaryContentDto binaryContentDto = binaryContentRepository.findById(binaryContentId)
        .map(binaryContentMapper::toDto)
        .orElseThrow(() -> new BinaryContentNotFoundException(binaryContentId));

    log.info("BinaryContent 조회 완료: id={}, fileName={}",
        binaryContentDto.id(), binaryContentDto.fileName());

    return binaryContentDto;
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {

    log.debug("BinaryContent 목록 조회 시작: ids={}", binaryContentIds);

    List<BinaryContentDto> binaryContentDtos = binaryContentRepository.findAllById(binaryContentIds).stream()
        .map(binaryContentMapper::toDto)
        .toList();

    log.info("BinaryContent 목록 조회 완료: 조회된 항목 수={}", binaryContentDtos.size());

    return binaryContentDtos;
  }

  @Override
  @Transactional
  public void delete(UUID binaryContentId) {

    log.debug("BinaryContent 삭제 시작: id={}", binaryContentId);

    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new BinaryContentNotFoundException(binaryContentId);
    }
    binaryContentRepository.deleteById(binaryContentId);

    log.info("BinaryContent 삭제 완료: id={}", binaryContentId);
  }
}
