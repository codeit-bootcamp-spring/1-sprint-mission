package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicBinaryContentServiceTest {

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private BasicBinaryContentService basicBinaryContentService;

    private User testUser;
    private Message testMessage;
    private BinaryContent testBinaryContent;
    private UUID testUserId;
    private UUID testMessageId;
    private UUID testBinaryContentId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testMessageId = UUID.randomUUID();
        testBinaryContentId = UUID.randomUUID();

        testUser = new User("testUser", "test@example.com", "password123");
        testMessage = new Message("Test Message", testMessageId, testUserId);
        testBinaryContent = new BinaryContent(testUser, testMessage, new byte[]{1, 2, 3}, "image/png");
    }

    @Test
    @DisplayName("Binary 생성 확인")
    void createBinaryContent_Success() throws IOException {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(file.getContentType()).thenReturn("image/png");

        BinaryContentCreateRequest request = new BinaryContentCreateRequest(testUserId, testMessageId, file);

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(messageRepository.findById(testMessageId)).thenReturn(Optional.of(testMessage));
        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(testBinaryContent);

        // When
        BinaryContentResponse response = basicBinaryContentService.create(request);

        // Then
        assertNotNull(response);
        assertEquals(testBinaryContent.getMimeType(), response.getMimeType());
        verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
    }

    @Test
    @DisplayName("Binary 생성 실패 사용자 X")
    void createBinaryContent_Fail_UserNotFound() {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        BinaryContentCreateRequest request = new BinaryContentCreateRequest(testUserId, testMessageId, file);

        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicBinaryContentService.create(request));
        verify(userRepository, times(1)).findById(testUserId);
    }

    @Test
    @DisplayName("Binary 생성 실패 메시지 X")
    void createBinaryContent_Fail_MessageNotFound() {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        BinaryContentCreateRequest request = new BinaryContentCreateRequest(testUserId, testMessageId, file);

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(messageRepository.findById(testMessageId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicBinaryContentService.create(request));
        verify(messageRepository, times(1)).findById(testMessageId);
    }

    @Test
    @DisplayName("Binary조회 확인")
    void findBinaryContent_Success() {
        // Given
        when(binaryContentRepository.findById(testBinaryContentId)).thenReturn(Optional.of(testBinaryContent));

        // When
        BinaryContentResponse response = basicBinaryContentService.find(testBinaryContentId);

        // Then
        assertNotNull(response);
        assertEquals(testBinaryContent.getMimeType(), response.getMimeType());
        verify(binaryContentRepository, times(1)).findById(testBinaryContentId);
    }

    @Test
    @DisplayName("Binary 조회 실패 없음")
    void findBinaryContent_Fail_NotFound() {
        // Given
        when(binaryContentRepository.findById(testBinaryContentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicBinaryContentService.find(testBinaryContentId));
    }

    @Test
    @DisplayName("Binary 모두 조회 확인")
    void findAllBinaryContents_Success() {
        // Given
        List<BinaryContent> binaryContents = List.of(testBinaryContent);
        List<UUID> ids = List.of(testBinaryContentId);

        when(binaryContentRepository.findAllByIdIn(ids)).thenReturn(binaryContents);

        // When
        List<BinaryContentResponse> responses = basicBinaryContentService.findAllByIdIn(ids);

        // Then
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        verify(binaryContentRepository, times(1)).findAllByIdIn(ids);
    }

    @Test
    @DisplayName("Binary 삭제 확인")
    void deleteBinaryContent_Success() {
        // Given
        when(binaryContentRepository.existsById(testBinaryContentId)).thenReturn(true);

        // When
        basicBinaryContentService.delete(testBinaryContentId);

        // Then
        verify(binaryContentRepository, times(1)).deleteById(testBinaryContentId);
    }

    @Test
    @DisplayName("Binary 삭제 없음")
    void deleteBinaryContent_Fail_NotFound() {
        // Given
        when(binaryContentRepository.existsById(testBinaryContentId)).thenReturn(false);

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicBinaryContentService.delete(testBinaryContentId));
    }
}
