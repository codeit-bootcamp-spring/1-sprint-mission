package com.sprint.mission.discodeit.storage.s3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import org.springframework.web.multipart.MultipartFile;
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

  private final S3Client s3Client;
  private final Region region;
  private final String bucketName;

  public AWSS3Test() {
    Properties props = new Properties();
    try {
      File envFile = new File(".env");

      if (envFile.exists()) {
        props.load(new FileInputStream(envFile));
      } else {
        System.out.println("No env file found");
      }
    } catch (FileNotFoundException e) {
      System.out.println("No env file found");
    } catch (IOException e) {
      System.err.println("속성 로드 중 오류 발생: " + e.getMessage());
    }

    String accessKeyId = props.getProperty("AWS_S3_ACCESS_KEY");
    String secretAccessKey = props.getProperty("AWS_S3_SECRET_KEY");
    region = Region.of(props.getProperty("AWS_S3_REGION"));
    bucketName = props.getProperty("AWS_S3_BUCKET_NAME");

    this.s3Client = S3Client.builder()
        .region(region)
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
        .build();
  }

  public String uploadFile(MultipartFile file) throws IOException {
    String originalFileName = file.getOriginalFilename();
    String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;

    //s3로 보낼 요청(PutObjectRequest) 만들기
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(uniqueFileName)
        // .acl("public-read") 퍼블릭 읽기 권한 주기
        .build();

    //s3에 파일과 요청 업로드
    s3Client.putObject(putObjectRequest,
        RequestBody.fromBytes(file.getBytes()));

    return uniqueFileName;
  }

  public byte[] downloadFile(String fileKey) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(fileKey)
        .build();

    ResponseInputStream<GetObjectResponse> objectData = s3Client.getObject(getObjectRequest);

    try {
      return objectData.readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException("Failed to download file from s3 bucket", e);
    } finally {
      try {
        objectData.close();
      } catch (IOException e) {
      }
    }
  }

  /* Presigned URL 생성

  - S3에 잠시 접근할 수 있는 임시 서명된 URL
  - 서버가 S3Client.presignedGetObject() → 새로운 URL 생성,
                                         지정된 만료시간까지 해당 객체를 다운로드 할 수 있음
  - 대용량, 민감 파일을 안전하게 전달하는 방법 중 하나
  - 퍼블릭 버킷 대신 프라이빗을 유지한 채로, 접근 가능한 URL을 임시로 만들어주는 패턴*/

  public String generatePresignedUrl(String objectKey) {
    // 1. S3Presigner 생성
    S3Presigner presigner = S3Presigner.builder()
        .region(region)
        .build();

    // 2. 다운로드 요청(GetObjectRequest) 생성
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(objectKey)
        .build();

    // 3. 프리사인 요청(GetObjectPresignRequest) 생성 - 30분 유효
    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(30))
        .getObjectRequest(getObjectRequest)
        .build();

    // 4. 프리사인드 URL 생성(요청 보내서 받음!)
    PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(
        presignRequest);

    // 5. 생성된 프리사인드 url 반환
    return presignedGetObjectRequest.url().toString();
  }
}
