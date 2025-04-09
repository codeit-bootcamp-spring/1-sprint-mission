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
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.message.MessageException;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    @Test
    @DisplayName("메시지 생성 성공")
    void createMessageSuccess() throws Exception {
        // Given
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MessageCreateRequest request = new MessageCreateRequest(channelId, userId, "Hello, world!");
        MessageDto response = new MessageDto(UUID.randomUUID(), userId, channelId, "Hello, world!");
        given(messageService.create(any(MessageCreateRequest.class))).willReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content").value("Hello, world!"))
            .andExpect(jsonPath("$.userId").value(userId.toString()))
            .andExpect(jsonPath("$.channelId").value(channelId.toString()));
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 메시지 생성 실패")
    void createMessageFailUserNotFound() throws Exception {
        // Given
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MessageCreateRequest request = new MessageCreateRequest(channelId, userId, "Hello, world!");
        given(messageService.create(any(MessageCreateRequest.class)))
            .willThrow(new UserException.UserNotFoundException(userId));

        // When & Then
        mockMvc.perform(post("/api/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("메시지 수정 성공")
    void updateMessageSuccess() throws Exception {
        // Given
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest request = new MessageUpdateRequest("Updated message");
        MessageDto response = new MessageDto(messageId, UUID.randomUUID(), UUID.randomUUID(), "Updated message");
        given(messageService.update(any(UUID.class), any(MessageUpdateRequest.class))).willReturn(response);

        // When & Then
        mockMvc.perform(put("/api/v1/messages/{messageId}", messageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value("Updated message"));
    }

    @Test
    @DisplayName("존재하지 않는 메시지 수정 실패")
    void updateMessageFailMessageNotFound() throws Exception {
        // Given
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest request = new MessageUpdateRequest("Updated message");
        given(messageService.update(any(UUID.class), any(MessageUpdateRequest.class)))
            .willThrow(new MessageException.MessageNotFoundException(messageId));

        // When & Then
        mockMvc.perform(put("/api/v1/messages/{messageId}", messageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("채널별 메시지 목록 조회 성공")
    void findMessagesByChannelIdSuccess() throws Exception {
        // Given
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        List<MessageDto> messages = List.of(
            new MessageDto(UUID.randomUUID(), userId, channelId, "First message"),
            new MessageDto(UUID.randomUUID(), userId, channelId, "Second message")
        );
        given(messageService.findAllByChannelId(channelId)).willReturn(messages);

        // When & Then
        mockMvc.perform(get("/api/v1/messages/channels/{channelId}", channelId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].content").value("First message"))
            .andExpect(jsonPath("$[1].content").value("Second message"));
    }

    @Test
    @DisplayName("존재하지 않는 채널의 메시지 목록 조회 실패")
    void findMessagesByChannelIdFailChannelNotFound() throws Exception {
        // Given
        UUID channelId = UUID.randomUUID();
        given(messageService.findAllByChannelId(channelId))
            .willThrow(new ChannelException.ChannelNotFoundException(channelId));

        // When & Then
        mockMvc.perform(get("/api/v1/messages/channels/{channelId}", channelId))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("메시지 삭제 성공")
    void deleteMessageSuccess() throws Exception {
        // Given
        UUID messageId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(delete("/api/v1/messages/{messageId}", messageId))
            .andExpect(status().isNoContent());

        then(messageService).should().delete(messageId);
    }
} 