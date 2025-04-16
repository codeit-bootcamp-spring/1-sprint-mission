package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.storage.StorageException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Component
@ConditionalOnProperty(value = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private String accessKey;
  private String secretKey;
  private String region;
  private String bucket;
  private int presignedUrlExpiration;
  private S3Client s3Client;
  private S3Presigner s3Presigner;

  public S3BinaryContentStorage(
      @Value("${discodeit.storage.s3.access-key}") String accessKey,
      @Value("${discodeit.storage.s3.secret-key}") String secretKey,
      @Value("${discodeit.storage.s3.region}") String region,
      @Value("${discodeit.storage.s3.bucket}") String bucket,
      @Value("${discodeit.storage.s3.presigned-url-expiration}") int presignedUrlExpiration) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;
    this.bucket = bucket;
    this.presignedUrlExpiration = presignedUrlExpiration;
    this.s3Client = getS3Client();
    this.s3Presigner = getS3Presigner();
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    // PutObjectRequest
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(id.toString())
        .build();

    try {
      s3Client.putObject(putObjectRequest,
          RequestBody.fromBytes(bytes));

    } catch (S3Exception e) {
      log.error("S3 예외 발생 - 코드: {}, 메시지: {}", e.awsErrorDetails().errorCode(),
          e.awsErrorDetails().errorMessage());
      throw new StorageException(ErrorCode.S3_UPLOAD_FAILED);

    } catch (SdkClientException e) {
      log.error("AWS SDK 예외 발생", e);
      throw new StorageException(ErrorCode.SDK_ERROR);

    } catch (Exception e) {
      throw new RuntimeException("파일 업로드 중 예기치 않은 오류 발생하였습니다.", e);
    }

    return id;
  }

  @Override
  public InputStream get(UUID id) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(id.toString())
        .build();

    try {
      return s3Client.getObject(getObjectRequest);

    } catch (SdkClientException e) {
      log.error("AWS SDK 예외 발생", e);
      throw new StorageException(ErrorCode.SDK_ERROR);

    } catch (NoSuchKeyException e) {
      throw new StorageException(ErrorCode.S3_KEY_NOT_FOUND, Map.of("key", id));

    } catch (Exception e) {
      throw new RuntimeException("파일 업로드 중 예기치 않은 오류 발생하였습니다.", e);
    }
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentResponse binaryContentResponse) {
    String key = binaryContentResponse.id().toString();
    String contentType = binaryContentResponse.contentType();

    String presignedUrl = generatePresignedUrl(key, contentType);

    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(presignedUrl)) // 리다이렉트 방식
        .build();
  }

  private S3Client getS3Client() {
    AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
        .build();
  }

  private S3Presigner getS3Presigner() {
    AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

    return S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
        .build();
  }

  // 클라이언트 단에 넘겨줄 url
  private String generatePresignedUrl(String key, String contentType) {

    try {

      // 다운로드 요청 생성
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .responseContentType(contentType)
          .build();

      // 프리사인 요청 생성
      GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
          .getObjectRequest(getObjectRequest)
          .build();

      PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(
          presignRequest);

      String presignedUrl = presignedGetObjectRequest.url().toString();

      log.debug("Presigned URL 생성 성공: {}", presignedUrl);
      return presignedGetObjectRequest.url().toString();

    } catch (S3Exception e) {

      log.error("Presigned URL 생성 중 S3 예외 발생 - 코드: {}, 메시지: {}"
          , e.awsErrorDetails().errorCode(), e.awsErrorDetails().errorMessage());
      throw new StorageException(ErrorCode.S3_PRESIGND_URL_CREATE_FAILED);

    } catch (Exception e) {
      log.error("Presigned URL 생성 중 알 수 없는 오류 발생", e);
      throw new RuntimeException(e);
    }
  }

}
