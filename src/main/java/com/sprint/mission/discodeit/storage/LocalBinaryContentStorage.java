package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.config.MDCLoggingInterceptor;
import com.sprint.mission.discodeit.dto.AsyncTaskFailure;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.event.AsyncTaskFailedEvent;
import com.sprint.mission.discodeit.exception.binarycontent.file.DirectoryCreateException;
import com.sprint.mission.discodeit.exception.binarycontent.file.FileCreateException;
import com.sprint.mission.discodeit.exception.binarycontent.file.FileDeleteException;
import com.sprint.mission.discodeit.exception.binarycontent.file.FileReadException;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final ApplicationEventPublisher eventPublisher;

  @Value("${discodeit.storage.local.root-path}")
  private Path root;

  @PostConstruct
  public void init() {
    if (!Files.exists(root)) {
      try {
        Files.createDirectories(root);
      } catch (IOException e) {
        throw new DirectoryCreateException(Map.of());
      }
    }
  }

  @Async("binaryContentExecutor")
  @Override
  @Retryable(
      retryFor = FileCreateException.class,
      backoff = @Backoff(delay = 2000)
  )
  public CompletableFuture<UUID> put(UUID id, byte[] data) {
    log.debug("LocalBinaryContentStorage.put() 호출");
    Path path = resolvePath(id);

    try {
      Files.write(path, data);
    } catch (IOException e) {
      log.error("error: ", e);
      throw new FileCreateException(Map.of("path", path));
    }

    log.info("LocalBinaryContentStorage.put() 정상 처리");
    return CompletableFuture.completedFuture(id);
  }

  @Recover
  public CompletableFuture<UUID> recover(FileCreateException cause, UUID id, byte[] data) {
    String requestId = MDC.get(MDCLoggingInterceptor.REQUEST_ID);

    AsyncTaskFailure failureDetails = new AsyncTaskFailure(
        "LocalBinaryContentStorage.put",
        requestId,
        cause.getMessage()
    );

    eventPublisher.publishEvent(AsyncTaskFailedEvent.of(failureDetails));

    log.error("비동기 파일 업로드 실패 : {}", failureDetails, cause);
    throw new FileCreateException(Map.of("failureDetails", failureDetails));
  }

  @Override
  public InputStream get(UUID id) {
    Path path = resolvePath(id);
    try {
      return new ByteArrayInputStream(Files.readAllBytes(path));
    } catch (IOException e) {
      throw new FileReadException(Map.of("path", path));
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto response) {
    Path path = resolvePath(response.id());
    try {
      UrlResource resource = new UrlResource("file:" + path);
      String contentDisposition = "attachment; filename=\"" + resource.getFilename() + "\"";
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
          .body(resource);
    } catch (MalformedURLException e) {
      throw new FileReadException(Map.of("path", path));
    }
  }

  @Override
  public void delete(UUID id) {
    Path path = resolvePath(id);
    if (Files.exists(path)) {
      try {
        Files.delete(path);
      } catch (IOException e) {
        throw new FileDeleteException(Map.of("path", path));
      }
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}