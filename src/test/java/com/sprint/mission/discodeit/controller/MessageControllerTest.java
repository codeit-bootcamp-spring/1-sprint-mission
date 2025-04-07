package com.sprint.mission.discodeit.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MessageService messageService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createMessage_Success() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("Test", channelId, userId);
    MockMultipartFile requestFile = new MockMultipartFile("messageCreateRequest",
        "messageCreateRequest.json", "application/json",
        objectMapper.writeValueAsBytes(request));
    List<MockMultipartFile> attachments = List.of(
        new MockMultipartFile("attachment", "file.txt", "text/plain", "file content".getBytes()));
    MessageDto messageDto = new MessageDto(UUID.randomUUID(), Instant.now(), null,
        request.content(), channelId, null, null);

    given(messageService.create(any(MessageCreateRequest.class), any())).willReturn(messageDto);

    // when, then
    mockMvc.perform(multipart("/api/messages")
            .file(requestFile)
            .file(attachments.get(0))
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.content").value("Test"));
  }

  @Test
  void updateMessage_Success() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("Updated Message");
    MessageDto messageDto = new MessageDto(messageId, Instant.now(), null,
        request.newContent(), channelId, null, null);

    given(messageService.update(any(UUID.class), any(MessageUpdateRequest.class))).willReturn(
        messageDto);
    // when, then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("Updated Message"));
  }

  @Test
  void deleteMessage_Success() throws Exception {
    // given
    UUID messageId = UUID.randomUUID();

    // when, then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());
  }

  @Test
  void findAllMessages_Success() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    PageResponse<MessageDto> pageResponse = new PageResponse<>(
        List.of(new MessageDto(UUID.randomUUID(), Instant.now().minusSeconds(3600), null, "Message",
            channelId, null,
            null)), 0, 1, false, null);

    given(messageService.findAllByChannelId(any(UUID.class), any(Instant.class),
        any(Pageable.class))).willReturn(pageResponse);

    // when, then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("cursor", "2025-03-28T10:15:30.00Z")
            .param("page", "0")
            .param("size", "50"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").exists())
        .andExpect(jsonPath("$.content[0].content").value("Message"));
  }
}
