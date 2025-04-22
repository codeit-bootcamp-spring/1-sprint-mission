package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.exception.storage.FailedToCreateUrl;
import com.sprint.mission.discodeit.exception.storage.FailedToFileConvert;
import com.sprint.mission.discodeit.exception.storage.FileNotFoundS3DirectoryException;
import com.sprint.mission.discodeit.exception.storage.SaveFailedS3Exception;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final String accessKey;
    private final String secretKey;
    private final String region;
    private final String bucket;
    private final int presignedUrlExpiration;

    private final String DIR_ROOT = "test/";
    private final String FILENAME_LINK_CHAR = "_";

    private S3Client s3Client;

    public S3BinaryContentStorage(
            @Value("${discodeit.storage.s3.access-key-id}") String accessKey,
            @Value("${discodeit.storage.s3.secret-access-key}") String secretKey,
            @Value("${discodeit.storage.s3.region}") String region,
            @Value("${discodeit.storage.s3.bucket}") String bucket,
            @Value("${discodeit.storage.s3.presigned-url-expiration}") int presignedUrlExpiration
    ) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
        this.bucket = bucket;
        this.presignedUrlExpiration = presignedUrlExpiration;
    }

    @PostConstruct
    private void init() {
        s3Client = getS3Client();
    }

    @Override
    public UUID put(UUID fileId, MultipartFile file) {
        try {
            File fileObj = convertMultipartFileToFile(file);
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(createKey(fileId, file.getOriginalFilename()))
                            .build(),
                    Paths.get(fileObj.getAbsolutePath()));
            fileObj.delete();
            return fileId;
        } catch (IOException ioe) {
            throw new SaveFailedS3Exception(fileId);
        }
    }

    @Override
    public InputStream get(UUID fileId) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(DIR_ROOT + fileId.toString())
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);
        String key = response.contents().stream()
                .map(S3Object::key)
                .findFirst()
                .orElseThrow(() -> new FileNotFoundS3DirectoryException(fileId));

        return s3Client.getObject(GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
    }

    @Override
    public ResponseEntity<?> download(BinaryContentResponseDto binaryContentResponseDto) {
        try {
            String key = createKey(binaryContentResponseDto.id(), binaryContentResponseDto.fileName());
            String contentType = binaryContentResponseDto.contentType();

            String presignedUrl = generatePresignedUrl(key, contentType);

            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(presignedUrl))
                    .build();
        } catch (Exception e) {
            // todo: exception
            log.warn("Presigned URL 생성 실패: {}", e);
            throw new FailedToCreateUrl(binaryContentResponseDto.fileName());
        }
    }

    private S3Client getS3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    private String generatePresignedUrl(String key, String contentType) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build()) {
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .responseContentType(contentType)
                    .build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
                    .getObjectRequest(objectRequest)
                    .build();
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            log.info("Presigned URL generated → [{}]", presignedRequest.url());
            log.info("Presigned URL method: [{}]", presignedRequest.httpRequest().method());

            return presignedRequest.url().toExternalForm();
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
            return convertFile;
        } catch (IOException e) {
            // todo: exception 생성 필요
            throw new FailedToFileConvert(file.getOriginalFilename());
        }
    }

    private String createKey(UUID fileId, String originalFilename) {
        return DIR_ROOT + fileId.toString() + FILENAME_LINK_CHAR + originalFilename;
    }
}
