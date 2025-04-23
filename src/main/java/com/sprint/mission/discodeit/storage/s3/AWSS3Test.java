//package com.sprint.mission.discodeit.storage.s3;
//
//import com.sprint.mission.discodeit.config.S3Config;
//import com.sprint.mission.discodeit.exception.ErrorCode;
//import com.sprint.mission.discodeit.exception.binary.AWSException;
//import java.io.IOException;
//import java.net.URI;
//import java.nio.file.Paths;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.Map;
//import lombok.extern.slf4j.Slf4j;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.presigner.S3Presigner;
//import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
//import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
//import software.amazon.awssdk.transfer.s3.S3TransferManager;
//import software.amazon.awssdk.transfer.s3.model.CompletedFileDownload;
//import software.amazon.awssdk.transfer.s3.model.CompletedFileUpload;
//import software.amazon.awssdk.transfer.s3.model.DownloadFileRequest;
//import software.amazon.awssdk.transfer.s3.model.FileDownload;
//import software.amazon.awssdk.transfer.s3.model.FileUpload;
//import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
//
//@Slf4j
//public class AWSS3Test {
//
//  // 업로드
//  public String uploadFile(S3TransferManager transferManager, String bucketName, String key,
//      URI filePathURI) {
//
//    UploadFileRequest request = UploadFileRequest.builder()
//        .putObjectRequest(b -> b.bucket(bucketName).key(key))
//        .source(Paths.get(filePathURI))
//        .build();
//
//    FileUpload fileUpload = transferManager.uploadFile(request);
//    CompletedFileUpload uploadResult = fileUpload.completionFuture().join();
//
//    return uploadResult.response().eTag();
//  }
//
//  // 다운로드
//  public Long downloadFile(S3TransferManager transferManager, String bucketName, String key,
//      String downloadFileWithPath) {
//
//    DownloadFileRequest downloadFileRequest = DownloadFileRequest.builder()
//        .getObjectRequest(b -> b.bucket(bucketName).key(key))
//        .destination(Paths.get(downloadFileWithPath))
//        .build();
//
//    FileDownload fileDownload = transferManager.downloadFile(downloadFileRequest);
//    CompletedFileDownload downloadResult = fileDownload.completionFuture().join();
//    log.info("다운로드 성공, 파일크기: {}", downloadResult.response().contentLength());
//    return downloadResult.response().contentLength();
//  }
//
//  // PresignedUrl URL
//  public String createPresignedURL(String bucketName, String keyName) {
//    try (S3Presigner presigner = S3Presigner.create()) {
//      GetObjectRequest objectRequest = GetObjectRequest.builder()
//          .bucket(bucketName)
//          .key(keyName)
//          .build();
//
//      GetObjectPresignRequest presignedRequest = GetObjectPresignRequest.builder()
//          .signatureDuration(Duration.ofMinutes(10)) // 10분동안 유효
//          .getObjectRequest(objectRequest)
//          .build();
//
//      PresignedGetObjectRequest presignedResult = presigner.presignGetObject(presignedRequest);
//      log.info("Presigned URL: [{}]", presignedResult.url().toString());
//      log.info("HTTP method: [{}]", presignedResult.httpRequest().method());
//
//      return presignedResult.url().toExternalForm();
//    } catch (Exception e) {
//      log.error("Presigned URL 생성 중 에러 발생 {}", e.getMessage());
//      throw new AWSException(Instant.now(), ErrorCode.AWS_ERROR,
//          Map.of(keyName, ErrorCode.AWS_ERROR.getMessage()));
//    }
//  }
//
//  private static void download() throws IOException {
//    PropertiesUtils properties = new PropertiesUtils();
//    String bucket = properties.getBucket();
//    String key = "test-file.txt";
//    String downloadFileWithPath = "/Users/wongil/Desktop/1-sprint-mission/.discodeit/storage/test-file.txt";
//
//    S3Config config = new S3Config();
//    S3TransferManager transferManager = config.transferManager(config.s3AsyncClient());
//
//    AWSS3Test awss3Test = new AWSS3Test();
//    Long fileSize = awss3Test.downloadFile(transferManager, bucket, key, downloadFileWithPath);
//
//    log.info("파일 다운로드 완료 {}", fileSize);
//
//    transferManager.close();
//  }
//
//  private static void upload() throws IOException {
//    PropertiesUtils properties = new PropertiesUtils();
//    String bucket = properties.getBucket();
//    String key = "test-file.txt";
//    String filePath = "/Users/wongil/Desktop/1-sprint-mission/.discodeit/storage/4d6496bf-d0c8-4df8-9cb2-3aa6a9d82eae";
//
//    S3Config config = new S3Config();
//    S3TransferManager transferManager = config.transferManager(config.s3AsyncClient());
//
//    AWSS3Test awss3Test = new AWSS3Test();
//    awss3Test.uploadFile(transferManager, bucket, key, URI.create("file://" + filePath));
//
//    System.out.println("awss3Test = " + awss3Test);
//    transferManager.close();
//  }
//}