package com.sprint.mission.discodeit.storage.s3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AWSS3Test {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final String bucketName;

  public AWSS3Test() {
    // .env 파일에서 AWS 정보 로드
    Properties properties = loadProperties();

    // AWS 자격증명 및 설정 초기화
    String accessKey = properties.getProperty("AWS_S3_ACCESS_KEY");
    String secretKey = properties.getProperty("AWS_S3_SECRET_KEY");
    String region = properties.getProperty("AWS_S3_REGION");
    this.bucketName = properties.getProperty("AWS_S3_BUCKET");

    AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
    Region awsRegion = Region.of(region);

    // S3 클라이언트 생성
    this.s3Client = S3Client.builder()
        .region(awsRegion)
        .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
        .build();

    // S3 Presigner 생성
    this.s3Presigner = S3Presigner.builder()
        .region(awsRegion)
        .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
        .build();
  }

  // .env 환경 변수
  private Properties loadProperties() {
    Properties properties = new Properties();
    try (FileInputStream fis = new FileInputStream(".env")) {
      properties.load(fis);
    } catch (FileNotFoundException e) {
      System.out.println(" .env 파일을 찾을 수 없습니다.");
    } catch (IOException e) {
      throw new RuntimeException("Properties 로드 실패", e);
    }
    return properties;
  }

  public void upload(String filePath, String keyName) {
    try {
      File file = new File(filePath);

      // 업로드 객체
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(keyName)
          .build();

      s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

      System.out.println("성공");
    } catch (S3Exception e) {
      System.out.println("실패 : " + e.getMessage());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String testGeneratePresignedUrl(String keyName, long expirationTimeInMinutes) {
    try {
      // GetObject 작업에 대한 Presigned 요청 생성
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucketName)
          .key(keyName)
          .build();

      GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofMinutes(expirationTimeInMinutes))
          .getObjectRequest(getObjectRequest)
          .build();

      // Presigned URL 생성
      PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
      String presignedUrl = presignedRequest.url().toString();

      System.out.println("Presigned URL 생성 성공: " + presignedUrl);
      return presignedUrl;

    } catch (S3Exception e) {
      System.err.println("Presigned URL 생성 실패: " + e.getMessage());
      return null;
    } catch (Exception e) {
      System.err.println("오류 발생: " + e.getMessage());
      return null;
    }
  }

  public void download(String keyName, String destinationPath) {
    try {
      // S3에서 객체 받기 요청 생성
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucketName)
          .key(keyName)
          .build();

      // S3에서 객체 다운로드
      ResponseInputStream<GetObjectResponse> s3ObjectResponse = s3Client.getObject(
          getObjectRequest);

      // 다운로드한 파일 저장
      try (FileOutputStream outputStream = new FileOutputStream(destinationPath)) {
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = s3ObjectResponse.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
        System.out.println("파일 다운로드 성공: " + destinationPath);
      } catch (IOException e) {
        System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
      }

    } catch (S3Exception e) {
      // S3 서비스 오류
      System.err.println("다운로드 실패: " + e.getMessage());
    } catch (Exception e) {
      // 기타 오류
      System.err.println("오류 발생: " + e.getMessage());
    }
  }

  public void close() {
    if (s3Client != null) {
      s3Client.close();
    }
    if (s3Presigner != null) {
      s3Presigner.close();
    }
  }

  public static void main(String[] args) {
    AWSS3Test s3Test = new AWSS3Test();

    try {
      // 테스트 파일 경로 및 키 이름 설정
      String uploadFilePath = "binaryContents/file.txt";
      String keyName = "test-filess/file.txt";
      String downloadDestination = "binaryContents/downloaded-file.txt";

      // 업로드 테스트
      s3Test.upload(uploadFilePath, keyName);

      // 다운로드 테스트
      s3Test.download(keyName, downloadDestination);

      // Presigned URL 생성 테스트 (유효기간 30분)
      String presignedUrl = s3Test.testGeneratePresignedUrl(keyName, 10);
      System.out.println("생성된 Presigned URL: " + presignedUrl);
    } finally {
      // 리소스 정리
      s3Test.close();
    }
  }
}
