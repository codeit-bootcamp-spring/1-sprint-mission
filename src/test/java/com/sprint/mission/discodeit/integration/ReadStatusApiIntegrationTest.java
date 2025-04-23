package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReadStatusApiIntegrationTest {

  private static final Logger logger = LoggerFactory.getLogger(ReadStatusApiIntegrationTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ReadStatusService readStatusService;

  @Autowired
  private UserService userService;

  @Autowired
  private ChannelService channelService;

  @Test
  @DisplayName("읽음 상태 생성 API 통합 테스트")
  void createReadStatus_Success() throws Exception {
    logger.info("==== 읽음 상태 생성 API 통합 테스트 시작 ====");

    // Given
    // 테스트 사용자 생성
    UserCreateRequest userRequest = new UserCreateRequest(
        "readstatususer",
        "readstatus@example.com",
        "Password1!"
    );
    UserDto user = userService.create(userRequest, Optional.empty());
    logger.info("테스트 사용자 생성 완료: {}", user.id());

    // 공개 채널 생성
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest(
        "읽음 상태 테스트 채널",
        "읽음 상태 테스트 채널 설명입니다."
    );
    ChannelDto channel = channelService.create(channelRequest);
    logger.info("테스트 채널 생성 완료: {}", channel.id());

    // 읽음 상태 생성 요청
    Instant lastReadAt = Instant.now();
    ReadStatusCreateRequest createRequest = new ReadStatusCreateRequest(
        user.id(),
        channel.id(),
        lastReadAt
    );

    String requestBody = objectMapper.writeValueAsString(createRequest);
    logger.info("읽음 상태 생성 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(post("/api/readStatuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.userId", is(user.id().toString())))
        .andExpect(jsonPath("$.channelId", is(channel.id().toString())))
        .andExpect(jsonPath("$.lastReadAt", is(lastReadAt.toString())));

    logger.info("==== 읽음 상태 생성 API 통합 테스트 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("읽음 상태 생성 실패 API 통합 테스트 - 중복 생성")
  void createReadStatus_Failure_Duplicate() throws Exception {
    logger.info("==== 읽음 상태 생성 실패 API 통합 테스트 - 중복 생성 시작 ====");

    // Given
    // 테스트 사용자 생성
    UserCreateRequest userRequest = new UserCreateRequest(
        "duplicateuser",
        "duplicate@example.com",
        "Password1!"
    );
    UserDto user = userService.create(userRequest, Optional.empty());
    logger.info("테스트 사용자 생성 완료: {}", user.id());

    // 공개 채널 생성
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest(
        "중복 테스트 채널",
        "중복 테스트 채널 설명입니다."
    );
    ChannelDto channel = channelService.create(channelRequest);
    logger.info("테스트 채널 생성 완료: {}", channel.id());

    // 첫 번째 읽음 상태 생성 요청 (성공)
    Instant lastReadAt = Instant.now();
    ReadStatusCreateRequest firstCreateRequest = new ReadStatusCreateRequest(
        user.id(),
        channel.id(),
        lastReadAt
    );

    String firstRequestBody = objectMapper.writeValueAsString(firstCreateRequest);
    logger.info("첫 번째 읽음 상태 생성 요청 데이터 준비 완료");

    mockMvc.perform(post("/api/readStatuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(firstRequestBody))
        .andExpect(status().isCreated());
    logger.info("첫 번째 읽음 상태 생성 성공");

    // 두 번째 읽음 상태 생성 요청 (동일 사용자, 동일 채널) - 실패해야 함
    ReadStatusCreateRequest duplicateCreateRequest = new ReadStatusCreateRequest(
        user.id(),
        channel.id(),
        Instant.now()
    );

    String duplicateRequestBody = objectMapper.writeValueAsString(duplicateCreateRequest);
    logger.info("중복 읽음 상태 생성 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(post("/api/readStatuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(duplicateRequestBody))
        .andExpect(status().isConflict());

    logger.info("==== 읽음 상태 생성 실패 API 통합 테스트 - 중복 생성 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("읽음 상태 업데이트 API 통합 테스트")
  void updateReadStatus_Success() throws Exception {
    logger.info("==== 읽음 상태 업데이트 API 통합 테스트 시작 ====");

    // Given
    // 테스트 사용자 생성
    UserCreateRequest userRequest = new UserCreateRequest(
        "updateuser",
        "update@example.com",
        "Password1!"
    );
    UserDto user = userService.create(userRequest, Optional.empty());
    logger.info("테스트 사용자 생성 완료: {}", user.id());

    // 공개 채널 생성
    PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest(
        "업데이트 테스트 채널",
        "업데이트 테스트 채널 설명입니다."
    );
    ChannelDto channel = channelService.create(channelRequest);
    logger.info("테스트 채널 생성 완료: {}", channel.id());

    // 읽음 상태 생성
    Instant initialLastReadAt = Instant.now().minusSeconds(3600); // 1시간 전
    ReadStatusCreateRequest createRequest = new ReadStatusCreateRequest(
        user.id(),
        channel.id(),
        initialLastReadAt
    );

    ReadStatusDto createdReadStatus = readStatusService.create(createRequest);
    UUID readStatusId = createdReadStatus.id();
    logger.info("초기 읽음 상태 생성 완료: {}, 마지막 읽음 시간: {}", readStatusId, initialLastReadAt);

    // 읽음 상태 업데이트 요청
    Instant newLastReadAt = Instant.now();
    ReadStatusUpdateRequest updateRequest = new ReadStatusUpdateRequest(
        newLastReadAt
    );

    String requestBody = objectMapper.writeValueAsString(updateRequest);
    logger.info("읽음 상태 업데이트 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(patch("/api/readStatuses/{readStatusId}", readStatusId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(readStatusId.toString())))
        .andExpect(jsonPath("$.userId", is(user.id().toString())))
        .andExpect(jsonPath("$.channelId", is(channel.id().toString())))
        .andExpect(jsonPath("$.lastReadAt", is(newLastReadAt.toString())));

    logger.info("==== 읽음 상태 업데이트 API 통합 테스트 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("읽음 상태 업데이트 실패 API 통합 테스트 - 존재하지 않는 읽음 상태")
  void updateReadStatus_Failure_NotFound() throws Exception {
    logger.info("==== 읽음 상태 업데이트 실패 API 통합 테스트 - 존재하지 않는 읽음 상태 시작 ====");

    // Given
    UUID nonExistentReadStatusId = UUID.randomUUID();
    logger.info("존재하지 않는 읽음 상태 ID 생성: {}", nonExistentReadStatusId);

    ReadStatusUpdateRequest updateRequest = new ReadStatusUpdateRequest(
        Instant.now()
    );

    String requestBody = objectMapper.writeValueAsString(updateRequest);
    logger.info("읽음 상태 업데이트 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(patch("/api/readStatuses/{readStatusId}", nonExistentReadStatusId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isNotFound());

    logger.info("==== 읽음 상태 업데이트 실패 API 통합 테스트 - 존재하지 않는 읽음 상태 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("사용자별 읽음 상태 목록 조회 API 통합 테스트")
  void findAllReadStatusesByUserId_Success() throws Exception {
    logger.info("==== 사용자별 읽음 상태 목록 조회 API 통합 테스트 시작 ====");

    // Given
    // 테스트 사용자 생성
    UserCreateRequest userRequest = new UserCreateRequest(
        "listuser",
        "list@example.com",
        "Password1!"
    );
    UserDto user = userService.create(userRequest, Optional.empty());
    logger.info("테스트 사용자 생성 완료: {}", user.id());

    // 여러 채널 생성
    PublicChannelCreateRequest channelRequest1 = new PublicChannelCreateRequest(
        "목록 테스트 채널 1",
        "목록 테스트 채널 설명입니다."
    );

    PublicChannelCreateRequest channelRequest2 = new PublicChannelCreateRequest(
        "목록 테스트 채널 2",
        "목록 테스트 채널 설명입니다."
    );

    ChannelDto channel1 = channelService.create(channelRequest1);
    ChannelDto channel2 = channelService.create(channelRequest2);
    logger.info("테스트 채널 2개 생성 완료: {}, {}", channel1.id(), channel2.id());

    // 각 채널에 대한 읽음 상태 생성
    ReadStatusCreateRequest createRequest1 = new ReadStatusCreateRequest(
        user.id(),
        channel1.id(),
        Instant.now().minusSeconds(3600)
    );

    ReadStatusCreateRequest createRequest2 = new ReadStatusCreateRequest(
        user.id(),
        channel2.id(),
        Instant.now()
    );

    readStatusService.create(createRequest1);
    readStatusService.create(createRequest2);
    logger.info("각 채널에 대한 읽음 상태 생성 완료");

    // When & Then
    mockMvc.perform(get("/api/readStatuses")
            .param("userId", user.id().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[*].channelId",
            hasItems(channel1.id().toString(), channel2.id().toString())));

    logger.info("==== 사용자별 읽음 상태 목록 조회 API 통합 테스트 완료 ====");
    System.out.println("\n");
  }
}