package com.sprint.mission.discodeit.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest  // Spring Boot 컨텍스트를 로드하는 테스트
class BinaryContentTest {

  @Test
  public void testBinaryContentCreation() {
    // Given: BinaryContent 객체 생성
    String fileName = "test-file.jpg";
    Long size = 1024L;
    String contentType = "image/jpeg";
    byte[] bytes = new byte[1024];

    BinaryContent binaryContent = new BinaryContent(fileName, size, contentType, bytes);

    // Then: UUID id와 createdAt 값이 자동으로 설정되었는지 확인
    assertNotNull(binaryContent.getId(), "ID should be generated");
    assertNotNull(binaryContent.getCreatedAt(), "CreatedAt should be generated");

    // 추가로 ID와 createdAt 값이 올바르게 설정되었는지 확인
    assertTrue(binaryContent.getId() instanceof UUID, "ID should be of type UUID");
    assertTrue(binaryContent.getCreatedAt() instanceof Instant,
        "CreatedAt should be of type Instant");

    // createdAt 값이 생성 시점과 비슷한 시간인지 (약간의 차이 있을 수 있음)
    assertTrue(binaryContent.getCreatedAt().isBefore(Instant.now()),
        "CreatedAt should be before current time");
  }
}