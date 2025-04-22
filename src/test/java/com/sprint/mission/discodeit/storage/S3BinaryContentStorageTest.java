package com.sprint.mission.discodeit.storage;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Testcontainers
class S3BinaryContentStorageTest {

  static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse(
      "localstack/localstack");

  @Container
  static LocalStackContainer localstack = new LocalStackContainer(LOCALSTACK_IMAGE)
      .withServices(LocalStackContainer.Service.S3);

  private S3BinaryContentStorage storage;
  private S3Client s3Client;

  private final String bucket = "test-bucket";

  @BeforeEach
  void setUp() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(
        localstack.getAccessKey(), localstack.getSecretKey());

    s3Client = S3Client.builder()
        .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.S3))
        .region(Region.of(localstack.getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();

    s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());

    S3Presigner presigner = S3Presigner.builder()
        .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.S3))
        .region(Region.of(localstack.getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();

    storage = new S3BinaryContentStorage(s3Client, presigner, bucket, 600);
  }

  @Test
  void putAndGet_shouldSucceed() throws Exception {
    UUID id = UUID.randomUUID();
    byte[] content = "Hello LocalStack S3".getBytes();

    storage.put(id, content);

    InputStream inputStream = storage.get(id);
    byte[] result = inputStream.readAllBytes();

    assertThat(result).isEqualTo(content);
  }

  @Test
  void download_shouldReturnRedirectUrl() {
    UUID id = UUID.randomUUID();
    byte[] content = "Presigned test".getBytes();
    storage.put(id, content);

    BinaryContentDto dto = new BinaryContentDto();
    dto.setId(id);
    dto.setContentType("text/plain");
    dto.setFileName("test.txt");
    dto.setSize(content.length);
    ResponseEntity<?> response = storage.download(dto);

    assertThat(response.getStatusCodeValue()).isEqualTo(302);
    assertThat(response.getHeaders().getFirst("Location")).contains(bucket);
  }
}
