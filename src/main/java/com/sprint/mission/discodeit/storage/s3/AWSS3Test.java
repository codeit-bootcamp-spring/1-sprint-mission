package com.sprint.mission.discodeit.storage.s3;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AWSS3Test {

  private final Properties properties;
  private final S3Client s3Client;
  private final String bucketName;

  public AWSS3Test() throws IOException {
    // .env 파일에서 AWS 설정 로드
    properties = new Properties();
    Path envPath = Paths.get(".env");
    try (InputStream is = Files.newInputStream(envPath)) {
      properties.load(is);
    }

    // AWS 인증 정보 설정
    String accessKey = properties.getProperty("AWS_S3_ACCESS_KEY");
    String secretKey = properties.getProperty("AWS_S3_SECRET_KEY");
    String region = properties.getProperty("AWS_S3_REGION");
    bucketName = properties.getProperty("AWS_S3_BUCKET");

    // S3 클라이언트 생성
    AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
    s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
        .build();
  }

  // 파일 업로드 테스트
  public String testUpload() throws IOException {
    // 테스트 파일 생성
    Path tempFile = Files.createTempFile("test", ".txt");
    Files.writeString(tempFile, "This is a test file for S3 upload.");

    // 파일 업로드
    String key = UUID.randomUUID().toString();
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromFile(tempFile.toFile()));
    System.out.println("파일이 S3에 업로드되었습니다. 키: " + key);

    // 임시 파일 삭제
    Files.deleteIfExists(tempFile);

    return key; // 다운로드 테스트를 위해 키 반환
  }

  // 파일 다운로드 테스트
  public void testDownload(String key) throws IOException {
    // 파일 다운로드
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);

    // 다운로드한 데이터 읽기
    byte[] content = response.readAllBytes();
    String fileContent = new String(content);
    System.out.println("다운로드한 콘텐츠: " + fileContent);
  }

  // Presigned URL 생성 테스트
  public void testPresignedUrl(String key) {
    // S3 Presigner 생성
    S3Presigner presigner = S3Presigner.builder()
        .region(s3Client.serviceClientConfiguration().region())
        .credentialsProvider(s3Client.serviceClientConfiguration().credentialsProvider())
        .build();

    // GetObject에 대한 Presigned URL 생성
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
    String presignedUrl = presignedRequest.url().toString();

    System.out.println("Presigned URL: " + presignedUrl);

    presigner.close();
  }

  public static void main(String[] args) throws IOException {
    AWSS3Test test = new AWSS3Test();
    String key = test.testUpload();
    test.testDownload(key);
    test.testPresignedUrl(key);
  }
}
