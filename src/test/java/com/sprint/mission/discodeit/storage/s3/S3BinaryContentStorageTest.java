package com.sprint.mission.discodeit.storage.s3;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class S3BinaryContentStorageTest {

  private static String accessKey;
  private static String secretKey;
  private static String region;
  private static String bucket;
  private static S3BinaryContentStorage s3Storage;

  @BeforeAll
  public static void setUp() {
    Properties props = new Properties();
    // .env 로드 + Properties 클래스 설정
    try {
      props.load(new FileInputStream(".env"));
    } catch (IOException e) {
      throw new RuntimeException("환경 설정 파일(.env) 로드 실패");
    }

    accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
    secretKey = props.getProperty("AWS_S3_SECRET_KEY");
    region = props.getProperty("AWS_S3_REGION");
    bucket = props.getProperty("AWS_S3_BUCKET");

    s3Storage = new S3BinaryContentStorage(accessKey, secretKey, region, bucket, 10);
  }

  @Test
  public void testUpload() {
    // 테스트할 데이터 준비
    UUID id = UUID.randomUUID();
    byte[] testData = "업로드 테스트 컨텐츠".getBytes();
    // 데이터 업로드
    UUID resultId = s3Storage.put(id, testData);

    // 검증
    assertEquals(id, resultId);
  }

  @Test
  public void testDownload() throws IOException {
    // 업로드할 테스트 데이터 준비
    UUID id = UUID.randomUUID();
    byte[] testData = "다운로드 테스트 컨텐츠".getBytes();

    // 데이터 업로드
    s3Storage.put(id, testData);

    // 데이터 다운로드

    try (InputStream inputStream = s3Storage.get(id);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
    ) {

      // InputStream 에서 데이터 읽기
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }

      // 검증
      byte[] downloadedData = outputStream.toByteArray();
      assertArrayEquals(testData, downloadedData);
    }
  }

  @Test
  public void testPresignedUrl() {
    // 업로드할 테스트 데이터 준비
    UUID id = UUID.randomUUID();
    byte[] testData = "Presigned Url 테스트 컨텐츠".getBytes();
    String contentType = "text/plain";

    // 데이터 업로드
    s3Storage.put(id, testData);

    // PresignedUrl 생성 테스트
    BinaryContentDto binaryContentDto = new BinaryContentDto(
        id,
        id.toString(),
        (long) testData.length,
        contentType,
        testData
    );
    ResponseEntity<?> response = s3Storage.download(binaryContentDto);

    // 검증
    assertEquals(HttpStatus.FOUND, response.getStatusCode());
    String locationHeader = response.getHeaders().getFirst("Location");
    assertNotNull(locationHeader);
    assertTrue(locationHeader.contains(id.toString()));
  }

}
