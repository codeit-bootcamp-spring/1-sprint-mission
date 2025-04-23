package com.sprint.mission.discodeit.storage.s3;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class AWSS3Test {

    private static AwsS3Properties props;
    private static S3Client s3Client;
    private static S3Presigner presigner;

    public static void main(String[] args) {
        try {
            loadProperties();

            s3Client = S3Client.builder()
                    .region(Region.of(props.getRegion()))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(props.getAccessKey(), props.getSecretKey())
                    ))
                    .build();

            presigner = S3Presigner.builder()
                    .region(Region.of(props.getRegion()))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(props.getAccessKey(), props.getSecretKey())
                    ))
                    .build();

            uploadTest();
            downloadTest();
            presignedUrlTest();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadProperties() {
        props = new AwsS3Properties();
        props.setAccessKey(System.getenv("AWS_S3_ACCESS_KEY"));
        props.setSecretKey(System.getenv("AWS_S3_SECRET_KEY"));
        props.setRegion(System.getenv("AWS_S3_REGION"));
        props.setBucket(System.getenv("AWS_S3_BUCKET"));
        props.setPresignedUrlExpiration(600);
    }

    private static void uploadTest() throws IOException {
        String key = "test-upload.txt";
        Path path = Paths.get("test-upload.txt");

        // 파일 없으면 자동 생성 (테스트용)
        if (!Files.exists(path)) {
            Files.writeString(path, "안녕하세요! AWS S3 테스트 파일입니다.");
        }

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(props.getBucket())
                .key(key)
                .build();

        s3Client.putObject(request, RequestBody.fromFile(path));

        System.out.println("✅ 업로드 완료: " + key);
    }

    private static void downloadTest() throws IOException {
        String key = "test-upload.txt";
        String downloadPath = "downloaded.txt";

        Path path = Path.of(downloadPath);
        if (Files.exists(path)) {
            Files.delete(path);
        }

        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(props.getBucket())
                .key(key)
                .build();

        s3Client.getObject(getRequest, Paths.get(downloadPath));
        System.out.println("✅ 다운로드 완료: " + downloadPath);
    }

    private static void presignedUrlTest() {
        String key = "test-upload.txt";

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(props.getBucket())
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofSeconds(props.getPresignedUrlExpiration()))
                .build();

        URL url = presigner.presignGetObject(presignRequest).url();
        System.out.println("✅ Presigned URL: " + url);
    }
}
