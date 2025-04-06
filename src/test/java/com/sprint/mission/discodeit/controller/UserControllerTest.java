package com.sprint.mission.discodeit.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;

@WebMvcTest(value = UserController.class)
class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BasicUserService userService;

	@MockitoBean
	private BasicUserStatusService userStatusService;

	@Test
	public void 유저_정보가_주어졌을_때_서비스의_생성_메서드를_통해_유저를_반환한다() throws Exception {
		//given
		UserCreateRequest userCreateRequest = new UserCreateRequest("유저", "email@email.com", "1234");
		Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
		UserDto createdUser = new UserDto(UUID.randomUUID(), "유저", "email@email.com", null, true);
		given(userService.create(any(UserCreateRequest.class), any(Optional.class))).willReturn(createdUser);

		//when
		MockMultipartFile data = new MockMultipartFile(
			"userCreateRequest",
			"",
			MediaType.APPLICATION_JSON_VALUE,
			"{\"username\":\"유저\",\"email\":\"email@email.com\",\"password\":\"1234\"}".getBytes(StandardCharsets.UTF_8)
		);
		ResultActions action = mockMvc.perform(
			MockMvcRequestBuilders.multipart("/api/users")
				.file(data)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
		);

		//then
		action.andExpect(status().isCreated())
			.andExpect(jsonPath("$.username").value("유저"))
			.andExpect(jsonPath("$.email").value("email@email.com"));

		verify(userService).create(userCreateRequest, profileRequest);
	}

	@Test
	public void 이미_존재하는_이름으로_인해_서비스의_생성_메서드를_통해_유저를_반환하지_않는다() throws Exception {
		//given
		UserCreateRequest userCreateRequest = new UserCreateRequest("유저", "email@email.com", "1234");
		Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
		given(userService.create(any(UserCreateRequest.class), any(Optional.class))).willThrow(
			new UserAlreadyExistsException("이미 존재하는 유저입니다", null));

		//when
		MockMultipartFile data = new MockMultipartFile(
			"userCreateRequest",
			"",
			MediaType.APPLICATION_JSON_VALUE,
			"{\"username\":\"유저\",\"email\":\"email@email.com\",\"password\":\"1234\"}".getBytes(StandardCharsets.UTF_8)
		);
		ResultActions action = mockMvc.perform(
			MockMvcRequestBuilders.multipart("/api/users")
				.file(data)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
		);

		//then
		action.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("이미 존재하는 유저입니다"))
			.andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));

		verify(userService).create(userCreateRequest, profileRequest);
	}
}