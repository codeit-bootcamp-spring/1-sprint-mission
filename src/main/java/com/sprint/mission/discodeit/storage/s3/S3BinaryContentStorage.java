package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.AsyncTaskFailure;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.exception.upload.CriticalException;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucket;

  @Value("${discodeit.storage.s3.presigned-url-expiration:600}") // 기본값 10분
  private long presignedUrlExpirationSeconds;

  public S3BinaryContentStorage(
      @Value("${discodeit.storage.s3.access-key}") String accessKey,
      @Value("${discodeit.storage.s3.secret-key}") String secretKey,
      @Value("${discodeit.storage.s3.region}") String region,
      @Value("${discodeit.storage.s3.bucket}") String bucket
  ) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;
    this.bucket = bucket;
  }

  @Retryable(
      value = { IOException.class },
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000)
  )
  @Async
  @Override
  public CompletableFuture<UUID> put(UUID binaryContentId, byte[] bytes) {
    try {
      S3Client s3Client = getS3Client();
      PutObjectRequest request = PutObjectRequest.builder()
          .bucket(bucket)
          .key(binaryContentId.toString())
          .build();

      s3Client.putObject(request, RequestBody.fromBytes(bytes));
      log.info("S3에 파일 업로드 성공: {}", binaryContentId);
      return CompletableFuture.completedFuture(binaryContentId);
    } catch (S3Exception e) {
      log.error("S3에 파일 업로드 실패: {}", e.getMessage());
      throw new RuntimeException("S3에 파일 업로드 실패: " + binaryContentId, e);
    }
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

  @Recover
  private AsyncTaskFailure recover(IOException e, UUID binaryContentId, byte[] bytes) {
    log.warn("업로드 재시도 모두 실패. 로컬 백업 시도: {}", binaryContentId, e);

    try {
      Path backupDir = Paths.get("/backup/binary");
      Files.createDirectories(backupDir);

      Path backupPath = backupDir.resolve(binaryContentId.toString());
      Files.write(backupPath, bytes);

      String requestId = MDC.get("requestId");

      AsyncTaskFailure failure = new AsyncTaskFailure(
          "BinaryUpload",
          requestId != null ? requestId : "UNKNOWN",
          "Failed after retries: " + e.getMessage()
      );
      log.error("AsyncTaskFailure 기록: {}", failure);

      return failure;

    } catch (IOException ioEx) {
      log.error("로컬 백업도 실패", ioEx);
      throw new CriticalException("로컬 백업 실패", ioEx);
    }
  }

} 