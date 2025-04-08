package com.sprint.mission.discodeit.cotroller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
public class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ChannelService channelService;

  private ChannelPublicRequest channelPublicRequest;
  private ChannelDto channelDto;
  private UUID channelId;

  @BeforeEach
  void setUp() {
    channelPublicRequest = new ChannelPublicRequest("testChannel", "Channel test");
    channelId = UUID.randomUUID();

    channelDto = ChannelDto.builder()
        .id(channelId)
        .type(ChannelType.PUBLIC)
        .name("testChannel")
        .description("Channel test")
        .build();

    given(channelService.createPublicChannel(any(ChannelPublicRequest.class))).willReturn(
        channelDto);
  }

  @Test
  @DisplayName("POST /api/channels/public - 성공")
  void createPublicChannel_Success() throws Exception {
    mockMvc.perform(
        post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(channelPublicRequest))
    ).andExpect(status().isCreated());
  }

  @Test
  @DisplayName("POST /api/channels/public - 실패")
  void createPublicChannel_Fail() throws Exception {
    ChannelPublicRequest invalidRequest = new ChannelPublicRequest("", "Channel test");
    mockMvc.perform(
        post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(invalidRequest))
    ).andExpect(status().isBadRequest());
  }
}
