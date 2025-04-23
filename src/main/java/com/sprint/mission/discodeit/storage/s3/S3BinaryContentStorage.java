package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

public class S3BinaryContentStorage implements BinaryContentStorage {

    private final AwsS3Properties props;
    private final S3Client s3Client;
    private final S3Presigner presigner;

    public S3BinaryContentStorage(AwsS3Properties props) {
        this.props = props;

        this.s3Client = S3Client.builder()
                .region(Region.of(props.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(props.getAccessKey(), props.getSecretKey())
                ))
                .build();

        this.presigner = S3Presigner.builder()
                .region(Region.of(props.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(props.getAccessKey(), props.getSecretKey())
                ))
                .build();
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        S3Client s3 = getS3Client();
        String key = binaryContentId.toString();

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(props.getBucket())
                .key(key)
                .build();

        s3.putObject(putRequest, RequestBody.fromBytes(bytes));
        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        S3Client s3 = getS3Client();
        String key = binaryContentId.toString();

        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(props.getBucket())
                .key(key)
                .build();

        ResponseBytes<GetObjectResponse> response = s3.getObjectAsBytes(getRequest);
        return new ByteArrayInputStream(response.asByteArray());
    }

    @Override
    public ResponseEntity<Void> download(BinaryContentDto dto) {
        String key = dto.id().toString();
        String contentType = dto.contentType();
        String fileName = dto.fileName();

        String encodedName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replace("+", "%20");

        String url = generatePresignedUrl(key, contentType);

        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, url)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .build();
    }

    public String generatePresignedUrl(String key, String contentType) {
        S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(props.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(props.getAccessKey(), props.getSecretKey())))
                .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(props.getBucket())
                .key(key)
                .responseContentType(contentType)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(props.getPresignedUrlExpiration()))
                .getObjectRequest(getObjectRequest)
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }

    private S3Client getS3Client() {
        return S3Client.builder()
                .region(Region.of(props.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(props.getAccessKey(), props.getSecretKey())))
                .build();
    }
}
