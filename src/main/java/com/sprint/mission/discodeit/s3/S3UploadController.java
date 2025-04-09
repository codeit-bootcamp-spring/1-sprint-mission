//package com.sprint.mission.discodeit.s3;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/api/s3")
//@RequiredArgsConstructor
//public class S3UploadController {
//
//  private final S3Service s3Service;
//
//  @PostMapping("/upload")
//  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//    try {
//      String storedFileName = s3Service.upload(file);
//      return ResponseEntity.ok("File Uploaded: " + storedFileName);
//    } catch (Exception e) {
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//          .body("File Upload Failed : " + e.getMessage());
//    }
//  }
//
//  @GetMapping("/download-url")
//  public ResponseEntity<String> getPresignedDownloadUrl(@RequestParam("fileKey") String fileKey) {
//    try {
//      String url = s3Service.generatePresignedUrl(fileKey);
//      return ResponseEntity.ok(url);
//    } catch (Exception e) {
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//          .body("URL 생성 실패: " + e.getMessage());
//    }
//  }
//}
