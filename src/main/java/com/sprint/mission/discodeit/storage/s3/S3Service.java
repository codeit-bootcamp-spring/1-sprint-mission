package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.util.EnvLoader;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@RequiredArgsConstructor
public class S3Service {

  private final String bucket;  // 작업할 대상 S3 버킷 이름
  private final S3Client s3Client;  // 실제로 S3에 요청을 보낼 때 사용하는 클라이언트
  private final S3Presigner s3Presigner;  // url에 서명을 추가해서 임시 접근 링크(Presigned URL)를 생성할 때 사용

  // 생성자
  public S3Service() {
    Properties env = EnvLoader.loadEnv();

    // AwsBasicCredentials 클래스
    // AWS 서비스에 액세스하기 위한 기본 자격 증명을 나타내는 클래스
    // 기본 자격 증명은 access key와 secret key로 이루어짐
    AwsBasicCredentials credentials = AwsBasicCredentials.create(
        env.getProperty("AWS_S3_ACCESS_KEY"),
        env.getProperty("AWS_S3_SECRET_KEY")
    );

    Region region = Region.of(env.getProperty("AWS_S3_REGION"));
    this.bucket = env.getProperty("AWS_S3_BUCKET");

    this.s3Client = S3Client.builder()
        .region(region)
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();

    this.s3Presigner = S3Presigner.builder()
        .region(region)
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

  // 업로드
  public void upload(String key, String filepath) {   // key: S3 내부에서 파일이 저장될 경로
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    s3Client.putObject(putObjectRequest, Paths.get(filepath));
  }

  // 다운로드
  public void download(String key, String downloadPath) {   // key: 다운로드할 S3의 파일 키
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    s3Client.getObject(getObjectRequest, Paths.get(downloadPath));
  }

  // Presigned URL 생성
  public URL generatePresignedUrl(String key) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()  // url 만들기위한 요청 정보
        .bucket(bucket)
        .key(key)
        .build();

    GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))    // 10분동안 유효한 url 생성
        .getObjectRequest(getObjectRequest)
        .build();

    return s3Presigner.presignGetObject(getObjectPresignRequest).url();
  }
}
