package com.sprint.mission.discodeit;

import static org.junit.jupiter.api.Assertions.*;
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

		UserCreateRequest request = new UserCreateRequest("john_doe", "john@example.com", "password123");
		Optional<BinaryContentCreateRequest> optionalProfile = Optional.empty();



		UserDto result = userService.create(request, optionalProfile);

		assertEquals("john_doe", result.username());
		assertEquals("john@example.com", result.email());
	}

	@Test
	@Rollback(false)
	public void givenInvalidUser_whenCreateUser_thenThrowException() {
		User user = new User("john_doe", "john@example.com", "password123", null);
		userRepository.save(user);
		userRepository.flush();
		UserCreateRequest request = new UserCreateRequest("john_doe", "john@example.com", "password123");
		Optional<BinaryContentCreateRequest> optionalProfile = Optional.empty();

	}

	@Test
	public void givenValidUser_whenUpdateUser_thenReturnCreatedUser() {
		UserUpdateRequest request = new UserUpdateRequest("newName", "newmail@mail.com", "newpassowrd");

		UUID testId = UUID.fromString("6510afab-6af5-48ee-bda1-414119604bd1");

		User findUser = new User(testId, "oldName", "oldmail@mail.com", "oldpassowrd", null);

		when(userRepository.existsByEmail("newmail@mail.com")).thenReturn(false);
		when(userRepository.existsByUsername("newName")).thenReturn(false);

		when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(findUser));

		doNothing().when(entityManager).flush();
		doNothing().when(entityManager).clear();

		UserDto updatedUser = userService.update(testId, request, Optional.empty());
		assertNotNull(updatedUser);
		assertEquals("newName", updatedUser.username());
		assertEquals("newmail@mail.com", updatedUser.email());
		verify(userRepository).findById(testId);
		verify(entityManager).flush();
		verify(entityManager).clear();
	}

	@Test
	public void givenValidUser_whenDeleteUser_thenReturnCreatedUser() {
		Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
		UserCreateRequest userRe = new UserCreateRequest("김승", "kk@example.com", "1234");

		UserDto createdUser = userService.create(userRe, profileRequest);
		userService.delete(createdUser.id());


	}
}
