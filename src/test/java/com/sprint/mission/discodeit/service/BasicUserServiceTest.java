package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @InjectMocks
    private BasicUserService basicUserService;

    private User testUser;
    private UserStatus testUserStatus;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testUser = new User("testUser", "test@example.com", "password123");
        testUserStatus = new UserStatus(testUser, Instant.now());
    }

    @Test
    void createUser_Success() {
        // Given
        UserCreateRequest request = new UserCreateRequest("testUser", "test@example.com", "password123", null);

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(testUserStatus);

        // When
        UserResponse response = basicUserService.create(request);

        // Then
        assertEquals("testUser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userStatusRepository, times(1)).save(any(UserStatus.class));
    }

    @Test
    void createUser_Fail_DuplicateUsername() {
        // Given
        UserCreateRequest request = new UserCreateRequest("testUser", "test@example.com", "password123", null);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> basicUserService.create(request));
    }

    @Test
    void createUser_Fail_DuplicateEmail() {
        // Given
        UserCreateRequest request = new UserCreateRequest("testUser", "test@example.com", "password123", null);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> basicUserService.create(request));
    }

    @Test
    void findUser_Success() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userStatusRepository.findByUser(testUser)).thenReturn(Optional.of(testUserStatus));

        // When
        UserResponse response = basicUserService.find(testUserId);

        // Then
        assertEquals("testUser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        verify(userRepository, times(1)).findById(testUserId);
    }

    @Test
    void findUser_Fail_NotFound() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicUserService.find(testUserId));
    }

    @Test
    void findAllUsers_Success() {
        // Given
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);
        when(userStatusRepository.findByUser(any(User.class))).thenReturn(Optional.of(testUserStatus));

        // When
        List<UserResponse> responseList = basicUserService.findAll();

        // Then
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUser_Success() {
        // Given
        UserUpdateRequest request = new UserUpdateRequest("updatedUser", "updated@example.com", "newPassword", null);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userStatusRepository.findByUser(testUser)).thenReturn(Optional.of(testUserStatus));

        // When
        UserResponse response = basicUserService.update(testUserId, request);

        // Then
        assertEquals("updatedUser", response.getUsername());
        assertEquals("updated@example.com", response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_Fail_NotFound() {
        // Given
        UserUpdateRequest request = new UserUpdateRequest("updatedUser", "updated@example.com", "newPassword", null);
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicUserService.update(testUserId, request));
    }

    @Test
    void deleteUser_Success() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // When
        basicUserService.delete(testUserId);

        // Then
        verify(userStatusRepository, times(1)).deleteByUser(testUser);
        verify(binaryContentRepository, times(1)).deleteByUser(testUser);
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void deleteUser_Fail_NotFound() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> basicUserService.delete(testUserId));
    }

}
