//package com.spirnt.mission.discodeit;
//
//import static org.junit.jupiter.api.Assertions.assertArrayEquals;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
//import com.spirnt.mission.discodeit.storage.s3.S3BinaryContentStorage;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.UUID;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//@ActiveProfiles("test")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class S3BinaryContentStorageTest {
//
//  @Autowired
//  private S3BinaryContentStorage storage;
//
//  @Test
//  @Transactional
//  @DisplayName("파일 업로드 테스트")
//  void uploadTest() throws IOException {
//    // given
//    UUID id = UUID.randomUUID();
//    byte[] bytes = Files.readAllBytes(Path.of("src/test/resources/sample.jpeg"));
//
//    // when
//    UUID resultId = storage.put(id, bytes);
//    InputStream inputStream = storage.get(resultId);
//    byte[] downloadedBytes = inputStream.readAllBytes();
//
//    // then
//    assertEquals(id, resultId);
//    assertEquals(bytes.length, downloadedBytes.length);
//    assertArrayEquals(bytes, downloadedBytes);
//
//  }
//
//  @Test
//  @Transactional
//  @DisplayName("파일 다운로드 테스트")
//  void downloadTest() {
//    // given
//    UUID id = UUID.fromString("c0ecdaa6-12c1-4e47-bb23-0853cd7df53f");
//    String contentType = "image/jpeg";
//    BinaryContentDto binaryContentDto = new BinaryContentDto(id, null, 0L, contentType);
//
//    // when
//    ResponseEntity<?> response = storage.download(binaryContentDto);
//
//    // then
//    assertEquals(HttpStatus.FOUND, response.getStatusCode());
//    assertNotNull(response.getHeaders().getLocation());
//    assertTrue(response.getHeaders().getLocation().toString().contains("amazonaws.com"));
//  }
//}
