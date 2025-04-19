package com.sprint.mission.discodeit.storage.s3;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AWSS3Test {

    // S3 버킷 이름
    static String bucket;

    // S3 파일 업로드/다운로드용 클라이언트
    static S3Client s3Client;

    // Presigned URL 생성을 위한 객체
    static S3Presigner presigner;

    @BeforeAll
    static void setUp() throws Exception {

        Path envPath = Paths.get(".env");

        // .env 파일이 없으면 테스트 전체 스킵
        if (!Files.exists(envPath)) {
            System.out.println("env 파일이 없어 S3 테스트를 건너뜁니다.");
            Assumptions.assumeTrue(false); // JUnit 5의 테스트 스킵
            return;
        }

        // .env 파일을 읽어와 AWS 자격 정보 및 설정을 로드
        Properties props = new Properties();
        props.load(Files.newBufferedReader(Paths.get(".env")));

        String accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
        String secretKey = props.getProperty("AWS_S3_SECRET_KEY");
        String region = props.getProperty("AWS_S3_REGION");
        bucket = props.getProperty("AWS_S3_BUCKET");

        // AWS SDK에서 사용할 자격증명 객체 생성
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        // 리전을 객체로 변환 (예: ap-northeast-2)
        Region awsRegion = Region.of(region);

        // S3 클라이언트 초기화 (파일 업로드/다운로드 등에 사용)
        s3Client = S3Client.builder()
                .region(awsRegion)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        // Presigned URL 생성을 위한 전용 클라이언트
        presigner = S3Presigner.builder()
                .region(awsRegion)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Test
    void uploadTest() {
        // S3에 저장할 파일의 키(Key)를 랜덤 UUID로 생성
        String key = "test/" + UUID.randomUUID() + ".txt";

        // 실제 저장할 문자열 내용
        String content = "Hello AWS S3!";

        // 파일 업로드 요청 객체 생성
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)          // 저장할 버킷 이름
                .key(key)               // 저장할 파일 키 (파일명처럼 사용됨)
                .contentType("text/plain") // MIME 타입 설정
                .build();

        // 파일 업로드 실행
        s3Client.putObject(putRequest, RequestBody.fromString(content));

        // 콘솔 출력
        System.out.println("업로드 완료 - key: " + key);
    }

    @Test
    void downloadTest() throws Exception {
        // 테스트용 업로드 파일 생성
        String key = "test/" + UUID.randomUUID() + ".txt";
        String content = "File uploaded before download";

        // 먼저 업로드를 수행
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("text/plain")
                .build();

        s3Client.putObject(putRequest, RequestBody.fromString(content));

        // 다운로드 요청 객체 생성
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        // 다운로드 실행 → InputStream으로 반환
        InputStream inputStream = s3Client.getObject(getRequest);

        // 읽은 내용을 문자열로 변환
        String result = new String(inputStream.readAllBytes());

        // 결과 출력 및 검증
        System.out.println("다운로드 완료 - 내용: " + result);
        assertThat(result).isEqualTo(content);
    }

    @Test
    void presignedUrlTest() {
        // 미리 존재하는 S3 객체의 key
        String key = "test/example.txt"; // 해당 경로의 파일이 S3에 실제로 있어야 함

        // 다운로드 요청을 생성
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        // Presigned URL 생성 요청 생성 (5분간 유효)
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(getRequest)
                .build();

        // URL 생성
        String url = presigner.presignGetObject(presignRequest).url().toString();

        // 출력
        System.out.println("Presigned URL: " + url);
    }
}