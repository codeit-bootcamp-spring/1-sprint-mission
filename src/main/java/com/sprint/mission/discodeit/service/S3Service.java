package com.sprint.mission.discodeit.service;


import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3Service {

  private final S3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  public S3Service(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  public String upload(MultipartFile file) throws IOException {
    // 1) 파일 이름 생성
    String originalName = file.getOriginalFilename();
    String uniqueFileName = System.currentTimeMillis() + "_" + originalName;

    // 2) PutObjectRequest 생성
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(uniqueFileName)
        // 만약 퍼블릭 읽기 권한을 주고 싶으면 아래처럼 .acl("public-read") 가능 (V4 SDK)
        // .acl("public-read")
        .build();

    // 3) 객체 업로드
    s3Client.putObject(putObjectRequest,
        RequestBody.fromBytes(file.getBytes()));

    return uniqueFileName;
  }
}