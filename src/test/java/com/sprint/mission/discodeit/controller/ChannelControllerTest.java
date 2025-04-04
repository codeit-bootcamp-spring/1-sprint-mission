package com.sprint.mission.discodeit.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post; > Reactive WebFlux 용
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath; > restTemplate 테스트용
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ChannelService channelService;

  @Autowired
  private ObjectMapper objectMapper;

  @DisplayName("/api/channels/public - 공개 채널 생성에 성공한다.")
  @Test
  void createPublicChannel() throws Exception {

    //given
    ChannelCreatePublicDTO request = new ChannelCreatePublicDTO("채널1", "채널 1입니다.");
    ChannelDto response = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PUBLIC,
        "채널1",
        "채널 1입니다.",
        List.of(),
        Instant.now()
    );

    given(channelService.create(any(ChannelCreatePublicDTO.class))).willReturn(response);

    //when then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("채널1"))
        .andExpect(jsonPath("$.description").value("채널 1입니다."));
  }

  @DisplayName("/api/channels/public - 채널 이름이 없으면 생성에 실패한다.")
  @Test
  void createPublicChannel_failWhenNameMissing() throws Exception {
    //given
    ChannelCreatePublicDTO invalidRequest = new ChannelCreatePublicDTO("", "설명 없음");

    //when then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(jsonPath("$.details.errors").isArray());
  }

  @DisplayName("/api/channels/{id} - 채널 이름과 설명을 수정한다.")
  @Test
  void updateChannel() throws Exception {
    UUID channelId = UUID.randomUUID();
    ChannelUpdateDTO request = new ChannelUpdateDTO("새 이름", "새 설명");
    ChannelDto response = new ChannelDto(channelId, ChannelType.PUBLIC, "새 이름", "새 설명", List.of(),
        Instant.now());

    given(channelService.update(eq(channelId), any(ChannelUpdateDTO.class))).willReturn(response);

    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("새 이름"))
        .andExpect(jsonPath("$.description").value("새 설명"));
  }
  
}