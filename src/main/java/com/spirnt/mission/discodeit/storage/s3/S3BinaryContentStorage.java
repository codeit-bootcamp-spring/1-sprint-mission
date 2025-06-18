package com.spirnt.mission.discodeit.storage.s3;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.presigner.PresignRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final String accessKey;
    private final String secretKey;
    private final String region;
    private final String bucket;
    private final String presignedUrlExpiration;


    public S3BinaryContentStorage(String accessKey, String secretKey, String region, String bucket,
        String presignedUrlExpiration) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
        this.bucket = bucket;
        this.presignedUrlExpiration = presignedUrlExpiration;
    }

    @Override
    public CompletableFuture<UUID> put(UUID binaryContentId, byte[] bytes) {
        // 파일 이름 생성
        String fileName = String.valueOf(binaryContentId);
        // PutObjectRequest 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .contentType("application/octet-stream")
            .contentLength((long) bytes.length)
            .key(fileName)
            .build();
        // 업로드
        try {
            getS3Client().putObject(putObjectRequest, RequestBody.fromBytes(bytes));
        } catch (Exception e) {
            throw e;
        }
        return CompletableFuture.completedFuture(binaryContentId);
    }


    @Override
    public InputStream get(UUID binaryContentId) {
        S3Client s3Client = getS3Client();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(binaryContentId.toString())
            .build();

        return s3Client.getObject(getObjectRequest);
    }

    @Override
    public ResponseEntity<Void> download(BinaryContentDto binaryContentDto) {
        String url = generatePresignedUrl(String.valueOf(binaryContentDto.getId()),
            binaryContentDto.getContentType());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));   //presignedUrl로 리다이렉트

        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 리다이렉트 상태 코드
    }


    // S3 clinet 객체 얻기
    public S3Client getS3Client() {
        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .build();
    }

    // S3에 잠시 접근할 수 있는 임시 서명된 URL
    public String generatePresignedUrl(String key, String contentType) {
        S3Presigner presigner = S3Presigner.builder()
            .region(Region.of(region))
            .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .responseContentType(contentType)
            .build();

        PresignRequest presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(Long.parseLong(presignedUrlExpiration)))
            .getObjectRequest(getObjectRequest)
            .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(
            (GetObjectPresignRequest) presignRequest);

        return presignedGetObjectRequest.url().toString();

    }
}
