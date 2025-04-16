package com.sprint.mission.discodeit.storage.s3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;

class AWSS3Test {

  static S3Client s3Client;
  static S3Presigner presigner;
  static String bucketName;

  @BeforeAll
  static void setup() throws IOException {
    Properties properties = new Properties();
    properties.load(new FileInputStream(".env"));


    String accessKey = properties.getProperty("AWS_S3_ACCESS_KEY");
    String secretKey = properties.getProperty("AWS_S3_SECRET_KEY");
    Region region = Region.of(properties.getProperty("AWS_S3_REGION"));
    bucketName = properties.getProperty("AWS_S3_BUCKET");

    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

    s3Client = S3Client.builder()
        .region(region)
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();

    presigner = S3Presigner.builder()
        .region(region)
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

  @Test
  void testUpload() {
    String filePath = "src/test/resources/sample.txt";
    String keyName = "test/sample.txt";

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(keyName)
        .build();

    s3Client.putObject(putObjectRequest, Paths.get(filePath));

    System.out.println("✅ Upload 완료: " + keyName);
  }

  @Test
  void testDownload() {
    String keyName = "test/sample.txt";
    String downloadPath = "src/test/resources/downloaded_sample.txt";

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(keyName)
        .build();

    s3Client.getObject(getObjectRequest, Paths.get(downloadPath));

    System.out.println("✅ Download 완료: " + downloadPath);
  }

  @Test
  void testPresignedUrl() {
    String keyName = "test/sample.txt";

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(keyName)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .getObjectRequest(getObjectRequest)
        .build();

    String url = presigner.presignGetObject(presignRequest).url().toString();

    System.out.println("✅ Presigned URL: " + url);
  }
}
