package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
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

@Component
@ConditionalOnProperty(
    prefix = "discodeit.storage",
    name = "type",
    havingValue = "s3"
)
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucket;

  private S3Client s3Client;
  private S3Presigner s3Presigner;

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

  @PostConstruct
  public void init() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
    this.s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
    this.s3Presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucket)
        .key(binaryContentId.toString())
        .build();
    s3Client.putObject(request, RequestBody.fromBytes(bytes));
    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(bucket)
        .key(binaryContentId.toString())
        .build();
    return s3Client.getObject(request);
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto metaData) {
    String url = generatePresignedUrl(metaData.id().toString(), metaData.contentType());
    return ResponseEntity.status(HttpStatus.FOUND)
        .header(HttpHeaders.LOCATION, url)
        .build();
  }

  private String generatePresignedUrl(String key, String contentType) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentType(contentType)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
    return presignedRequest.url().toString();
  }
}
