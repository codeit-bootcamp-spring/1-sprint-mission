package com.sprint.mission.discodeit.storage.s3;

import java.io.FileInputStream;
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

public class AWS3Test {

  private final Properties props = new Properties();
  private final S3Client s3Client;

  public static void main(String[] args) {
    AWS3Test test = new AWS3Test();
    // test.upload();
    // test.download();
    test.generatePresignedUrl();
  }

  public AWS3Test() {

    // .env 로드 + Properties 클래스 설정
    try {
      props.load(new FileInputStream(".env"));
    } catch (IOException e) {
      throw new RuntimeException("환경 설정 파일(.env) 로드 실패");
    }

    // upload, download 등에 필요한  s3Client 생성
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
    String key = "test.txt"; // 버킷에 저장될 객체의 key 명칭
    String fileName = "test";

    s3Client.putObject(builder ->
        builder.bucket(bucketName).key(key).build(), Paths.get(fileName));
  }

  void download() {
    String bucketName = props.getProperty("AWS_S3_BUCKET");
    String key = "downloaded-test.txt"; // 버킷에서 다운로드할 객체의 이름
    String objPath = "downloaded-test.txt"; // 로컬에서 저장할 이름

    s3Client.getObject(
        builder -> builder.bucket(bucketName).key(key).build(), Paths.get(objPath)
        // 다운로드한 파일 해당 경로에 저장
    );
  }

  void generatePresignedUrl() {
    String bucketName = props.getProperty("AWS_S3_BUCKET");
    String key = "presigned-url-test.txt";

    // Presign client 만들기
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

    // 요청 객체 생성
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    // Presigned URL 생성 요청 - limit, 어떤 객체를
    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .getObjectRequest(getObjectRequest)
        .build();

    // URL 생성
    PresignedGetObjectRequest presignedReqeust = presigner.presignGetObject(presignRequest);

    System.out.println("생성된 Presigned Url    :     " + presignedReqeust.url());

    presigner.close();
  }

}
