package com.spirnt.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageDto;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.dto.response.PageResponse;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.spirnt.mission.discodeit.exception.Message.MessageNotFoundException;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private MessageService messageService;

    @Test
    @DisplayName("POST /api/messages - 성공")
    void testCreateMessageSuccess() throws Exception {
        // given
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        String content = "안녕하세요";

        MessageCreateRequest request = new MessageCreateRequest(content, authorId, channelId);

        MessageDto messageDto = new MessageDto(
            UUID.randomUUID(),
            Instant.now(),
            Instant.now(),
            content,
            channelId,
            new UserDto(authorId, null, null, null, null, null, null),
            null
        );

        MockMultipartFile jsonPart = new MockMultipartFile(
            "messageCreateRequest",
            "",
            "application/json",
            new ObjectMapper().writeValueAsBytes(request)
        );

        when(messageService.create(any(), any())).thenReturn(messageDto);

        // when & then
        mockMvc.perform(multipart("/api/messages")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content").value("안녕하세요"))
            .andExpect(jsonPath("$.channelId").value(channelId.toString()));
    }

    @Test
    @DisplayName("POST /api/messages - 실패")
    void testCreateMessageFail() throws Exception {
        // given
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        String content = "실패";

        MessageCreateRequest request = new MessageCreateRequest(content, authorId, channelId);

        when(messageService.create(request, null)).thenThrow(
            new UserNotFoundException(Instant.now(), Map.of("userId", authorId)));

        MockMultipartFile jsonPart = new MockMultipartFile(
            "messageCreateRequest",
            "",
            "application/json",
            new ObjectMapper().writeValueAsBytes(request)
        );

        // when & then
        mockMvc.perform(multipart("/api/messages")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("USER_404"))
            .andExpect(jsonPath("$.exceptionType").value("UserNotFoundException"));
    }

    @Test
    @DisplayName("PATCH /api/messages/{messageId} - 성공")
    void testUpdateMessageSuccess() throws Exception {
        // given
        UUID messageId = UUID.randomUUID();
        String newContent = "수정된 메시지입니다";

        MessageUpdateRequest updateRequest = new MessageUpdateRequest(newContent);

        MessageDto updatedMessage = new MessageDto(
            messageId,
            Instant.now(),
            Instant.now(),
            newContent,
            UUID.randomUUID(),
            new UserDto(UUID.randomUUID(), null, null, null, null, null, null),
            null
        );

        when(messageService.update(eq(messageId), any(MessageUpdateRequest.class))).thenReturn(
            updatedMessage);

        // when & then
        mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(messageId.toString()))
            .andExpect(jsonPath("$.content").value("수정된 메시지입니다"));
    }

    @Test
    @DisplayName("PATCH /api/messages/{messageId} - 실패")
    void testUpdateMessageFail() throws Exception {
        // given
        UUID messageId = UUID.randomUUID();
        String newContent = "수정된 메시지입니다";

        MessageUpdateRequest updateRequest = new MessageUpdateRequest(newContent);

        when(messageService.update(eq(messageId), any(MessageUpdateRequest.class))).thenThrow(
            new MessageNotFoundException(Instant.now(), Map.of("messageId", messageId)));

        // when & then
        mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateRequest)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("MESSAGE_404"))
            .andExpect(jsonPath("$.exceptionType").value("MessageNotFoundException"));
    }

    @Test
    @DisplayName("DELETE /api/messages/{messageId} - 성공")
    void testDeleteMessageSuccess() throws Exception {
        // given
        UUID messageId = UUID.randomUUID();

        doNothing().when(messageService).delete(messageId);

        // when & then
        mockMvc.perform(delete("/api/messages/{messageId}", messageId))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/messages/{messageId} - 실패")
    void testDeleteMessageFail() throws Exception {
        // given
        UUID messageId = UUID.randomUUID();

        doThrow(new MessageNotFoundException(Instant.now(), Map.of("messageId", messageId))).when(
            messageService).delete(messageId);

        // when & then
        mockMvc.perform(delete("/api/messages/{messageId}", messageId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("MESSAGE_404"))
            .andExpect(jsonPath("$.exceptionType").value("MessageNotFoundException"));
    }

    @Test
    @DisplayName("GET /api/messages - 성공")
    void testGetAllMessagesByChannelSuccess() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();
        Instant now = Instant.now();

        MessageDto message1 = new MessageDto(
            UUID.randomUUID(), now, now, "첫 번째 메시지", channelId, null, null);
        MessageDto message2 = new MessageDto(
            UUID.randomUUID(), now.minusSeconds(10), now.minusSeconds(10), "두 번째 메시지", channelId,
            null,
            null);

        PageResponse<MessageDto> pageResponse = new PageResponse<>(
            List.of(message1, message2), message2.getCreatedAt(), 2, true, 0);

        when(messageService.findAllByChannelId(eq(channelId), isNull(), any(Pageable.class)))
            .thenReturn(pageResponse);

        // when & then
        mockMvc.perform(get("/api/messages")
                .param("channelId", channelId.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size").value(2))
            .andExpect(jsonPath("$.content[0].content").value("첫 번째 메시지"))
            .andExpect(jsonPath("$.content[1].content").value("두 번째 메시지"))
            .andExpect(jsonPath("$.hasNext").value(true));
    }

    @Test
    @DisplayName("GET /api/messages - 실패")
    void testGetAllMessagesByChannelFail() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();

        when(messageService.findAllByChannelId(eq(channelId), isNull(), any(Pageable.class)))
            .thenThrow(new ChannelNotFoundException(Instant.now(), Map.of("channelId", channelId)));

        // when & then
        mockMvc.perform(get("/api/messages")
                .param("channelId", channelId.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("CHANNEL_404"))
            .andExpect(jsonPath("$.exceptionType").value("ChannelNotFoundException"));
    }
}
