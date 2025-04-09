package com.sprint.mission.discodeit.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ChannelService channelService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void 공개_채널을_생성하면_201_응답을_반환한다() throws Exception {
		// given
		PublicChannelCreateRequest request = new PublicChannelCreateRequest("General", "Public channel for all");
		ChannelDto responseDto = new ChannelDto(
			UUID.randomUUID(),
			ChannelType.PUBLIC,
			"General",
			"Public channel for all",
			List.of(), // 빈 참여자 목록
			Instant.now() // 현재 시간
		);

		when(channelService.create(any(PublicChannelCreateRequest.class))).thenReturn(responseDto);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/channels/public")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name").value("General"))
			.andExpect(jsonPath("$.type").value("PUBLIC"))
			.andExpect(jsonPath("$.description").value("Public channel for all"));

		verify(channelService).create(any(PublicChannelCreateRequest.class));
	}

	@Test
	void 잘못된_입력으로_공개_채널을_생성하면_400_응답을_반환한다() throws Exception {
		// given
		PublicChannelCreateRequest invalidRequest = new PublicChannelCreateRequest("", ""); // 빈 값

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/channels/public")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest)))
			.andExpect(status().isBadRequest()); // 유효성 검증 실패

		verifyNoInteractions(channelService);
	}

	@Test
	void 채널을_업데이트하면_200_응답을_반환한다() throws Exception {
		// given
		UUID channelId = UUID.randomUUID();
		PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("Updated Channel", "Updated description");
		ChannelDto responseDto = new ChannelDto(
			channelId,
			ChannelType.PUBLIC,
			"Updated Channel",
			"Updated description",
			List.of(), // 빈 참여자 목록 추가
			Instant.now() // 마지막 메시지 시간 추가
		);

		when(channelService.update(eq(channelId), any(PublicChannelUpdateRequest.class))).thenReturn(responseDto);

		// when & then
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/api/channels/{channelId}", channelId.toString()) // UUID to String 변환
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("Updated Channel"))
			.andExpect(jsonPath("$.description").value("Updated description"));

		verify(channelService).update(eq(channelId), any(PublicChannelUpdateRequest.class));
	}

	@Test
	void 존재하지_않는_채널을_업데이트하면_404_응답을_반환한다() throws Exception {
		// given
		UUID nonExistentChannelId = UUID.randomUUID();
		PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("Updated Channel", "Updated description");

		when(channelService.update(eq(nonExistentChannelId), any(PublicChannelUpdateRequest.class)))
			.thenThrow(new ChannelNotFoundException(Map.of("channelId", nonExistentChannelId)));

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.patch("/api/channels/{channelId}", nonExistentChannelId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNotFound());

		verify(channelService).update(eq(nonExistentChannelId), any(PublicChannelUpdateRequest.class));
	}

	@Test
	void 채널을_삭제하면_204_응답을_반환한다() throws Exception {
		// given
		UUID channelId = UUID.randomUUID();

		// when & then
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/channels/{channelId}", channelId.toString())) // UUID -> toString()
			.andExpect(status().isNoContent());

		verify(channelService).delete(eq(channelId));
	}

	@Test
	void 사용자의_모든_채널을_조회하면_200_응답을_반환한다() throws Exception {
		// given
		UUID userId = UUID.randomUUID();
		List<ChannelDto> channels = List.of(
			new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "General", "Public chat", List.of(), Instant.now()),
			new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE, "Secret", "Private chat", List.of(), Instant.now())
		);

		when(channelService.findAllByUserId(eq(userId))).thenReturn(channels);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/channels")
				.param("userId", userId.toString())) // UUID -> toString()
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].name").value("General"))
			.andExpect(jsonPath("$[1].name").value("Secret"));

		verify(channelService).findAllByUserId(eq(userId));
	}
}

