package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binary.AWSException;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedFileUpload;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3AsyncClient s3AsyncClient;
  private final S3TransferManager s3TransferManager;

  @Value("${discodeit.storage.s3.access-key}")
  private String accessKey;

  @Value("${discodeit.storage.s3.secret-key}")
  private String secretKey;

  @Value("${discodeit.storage.s3.region}")
  private String region;

  @Value("${discodeit.storage.s3.bucket}")
  private String bucket;

  @Autowired
  public S3BinaryContentStorage(S3AsyncClient s3AsyncClient, S3TransferManager s3TransferManager) {
    this.s3AsyncClient = s3AsyncClient;
    this.s3TransferManager = s3TransferManager;
  }

  public S3BinaryContentStorage(String accessKey, String secretKey, String region, String bucket) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;
    this.bucket = bucket;

    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
    this.s3AsyncClient = S3AsyncClient.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();

    this.s3TransferManager = S3TransferManager.builder()
        .s3Client(this.s3AsyncClient)
        .build();
  }

  // 파일 다운로드
  @Override
  public ResponseEntity<?> download(BinaryContentDto file) {
    try {
      String presingedUrl = generatePresingedUrl(file.id().toString(), file.contentType());

      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(URI.create(presingedUrl));

      log.info("파일 다운로드: {}", file.fileName());
      return new ResponseEntity<>(headers, HttpStatus.FOUND);
    } catch (Exception e) {
      log.error("파일 다운로드 실패: {}", file.fileName());
      throw new AWSException(Instant.now(), ErrorCode.AWS_ERROR,
          Map.of(file.fileName(), ErrorCode.AWS_ERROR.getMessage()));
    }
  }

  // 파일 업로드
  @Override
  public UUID put(UUID fileId, byte[] bytes) {
    String bucketName = PropertiesUtils.getBucket();

    try {
      Path tempFile = Files.createTempFile("s3-" + UUID.randomUUID(), ".tmp");
      Files.write(tempFile, bytes);

      UploadFileRequest request = UploadFileRequest.builder()
          .putObjectRequest(b -> b.bucket(bucketName).key(fileId.toString()))
          .source(tempFile)
          .build();

      FileUpload fileUpload = s3TransferManager.uploadFile(request);
      CompletedFileUpload uploadResult = fileUpload.completionFuture().join();

      Files.delete(tempFile);
      log.info("파일 업로드: {}", uploadResult.response().eTag());

      return fileId;
    } catch (Exception e) {
      log.error("파일 업로드 실패: {}", fileId);
      throw new AWSException(Instant.now(), ErrorCode.AWS_ERROR,
          Map.of(fileId.toString(), ErrorCode.AWS_ERROR.getMessage()));
    }
  }


  // 파일 조회(읽기)
  @Override
  public InputStream get(UUID fileId) {
    try {
      S3Client s3Client = S3Client.create();

      GetObjectRequest objectRequest = GetObjectRequest.builder()
          .bucket(bucket)
          .key(fileId.toString())
          .build();

      ResponseInputStream<GetObjectResponse> object = s3Client.getObject(objectRequest);

      log.info("파일 get: {}", fileId);
      return object;
    } catch (Exception e) {
      log.error("파일 get 실패: {}", fileId);
      throw new AWSException(Instant.now(), ErrorCode.AWS_ERROR,
          Map.of(fileId.toString(), ErrorCode.AWS_ERROR.getMessage()));
    }
  }

  // presigned url 생성
  private String generatePresingedUrl(String key, String contentType) {
    String bucketName = PropertiesUtils.getBucket();

    try (S3Presigner presigner = S3Presigner.create()) {
      GetObjectRequest objectRequest = GetObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .responseContentType(contentType)
          .responseContentDisposition("attachment; filename=\"" + key + "\"")
          .build();

      GetObjectPresignRequest presignedRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofMinutes(10)) // 10분동안 유효
          .getObjectRequest(objectRequest)
          .build();

      PresignedGetObjectRequest presignedResult = presigner.presignGetObject(presignedRequest);
      log.info("Presigned URL: [{}]", presignedResult.url().toString());
      log.info("HTTP method: [{}]", presignedResult.httpRequest().method());

      return presignedResult.url().toExternalForm();
    } catch (Exception e) {
      log.error("Presigned URL 생성 중 에러 발생 {}", e.getMessage());
      throw new AWSException(Instant.now(), ErrorCode.AWS_ERROR,
          Map.of(key, ErrorCode.AWS_ERROR.getMessage()));
    }
  }

}
