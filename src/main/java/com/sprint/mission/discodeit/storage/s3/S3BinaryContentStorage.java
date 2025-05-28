package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucket;
  private final int presignedUrlExpiration;
  private final S3Client s3Client; // s3Client를 하나로 쓰기 위해서 추가
  private final S3Presigner s3Presigner;

  public S3BinaryContentStorage(String accessKey, String secretKey, String region, String bucket,
      int presignedUrlExpiration) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;
    this.bucket = bucket;
    this.presignedUrlExpiration = presignedUrlExpiration;
    this.s3Client = getS3Client();
    this.s3Presigner = getS3Presigner();
  }

  protected S3Client getS3Client() { // 테스트 때문에 접근 제어자 변경 -> 이런 경우(테스트 때문에 실제 코드 변경)가 흔할까요?
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )
        )
        .build();
  }

  protected S3Presigner getS3Presigner() {
    // Presign Client 생성
    return S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )
        )
        .build();
  }

  private String generatePresignedUrl(String key, String contentType) {
    // 요청 객체 생성
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentType(contentType)// Content-Type 전달, 다운로드할 때 이 타입으로 인식한다.
        .build();

    // Presigned URL 생성 요청
    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(presignedUrlExpiration))
        .getObjectRequest(getObjectRequest)
        .build();

    // URL 생성
    PresignedGetObjectRequest presignedReqeust = s3Presigner.presignGetObject(presignRequest);

    s3Presigner.close();
    return presignedReqeust.url().toString();
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    log.info("S3 버킷에 객체 업로드 시도");
    String key = id.toString();

    try {
      s3Client.putObject(builder ->
          builder.bucket(bucket).key(key).build(), RequestBody.fromBytes(bytes));
      log.info("S3 버킷에 객체 업로드 완료. key: {}", key);
      return id;
    } catch (Exception e) {
      log.error("S3 업로드 실패. key: {}", key, e);
      throw new RuntimeException("S3 객체 업로드 실패: " + key, e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    log.info("S3 버킷에 객체(InputStream) 조회 시도");

    String key = id.toString();

    try {
      // 요청 객체 생성
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .build();
      InputStream inputStream = s3Client.getObject(getObjectRequest);
      log.info("S3 버킷에 객체(InputStream) 조회 완료. key: {}", key);
      return inputStream;
    } catch (RuntimeException e) {
      log.error("S3 객체 조회 실패. key: {}", key, e);
      throw new RuntimeException("S3 객체 조회 실패: " + key, e);
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    log.info("S3 버킷에서 객체 다운로드 시도");
    String key = binaryContentDto.id().toString();

    try {
      String presignedUrl = generatePresignedUrl(key, binaryContentDto.contentType());
      log.info("S3 버킷에서 객체 다운로드 시도 성공");
      return ResponseEntity
          .status(HttpStatus.FOUND) //302
          .header("Location", presignedUrl)
          .build();
    } catch (Exception e) {
      log.error("S3 Presigned URL 생성 실패. key: {}", key, e);
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Presigned URL 생성 실패: " + e.getMessage());
    }
  }
}
