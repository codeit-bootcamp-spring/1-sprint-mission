package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.exception.BinaryContentException;
import com.sprint.mission.discodeit.repository.file.json.JsonBinaryContentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BinaryContentServiceImplTest {
    private BinaryContentServiceImpl binaryContentService;
    private UUID userId;
    private UUID messageId;

    @BeforeEach
    void setUp() {
        binaryContentService = new BinaryContentServiceImpl(new JsonBinaryContentRepository());
        userId = UUID.randomUUID();
        messageId = UUID.randomUUID();
    }

    @Test
    @DisplayName("BinaryContent 생성 성공")
    void createBinaryContent_Success() {
        BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                userId,
                messageId,
                "test.txt",
                "text/plain"
        );

        BinaryContent result = binaryContentService.create(request);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(messageId, result.getMessageId());
        assertEquals("test.txt", result.getFileName());
        assertEquals("text/plain", result.getFileType());
    }

    @Test
    @DisplayName("null request로 BinaryContent 생성 시 예외 발생")
    void createBinaryContent_WithNullRequest_ThrowsException() {
        assertThrows(BinaryContentException.class,
                () -> binaryContentService.create(null));
    }

    @Test
    @DisplayName("ID로 BinaryContent 조회 성공")
    void findBinaryContent_Success() {
        BinaryContent content = createSampleContent();
        UUID contentId = content.getId();

        BinaryContent result = binaryContentService.find(contentId);

        assertNotNull(result);
        assertEquals(contentId, result.getId());
        assertEquals(userId, result.getUserId());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 예외 발생")
    void findBinaryContent_WithNonExistentId_ThrowsException() {
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(BinaryContentException.class,
                () -> binaryContentService.find(nonExistentId));
    }

    @Test
    @DisplayName("ID로 모든 BinaryContent 조회")
    void findAllById_Success() {
        BinaryContent content = createSampleContent();
        UUID contentId = content.getId();

        Map<UUID, BinaryContent> result = binaryContentService.findAllById(contentId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(contentId));
    }

    @Test
    @DisplayName("BinaryContent 삭제 성공")
    void deleteBinaryContent_Success() {
        BinaryContent content = createSampleContent();
        UUID contentId = content.getId();

        binaryContentService.delete(contentId);

        assertThrows(BinaryContentException.class,
                () -> binaryContentService.find(contentId));
    }

    private BinaryContent createSampleContent() {
        BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                userId,
                messageId,
                "test.txt",
                "text/plain"
        );
        return binaryContentService.create(request);
    }
}