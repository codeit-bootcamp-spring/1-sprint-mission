package com.sprint.mission.discodeit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.User.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import jakarta.persistence.EntityManager;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
	private UserRepository userRepository;

	@Mock
	private UserStatusRepository userStatusRepository;

	@Mock
	private UserMapper userMapper;

	@InjectMocks
	private BasicUserService userService; //

	@Mock
	private EntityManager entityManager;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	@Test
	public void givenValidUser_whenCreateUser_thenReturnCreatedUser() {

		UserCreateRequest request = new UserCreateRequest("johnDoe", "john@example.com", "password123");
		Optional<BinaryContentCreateRequest> optionalProfile = Optional.empty();



		UserDto result = userService.create(request, optionalProfile);

		assertEquals("johnDoe", result.username());
		assertEquals("john@example.com", result.email());
	}

	@Test
	@Rollback(false)
	public void givenInvalidUser_whenCreateUser_thenThrowException() {
		User user = new User("johnDoe", "john@example.com", "password123", null);
		userRepository.save(user);
		userRepository.flush();
		UserCreateRequest request = new UserCreateRequest("johnDoe", "john@example.com", "password123");
		Optional<BinaryContentCreateRequest> optionalProfile = Optional.empty();

		assertThrows(UserAlreadyExistException.class, () -> {
			userService.create(request, optionalProfile);
		});
	}

	@Test
	public void updateUser_Success() {
		// given
		User existingUser = new User("oldName", "oldmail@mail.com", "oldpassword", null);
		UUID existingUserId = existingUser.getId(); // 기존 유저의 UUID 저장

		UserUpdateRequest request = new UserUpdateRequest("newName", "newmail@mail.com", "newpassword");
		// User updatedUser = new User(existingUserId, request.newUsername(), request.newEmail(), request.newPassword(), null);
		//생성자는 param 4개 짜리(id는 paramX) 만 존재. 89 라인 불가

		given(userRepository.findById(existingUserId)).willReturn(Optional.of(existingUser));
		//given(userRepository.save(any(User.class))).willReturn(updatedUser);
		given(userMapper.toDto(any(User.class))).willReturn(new UserDto(existingUserId, request.newUsername(), request.newEmail(), null, true));

		// when
		UserDto result = userService.update(existingUserId, request, Optional.empty());

		// then
		assertThat(result).isNotNull();
		assertThat(result.id()).isEqualTo(existingUserId); // 업데이트된 유저의 UUID와 기존 유저의 UUID 비교
		assertThat(result.username()).isEqualTo(request.newUsername());
		assertThat(result.email()).isEqualTo(request.newEmail());
		verify(userRepository).findById(existingUserId);
		//verify(userRepository).save(any(User.class)); --> 제대로 세이브되는지 더티체크 적용여부를 확인해야 하는데, 89, 92 라인 해결법을 모르는 상황
	}


	@Test
	public void givenValidUser_whenDeleteUser_thenReturnCreatedUser() {
		Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
		UserCreateRequest userRe = new UserCreateRequest("johnDoe", "john@example.com", "password123");

		UserDto createdUser = userService.create(userRe, profileRequest);
		userService.delete(createdUser.id());
		assertThrows(UserNotFoundException.class, () -> {
			userService.find(createdUser.id());
		});

	}
}