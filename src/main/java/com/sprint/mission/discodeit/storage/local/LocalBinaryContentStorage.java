package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.UploadStatus;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Component
@Slf4j
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;
  private final BinaryContentRepository binaryContentRepository;
  private final AsyncTaskFailureRepository asyncTaskFailureRepository;

  public LocalBinaryContentStorage(
      @Value("${discodeit.storage.local.root-path}") String rootPath,
      BinaryContentRepository binaryContentRepository,
      AsyncTaskFailureRepository asyncTaskFailureRepository
  ) {
    this.root = Paths.get(rootPath); // String → Path 변환 수동
    this.binaryContentRepository = binaryContentRepository;
    this.asyncTaskFailureRepository = asyncTaskFailureRepository;
  }

  @PostConstruct
  public void init() {
    if (!Files.exists(root)) {
      try {
        Files.createDirectories(root);
      } catch (IOException e) {
        log.error("Failed to create root directory", e);
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    Path filePath = resolvePath(binaryContentId);
    if (Files.exists(filePath)) {
      throw new IllegalArgumentException("File with key " + binaryContentId + " already exists");
    }
    try (OutputStream outputStream = Files.newOutputStream(filePath)) {
      outputStream.write(bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    Path filePath = resolvePath(binaryContentId);
    if (Files.notExists(filePath)) {
      throw new NoSuchElementException("File with key " + binaryContentId + " does not exist");
    }
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Path resolvePath(UUID key) {
    return root.resolve(key.toString());
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto metaData) {
    InputStream inputStream = get(metaData.id());
    Resource resource = new InputStreamResource(inputStream);

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + metaData.fileName() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, metaData.contentType())
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metaData.size()))
        .body(resource);
  }

  /**
   * 트랜잭션 커밋 후 비동기 업로드 실행용
   */
  public void putAfterTransaction(UUID binaryContentId, byte[] bytes) {
    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
      @Override
      public void afterCommit() {
        putAsync(binaryContentId, bytes);
      }
    });
  }

  /**
   * 비동기 파일 업로드
   */
  @Async
  @Retryable(
      value = { IOException.class },
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000)
  )
  public CompletableFuture<UUID> putAsync(UUID binaryContentId, byte[] bytes) {
    try (MDC.MDCCloseable ignored = MDC.putCloseable("requestId", MDC.get("requestId"))) {
      SecurityContext context = SecurityContextHolder.getContext();

      return CompletableFuture.supplyAsync(() -> {
        SecurityContextHolder.setContext(context);
        try {
          UUID id = put(binaryContentId, bytes);
          binaryContentRepository.updateStatus(binaryContentId, UploadStatus.SUCCESS);
          return id;
        } catch (Exception e) {
          binaryContentRepository.updateStatus(binaryContentId, UploadStatus.FAILED);
          throw new RuntimeException("Async file upload failed", e);
        }
      });
    }
  }

  /**
   * 모든 재시도 실패 시 호출되는 복구 로직
   */
  @Recover
  public CompletableFuture<UUID> recover(IOException e, UUID binaryContentId, byte[] bytes) {
    String requestId = MDC.get("requestId");
    log.error("Upload failed after retries. ID: {}, RequestID: {}", binaryContentId, requestId, e);

    AsyncTaskFailure failure = new AsyncTaskFailure(binaryContentId, requestId, e.getMessage());
    asyncTaskFailureRepository.save(failure);

    binaryContentRepository.updateStatus(binaryContentId, UploadStatus.FAILED);

    return CompletableFuture.completedFuture(null);
  }
}

