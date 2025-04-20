package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.config.S3Properties;
import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
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

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3Properties s3Properties;

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    String filename = binaryContentId.toString();

    try (S3Client s3Client = getS3Client()) {
      s3Client.putObject(
          PutObjectRequest.builder()
              .bucket(s3Properties.getBucket())
              .key(filename)
              .build(),
          RequestBody.fromBytes(bytes)
      );
    }

    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    String filename = binaryContentId.toString();

    S3Client s3Client = getS3Client();

    return s3Client.getObject(
        GetObjectRequest.builder()
            .bucket(s3Properties.getBucket())
            .key(filename)
            .build()
    );
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto metaData) {
    String key = metaData.id().toString();
    String contentType = metaData.contentType();
    String presignedUrl = generatePresignedUrl(key, contentType);

    return ResponseEntity.status(302)
        .header(HttpHeaders.LOCATION, presignedUrl)
        .build();
  }

  private S3Client getS3Client() {
    return S3Client.builder()
        .region(Region.of(s3Properties.getRegion()))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    s3Properties.getAccessKey(),
                    s3Properties.getSecretKey()
                )
            )
        )
        .build();
  }

  private String generatePresignedUrl(String key, String contentType) {
    try (S3Presigner presigner = S3Presigner.builder()
        .region(Region.of(s3Properties.getRegion()))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    s3Properties.getAccessKey(),
                    s3Properties.getSecretKey()
                )
            )
        )
        .build()) {

      GetObjectRequest.Builder requestBuilder = GetObjectRequest.builder()
          .bucket(s3Properties.getBucket())
          .key(key);

      if (contentType != null && !contentType.isBlank()) {
        requestBuilder = requestBuilder.responseContentType(contentType);
      }

      GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofSeconds(s3Properties.getPresignedUrlExpiration()))
          .getObjectRequest(requestBuilder.build())
          .build();

      PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

      return presignedRequest.url().toString();
    }
  }

}
