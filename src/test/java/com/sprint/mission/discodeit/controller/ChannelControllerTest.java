package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ChannelService channelService;

  @Test
  @DisplayName("[성공] public 채널 생성")
  void createPublicChannel_success() throws Exception {
    // Given: 유효한 public 채널 생성 요청
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("Test Channel",
        "This is a test description.");
    ChannelDto response = new ChannelDto(UUID.randomUUID(), null, "Test Channel",
        "This is a test description.", List.of(), null);

    // When: 서비스 계층의 create 메서드가 호출되었을 때 response 반환
    when(channelService.create(any(PublicChannelCreateRequest.class))).thenReturn(response);

    // Then: 201 Created 응답과 함께 채널 이름이 응답에 포함되어야 함
    mockMvc.perform(MockMvcRequestBuilders.post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Channel"));
  }

  @Test
  @DisplayName("[실패] public 채널 생성 - 이름 누락")
  void createPublicChannel_fail_missingName() throws Exception {
    // Given: 이름이 null인 요청
    PublicChannelCreateRequest request = new PublicChannelCreateRequest(null, "Valid description.");

    // When & Then: 400 Bad Request 반환 기대
    mockMvc.perform(MockMvcRequestBuilders.post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("[성공] private 채널 생성")
  void createPrivateChannel_success() throws Exception {
    // Given: 유효한 private 채널 생성 요청
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
        List.of(UUID.randomUUID()));
    ChannelDto response = new ChannelDto(UUID.randomUUID(), null, "Private Channel", null,
        List.of(), null);

    // When: 서비스 create 호출 시 응답 반환
    when(channelService.create(any(PrivateChannelCreateRequest.class))).thenReturn(response);

    // Then: 201 Created 응답 기대
    mockMvc.perform(MockMvcRequestBuilders.post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  @DisplayName("[실패] private 채널 생성 - 멤버 없음")
  void createPrivateChannel_fail_emptyMembers() throws Exception {
    // Given: 멤버 UUID 리스트가 비어 있음
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(Collections.emptyList());

    // When & Then: 400 Bad Request 응답 기대
    mockMvc.perform(MockMvcRequestBuilders.post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("[성공] 채널 수정")
  void updateChannel_success() throws Exception {
    // Given: 수정 요청을 위한 채널 ID와 유효한 이름과 설명
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("Updated Name",
        "Updated description text.");
    ChannelDto response = new ChannelDto(channelId, null, "Updated Name", null, List.of(), null);

    // When: update 호출 시 수정된 채널 반환
    when(channelService.update(eq(channelId), any())).thenReturn(response);

    // Then: 200 OK와 이름이 변경된 응답 확인
    mockMvc.perform(MockMvcRequestBuilders.patch("/api/channels/" + channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Name"));
  }

  @Test
  @DisplayName("[실패] 채널 수정 - 이름 누락")
  void updateChannel_fail_missingName() throws Exception {
    // Given: null 이름을 포함한 요청
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(null, "Some description");

    // When & Then: 400 Bad Request 기대
    mockMvc.perform(MockMvcRequestBuilders.patch("/api/channels/" + channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("[성공] 채널 삭제")
  void deleteChannel_success() throws Exception {
    // Given: 삭제할 채널 ID 준비
    UUID channelId = UUID.randomUUID();

    // When & Then: 204 No Content 응답 기대
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/channels/" + channelId))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("[성공] 사용자 채널 목록 조회")
  void findAllChannels_success() throws Exception {
    // Given: 사용자 ID와 조회될 채널 리스트
    UUID userId = UUID.randomUUID();
    ChannelDto channel1 = new ChannelDto(UUID.randomUUID(), null, "Channel A", null, List.of(),
        null);
    ChannelDto channel2 = new ChannelDto(UUID.randomUUID(), null, "Channel B", null, List.of(),
        null);

    // When: 서비스에서 해당 채널 리스트를 반환하도록 설정
    when(channelService.findAllByUserId(userId)).thenReturn(List.of(channel1, channel2));

    // Then: 응답 상태가 200이고 채널 목록이 포함되어야 함
    mockMvc.perform(MockMvcRequestBuilders.get("/api/channels")
            .param("userId", userId.toString()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Channel A"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Channel B"));
  }

  @Test
  @DisplayName("[실패] 사용자 채널 목록 조회 - userId 누락")
  void findAllChannels_fail_missingUserId() throws Exception {
    // When & Then: 필수 파라미터 누락으로 인한 400 Bad Request 기대
    mockMvc.perform(MockMvcRequestBuilders.get("/api/channels"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
