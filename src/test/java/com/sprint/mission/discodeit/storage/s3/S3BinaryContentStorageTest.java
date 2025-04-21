package com.sprint.mission.discodeit.storage.s3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class S3BinaryContentStorageTest {

  private static Properties props = new Properties();
  private static String accessKey;
  private static String secretKey;
  private static String region;
  private static String bucket;

  @BeforeAll
  public static void loadProperties() {
    try (FileInputStream fis = new FileInputStream(".env")) {
      props.load(fis);
      accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
      secretKey = props.getProperty("AWS_S3_SECRET_KEY");
      region = props.getProperty("AWS_S3_REGION");
      bucket = props.getProperty("AWS_S3_BUCKET");
      System.out.println("AWS 설정 정보가 성공적으로 로드되었습니다.");
    } catch (IOException e) {
      System.out.println("AWS 설정 정보 로드 중 에러 발생: " + e.getMessage());
    }
  }

  @Test
  public void testPutAndGet() {
    S3BinaryContentStorage storage = new S3BinaryContentStorage(accessKey, secretKey, region,
        bucket);

    //업로드 할 테스트 데이터
    UUID testId = UUID.randomUUID();
    String testContent = "test data";
    byte[] testBytes = testContent.getBytes();

    //put
    UUID putId = storage.put(testId, testBytes);
    assertEquals(testId, putId, "테스트 ID와 업로드 ID가 동일해야 함");

    // get() 메서드 테스트
    try (InputStream inputStream = storage.get(testId)) {
      byte[] retrievedBytes = inputStream.readAllBytes();
      String retrievedContent = new String(retrievedBytes, StandardCharsets.UTF_8);
      assertEquals(testContent, retrievedContent, "S3에서 가져온 콘텐츠가 업로드된 콘텐츠와 일치해야 합니다.");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Test
  public void testDownload() {
    S3BinaryContentStorage storage = new S3BinaryContentStorage(accessKey, secretKey, region,
        bucket);

    //테스트 데이터
    UUID testId = UUID.randomUUID();
    String testContent = "test data";
    byte[] testBytes = testContent.getBytes();
    storage.put(testId, testBytes);

    //DTO 생성
    BinaryContentDto dto = new BinaryContentDto(
        testId, "testdownload.txt", (long) testBytes.length, testContent
    );

    //download 메서드 테스트
    ResponseEntity<?> responseEntity = storage.download(dto);
    assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());

    HttpHeaders headers = responseEntity.getHeaders();
    assertTrue(headers.containsKey(HttpHeaders.LOCATION), "응답 헤더에 Location 정보가 있어야 합니다.");
    String location = headers.getFirst(HttpHeaders.LOCATION);
    assertNotNull(location, "Location 헤더 값은 null이 아니어야 합니다.");
    assertTrue(location.startsWith("https://"), "생성된 Presigned URL은 https://로 시작해야 합니다.");

    // 추가: 콘솔에 URL 출력 (테스트 시 확인 용도)
    System.out.println("생성된 Presigned URL: " + location);
  }
}
