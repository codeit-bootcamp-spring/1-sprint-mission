package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3BinaryContentStorageTest {

  @Mock
  private S3Client s3Client;

  @Mock
  private ResponseInputStream<GetObjectResponse> responseInputStream;

  private S3BinaryContentStorage s3Storage;
  private final String bucket = "test-bucket";

  @BeforeEach
  void setUp() {
    // S3 클라이언트를 모킹하여 실제 AWS에 연결하지 않도록 함
    s3Storage = new S3BinaryContentStorage("test-access-key", "test-secret-key", "us-east-1");
    ReflectionTestUtils.setField(s3Storage, "s3Client", s3Client);
    ReflectionTestUtils.setField(s3Storage, "bucket", bucket);
  }

  @Test
  void init_bucketExists_shouldNotCreateBucket() {
    // 버킷이 존재하는 경우 테스트
    when(s3Client.headBucket(any(HeadBucketRequest.class))).thenReturn(HeadBucketResponse.builder().build());

    s3Storage.init();

    verify(s3Client, times(1)).headBucket(any(HeadBucketRequest.class));
    verify(s3Client, never()).createBucket(any(CreateBucketRequest.class));
  }

  @Test
  void init_bucketNotExists_shouldCreateBucket() {
    // 버킷이 존재하지 않는 경우 테스트
    when(s3Client.headBucket(any(HeadBucketRequest.class))).thenThrow(NoSuchBucketException.class);
    when(s3Client.createBucket(any(CreateBucketRequest.class))).thenReturn(CreateBucketResponse.builder().build());

    s3Storage.init();

    verify(s3Client, times(1)).headBucket(any(HeadBucketRequest.class));
    verify(s3Client, times(1)).createBucket(any(CreateBucketRequest.class));
  }

  @Test
  void put_shouldUploadToS3AndReturnId() {
    // given
    UUID binaryContentId = UUID.randomUUID();
    byte[] testData = "test content".getBytes();
    when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .thenReturn(PutObjectResponse.builder().build());

    // when
    UUID result = s3Storage.put(binaryContentId, testData);

    // then
    assertEquals(binaryContentId, result);

    ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
    ArgumentCaptor<RequestBody> bodyCaptor = ArgumentCaptor.forClass(RequestBody.class);

    verify(s3Client).putObject(requestCaptor.capture(), bodyCaptor.capture());
    assertEquals(bucket, requestCaptor.getValue().bucket());
    assertEquals(binaryContentId.toString(), requestCaptor.getValue().key());
  }

  @Test
  void get_objectExists_shouldReturnInputStream() {
    // given
    UUID binaryContentId = UUID.randomUUID();
    when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(responseInputStream);

    // when
    InputStream result = s3Storage.get(binaryContentId);

    // then
    assertNotNull(result);
    assertEquals(responseInputStream, result);

    ArgumentCaptor<GetObjectRequest> requestCaptor = ArgumentCaptor.forClass(GetObjectRequest.class);
    verify(s3Client).getObject(requestCaptor.capture());
    assertEquals(bucket, requestCaptor.getValue().bucket());
    assertEquals(binaryContentId.toString(), requestCaptor.getValue().key());
  }

  @Test
  void get_objectNotExists_shouldThrowNoSuchElementException() {
    // given
    UUID binaryContentId = UUID.randomUUID();
    when(s3Client.getObject(any(GetObjectRequest.class))).thenThrow(NoSuchKeyException.class);

    // when, then
    NoSuchElementException exception = assertThrows(
        NoSuchElementException.class,
        () -> s3Storage.get(binaryContentId)
    );
    assertTrue(exception.getMessage().contains(binaryContentId.toString()));
  }

  @Test
  void download_shouldReturnResponseEntity() {
    // given
    UUID binaryContentId = UUID.randomUUID();
    String fileName = "test-file.txt";
    Long fileSize = 100L;
    String contentType = "text/plain";

    BinaryContentDto metaData = new BinaryContentDto(binaryContentId, fileName, fileSize, contentType);

    when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(responseInputStream);

    // when
    ResponseEntity<Resource> response = s3Storage.download(metaData);

    // then
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    // 헤더 검증
    assertEquals("attachment; filename=\"" + fileName + "\"",
        response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    assertEquals(contentType, response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
    assertEquals(String.valueOf(fileSize), response.getHeaders().getFirst(HttpHeaders.CONTENT_LENGTH));
  }
}