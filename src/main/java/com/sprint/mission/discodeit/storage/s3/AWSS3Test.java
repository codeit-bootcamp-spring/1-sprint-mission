package com.sprint.mission.discodeit.storage.s3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
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

public class AWSS3Test {

  private S3Client s3Client;
  private String bucketName;
  private final Properties properties = new Properties();

  public void init() throws IOException {
    Path path = Paths.get(".env");
    if (Files.exists(path)) {
      try (InputStream is = Files.newInputStream(path)) {
        properties.load(is);
      }
    } else {
      System.err.println(".env 파일을 찾을 수 없습니다.");
      return;
    }

    String accessKey = properties.getProperty("AWS_S3_ACCESS_KEY");
    String secretKey = properties.getProperty("AWS_S3_SECRET_KEY");
    String region = properties.getProperty("AWS_S3_REGION");
    bucketName = properties.getProperty("AWS_S3_BUCKET");

    s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
        .build();

    System.out.println("S3 클라이언트가 초기화되었습니다.");
  }

  public void upload(String filePath, String objectKey) throws IOException {
    File file = new File(filePath);

    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(objectKey)
        .build();

    s3Client.putObject(putRequest, RequestBody.fromFile(file));
    System.out.println("파일 업로드 성공: " + objectKey);
  }

  public void download(String objectKey, String downloadPath) throws IOException {
    GetObjectRequest getRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(objectKey)
        .build();

    File downloadFile = new File(downloadPath);
    File parentDir = downloadFile.getParentFile();
    if (parentDir != null && !parentDir.exists()) {
      parentDir.mkdirs();
    }

    try (InputStream is = s3Client.getObject(getRequest);
        OutputStream os = new FileOutputStream(downloadFile)) {
      byte[] buffer = new byte[4096];
      int bytesRead;
      while ((bytesRead = is.read(buffer)) != -1) {
        os.write(buffer, 0, bytesRead);
      }
    }
    System.out.println("파일 다운로드 성공: " + downloadPath);
  }

  public String createPresignedUrl(String objectKey, Duration expiration) {
    String accessKey = properties.getProperty("AWS_S3_ACCESS_KEY");
    String secretKey = properties.getProperty("AWS_S3_SECRET_KEY");
    String region = properties.getProperty("AWS_S3_REGION");

    S3Presigner presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
        .build();

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(objectKey)
        .build();

    GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(expiration)
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(
        getObjectPresignRequest);
    String presignedUrl = presignedRequest.url().toString();

    System.out.println("미리 서명된 URL 생성: " + presignedUrl);
    presigner.close();

    return presignedUrl;
  }

  public static void main(String[] args) {
    try {
      AWSS3Test test = new AWSS3Test();
      test.init();

      String testFilePath = "test-file.txt";
      String objectKey = "test/test-file.txt";
      String downloadPath = "downloaded-file.txt";

      System.out.println("==== 업로드 테스트 ====");
      test.upload(testFilePath, objectKey);

      System.out.println("==== 다운로드 테스트 ====");
      test.download(objectKey, downloadPath);

      System.out.println("==== PresignedUrl 생성 테스트 ====");
      String presignedUrl = test.createPresignedUrl(objectKey, Duration.ofMinutes(10));
      System.out.println("생성된 URL: " + presignedUrl);

    } catch (IOException e) {
      System.err.println("테스트 실행 중 오류 발생: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void clear() {
    if (s3Client != null) {
      s3Client.close();
      System.out.println("S3 클라이언트 종료");
    }
  }

}
