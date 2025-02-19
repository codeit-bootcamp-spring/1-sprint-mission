package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
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
class BasicReadStatusServiceTest {

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChannelRepository channelRepository;

    @InjectMocks
    private BasicReadStatusService basicReadStatusService;

    private User testUser;
    private Channel testChannel;
    private ReadStatus testReadStatus;
    private UUID testUserId;
    private UUID testChannelId;
    private UUID testReadStatusId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testChannelId = UUID.randomUUID();
        testReadStatusId = UUID.randomUUID();

        testUser = new User("testUser", "test@example.com", "password123");
        testChannel = new Channel(ChannelType.PUBLIC, "Test Channel", "Test Description");
        testReadStatus = new ReadStatus(testUser, testChannel, Instant.now());
    }


    @Test
    @DisplayName("읽기 상태 생성 확인")
    void createReadStatus_Success() {
        // Given
        ReadStatusCreateRequest request = new ReadStatusCreateRequest(testUserId, testChannelId, Instant.now());
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(channelRepository.findById(testChannelId)).thenReturn(Optional.of(testChannel));
        when(readStatusRepository.existsByUserAndChannel(testUser, testChannel)).thenReturn(false);
        doNothing().when(readStatusRepository).save(any(ReadStatus.class));

        // When
        ReadStatusResponse response = basicReadStatusService.create(request);

        // Then
        assertNotNull(response);
        verify(readStatusRepository, times(1)).save(any(ReadStatus.class));
    }

    @Test
    @DisplayName("읽기 상태 생성 실패 유저 없음")
    void createReadStatus_Fail_UserNotFound() {
        // Given
        ReadStatusCreateRequest request = new ReadStatusCreateRequest(testUserId, testChannelId, Instant.now());
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicReadStatusService.create(request));
    }

    @Test
    @DisplayName("읽기 상태 생성 실패 채널 없음")
    void createReadStatus_Fail_ChannelNotFound() {
        // Given
        ReadStatusCreateRequest request = new ReadStatusCreateRequest(testUserId, testChannelId, Instant.now());
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(channelRepository.findById(testChannelId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicReadStatusService.create(request));
    }

    @Test
    @DisplayName("읽기 상태 생성 실패 중복")
    void createReadStatus_Fail_Duplicate() {
        // Given
        ReadStatusCreateRequest request = new ReadStatusCreateRequest(testUserId, testChannelId, Instant.now());
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(channelRepository.findById(testChannelId)).thenReturn(Optional.of(testChannel));
        when(readStatusRepository.existsByUserAndChannel(testUser, testChannel)).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> basicReadStatusService.create(request));
    }

    @Test
    @DisplayName("읽기 상태 찾기 확인")
    void findReadStatus_Success() {
        // Given
        when(readStatusRepository.findById(testReadStatusId)).thenReturn(Optional.of(testReadStatus));

        // When
        ReadStatusResponse response = basicReadStatusService.find(testReadStatusId);

        // Then
        assertNotNull(response);
        verify(readStatusRepository, times(1)).findById(testReadStatusId);
    }

    @Test
    @DisplayName("읽기 상태 찾기 실패 없음")
    void findReadStatus_Fail_NotFound() {
        // Given
        when(readStatusRepository.findById(testReadStatusId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicReadStatusService.find(testReadStatusId));
    }

    @Test
    @DisplayName("유저id로 모든 읽기 상태 확인")
    void findAllReadStatusesByUserId_Success() {
        // Given
        List<ReadStatus> readStatuses = List.of(testReadStatus);
        when(readStatusRepository.findByUserId(testUserId)).thenReturn(readStatuses);

        // When
        List<ReadStatusResponse> responses = basicReadStatusService.findAllByUserId(testUserId);

        // Then
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        verify(readStatusRepository, times(1)).findByUserId(testUserId);
    }

    @Test
    @DisplayName("읽기 상태 업데이트 확인")
    void updateReadStatus_Success() {
        // Given
        ReadStatusUpdateRequest request = new ReadStatusUpdateRequest(testReadStatusId, Instant.now());
        when(readStatusRepository.findById(testReadStatusId)).thenReturn(Optional.of(testReadStatus));

        // When
        ReadStatusResponse response = basicReadStatusService.update(request);

        // Then
        assertNotNull(response);
        verify(readStatusRepository, times(1)).save(any(ReadStatus.class));
    }

    @Test
    @DisplayName("읽기 상태 업데이트 실패 없음")
    void updateReadStatus_Fail_NotFound() {
        // Given
        ReadStatusUpdateRequest request = new ReadStatusUpdateRequest(testReadStatusId, Instant.now());
        when(readStatusRepository.findById(testReadStatusId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicReadStatusService.update(request));
    }

    @Test
    @DisplayName("읽기 상태 삭제 확인")
    void deleteReadStatus_Success() {
        // Given
        when(readStatusRepository.existsById(testReadStatusId)).thenReturn(true);

        // When
        basicReadStatusService.delete(testReadStatusId);

        // Then
        verify(readStatusRepository, times(1)).deleteById(testReadStatusId);
    }

    @Test
    @DisplayName("읽기 상태 삭제 없음")
    void deleteReadStatus_Fail_NotFound() {
        // Given
        when(readStatusRepository.existsById(testReadStatusId)).thenReturn(false);

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicReadStatusService.delete(testReadStatusId));
    }
}
