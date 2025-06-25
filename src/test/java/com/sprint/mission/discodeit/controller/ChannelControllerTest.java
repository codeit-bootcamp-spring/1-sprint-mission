package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel.Type;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ChannelController.class)
@Import({GlobalExceptionHandler.class})
class ChannelControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  ChannelService channelService;

  @Test
  @DisplayName("createPublicChannel")
  void createPublicChannel() throws Exception {
    PublicChannelRequest request = new PublicChannelRequest("public",
        "public channel");
    UUID channelId = UUID.randomUUID();
    ChannelDto channelDto = new ChannelDto(
        channelId,
        Type.PUBLIC,
        request.name(),
        request.description(),
        null,
        Instant.now()
    );

    given(channelService.createPublicChannel(any())).willReturn(channelDto);

    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value(request.name()));
  }

  @Test
  @DisplayName("createPrivateChannel")
  void createPrivateChannel() throws Exception {
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();
    PrivateChannelRequest request = new PrivateChannelRequest(List.of(userId1, userId2));
    UUID channelId = UUID.randomUUID();
    ChannelDto channelDto = new ChannelDto(
        channelId,
        Type.PRIVATE,
        null,
        null,
        null,
        Instant.now()
    );

    given(channelService.createPrivateChannel(any())).willReturn(channelDto);

    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("updatePublicChannel")
  void updatePublicChannel() throws Exception {
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(
        "newname",
        "newDescription"
    );
    UUID channelId = UUID.randomUUID();
    ChannelDto channelDto = new ChannelDto(
        channelId,
        Type.PUBLIC,
        request.newName(),
        request.newDescription(),
        null,
        Instant.now()
    );

    given(channelService.updateChannel(any(), any())).willReturn(channelDto);

    mockMvc.perform(patch("/api/channels/" + channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value(request.newName()))
        .andExpect(jsonPath("$.description").value(request.newDescription()));
  }

  @Test
  @DisplayName("deleteChannel")
  void deleteChannel() throws Exception {
    UUID channelId = UUID.randomUUID();

    mockMvc.perform(delete("/api/channels/" + channelId)
        )
        .andExpect(status().isNoContent());
  }


}