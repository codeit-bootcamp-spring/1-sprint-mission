package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.exception.file.FileNotFoundCustomException;
import com.sprint.mission.discodeit.exception.file.FileReadFailedException;
import com.sprint.mission.discodeit.exception.file.FileSaveFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;


@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final String bucket;
    private final S3Client s3Client;
    private final S3Presigner presigner;

    // 환경 변수에서 URL 만료 시간을 문자열로 받아서 Duration으로 변환
    private final Duration expiration;

    /**
     * 생성자에서 S3Client, Presigner 초기화
     */
    public S3BinaryContentStorage(
            @Value("${AWS_S3_ACCESS_KEY}") String accessKey,
            @Value("${AWS_S3_SECRET_KEY}") String secretKey,
            @Value("${AWS_S3_REGION}") String region,
            @Value("${AWS_S3_BUCKET}") String bucket
    ) {
        this.bucket = bucket;

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        Region awsRegion = Region.of(region);

        this.s3Client = S3Client.builder()
                .region(awsRegion)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        this.presigner = S3Presigner.builder()
                .region(awsRegion)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        // URL 만료 시간은 별도 환경 변수에서 받아서 Duration으로 설정
        String expirationStr = System.getenv().getOrDefault("AWS_S3_PRESIGNED_URL_EXPIRATION", "600");
        long seconds = 600;
        try {
            seconds = Long.parseLong(expirationStr);
        } catch (NumberFormatException e) {
            log.warn("잘못된 presigned URL 만료 시간: '{}', 기본값 600초 사용", expirationStr);
        }
        this.expiration = Duration.ofSeconds(seconds);
    }

    /**
     * 파일 업로드
     */
    @Override
    public UUID put(UUID id, byte[] bytes) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(id.toString())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(bytes));
            log.info("S3 업로드 성공 - key: {}, bucket: {}", id, bucket);
            return id;
        } catch (Exception e) {
            log.error("S3 업로드 실패 - key: {}, error: {}", id, e.toString());
            throw new FileSaveFailedException(e.toString());
        }
    }

    /**
     * 파일 다운로드 (InputStream)
     */
    @Override
    public InputStream get(UUID id) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(id.toString())
                    .build();

            return s3Client.getObject(request);
        } catch (NoSuchKeyException e) {
            throw new FileNotFoundCustomException(id.toString());
        } catch (Exception e) {
            throw new FileReadFailedException(e.toString());
        }
    }

    /**
     * Presigned URL을 생성해 리다이렉트
     */
    @Override
    public ResponseEntity<?> download(BinaryContentDto dto) {
        String url = generatePresignedUrl(dto.getId().toString(), dto.getContentType());
        log.info("Presigned URL 생성 완료: {}", url);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, url)
                .build();
    }

    /**
     * Presigned URL 생성 메서드
     */
    public String generatePresignedUrl(String key, String contentType) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .responseContentType(contentType)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getRequest)
                .signatureDuration(expiration)
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }

    public S3Client getS3Client() {
        return this.s3Client;
    }
}