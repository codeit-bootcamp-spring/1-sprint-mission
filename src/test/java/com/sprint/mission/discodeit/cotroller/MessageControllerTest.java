package com.sprint.mission.discodeit.cotroller;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.reponse.PageResponse;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MessageService messageService;


  @Test
  @DisplayName("POST 메시지 생성 성공 테스트")
  void createMessage_Success() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest createRequest = new MessageCreateRequest(
        channelId,
        authorId,
        "안녕하세요, 테스트 메시지입니다."
    );

    MockMultipartFile messageCreateRequestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(createRequest) // JSON -> 바이트배열 ( multipart/form-data 전송할 때 )
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

    BinaryContentDto attachmentDto = BinaryContentDto.builder()
        .fileName("attachment.jpg")
        .id(UUID.randomUUID())
        .size(12L)
        .contentType(MediaType.IMAGE_JPEG_VALUE)
        .build();

    MessageDto createdMessage = new MessageDto(
        messageId,
        now,
        now,
        "안녕하세요, 테스트 메시지입니다.",
        channelId,
        author,
        List.of(attachmentDto)
    );

    given(messageService.createMessage(any(MessageCreateRequest.class), any(List.class)))
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
        .andExpect(jsonPath("$.attachments[0].fileName").value("attachment.jpg"));
  }

  @Test
  @DisplayName("POST 메시지 생성 실패 테스트 - 유효하지 않은 요청")
  void createMessage_Fail_InvalidRequest() throws Exception {
    // Given
    MessageCreateRequest invalidRequest = new MessageCreateRequest(
        null, // 채널 ID가 비어있음 (NotNull 위반)
        null,  // 작성자 ID가 비어있음 (NotNull 위반)
        "" // 내용이 비어있음 (NotBlank 위반)
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
  @DisplayName("PATCH 메시지 업데이트 성공 테스트")
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

    given(messageService.updateMessageText(eq(messageId), any(MessageUpdateRequest.class)))
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
  @DisplayName("PATCH 메시지 업데이트 실패 테스트 - 존재하지 않는 메시지")
  void updateMessage_Failure_MessageNotFound() throws Exception {
    // Given
    UUID nonExistentMessageId = UUID.randomUUID();

    MessageUpdateRequest updateRequest = new MessageUpdateRequest(
        "수정된 메시지 내용입니다."
    );

    given(
        messageService.updateMessageText(eq(nonExistentMessageId), any(MessageUpdateRequest.class)))
        .willThrow(
            new MessageNotFoundException(Map.of("nonExistentMessageId", nonExistentMessageId)));

    // When & Then
    mockMvc.perform(patch("/api/messages/{messageId}", nonExistentMessageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("DELETE 메시지 삭제 성공 테스트")
  void deleteMessage_Success() throws Exception {
    // Given
    UUID messageId = UUID.randomUUID();
    willDoNothing().given(messageService).deleteMessageById(messageId);

    // When & Then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("DELETE 메시지 삭제 실패 테스트 - 존재하지 않는 메시지")
  void deleteMessage_Failure_MessageNotFound() throws Exception {
    // Given
    UUID nonExistentMessageId = UUID.randomUUID();
    willThrow(new MessageNotFoundException(Map.of("nonExistentMessageId", nonExistentMessageId)))
        .given(messageService).deleteMessageById(nonExistentMessageId);

    // When & Then
    mockMvc.perform(delete("/api/messages/{messageId}", nonExistentMessageId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("GET 채널별 메시지 목록 조회 성공 테스트")
  void findAllByChannelId_Success() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    UserDto authorDto = UserDto.builder()
        .id(authorId)
        .username("testUser")
        .email("test@example.com")
        .online(true)
        .build();

    List<MessageDto> messageDtos = List.of(
        MessageDto.builder()
            .id(UUID.randomUUID())
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .content("Test Message 1")
            .channelId(channelId)
            .author(authorDto)
            .build(),
        MessageDto.builder()
            .id(UUID.randomUUID())
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .content("Test Message 2")
            .channelId(channelId)
            .author(authorDto)
            .build()
    );

    Page<MessageDto> page = new PageImpl<>(
        messageDtos,
        PageRequest.of(0, 2, Sort.by("createdAt").ascending()),
        messageDtos.size()
    );

    PageResponse<MessageDto> pageResponse = new PageResponse<>(
        messageDtos,
        page.getNumber(),
        page.getSize(),
        page.hasNext(),
        page.getTotalElements());

    given(messageService.findAllByChannelId(
        any(UUID.class),
        any(Pageable.class))
    ).willReturn(pageResponse);

    // When & Then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("page", "0")
            .param("size", "2")
            .param("sortBy", "createdAt")
            .param("direction", "asc")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[0].content").value("Test Message 1"))
        .andExpect(jsonPath("$.content[1].content").value("Test Message 2"))
        .andExpect(jsonPath("$.size").value(2))
        .andExpect(jsonPath("$.hasNext").value(false))
        .andExpect(jsonPath("$.totalElements").value(2));
  }


}
