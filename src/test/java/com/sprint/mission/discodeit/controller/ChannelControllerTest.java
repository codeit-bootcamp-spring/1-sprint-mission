package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.ChannelService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChannelService channelService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("요청한 값에 따라 공개 채널을 생성한다.")
    void createPublicChannel() throws Exception {
        // given
        PublicChannelRequestDto requestDto = new PublicChannelRequestDto(
                "serverName", null, UUID.randomUUID()
        );

        ChannelResponseDto responseDto = new ChannelResponseDto(
                UUID.randomUUID(), Channel.ChannelType.PUBLIC, requestDto.serverName(),
                requestDto.description(), null, Instant.now()
        );

        given(channelService.createPublicChannel(any())).willReturn(responseDto);

        mockMvc.perform(multipart("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.type").value("PUBLIC"))
                .andExpect(jsonPath("$.data.name").value("serverName"));
    }

    @Test
    @DisplayName("채널 생성 시 필수 항목을 누락하면 Validation 예외가 발생한다.")
    void createPublicChannelFailed() throws Exception {
        // given
        PublicChannelRequestDto requestDto = new PublicChannelRequestDto(
                null, null, UUID.randomUUID()
        );

        mockMvc.perform(multipart("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value(ErrorCode.INVALID_REQUEST.getCode()))
                .andExpect(jsonPath("$.error.details.validationError[0]")
                        .value(Matchers.containsString("serverName")));
    }

}