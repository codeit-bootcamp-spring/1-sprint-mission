package com.sprint.mission.discodeit.storage;

import java.nio.file.Files;
import java.nio.file.Paths;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.*;
import java.time.Duration;
import java.util.Properties;

public class AWS3Test {

  public static void main(String[] args) throws IOException {
    // Properties 로드
    Properties props = new Properties();
    props.load(Files.newInputStream(Paths.get(".env")));

    String accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
    String secretKey = props.getProperty("AWS_S3_SECRET_KEY");
    String region = props.getProperty("AWS_S3_REGION");
    String bucket = props.getProperty("AWS_S3_BUCKET");

    S3Client s3 = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)
        ))
        .build();

    // ✅ 업로드
    String key = "test.txt";
    s3.putObject(PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType("text/plain")
            .build(),
        RequestBody.fromString("This is a test file.")
    );
    System.out.println("✅ 업로드 완료");

    // ✅ Presigned URL 생성
    S3Presigner presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)
        ))
        .build();

    PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(b -> b
        .getObjectRequest(r -> r.bucket(bucket).key(key))
        .signatureDuration(Duration.ofMinutes(5)));

    System.out.println("✅ presignedUrl: " + presignedRequest.url());

    // ✅ 다운로드
    ResponseBytes<GetObjectResponse> downloaded = s3.getObject(GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build(), ResponseTransformer.toBytes());

    System.out.println("✅ 다운로드한 내용: " + new String(downloaded.asByteArray()));
  }
}
