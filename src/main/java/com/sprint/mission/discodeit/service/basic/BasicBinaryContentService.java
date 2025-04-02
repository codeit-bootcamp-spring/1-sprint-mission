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
  private final BinaryContentStorage binaryContentStorage;
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
    String extension = getFileExtension(binaryContent.getFileName());
    Path filePath = binaryContentStorage.put(savedContent.getId(), dto.getBytes(), extension);

    savedContent.setFilePath(filePath.toString());

    log.info("파일 저장 완료: id={}, fileName={}, size={}bytes", savedContent.getId(),
        savedContent.getFileName(), savedContent.getSize());
    return savedContent;
  }


  @Override
  public InputStream getBinaryContent(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(FileNotFoundException::new);
    String extension = getFileExtension(binaryContent.getFileName());
    return binaryContentStorage.get(id, extension);
  }


  @Override
  public ResponseEntity<?> downloadBinaryContent(UUID id) {
    log.info("파일 다운로드 요청: id={}", id);

    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("파일 다운로드 실패 - 존재하지 않음: id={}", id);
          return new FileNotFoundException();
        });

    String extension = getFileExtension(binaryContent.getFileName());

    if (!binaryContentStorage.exists(id, extension)) {
      log.warn("파일 존재하지 않음 (Storage): id={}", id);
      throw new FileNotFoundException();
    }

    log.debug("파일 다운로드 응답 준비 완료: id={}, fileName={}", id, binaryContent.getFileName());
    return binaryContentStorage.download(binaryContentMapper.toDto(binaryContent), extension);
  }


  @Override
  public BinaryContent find(UUID id) {
    return binaryContentRepository.findById(id)
        .orElseThrow(FileNotFoundException::new);
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllByIdIn(ids);
  }

  @Override
  public void delete(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(FileNotFoundException::new);
    String extension = getFileExtension(binaryContent.getFileName());
    binaryContentStorage.delete(id, extension);
    binaryContentRepository.deleteById(id);
  }

  private String getFileExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf(".");
    return (dotIndex > 0) ? fileName.substring(dotIndex) : "";
  }
}
