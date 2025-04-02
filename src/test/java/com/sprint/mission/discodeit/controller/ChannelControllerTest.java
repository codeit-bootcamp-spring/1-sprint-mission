package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.service.Interface.ChannelService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ChannelController.class)
@Import(GlobalExceptionHandler.class)
class ChannelControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private ChannelService channelService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createChannel_public() throws Exception {
    PublicChannelCreateRequestDto request = new PublicChannelCreateRequestDto("public", "test");

    ChannelDto responseDto = new ChannelDto();
    responseDto.setName("public");
    responseDto.setDescription("test");
    responseDto.setType(ChannelType.PUBLIC);

    given(channelService.createPublicChannel(any(PublicChannelCreateRequestDto.class))).willReturn(
        responseDto);

    mvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("public"))
        .andExpect(jsonPath("$.type").value("PUBLIC"))
        .andExpect(jsonPath("$.description").value("test"));
  }

  @Test
  void createChannel_private() throws Exception {
    UUID user1 = UUID.randomUUID();
    UUID user2 = UUID.randomUUID();
    List<UUID> users = List.of(user1, user2);
    PrivateChannelCreateRequestDto request = new PrivateChannelCreateRequestDto(users);
    ChannelDto responseDto = new ChannelDto();
    responseDto.setName("private");
    responseDto.setType(ChannelType.PRIVATE);

    given(
        channelService.createPrivateChannel(any(PrivateChannelCreateRequestDto.class))).willReturn(
        responseDto);

    mvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("private"))
        .andExpect(jsonPath("$.type").value("PRIVATE"));
  }

  @Test
  void 공채_채널_유효성_실패() throws Exception {
    PublicChannelCreateRequestDto invalidRequest = new PublicChannelCreateRequestDto("", "desc");

    mvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
  }

  @Test
  void 비공개_채널_실패() throws Exception {
    PrivateChannelCreateRequestDto request =
        new PrivateChannelCreateRequestDto(List.of(UUID.randomUUID(), UUID.randomUUID()));

    given(channelService.createPrivateChannel(any()))
        .willThrow(new NoSuchElementException());

    mvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("COMMON_404"));
  }

  @Test
  void updateChannel() throws Exception {
    UUID channelId = UUID.randomUUID();
    ChannelUpdateRequestDto request = new ChannelUpdateRequestDto("nweChannel", "");
    ChannelDto responseDto = new ChannelDto();
    responseDto.setId(channelId);
    responseDto.setName("nweChannel");
    responseDto.setType(ChannelType.PUBLIC);

    given(
        channelService.updateChannel(eq(channelId), any(ChannelUpdateRequestDto.class))).willReturn(
        responseDto);

    mvc.perform(patch("/api/channels/" + channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("nweChannel"))
        .andExpect(jsonPath("$.type").value("PUBLIC"));
  }

  @Test
  void 존재하지_않는_채널_업데이트() throws Exception {
    UUID channelId = UUID.randomUUID();
    ChannelUpdateRequestDto request = new ChannelUpdateRequestDto("name", "desc");

    given(channelService.updateChannel(eq(channelId), any()))
        .willThrow(new ChannelNotFoundException());

    mvc.perform(patch("/api/channels/" + channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("CHANNEL_001"))
        .andExpect(jsonPath("$.message").value("채널을 찾을 수 없습니다."));
  }

  @Test
  void 비공개_채널_수정_뿔가() throws Exception {
    UUID channelId = UUID.randomUUID();
    ChannelUpdateRequestDto request = new ChannelUpdateRequestDto("newName", "newDesc");

    given(channelService.updateChannel(eq(channelId), any()))
        .willThrow(new PrivateChannelUpdateException());

    mvc.perform(patch("/api/channels/" + channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.code").value("CHANNEL_002"))
        .andExpect(jsonPath("$.message").value("비공개 채널은 수정할 수 없습니다."));
  }

  @Test
  void deleteChannel() throws Exception {
    UUID channelId = UUID.randomUUID();
    mvc.perform(delete("/api/channels/" + channelId)).andExpect(status().isNoContent());
  }

  @Test
  void findAll() throws Exception {
    UUID user1 = UUID.randomUUID();
    ChannelDto dto = new ChannelDto();
    dto.setId(UUID.randomUUID());
    dto.setType(ChannelType.PUBLIC);
    dto.setName("public");
    ChannelDto dto2 = new ChannelDto();
    dto2.setId(UUID.randomUUID());
    dto2.setType(ChannelType.PRIVATE);
    dto2.setName("private");

    given(channelService.findAllByUserId(user1)).willReturn(List.of(dto, dto2));
    mvc.perform(get("/api/channels")
            .param("userId", user1.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  void 잘못된_아이디로_조회_All() throws Exception {
    mvc.perform(get("/api/channels")
            .param("userId", "invalid-uuid"))
        .andExpect(status().is(500))
        .andExpect(jsonPath("$.code").value("COMMON_500"));
  }
}