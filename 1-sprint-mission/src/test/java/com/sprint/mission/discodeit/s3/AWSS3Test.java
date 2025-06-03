package com.sprint.mission.discodeit.s3;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URL;
import java.time.Duration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AWSS3Test {

  private static S3Client s3;
  private static String bucketName;

  @BeforeAll
  public static void setup() {
    Dotenv dotenv = Dotenv.load();

    s3 = S3Client.builder()
        .region(Region.of(dotenv.get("AWS_S3_REGION")))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(
                dotenv.get("AWS_S3_ACCESS_KEY"),
                dotenv.get("AWS_S3_SECRET_KEY")
            )
        ))
        .build();

    bucketName = dotenv.get("AWS_S3_BUCKET");
  }

  @Test
  public void upload() {
    String key = "test-upload.txt";
    String content = "Hello from AWS S3 Test";
    s3.putObject(PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build(),
        RequestBody.fromString(content));
    System.out.println("Upload successful!");
  }

  @Test
  public void download() {
    String key = "test-download.txt";
    GetObjectResponse response = s3.getObject(GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build(), software.amazon.awssdk.core.sync.ResponseTransformer.toFile(
        java.nio.file.Paths.get("downloaded.txt")));
    System.out.println("Downloaded file to downloaded.txt");
  }

  @Test
  public void generate() {
    Dotenv dotenv = Dotenv.load();

    S3Presigner presigner = S3Presigner.builder()
        .region(Region.of(dotenv.get("AWS_S3_REGION")))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(
                dotenv.get("AWS_S3_ACCESS_KEY"),
                dotenv.get("AWS_S3_SECRET_KEY")
            )
        ))
        .build();

    String key = "test-generate.txt";

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .getObjectRequest(getObjectRequest)
        .signatureDuration(Duration.ofMinutes(10))
        .build();

    PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

    URL url = presignedRequest.url();
    System.out.println("Presigned URL: " + url);

    presigner.close();
  }
}
