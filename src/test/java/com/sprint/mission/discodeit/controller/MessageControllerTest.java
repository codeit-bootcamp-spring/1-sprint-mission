package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.facade.message.MessageFacade;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private MessageFacade messageFacade;
  private MessageResponseDto sampleMessageResponse;
  private String sampleMessageId;
  private String sampleChannelId;


  @BeforeEach
  void setUp() {
    sampleMessageId = UUID.randomUUID().toString();
    sampleChannelId = UUID.randomUUID().toString();
    sampleMessageResponse = new MessageResponseDto(
        sampleMessageId,
        Instant.now(),
        null,
        "sample content",
        sampleChannelId,
        null,
        null
    );
  }

  @Test
  @DisplayName("POST /api/messages - 성공")
  void sendMessage_success() throws Exception {
    CreateMessageDto request = new CreateMessageDto("new content", UUID.randomUUID().toString(),
        UUID.randomUUID().toString());
    String jsonRequest = objectMapper.writeValueAsString(request);
    MockMultipartFile messagePart = new MockMultipartFile(
        "messageCreateRequest", "", "application/json", jsonRequest.getBytes()
    );

    MockMultipartFile emptyAttachments = new MockMultipartFile(
        "attachments",
        "empty.txt",
        MediaType.TEXT_PLAIN_VALUE,
        new byte[0]
    );
    MessageResponseDto responseDto = mock(MessageResponseDto.class);
    given(messageFacade.createMessage(eq(request), any()))
        .willReturn(responseDto);

    mockMvc.perform(
            multipart("/api/messages")
                .file(messagePart)
                .file(emptyAttachments)
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("POST /api/messages - 실패 (AuthorId 명시 x)")
  void createMessage_fail() throws Exception {
    CreateMessageDto request = new CreateMessageDto("new content", UUID.randomUUID().toString(),
        null);
    String jsonRequest = objectMapper.writeValueAsString(request);
    MockMultipartFile messagePart = new MockMultipartFile(
        "messageCreateRequest", "", "application/json", jsonRequest.getBytes()
    );

    MockMultipartFile emptyAttachments = new MockMultipartFile(
        "attachments",
        "empty.txt",
        MediaType.TEXT_PLAIN_VALUE,
        new byte[0]
    );
    mockMvc.perform(
            multipart("/api/messages")
                .file(messagePart)
                .file(emptyAttachments)
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("PATCH /api/messages/{id} - 성공")
  void updateMessage_success() throws Exception {
    String id = UUID.randomUUID().toString();
    MessageUpdateDto updateDto = new MessageUpdateDto("updated");
    MessageResponseDto response = new MessageResponseDto(id, Instant.now(), Instant.now(),
        "updated", "cid", null, null);

    given(messageFacade.updateMessage(eq(id), eq(updateDto))).willReturn(response);

    mockMvc.perform(patch("/api/messages/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(updateDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id));
  }

  // TODO : updateMessage 실패 케이스


  @Test
  @DisplayName("DELETE /api/messages/{id} - 성공")
  void deleteMessage_success() throws Exception {
    mockMvc.perform(delete("/api/messages/" + UUID.randomUUID()))
        .andExpect(status().isNoContent());
  }

  // TODO : DeleteMessage 실패 케이스

  @Test
  @DisplayName("GET /api/messages - 성공")
  void getMessages_success() throws Exception {
    String channelId = UUID.randomUUID().toString();
    MessageResponseDto responseDto = new MessageResponseDto(UUID.randomUUID().toString(),
        Instant.now(), Instant.now(), "content", UUID.randomUUID().toString(), null, null);
    PageResponse<MessageResponseDto> page = new PageResponse<>(List.of(responseDto), null, 1, false,
        1L);
    given(messageFacade.findMessagesByChannel(eq(channelId), any(), any())).willReturn(page);

    mockMvc.perform(get("/api/messages").param("channelId", channelId))
        .andExpect(status().isOk());
  }
  // TODO : getChannelMessages 실패 케이스

  // TODO : 성공 실패 예외 및 나머지 메서드 테스트
}
