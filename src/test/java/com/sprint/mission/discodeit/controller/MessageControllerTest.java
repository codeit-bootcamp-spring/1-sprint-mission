package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
  @DisplayName("메시지 생성 성공 테스트")
  void createMessage_Success() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest createRequest = new MessageCreateRequest(
        "안녕하세요, 테스트 메시지입니다.",
        channelId,
        authorId
    );

    MockMultipartFile messageCreateRequestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(createRequest)
    );

    MockMultipartFile attachment = new MockMultipartFile(
        "attachments",
        "test.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "test-image".getBytes()
    );

    UUID messageId = UUID.randomUUID();
    Instant now = Instant.now();
    
    UserDto author = new UserDto(
        authorId,
        "testuser",
        "test@example.com",
        null,
        true
    );
    
    BinaryContentDto attachmentDto = new BinaryContentDto(
        UUID.randomUUID(),
        "test.jpg",
        10L,
        MediaType.IMAGE_JPEG_VALUE
    );

    MessageDto createdMessage = new MessageDto(
        messageId,
        now,
        now,
        "안녕하세요, 테스트 메시지입니다.",
        channelId,
        author,
        List.of(attachmentDto)
    );

    given(messageService.create(any(MessageCreateRequest.class), any(List.class)))
        .willReturn(createdMessage);

    // When & Then
    mockMvc.perform(multipart("/api/messages")
            .file(messageCreateRequestPart)
            .file(attachment)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("안녕하세요, 테스트 메시지입니다."))
        .andExpect(jsonPath("$.channelId").value(channelId.toString()))
        .andExpect(jsonPath("$.author.id").value(authorId.toString()))
        .andExpect(jsonPath("$.attachments[0].fileName").value("test.jpg"));
  }

  @Test
  @DisplayName("메시지 생성 실패 테스트 - 유효하지 않은 요청")
  void createMessage_Failure_InvalidRequest() throws Exception {
    // Given
    MessageCreateRequest invalidRequest = new MessageCreateRequest(
        "", // 내용이 비어있음 (NotBlank 위반)
        null, // 채널 ID가 비어있음 (NotNull 위반)
        null  // 작성자 ID가 비어있음 (NotNull 위반)
    );

    MockMultipartFile messageCreateRequestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(invalidRequest)
    );

    // When & Then
    mockMvc.perform(multipart("/api/messages")
            .file(messageCreateRequestPart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("메시지 업데이트 성공 테스트")
  void updateMessage_Success() throws Exception {
    // Given
    UUID messageId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    
    MessageUpdateRequest updateRequest = new MessageUpdateRequest(
        "수정된 메시지 내용입니다."
    );

    Instant now = Instant.now();
    
    UserDto author = new UserDto(
        authorId,
        "testuser",
        "test@example.com",
        null,
        true
    );

    MessageDto updatedMessage = new MessageDto(
        messageId,
        now.minusSeconds(60),
        now,
        "수정된 메시지 내용입니다.",
        channelId,
        author,
        new ArrayList<>()
    );

    given(messageService.update(eq(messageId), any(MessageUpdateRequest.class)))
        .willReturn(updatedMessage);

    // When & Then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("수정된 메시지 내용입니다."))
        .andExpect(jsonPath("$.channelId").value(channelId.toString()))
        .andExpect(jsonPath("$.author.id").value(authorId.toString()));
  }

  @Test
  @DisplayName("메시지 업데이트 실패 테스트 - 존재하지 않는 메시지")
  void updateMessage_Failure_MessageNotFound() throws Exception {
    // Given
    UUID nonExistentMessageId = UUID.randomUUID();
    
    MessageUpdateRequest updateRequest = new MessageUpdateRequest(
        "수정된 메시지 내용입니다."
    );

    given(messageService.update(eq(nonExistentMessageId), any(MessageUpdateRequest.class)))
        .willThrow(MessageNotFoundException.withId(nonExistentMessageId));

    // When & Then
    mockMvc.perform(patch("/api/messages/{messageId}", nonExistentMessageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("메시지 삭제 성공 테스트")
  void deleteMessage_Success() throws Exception {
    // Given
    UUID messageId = UUID.randomUUID();
    willDoNothing().given(messageService).delete(messageId);

    // When & Then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("메시지 삭제 실패 테스트 - 존재하지 않는 메시지")
  void deleteMessage_Failure_MessageNotFound() throws Exception {
    // Given
    UUID nonExistentMessageId = UUID.randomUUID();
    willThrow(MessageNotFoundException.withId(nonExistentMessageId))
        .given(messageService).delete(nonExistentMessageId);

    // When & Then
    mockMvc.perform(delete("/api/messages/{messageId}", nonExistentMessageId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("채널별 메시지 목록 조회 성공 테스트")
  void findAllByChannelId_Success() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    Instant cursor = Instant.now();
    Pageable pageable = PageRequest.of(0, 50, Sort.Direction.DESC, "createdAt");
    
    UserDto author = new UserDto(
        authorId,
        "testuser",
        "test@example.com",
        null,
        true
    );
    
    List<MessageDto> messages = List.of(
        new MessageDto(
            UUID.randomUUID(),
            cursor.minusSeconds(10),
            cursor.minusSeconds(10),
            "첫 번째 메시지",
            channelId,
            author,
            new ArrayList<>()
        ),
        new MessageDto(
            UUID.randomUUID(),
            cursor.minusSeconds(20),
            cursor.minusSeconds(20),
            "두 번째 메시지",
            channelId,
            author,
            new ArrayList<>()
        )
    );
    
    PageResponse<MessageDto> pageResponse = new PageResponse<>(
        messages,
        cursor.minusSeconds(30), // nextCursor 값
        pageable.getPageSize(),
        true, // hasNext
        (long) messages.size() // totalElements
    );

    given(messageService.findAllByChannelId(eq(channelId), eq(cursor), any(Pageable.class)))
        .willReturn(pageResponse);

    // When & Then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("cursor", cursor.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[0].content").value("첫 번째 메시지"))
        .andExpect(jsonPath("$.content[1].content").value("두 번째 메시지"))
        .andExpect(jsonPath("$.nextCursor").exists())
        .andExpect(jsonPath("$.size").value(50))
        .andExpect(jsonPath("$.hasNext").value(true))
        .andExpect(jsonPath("$.totalElements").value(2));
  }
} 