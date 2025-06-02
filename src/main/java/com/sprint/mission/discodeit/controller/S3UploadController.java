package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.S3Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/s3")
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3UploadController {

  private final S3Service s3Service;

  public S3UploadController(S3Service s3Service) {
    this.s3Service = s3Service;
  }

  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      String storedFileName = s3Service.upload(file);
      return ResponseEntity.ok("File uploaded successfully: " + storedFileName);
    } catch (Exception e) {
      // 예외 처리 (로그 찍기 등)
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("File upload failed: " + e.getMessage());
    }
  }
}
