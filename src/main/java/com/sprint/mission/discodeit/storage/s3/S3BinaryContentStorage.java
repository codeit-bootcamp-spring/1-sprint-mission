package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest.Builder;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Component
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucket;

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

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(binaryContentId.toString())
        .build();

    getS3Client().putObject(putObjectRequest, RequestBody.fromBytes(bytes));
    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(binaryContentId.toString())
        .build();

    try {
      ResponseInputStream<GetObjectResponse> response = getS3Client().getObject(getObjectRequest);
      return response;
    } catch (Exception e) {
      throw new NoSuchElementException("ID가 " + binaryContentId + "인 파일이 존재하지 않습니다");
    }
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentDto metaData) {
    String presignedUrl = generatePresignedUrl(metaData.id().toString(),
        metaData.contentType());

    // 리다이렉트 응답 생성
    return ResponseEntity
        .status(HttpStatus.FOUND)
        .header(HttpHeaders.LOCATION, presignedUrl)
        .build();
  }

  S3Client getS3Client() {
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)))
        .build();
  }

  String generatePresignedUrl(String key, String contentType) {
    // presignUrlExpiration 값 환경 변수에서 가져오기
    int presignedUrlExpiration;
    try {
      presignedUrlExpiration = Integer.parseInt(
          System.getProperty("discodeit.storage.s3.presigned-url-expiration", "600"));
    } catch (NumberFormatException e) {
      presignedUrlExpiration = 600;
    }

    AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
    try (S3Presigner presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
        .build()) {

      Builder requestBuilder = GetObjectRequest.builder()
          .bucket(bucket)
          .key(key);

      if (contentType != null && !contentType.isEmpty()) {
        requestBuilder.responseContentType(contentType);
      }

      GetObjectRequest getObjectRequest = requestBuilder.build();

      GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
          .getObjectRequest(getObjectRequest)
          .build();

      PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(
          presignRequest);

      return presignedRequest.url().toString();
    }
  }
}
