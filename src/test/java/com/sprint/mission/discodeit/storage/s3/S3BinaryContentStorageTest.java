package com.sprint.mission.discodeit.storage.s3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
class S3BinaryContentStorageTest {

  @Mock
  private S3Client s3Client;

  @Mock
  private ResponseInputStream<GetObjectResponse> responseInputStream;

  private S3BinaryContentStorage s3BinaryContentStorage;

  @BeforeEach
  void setUp() {
    s3BinaryContentStorage = spy(new S3BinaryContentStorage(
        "test-access-key",
        "test-secret-key",
        "ap-northeast-2",
        "test-bucket"
    ));
  }

  @Test
  void put_S3로_업로드후_id_반환() {
    // Given
    UUID id = UUID.randomUUID();
    byte[] content = "Test content".getBytes();
    doReturn(s3Client).when(s3BinaryContentStorage).getS3Client();

    // When
    UUID result = s3BinaryContentStorage.put(id, content);

    // Then
    verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    assertEquals(id, result);
  }

  @Test
  void get_InputStream_반환() throws IOException {
    // Given
    UUID id = UUID.randomUUID();
    byte[] mockData = "Test data".getBytes();
    doReturn(s3Client).when(s3BinaryContentStorage).getS3Client();
    when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(responseInputStream);
    when(responseInputStream.readAllBytes()).thenReturn(mockData);

    // When
    InputStream result = s3BinaryContentStorage.get(id);

    // Then
    verify(s3Client).getObject(any(GetObjectRequest.class));
    assertArrayEquals(mockData, result.readAllBytes());
  }

  @Test
  void download_PresignedUrl로_리다이렉트_응답_반환() {
    // Given
    UUID id = UUID.randomUUID();
    String fileName = "test.txt";
    long size = 100L;
    String contentType = "text/plain";
    BinaryContentDto metaData = new BinaryContentDto(id, fileName, size, contentType);

    doReturn("http://discodeit.com/test").when(s3BinaryContentStorage)
        .generatePresignedUrl(id.toString(), contentType);

    // When
    ResponseEntity<?> response = s3BinaryContentStorage.download(metaData);

    // Then
    assertEquals(HttpStatus.FOUND, response.getStatusCode());
    assertEquals("http://discodeit.com/test",
        response.getHeaders().getFirst(HttpHeaders.LOCATION));
    verify(s3BinaryContentStorage).generatePresignedUrl(id.toString(), contentType);
  }
}
