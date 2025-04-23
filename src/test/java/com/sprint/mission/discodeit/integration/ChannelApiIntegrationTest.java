package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
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
class ChannelApiIntegrationTest {

  private static final Logger logger = LoggerFactory.getLogger(ChannelApiIntegrationTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ChannelService channelService;

  @Autowired
  private UserService userService;

  @Test
  @DisplayName("공개 채널 생성 API 통합 테스트")
  void createPublicChannel_Success() throws Exception {
    logger.info("==== 공개 채널 생성 API 통합 테스트 시작 ====");

    // Given
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest(
        "테스트 채널",
        "테스트 채널 설명입니다."
    );

    String requestBody = objectMapper.writeValueAsString(createRequest);
    logger.info("공개 채널 생성 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.type", is(ChannelType.PUBLIC.name())))
        .andExpect(jsonPath("$.name", is("테스트 채널")))
        .andExpect(jsonPath("$.description", is("테스트 채널 설명입니다.")));

    logger.info("==== 공개 채널 생성 API 통합 테스트 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("공개 채널 생성 실패 API 통합 테스트 - 유효하지 않은 요청")
  void createPublicChannel_Failure_InvalidRequest() throws Exception {
    logger.info("==== 공개 채널 생성 실패 API 통합 테스트 - 유효하지 않은 요청 시작 ====");

    // Given
    PublicChannelCreateRequest invalidRequest = new PublicChannelCreateRequest(
        "a", // 최소 길이 위반
        "테스트 채널 설명입니다."
    );

    String requestBody = objectMapper.writeValueAsString(invalidRequest);
    logger.info("유효하지 않은 공개 채널 생성 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isBadRequest());

    logger.info("==== 공개 채널 생성 실패 API 통합 테스트 - 유효하지 않은 요청 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("비공개 채널 생성 API 통합 테스트")
  void createPrivateChannel_Success() throws Exception {
    logger.info("==== 비공개 채널 생성 API 통합 테스트 시작 ====");

    // Given
    // 테스트 사용자 생성
    UserCreateRequest userRequest1 = new UserCreateRequest(
        "testuser1",
        "testuser1@example.com",
        "Password1!"
    );

    UserCreateRequest userRequest2 = new UserCreateRequest(
        "testuser2",
        "testuser2@example.com",
        "Password2!"
    );

    UserDto user1 = userService.create(userRequest1, Optional.empty());
    UserDto user2 = userService.create(userRequest2, Optional.empty());
    logger.info("테스트 사용자 2명 생성 완료");

    List<UUID> participantIds = List.of(user1.id(), user2.id());
    PrivateChannelCreateRequest createRequest = new PrivateChannelCreateRequest(participantIds);

    String requestBody = objectMapper.writeValueAsString(createRequest);
    logger.info("비공개 채널 생성 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.type", is(ChannelType.PRIVATE.name())))
        .andExpect(jsonPath("$.participantIds", hasSize(2)));

    logger.info("==== 비공개 채널 생성 API 통합 테스트 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("사용자별 채널 목록 조회 API 통합 테스트")
  void findAllChannelsByUserId_Success() throws Exception {
    logger.info("==== 사용자별 채널 목록 조회 API 통합 테스트 시작 ====");

    // Given
    // 테스트 사용자 생성
    UserCreateRequest userRequest = new UserCreateRequest(
        "channeluser",
        "channeluser@example.com",
        "Password1!"
    );

    UserDto user = userService.create(userRequest, Optional.empty());
    UUID userId = user.id();
    logger.info("테스트 사용자 생성 완료");

    // 공개 채널 생성
    PublicChannelCreateRequest publicChannelRequest = new PublicChannelCreateRequest(
        "공개 채널 1",
        "공개 채널 설명입니다."
    );

    channelService.create(publicChannelRequest);
    logger.info("공개 채널 생성 완료");

    // 비공개 채널 생성
    UserCreateRequest otherUserRequest = new UserCreateRequest(
        "otheruser",
        "otheruser@example.com",
        "Password1!"
    );

    UserDto otherUser = userService.create(otherUserRequest, Optional.empty());
    logger.info("다른 테스트 사용자 생성 완료");

    PrivateChannelCreateRequest privateChannelRequest = new PrivateChannelCreateRequest(
        List.of(userId, otherUser.id())
    );

    channelService.create(privateChannelRequest);
    logger.info("비공개 채널 생성 완료");

    // When & Then
    mockMvc.perform(get("/api/channels")
            .param("userId", userId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].type", is(ChannelType.PUBLIC.name())))
        .andExpect(jsonPath("$[1].type", is(ChannelType.PRIVATE.name())));

    logger.info("==== 사용자별 채널 목록 조회 API 통합 테스트 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("채널 업데이트 API 통합 테스트")
  void updateChannel_Success() throws Exception {
    logger.info("==== 채널 업데이트 API 통합 테스트 시작 ====");

    // Given
    // 공개 채널 생성
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest(
        "원본 채널",
        "원본 채널 설명입니다."
    );

    ChannelDto createdChannel = channelService.create(createRequest);
    UUID channelId = createdChannel.id();
    logger.info("원본 채널 생성 완료: {}", channelId);

    PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest(
        "수정된 채널",
        "수정된 채널 설명입니다."
    );

    String requestBody = objectMapper.writeValueAsString(updateRequest);
    logger.info("채널 업데이트 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(channelId.toString())))
        .andExpect(jsonPath("$.name", is("수정된 채널")))
        .andExpect(jsonPath("$.description", is("수정된 채널 설명입니다.")));

    logger.info("==== 채널 업데이트 API 통합 테스트 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("채널 업데이트 실패 API 통합 테스트 - 존재하지 않는 채널")
  void updateChannel_Failure_ChannelNotFound() throws Exception {
    logger.info("==== 채널 업데이트 실패 API 통합 테스트 - 존재하지 않는 채널 시작 ====");

    // Given
    UUID nonExistentChannelId = UUID.randomUUID();
    logger.info("존재하지 않는 채널 ID 생성: {}", nonExistentChannelId);

    PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest(
        "수정된 채널",
        "수정된 채널 설명입니다."
    );

    String requestBody = objectMapper.writeValueAsString(updateRequest);
    logger.info("채널 업데이트 요청 데이터 준비 완료");

    // When & Then
    mockMvc.perform(patch("/api/channels/{channelId}", nonExistentChannelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isNotFound());

    logger.info("==== 채널 업데이트 실패 API 통합 테스트 - 존재하지 않는 채널 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("채널 삭제 API 통합 테스트")
  void deleteChannel_Success() throws Exception {
    logger.info("==== 채널 삭제 API 통합 테스트 시작 ====");

    // Given
    // 공개 채널 생성
    PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest(
        "삭제할 채널",
        "삭제할 채널 설명입니다."
    );

    ChannelDto createdChannel = channelService.create(createRequest);
    UUID channelId = createdChannel.id();
    logger.info("삭제할 채널 생성 완료: {}", channelId);

    // When & Then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());
    logger.info("채널 삭제 요청 완료");

    // 삭제 확인 - 사용자로 채널 조회 시 삭제된 채널은 조회되지 않아야 함
    UserCreateRequest userRequest = new UserCreateRequest(
        "testuser",
        "testuser@example.com",
        "Password1!"
    );

    UserDto user = userService.create(userRequest, Optional.empty());
    logger.info("채널 삭제 확인용 테스트 사용자 생성 완료");

    mockMvc.perform(get("/api/channels")
            .param("userId", user.id().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.id == '" + channelId + "')]").doesNotExist());

    logger.info("==== 채널 삭제 API 통합 테스트 완료 ====");
    System.out.println("\n");
  }

  @Test
  @DisplayName("채널 삭제 실패 API 통합 테스트 - 존재하지 않는 채널")
  void deleteChannel_Failure_ChannelNotFound() throws Exception {
    logger.info("==== 채널 삭제 실패 API 통합 테스트 - 존재하지 않는 채널 시작 ====");

    // Given
    UUID nonExistentChannelId = UUID.randomUUID();
    logger.info("존재하지 않는 채널 ID 생성: {}", nonExistentChannelId);

    // When & Then
    mockMvc.perform(delete("/api/channels/{channelId}", nonExistentChannelId))
        .andExpect(status().isNotFound());

    logger.info("==== 채널 삭제 실패 API 통합 테스트 - 존재하지 않는 채널 완료 ====");
    System.out.println("\n");
  }
}