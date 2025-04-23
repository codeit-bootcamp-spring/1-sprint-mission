package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MessageApiIntegrationTest {

  private static final Logger logger = LoggerFactory.getLogger(MessageApiIntegrationTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MessageService messageService;

  @Autowired
  private ChannelService channelService;

  @Autowired
  private UserService userService;

  @Test
  @DisplayName("메시지 생성 API 통합 테스트")
  void createMessage_Success() throws Exception {
    logger.info("==== 메시지 생성 API 통합 테스트 시작 ====");

    // Given
    // 테스트 채널 생성
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest(
        "테스트 채널",
        "테스트 채널 설명입니다."
    );

    ChannelDto channel = channelService.create(channelRequest);
    logger.info("테스트 채널 생성 완료: {}", channel.id());

    // 테스트 사용자 생성
    UserCreateRequest userRequest = new UserCreateRequest(
        "messageuser",
        "messageuser@example.com",
        "Password1!"
    );

    UserDto user = userService.create(userRequest, Optional.empty());
    logger.info("테스트 사용자 생성 완료: {}", user.id());

    // 메시지 생성 요청
    MessageCreateRequest createRequest = new MessageCreateRequest(
        "테스트 메시지 내용입니다.",
        channel.id(),
        user.id()
    );

    MockMultipartFile messageCreateRequestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(createRequest)
    );

    MockMultipartFile attachmentPart = new MockMultipartFile(
        "attachments",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "테스트 첨부 파일 내용".getBytes()
    );

    logger.info("메시지 생성 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(multipart("/api/messages")
            .file(messageCreateRequestPart)
            .file(attachmentPart))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.content", is("테스트 메시지 내용입니다.")))
        .andExpect(jsonPath("$.channelId", is(channel.id().toString())))
        .andExpect(jsonPath("$.author.id", is(user.id().toString())))
        .andExpect(jsonPath("$.attachments", hasSize(1)))
        .andExpect(jsonPath("$.attachments[0].fileName", is("test.txt")));

    logger.info("==== 메시지 생성 API 통합 테스트 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("메시지 생성 실패 API 통합 테스트 - 유효하지 않은 요청")
  void createMessage_Failure_InvalidRequest() throws Exception {
    logger.info("==== 메시지 생성 실패 API 통합 테스트 - 유효하지 않은 요청 시작 ====");

    // Given
    MessageCreateRequest invalidRequest = new MessageCreateRequest(
        "", // 내용이 비어있음
        UUID.randomUUID(),
        UUID.randomUUID()
    );

    MockMultipartFile messageCreateRequestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(invalidRequest)
    );

    logger.info("유효하지 않은 메시지 생성 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(multipart("/api/messages")
            .file(messageCreateRequestPart))
        .andExpect(status().isNotFound());

    logger.info("==== 메시지 생성 실패 API 통합 테스트 - 유효하지 않은 요청 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("채널별 메시지 목록 조회 API 통합 테스트")
  void findAllMessagesByChannelId_Success() throws Exception {
    logger.info("==== 채널별 메시지 목록 조회 API 통합 테스트 시작 ====");

    // Given
    // 테스트 채널 생성
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest(
        "테스트 채널",
        "테스트 채널 설명입니다."
    );

    ChannelDto channel = channelService.create(channelRequest);
    logger.info("테스트 채널 생성 완료: {}", channel.id());

    // 테스트 사용자 생성
    UserCreateRequest userRequest = new UserCreateRequest(
        "messageuser",
        "messageuser@example.com",
        "Password1!"
    );

    UserDto user = userService.create(userRequest, Optional.empty());
    logger.info("테스트 사용자 생성 완료: {}", user.id());

    // 메시지 생성
    MessageCreateRequest messageRequest1 = new MessageCreateRequest(
        "첫 번째 메시지 내용입니다.",
        channel.id(),
        user.id()
    );

    MessageCreateRequest messageRequest2 = new MessageCreateRequest(
        "두 번째 메시지 내용입니다.",
        channel.id(),
        user.id()
    );

    messageService.create(messageRequest1, new ArrayList<>());
    messageService.create(messageRequest2, new ArrayList<>());
    logger.info("테스트 메시지 2개 생성 완료");

    // When & Then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channel.id().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.content[0].content", is("두 번째 메시지 내용입니다.")))
        .andExpect(jsonPath("$.content[1].content", is("첫 번째 메시지 내용입니다.")))
        .andExpect(jsonPath("$.size").exists())
        .andExpect(jsonPath("$.hasNext").exists())
        .andExpect(jsonPath("$.totalElements").isEmpty());

    logger.info("==== 채널별 메시지 목록 조회 API 통합 테스트 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("메시지 업데이트 API 통합 테스트")
  void updateMessage_Success() throws Exception {
    logger.info("==== 메시지 업데이트 API 통합 테스트 시작 ====");

    // Given
    // 테스트 채널 생성
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest(
        "테스트 채널",
        "테스트 채널 설명입니다."
    );

    ChannelDto channel = channelService.create(channelRequest);
    logger.info("테스트 채널 생성 완료: {}", channel.id());

    // 테스트 사용자 생성
    UserCreateRequest userRequest = new UserCreateRequest(
        "messageuser",
        "messageuser@example.com",
        "Password1!"
    );

    UserDto user = userService.create(userRequest, Optional.empty());
    logger.info("테스트 사용자 생성 완료: {}", user.id());

    // 메시지 생성
    MessageCreateRequest createRequest = new MessageCreateRequest(
        "원본 메시지 내용입니다.",
        channel.id(),
        user.id()
    );

    MessageDto createdMessage = messageService.create(createRequest, new ArrayList<>());
    UUID messageId = createdMessage.id();
    logger.info("원본 메시지 생성 완료: {}", messageId);

    // 메시지 업데이트 요청
    MessageUpdateRequest updateRequest = new MessageUpdateRequest(
        "수정된 메시지 내용입니다."
    );

    String requestBody = objectMapper.writeValueAsString(updateRequest);
    logger.info("메시지 업데이트 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(messageId.toString())))
        .andExpect(jsonPath("$.content", is("수정된 메시지 내용입니다.")))
        .andExpect(jsonPath("$.updatedAt").exists());

    logger.info("==== 메시지 업데이트 API 통합 테스트 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("메시지 업데이트 실패 API 통합 테스트 - 존재하지 않는 메시지")
  void updateMessage_Failure_MessageNotFound() throws Exception {
    logger.info("==== 메시지 업데이트 실패 API 통합 테스트 - 존재하지 않는 메시지 시작 ====");

    // Given
    UUID nonExistentMessageId = UUID.randomUUID();
    logger.info("존재하지 않는 메시지 ID 생성: {}", nonExistentMessageId);

    MessageUpdateRequest updateRequest = new MessageUpdateRequest(
        "수정된 메시지 내용입니다."
    );

    String requestBody = objectMapper.writeValueAsString(updateRequest);
    logger.info("메시지 업데이트 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(patch("/api/messages/{messageId}", nonExistentMessageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isNotFound());

    logger.info("==== 메시지 업데이트 실패 API 통합 테스트 - 존재하지 않는 메시지 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("메시지 삭제 API 통합 테스트")
  void deleteMessage_Success() throws Exception {
    logger.info("==== 메시지 삭제 API 통합 테스트 시작 ====");

    // Given
    // 테스트 채널 생성
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest(
        "테스트 채널",
        "테스트 채널 설명입니다."
    );

    ChannelDto channel = channelService.create(channelRequest);
    logger.info("테스트 채널 생성 완료: {}", channel.id());

    // 테스트 사용자 생성
    UserCreateRequest userRequest = new UserCreateRequest(
        "messageuser",
        "messageuser@example.com",
        "Password1!"
    );

    UserDto user = userService.create(userRequest, Optional.empty());
    logger.info("테스트 사용자 생성 완료: {}", user.id());

    // 메시지 생성
    MessageCreateRequest createRequest = new MessageCreateRequest(
        "삭제할 메시지 내용입니다.",
        channel.id(),
        user.id()
    );

    MessageDto createdMessage = messageService.create(createRequest, new ArrayList<>());
    UUID messageId = createdMessage.id();
    logger.info("삭제할 메시지 생성 완료: {}", messageId);

    // When & Then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());
    logger.info("메시지 삭제 요청 완료");

    // 삭제 확인 - 채널의 메시지 목록 조회 시 삭제된 메시지는 조회되지 않아야 함
    mockMvc.perform(get("/api/messages")
            .param("channelId", channel.id().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)));

    logger.info("==== 메시지 삭제 API 통합 테스트 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("메시지 삭제 실패 API 통합 테스트 - 존재하지 않는 메시지")
  void deleteMessage_Failure_MessageNotFound() throws Exception {
    logger.info("==== 메시지 삭제 실패 API 통합 테스트 - 존재하지 않는 메시지 시작 ====");

    // Given
    UUID nonExistentMessageId = UUID.randomUUID();
    logger.info("존재하지 않는 메시지 ID 생성: {}", nonExistentMessageId);

    // When & Then
    mockMvc.perform(delete("/api/messages/{messageId}", nonExistentMessageId))
        .andExpect(status().isNotFound());

    logger.info("==== 메시지 삭제 실패 API 통합 테스트 - 존재하지 않는 메시지 완료 ====");
    System.out.println("\n");
  }
}