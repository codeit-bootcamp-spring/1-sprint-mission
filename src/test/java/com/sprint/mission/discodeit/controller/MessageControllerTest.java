package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.PageResponse;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
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

  @MockitoBean
  private MessageService messageService;

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  @DisplayName("/api/messages - 첨부파일 없이 메시지를 생성할 수 있다.")
  void createMessage_withoutAttachments() throws Exception {
    //given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    MessageCreateDTO request = new MessageCreateDTO(
        "안녕하세요",
        channelId,
        authorId
    );

    MessageDto response = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        "안녕하세요",
        request.getChannelId(),
        new UserDto(authorId, "user1", "user1@abc.com", null, true),
        List.of()
    );

    MockMultipartFile messagePart = new MockMultipartFile(
        "messageCreateRequest", null,
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );

    given(messageService.create(any(), any())).willReturn(response);

    //when then
    mockMvc.perform(multipart("/api/messages")
            .file(messagePart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value("안녕하세요"))
        .andExpect(jsonPath("$.channelId").value(request.getChannelId().toString()))
        .andExpect(jsonPath("$.author.id").value(request.getAuthorId().toString()));
  }

  @Test
  @DisplayName("/api/messages - 메시지 내용이 비어있으면 VALIDATION_FAILED 에러를 반환한다.")
  void createMessage_validationFailed() throws Exception {
    //given
    MessageCreateDTO invalidRequest = new MessageCreateDTO(
        "", // content 비어있음
        UUID.randomUUID(),
        UUID.randomUUID()
    );

    MockMultipartFile messagePart = new MockMultipartFile(
        "messageCreateRequest", null,
        "application/json",
        objectMapper.writeValueAsBytes(invalidRequest)
    );

    //when then
    mockMvc.perform(multipart("/api/messages")
            .file(messagePart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(jsonPath("$.details.errors[0]").value("content: 메시지 내용은 필수입니다."));
  }

  @Test
  @DisplayName("/api/messages/{id} - 메시지를 수정할 수 있다.")
  void updateMessage_success() throws Exception {
    //given
    UUID messageId = UUID.randomUUID();
    MessageUpdateDTO request = new MessageUpdateDTO("수정된 메시지");

    MessageDto response = new MessageDto(
        messageId,
        Instant.now(),
        Instant.now(),
        "수정된 메시지",
        UUID.randomUUID(),
        new UserDto(UUID.randomUUID(), "user1", "user1@abc.com", null, true),
        List.of()
    );

    given(messageService.update(eq(messageId), any())).willReturn(response);

    //when then
    mockMvc.perform(patch("/api/messages/" + messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("수정된 메시지"));
  }

  @Test
  @DisplayName("/api/messages/{id} - 수정할 메시지 내용이 비어있으면 VALIDATION_FAILED 에러를 반환한다.")
  void updateMessage_validationFailed() throws Exception {
    //given
    UUID messageId = UUID.randomUUID();
    MessageUpdateDTO invalid = new MessageUpdateDTO(""); // 빈 값

    //when then
    mockMvc.perform(patch("/api/messages/" + messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalid)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(jsonPath("$.details.errors[0]").value("newContent: 메시지 내용은 필수입니다."));
  }

  @Test
  @DisplayName("/api/messages/{id} - 메시지를 삭제할 수 있다.")
  void deleteMessage_success() throws Exception {
    //given
    UUID messageId = UUID.randomUUID();

    //when then
    mockMvc.perform(delete("/api/messages/" + messageId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("/api/messages - 채널 메시지를 커서 기반으로 조회할 수 있다.")
  void getMessages_success() throws Exception {
    //given
    UUID channelId = UUID.randomUUID();

    MessageDto dto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        "헬로",
        channelId,
        null, // author
        List.of()
    );

    PageResponse<MessageDto> response = new PageResponse<>(
        List.of(dto), // content
        null, // nextCursor
        50, // size
        false, // hasNext
        1L // totalElements
    );

    given(messageService.findAllByChannelId(eq(channelId), any(), any())).willReturn(response);

    //when then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].content").value("헬로"))
        .andExpect(jsonPath("$.hasNext").value(false))
        .andExpect(jsonPath("$.size").value(50))
        .andExpect(jsonPath("$.totalElements").value(1));
  }
}