package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReadStatusController.class)
class ReadStatusControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ReadStatusService readStatusService;

  @Test
  @DisplayName("읽음 상태 생성 성공 테스트")
  void create_Success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    Instant lastReadAt = Instant.now();
    
    ReadStatusCreateRequest createRequest = new ReadStatusCreateRequest(
        userId,
        channelId,
        lastReadAt
    );

    UUID readStatusId = UUID.randomUUID();
    ReadStatusDto createdReadStatus = new ReadStatusDto(
        readStatusId,
        userId,
        channelId,
        lastReadAt
    );

    given(readStatusService.create(any(ReadStatusCreateRequest.class)))
        .willReturn(createdReadStatus);

    // When & Then
    mockMvc.perform(post("/api/readStatuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(readStatusId.toString()))
        .andExpect(jsonPath("$.userId").value(userId.toString()))
        .andExpect(jsonPath("$.channelId").value(channelId.toString()))
        .andExpect(jsonPath("$.lastReadAt").exists());
  }

  @Test
  @DisplayName("읽음 상태 생성 실패 테스트 - 유효하지 않은 요청")
  void create_Failure_InvalidRequest() throws Exception {
    // Given
    ReadStatusCreateRequest invalidRequest = new ReadStatusCreateRequest(
        null, // userId가 null (NotNull 위반)
        null, // channelId가 null (NotNull 위반)
        null  // lastReadAt이 null (NotNull 위반)
    );

    // When & Then
    mockMvc.perform(post("/api/readStatuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("읽음 상태 업데이트 성공 테스트")
  void update_Success() throws Exception {
    // Given
    UUID readStatusId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    Instant newLastReadAt = Instant.now();
    
    ReadStatusUpdateRequest updateRequest = new ReadStatusUpdateRequest(newLastReadAt);

    ReadStatusDto updatedReadStatus = new ReadStatusDto(
        readStatusId,
        userId,
        channelId,
        newLastReadAt
    );

    given(readStatusService.update(eq(readStatusId), any(ReadStatusUpdateRequest.class)))
        .willReturn(updatedReadStatus);

    // When & Then
    mockMvc.perform(patch("/api/readStatuses/{readStatusId}", readStatusId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(readStatusId.toString()))
        .andExpect(jsonPath("$.userId").value(userId.toString()))
        .andExpect(jsonPath("$.channelId").value(channelId.toString()))
        .andExpect(jsonPath("$.lastReadAt").exists());
  }

  @Test
  @DisplayName("읽음 상태 업데이트 실패 테스트 - 존재하지 않는 읽음 상태")
  void update_Failure_ReadStatusNotFound() throws Exception {
    // Given
    UUID nonExistentId = UUID.randomUUID();
    Instant newLastReadAt = Instant.now();
    
    ReadStatusUpdateRequest updateRequest = new ReadStatusUpdateRequest(newLastReadAt);

    given(readStatusService.update(eq(nonExistentId), any(ReadStatusUpdateRequest.class)))
        .willThrow(ReadStatusNotFoundException.withId(nonExistentId));

    // When & Then
    mockMvc.perform(patch("/api/readStatuses/{readStatusId}", nonExistentId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("사용자별 읽음 상태 목록 조회 성공 테스트")
  void findAllByUserId_Success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    UUID channelId1 = UUID.randomUUID();
    UUID channelId2 = UUID.randomUUID();
    Instant now = Instant.now();
    
    List<ReadStatusDto> readStatuses = List.of(
        new ReadStatusDto(UUID.randomUUID(), userId, channelId1, now.minusSeconds(60)),
        new ReadStatusDto(UUID.randomUUID(), userId, channelId2, now)
    );

    given(readStatusService.findAllByUserId(userId)).willReturn(readStatuses);

    // When & Then
    mockMvc.perform(get("/api/readStatuses")
            .param("userId", userId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].userId").value(userId.toString()))
        .andExpect(jsonPath("$[0].channelId").value(channelId1.toString()))
        .andExpect(jsonPath("$[1].userId").value(userId.toString()))
        .andExpect(jsonPath("$[1].channelId").value(channelId2.toString()));
  }
} 