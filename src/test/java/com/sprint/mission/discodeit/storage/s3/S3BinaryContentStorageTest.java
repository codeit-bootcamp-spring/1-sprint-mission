package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@Disabled
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("S3BinaryContentStorage 테스트")
class S3BinaryContentStorageTest {

  @Autowired
  private S3BinaryContentStorage s3BinaryContentStorage;

  @Value("${discodeit.storage.s3.bucket}")
  private String bucket;

  @Value("${discodeit.storage.s3.access-key}")
  private String accessKey;

  @Value("${discodeit.storage.s3.secret-key}")
  private String secretKey;

  @Value("${discodeit.storage.s3.region}")
  private String region;

  private final UUID testId = UUID.randomUUID();
  private final byte[] testData = "테스트 데이터".getBytes();

  @BeforeEach
  void setUp() {
    // 테스트 준비 작업
    // 실제 S3BinaryContentStorage는 스프링이 의존성 주입으로 제공
  }

  @AfterEach
  void tearDown() {
    // 테스트 종료 후 생성된 S3 객체 삭제
    try {
      // S3 클라이언트 생성
      S3Client s3Client = S3Client.builder()
          .region(Region.of(region))
          .credentialsProvider(
              StaticCredentialsProvider.create(
                  AwsBasicCredentials.create(accessKey, secretKey)
              )
          )
          .build();

      // 테스트에서 생성한 객체 삭제
      DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
          .bucket(bucket)
          .key(testId.toString())
          .build();

      s3Client.deleteObject(deleteRequest);
      System.out.println("테스트 객체 삭제 완료: " + testId);
    } catch (NoSuchKeyException e) {
      // 객체가 이미 없는 경우는 무시
      System.out.println("삭제할 객체가 없음: " + testId);
    } catch (Exception e) {
      // 정리 실패 시 로그만 남기고 테스트는 실패로 처리하지 않음
      System.err.println("테스트 객체 정리 실패: " + e.getMessage());
    }
  }

  @Test
  @DisplayName("S3에 파일 업로드 성공 테스트")
  void put_success() {
    // when
    UUID resultId = s3BinaryContentStorage.put(testId, testData);

    // then
    assertThat(resultId).isEqualTo(testId);
  }

  @Test
  @DisplayName("S3에서 파일 다운로드 테스트")
  void get_success() throws IOException {
    // given
    s3BinaryContentStorage.put(testId, testData);

    // when
    InputStream result = s3BinaryContentStorage.get(testId);

    // then
    assertNotNull(result);

    // 내용 검증
    byte[] resultBytes = result.readAllBytes();
    assertThat(resultBytes).isEqualTo(testData);
  }

  @Test
  @DisplayName("존재하지 않는 파일 조회 시 예외 발생 테스트")
  void get_notFound() {
    // when & then
    assertThatThrownBy(() -> s3BinaryContentStorage.get(UUID.randomUUID()))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  @DisplayName("Presigned URL 생성 테스트")
  void download_success() {
    // given
    s3BinaryContentStorage.put(testId, testData);
    BinaryContentDto dto = new BinaryContentDto(
        testId, "test.txt", (long) testData.length, "text/plain"
    );

    // when
    ResponseEntity<Void> response = s3BinaryContentStorage.download(dto);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    assertThat(response.getHeaders().get(HttpHeaders.LOCATION)).isNotNull();

    String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
    assertThat(location).contains(bucket);
    assertThat(location).contains(testId.toString());
  }
} 