package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import java.nio.charset.StandardCharsets;
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

@WebMvcTest(MessageController.class)
@Import(GlobalExceptionHandler.class)
public class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private MessageService messageService;

  @MockitoBean
  private AuthController authController;

  @MockitoBean
  private BinaryContentController binaryContentController;

  @MockitoBean
  private ChannelController channelController;

  @MockitoBean
  private ReadStatusController readStatusController;

  @MockitoBean
  private UserController userController;

  @Test
  @DisplayName("POST /messages - 성공")
  void createMessageSuccess() throws Exception {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    UserDto author = new UserDto(authorId, "yubin", "yubin@codeit.com", null, true);

    MessageDto response = new MessageDto(messageId, Instant.now(), null, "Hello!", channelId,
        author, List.of());

    String messageJson = String.format("""
        {
          "channelId": "%s",
          "authorId": "%s",
          "content": "Hello!"
        }
        """, channelId, authorId);

    MockMultipartFile messagePart = new MockMultipartFile(
        "messageCreateRequest",
        "message.json",
        MediaType.APPLICATION_JSON_VALUE,
        messageJson.getBytes(StandardCharsets.UTF_8)
    );

    MockMultipartFile attachmentPart = new MockMultipartFile(
        "attachments",
        "file.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Test File Content".getBytes()
    );

    when(messageService.create(any(MessageCreateRequest.class), any()))
        .thenReturn(response);

    mockMvc.perform(multipart("/api/messages")
            .file(messagePart)
            .file(attachmentPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.channelId").value(channelId.toString()))
        .andExpect(jsonPath("$.author.username").value("yubin"))
        .andExpect(jsonPath("$.content").value("Hello!"));
  }

  @Test
  @DisplayName("POST /messages - 실패 (채널을 찾을 수 없음)")
  void createMessageFail_ChannelNotFound() throws Exception {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    when(messageService.create(any(MessageCreateRequest.class), anyList()))
        .thenThrow(new ChannelNotFoundException());

    String messageJson = String.format("""
        {
          "channelId": "%s",
          "authorId": "%s",
          "content": "Hello world!"
        }
        """, channelId, authorId);

    MockMultipartFile messagePart = new MockMultipartFile(
        "messageCreateRequest",
        "message.json",
        MediaType.APPLICATION_JSON_VALUE,
        messageJson.getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(multipart("/api/messages")
            .file(messagePart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("채널을 찾을 수 없습니다."));
  }

  @Test
  @DisplayName("DELETE /messages/ - 성공")
  void deleteMessageSuccess() throws Exception {
    UUID messageId = UUID.randomUUID();

    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("DELETE /api/messages/{messageId} - 실패 (메시지를 찾을 수 없음)")
  void deleteMessageFail() throws Exception {
    UUID messageId = UUID.randomUUID();

    doThrow(new MessageNotFoundException()).when(messageService).delete(messageId);

    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("메세지를 찾을 수 없습니다."));
  }

}
