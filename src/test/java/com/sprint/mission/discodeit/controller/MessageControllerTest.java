package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MessageController.class)
@Import({GlobalExceptionHandler.class})
class MessageControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  MessageService messageService;

  @Test
  @DisplayName("createMessageWithAttachment")
  void createMessageWithAttachment() throws Exception {
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    String content = "message";
    MessageCreateRequest request = new MessageCreateRequest(
        authorId,
        content,
        channelId
    );
    MockMultipartFile messageCreateRequest = new MockMultipartFile(
        "messageCreateRequest",
        null,
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );
    MockMultipartFile attachment = new MockMultipartFile(
        "profile",
        "profile.jpg",
        "imege/jpeg",
        "file".getBytes()
    );
    BinaryContentDto binaryContentDto = new BinaryContentDto(
        UUID.randomUUID(),
        attachment.getSize(),
        attachment.getName(),
        attachment.getContentType()
    );
    MessageDto messageDto = new MessageDto(
        messageId,
        Instant.now(),
        Instant.now(),
        content,
        new UserDto(authorId, null, null, null, false),
        channelId,
        List.of(binaryContentDto)
    );

    given(messageService.createMessage(any(), any())).willReturn(messageDto);

    mockMvc.perform(multipart("/api/messages")
            .file(messageCreateRequest)
            .file(attachment)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value(content));
  }

  @Test
  @DisplayName("createMessage")
  void createMessage() throws Exception {
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    String content = "message";
    MessageCreateRequest request = new MessageCreateRequest(
        authorId,
        content,
        channelId
    );
    MockMultipartFile messageCreateRequest = new MockMultipartFile(
        "messageCreateRequest",
        null,
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );
    MessageDto messageDto = new MessageDto(
        messageId,
        Instant.now(),
        Instant.now(),
        content,
        new UserDto(authorId, null, null, null, false),
        channelId,
        List.of()
    );

    given(messageService.createMessage(any(), any())).willReturn(messageDto);

    mockMvc.perform(multipart("/api/messages")
            .file(messageCreateRequest)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value(content));
  }

  @Test
  @DisplayName("getMessages")
  void getMessages() throws Exception {
    UUID channelId = UUID.randomUUID();
    Instant cursor = Instant.now();
    MessageDto messageDto = new MessageDto(
        UUID.randomUUID(),
        cursor.minusSeconds(10),
        cursor.minusSeconds(10),
        "message",
        new UserDto(UUID.randomUUID(), null, null, null, false),
        channelId,
        List.of()
    );
    PageResponse<MessageDto> pageResponse = new PageResponse<>(
        List.of(messageDto),
        cursor,
        10,
        false,
        null
    );

    given(messageService.readAllByChannelId(any(), any(), any())).willReturn(pageResponse);

    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("cursor", cursor.toString())
            .param("size", "0")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].content").value("message"))
        .andExpect(jsonPath("$.content[0].channelId").value(channelId.toString()))
        .andExpect(jsonPath("$.nextCursor").value(cursor.toString()))
        .andExpect(jsonPath("$.hasNext").value(false))
        .andExpect(jsonPath("$.size").value(10));
  }

  @Test
  @DisplayName("updateMessage")
  void updateMessage() throws Exception {
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("newMessage");
    MessageDto messageDto = new MessageDto(
        messageId,
        Instant.now(),
        Instant.now(),
        "newMessage",
        new UserDto(UUID.randomUUID(), null, null, null, false),
        UUID.randomUUID(),
        List.of()
    );
    given(messageService.updateMessage(any(), any())).willReturn(messageDto);

    mockMvc.perform(patch("/api/messages/" + messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("newMessage"));
  }

  @Test
  @DisplayName("deleteMessage")
  void deleteMessage() throws Exception {
    UUID messageId = UUID.randomUUID();

    mockMvc.perform(delete("/api/messages/" + messageId))
        .andExpect(status().isNoContent());
  }
}