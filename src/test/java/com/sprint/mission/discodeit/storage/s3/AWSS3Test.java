package com.sprint.mission.discodeit.storage.s3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    properties = "spring.config.import=optional:envfile:./.env",
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
class AWSS3Test {

  @BeforeAll
  static void beforeAll() {
    System.out.println("ACCESS KEY = " + AWSTestConfig.get("AWS_S3_ACCESS_KEY"));
    System.out.println("SECRET KEY = " + AWSTestConfig.get("AWS_S3_SECRET_KEY"));
    System.out.println("REGION     = " + AWSTestConfig.get("AWS_S3_REGION"));
    System.out.println("BUCKET     = " + AWSTestConfig.get("AWS_S3_BUCKET"));
  }

  String accessKey = AWSTestConfig.get("AWS_S3_ACCESS_KEY");
  String secretKey = AWSTestConfig.get("AWS_S3_SECRET_KEY");
  String region    = AWSTestConfig.get("AWS_S3_REGION");
  String bucket    = AWSTestConfig.get("AWS_S3_BUCKET");

  private S3Client getClient() {
    return S3Client.builder()
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)))
        .region(Region.of(region))
        .build();
  }

  @Test
  void uploadTest() {
    S3Client s3 = getClient();
    String key = "test/upload.txt";
    byte[] data = "업로드 테스트 데이터".getBytes(StandardCharsets.UTF_8);

    PutObjectResponse response = s3.putObject(
        PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build(),
        RequestBody.fromBytes(data));

    assertThat(response).isNotNull();
    System.out.println("업로드 성공, ETag: " + response.eTag());
  }

  @Test
  void downloadTest() {
    S3Client s3 = getClient();
    String key = "test/upload.txt";
    byte[] originalData = "업로드 테스트 데이터".getBytes(StandardCharsets.UTF_8);

    byte[] downloaded = s3.getObject(
        GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build(),
        ResponseTransformer.toBytes()).asByteArray();

    assertThat(downloaded).isEqualTo(originalData);
    System.out.println("다운로드 성공, 내용: " + new String(downloaded, StandardCharsets.UTF_8));
  }

  @Test
  void presignedUrlTest() {
    S3Presigner presigner = S3Presigner.builder()
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)))
        .region(Region.of(region))
        .build();

    String key = "test/upload.txt";

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .getObjectRequest(GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build())
        .signatureDuration(Duration.ofMinutes(10))
        .build();

    String presignedUrl = presigner.presignGetObject(presignRequest).url().toString();

    assertThat(presignedUrl).contains(key);
    System.out.println("Presigned URL 생성 성공: " + presignedUrl);
  }
}
