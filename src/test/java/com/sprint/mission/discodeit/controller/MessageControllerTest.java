package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    private MessageDto testMessageDto;
    private UUID testMessageId;
    private UUID testChannelId;
    private UUID testAuthorId;

    @BeforeEach
    void setUp() {
        testMessageId = UUID.randomUUID();
        testChannelId = UUID.randomUUID();
        testAuthorId = UUID.randomUUID();

        testMessageDto = MessageDto.builder()
                .id(testMessageId)
                .channelId(testChannelId)
                .authorId(testAuthorId)
                .content("테스트 메시지")
                .createdAt(Instant.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("메시지 생성 성공 테스트")
    void createMessage_Success() throws Exception {
        when(messageService.createMessage(any(MessageDto.class)))
                .thenReturn(testMessageDto);

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMessageDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testMessageId.toString()))
                .andExpect(jsonPath("$.content").value("테스트 메시지"));
    }

    @Test
    @DisplayName("메시지 생성 실패 테스트 - 유효하지 않은 입력")
    void createMessage_InvalidInput() throws Exception {
        MessageDto invalidMessageDto = MessageDto.builder()
                .content("")  // 빈 메시지 내용
                .build();

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMessageDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("모든 메시지 조회 성공 테스트")
    void getAllMessages_Success() throws Exception {
        when(messageService.findAll())
                .thenReturn(Arrays.asList(testMessageDto));

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testMessageId.toString()))
                .andExpect(jsonPath("$[0].content").value("테스트 메시지"));
    }
    
    @Test
    @DisplayName("모든 메시지 조회 성공 테스트 - 빈 목록")
    void getAllMessages_EmptyList() throws Exception {
        when(messageService.findAll())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
    
    @Test
    @DisplayName("ID로 메시지 조회 성공 테스트")
    void getMessageById_Success() throws Exception {
        when(messageService.getMessageById(testMessageId))
                .thenReturn(testMessageDto);

        mockMvc.perform(get("/api/messages/{id}", testMessageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testMessageId.toString()))
                .andExpect(jsonPath("$.content").value("테스트 메시지"));
    }
    
    @Test
    @DisplayName("ID로 메시지 조회 실패 테스트 - 메시지 없음")
    void getMessageById_NotFound() throws Exception {
        when(messageService.getMessageById(testMessageId))
                .thenThrow(new RestApiException(DomainErrorCode.MESSAGE_NOT_FOUND, "메시지를 찾을 수 없습니다."));

        mockMvc.perform(get("/api/messages/{id}", testMessageId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("채널별 메시지 조회 성공 테스트")
    void getChannelMessages_Success() throws Exception {
        when(messageService.getChannelMessages(testChannelId))
                .thenReturn(Arrays.asList(testMessageDto));

        mockMvc.perform(get("/api/messages/channels/{channelId}/messages", testChannelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testMessageId.toString()))
                .andExpect(jsonPath("$[0].content").value("테스트 메시지"));
    }
    
    @Test
    @DisplayName("채널별 메시지 조회 성공 테스트 - 빈 목록")
    void getChannelMessages_EmptyList() throws Exception {
        when(messageService.getChannelMessages(testChannelId))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/messages/channels/{channelId}/messages", testChannelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("메시지 업데이트 성공 테스트")
    void updateMessage_Success() throws Exception {
        MessageDto updatedMessageDto = MessageDto.builder()
                .id(testMessageId)
                .channelId(testChannelId)
                .authorId(testAuthorId)
                .content("수정된 메시지")
                .build();

        when(messageService.updateMessage(eq(testMessageId), any(MessageDto.class)))
                .thenReturn(updatedMessageDto);

        mockMvc.perform(patch("/api/messages/{id}", testMessageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMessageDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testMessageId.toString()))
                .andExpect(jsonPath("$.content").value("수정된 메시지"));
    }
    
    @Test
    @DisplayName("메시지 업데이트 실패 테스트 - 메시지 없음")
    void updateMessage_NotFound() throws Exception {
        MessageDto updatedMessageDto = MessageDto.builder()
                .channelId(testChannelId)
                .content("수정된 메시지")
                .build();

        when(messageService.updateMessage(eq(testMessageId), any(MessageDto.class)))
                .thenThrow(new RestApiException(DomainErrorCode.MESSAGE_NOT_FOUND, "메시지를 찾을 수 없습니다."));

        mockMvc.perform(patch("/api/messages/{id}", testMessageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMessageDto)))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("메시지 업데이트 실패 테스트 - 유효하지 않은 입력")
    void updateMessage_InvalidInput() throws Exception {
        MessageDto invalidMessageDto = MessageDto.builder()
                .content("") // 빈 메시지 내용
                .build();

        mockMvc.perform(patch("/api/messages/{id}", testMessageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMessageDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메시지 삭제 성공 테스트")
    void deleteMessage_Success() throws Exception {
        doNothing().when(messageService).deleteMessage(testMessageId);

        mockMvc.perform(delete("/api/messages/{id}", testMessageId))
                .andExpect(status().isNoContent());
        
        verify(messageService).deleteMessage(testMessageId);
    }
    
    @Test
    @DisplayName("메시지 삭제 실패 테스트 - 메시지 없음")
    void deleteMessage_NotFound() throws Exception {
        doThrow(new RestApiException(DomainErrorCode.MESSAGE_NOT_FOUND, "메시지를 찾을 수 없습니다."))
                .when(messageService).deleteMessage(testMessageId);

        mockMvc.perform(delete("/api/messages/{id}", testMessageId))
                .andExpect(status().isNotFound());
    }
} 