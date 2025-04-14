package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Component
public class S3BinaryContentStorage implements BinaryContentStorage {

    @Value("${discodeit.storage.s3.access-key}")
    private String accessKey;

    @Value("${discodeit.storage.s3.secret-key}")
    private String secretKey;

    @Value("${discodeit.storage.s3.region}")
    private String region;

    @Value("${discodeit.storage.s3.bucket}")
    private String bucketName;

    @Value("${discodeit.storage.s3.presigned-url-expiration:600}")
    private long presignedUrlExpirationSeconds;

    private S3Client s3Client;
    private S3Presigner s3Presigner;

    @PostConstruct
    public void init() {
        try {
            AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
            Region awsRegion = Region.of(region);

            // S3 클라이언트 초기화
            this.s3Client = S3Client.builder()
                    .region(awsRegion)
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();

            // S3 Presigner 초기화
            this.s3Presigner = S3Presigner.builder()
                    .region(awsRegion)
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();

            // 버킷 존재 확인
            try {
                s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
                log.info("S3 버킷 '{}' 접속 성공", bucketName);
            } catch (NoSuchBucketException e) {
                log.error("S3 버킷 '{}' 존재하지 않음: {}", bucketName, e.getMessage());
                throw new RestApiException(DomainErrorCode.STORAGE_FAILED, "S3 버킷이 존재하지 않습니다");
            } catch (S3Exception e) {
                log.error("S3 버킷 접근 오류: {}", e.getMessage());
                throw new RestApiException(DomainErrorCode.STORAGE_FAILED, "S3 버킷 접근 오류");
            }
        } catch (Exception e) {
            log.error("S3 스토리지 초기화 실패: {}", e.getMessage());
            throw new RestApiException(DomainErrorCode.STORAGE_FAILED, "S3 스토리지 초기화 실패");
        }
    }

    @Override
    public UUID put(UUID id, byte[] data) throws IOException {
        try {
            String key = buildObjectKey(id);
            
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(data));
            log.info("S3에 파일 업로드 완료: {}", key);
            return id;
        } catch (S3Exception e) {
            log.error("S3 파일 업로드 실패: {}", e.getMessage());
            throw new RestApiException(DomainErrorCode.STORAGE_NOT_SAVE, "S3 파일 업로드 실패");
        }
    }

    @Override
    public InputStream get(UUID id) throws IOException {
        try {
            String key = buildObjectKey(id);
            
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            
            ResponseInputStream<GetObjectResponse> s3InputStream = s3Client.getObject(getObjectRequest);
            
            // S3 응답 스트림으로부터 바이트 배열을 읽어와 새 입력 스트림으로 변환
            byte[] bytes = s3InputStream.readAllBytes();
            return new ByteArrayInputStream(bytes);
        } catch (NoSuchKeyException e) {
            log.error("S3에서 파일을 찾을 수 없습니다: {}", id);
            throw new RestApiException(DomainErrorCode.STORAGE_NOT_FOUND, "파일을 찾을 수 없습니다");
        } catch (S3Exception e) {
            log.error("S3 파일 다운로드 실패: {}", e.getMessage());
            throw new RestApiException(DomainErrorCode.STORAGE_NOT_DOWNLOAD, "S3 파일 다운로드 실패");
        }
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto binaryContentDto) throws IOException {
        try {
            UUID id = binaryContentDto.getId();
            String key = buildObjectKey(id);
            
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            
            // Presigned URL 생성
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofSeconds(presignedUrlExpirationSeconds))
                    .getObjectRequest(getObjectRequest)
                    .build();
            
            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            String presignedUrl = presignedRequest.url().toString();
            
            log.info("S3 Presigned URL 생성: {}", presignedUrl);
            
            // 리다이렉트로 처리
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, presignedUrl);
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } catch (Exception e) {
            log.error("S3 Presigned URL 생성 실패: {}", e.getMessage());
            throw new RestApiException(DomainErrorCode.STORAGE_NOT_DOWNLOAD, "S3 파일 다운로드 URL 생성 실패");
        }
    }
    
    private String buildObjectKey(UUID id) {
        // 객체 키 형식: /파일ID
        return id.toString();
    }
} 