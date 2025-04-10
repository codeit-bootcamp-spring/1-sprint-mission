package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import java.io.InputStream;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Component
@ConditionalOnProperty(value = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private String accessKey;
  private String secretKey;
  private String region;
  private String bucket;

  public S3BinaryContentStorage(String accessKey, String secretKey, String region, String bucket) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;
    this.bucket = bucket;
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    return null;
  }

  @Override
  public InputStream get(UUID id) {
    return null;
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentResponse binaryContentResponse) {
    return null;
  }

  private S3Client getS3Client() {

    return null;
  }

  // 클라이언트 단에 넘겨줄 url
  private String generatePresignedUrl(String key, String contentType) {
    
    return null;
  }

}
