package com.sprint.mission.discodeit.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private UserStatusService userStatusService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	//Jackson 라이브러리에서 Instant타입을 직렬화하지 못하여 JavaTimeModule을
	//ObjectMapper에 수동을 등록
	@BeforeEach
	void setUp() {
		objectMapper.registerModule(new JavaTimeModule());
	}

	@Test
	void 사용자를_생성하면_201_응답을_반환한다() throws Exception {
		// given
		UserCreateRequest request = new UserCreateRequest("john_doe", "john.doe@example.com", "password123");
		UserDto responseDto = new UserDto(
			UUID.randomUUID(),
			"john_doe",
			"john.doe@example.com",
			null, // 프로필 정보는 null
			true  // 온라인 상태
		);

		when(userService.create(any(UserCreateRequest.class), any())).thenReturn(responseDto);

		// JSON 데이터를 MockMultipartFile로 생성
		MockMultipartFile userCreateRequestFile = new MockMultipartFile(
			"userCreateRequest",                             // @RequestPart 이름과 일치
			"userCreateRequest.json",                        // 파일 이름 (임의)
			MediaType.APPLICATION_JSON_VALUE,               // Content-Type 지정
			objectMapper.writeValueAsBytes(request)         // JSON 직렬화 데이터
		);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
				.file(userCreateRequestFile)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.username").value("john_doe"))
			.andExpect(jsonPath("$.email").value("john.doe@example.com"))
			.andExpect(jsonPath("$.online").value(true));

		verify(userService).create(any(UserCreateRequest.class), any());
	}

	@Test
	void 사용자를_생성할_때_잘못된_입력이면_400_응답을_반환한다() throws Exception {
		// given
		UserCreateRequest invalidRequest = new UserCreateRequest("", "invalid_email", "");  // 잘못된 값

		// JSON 데이터를 MockMultipartFile로 생성
		MockMultipartFile userCreateRequestFile = new MockMultipartFile(
			"userCreateRequest",
			"userCreateRequest.json",
			MediaType.APPLICATION_JSON_VALUE,
			objectMapper.writeValueAsBytes(invalidRequest)
		);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
				.file(userCreateRequestFile)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isBadRequest()); // 유효성 검증 실패

		verifyNoInteractions(userService);
	}

	@Test
	void 사용자를_업데이트하면_200_응답을_반환한다() throws Exception {
		// given
		UUID userId = UUID.randomUUID();
		UserUpdateRequest request = new UserUpdateRequest("john_doe_updated", "john.doe.updated@example.com",
			"newpassword123");
		UserDto responseDto = new UserDto(
			userId,
			"john_doe_updated",
			"john.doe.updated@example.com",
			null, // 프로필 정보는 null
			true  // 온라인 상태
		);

		when(userService.update(eq(userId), any(UserUpdateRequest.class), any())).thenReturn(responseDto);

		// JSON 데이터를 MockMultipartFile로 생성
		MockMultipartFile userUpdateRequestFile = new MockMultipartFile(
			"userUpdateRequest",
			"userUpdateRequest.json",
			MediaType.APPLICATION_JSON_VALUE,
			objectMapper.writeValueAsBytes(request)
		);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PATCH, "/api/users/{userId}", userId.toString())
				.file(userUpdateRequestFile)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value("john_doe_updated"))
			.andExpect(jsonPath("$.email").value("john.doe.updated@example.com"))
			.andExpect(jsonPath("$.online").value(true));

		verify(userService).update(eq(userId), any(UserUpdateRequest.class), any());
	}

	@Test
	void 사용자가_존재하지_않으면_업데이트_시_404_응답을_반환한다() throws Exception {
		// given
		UUID nonExistentUserId = UUID.randomUUID();
		UserUpdateRequest request = new UserUpdateRequest("unknown_user", "unknown@example.com", "password");

		when(userService.update(eq(nonExistentUserId), any(UserUpdateRequest.class), any()))
			.thenThrow(new UserNotFoundException(Map.of("userId", nonExistentUserId)));

		// JSON 데이터를 MockMultipartFile로 생성
		MockMultipartFile userUpdateRequestFile = new MockMultipartFile(
			"userUpdateRequest",
			"userUpdateRequest.json",
			MediaType.APPLICATION_JSON_VALUE,
			objectMapper.writeValueAsBytes(request)
		);

		// when & then
		mockMvc.perform(
				MockMvcRequestBuilders.multipart(HttpMethod.PATCH, "/api/users/{userId}", nonExistentUserId.toString())
					.file(userUpdateRequestFile)
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isNotFound()); // UserNotFoundException 처리

		verify(userService).update(eq(nonExistentUserId), any(UserUpdateRequest.class), any());
	}

	@Test
	void 사용자를_삭제하면_204_응답을_반환한다() throws Exception {
		// given
		UUID userId = UUID.randomUUID();

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{userId}", userId.toString()))
			.andExpect(status().isNoContent());

		verify(userService).delete(eq(userId));
	}

	@Test
	void 모든_사용자를_조회하면_200_응답을_반환한다() throws Exception {
		// given
		List<UserDto> users = List.of(
			new UserDto(UUID.randomUUID(), "john_doe", "john.doe@example.com", null, true),
			new UserDto(UUID.randomUUID(), "jane_doe", "jane.doe@example.com", null, false)
		);

		when(userService.findAll()).thenReturn(users);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].username").value("john_doe"))
			.andExpect(jsonPath("$[1].username").value("jane_doe"));

		verify(userService).findAll();
	}

	@Test
	void 사용자_상태를_업데이트하면_200_응답을_반환한다() throws Exception {
		// given
		UUID userId = UUID.randomUUID();
		UserStatusUpdateRequest request = new UserStatusUpdateRequest(Instant.now());
		UserStatusDto responseDto = new UserStatusDto(userId, userId, Instant.now());

		//when
		when(userStatusService.updateByUserId(eq(userId), any(UserStatusUpdateRequest.class)))
			.thenReturn(responseDto);

		// then
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/api/users/{userId}/userStatus", userId.toString())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.lastActiveAt").exists());

		verify(userStatusService).updateByUserId(eq(userId), any(UserStatusUpdateRequest.class));
	}
}


