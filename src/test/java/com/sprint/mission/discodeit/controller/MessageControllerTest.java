package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.MessageService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessageService messageService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("파일을 첨부한 메시지 생성 성공 시 올바른 응답을 반환한다.")
    void createUserSuccess() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        MessageRequestDto requestDto = new MessageRequestDto(
                "message", authorId, channelId
        );
        UserResponseDto userResponseDto = mock(UserResponseDto.class);

        List<MockMultipartFile> attachmentsPart = List.of(
                new MockMultipartFile("attachment1", "attachment1.png", "image/png", "fake-image".getBytes()),
                new MockMultipartFile("attachment2", "attachment2.png", "image/png", "fake-image".getBytes()),
                new MockMultipartFile("attachment3", "attachment3.png", "image/png", "fake-image".getBytes())
        );

        List<BinaryContentResponseDto> binaryContentResponseDtos = List.of(
                mock(BinaryContentResponseDto.class),
                mock(BinaryContentResponseDto.class),
                mock(BinaryContentResponseDto.class)
        );

        MockMultipartFile messagePart = new MockMultipartFile(
                "message", "message.json", "application/json",
                objectMapper.writeValueAsBytes(requestDto)
        );

        MessageResponseDto responseDto = new MessageResponseDto(
                UUID.randomUUID(), Instant.now(), Instant.now(), requestDto.content(), channelId,
                userResponseDto, binaryContentResponseDtos
        );

        given(messageService.create(any(), any())).willReturn(responseDto);

        MockMultipartHttpServletRequestBuilder requestBuilder = multipart("/api/messages")
                .file(messagePart);
        for (MockMultipartFile attachment : attachmentsPart) {
            requestBuilder.file(attachment);
        }

        // when & then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").value("message"))
                .andExpect(jsonPath("$.data.channelId").value(channelId.toString()));
    }

    @Test
    @DisplayName("메시지 생성 요청 시 필수 항목을 누락할 시 Validation 예외가 발생한다.")
    void createUserFailed() throws Exception {
        // given
        UUID authorId = UUID.randomUUID();
        MessageRequestDto requestDto = new MessageRequestDto(
                "message", authorId, null
        );
        UserResponseDto userResponseDto = mock(UserResponseDto.class);

        MockMultipartFile messagePart = new MockMultipartFile(
                "message", "message.json", "application/json",
                objectMapper.writeValueAsBytes(requestDto)
        );

        MockMultipartHttpServletRequestBuilder requestBuilder = multipart("/api/messages")
                .file(messagePart);

        // when & then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value(ErrorCode.INVALID_REQUEST.getCode()))
                .andExpect(jsonPath("$.error.details.validationError[0]")
                        .value(Matchers.containsString("channelId")));
    }


}