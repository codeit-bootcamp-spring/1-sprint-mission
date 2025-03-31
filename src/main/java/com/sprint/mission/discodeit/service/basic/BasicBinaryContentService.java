package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentException;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @Transactional
  public BinaryContentDto create(MultipartFile file) {
    log.info("파일 정보 생성 시작 : {}", file);
    try {
      BinaryContent binaryContent = new BinaryContent(
          file.getName(),
          file.getContentType(),
          file.getSize()
      );
      BinaryContent savedContent = binaryContentRepository.save(binaryContent);
      UUID contentId = binaryContentStorage.put(binaryContent.getId(), file.getBytes());
      log.info("파일 정보 생성 완료: binaryContentId = {}", contentId);
      return binaryContentMapper.toDto(savedContent);
    } catch (IOException e) {
      log.error("파일 정보 생성 중 오류 발생");
      throw new BinaryContentException(ErrorCode.FILE_NOT_CREATED);
    }
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
