package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChannelService channelService;

    @Test
    @DisplayName("공개 채널 생성 성공")
    void createPublicChannelSuccess() throws Exception {
        // Given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("general");
        ChannelDto response = new ChannelDto(UUID.randomUUID(), "general", ChannelType.PUBLIC);
        given(channelService.create(any(PublicChannelCreateRequest.class))).willReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("general"))
            .andExpect(jsonPath("$.type").value("PUBLIC"));
    }

    @Test
    @DisplayName("비공개 채널 생성 성공")
    void createPrivateChannelSuccess() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(userId);
        ChannelDto response = new ChannelDto(UUID.randomUUID(), null, ChannelType.PRIVATE);
        given(channelService.create(any(PrivateChannelCreateRequest.class))).willReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/channels/private")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.type").value("PRIVATE"));
    }

    @Test
    @DisplayName("채널 정보 수정 성공")
    void updateChannelSuccess() throws Exception {
        // Given
        UUID channelId = UUID.randomUUID();
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("new-general");
        ChannelDto response = new ChannelDto(channelId, "new-general", ChannelType.PUBLIC);
        given(channelService.update(any(UUID.class), any(PublicChannelUpdateRequest.class)))
            .willReturn(response);

        // When & Then
        mockMvc.perform(put("/api/v1/channels/{channelId}", channelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("new-general"));
    }

    @Test
    @DisplayName("존재하지 않는 채널 수정 실패")
    void updateChannelFailChannelNotFound() throws Exception {
        // Given
        UUID channelId = UUID.randomUUID();
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("new-general");
        given(channelService.update(any(UUID.class), any(PublicChannelUpdateRequest.class)))
            .willThrow(new ChannelException.ChannelNotFoundException(channelId));

        // When & Then
        mockMvc.perform(put("/api/v1/channels/{channelId}", channelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("사용자별 채널 목록 조회 성공")
    void findChannelsByUserIdSuccess() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        List<ChannelDto> channels = List.of(
            new ChannelDto(UUID.randomUUID(), "general", ChannelType.PUBLIC),
            new ChannelDto(UUID.randomUUID(), null, ChannelType.PRIVATE)
        );
        given(channelService.findAllByUserId(userId)).willReturn(channels);

        // When & Then
        mockMvc.perform(get("/api/v1/channels/users/{userId}", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].type").value("PUBLIC"))
            .andExpect(jsonPath("$[1].type").value("PRIVATE"));
    }

    @Test
    @DisplayName("채널 삭제 성공")
    void deleteChannelSuccess() throws Exception {
        // Given
        UUID channelId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(delete("/api/v1/channels/{channelId}", channelId))
            .andExpect(status().isNoContent());

        then(channelService).should().delete(channelId);
    }
} 