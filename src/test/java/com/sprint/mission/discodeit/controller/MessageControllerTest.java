package com.sprint.mission.discodeit.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MessageController.class)
class MessageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MessageService messageService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		// JavaTimeModule 등록 (Instant 직렬화/역직렬화 지원)
		objectMapper.registerModule(new JavaTimeModule());
	}

	@Test
	void 메시지를_생성하면_201_응답을_반환한다() throws Exception {
		// given
		MessageCreateRequest request = new MessageCreateRequest(
			"Hello, World!", UUID.randomUUID(), UUID.randomUUID());
		MessageDto responseDto = new MessageDto(
			UUID.randomUUID(),
			Instant.now(),
			Instant.now(),
			"Hello, World!",
			request.channelId(),
			new UserDto(request.authorId(), "john_doe", "john.doe@example.com", null, true),
			List.of()
		);

		when(messageService.create(any(MessageCreateRequest.class), any())).thenReturn(responseDto);

		// JSON 데이터를 MockMultipartFile로 생성
		MockMultipartFile messageCreateRequestFile = new MockMultipartFile(
			"messageCreateRequest",
			"messageCreateRequest.json",
			MediaType.APPLICATION_JSON_VALUE,
			objectMapper.writeValueAsBytes(request)
		);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/messages")
				.file(messageCreateRequestFile)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.content").value("Hello, World!"))
			.andExpect(jsonPath("$.channelId").value(request.channelId().toString()))
			.andExpect(jsonPath("$.author.id").value(request.authorId().toString()));

		verify(messageService).create(any(MessageCreateRequest.class), any());
	}

	@Test
	void 메시지를_생성할_때_잘못된_입력이면_400_응답을_반환한다() throws Exception {
		// given
		MessageCreateRequest invalidRequest = new MessageCreateRequest("", null, null);

		// JSON 데이터를 MockMultipartFile로 생성
		MockMultipartFile messageCreateRequestFile = new MockMultipartFile(
			"messageCreateRequest",
			"messageCreateRequest.json",
			MediaType.APPLICATION_JSON_VALUE,
			objectMapper.writeValueAsBytes(invalidRequest)
		);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/messages")
				.file(messageCreateRequestFile)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isBadRequest());

		verifyNoInteractions(messageService);
	}

	@Test
	void 메시지를_업데이트하면_200_응답을_반환한다() throws Exception {
		// given
		UUID messageId = UUID.randomUUID();
		MessageUpdateRequest request = new MessageUpdateRequest("Updated content");
		MessageDto responseDto = new MessageDto(
			messageId,
			Instant.now(),
			Instant.now(),
			"Updated content",
			UUID.randomUUID(),
			new UserDto(UUID.randomUUID(), "john_doe", "john.doe@example.com", null, true),
			List.of()
		);

		when(messageService.update(eq(messageId), any(MessageUpdateRequest.class))).thenReturn(responseDto);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.patch("/api/messages/{messageId}", messageId.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").value("Updated content"));

		verify(messageService).update(eq(messageId), any(MessageUpdateRequest.class));
	}

	@Test
	void 메시지를_삭제하면_204_응답을_반환한다() throws Exception {
		// given
		UUID messageId = UUID.randomUUID();

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/messages/{messageId}", messageId.toString()))
			.andExpect(status().isNoContent());

		verify(messageService).delete(eq(messageId));
	}

	@Test
	void 특정_채널의_모든_메시지를_조회하면_200_응답을_반환한다() throws Exception {
		// given
		UUID channelId = UUID.randomUUID();
		PageResponse<MessageDto> response = new PageResponse<>(
			List.of(
				new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(), "Message 1", channelId,
					new UserDto(UUID.randomUUID(), "john_doe", "john.doe@example.com", null, true), List.of()),
				new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(), "Message 2", channelId,
					new UserDto(UUID.randomUUID(), "jane_doe", "jane.doe@example.com", null, false), List.of())
			),
			null, // nextCursor
			2,    // size
			false, // hasNext
			2L     // totalElements (추가됨)
		);

		when(messageService.findAllByChannelId(eq(channelId), any(), any())).thenReturn(response);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/messages")
				.param("channelId", channelId.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalElements").value(2))
			.andExpect(jsonPath("$.content[0].content").value("Message 1"))
			.andExpect(jsonPath("$.content[1].content").value("Message 2"));

		verify(messageService).findAllByChannelId(eq(channelId), any(), any());
	}

}

