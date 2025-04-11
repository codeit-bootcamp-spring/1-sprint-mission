package com.sprint.mission.repository;

import com.sprint.mission.dto.response.BinaryContentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
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

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.*;

@Repository
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final String accessKey;
    private final String secretKey;
    private final String region;
    private final String bucket;
    private final Long presinged_url_expiration;
    private final S3Client s3Client;

    public S3BinaryContentStorage(@Value("${discodeit.storage.s3.access-key}") String accessKey,
                                  @Value("${discodeit.storage.s3.secret-key}") String secretKey,
                                  @Value("${discodeit.storage.s3.region}") String region,
                                  @Value("${discodeit.storage.s3.bucket}") String bucket,
                                  @Value("${discodeit.storage.s3.presigned-url-expiration}") Long presinged_url_expiration) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
        this.bucket = bucket;
        this.presinged_url_expiration = presinged_url_expiration;
        this.s3Client = getS3Client();
    }

    @Override
    public UUID put(UUID id, byte[] content) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .key(id.toString())
                .bucket(bucket).build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
        return id;
    }

    @Override
    public InputStream get(UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> download(BinaryContentDto content) {
        String presignedUrl = generatePresignedUrl(content.id().toString(), content.contentType());
        return ResponseEntity.status(FOUND).header(LOCATION, presignedUrl).build();
    }

    /**
     * 편의 메서드
     */
    private S3Client getS3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    private String generatePresignedUrl(String key, String contentType) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .key(key)
                .responseContentType(contentType)
                .bucket(bucket)
                .build();

        GetObjectPresignRequest getPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(presinged_url_expiration))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(getPresignRequest);
        return presignedRequest.url().toString();
    }
}
