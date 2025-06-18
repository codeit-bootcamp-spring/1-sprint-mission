package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    @Value("${discodeit.storage.s3.access-key}")
    private String accessKey;

    @Value("${discodeit.storage.s3.secret-key}")
    private String secretKey;

    @Value("${discodeit.storage.s3.bucket}")
    private String bucket;

    @Value("${discodeit.storage.s3.region}")
    private String region;

    @Value("${discodeit.storage.s3.presigned-url-expiration:}")
    private int presignedUrlExpiration;

    @Override
    public CompletableFuture<UUID> putAsync(UUID id, byte[] content) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(id.toString())
            .build();

        S3Client s3Client = getS3Client();
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));

        return CompletableFuture.completedFuture(id);
    }

    @Override
    public void put(UUID id, byte[] content) throws InterruptedException {

    }

    @Override
    public InputStream get(UUID id) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(id.toString())
            .build();

        S3Client s3Client = getS3Client();
        return s3Client.getObject(getObjectRequest);
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
        String presignedUrl =
            generatePresignedUrl(binaryContentDto.id().toString(), binaryContentDto.contentType());
        return ResponseEntity.status(HttpStatusCode.valueOf(302))
            .body(presignedUrl);
    }

    public S3Client getS3Client() {
        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        accessKey,
                        secretKey
                    )
                )
            ).build();
    }

    public String generatePresignedUrl(String key, String contentType) {
        S3Presigner s3Presigner = S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        accessKey,
                        secretKey
                    )
                )
            ).build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
            .getObjectRequest(getObjectRequest)
            .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
