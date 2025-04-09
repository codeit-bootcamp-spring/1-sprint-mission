package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;
    
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDto testUserDto;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = User.builder()
                .id(userId)
                .name("testUser")
                .email("test@email.com")
                .password("password")
                .online(false)
                .build();
        
        testUserDto = UserDto.builder()
                .id(userId)
                .name("testUser")
                .username("testUser")
                .email("test@email.com")
                .password("password")
                .online(false)
                .build();
    }

    @Test
    @DisplayName("사용자 생성 성공 테스트")
    void createUser_Success() {
        // Given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willReturn(testUser);
        given(userMapper.toDto(any(User.class))).willReturn(testUserDto);

        // When
        UserDto createdUser = userService.create(testUserDto, (byte[]) null);

        // Then
        assertNotNull(createdUser);
        assertEquals(testUserDto.getName(), createdUser.getName());
        assertEquals(testUserDto.getEmail(), createdUser.getEmail());
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("사용자 생성 실패 테스트 - 이메일 중복")
    void createUser_Failure_DuplicateEmail() {
        // Given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(testUser));

        // When & Then
        RestApiException exception = assertThrows(RestApiException.class, () -> 
            userService.create(testUserDto, (byte[]) null)
        );
        assertEquals(DomainErrorCode.DUPLICATED_EMAIL, exception.getErrorCode());
    }

    @Test
    @DisplayName("사용자 정보 수정 성공 테스트")
    void updateUser_Success() {
        // Given
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setNewUsername("updatedUser");
        updateRequest.setNewEmail("updated@email.com");
        
        User updatedUser = User.builder()
                .id(userId)
                .name("updatedUser")
                .email("updated@email.com")
                .password("password")
                .online(false)
                .build();
        
        UserDto updatedUserDto = UserDto.builder()
                .id(userId)
                .name("updatedUser")
                .username("updatedUser")
                .email("updated@email.com")
                .password("password")
                .online(false)
                .build();
        
        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));
        given(userRepository.findByEmail(updateRequest.getNewEmail())).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willReturn(updatedUser);
        given(userMapper.toDto(any(User.class))).willReturn(updatedUserDto);

        // When
        UserDto result = userService.update(userId, updateRequest, (byte[]) null);

        // Then
        assertNotNull(result);
        assertEquals(updateRequest.getNewUsername(), result.getName());
        assertEquals(updateRequest.getNewEmail(), result.getEmail());
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("사용자 정보 수정 실패 테스트 - 사용자 없음")
    void updateUser_Failure_UserNotFound() {
        // Given
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setNewUsername("updatedUser");
        
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // When & Then
        RestApiException exception = assertThrows(RestApiException.class, () -> 
            userService.update(userId, updateRequest, (byte[]) null)
        );
        assertEquals(DomainErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("사용자 삭제 성공 테스트")
    void deleteUser_Success() {
        // Given
        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));
        willDoNothing().given(userRepository).delete(any(User.class));

        // When & Then
        assertDoesNotThrow(() -> userService.delete(userId));
        then(userRepository).should().delete(any(User.class));
    }

    @Test
    @DisplayName("사용자 삭제 실패 테스트 - 사용자 없음")
    void deleteUser_Failure_UserNotFound() {
        // Given
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // When & Then
        RestApiException exception = assertThrows(RestApiException.class, () -> 
            userService.delete(userId)
        );
        assertEquals(DomainErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}