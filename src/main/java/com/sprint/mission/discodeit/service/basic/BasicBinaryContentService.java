package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.status.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentUploadException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public BinaryContentDto create(MultipartFile file) {
    log.info("파일 정보 생성 시작 : {}", file);
    BinaryContent binaryContent = new BinaryContent(
        file.getName(),
        file.getContentType(),
        file.getSize(),
        BinaryContentUploadStatus.WAITING
    );

    BinaryContent savedContent = binaryContentRepository.save(binaryContent);

//    // 동기 처리로 임시 변경 (성능 비교용)
//    UUID contentId = binaryContent.getId();
//    try {
//      UUID result = binaryContentStorage.put(contentId, file.getBytes());
//      binaryContentStatusService.updateStatus(contentId, BinaryContentUploadStatus.SUCCESS);
//      log.info("동기 파일 업로드 성공: id = {}", result);
//    } catch (Exception e) {
//      binaryContentStatusService.updateStatus(contentId, BinaryContentUploadStatus.FAILED);
//      log.error("동기 파일 업로드 실패: {}", e.getMessage());
//    }

    log.info("비동기 파일 업로드");

    try {
      eventPublisher.publishEvent(new BinaryContentCreatedEvent(
          savedContent.getId(),
          file.getBytes()
      ));
    } catch (IOException e) {
      throw new BinaryContentUploadException(ErrorCode.FILE_NOT_SAVED);
    }

    return binaryContentMapper.toDto(savedContent);
  }

  @Override
  @Transactional(readOnly = true)
  public BinaryContentDto findById(String contentId) throws BinaryContentNotFoundException {
    BinaryContent binaryContent = binaryContentRepository.findById(UUID.fromString(contentId))
        .orElseThrow(() -> new BinaryContentNotFoundException(ErrorCode.CONTENT_NOT_FOUND));

    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BinaryContentDto> findAllByIdIn(List<String> contentIds) {

    List<BinaryContent> list = binaryContentRepository.findAll().stream()
        .filter(b -> contentIds.contains(b.getId())).toList();
    return list.stream().map(binaryContentMapper::toDto).toList();
  }

  @Override
  @Transactional
  public boolean deleteById(String contentId) throws BinaryContentNotFoundException {
    log.info("파일 삭제 시작: contentId = {}", contentId);
    BinaryContent binaryContent = binaryContentRepository.findById(UUID.fromString(contentId))
        .orElseThrow(() -> new BinaryContentNotFoundException(ErrorCode.CONTENT_NOT_FOUND));

    binaryContentRepository.deleteById(binaryContent.getId());
    log.info("파일 삭제 완료: contentId = {}", contentId);
    return true;
  }
}
