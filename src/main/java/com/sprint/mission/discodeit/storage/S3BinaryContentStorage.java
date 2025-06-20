package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.config.MDCLoggingInterceptor;
import com.sprint.mission.discodeit.config.S3StorageProperties;
import com.sprint.mission.discodeit.dto.AsyncTaskFailure;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.event.AsyncTaskFailedEvent;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final S3StorageProperties properties;
  private final ApplicationEventPublisher eventPublisher;

  @Async("binaryContentExecutor")
  @Override
  @Retryable(
      retryFor = SdkException.class,
      backoff = @Backoff(delay = 2000)
  )
  public CompletableFuture<UUID> put(UUID id, byte[] data) {
    log.debug("S3BinaryContentStorage.put() 호출");

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(properties.getBucket())
        .key(id.toString())
        .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(data));

    log.debug("S3BinaryContentStorage.put() 정상 처리");
    return CompletableFuture.completedFuture(id);
  }

  @Recover
  @Transactional
  public CompletableFuture<UUID> recover(SdkException cause, UUID id, byte[] data) {
    String requestId = MDC.get(MDCLoggingInterceptor.REQUEST_ID);

    AsyncTaskFailure failureDetails = new AsyncTaskFailure(
        "S3BinaryContentStorage.put",
        requestId,
        cause.getMessage()
    );

    eventPublisher.publishEvent(AsyncTaskFailedEvent.of(failureDetails));

    log.error("비동기 파일 업로드 실패 : {}", failureDetails, cause);
    return CompletableFuture.failedFuture(cause);
  }

  @Override
  public InputStream get(UUID id) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(properties.getBucket())
        .key(id.toString())
        .build();

    return s3Client.getObject(getObjectRequest);
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto response) {
    String presignedUrl = generatePresignedUrl(response.id().toString(), response.contentType());
    return ResponseEntity
        .status(HttpStatus.FOUND)
        .location(URI.create(presignedUrl))
        .build();
  }

  private String generatePresignedUrl(String key, String contentType) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(properties.getBucket())
        .key(key)
        .responseContentDisposition("attachment; filename=\"" + key + "." + contentType + "\"")
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofSeconds(properties.getPresignedUrlExpiration()))
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);

    return presignedGetObjectRequest.url().toString();
  }

  @Override
  public void delete(UUID id) {
    throw new UnsupportedOperationException("s3 파일 삭제는 지원하지 않음");
  }
}
