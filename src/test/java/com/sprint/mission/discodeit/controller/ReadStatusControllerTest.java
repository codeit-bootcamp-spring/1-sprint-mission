package com.sprint.mission.discodeit.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.status.ReadStatusService;
import java.time.Instant;
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
  @DisplayName("POST /api/readStatuses - 읽음 상태 생성")
  void createReadStatus() throws Exception {
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    ReadStatusRequest request = new ReadStatusRequest(userId, channelId, Instant.now());
    String requestJson = objectMapper.writeValueAsString(request);

    ReadStatusDto readStatusDto = new ReadStatusDto(UUID.randomUUID(), userId, channelId,
        Instant.now());
    when(readStatusService.create(any(ReadStatusRequest.class)))
        .thenReturn(readStatusDto);

    mockMvc
        .perform(post("/api/readStatuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(content().json(objectMapper.writeValueAsString(readStatusDto)));

    verify(readStatusService).create(request);
  }

  @Test
  @DisplayName("PATCH /api/readStatuses/{readStatusId} - 읽음 상태 수정")
  void modifyReadStatus() throws Exception {
    UUID readStatusId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    ReadStatusUpdateRequest request = new ReadStatusUpdateRequest(Instant.now());
    String requestJson = objectMapper.writeValueAsString(request);

    ReadStatusDto readStatusDto = new ReadStatusDto(UUID.randomUUID(), userId, channelId,
        Instant.now());
    when(readStatusService.update(any(UUID.class), any(ReadStatusUpdateRequest.class)))
        .thenReturn(readStatusDto);

    mockMvc
        .perform(patch("/api/readStatuses/{readStatusId}", readStatusId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(readStatusDto)));

    verify(readStatusService).update(readStatusId, request);
  }
}