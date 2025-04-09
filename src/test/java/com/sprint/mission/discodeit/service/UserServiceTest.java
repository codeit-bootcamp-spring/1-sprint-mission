package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicUserService userService;

    @Nested
    @DisplayName("사용자 생성 테스트")
    class CreateTest {

        private UserCreateRequest request;
        private User user;
        private UserDto userDto;

        @BeforeEach
        void setUp() {
            request = new UserCreateRequest("testuser", "test@email.com", "password123!");
            user = new User("testuser", "test@email.com", "password123!", null);
            userDto = new UserDto(UUID.randomUUID(), "testuser", "test@email.com", null);
        }

        @Test
        @DisplayName("성공: 새로운 사용자를 생성한다")
        void createSuccess() {
            // Given
            given(userRepository.existsByEmail(request.email())).willReturn(false);
            given(userRepository.existsByUsername(request.username())).willReturn(false);
            given(userRepository.save(any(User.class))).willReturn(user);
            given(userMapper.toDto(user)).willReturn(userDto);

            // When
            UserDto result = userService.create(request, Optional.empty());

            // Then
            assertThat(result).isNotNull();
            assertThat(result.username()).isEqualTo(request.username());
            assertThat(result.email()).isEqualTo(request.email());
            then(userRepository).should().save(any(User.class));
        }

        @Test
        @DisplayName("실패: 이미 존재하는 이메일로 생성 시도")
        void createFailWithDuplicateEmail() {
            // Given
            given(userRepository.existsByEmail(request.email())).willReturn(true);

            // When & Then
            assertThatThrownBy(() -> userService.create(request, Optional.empty()))
                .isInstanceOf(UserException.DuplicateEmailException.class);

            then(userRepository).should(never()).save(any(User.class));
        }

        @Test
        @DisplayName("실패: 이미 존재하는 사용자명으로 생성 시도")
        void createFailWithDuplicateUsername() {
            // Given
            given(userRepository.existsByEmail(request.email())).willReturn(false);
            given(userRepository.existsByUsername(request.username())).willReturn(true);

            // When & Then
            assertThatThrownBy(() -> userService.create(request, Optional.empty()))
                .isInstanceOf(UserException.DuplicateUsernameException.class);

            then(userRepository).should(never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("사용자 수정 테스트")
    class UpdateTest {

        private UUID userId;
        private UserUpdateRequest request;
        private User user;
        private UserDto userDto;

        @BeforeEach
        void setUp() {
            userId = UUID.randomUUID();
            request = new UserUpdateRequest("newuser", "new@email.com", "newpassword123!");
            user = new User("olduser", "old@email.com", "oldpassword123!", null);
            userDto = new UserDto(userId, "newuser", "new@email.com", null);
        }

        @Test
        @DisplayName("성공: 사용자 정보를 수정한다")
        void updateSuccess() {
            // Given
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(userRepository.existsByEmail(request.newEmail())).willReturn(false);
            given(userRepository.existsByUsername(request.newUsername())).willReturn(false);
            given(userMapper.toDto(user)).willReturn(userDto);

            // When
            UserDto result = userService.update(userId, request, Optional.empty());

            // Then
            assertThat(result).isNotNull();
            assertThat(result.username()).isEqualTo(request.newUsername());
            assertThat(result.email()).isEqualTo(request.newEmail());
        }

        @Test
        @DisplayName("실패: 존재하지 않는 사용자 수정 시도")
        void updateFailWithNonExistentUser() {
            // Given
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userService.update(userId, request, Optional.empty()))
                .isInstanceOf(UserException.UserNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("사용자 삭제 테스트")
    class DeleteTest {

        private UUID userId;

        @BeforeEach
        void setUp() {
            userId = UUID.randomUUID();
        }

        @Test
        @DisplayName("성공: 사용자를 삭제한다")
        void deleteSuccess() {
            // Given
            given(userRepository.existsById(userId)).willReturn(true);

            // When
            userService.delete(userId);

            // Then
            then(userRepository).should().deleteById(userId);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 사용자 삭제 시도")
        void deleteFailWithNonExistentUser() {
            // Given
            given(userRepository.existsById(userId)).willReturn(false);

            // When & Then
            assertThatThrownBy(() -> userService.delete(userId))
                .isInstanceOf(UserException.UserNotFoundException.class);

            then(userRepository).should(never()).deleteById(any());
        }
    }
} 