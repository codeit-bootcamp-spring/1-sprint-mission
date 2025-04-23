package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ChannelService channelService;

  @Test
  @DisplayName("공개 채널 생성 API 테스트")
  void createPublicChannel() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    PublicChannelCreateRequest request = new PublicChannelCreateRequest();
    request.setName("테스트 공개 채널");
    request.setDescription("테스트 설명");

    ChannelDto responseDto = ChannelDto.builder()
        .id(channelId)
        .name("테스트 공개 채널")
        .description("테스트 설명")
        .type("PUBLIC")
        .participants(new ArrayList<>())
        .build();

    given(channelService.createPublicChannel(any(PublicChannelCreateRequest.class)))
        .willReturn(responseDto);

    // When & Then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value("테스트 공개 채널"))
        .andExpect(jsonPath("$.description").value("테스트 설명"))
        .andExpect(jsonPath("$.type").value("PUBLIC"));
  }

  @Test
  @DisplayName("비공개 채널 생성 API 테스트")
  void createPrivateChannel() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();

    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest();
    request.setParticipantIds(Arrays.asList(userId1, userId2));

    List<UserDto> participants = Arrays.asList(
        UserDto.builder().id(userId1).name("User 1").username("user1").build(),
        UserDto.builder().id(userId2).name("User 2").username("user2").build()
    );

    ChannelDto responseDto = ChannelDto.builder()
        .id(channelId)
        .name("User 1, User 2")
        .description("비공개 채널")
        .type("PRIVATE")
        .participants(participants)
        .build();

    given(channelService.createPrivateChannel(any(PrivateChannelCreateRequest.class)))
        .willReturn(responseDto);

    // When & Then
    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value("User 1, User 2"))
        .andExpect(jsonPath("$.type").value("PRIVATE"))
        .andExpect(jsonPath("$.participants", hasSize(2)))
        .andExpect(jsonPath("$.participants[0].id").value(userId1.toString()))
        .andExpect(jsonPath("$.participants[1].id").value(userId2.toString()));
  }

  @Test
  @DisplayName("채널 정보 수정 API 테스트")
  void updateChannel() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest();
    request.setNewName("수정된 채널명");
    request.setNewDescription("수정된 설명");

    ChannelDto responseDto = ChannelDto.builder()
        .id(channelId)
        .name("수정된 채널명")
        .description("수정된 설명")
        .type("PUBLIC")
        .participants(new ArrayList<>())
        .build();

    given(channelService.updateChannel(eq(channelId), any(PublicChannelUpdateRequest.class)))
        .willReturn(responseDto);

    // When & Then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value("수정된 채널명"))
        .andExpect(jsonPath("$.description").value("수정된 설명"));
  }

  @Test
  @DisplayName("채널 삭제 API 테스트")
  void deleteChannel() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    doNothing().when(channelService).delete(channelId);

    // When & Then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("사용자별 채널 목록 조회 API 테스트")
  void getChannelsByUser() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    UUID channelId1 = UUID.randomUUID();
    UUID channelId2 = UUID.randomUUID();

    List<ChannelDto> channelDtos = Arrays.asList(
        ChannelDto.builder()
            .id(channelId1)
            .name("채널 1")
            .description("설명 1")
            .type("PUBLIC")
            .participants(new ArrayList<>())
            .build(),
        ChannelDto.builder()
            .id(channelId2)
            .name("채널 2")
            .description("설명 2")
            .type("PRIVATE")
            .participants(Arrays.asList(
                UserDto.builder().id(userId).name("User").username("user").build()
            ))
            .build()
    );

    given(channelService.findAllByUserId(userId)).willReturn(channelDtos);

    // When & Then
    mockMvc.perform(get("/api/channels").param("userId", userId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value(channelId1.toString()))
        .andExpect(jsonPath("$[0].name").value("채널 1"))
        .andExpect(jsonPath("$[0].type").value("PUBLIC"))
        .andExpect(jsonPath("$[1].id").value(channelId2.toString()))
        .andExpect(jsonPath("$[1].name").value("채널 2"))
        .andExpect(jsonPath("$[1].type").value("PRIVATE"));
  }

  @Test
  @DisplayName("채널 상세 정보 조회 API 테스트")
  void getChannelDetails() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();

    List<UserDto> participants = Arrays.asList(
        UserDto.builder().id(userId).name("User").username("user").build()
    );

    ChannelDto responseDto = ChannelDto.builder()
        .id(channelId)
        .name("테스트 채널")
        .description("테스트 설명")
        .type("PUBLIC")
        .participants(participants)
        .build();

    given(channelService.findById(channelId)).willReturn(responseDto);

    // When & Then
    mockMvc.perform(get("/api/channels/{channelId}", channelId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value("테스트 채널"))
        .andExpect(jsonPath("$.description").value("테스트 설명"))
        .andExpect(jsonPath("$.type").value("PUBLIC"))
        .andExpect(jsonPath("$.participants", hasSize(1)))
        .andExpect(jsonPath("$.participants[0].id").value(userId.toString()));
  }

  @Test
  @DisplayName("존재하지 않는 채널 조회 시 예외 발생 테스트")
  void getChannelDetails_NotFound() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    given(channelService.findById(channelId))
        .willThrow(new RestApiException(DomainErrorCode.CHANNEL_NOT_FOUND));

    // When & Then
    mockMvc.perform(get("/api/channels/{channelId}", channelId))
        .andExpect(status().isNotFound());
  }
} 