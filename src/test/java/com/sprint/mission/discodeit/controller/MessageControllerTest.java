package com.sprint.mission.discodeit.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.file.InvalidFileDataException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.Interface.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MessageController.class)
@Import(GlobalExceptionHandler.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private MessageService messageService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createMessage() throws Exception {
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    CreateMessageRequestDto request = new CreateMessageRequestDto("test", channelId, userId);

    MessageDto responseDto = new MessageDto();
    responseDto.setId(messageId);
    responseDto.setContent("test");

    given(messageService.createMessage(ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(
        responseDto);

    MockMultipartFile jsonPart = new MockMultipartFile("messageCreateRequest", "",
        "application/json",
        objectMapper.writeValueAsBytes(request));
    MockMultipartFile attachment = new MockMultipartFile("attachments", "file.txt", "text/plain",
        "Hello World".getBytes());

    mvc.perform(multipart("/api/messages")
            .file(jsonPart)
            .file(attachment)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value("test"));
  }

  @Test
  void createMessage_첨부파일_오류() throws Exception {
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    CreateMessageRequestDto request = new CreateMessageRequestDto("내용", channelId, userId);

    given(messageService.createMessage(any(), any()))
        .willThrow(new InvalidFileDataException());

    MockMultipartFile jsonPart = new MockMultipartFile(
        "messageCreateRequest", "", "application/json", objectMapper.writeValueAsBytes(request));

    mvc.perform(multipart("/api/messages")
            .file(jsonPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("FILE_002"))
        .andExpect(jsonPath("$.message").value("파일 데이터가 유효하지 않습니다."));
  }

  @Test
  void updateMessage() throws Exception {
    UUID id = UUID.randomUUID();
    UpdateMessageRequestDto request = new UpdateMessageRequestDto("new");
    MessageDto responseDto = new MessageDto();
    responseDto.setId(id);
    responseDto.setContent("new");

    given(messageService.updateMessage(ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(
        responseDto);

    mvc.perform(patch("/api/messages/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("new"));
  }

  @Test
  void updateMessage_존재하지_않는_메시지() throws Exception {
    UUID messageId = UUID.randomUUID();
    UpdateMessageRequestDto request = new UpdateMessageRequestDto("new content");

    given(messageService.updateMessage(eq(messageId), any()))
        .willThrow(new MessageNotFoundException());

    mvc.perform(patch("/api/messages/" + messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteMessage() throws Exception {
    UUID id = UUID.randomUUID();
    mvc.perform(delete("/api/messages/" + id)).andExpect(status().isNoContent());
  }

  @Test
  void deleteMessage_권한없음() throws Exception {
    UUID messageId = UUID.randomUUID();

    willThrow(new RuntimeException("해당 메시지에 대한 권한이 없습니다."))
        .given(messageService).deleteMessage(eq(messageId));

    mvc.perform(delete("/api/messages/" + messageId))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.code").value("COMMON_500"));
  }


  @Test
  void findAllByChannelId() throws Exception {
    UUID channelId = UUID.randomUUID();
    Instant cursor = Instant.now();

    MessageDto message1 = new MessageDto();
    message1.setId(UUID.randomUUID());
    message1.setChannelId(channelId);
    message1.setContent("Hello");
    message1.setCreatedAt(Instant.now());

    MessageDto message2 = new MessageDto();
    message2.setId(UUID.randomUUID());
    message2.setChannelId(channelId);
    message2.setContent("World");
    message2.setCreatedAt(Instant.now());

    List<MessageDto> messages = List.of(message1, message2);
    Object nextCursor = message2.getCreatedAt();

    PageResponse<MessageDto> response = new PageResponse<>(
        messages,
        nextCursor,
        50,
        false,
        2L
    );

    given(messageService.findAllByChannelId(eq(channelId), any(), eq(50))).willReturn(response);

    mvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("cursor", cursor.toString())
            .param("size", "50"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.hasNext").value(false))
        .andExpect(jsonPath("$.size").value(50))
        .andExpect(jsonPath("$.totalElements").value(2));
  }

  @Test
  void findAllByChannelId_형식_잘못된_cursor() throws Exception {
    UUID channelId = UUID.randomUUID();

    mvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("cursor", "invalid-cursor-format")
            .param("size", "50"))
        .andExpect(status().is(500));
  }
}