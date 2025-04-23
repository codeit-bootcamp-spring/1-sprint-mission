package com.sprint.mission.discodeit.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.sprint.mission.discodeit.config.S3StorageProperties;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ExtendWith(MockitoExtension.class)
class S3BinaryContentStorageTest {

  @Mock
  S3Client s3Client;
  @Mock
  S3Presigner s3Presigner;
  @Spy
  S3StorageProperties properties;

  @InjectMocks
  S3BinaryContentStorage storage;

  @BeforeEach
  void setUp() {
    properties.setAccessKey("accessKey");
    properties.setSecretKey("secretKey");
    properties.setBucket("bucket");
    properties.setRegion("us-east-1");
    properties.setPresignedUrlExpiration(600);
  }

  @Test
  @DisplayName("put")
  void put() {
    UUID id = UUID.randomUUID();
    byte[] bytes = "test.txt".getBytes();

    UUID responseId = storage.put(id, bytes);
    ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);

    then(s3Client).should().putObject(requestCaptor.capture(), any(RequestBody.class));
    then(s3Client).should().putObject(any(PutObjectRequest.class), any(RequestBody.class));

    PutObjectRequest putObjectRequest  = requestCaptor.getValue();
    assertThat(putObjectRequest.bucket()).isEqualTo(properties.getBucket());
    assertThat(putObjectRequest.key()).isEqualTo(id.toString());
  }

  @Test
  @DisplayName("get")
  void get() throws IOException {
    UUID id = UUID.randomUUID();
    byte[] bytes = "test.txt".getBytes();
    ResponseInputStream<GetObjectResponse> responseInputStream = mock(ResponseInputStream.class);
    given(s3Client.getObject(any(GetObjectRequest.class))).willReturn(responseInputStream);

    InputStream inputStream = storage.get(id);

    then(s3Client).should().getObject(any(GetObjectRequest.class));
    assertThat(inputStream).isNotNull();
  }

  @Test
  @DisplayName("download")
  void download() throws MalformedURLException, URISyntaxException {
    BinaryContentResponse binaryContentResponse = new BinaryContentResponse(
        UUID.randomUUID(),
        1024L,
        "test.jpg",
        "image/jpeg"
    );
    PresignedGetObjectRequest presignedGetObjectRequest = mock(PresignedGetObjectRequest.class);
    URL url = URI.create("http://localhost:8080/test/1").toURL();
    given(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).willReturn(presignedGetObjectRequest);
    given(presignedGetObjectRequest.url()).willReturn(url);

    ResponseEntity<Resource> responseEntity = storage.download(binaryContentResponse);

    then(s3Presigner).should().presignGetObject(any(GetObjectPresignRequest.class));
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    assertThat(responseEntity.getHeaders().getLocation()).isEqualTo(url.toURI());


  }


}