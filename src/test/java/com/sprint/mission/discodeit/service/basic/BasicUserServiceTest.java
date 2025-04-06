package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private UserMapper userMapper;
	@InjectMocks
	private BasicUserService userService;

	@Test
	public void 이미_존재하는_이메일로_인해_서비스의_생성_메서드를_호출하여_이미지_없는_유저가_생성되지_않는다() throws Exception {
		//given
		String email = "email@email.com";
		given(userRepository.existsByEmail(anyString())).willReturn(true);

		//when
		UserCreateRequest request = new UserCreateRequest("유저1", email, "1234");
		Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();

		//then
		assertThatThrownBy(() -> userService.create(request, profileRequest))
			.isInstanceOf(UserAlreadyExistsException.class);

		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	public void 유저_정보가_주어졌을_때_서비스의_생성_메서드를_호출하여_이미지_없는_유저가_생성된다() throws Exception {
		//given
		User user = new User("유저1", "email@email.com", "1234", null);
		given(userRepository.save(any(User.class))).willReturn(user);
		given(userRepository.existsByEmail(anyString())).willReturn(false);
		given(userRepository.existsByUsername(anyString())).willReturn(false);
		given(userMapper.toDto(any(User.class))).willReturn(
			new UserDto(UUID.randomUUID(), "유저1", "email@email.com", null, true));

		//when
		UserCreateRequest request = new UserCreateRequest("유저1", "email@email.com", "1234");
		Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
		UserDto savedUser = userService.create(request, profileRequest);

		//then
		assertThat(savedUser.username()).isEqualTo("유저1");
		assertThat(savedUser.email()).isEqualTo("email@email.com");
		assertThat(savedUser.profile()).isNull();
		assertThat(savedUser.online()).isTrue();
	}

	@Test
	public void 이미_존재하는_이름으로_인해_서비스의_수정_메서드를_호출하여_유저의_정보가_수정되지_않는다() throws Exception {
		//given
		User user = new User("유저1", "email@email.com", "1234", null);
		String name = "유저11";
		given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(user));
		given(userRepository.existsByUsername(anyString())).willReturn(true);

		//when
		UserUpdateRequest request = new UserUpdateRequest(name, "email1@email.com", "12341");
		Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();

		//then
		assertThatThrownBy(() -> userService.update(UUID.randomUUID(), request, profileRequest))
			.isInstanceOf(UserAlreadyExistsException.class);

		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	public void 유저_정보가_주어졌을_때_서비스의_수정_메서드를_호출하여_유저의_이름_및_이메일_및_비밀번호가_수정된다() throws Exception {
		//given
		User user = new User("유저1", "email@email.com", "1234", null);
		UUID id = UUID.randomUUID();
		given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(user));
		given(userRepository.existsByEmail(anyString())).willReturn(false);
		given(userRepository.existsByUsername(anyString())).willReturn(false);
		given(userMapper.toDto(any(User.class))).willReturn(
			new UserDto(UUID.randomUUID(), "유저11", "email1@email.com", null, true));

		//when
		UserUpdateRequest request = new UserUpdateRequest("유저11", "email1@email.com", "12341");
		Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
		UserDto savedUser = userService.update(id, request, profileRequest);

		//then
		assertThat(savedUser.username()).isEqualTo("유저11");
		assertThat(savedUser.email()).isEqualTo("email1@email.com");
		assertThat(savedUser.profile()).isNull();
		assertThat(savedUser.online()).isTrue();
	}

	@Test
	public void 유저_id가_존재하지_않으므로_서비스의_삭제_메서드를_호출하여_유저가_삭제되지_않는다() throws Exception {
		//given
		UUID id = UUID.randomUUID();
		given(userRepository.existsById(any(UUID.class))).willReturn(false);

		//when

		//then
		assertThatThrownBy(() -> userService.delete(id))
			.isInstanceOf(UserNotFoundException.class);

		verify(userRepository, never()).deleteById(any(UUID.class));
	}

	@Test
	public void 유저_id가_주어졌을_때_서비스의_삭제_메서드를_호출하여_유저가_삭제된다() throws Exception {
		//given
		UUID id = UUID.randomUUID();
		given(userRepository.existsById(any(UUID.class))).willReturn(true);

		//when
		userService.delete(id);

		//then
		verify(userRepository).deleteById(id);
	}

}