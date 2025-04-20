package com.sprint.mission.discodeit.storage.s3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AWSS3Test {

  private final Properties props = new Properties();
  private final S3Client s3Client;

  public static void main(String[] args) {
    AWSS3Test awss3Test = new AWSS3Test();
    awss3Test.upload();
    awss3Test.download();
    awss3Test.generatePresignedUrl();
  }

  public AWSS3Test() {

    try {
      props.load(new FileInputStream(".env"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    s3Client = S3Client.builder()
        .region(Region.of(props.getProperty("AWS_S3_REGION")))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    props.getProperty("AWS_S3_ACCESS_KEY"),
                    props.getProperty("AWS_S3_SECRET_KEY")
                )
            )
        )
        .build();
  }

  void upload() {
    String bucketName = props.getProperty("AWS_S3_BUCKET");
    String key = "test.txt";
    String fileName = "file/upload_test.txt";

    File tempFile = new File(fileName);
    if (!tempFile.exists()) {
      try (FileOutputStream fos = new FileOutputStream(tempFile)) {
        fos.write("Hello, this is a S3 upload test.".getBytes());
      } catch (IOException e) {
        throw new RuntimeException("테스트 파일 생성 실패", e);
      }
    }

    s3Client.putObject(builder ->
        builder.bucket(bucketName).key(key).build(), Paths.get(fileName));
  }

  void download() {
    String bucketName = props.getProperty("AWS_S3_BUCKET");
    String key = "test.txt";
    String objPath = "file/downloaded-test.txt";

    s3Client.getObject(
        builder -> builder.bucket(bucketName).key(key).build(), Paths.get(objPath)
    );
  }

  void generatePresignedUrl() {
    String bucketName = props.getProperty("AWS_S3_BUCKET");
    String key = "presigned-url-test.txt";

    S3Presigner presigner = S3Presigner.builder()
        .region(Region.of(props.getProperty("AWS_S3_REGION")))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    props.getProperty("AWS_S3_ACCESS_KEY"),
                    props.getProperty("AWS_S3_SECRET_KEY")
                )
            )
        )
        .build();

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedReqeust = presigner.presignGetObject(presignRequest);

    System.out.println("생성된 Presigned Url    :     " + presignedReqeust.url());

    presigner.close();
  }
}
