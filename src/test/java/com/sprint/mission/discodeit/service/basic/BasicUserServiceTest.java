package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

    @InjectMocks
    private BasicUserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatusService userStatusService;

    @Mock
    private BinaryContentService binaryContentService;

    private UserCreateRequest request;

    @BeforeEach
    void setUp() {
        request = new UserCreateRequest("newUser", "newuser@example.com", "1234567890", "password123", null);
    }

    @Test
    void createUser_Success() {
        //given
        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);

        // When
        User createdUser = userService.create(request);

        // Then
        assertEquals(request.username(), createdUser.getUsername());
        assertEquals(request.email(), createdUser.getEmail());
    }

    @Test
    void usernameAlreadyExist() {
        // given
        UserCreateRequest request = new UserCreateRequest("existingUsername", "test1@example.com", "11234567890", "password1", null);
        when(userRepository.existsByUsername(request.username())).thenReturn(true);

        // when
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.create(request));

        // then
        assertEquals("Username already exists.", thrown.getMessage());
    }

    @Test
    void emailAlreadyExist() {
        // given
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // when
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.create(request));

        // then
        assertEquals("Email already exists.", thrown.getMessage());
    }

    @Test
    void withProfileImage(){
        // given
        byte[] profileImage = new byte[]{1, 2, 3};
        UserCreateRequest request = new UserCreateRequest("Username", "existingtest@example.com", "1234567890", "password", profileImage);
        when(userRepository.existsByUsername(request.username())).thenReturn(false); // 사용자 이름이 중복되지 않음
        when(userRepository.existsByEmail(request.email())).thenReturn(false);

        // when
        userService.create(request);

        // then
        verify(binaryContentService).create(any(BinaryContentCreateRequest.class));
    }

    @Test
    void findByUserId_Succes(){

    }
}