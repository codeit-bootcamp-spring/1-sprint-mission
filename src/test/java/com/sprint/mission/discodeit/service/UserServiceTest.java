package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserStatusRepository userStatusRepository;

	@Mock
	private UserMapper userMapper;

	@Mock
	private BinaryContentRepository binaryContentRepository;

	@Mock
	private BinaryContentStorage binaryContentStorage;

	@InjectMocks
	private BasicUserService userService;

	@Test
	void 유효한_요청으로_유저_생성시_유저_DTO를_반환한다() {
		// given
		UserCreateRequest request = new UserCreateRequest("testuser", "test@example.com", "password");
		User user = new User("testuser", "test@example.com", "password", null);
		UserDto userDto = new UserDto(UUID.randomUUID(), "testuser", "test@example.com", null, true);

		given(userRepository.existsByEmail(request.email())).willReturn(false);
		given(userRepository.existsByUsername(request.username())).willReturn(false);
		given(userRepository.save(any(User.class))).willReturn(user);
		given(userMapper.toDto(any(User.class))).willReturn(userDto);

		// when
		UserDto result = userService.create(request, Optional.empty());

		// then
		assertThat(result).isNotNull();
		assertThat(result.username()).isEqualTo("testuser");
		assertThat(result.email()).isEqualTo("test@example.com");
	}

	@Test
	void 중복된_이메일로_유저_생성시_예외를_던진다() {
		// given
		UserCreateRequest request = new UserCreateRequest("testuser", "duplicate@example.com", "password");
		given(userRepository.existsByEmail(request.email())).willReturn(true);

		// when & then
		assertThrows(UserAlreadyExistsException.class, () -> userService.create(request, Optional.empty()));
	}

	@Test
	void 유효한_요청으로_유저_수정시_수정된_유저_DTO를_반환한다() {
		// given
		UUID userId = UUID.randomUUID();
		UserUpdateRequest request = new UserUpdateRequest("newusername", "newemail@example.com", "newpassword");
		User existingUser = new User("oldusername", "oldemail@example.com", "oldpassword", null);
		User updatedUser = new User("newusername", "newemail@example.com", "newpassword", null);
		UserDto updatedUserDto = new UserDto(userId, "newusername", "newemail@example.com", null, true);

		given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
		given(userRepository.existsByEmail(request.newEmail())).willReturn(false);
		given(userRepository.existsByUsername(request.newUsername())).willReturn(false);
		given(userMapper.toDto(existingUser)).willReturn(updatedUserDto);

		// when
		UserDto result = userService.update(userId, request, Optional.empty());

		// then
		assertThat(result).isNotNull();
		assertThat(result.username()).isEqualTo("newusername");
		assertThat(result.email()).isEqualTo("newemail@example.com");
	}

	@Test
	void 존재하지_않는_유저_ID로_수정시_예외를_던진다() {
		// given
		UUID userId = UUID.randomUUID();
		UserUpdateRequest request = new UserUpdateRequest("newusername", "newemail@example.com", "newpassword");

		given(userRepository.findById(userId)).willReturn(Optional.empty());

		// when & then
		assertThrows(UserNotFoundException.class, () -> userService.update(userId, request, Optional.empty()));
	}

	//Todo 생성된 유저ID를 넣어야된다.
	@Test
	void 유효한_유저_ID로_삭제시_성공한다() {
		// given
		UUID userId = UUID.randomUUID();

		given(userRepository.existsById(userId)).willReturn(true);

		// when
		userService.delete(userId);

		// then
		verify(userRepository, times(1)).deleteById(userId);
	}

	@Test
	void 존재하지_않는_유저_ID로_삭제시_예외를_던진다() {
		// given
		UUID userId = UUID.randomUUID();

		given(userRepository.existsById(userId)).willReturn(false);

		// when & then
		assertThrows(UserNotFoundException.class, () -> userService.delete(userId));
	}
}

