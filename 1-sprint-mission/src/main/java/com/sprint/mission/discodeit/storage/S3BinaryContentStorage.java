package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class S3BinaryContentStorage implements BinaryContentStorage {

  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucket;

  public S3BinaryContentStorage(
      @Value("${discodeit.storage.s3.access-key}") String accessKey,
      @Value("${discodeit.storage.s3.secret-key}") String secretKey,
      @Value("${discodeit.storage.s3.region}") String region,
      @Value("${discodeit.storage.s3.bucket}") String bucket) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;
    this.bucket = bucket;
  }

  @Override
  public UUID put(UUID uuid, byte[] data) {
    String key = uuid.toString();
    S3Client s3 = getS3Client();

    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentLength((long) data.length)
        .build();

    s3.putObject(putRequest, RequestBody.fromBytes(data));
    return uuid;
  }

  @Override
  public InputStream get(UUID uuid) {
    String key = uuid.toString();
    S3Client s3 = getS3Client();

    GetObjectRequest getRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    return s3.getObject(getRequest);
  }


  @Override
  public ResponseEntity<Void> download(BinaryContentDto dto) {
    String presignedUrl = generatePresignedUrl(dto.getKey(), dto.getContentType());
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(java.net.URI.create(presignedUrl));
    return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
  }

  private S3Client getS3Client() {
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)))
        .build();
  }

  public String generatePresignedUrl(String key, String contentType) {
    S3Presigner presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)))
        .build();

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentType(contentType)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .getObjectRequest(getObjectRequest)
        .signatureDuration(Duration.ofMinutes(10))
        .build();

    PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

    presigner.close();
    return presignedRequest.url().toString();
  }
}
