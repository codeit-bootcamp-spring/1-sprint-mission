package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.binarycontent.NullFileInfoExeption;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public BinaryContentDto create(BinaryContentCreateRequest binaryContentCreateRequest) {
    log.info("BinaryContent 생성 시작: {}", binaryContentCreateRequest);
    if (binaryContentCreateRequest.fileName() == null
        || binaryContentCreateRequest.contentType() == null) {
      log.error("파일 이름, 내용 = null");
      throw new NullFileInfoExeption(ErrorCode.NULL_FILE_INFO,
          Map.of("binaryContentCreateRequest", binaryContentCreateRequest));
    }

    BinaryContent binaryContent = new BinaryContent(
        binaryContentCreateRequest.fileName(),
        (long) binaryContentCreateRequest.bytes().length,
        binaryContentCreateRequest.contentType()
    );
    binaryContentRepository.save(binaryContent);
    binaryContentStorage.put(binaryContent.getId(), binaryContentCreateRequest.bytes());
    log.info("BinaryContent 생성 성공: {}", binaryContent);
    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  public BinaryContentDto findById(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .map(binaryContentMapper::toDto)
        .orElseThrow(() -> new BinaryContentNotFoundException(ErrorCode.BINARYCONENT_NOT_FOUND,
            Map.of("binaryContentId", binaryContentId)));
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllById(binaryContentIds)
        .stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public void deleteById(UUID binaryContentId) {
    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new BinaryContentNotFoundException(ErrorCode.BINARYCONENT_NOT_FOUND,
          Map.of("binaryContentId", binaryContentId));
    }
    binaryContentRepository.deleteById(binaryContentId);
  }
}
