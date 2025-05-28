package com.spirnt.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spirnt.mission.discodeit.dto.channel.ChannelDto;
import com.spirnt.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.spirnt.mission.discodeit.entity.ChannelType;
import com.spirnt.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(ChannelController.class)
public class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ChannelService channelService;

    @Test
    @DisplayName("POST /api/channels/public - 성공")
    void testCreatePublicChannelSuccess() throws Exception {
        // given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("Ch1", "this is ch1");
        ChannelDto response = new ChannelDto(
            UUID.randomUUID(),
            request.name(),
            request.description(),
            ChannelType.PUBLIC,
            null,
            Instant.now()
        );

        when(channelService.createChannelPublic(any(PublicChannelCreateRequest.class))).thenReturn(
            response);

        // when & then
        mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Ch1"))
            .andExpect(jsonPath("$.description").value("this is ch1"))
            .andExpect(jsonPath("$.type").value("PUBLIC"));
    }

    @Test
    @DisplayName("POST /api/channels/public - 실패")
    void testCreatePublicChannelFail() throws Exception {
        // given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(null, null);

        // when & then
        mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("InvalidMethodArgumentException"))
            .andExpect(jsonPath("$.exceptionType").value("MethodArgumentNotValidException"));
    }

    @Test
    @DisplayName("POST /api/channels/private - 성공")
    void testCreatePrivateChannelSuccess() throws Exception {
        // given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
            List.of(userId1, userId2));
        ChannelDto response = new ChannelDto(
            UUID.randomUUID(),
            null, null,
            ChannelType.PRIVATE,
            null,
            Instant.now()
        );

        when(channelService.createChannelPrivate(any(PrivateChannelCreateRequest.class)))
            .thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/channels/private")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.type").value("PRIVATE"));
    }

    @Test
    @DisplayName("POST /api/channels/private - 실패")
    void testCreatePrivateChannelFail() throws Exception {
        // given
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
            null);

        // when & then
        mockMvc.perform(post("/api/channels/private")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("InvalidMethodArgumentException"))
            .andExpect(jsonPath("$.exceptionType").value("MethodArgumentNotValidException"));
    }

    @Test
    @DisplayName("PATCH /api/channels/{channelId} - 공개 채널 수정 성공")
    void testUpdatePublicChannelSuccess() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("UpdatedName",
            "Updated description");

        ChannelDto updatedChannel = new ChannelDto(
            channelId,
            request.newName(),
            request.newDescription(),
            ChannelType.PUBLIC,
            null,
            Instant.now()
        );

        when(channelService.update(eq(channelId), any(PublicChannelUpdateRequest.class)))
            .thenReturn(updatedChannel);

        // when & then
        mockMvc.perform(patch("/api/channels/" + channelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(channelId.toString()))
            .andExpect(jsonPath("$.name").value("UpdatedName"))
            .andExpect(jsonPath("$.description").value("Updated description"))
            .andExpect(jsonPath("$.type").value("PUBLIC"));
    }

    @Test
    @DisplayName("PATCH /api/channels/{channelId} - 공개 채널 수정 실패")
    void testUpdatePublicChannelFail() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("UpdatedName",
            "Updated description");

        when(channelService.update(eq(channelId), any(PublicChannelUpdateRequest.class)))
            .thenThrow(new ChannelNotFoundException(Instant.now(), Map.of("channelId", channelId)));

        // when & then
        mockMvc.perform(patch("/api/channels/" + channelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("CHANNEL_404"))
            .andExpect(jsonPath("$.exceptionType").value("ChannelNotFoundException"));
    }

    @Test
    @DisplayName("DELETE /api/channels/{channelId} - 채널 삭제 성공")
    void testDeleteChannelSuccess() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();

        // doNothing은 void 리턴 메서드에 사용
        doNothing().when(channelService).delete(channelId);

        // when & then
        mockMvc.perform(delete("/api/channels/" + channelId))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/channels/{channelId} - 채널 삭제 실패")
    void testDeleteChannelFail() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();

        doThrow(new ChannelNotFoundException(
            Instant.now(),
            Map.of("channelId", channelId)
        )).when(channelService).delete(eq(channelId));

        // when & then
        mockMvc.perform(delete("/api/channels/" + channelId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("CHANNEL_404"))
            .andExpect(jsonPath("$.exceptionType").value("ChannelNotFoundException"));
    }

    @Test
    @DisplayName("GET /api/channels?userId - 성공")
    void testGetAllChannelsByUserIdSuccess() throws Exception {
        // given
        UUID userId = UUID.randomUUID();

        ChannelDto ch1 = new ChannelDto(UUID.randomUUID(), "Ch1", "d1", ChannelType.PUBLIC, null,
            Instant.now());
        ChannelDto ch2 = new ChannelDto(UUID.randomUUID(), "Ch2", "d2", ChannelType.PUBLIC, null,
            Instant.now());

        when(channelService.findAllByUserId(userId)).thenReturn(List.of(ch1, ch2));

        // when & then
        mockMvc.perform(get("/api/channels")
                .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Ch1"))
            .andExpect(jsonPath("$[1].name").value("Ch2"));
    }

    @Test
    @DisplayName("GET /api/channels?userId - 실패")
    void testGetAllChannelsByUserIdFail() throws Exception {
        // given
        UUID userId = UUID.randomUUID();

        when(channelService.findAllByUserId(userId)).thenThrow(
            new UserNotFoundException(Instant.now(), Map.of("userId", userId)));

        // when & then
        mockMvc.perform(get("/api/channels")
                .param("userId", userId.toString()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("USER_404"))
            .andExpect(jsonPath("$.exceptionType").value("UserNotFoundException"));
    }
}
