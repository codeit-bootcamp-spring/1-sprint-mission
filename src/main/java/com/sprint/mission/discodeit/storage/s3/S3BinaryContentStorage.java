package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.AsyncTaskFailure;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentUploadException;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import java.io.InputStream;
import java.time.Duration;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final String region;

  private final String bucket;

  private final String accessKey;

  private final String secretKey;

  private final S3Client s3Client;

  public S3BinaryContentStorage(
      @Value("${discodeit.s3.region}") String region,
      @Value("${discodeit.s3.bucket}") String bucket,
      @Value("${discodeit.s3.access-key}") String accessKey,
      @Value("${discodeit.s3.secret-key}") String secretKey) {
    this.region = region;
    this.bucket = bucket;
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)))
        .build();
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {

    //s3로 보낼 요청(PutObjectRequest) 만들기
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(id.toString())
        // .acl("public-read") 퍼블릭 읽기 권한 주기
        .build();

    //s3에 파일과 요청 업로드
    s3Client.putObject(putObjectRequest,
        RequestBody.fromBytes(bytes));

    return id;
  }

  @Async("binaryContentTaskExecutor")
  @Override
  @Retryable(
      retryFor = {
          BinaryContentUploadException.class
      },
      maxAttempts = 3,
      backoff = @Backoff(
          delay = 500,      // 0.5초 시작
          multiplier = 2.0, // 매번 2배씩 증가
          maxDelay = 5000   // 최대 5초
      )
  )
  public CompletableFuture<UUID> asyncPut(UUID id, byte[] file) {
    log.info("파일 s3에 저장 시작: id = {}, 스레드 = {}, 사용자 = {}",
        id,
        Thread.currentThread().getName(),
        getCurrentUser().id()
    );

    //s3로 보낼 요청(PutObjectRequest) 만들기
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(id.toString())
        // .acl("public-read") 퍼블릭 읽기 권한 주기
        .build();

    //s3에 파일과 요청 업로드
    s3Client.putObject(putObjectRequest,
        RequestBody.fromBytes(file));

    return CompletableFuture.completedFuture(id);
  }

  // SecurityContext에서 사용자 정보 가져오기
  private UserDto getCurrentUser() {
    return ((DiscodeitUserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal()).getUserDto();
  }

  // 재시도 실패 시 처리 - 무조건 맨 첫번째 매개변수는 예외여야함
  @Recover
  public CompletableFuture<UUID> recoverAsyncPut(BinaryContentUploadException ex, UUID id,
      byte[] content) {

    AsyncTaskFailure asyncTaskFailure = new AsyncTaskFailure("UPLOAD_FILE",
        MDC.get("requestId"),
        ex.getMessage());

    log.error("파일 저장 최종 실패: AsyncTaskFailure = {}", asyncTaskFailure.toString());

    CompletableFuture<UUID> future = new CompletableFuture<>();
    future.completeExceptionally(ex);
    return future;
  }

  @Override
  public InputStream get(UUID uuid) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(uuid.toString())
        .build();

    return s3Client.getObject(getObjectRequest);
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentDto binaryContentDto) {
    return ResponseEntity.status(HttpStatus.FOUND)
        .header(HttpHeaders.LOCATION, generatePresignedUrl(binaryContentDto.id().toString()))
        .build();
  }

  private S3Client getS3Client() {
    return s3Client;
  }

  private String generatePresignedUrl(String objectKey) {
    // 1. S3Presigner 생성
    S3Presigner presigner = S3Presigner.builder()
        .region(Region.of(region))
        .build();

    // 2. 다운로드 요청(GetObjectRequest) 생성
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(objectKey)
        .build();

    // 3. 프리사인 요청(GetObjectPresignRequest) 생성 - 30분 유효
    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(30))
        .getObjectRequest(getObjectRequest)
        .build();

    // 4. 프리사인드 URL 생성(요청 보내서 받음!)
    PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(
        presignRequest);

    // 5. 생성된 프리사인드 url 반환
    return presignedGetObjectRequest.url().toString();
  }
}
