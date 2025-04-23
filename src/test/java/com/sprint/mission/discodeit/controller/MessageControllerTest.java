package com.sprint.mission.discodeit.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.message.MessageContentUpdateRequest;
import com.sprint.mission.discodeit.entity.message.MessageCreateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(MessageController.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MessageService messageService;

  @Test
  @DisplayName("POST /api/messages - 메세지 생성")
  void createMessage() throws Exception {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("content", channelId, authorId);
    String requestJson = objectMapper.writeValueAsString(request);

    MessageDto messageDto = new MessageDto(UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        "content",
        channelId,
        new UserDto(authorId, "user", "user@mail.com", null, true),
        null
    );
    when(messageService.create(any(MessageCreateRequest.class), isNull()))
        .thenReturn(messageDto);

    MockMultipartFile requestPart = new MockMultipartFile(
        "messageCreateRequest",
        null,
        "application/json",
        requestJson.getBytes()
    );

    mockMvc
        .perform(multipart("/api/messages")
            .file(requestPart))
        .andExpect(status().isCreated())
        .andExpect(content().json(objectMapper.writeValueAsString(messageDto)));
  }

  @Test
  @DisplayName("DELETE /api/messages{messageId} - 메세지 삭제")
  void deleteMessage() throws Exception {
    UUID messageId = UUID.randomUUID();

    doNothing().when(messageService).remove(messageId);

    mockMvc
        .perform(delete("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("PATCH /api/messages/{messageId} - 메세지 수정")
  void modifyMessage() throws Exception {
    UUID messageId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageContentUpdateRequest request = new MessageContentUpdateRequest("newContent");
    String requestJson = objectMapper.writeValueAsString(request);

    MessageDto messageDto = new MessageDto(UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        "content",
        channelId,
        new UserDto(authorId, "user", "user@mail.com", null, true),
        null
    );
    when(messageService.update(any(UUID.class), any(MessageContentUpdateRequest.class)))
        .thenReturn(messageDto);

    mockMvc
        .perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson.getBytes()))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(messageDto)));
  }
}