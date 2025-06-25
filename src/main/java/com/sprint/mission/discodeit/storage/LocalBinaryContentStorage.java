package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.events.AsyncFailedEvent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.async.AsyncTaskFailure;
import com.sprint.mission.discodeit.exception.binary.FailReadBinaryContent;
import com.sprint.mission.discodeit.exception.binary.NotSavedBinaryContentException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.notification.NotificationService;
import io.micrometer.core.annotation.Timed;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final NotificationService notificationService;
  private final ApplicationEventPublisher eventPublisher;
  private final BinaryContentRepository binaryContentRepository;
  private final Path path;

  public LocalBinaryContentStorage(NotificationService notificationService,
      ApplicationEventPublisher eventPublisher,
      BinaryContentRepository binaryContentRepository,
      @Value("${discodeit.storage.local.root-path}") String path) {
    this.notificationService = notificationService;
    this.eventPublisher = eventPublisher;
    this.binaryContentRepository = binaryContentRepository;
    this.path = Paths.get(path);
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(path);
    } catch (IOException e) {
      throw new RuntimeException("local storage init fail: ", e);
    }
  }

  @Override
  public Path resolvePath(UUID id) {
    return path.resolve(id.toString());
  }

  /**
   * @methodName : put
   * @date : 2025-06-03 오후 3:33
   * @author : wongil
   * @Description: 비동기 파일 업로드
   **/
  @Timed(value = "upload.async", description = "비동기 업로드 소요 시간")
  @Transactional
  @Retryable(retryFor = NotSavedBinaryContentException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
  @Async("fileUploadExecutor")
  @Override
  public CompletableFuture<UUID> put(UUID fileId, byte[] bytes) {
    Path filePath = resolvePath(fileId);

    return nonBlockingUpload(fileId, bytes, filePath);
  }

  @Timed(value = "upload.sync", description = "동기 업로드 소요 시간")
  @Transactional
//  @Async("fileUploadExecutor")
  public CompletableFuture<UUID> syncPut(UUID fileId, byte[] bytes) {
    Path filePath = resolvePath(fileId);

    return blockingUpload(fileId, bytes, filePath);
  }

  /**
   * @methodName : blockingUpload
   * @date : 2025-06-03 오후 6:42
   * @author : wongil
   * @Description: 기본 IO 방식 업로드
   **/
  private CompletableFuture<UUID> blockingUpload(UUID fileId, byte[] bytes,
      Path filePath) {
    try {
      Files.write(filePath, bytes);
      log.info("파일 저장 완료: {}", fileId);
      return CompletableFuture.completedFuture(fileId);
    } catch (IOException e) {
      log.error("파일 저장 실패: {}", fileId, e);
      throw new NotSavedBinaryContentException(Instant.now(), ErrorCode.NOT_SAVED_FILE,
          Map.of("fileId", ErrorCode.NOT_SAVED_FILE.getMessage()));
    }
  }

  /**
   * @methodName : nonBlockingUpload
   * @date : 2025-06-03 오후 6:42
   * @author : wongil
   * @Description: 논블로킹 파일 저장
   **/
  private CompletableFuture<UUID> nonBlockingUpload(UUID fileId, byte[] bytes,
      Path filePath) {

    try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(filePath,
        StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

      ByteBuffer buffer = ByteBuffer.wrap(bytes);
      Future<Integer> write = fileChannel.write(buffer, 0);

      write.get();

      log.info("비동기 파일 저장 완료: {}", fileId);
      return CompletableFuture.completedFuture(fileId);
    } catch (Exception e) {
      log.error("파일 저장 실패: {}", fileId, e);
      throw new NotSavedBinaryContentException(Instant.now(), ErrorCode.NOT_SAVED_FILE,
          Map.of("fileId", ErrorCode.NOT_SAVED_FILE.getMessage()));
    }
  }

  @Override
  public InputStream get(UUID fileId) {
    Path filePath = resolvePath(fileId);

    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      log.error("파일 읽기 실패: {}", fileId);
      throw new FailReadBinaryContent(Instant.now(), ErrorCode.FAIL_READ_FILE,
          Map.of("fileId", ErrorCode.FAIL_READ_FILE.getMessage()));
    }

  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto file) {
    InputStream findFileStream = get(file.id());

    InputStreamResource fileResource = new InputStreamResource(findFileStream);
    log.info("파일 다운로드: {}", file.fileName());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.fileName() + "\"")
        .contentLength(file.size()).contentType(MediaType.parseMediaType(file.contentType()))
        .body(fileResource);
  }

  @Recover
  public CompletableFuture<UUID> recover(NotSavedBinaryContentException e, UUID fileId,
      byte[] bytes) {

    String requestId = MDC.get("requestId");
    AsyncTaskFailure failure = AsyncTaskFailure.builder()
        .taskName("File Upload")
        .requestId(requestId)
        .failureReason(e.getClass().getName())
        .build();
    log.error("비동기 업로드 재시도 실패: {}", failure);
    eventPublisher.publishEvent(
        new AsyncFailedEvent(fileId, UUID.fromString(requestId), e.toString()));

    return CompletableFuture.completedFuture(fileId);
  }
}
