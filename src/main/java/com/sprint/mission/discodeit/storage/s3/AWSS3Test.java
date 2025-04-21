package com.sprint.mission.discodeit.storage.s3;


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.presigner.PresignRequest;
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

  //AWS
  private static Properties props = new Properties();
  private static String accessKey;
  private static String secretKey;
  private static String region;
  private static String bucket;

  //S3
  private static S3Client s3Client;
  private static S3Presigner s3Presigner;

  //.env 파일에 있는 AWS 정보 업로드
  private static void loadProperties() {
    try (FileInputStream fis = new FileInputStream(".env")) {
      props.load(fis);
      accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
      secretKey = props.getProperty("AWS_S3_SECRET_KEY");
      region = props.getProperty("AWS_S3_REGION");
      bucket = props.getProperty("AWS_S3_BUCKET");
      System.out.println("AWS 설정 정보가 성공적으로 로드되었습니다.");
    } catch (IOException e) {
      System.out.println("AWS 설정 정보 로드 중 에러 발생: " + e.getMessage());
    }
  }


  //업로드
  public String upload(MultipartFile file) throws IOException {
    //파일 이름 생성
    String originalName = file.getOriginalFilename();
    String uniqueFileName = System.currentTimeMillis() + "_" + originalName;

    //PutObjectRequest 생성
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(uniqueFileName)
        .build();

    //객체 업로드
    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
    System.out.println("파일 업로드 완료. 파일 키: " + uniqueFileName);
    return uniqueFileName;
  }

  //다운로드
  public byte[] download(String fileKey) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(fileKey)
        .build();

    try (ResponseInputStream<GetObjectResponse> objectData = s3Client.getObject(getObjectRequest)) {
      byte[] data = objectData.readAllBytes();
      System.out.println("파일 다운로드 완료. 파일 크기: " + data.length + " 바이트");
      return data;
    } catch (IOException e) {
      throw new RuntimeException("S3에서 파일 다운로드 실패", e);
    }
  }

  //Presigned URL
  public String generatePresignedUrl(String objectKey) {
    // 다운로드 요청 생성 (리전 값을 .env 파일에서 읽은 값으로 설정)
    GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofHours(1))  // URL 유효 시간 1시간
        .getObjectRequest(GetObjectRequest.builder()
            .bucket(bucket)
            .key(objectKey)
            .build())
        .build();

    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(
        getObjectPresignRequest);
    String url = presignedRequest.url().toString();
    System.out.println("Presigned URL 생성 완료: " + url);
    return url;
  }

  // 테스트용 MultipartFile 구현
  private static class TestMultipartFile implements MultipartFile {

    private final String originalFilename;
    private final byte[] content;

    public TestMultipartFile(String originalFilename, byte[] content) {
      this.originalFilename = originalFilename;
      this.content = content;
    }

    @Override
    public String getName() {
      return originalFilename;
    }

    @Override
    public String getOriginalFilename() {
      return originalFilename;
    }

    @Override
    public String getContentType() {
      return "text/plain";
    }

    @Override
    public boolean isEmpty() {
      return content == null || content.length == 0;
    }

    @Override
    public long getSize() {
      return content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
      return content;
    }

    @Override
    public java.io.InputStream getInputStream() throws IOException {
      return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
      java.nio.file.Files.write(dest.toPath(), content);
    }
  }

  public static void main(String[] args) {
    // 1. .env 파일로부터 AWS 정보 로드
    loadProperties();

    // 2. S3Client와 S3Presigner 초기화 (설정 정보를 활용)
    s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)))
        .build();

    s3Presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)))
        .build();

    // 3. S3 API 테스트
    AWSS3Test awsS3Test = new AWSS3Test();
    try {
      String fileKey = awsS3Test.upload(new TestMultipartFile("testFile.txt", "test".getBytes()));

      awsS3Test.download(fileKey);

      awsS3Test.generatePresignedUrl(fileKey);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      // 4. 리소스 해제
      s3Client.close();
      s3Presigner.close();
    }
  }
}
