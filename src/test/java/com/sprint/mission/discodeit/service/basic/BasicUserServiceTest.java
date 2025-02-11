package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.validation.ValidateUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

    @InjectMocks
    BasicUserService basicUserService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserStatusRepository userStatusRepository;

    @Mock
    BinaryContentRepository binaryContentRepository;

    @Mock
    ValidateUser validateUser;

    @Test
    void create_Success(){
        // given
        byte[] profileImage = new byte[]{1, 2, 3};
        UserCreateRequest request = new UserCreateRequest("코드잇", "test@gmail.com", "01012345678", "qwer123$", profileImage);
        User user = new User(request.username(), request.email(), request.phoneNumber(), request.password());
        UserStatus userStatus = new UserStatus(user.getUserId(), Instant.now());
        BinaryContent binaryContent = new BinaryContent(user.getUserId(), null, profileImage);

        // when
        doNothing().when(validateUser).validateUser(request.username(), request.email(), request.phoneNumber(), request.password());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(userStatus);
        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(binaryContent);

        UserDto result = basicUserService.create(request);

        // then
        assertNotNull(result);
        assertEquals(result.username(), request.username());

        verify(userRepository, times(1)).save(any(User.class));
        verify(userStatusRepository, times(1)).save(any(UserStatus.class));
        verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
    }

    @Test
    void findByUserId_Success(){
        // given
        User user = new User();
        UserStatus userStatus = new UserStatus();

        // when
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        when(userStatusRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(userStatus));

        UserDto result = basicUserService.findByUserId(user.getUserId());

        // then
        assertNotNull(result);
        assertEquals(result.userId(), user.getUserId());

        verify(userRepository, times(1)).findByUserId(user.getUserId());
        verify(userStatusRepository, times(1)).findByUserId(user.getUserId());
    }

    @Test
    void findByUserId_UserNotFound(){
        // given
        User user = new User();

        // when
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicUserService.findByUserId(user.getUserId()));

        verify(userStatusRepository, never()).findByUserId(user.getUserId());
    }

    @Test
    void findByUserId_UserStatusNotFound(){
        // given
        User user = new User();
        UserStatus userStatus = new UserStatus();

        // when
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        when(userStatusRepository.findByUserId(user.getUserId())).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicUserService.findByUserId(user.getUserId()));

        verify(userRepository, times(1)).findByUserId(user.getUserId());
    }

    @Test
    void findAll_Success(){
        // given
        List<User> users = new ArrayList<>(List.of(
                new User(),
                new User()
        ));
        UUID userId = users.get(0).getUserId();
        UserStatus userStatus = new UserStatus(userId, Instant.now());

        // when
        when(userRepository.findAll()).thenReturn(users);
        when(userStatusRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(userStatus));

        List<UserDto> results = basicUserService.findAll();

        // then
        assertNotNull(results);
        assertEquals(2, results.size());

        verify(userRepository, times(1)).findAll();
        verify(userStatusRepository, times(2)).findByUserId(any(UUID.class));
    }

    @Test
    void findAll_UserStatusNotFound(){
        // given
        List<User> users = new ArrayList<>(List.of(
                new User(),
                new User()
        ));

        // when
        when(userRepository.findAll()).thenReturn(users);
        when(userStatusRepository.findByUserId(any(UUID.class))).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicUserService.findAll());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void update_Success(){
        // given
        User user = new User("코드잇", "test@gmail.com", "01012345678", "qwer123$");
        byte[] profileImage = new byte[]{1, 2, 3};
        UserUpdateRequest request = new UserUpdateRequest(user.getUserId(), "수정 코드잇", "test@gmail.com", "01012345678", "qwer123$", profileImage);
        UserStatus userStatus = new UserStatus(user.getUserId(), Instant.now());
        BinaryContent binaryContent = new BinaryContent(user.getUserId(), null, profileImage);


        // when
        doNothing().when(validateUser).validateUser(request.username(), request.email(), request.phoneNumber(), request.password());
        when(userRepository.findByUserId(request.userId())).thenReturn(Optional.of(user));
        when(binaryContentRepository.findProfileByUserId(user.getUserId())).thenReturn(Optional.of(binaryContent));
        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(binaryContent);
        when(userStatusRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(userStatus));

        UserDto result = basicUserService.update(request);

        // then
        assertNotNull(result);
        assertEquals(result.userId(), request.userId());

        verify(userRepository, times(1)).findByUserId(request.userId());
        verify(binaryContentRepository, times(1)).findProfileByUserId(user.getUserId());
        verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
        verify(userStatusRepository, times(1)).findByUserId(user.getUserId());
    }

    @Test
    void update_UserNotFound(){
        // given
        User user = new User("코드잇", "test@gmail.com", "01012345678", "qwer123$");
        byte[] profileImage = new byte[]{1, 2, 3};
        UserUpdateRequest request = new UserUpdateRequest(user.getUserId(), "수정 코드잇", "test@gmail.com", "01012345678", "qwer123$", profileImage);

        // when
        doNothing().when(validateUser).validateUser(request.username(), request.email(), request.phoneNumber(), request.password());
        when(userRepository.findByUserId(request.userId())).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicUserService.update(request));
    }

    @Test
    void update_UserStatusNotFound(){
        // given
        User user = new User("코드잇", "test@gmail.com", "01012345678", "qwer123$");
        byte[] profileImage = new byte[]{1, 2, 3};
        UserUpdateRequest request = new UserUpdateRequest(user.getUserId(), "수정 코드잇", "test@gmail.com", "01012345678", "qwer123$", profileImage);

        // when
        doNothing().when(validateUser).validateUser(request.username(), request.email(), request.phoneNumber(), request.password());
        when(userRepository.findByUserId(request.userId())).thenReturn(Optional.of(user));
        when(binaryContentRepository.findProfileByUserId(user.getUserId())).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicUserService.update(request));
    }

    @Test
    void delete_Success(){
        // given
        UUID userId = UUID.randomUUID();

        // when
        basicUserService.delete(userId);

        // then
        verify(binaryContentRepository, times(1)).deleteByUserId(userId);
        verify(userStatusRepository, times(1)).deleteByUserId(userId);
        verify(userRepository, times(1)).delete(userId);
    }
}