package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@RequiredArgsConstructor
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  @Value("${discodeit.storage.s3.bucket}")
  private String bucketName;

  @Value("${discodeit.storage.s3.presigned-url-expiration}")
  private long presignedUrlExpiration;

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    String key = binaryContentId.toString();

    try {
      PutObjectRequest request = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .build();

      s3Client.putObject(request, RequestBody.fromBytes(bytes));
      return binaryContentId;
    } catch (S3Exception e) {
      throw new RuntimeException("[ERROR] S3 업로드 실패: " + e.awsErrorDetails().errorMessage(), e);
    }
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    String key = binaryContentId.toString();

    try {
      GetObjectRequest request = GetObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .build();

      ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(request);
      return new ByteArrayInputStream(objectBytes.asByteArray());
    } catch (NoSuchKeyException e) {
      throw new IllegalArgumentException("[ERROR] 존재하지 않는 파일입니다.");
    } catch (S3Exception e) {
      throw new RuntimeException("[ERROR] S3 다운로드 실패: " + e.awsErrorDetails().errorMessage(), e);
    }
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentDto binaryContentDto) {
    String key = binaryContentDto.id().toString();

    try {
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .build();

      GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))  // 설정한 유효기간 사용
          .getObjectRequest(getObjectRequest)
          .build();

      PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
      log.info("Presigned URL = {}", presignedRequest.url());

      return ResponseEntity
          .status(HttpStatus.FOUND)  // 302 Redirect
          .header(HttpHeaders.LOCATION, presignedRequest.url().toString())
          .build();

    } catch (S3Exception e) {
      throw new RuntimeException("[ERROR] S3 presigned URL 생성 실패: " + e.awsErrorDetails().errorMessage(), e);
    }
  }
}
