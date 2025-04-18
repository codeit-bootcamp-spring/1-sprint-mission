package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

class S3BinaryContentStorageTest {

  private S3Client s3Client;
  private S3Presigner s3Presigner;
  private S3BinaryContentStorage storage;

  private final String bucketName = "test-bucket";
  private final long presignedUrlExpiration = 600L;

  @BeforeEach
  void setUp() {
    s3Client = mock(S3Client.class);
    s3Presigner = mock(S3Presigner.class);

    storage = new S3BinaryContentStorage(s3Client, s3Presigner);
    setField(storage, "bucketName", bucketName);
    setField(storage, "presignedUrlExpiration", presignedUrlExpiration);
  }

  @Test
  @DisplayName("s3 이미지 put 성공")
  void putTest() {
    // given
    UUID binaryContentId = UUID.randomUUID();
    byte[] sampleData = "sample data".getBytes();

    // when
    when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .thenReturn(PutObjectResponse.builder().build());

    UUID result = storage.put(binaryContentId, sampleData);

    // then
    assertThat(result).isEqualTo(binaryContentId);

    ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
    verify(s3Client).putObject(requestCaptor.capture(), any(RequestBody.class));

    PutObjectRequest capturedRequest = requestCaptor.getValue();
    assertThat(capturedRequest.key()).isEqualTo(binaryContentId.toString());
  }

  @Test
  @DisplayName("s3 이미지 get 성공")
  void getTest() {
    // given
    UUID binaryContentId = UUID.randomUUID();
    byte[] expectedData = "mocked content".getBytes();

    GetObjectRequest expectedRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(binaryContentId.toString())
        .build();

    ResponseBytes<GetObjectResponse> mockResponse = ResponseBytes.fromByteArray(
        GetObjectResponse.builder().build(), expectedData);

    // when
    when(s3Client.getObjectAsBytes(expectedRequest)).thenReturn(mockResponse);

    InputStream inputStream = storage.get(binaryContentId);

    // then
    assertThat(inputStream).isNotNull();
  }

  @Test
  @DisplayName("s3 이미지 download 성공")
  void downloadTest() throws MalformedURLException {
    // given
    UUID id = UUID.randomUUID();
    BinaryContentDto dto = new BinaryContentDto(id, "test.txt", 123L, "text/plain");

    PresignedGetObjectRequest presignedGetObjectRequest = mock(PresignedGetObjectRequest.class);

    // when
    when(presignedGetObjectRequest.url()).thenReturn(new URL("https://example.com/test.txt"));

    when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
        .thenReturn(presignedGetObjectRequest);

    ResponseEntity<?> response = storage.download(dto);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION)).isEqualTo("https://example.com/test.txt");

    verify(s3Presigner).presignGetObject(any(GetObjectPresignRequest.class));
  }

  private static void setField(Object target, String fieldName, Object value) {
    try {
      var field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
