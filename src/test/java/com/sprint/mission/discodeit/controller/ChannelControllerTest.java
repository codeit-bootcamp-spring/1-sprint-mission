package com.sprint.mission.discodeit.controller;


import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.service.facade.channel.ChannelMasterFacade;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ChannelController.class)
public class ChannelControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ChannelMasterFacade channelMasterFacade;

  @Test
  @DisplayName("POST /api/channels/private - 비공개 채널 생성 성공")
  void createPrivateChannel_success() throws Exception {
    // given
    String channelId = UUID.randomUUID().toString();
    List<String> participantIds = List.of(UUID.randomUUID().toString(),
        UUID.randomUUID().toString());

    CreatePrivateChannelDto dto = new CreatePrivateChannelDto(participantIds);
    ChannelResponseDto response = new ChannelResponseDto(
        channelId,
        ChannelType.PRIVATE,
        null,
        null,
        null,
        Instant.EPOCH
    );

    given(channelMasterFacade.createPrivateChannel(dto))
        .willReturn(response);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/channels/private")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(dto)));

    // then
    resultActions
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.type").value("PRIVATE"));
  }

  @Test
  @DisplayName("POST /api/channels/private - 비공개 채널 생성 실패 (빈 dto)")
  void createPrivateChannel_fail() throws Exception {

    //given
    CreatePrivateChannelDto dto = new CreatePrivateChannelDto(null);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/channels/private")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(dto)));

    // then
    resultActions
        .andExpect(status().isBadRequest());

    then(channelMasterFacade).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("POST /api/channels/public - 공개 채널 생성 성공")
  void createPublicChannel_success() throws Exception {
    String channelId = UUID.randomUUID().toString();
    CreateChannelDto channelDto = new CreateChannelDto("name", "description");
    ChannelResponseDto responseDto = new ChannelResponseDto(
        channelId,
        ChannelType.PUBLIC,
        channelDto.name(),
        channelDto.description(),
        null,
        Instant.EPOCH
    );
    given(channelMasterFacade.createPublicChannel(channelDto))
        .willReturn(responseDto);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/channels/public")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(channelDto)));

    // then
    resultActions
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(responseDto.id()))
        .andExpect(jsonPath("$.name").value(responseDto.name()))
        .andExpect(jsonPath("$.type").value("PUBLIC"));
  }

  // TODO : 공개 채널 생성 실패

  @Test
  @DisplayName("PATCH /api/channels/{channelId}")
  void updateChannel_success() throws Exception {
    String channelId = UUID.randomUUID().toString();
    ChannelUpdateDto request = new ChannelUpdateDto("newName", "newDescription");
    ChannelResponseDto response = new ChannelResponseDto(
        channelId,
        ChannelType.PUBLIC,
        request.newName(),
        request.newDescription(),
        null,
        Instant.EPOCH
    );
    given(channelMasterFacade.updateChannel(channelId, request))
        .willReturn(response);

    //when
    ResultActions resultActions = mockMvc.perform(patch("/api/channels/" + channelId)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(request)));

    //then
    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(channelId))
        .andExpect(jsonPath("$.name").value("newName"));
  }

  // TODO : 채널 업데이트 실패 (파라미터 오류, private channel)
  // TODO : deleteChannel 성공, 실패
  // TODO : findChannelVisibleToUser 성공 실패
}
