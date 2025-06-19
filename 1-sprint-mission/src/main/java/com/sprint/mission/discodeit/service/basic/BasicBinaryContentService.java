package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.configure.UploadStatus;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public BinaryContentDto create(BinaryContentCreateRequest request) {
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

    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  public BinaryContentDto find(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .map(binaryContentMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException(
            "BinaryContent with id " + binaryContentId + " not found"));
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
    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
    }
    binaryContentRepository.deleteById(binaryContentId);
  }

  private final AsyncTaskFailureRepository failureRepository;

  @Async
  @Retryable(
      value = {IOException.class, RuntimeException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000, multiplier = 2)
  )
  public void uploadFileAsync(BinaryContent content, byte[] data) {
    MDC.put("requestId", getRequestId());

    try {
      simulateFileUpload(content.getId(), data); // 예외 발생 가능 메소드
      content.setUploadStatus(UploadStatus.SUCCESS);
      binaryContentRepository.save(content);
    } catch (Exception e) {
      throw e; // 재시도를 유도
    } finally {
      MDC.clear();
    }
  }

  @Recover
  public void uploadFileRecover(Exception e, BinaryContent content, byte[] data) {
    content.setUploadStatus(UploadStatus.FAILED);
    binaryContentRepository.save(content);

    AsyncTaskFailure failure = new AsyncTaskFailure();
    failure.setContentId(content.getId());
    failure.setReason(e.getMessage());
    failure.setRequestId(MDC.get("requestId"));
    failure.setOccurredAt(LocalDateTime.now());
    failureRepository.save(failure);
  }

  private void simulateFileUpload(UUID id, byte[] data) {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    String storagePath = "./uploaded-files";
    try {
      Path dir = Paths.get(storagePath);
      if (!Files.exists(dir)) {
        Files.createDirectories(dir);
      }

      Path filePath = dir.resolve("content-" + id + ".bin");
      Files.write(filePath, data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  private String getRequestId() {
    return MDC.get("requestId") != null ? MDC.get("requestId") : UUID.randomUUID().toString();
  }

  public byte[] loadFile(Long contentId) throws IOException {
    String storagePath = "./uploaded-files";
    Path filePath = Paths.get(storagePath, "content-" + contentId + ".bin");
    if (!Files.exists(filePath)) {
      throw new FileNotFoundException("파일이 존재하지 않습니다.");
    }
    return Files.readAllBytes(filePath);
  }
}
