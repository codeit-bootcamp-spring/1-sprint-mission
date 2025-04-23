package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URI;
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
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  @Value("${discodeit.storage.s3.access-key}")
  private String accessKey;

  @Value("${discodeit.storage.s3.secret-key}")
  private String secretKey;

  @Value("${discodeit.storage.s3.region}")
  private String region;

  @Value("${discodeit.storage.s3.bucket}")
  private String bucket;

  @Value("${discodeit.storage.s3.presigned-url-expiration:600}")
  private int presignedUrlExpirationSeconds;

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    S3Client s3Client = getS3Client();

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(binaryContentId.toString())
        .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
    s3Client.close();

    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    S3Client s3Client = getS3Client();

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(binaryContentId.toString())
        .build();

    ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
    return s3Object;
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentDto metaData) {
    String presignedUrl = generatePresignedUrl(
        metaData.id().toString(),
        metaData.contentType());

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(presignedUrl));

    if (metaData.fileName() != null) {
      headers.add(HttpHeaders.CONTENT_DISPOSITION,
          "attachment; filename=\"" + metaData.fileName());
    }
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  private S3Client getS3Client() {
    StaticCredentialsProvider credentials = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(accessKey, secretKey));
    Region awsRegion = Region.of(region);

    return S3Client.builder()
        .region(awsRegion)
        .credentialsProvider(credentials)
        .build();
  }

  public String generatePresignedUrl(String key, String contentType) {
    StaticCredentialsProvider credentials = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(accessKey, secretKey));
    Region awsRegion = Region.of(region);

    S3Presigner s3Presigner = S3Presigner.builder()
        .region(awsRegion)
        .credentialsProvider(credentials)
        .build();

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentType(contentType)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofSeconds(presignedUrlExpirationSeconds))
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
    String url = presignedRequest.url().toString();
    s3Presigner.close();

    return url;
  }

}
