package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @InjectMocks
    private BasicChannelService basicChannelService;

    private User testUser;
    private Channel testPrivateChannel;
    private Channel testPublicChannel;
    private UUID testUserId;
    private UUID testPrivateChannelId;
    private UUID testPublicChannelId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testPrivateChannelId = UUID.randomUUID();
        testPublicChannelId = UUID.randomUUID();

        testUser = new User("testUser", "test@example.com", "password123");
        testPrivateChannel = new Channel(ChannelType.PRIVATE, null, null);
        testPublicChannel = new Channel(ChannelType.PUBLIC, "Public Channel", "A public chat room");

    }

    @Test
    @DisplayName("privateChannel 생성 확인")
    void createPrivateChannel_Success() {
        // Given
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(testUserId), "testCh", "private channel");
        when(userRepository.findAllById(request.getUserIds())).thenReturn(List.of(testUser));
        when(channelRepository.save(any(Channel.class))).thenReturn(testPrivateChannel);
        doNothing().when(readStatusRepository).save(any(ReadStatus.class));

        // When
        ChannelResponse response = basicChannelService.createPrivateChannel(request);

        // Then
        assertNotNull(response);
        verify(channelRepository, times(1)).save(any(Channel.class));
        verify(readStatusRepository, times(1)).save(any(ReadStatus.class));
    }

    @Test
    @DisplayName("privateChannel 생성 실패 사용자 X")
    void createPrivateChannel_Fail_NoUsers() {
        // Given
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(Collections.emptyList(), "testCh", "private channel");
        when(userRepository.findAllById(request.getUserIds())).thenReturn(Collections.emptyList());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> basicChannelService.createPrivateChannel(request));
    }

    @Test
    @DisplayName("publicChannel 생성 확인")
    void createPublicChannel_Success() {
        // Given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("Public Channel", "A public chat room");
        when(channelRepository.save(any(Channel.class))).thenReturn(testPublicChannel);

        // When
        ChannelResponse response = basicChannelService.createPublicChannel(request);

        // Then
        assertNotNull(response);
        verify(channelRepository, times(1)).save(any(Channel.class));
    }

    @Test
    @DisplayName("채널 찾기 확인")
    void findChannel_Success() {
        // Given
        when(channelRepository.findById(testPublicChannelId)).thenReturn(Optional.of(testPublicChannel));
        when(messageRepository.findLastMessageTimeByChannelId(testPublicChannelId)).thenReturn(Instant.now());

        // When
        ChannelResponse response = basicChannelService.find(testPublicChannelId);

        // Then
        assertNotNull(response);
        verify(channelRepository, times(1)).findById(testPublicChannelId);
    }

    @Test
    @DisplayName("채널 찾기 실패 없음")
    void findChannel_Fail_NotFound() {
        // Given
        when(channelRepository.findById(testPublicChannelId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicChannelService.find(testPublicChannelId));
    }

    @Test
    @DisplayName("사용자 id로 모든 채널 찾기")
    void findAllChannelsByUserId_Success() {
        // Given
        when(channelRepository.findByType(ChannelType.PUBLIC)).thenReturn(List.of(testPublicChannel));
        when(channelRepository.findByUserId(testUserId)).thenReturn(List.of(testPrivateChannel));
        when(readStatusRepository.findUserIdsByChannelId(any(UUID.class))).thenReturn(List.of(testUserId));

        // When
        List<ChannelResponse> responses = basicChannelService.findAllByUserId(testUserId);

        // Then
        assertFalse(responses.isEmpty());
        verify(channelRepository, times(1)).findByType(ChannelType.PUBLIC);
        verify(channelRepository, times(1)).findByUserId(testUserId);
    }

    @Test
    @DisplayName("채널 업데이트 확인")
    void updateChannel_Success() {
        // Given
        ChannelUpdateRequest request = new ChannelUpdateRequest(testPublicChannelId, "Updated Name", "Updated Desc");
        when(channelRepository.findById(testPublicChannelId)).thenReturn(Optional.of(testPublicChannel));
        when(channelRepository.save(any(Channel.class))).thenReturn(testPublicChannel);

        // When
        ChannelResponse response = basicChannelService.update(request);

        // Then
        assertEquals("Updated Name", response.getName());
        verify(channelRepository, times(1)).save(any(Channel.class));
    }

    @Test
    @DisplayName("privatechannel 업데이트 실패")
    void updateChannel_Fail_PrivateChannel() {
        // Given
        ChannelUpdateRequest request = new ChannelUpdateRequest(testPrivateChannelId, "Updated Name", "Updated Desc");
        when(channelRepository.findById(testPrivateChannelId)).thenReturn(Optional.of(testPrivateChannel));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> basicChannelService.update(request));
    }

    @Test
    @DisplayName("채널 삭제 확인")
    void deleteChannel_Success() {
        // Given
        when(channelRepository.findById(testPublicChannelId)).thenReturn(Optional.of(testPublicChannel));

        // When
        basicChannelService.delete(testPublicChannelId);

        // Then
        verify(messageRepository, times(1)).deleteByChannelId(testPublicChannelId);
        verify(readStatusRepository, times(1)).deleteByChannelId(testPublicChannelId);
        verify(channelRepository, times(1)).delete(testPublicChannel);
    }

    @Test
    @DisplayName("채널 삭제 실패 없음")
    void deleteChannel_Fail_NotFound() {
        // Given
        when(channelRepository.findById(testPublicChannelId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicChannelService.delete(testPublicChannelId));
    }
}
