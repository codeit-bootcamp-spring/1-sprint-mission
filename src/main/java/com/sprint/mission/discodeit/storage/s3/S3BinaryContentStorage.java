package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
