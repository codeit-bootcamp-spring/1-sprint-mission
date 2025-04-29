package com.spring.mission.discodeit.storage.s3;


import java.io.IOException;
import java.util.Properties;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class AWSS3Test {

  private static AmazonS3 s3Client;
  private static String bucketName;

  static {
    try {
      // 1. Load AWS credentials from .env
      Properties props = new Properties();
      props.load(new FileInputStream(".env")); // 파일 경로는 프로젝트 루트 기준

      String accessKey = props.getProperty("AWS_ACCESS_KEY_ID");
      String secretKey = props.getProperty("AWS_SECRET_ACCESS_KEY");
      String region = props.getProperty("AWS_REGION");
      bucketName = props.getProperty("AWS_BUCKET_NAME");

      BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
      s3Client = AmazonS3ClientBuilder.standard()
          .withRegion(region)
          .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
          .build();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // ✅ 1. Upload test
  public static void testUpload() {
    try {
      String keyName = "test-upload.txt";
      File file = new File("sample.txt"); // 업로드할 테스트 파일

      if (!file.exists()) {
        Files.write(file.toPath(), "This is a test file.".getBytes()); // 테스트용 파일 생성
      }

      s3Client.putObject(new PutObjectRequest(bucketName, keyName, file));
      System.out.println("✅ Upload complete.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // ✅ 2. Download test
  public static void testDownload() {
    try {
      String keyName = "test-upload.txt";
      S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, keyName));
      InputStream input = object.getObjectContent();

      FileOutputStream output = new FileOutputStream("downloaded.txt");
      byte[] read_buf = new byte[1024];
      int read_len;
      while ((read_len = input.read(read_buf)) > 0) {
        output.write(read_buf, 0, read_len);
      }
      input.close();
      output.close();

      System.out.println("✅ Download complete.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // ✅ 3. Generate Presigned URL test
  public static void testPresignedUrl() {
    try {
      String keyName = "test-upload.txt";
      Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 10); // 10분 유효

      GeneratePresignedUrlRequest generatePresignedUrlRequest =
          new GeneratePresignedUrlRequest(bucketName, keyName)
              .withMethod(HttpMethod.GET)
              .withExpiration(expiration);

      URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
      System.out.println("✅ Presigned URL: " + url.toString());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // ✅ Main method for running tests
  public static void main(String[] args) {
    System.out.println("------ AWS S3 TEST ------");
    testUpload();
    testDownload();
    testPresignedUrl();
  }
}