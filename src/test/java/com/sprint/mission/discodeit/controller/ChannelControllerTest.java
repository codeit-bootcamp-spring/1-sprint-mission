package com.sprint.mission.discodeit.controller;


import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.TestConfig;
import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.channel.create.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelModifyRequest;
import com.sprint.mission.discodeit.entity.role.Role;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
@Import(TestConfig.class)
class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ChannelService channelService;

  @Test
  @DisplayName("POST /api/channels - 공개 채널 생성")
  void createPubCh() throws Exception {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("ch1", "channel");
    String requestJson = objectMapper.writeValueAsString(request);

    ChannelDto channelDto = new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC,
        "ch1",
        "channel",
        null,
        Instant.now()
    );
    when(channelService.createPublic(any(PublicChannelCreateRequest.class)))
        .thenReturn(channelDto);

    mockMvc
        .perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(content().json(objectMapper.writeValueAsString(channelDto)));
  }

  @Test
  @DisplayName("POST /api/channels - 비공개 채널 생성")
  void createPriCh() throws Exception {

    UUID user1Id = UUID.randomUUID();
    UUID user2Id = UUID.randomUUID();
    UUID user3Id = UUID.randomUUID();
    List<UUID> ids = List.of(user1Id, user2Id, user3Id);
    List<UserDto> userDtos = List.of(
        new UserDto(user1Id, "user1", "user1@mail.com", null, true, Role.ROLE_USER),
        new UserDto(user2Id, "user2", "user2@mail.com", null, true, Role.ROLE_USER),
        new UserDto(user3Id, "user3", "user3@mail.com", null, true, Role.ROLE_USER)
    );
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(ids);
    String requestJson = objectMapper.writeValueAsString(request);

    ChannelDto channelDto = new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE,
        null, null,
        userDtos,
        Instant.now()
    );
    when(channelService.createPrivate(any(PrivateChannelCreateRequest.class)))
        .thenReturn(channelDto);

    mockMvc
        .perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(content().json(objectMapper.writeValueAsString(channelDto)));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  @DisplayName("DELETE /api/channels/{channelId} - 채널 삭제")
  void deleteChannel() throws Exception {
    UUID channelId = UUID.randomUUID();

    doNothing().when(channelService).remove(channelId);

    mockMvc
        .perform(delete("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
        )
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("DELETE /api/channels/{channelId} - 채널 수정")
  void modifyChannel() throws Exception {
    UUID channelId = UUID.randomUUID();
    ChannelModifyRequest request = new ChannelModifyRequest("newCh", "newDescription");
    String requestJson = objectMapper.writeValueAsString(request);

    ChannelDto channelDto = new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC,
        "newCh",
        "newDescription",
        null,
        Instant.now()
    );
    when(channelService.update(any(UUID.class), any(ChannelModifyRequest.class)))
        .thenReturn(channelDto);

    mockMvc
        .perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
        )
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(channelDto)));
  }
}