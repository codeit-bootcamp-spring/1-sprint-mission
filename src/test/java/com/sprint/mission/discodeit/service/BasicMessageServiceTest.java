package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @InjectMocks
    private BasicMessageService basicMessageService;

    private User testUser;
    private Channel testChannel;
    private Message testMessage;
    private UUID testUserId;
    private UUID testChannelId;
    private UUID testMessageId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testChannelId = UUID.randomUUID();
        testMessageId = UUID.randomUUID();

        testUser = new User("testUser", "test@example.com", "password123");
        testChannel = new Channel(ChannelType.PUBLIC, "Test Channel", "Test Description");
        testMessage = new Message("Hello World!", testChannelId, testUserId);

    }

    @Test
    @DisplayName("메시지 생성 확인")
    void createMessage_Success() {
        // Given
        MessageCreateRequest request = new MessageCreateRequest(testChannelId, testUserId, "Hello World!", null);
        when(channelRepository.findById(testChannelId)).thenReturn(Optional.of(testChannel));
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        // When
        MessageResponse response = basicMessageService.create(request);

        // Then
        assertNotNull(response);
        assertEquals("Hello World!", response.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    @DisplayName("메시지 생성 실패 채널 없음")
    void createMessage_Fail_ChannelNotFound() {
        // Given
        MessageCreateRequest request = new MessageCreateRequest(testChannelId, testUserId, "Hello World!", null);
        when(channelRepository.findById(testChannelId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicMessageService.create(request));
    }

    @Test
    @DisplayName("메시지 생성 실패 유저 없음")
    void createMessage_Fail_UserNotFound() {
        // Given
        MessageCreateRequest request = new MessageCreateRequest(testChannelId, testUserId, "Hello World!", null);
        when(channelRepository.findById(testChannelId)).thenReturn(Optional.of(testChannel));
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicMessageService.create(request));
    }

    @Test
    @DisplayName("채널id로 모든 메시지 찾기")
    void findAllMessagesByChannelId_Success() {
        // Given
        List<Message> messages = List.of(testMessage);
        when(messageRepository.findByChannelId(testChannelId)).thenReturn(messages);
        when(binaryContentRepository.findByMessage(any(Message.class))).thenReturn(Collections.emptyList());
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // When
        List<MessageResponse> responses = basicMessageService.findAllByChannelId(testChannelId);

        // Then
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        verify(messageRepository, times(1)).findByChannelId(testChannelId);
    }

    @Test
    @DisplayName("메시지 업데이트 확인")
    void updateMessage_Success() {
        // Given
        MessageUpdateRequest request = new MessageUpdateRequest(testMessageId, "Updated Content");
        when(messageRepository.findById(testMessageId)).thenReturn(Optional.of(testMessage));
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        // When
        MessageResponse response = basicMessageService.update(request);

        // Then
        assertEquals("Updated Content", response.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    @DisplayName("메시지 업데이트 실패 없음")
    void updateMessage_Fail_NotFound() {
        // Given
        MessageUpdateRequest request = new MessageUpdateRequest(testMessageId, "Updated Content");
        when(messageRepository.findById(testMessageId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicMessageService.update(request));
    }

    @Test
    @DisplayName("메시지 삭제 확인")
    void deleteMessage_Success() {
        // Given
        when(messageRepository.findById(testMessageId)).thenReturn(Optional.of(testMessage));

        // When
        basicMessageService.delete(testMessageId);

        // Then
        verify(binaryContentRepository, times(1)).deleteByMessageId(testMessage);
        verify(messageRepository, times(1)).delete(testMessage);
    }

    @Test
    @DisplayName("메시지 삭제 실패 없음")
    void deleteMessage_Fail_NotFound() {
        // Given
        when(messageRepository.findById(testMessageId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicMessageService.delete(testMessageId));
    }
}
