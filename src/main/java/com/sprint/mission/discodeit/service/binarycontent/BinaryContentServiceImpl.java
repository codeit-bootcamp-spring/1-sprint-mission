package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.binarycontent.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binary.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.entitymapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service("binaryContentService")
public class BinaryContentServiceImpl implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public BinaryContentResponse find(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new BinaryContentNotFoundException(Instant.now(),
            ErrorCode.BINARY_CONTENT_NOT_FOUND,
            Map.of(binaryContentId.toString(), ErrorCode.BINARY_CONTENT_NOT_FOUND.getMessage())
        ));

    if (binaryContent == null) {
      log.warn("존재하지 않는 파일: {}", binaryContentId);
      throw new BinaryContentNotFoundException(Instant.now(), ErrorCode.BINARY_CONTENT_NOT_FOUND,
          Map.of(binaryContentId.toString(), ErrorCode.BINARY_CONTENT_NOT_FOUND.getMessage())
      );
    }

    log.info("파일 단건 조회: {}", binaryContentId);
    
    return new BinaryContentResponse(binaryContent.getId(), binaryContent.getFileName(),
        binaryContent.getSize(), binaryContent.getContentType());
  }

  @Override
  public List<BinaryContentResponse> findAll(List<String> binaryContentIds) {
    List<UUID> binaryContentsUUIDs = binaryContentIds.stream()
        .map(UUID::fromString)
        .toList();

    return binaryContentRepository.findAllByIdIn(binaryContentsUUIDs).stream()
        .map(bin -> new BinaryContentResponse(bin.id(), bin.fileName(), bin.size(),
            bin.contentType()))
        .toList();
  }

  @Override
  public ResponseEntity<?> downloadBinaryContent(UUID binaryContentId) {
    BinaryContent file = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> {
          log.error("존재하지 않은 파일 다운로드: {}", binaryContentId);
          return new BinaryContentNotFoundException(Instant.now(),
              ErrorCode.BINARY_CONTENT_NOT_FOUND,
              Map.of(binaryContentId.toString(), ErrorCode.BINARY_CONTENT_NOT_FOUND.getMessage())
          );
        });

    log.info("파일 다운로드: {}", file.getFileName());
    return binaryContentStorage.download(BinaryContentMapper.toDto(file));
  }
}
