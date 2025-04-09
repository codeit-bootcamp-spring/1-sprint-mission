//package com.sprint.mission.discodeit.s3;
//
//import java.io.IOException;
//import java.time.Duration;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import software.amazon.awssdk.core.ResponseInputStream;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.model.GetObjectResponse;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.presigner.S3Presigner;
//import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
//import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
//
//@Service
//@RequiredArgsConstructor
//public class S3Service {
//
//  @Value("${cloud.aws.s3.bucket}")
//  private String bucketName;
//  @Value("${cloud.aws.region.static}")
//  private String region;
//  private final S3Client s3Client;
//
//  public String upload(MultipartFile file) throws IOException {
//
//    String originalName = file.getOriginalFilename();
//    String uniqueFileName = System.currentTimeMillis() + "_" + originalName;
//
//    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//        .bucket(bucketName)
//        .key(uniqueFileName)
//        .build();
//
//    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
//
//    return uniqueFileName;
//  }
//
//  public byte[] download(String fileKey) {
//    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//        .bucket(bucketName)
//        .key(fileKey)
//        .build();
//
//    ResponseInputStream<GetObjectResponse> objectData = s3Client.getObject(getObjectRequest);
//
//    try {
//      return objectData.readAllBytes();
//    } catch (IOException e) {
//      throw new RuntimeException();
//    } finally {
//      try {
//        objectData.close();
//      } catch (IOException e) {
//      }
//    }
//  }
//
//  public String generatePresignedUrl(String objectKey) {
//    try (S3Presigner presigner = S3Presigner.builder()
//        .region(Region.of(region))
//        .build()) {
//
//      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//          .bucket(bucketName)
//          .key(objectKey)
//          .build();
//
//      GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//          .signatureDuration(Duration.ofHours(1))
//          .getObjectRequest(getObjectRequest)
//          .build();
//
//      PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(
//          presignRequest);
//
//      return presignedGetObjectRequest.url().toString();
//    }
//  }
//
//}
