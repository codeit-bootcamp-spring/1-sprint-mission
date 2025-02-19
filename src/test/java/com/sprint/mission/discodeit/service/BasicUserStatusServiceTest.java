package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
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
class BasicUserStatusServiceTest {

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BasicUserStatusService basicUserStatusService;

    private User testUser;
    private UserStatus testUserStatus;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUser = new User("testUser", "test@example.com", "password123");
        testUserStatus = new UserStatus(testUser, Instant.now());

    }

    @Test
    @DisplayName("유저 생성 확인")
    void createUserStatus_Success() {
        // Given
        UserStatusCreateRequest request = new UserStatusCreateRequest(testUserId, Instant.now());
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userStatusRepository.existsByUser(testUser)).thenReturn(false);

        // When
        UserStatusResponse response = basicUserStatusService.create(request);

        // Then
        assertNotNull(response);
        verify(userStatusRepository, times(1)).save(any(UserStatus.class));
    }

    @Test
    @DisplayName("유저 생성 실패 유저 없음")
    void createUserStatus_Fail_UserNotFound() {
        // Given
        UserStatusCreateRequest request = new UserStatusCreateRequest(testUserId, Instant.now());
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicUserStatusService.create(request));
    }

    @Test
    @DisplayName("유저 생성 실패 이미 유저 존재")
    void createUserStatus_Fail_AlreadyExists() {
        // Given
        UserStatusCreateRequest request = new UserStatusCreateRequest(testUserId, Instant.now());
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userStatusRepository.existsByUser(testUser)).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> basicUserStatusService.create(request));
    }

    @Test
    @DisplayName("유저 상태 조회 확인")
    void findUserStatus_Success() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userStatusRepository.findByUser(testUser)).thenReturn(Optional.of(testUserStatus));

        // When
        UserStatusResponse response = basicUserStatusService.findByUserId(testUserId);

        // Then
        assertNotNull(response);
        verify(userStatusRepository, times(1)).findByUser(testUser);
    }

    @Test
    @DisplayName("유저 상태 조회 실패 유저 없음")
    void findUserStatus_Fail_UserNotFound() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicUserStatusService.findByUserId(testUserId));
    }

    @Test
    @DisplayName("유저 상태 조회 실패 없음")
    void findUserStatus_Fail_NotFound() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userStatusRepository.findByUser(testUser)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicUserStatusService.findByUserId(testUserId));
    }

    @Test
    @DisplayName("유저 상태 업데이트 확인")
    void updateUserStatus_Success() {
        // Given
        UserStatusUpdateByUserIdRequest request = new UserStatusUpdateByUserIdRequest(testUserId, Instant.now());
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userStatusRepository.findByUser(testUser)).thenReturn(Optional.of(testUserStatus));

        // When
        UserStatusResponse response = basicUserStatusService.updateByUserId(request);

        // Then
        assertNotNull(response);
        verify(userStatusRepository, times(1)).save(any(UserStatus.class));
    }

    @Test
    @DisplayName("유저 상태 업데이트 실패 유저 없음")
    void updateUserStatus_Fail_UserNotFound() {
        // Given
        UserStatusUpdateByUserIdRequest request = new UserStatusUpdateByUserIdRequest(testUserId, Instant.now());
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicUserStatusService.updateByUserId(request));
    }

    @Test
    @DisplayName("유저 상태 삭제 확인")
    void deleteUserStatus_Success() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userStatusRepository.existsByUser(testUser)).thenReturn(true);

        // When
        basicUserStatusService.deleteByUserId(testUserId);

        // Then
        verify(userStatusRepository, times(1)).deleteByUser(testUser);
    }

    @Test
    @DisplayName("유저 상태 삭제 실패 유저 없음")
    void deleteUserStatus_Fail_UserNotFound() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicUserStatusService.deleteByUserId(testUserId));
    }

    @Test
    @DisplayName("유저 상태 삭제 실패 없음")
    void deleteUserStatus_Fail_NotFound() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userStatusRepository.existsByUser(testUser)).thenReturn(false);

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicUserStatusService.deleteByUserId(testUserId));
    }
}
