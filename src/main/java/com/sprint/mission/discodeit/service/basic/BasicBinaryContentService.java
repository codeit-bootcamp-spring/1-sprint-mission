package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

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
      throw new IllegalArgumentException("파일 정보가 없습니다.");
    }
    if (dto.getBytes() == null) {
      throw new IllegalArgumentException("파일 데이터가 올바르지 않습니다. (파일이 비어있음)");
    }

    Optional<BinaryContent> existing = binaryContentRepository.findById(dto.getId());
    if (existing.isPresent()) {
      return existing.get();
    }

    BinaryContent binaryContent = new BinaryContent(
        dto.getFileName(),
        dto.getBytes().length,
        dto.getContentType()
    );
    BinaryContent savedContent = binaryContentRepository.save(binaryContent);
    String extension = getFileExtension(binaryContent.getFileName());

    System.out.println("🔹 파일 저장 시작: " + dto.getFileName());
    System.out.println("put호출전" + savedContent.getId());

    Path filePath = binaryContentStorage.put(savedContent.getId(), dto.getBytes(), extension);

    System.out.println("put 호출 완료: " + filePath);
    savedContent.setFilePath(filePath.toString());

    System.out.println("✅ 파일 저장 완료: " + savedContent.getId());
    return savedContent;
  }


  @Override
  public InputStream getBinaryContent(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(NoSuchElementException::new);
    String extension = getFileExtension(binaryContent.getFileName());
    return binaryContentStorage.get(id, extension);
  }


  @Override
  public ResponseEntity<?> downloadBinaryContent(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(
            () -> new NoSuchElementException("BinaryContent with id " + id + " not found"));
    String extension = getFileExtension(binaryContent.getFileName());
    if (!binaryContentStorage.exists(id, extension)) {
      return ResponseEntity.notFound().build();
    }

    return binaryContentStorage.download(binaryContentMapper.toDto(binaryContent), extension);
  }

  @Override
  public BinaryContent find(UUID id) {
    return binaryContentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("BinaryContent id not found"));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllByIdIn(ids);
  }

  @Override
  public void delete(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(NoSuchElementException::new);
    String extension = getFileExtension(binaryContent.getFileName());
    binaryContentStorage.delete(id, extension);
    binaryContentRepository.deleteById(id);
  }

  private String getFileExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf(".");
    return (dotIndex > 0) ? fileName.substring(dotIndex) : "";
  }
}
