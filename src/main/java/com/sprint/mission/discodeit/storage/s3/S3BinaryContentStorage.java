package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3StorageProperties props;
  private final S3Client client;
  private final S3Presigner presigner;

  public S3BinaryContentStorage(S3StorageProperties props) {
    this.props = props;

    // AWS 자격증명
    AwsBasicCredentials creds = AwsBasicCredentials.create(
        props.accessKey(),
        props.secretKey()
    );

    // S3 클라이언트
    this.client = S3Client.builder()
        .credentialsProvider(StaticCredentialsProvider.create(creds))
        .region(Region.of(props.region()))
        .build();

    // 프리사인 URL 생성기
    this.presigner = S3Presigner.builder()
        .credentialsProvider(StaticCredentialsProvider.create(creds))
        .region(Region.of(props.region()))
        .build();

    log.info("Initialized S3BinaryContentStorage (bucket={}, region={})",
        props.bucket(), props.region());
  }

  @Override
  public UUID put(UUID id, byte[] data) {
    log.debug("S3 업로드 시작: key={} ({} bytes)", id, data.length);
    client.putObject(
        PutObjectRequest.builder()
            .bucket(props.bucket())
            .key(id.toString())
            .build(),
        RequestBody.fromBytes(data)
    );
    log.debug("S3 업로드 완료: key={}", id);
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    log.debug("S3 다운로드 시작: key={}", id);
    InputStream is = client.getObject(
        GetObjectRequest.builder()
            .bucket(props.bucket())
            .key(id.toString())
            .build(),
        ResponseTransformer.toInputStream()
    );
    log.debug("S3 다운로드 완료: key={}", id);
    return is;
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentDto dto) {
    log.debug("S3 프리사인 URL 생성 시작: key={}", dto.id());

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofSeconds(props.presignedUrlExpiration()))
        .getObjectRequest(r -> r
            .bucket(props.bucket())
            .key(dto.id().toString())
            .responseContentType(dto.contentType())
        )
        .build();

    String url = presigner.presignGetObject(presignRequest)
        .url()
        .toString();

    log.debug("S3 프리사인 URL 생성 완료: url={}", url);
    return ResponseEntity.status(HttpStatus.FOUND)
        .header(HttpHeaders.LOCATION, url)
        .build();
  }
}
