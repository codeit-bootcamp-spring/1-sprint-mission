package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = MessageController.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MessageService messageService;

  @Test
  @DisplayName("[성공] 메시지 생성")
  void createMessage_success() throws Exception {
    // Given: 유효한 메시지 생성 요청과 응답
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageDto response = new MessageDto(UUID.randomUUID(), Instant.now(), null, "hello", channelId,
        null, null);

    MockMultipartFile messageRequest = new MockMultipartFile(
        "messageCreateRequest",
        null,
        MediaType.APPLICATION_JSON_VALUE,
        ("{" +
            "\"channelId\":\"" + channelId + "\"," +
            "\"authorId\":\"" + authorId + "\"," +
            "\"content\":\"hello\"}").getBytes()
    );

    // attachments를 빈 리스트로라도 반드시 넘겨야 null 에러 안 남
    MockMultipartFile dummyAttachment = new MockMultipartFile(
        "attachments",
        "dummy.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "dummy file content".getBytes()
    );

    // When: 서비스가 정상 응답을 반환하도록 설정
    when(messageService.create(any(), anyList())).thenReturn(response);

    // Then: 응답 상태 201과 메시지 본문 확인
    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/messages")
            .file(messageRequest)
            .file(dummyAttachment))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("hello"));
  }

  @Test
  @DisplayName("[실패] 메시지 생성 - content 누락")
  void createMessage_fail_missingContent() throws Exception {
    // Given: content 없는 메시지 요청
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    MockMultipartFile messageRequest = new MockMultipartFile(
        "messageCreateRequest",
        null,
        MediaType.APPLICATION_JSON_VALUE,
        ("{" +
            "\"channelId\":\"" + channelId + "\"," +
            "\"authorId\":\"" + authorId + "\"}").getBytes()
    );

    // 빈파일
    MockMultipartFile dummyAttachment = new MockMultipartFile(
        "attachments",
        "dummy.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "dummy file content".getBytes()
    );

    // When & Then: 유효성 실패로 400 반환
    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/messages")
            .file(messageRequest)
            .file(dummyAttachment))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("[성공] 메시지 수정")
  void updateMessage_success() throws Exception {
    // Given: 수정 요청 및 응답 정의
    UUID messageId = UUID.randomUUID();
    MessageDto response = new MessageDto(messageId, Instant.now(), Instant.now(), "updated",
        UUID.randomUUID(), null, null);
    String payload = "{\"newContent\":\"updated\"}";

    // When: 서비스 호출 시 수정된 메시지 반환
    when(messageService.update(eq(messageId), any())).thenReturn(response);

    // Then: 200 OK 응답과 업데이트된 내용 확인
    mockMvc.perform(MockMvcRequestBuilders.patch("/api/messages/" + messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("updated"));
  }

  @Test
  @DisplayName("[실패] 메시지 수정 - 내용 없음")
  void updateMessage_fail_blankContent() throws Exception {
    // Given: 빈 문자열 전달
    UUID messageId = UUID.randomUUID();
    String payload = "{\"newContent\":\"\"}";

    // When & Then: 유효성 실패로 400 반환
    mockMvc.perform(MockMvcRequestBuilders.patch("/api/messages/" + messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("[성공] 메시지 삭제")
  void deleteMessage_success() throws Exception {
    // Given: 메시지 ID
    UUID messageId = UUID.randomUUID();

    // When & Then: 삭제 요청 시 204 응답
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/messages/" + messageId))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("[성공] 메시지 목록 조회")
  void findAllMessages_success() throws Exception {
    // Given: 채널 ID에 해당하는 메시지 목록
    UUID channelId = UUID.randomUUID();
    MessageDto msg = new MessageDto(UUID.randomUUID(), Instant.now(), null, "hello", channelId,
        null, null);
    PageResponse<MessageDto> response = new PageResponse<>(List.of(msg), null, 1, false, 1L);

    // When: 서비스가 해당 목록 반환하도록 설정
    when(messageService.findAllByChannelId(eq(channelId), any(), any())).thenReturn(response);

    // Then: 응답 상태 200과 목록 항목 확인
    mockMvc.perform(MockMvcRequestBuilders.get("/api/messages")
            .param("channelId", channelId.toString()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].content").value("hello"));
  }

  @Test
  @DisplayName("[실패] 메시지 목록 조회 - channelId 누락")
  void findAllMessages_fail_missingChannelId() throws Exception {
    // Given 없음

    // When & Then: 필수 파라미터 누락으로 400 반환
    mockMvc.perform(MockMvcRequestBuilders.get("/api/messages"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
