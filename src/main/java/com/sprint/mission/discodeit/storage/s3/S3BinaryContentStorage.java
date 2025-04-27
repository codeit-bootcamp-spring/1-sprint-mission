package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.storage.s3.BinaryContentStorage; // 심볼 'BinaryContentStorage'를 해결할 수 없습니다
import com.sprint.mission.discodeit.storage.s3.BinaryContentDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@Service
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Client s3Client;
    private final S3Presigner presigner;
    private final String bucket;
    private final Duration presignExpiration;

    public S3BinaryContentStorage(AWSS3Properties props,
                                  S3Client s3Client,
                                  S3Presigner presigner) {
        this.s3Client = s3Client;
        this.presigner = presigner;
        this.bucket = props.getBucket();
        // presigned-url-expiration in seconds, defaulted in props
        this.presignExpiration = Duration.ofSeconds(props.getPresignedUrlExpiration());
    }

    @Override
    public UUID put(UUID id, byte[] content) {
        String key = id.toString();
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build(),
                RequestBody.fromBytes(content)
        );
        return id;
    }

    @Override
    public InputStream get(UUID id) {
        ResponseBytes<GetObjectResponse> bytes = s3Client.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(id.toString())
                        .build()
        );
        return new ByteArrayInputStream(bytes.asByteArray());
    }

    @Override
    public ResponseEntity<Void> download(BinaryContentDto dto) {
        String key = dto.getId().toString();
        String presignedUrl = generatePresignedUrl(key);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(presignedUrl))
                .build();
    }

    /**
     * 생성된 Presigned GET URL 을 반환합니다.
     */
    public String generatePresignedUrl(String key) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(presignExpiration)
                .getObjectRequest(GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build())
                .build();

        return presigner.presignGetObject(presignRequest)
                .url()
                .toString();
    }
}
