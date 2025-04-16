package com.sprint.mission.repository.binary;
import com.sprint.mission.config.S3ConfigProperties;
import com.sprint.mission.dto.response.BinaryContentDto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
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
@RequiredArgsConstructor
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Client s3Client;
    private final S3ConfigProperties s3Properties;

    @Override
    public UUID put(UUID id, byte[] content) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .key(id.toString())
                .bucket(s3Properties.bucket()).build();

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
        return S3Client.builder()
                .region(Region.of(s3Properties.region()))
                .credentialsProvider(generateCredentialsProvider())
                .build();
    }

    private String generatePresignedUrl(String key, String contentType) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .key(key)
                .responseContentType(contentType)
                .bucket(s3Properties.bucket())
                .build();

        GetObjectPresignRequest getPresignedRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(s3Properties.presignedUrlExpiration()))
                .getObjectRequest(getObjectRequest)
                .build();

        S3Presigner s3Presigner = generateS3Presigner();
        PresignedGetObjectRequest s3PresignedRequest = s3Presigner.presignGetObject(getPresignedRequest);
        return s3PresignedRequest.url().toString();
    }

    private S3Presigner generateS3Presigner(){
        return S3Presigner.builder()
                .region(Region.of(s3Properties.region()))
                .credentialsProvider(generateCredentialsProvider())
                .build();
    }

    private StaticCredentialsProvider generateCredentialsProvider() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(s3Properties.accessKey(), s3Properties.secretKey());
        return StaticCredentialsProvider.create(credentials);
    }
}
