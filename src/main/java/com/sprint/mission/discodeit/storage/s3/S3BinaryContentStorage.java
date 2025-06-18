package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.UploadStatus;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Component
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final BinaryContentRepository binaryContentRepository;
  private final AsyncTaskFailureRepository asyncTaskFailureRepository;
  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucket;
  private final NotificationService notificationService;

  @Value("${discodeit.storage.s3.presigned-url-expiration:600}") // 기본값 10분
  private long presignedUrlExpirationSeconds;

  public S3BinaryContentStorage(
      @Value("${discodeit.storage.s3.access-key}") String accessKey,
      @Value("${discodeit.storage.s3.secret-key}") String secretKey,
      @Value("${discodeit.storage.s3.region}") String region,
      @Value("${discodeit.storage.s3.bucket}") String bucket,
      BinaryContentRepository binaryContentRepository,
      AsyncTaskFailureRepository asyncTaskFailureRepository,
      NotificationService notificationService
  ) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;
    this.bucket = bucket;
    this.binaryContentRepository = binaryContentRepository;
    this.asyncTaskFailureRepository = asyncTaskFailureRepository;
    this.notificationService = notificationService;
  }


  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    String key = binaryContentId.toString();
    try {
      S3Client s3Client = getS3Client();

      PutObjectRequest request = PutObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .build();

      s3Client.putObject(request, RequestBody.fromBytes(bytes));
      log.info("S3에 파일 업로드 성공: {}", key);

      binaryContentRepository.findById(binaryContentId).ifPresentOrElse(
          binaryContent -> {
            binaryContent.setUploadStatus(UploadStatus.SUCCESS);
            binaryContentRepository.save(binaryContent);
            log.info("BinaryContent 상태를 SUCCESS로 업데이트함: {}", binaryContentId);
          },
          () -> {
            log.warn("BinaryContent 엔티티를 찾을 수 없어 상태를 업데이트하지 못함: {}", binaryContentId);
          }
      );

      return binaryContentId;
    } catch (S3Exception e) {
      log.error("S3에 파일 업로드 실패: {}", e.getMessage());
      throw new RuntimeException("S3에 파일 업로드 실패: " + key, e);
    }
  }

  @Override
  @Async
  @Retryable(
      value = {Exception.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public CompletableFuture<Void> putAsync(UUID id, byte[] content) {
    log.info("비동기 파일 업로드 시작: id={}", id);
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    this.put(id, content);
    return CompletableFuture.completedFuture(null);
  }

  @Recover
  public CompletableFuture<Void> recover(Exception e, UUID id, byte[] content) {
    String requestId = MDC.get("requestId");
    String userIdStr = MDC.get("userId");
    UUID userId = userIdStr != null ? UUID.fromString(userIdStr) : null;

    log.error("S3 업로드 최종 실패. fileId={}, userId={}, requestId={}, error={}", id, userId, requestId, e.getMessage(), e);

    binaryContentRepository.findById(id).ifPresentOrElse(
        binaryContent -> {
          binaryContent.setUploadStatus(UploadStatus.FAILED);
          binaryContentRepository.save(binaryContent);
          log.warn("BinaryContent 상태를 FAILED로 업데이트함: {}", id);
        },
        () -> log.error("BinaryContent 엔티티를 찾을 수 없습니다. fileId={}", id)
    );

    // 실패 로그 저장
    String reason = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
    String taskName = "S3BinaryContentStorage.putAsync";

    AsyncTaskFailure failure = new AsyncTaskFailure(
        taskName,
        requestId != null ? requestId : "UNKNOWN",
        reason
    );
    asyncTaskFailureRepository.save(failure);
    log.info("AsyncTaskFailure 기록 저장 완료. task={}, requestId={}", taskName, requestId);

    if (userId != null) {
      notificationService.publishNotificationEvent(
          userId,
          "파일 업로드 실패",
          "파일 업로드 중 문제가 발생했습니다. 다시 시도해주세요.",
          NotificationType.ASYNC_FAILED,
          null
      );
      log.info("업로드 실패 알림 전송 완료. userId={}", userId);
    }

    return CompletableFuture.completedFuture(null);
  }




  @Override
  public InputStream get(UUID binaryContentId) {
    String key = binaryContentId.toString();
    try {
      S3Client s3Client = getS3Client();

      GetObjectRequest request = GetObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .build();

      byte[] bytes = s3Client.getObjectAsBytes(request).asByteArray();
      return new ByteArrayInputStream(bytes);
    } catch (S3Exception e) {
      log.error("S3에서 파일 다운로드 실패: {}", e.getMessage());
      throw new NoSuchElementException("File with key " + key + " does not exist");
    }
  }

  private S3Client getS3Client() {
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )
        )
        .build();
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentDto metaData) {
    try {
      String key = metaData.id().toString();
      String presignedUrl = generatePresignedUrl(key, metaData.contentType());

      log.info("생성된 Presigned URL: {}", presignedUrl);

      return ResponseEntity
          .status(HttpStatus.FOUND)
          .header(HttpHeaders.LOCATION, presignedUrl)
          .build();
    } catch (Exception e) {
      log.error("Presigned URL 생성 실패: {}", e.getMessage());
      throw new RuntimeException("Presigned URL 생성 실패", e);
    }
  }

  private String generatePresignedUrl(String key, String contentType) {
    try (S3Presigner presigner = getS3Presigner()) {
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .responseContentType(contentType)
          .build();

      GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofSeconds(presignedUrlExpirationSeconds))
          .getObjectRequest(getObjectRequest)
          .build();

      PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
      return presignedRequest.url().toString();
    }
  }

  private S3Presigner getS3Presigner() {
    return S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )
        )
        .build();
  }
} 