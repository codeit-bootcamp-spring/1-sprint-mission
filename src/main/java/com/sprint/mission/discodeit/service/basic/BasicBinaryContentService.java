package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.file.FileNotFoundException;
import com.sprint.mission.discodeit.exception.file.InvalidFileDataException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
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

  @Override
  @Transactional
  public BinaryContent saveBinaryContent(BinaryContentDto dto) {
    if (dto == null) {
      log.warn("파일 저장 실패: DTO가 null입니다.");
      throw new InvalidFileDataException();
    }

    if (dto.getBytes() == null) {
      log.warn("파일 저장 실패: 파일 바이트가 null입니다. fileName={}", dto.getFileName());
      throw new InvalidFileDataException();
    }

    Optional<BinaryContent> existing = binaryContentRepository.findById(dto.getId());
    if (existing.isPresent()) {
      log.info("이미 존재하는 파일 요청: id={}, fileName={}", dto.getId(), dto.getFileName());
      return existing.get();
    }

    BinaryContent binaryContent = new BinaryContent(
        dto.getFileName(),
        dto.getBytes().length,
        dto.getContentType()
    );

    BinaryContent savedContent = binaryContentRepository.save(binaryContent);
    log.info("파일 저장 완료: id={}, fileName={}, size={}bytes", savedContent.getId(),
        savedContent.getFileName(), savedContent.getSize());
    return savedContent;
  }


  @Override
  public BinaryContentDto find(UUID id) {
    BinaryContentDto dto = binaryContentRepository.findById(id)
        .map(binaryContentMapper::toDto)
        .orElseThrow(FileNotFoundException::new);
    return dto;
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllByIdIn(ids).stream().map(binaryContentMapper::toDto)
        .toList();
  }

  @Override
  public void delete(UUID id) {
    binaryContentRepository.findById(id).orElseThrow(FileNotFoundException::new);
    binaryContentRepository.deleteById(id);
  }
}
