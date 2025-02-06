package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

    @InjectMocks
    private BasicUserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    private UserCreateRequest request;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createUser_Success() {
        // given
        UserCreateRequest request = new UserCreateRequest("username", "email@example.com", "1234567890", "password", null);
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("email@example.com")).thenReturn(false);
        User user = new User("username", "email@example.com", "1234567890", "password");
        when(userRepository.save(any(User.class))).thenReturn(Optional.of(user));

        // Use the correct userId and match any UUID argument
        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(new UserStatus(user.getId(), Instant.now()));
        when(userStatusRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(new UserStatus(user.getId(), Instant.now())));

        // when
        UserDto result = userService.create(request);

        // then
        assertNotNull(result);
        assertEquals("username", result.username());
        assertTrue(result.isOnline());
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
        UserCreateRequest request = new UserCreateRequest("existingUsername", "test1@example.com", "11234567890", "password1", null);
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // when
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.create(request));

        // then
        assertEquals("Email already exists.", thrown.getMessage());
    }

    @Test
    void withProfileImage() {
        // given
        byte[] profileImage = new byte[]{1, 2, 3};
        UserCreateRequest request = new UserCreateRequest("username", "existingtest@example.com", "1234567890", "password", profileImage);
        User user = new User("username", "existingtest@example.com", "1234567890", "password");
        when(userRepository.existsByUsername(request.username())).thenReturn(false); // 사용자 이름이 중복되지 않음
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(Optional.of(user));
        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(new UserStatus(user.getId(), Instant.now()));
        when(userStatusRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(new UserStatus(user.getId(), Instant.now())));

        // when
        // binaryContentRepository가 프로필 이미지를 저장하도록 모킹
        BinaryContent binaryContent = new BinaryContent(user.getId(), null, profileImage);
        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(Optional.of(binaryContent));
// when
        UserDto result = userService.create(request);

        // then
        assertNotNull(result); // 결과가 null이 아닌지 확인
        assertEquals("username", result.username()); // 사용자의 이름이 예상대로인지 확인
        assertTrue(result.isOnline()); // 사용자가 온라인 상태인지를 확인

        // 프로필 이미지가 저장되었는지 확인
        verify(binaryContentRepository).save(any(BinaryContent.class));
    }

    @Test
    void findByUserId_Succes(){

    }
}