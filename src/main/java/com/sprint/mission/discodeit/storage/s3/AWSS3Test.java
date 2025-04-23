package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.config.S3StorageProperties;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AWSS3Test {
  private final S3Client s3Client;

  private final S3StorageProperties properties;

  @PostMapping("/s3/test")
  public String uploadFile(@RequestParam MultipartFile file) throws IOException {
    String originalName = file.getOriginalFilename();
    String uniqueFileName = System.currentTimeMillis() + "_" + originalName;

    // 2) PutObjectRequest 생성
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(properties.getBucket())
        .key(uniqueFileName)
        .build();

    // 3) 객체 업로드
    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

    return uniqueFileName;
  }

  @GetMapping("/s3/test")
  public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) throws IOException {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(properties.getBucket())
        .key(fileName)
        .build();

    ResponseInputStream<GetObjectResponse> objectData = s3Client.getObject(getObjectRequest);
    byte[] bytes = objectData.readAllBytes();
    Resource resource = new ByteArrayResource(bytes);
    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + fileName + "\"")
        .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()))
        .body(resource);
  }
}
