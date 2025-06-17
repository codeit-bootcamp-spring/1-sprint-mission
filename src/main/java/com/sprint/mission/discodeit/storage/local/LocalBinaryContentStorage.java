package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.AsyncTaskFailure;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.event.AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentUploadException;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.micrometer.core.annotation.Timed;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path rootPath;
  private final ApplicationEventPublisher eventPublisher;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath,
      ApplicationEventPublisher eventPublisher) {
    this.rootPath = Paths.get(rootPath);
    this.eventPublisher = eventPublisher;
    init();
  }

  @PostConstruct // 빈 생성시 자동으로 호출되어 저장소 디렉터리 구조를 준비
  private void init() {
    log.info("Initializing storage directory {}", rootPath);
    try {
      if (!Files.exists(this.rootPath)) {
        Files.createDirectories(this.rootPath);
      }
      log.info("Initialized storage directory {}", this.rootPath);
    } catch (IOException e) {
      throw new RuntimeException("Failed to initialized storage directory", e);
    }
  }

  //파일의 실제 저장 위치에 대한 규칙을 정의하고 일관된 파일 경로 규칙을 유지하기 위한 메소드
  private Path resolvePath(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("Content id cannot be null");
    }
    return rootPath.resolve(id.toString());
  }

  // SecurityContext에서 사용자 정보 가져오기
  private UserDto getCurrentUser() {
    return ((DiscodeitUserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal()).getUserDto();
  }

  @Override
  public UUID put(UUID id, byte[] content) {

    log.info("파일 저장: id = {}", id);
    try {
      Path filePath = resolvePath(id);
      Files.write(filePath, content);
      log.info("파일 저장 완료");
      return id;
    } catch (IOException e) {
      log.error("파일 쓰기 오류: {}", e.getMessage());
      throw new RuntimeException("Failed to store file", e);
    }
  }


  @Async("binaryContentTaskExecutor")
  //일반 타입으로 반환하면 @Async가 무시되고 동기 실행된다!
  // CompletableFuture<T> - 비동기 + 결과 추적 (권장: 결과가 필요한 경우)
  @Override
  @Retryable(
      retryFor = {
          IOException.class,
          FileSystemException.class,
          BinaryContentUploadException.class
      },
      maxAttempts = 3,
      backoff = @Backoff(
          delay = 500,      // 0.5초 시작
          multiplier = 2.0, // 매번 2배씩 증가
          maxDelay = 10000   // 최대 10초
      )
  )
  @Timed(value = "file.upload.async", description = "비동기 파일 업로드")
  public CompletableFuture<UUID> asyncPut(UUID id, byte[] file) {
    return CompletableFuture.completedFuture(put(id, file));
  }

  // 재시도 실패 시 처리 - 무조건 맨 첫번째 매개변수는 예외여야함
  @Recover
  public CompletableFuture<UUID> recoverAsyncPut(BinaryContentUploadException ex, UUID id,
      byte[] content) {

    AsyncTaskFailure asyncTaskFailure = new AsyncTaskFailure("UPLOAD_FILE",
        MDC.get("requestId"),
        ex.getMessage());

    log.error("파일 저장 최종 실패: AsyncTaskFailure = {}", asyncTaskFailure.toString());

    UUID userId = getCurrentUser().id();
    AsyncFailedNotificationEvent event = new AsyncFailedNotificationEvent(userId, ex.getMessage());
    eventPublisher.publishEvent(event);

    CompletableFuture<UUID> future = new CompletableFuture<>();
    future.completeExceptionally(ex);
    return future;
  }

  @Override
  @Transactional(readOnly = true)
  public InputStream get(UUID id) {
    try {
      Path filePath = resolvePath(id);
      return new FileInputStream(filePath.toFile());
    } catch (IOException e) {
      log.error("파일 읽기 오류: {}", e.getMessage());
      throw new RuntimeException("Failed to read file", e);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    log.info("파일 다운로드: binaryContentId = {}", binaryContentDto.id());
    try {
      InputStream fileStream = get(binaryContentDto.id());
      InputStreamResource resource = new InputStreamResource(fileStream);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.parseMediaType(binaryContentDto.contentType()));

      String filename = binaryContentDto.fileName() + "." + binaryContentDto.contentType();

      headers.setContentDisposition(
          ContentDisposition.builder("attachment")
              .filename(filename, StandardCharsets.UTF_8)
              .build());
      if (binaryContentDto.size() != null) {
        headers.setContentLength(binaryContentDto.size());
      }
      return ResponseEntity.ok().headers(headers).body(resource);
    } catch (Exception e) {

      // 오류 메시지를 StringResource로 변환하여 Resource 타입으로 반환
      log.error("파일 다운로드 실패: {}", e.getMessage());
      ByteArrayResource errorResource = new ByteArrayResource(
          ("Failed to download file: " + e.getMessage()).getBytes());
      throw new RuntimeException("Failed to download file", e);
    }
  }
}
