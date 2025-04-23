package com.sprint.mission.discodeit.storage.s3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.exception.binary.AWSException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.http.SdkHttpRequest;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedFileUpload;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;

@ExtendWith(MockitoExtension.class)
class S3BinaryContentStorageTest {

  @Mock
  private S3AsyncClient s3AsyncClient;

  @Mock
  private S3TransferManager s3TransferManager;

  private S3BinaryContentStorage S3storage;

  @BeforeEach
  void setUp() {
    // 직접 의존성을 주입하여 인스턴스 생성
    S3storage = new S3BinaryContentStorage(s3AsyncClient, s3TransferManager);

    ReflectionTestUtils.setField(S3storage, "accessKey", "test-access-key");
    ReflectionTestUtils.setField(S3storage, "secretKey", "test-secret-key");
    ReflectionTestUtils.setField(S3storage, "region", "ap-northeast-2");
    ReflectionTestUtils.setField(S3storage, "bucket", "test-bucket");
  }


  @Test
  @DisplayName("다운로드 성공")
  void download() throws Exception {
    UUID fileId = UUID.randomUUID();
    String contentType = "application/pdf";
    BinaryContentDto file = new BinaryContentDto(fileId, "test-file.pdf", 1024L, contentType);
    String presignedUrl = "https://test-bucket.s3.amazonaws.com/" + fileId;

    try (MockedStatic<PropertiesUtils> utils = Mockito.mockStatic(PropertiesUtils.class)) {

      utils.when(PropertiesUtils::getBucket).thenReturn("test-bucket");

      S3Presigner mockPresigner = mock(S3Presigner.class);
      PresignedGetObjectRequest mockPresignedGetObjectRequest = mock(
          PresignedGetObjectRequest.class);
      SdkHttpRequest mockHttpRequest = mock(SdkHttpRequest.class);
      URL mockUrl = new URL(presignedUrl);

      try (MockedStatic<S3Presigner> presignerMockedStatic = Mockito.mockStatic(
          S3Presigner.class)) {

        presignerMockedStatic.when(S3Presigner::create).thenReturn(mockPresigner);

        when(mockPresigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(
            mockPresignedGetObjectRequest);
        when(mockPresignedGetObjectRequest.url()).thenReturn(mockUrl);
        when(mockPresignedGetObjectRequest.httpRequest()).thenReturn(mockHttpRequest);
        when(mockHttpRequest.method()).thenReturn(SdkHttpMethod.GET);

        ResponseEntity<?> response = S3storage.download(file);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(URI.create(presignedUrl), response.getHeaders().getLocation());
      }
    }
  }

  @Test
  @DisplayName("다운로드 실패")
  void downloadFail() throws Exception {
    UUID fileId = UUID.randomUUID();
    String contentType = "application/pdf";
    BinaryContentDto file = new BinaryContentDto(fileId, "test-file.pdf", 1024L, contentType);

    try (MockedStatic<PropertiesUtils> utils = mockStatic(PropertiesUtils.class)) {

      utils.when(PropertiesUtils::getBucket).thenReturn("test-bucket");

      try (MockedStatic<S3Presigner> presignerMockedStatic = mockStatic(S3Presigner.class)) {

        presignerMockedStatic.when(S3Presigner::create).thenThrow(new RuntimeException());

        assertThrows(AWSException.class, () -> S3storage.download(file));
      }
    }
  }

  @Test
  @DisplayName("파일 업로드 성공")
  void testPut_Success() throws IOException {
    UUID fileId = UUID.randomUUID();
    byte[] fileContent = "test".getBytes();
    String bucketName = "test-bucket";

    Path mockPath = mock(Path.class);
    FileUpload mockFileUpload = mock(FileUpload.class);
    CompletedFileUpload mockCompletedFileUpload = mock(CompletedFileUpload.class);
    PutObjectResponse mockPutResponse = mock(PutObjectResponse.class);

    try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class);
        MockedStatic<PropertiesUtils> utils = Mockito.mockStatic(PropertiesUtils.class)) {

      filesMock.when(() -> Files.createTempFile(any(String.class), any(String.class)))
          .thenReturn(mockPath);
      filesMock.when(() -> Files.write(any(Path.class), any(byte[].class)))
          .thenReturn(mockPath);
      filesMock.when(() -> Files.delete(any(Path.class)))
          .thenAnswer(invocation -> null);
      utils.when(PropertiesUtils::getBucket)
          .thenReturn(bucketName);

      when(s3TransferManager.uploadFile(any(UploadFileRequest.class)))
          .thenReturn(mockFileUpload);
      when(mockFileUpload.completionFuture())
          .thenReturn(
              CompletableFuture.completedFuture(mockCompletedFileUpload));
      when(mockCompletedFileUpload.response())
          .thenReturn(mockPutResponse);
      when(mockPutResponse.eTag())
          .thenReturn("\"test\"");

      // When
      UUID result = S3storage.put(fileId, fileContent);

      // Then
      assertEquals(fileId, result);
      verify(s3TransferManager).uploadFile(any(UploadFileRequest.class));
    }
  }

  @Test
  @DisplayName("파일 업로드 실패")
  void testPut_Failure() throws IOException {
    UUID fileId = UUID.randomUUID();
    byte[] fileContent = "test".getBytes();

    try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class);
        MockedStatic<PropertiesUtils> utils = Mockito.mockStatic(PropertiesUtils.class)) {

      filesMock.when(() -> Files.createTempFile(any(String.class), any(String.class)))
          .thenThrow(new IOException("File error"));
      utils.when(PropertiesUtils::getBucket).thenReturn("test-bucket");

      // When & Then
      assertThrows(AWSException.class, () -> S3storage.put(fileId, fileContent));
    }
  }
}