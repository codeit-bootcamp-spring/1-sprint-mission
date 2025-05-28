package com.spirnt.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.entity.UserStatusType;
import com.spirnt.mission.discodeit.exception.User.UserAlreadyExistException;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.basic.BasicUserService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserStatusRepository userStatusRepository;
    @InjectMocks
    private BasicUserService basicUserService;

    @DisplayName("사용자 생성 테스트 - 성공")
    @Test
    void testCreateUserSuccess() {
        // given
        UserCreateRequest userCreateRequest = new UserCreateRequest("Anna", "Anna@mail.com",
            "password");
        User user = new User("Anna", "Anna@mail.com", "password");

        // 중복 검사 통과하도록 설정
        when(userRepository.existsByEmail(userCreateRequest.email())).thenReturn(false);
        when(userRepository.existsByUsername(userCreateRequest.username())).thenReturn(false);
        // mock 객체 반환값 설정
        when(userRepository.save(eq(user))).thenReturn(user);
        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(
            new UserStatus(user, UserStatusType.ONLINE, Instant.now()));
        when(userMapper.toDto(any(User.class))).thenAnswer(invocation -> {
            User userArg = invocation.getArgument(0);
            return new UserDto(userArg.getId(), userArg.getCreatedAt(), userArg.getUpdatedAt(),
                userArg.getUsername(), userArg.getEmail(), true, null);
        });

        // when
        UserDto result = basicUserService.create(userCreateRequest, null);

        // then
        assertNotNull(result);
        assertEquals(result.getUsername(), userCreateRequest.username());
        assertEquals(result.getEmail(), userCreateRequest.email());
        // DB에 저장하는 로직을 실행했는가?
        verify(userRepository).save(any(User.class));
        verify(userStatusRepository).save(any(UserStatus.class));
    }

    @DisplayName("사용자 생성 테스트 - 실패: 이메일 중복")
    @Test
    void testCreateUserFailDueToDuplicate1() {
        // given
        UserCreateRequest userCreateRequest = new UserCreateRequest("Anna1", "Anna@mail.com",
            "password");

        // 중복 검사 시 true로 설정
        when(userRepository.existsByEmail(userCreateRequest.email())).thenReturn(true);

        // when & then
        assertThrows(UserAlreadyExistException.class, () ->
            basicUserService.create(userCreateRequest, null)
        );
    }

    @DisplayName("사용자 생성 테스트 - 실패: 이름 중복")
    @Test
    void testCreateUserFailDueToDuplicate2() {
        // given
        UserCreateRequest userCreateRequest = new UserCreateRequest("Anna", "Anna1@mail.com",
            "password");

        // 중복 검사 시 true로 설정
        when(userRepository.existsByEmail(userCreateRequest.email())).thenReturn(false);
        when(userRepository.existsByUsername(userCreateRequest.username())).thenReturn(true);

        // when & then
        assertThrows(UserAlreadyExistException.class, () ->
            basicUserService.create(userCreateRequest, null)
        );
    }


    @DisplayName("사용자 수정 테스트 - 성공")
    @Test
    void testUpdateUserSuccess() {
        // given
        UUID userId = UUID.randomUUID();
        User user = new User("Anna", "Anna@mail.com", "password");
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("Andy", "Andy@mail.com",
            "newPassword");

        when(userRepository.existsByEmail(userUpdateRequest.newEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userUpdateRequest.newUsername())).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(any(User.class))).thenAnswer(invocation -> {
            User userArg = invocation.getArgument(0);
            return new UserDto(userArg.getId(), userArg.getCreatedAt(), userArg.getUpdatedAt(),
                userArg.getUsername(), userArg.getEmail(), true, null);
        });

        // when
        UserDto result = basicUserService.update(userId, userUpdateRequest, null);

        // then
        assertNotNull(result);
        assertEquals(result.getUsername(), userUpdateRequest.newUsername());
        assertEquals(result.getEmail(), userUpdateRequest.newEmail());
    }

    @DisplayName("사용자 수정 테스트 - 실패: 유저가 존재하지 않음")
    @Test
    void testUpdateUserFailDueToNotFound() {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("Andy", "Andy@mail.com",
            "newPassword");

        // repository에서 조회 시 조회되는 객체가 없게 설정
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () ->
            basicUserService.update(userId, userUpdateRequest, null)
        );
    }


    @DisplayName("사용자 삭제 테스트- 성공")
    @Test
    void testDeleteUserSuccess() {
        // given
        UUID userId = UUID.randomUUID();
        User user = new User("Anna", "Anna@mail.com", "password");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        basicUserService.delete(userId);

        // then
        // delete 메서드가 정상 호출되었는지 검증
        verify(userRepository).delete(user);
    }

    @DisplayName("사용자 삭제 테스트- 실패: 유저가 존재하지 않음")
    @Test
    void testDeleteUserFailDueToNotFound() {
        // given
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () ->
            basicUserService.delete(userId)
        );
    }
}
