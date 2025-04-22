package com.sprint.mission.discodeit.storage.s3;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;

import static org.aspectj.bridge.MessageUtil.fail;

@Tag("db")
@ActiveProfiles("test")
public class AwsS3Test {

    private static final Logger log = LoggerFactory.getLogger(AwsS3Test.class);

    private static S3Client s3Client;
    private static String bucketName;
    private static Region regionName;
    private static AwsBasicCredentials awsBasicCredentials;

    @BeforeAll
    static void init() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(Paths.get(".env"))) {
            properties.load(inputStream);
        }

        bucketName = properties.getProperty("AWS_S3_BUCKET");
        regionName = Region.of(properties.getProperty("AWS_S3_REGION"));

        awsBasicCredentials = AwsBasicCredentials.create(
                properties.getProperty("AWS_S3_ACCESS_KEY"),
                properties.getProperty("AWS_S3_SECRET_KEY")
        );

        s3Client = S3Client.builder()
                .region(regionName)
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();

    }

    @Test
    @DisplayName("AWS S3에 간단한 텍스트 파일을 성공적으로 업로드한다.")
    void awsS3UploadTest() {
        // given
        String filename = "test/upload_test.txt";
        File file = new File("upload_test.txt");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("Hello S3 upload TEST!".getBytes());
        } catch (IOException e) {
            fail("파일 생성 중 오류 발생: " + e.getMessage());
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        // when & then
        Assertions.assertDoesNotThrow(() -> {
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
        });

        file.delete();
    }

    @Test
    @DisplayName("AWS S3에 존재하는 파일을 성공적으로 다운로드 한다.")
    void awsS3DownloadTest() {
        // given
        String filename = "test/upload_test.txt";
        String downloadPath = "temp/download_test.txt";

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        // when & then
        Assertions.assertDoesNotThrow(() -> {
            s3Client.getObject(getObjectRequest, Paths.get(downloadPath));
        });

        File downloadFile = new File(downloadPath);
        Assertions.assertTrue(downloadFile.exists(), "파일이 성공적으로 다운로드되지 않았습니다.");
        downloadFile.delete();
    }

    @Test
    @DisplayName("AWS S3의 Presigned Url을 성공적으로 생성할 수 있다.")
    void awsS3PresignedUrlCreate() {
        // given
        String filename = "test/upload_test.txt";
        try (S3Presigner presigner = S3Presigner.builder()
                .region(regionName)
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build()
        ) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))   // presignedURL 10분간 접근 허용
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = presigner
                    .presignGetObject(getObjectPresignRequest);

            String presignedUrl = presignedGetObjectRequest.url().toString();

            // when & then
            Assertions.assertNotNull(presignedUrl);
            Assertions.assertTrue(presignedUrl.contains("https://"), "Presigned URL이 올바르게 생성되지 않았습니다.");
            log.info("[AWS S3 TEST] Create Presigned URL: {}", presignedUrl);
        }
    }

}
