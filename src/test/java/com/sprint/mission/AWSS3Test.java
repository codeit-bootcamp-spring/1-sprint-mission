package com.sprint.mission;

import java.nio.file.Paths;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class AWSS3Test {

  private final Properties properties = new Properties();

  public AWSS3Test() throws Exception {
    properties.load(Paths.get(".env").toUri().toURL().openStream());
  }

  private S3Client getClient() {
    return S3Client.builder()
        .region(Region.of(properties.getProperty("AWS_S3_REGION")))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    properties.getProperty("AWS_S3_ACCESS_KEY"),
                    properties.getProperty("AWS_S3_SECRET_KEY")
                )
            )
        )
        .build();
  }

  @Test
  void uploadTest() {
    var client = getClient();
    client.putObject(PutObjectRequest.builder()
            .bucket(properties.getProperty("AWS_S3_BUCKET"))
            .key("test-file.txt")
            .build(),
        Paths.get("README.md"));
  }

  @Test
  void downloadTest() {
    var client = getClient();
    client.getObject(GetObjectRequest.builder()
            .bucket(properties.getProperty("AWS_S3_BUCKET"))
            .key("test-file.txt")
            .build(),
        Paths.get("downloaded-test-file.txt"));
  }

  @Test
  void presignedUrlTest() {
  }
}