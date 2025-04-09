package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserStatusService userStatusService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("프로필을 첨부하지 않은 유저 생성 성공 시 201 응답을 반환한다.")
    void createUserSuccess() throws Exception {
        // given
        UserSignupRequestDto requestDto = new UserSignupRequestDto(
                "username", "nickname", "test@email.com", "password",
                "010-1234-5678", Phone.RegionCode.KR, User.UserType.COMMON, "자기소개"
        );

        UserResponseDto responseDto = new UserResponseDto(
                UUID.randomUUID(), "nickname", "test@email.com", null, true
        );

        MockMultipartFile userPart = new MockMultipartFile(
                "user", "user.json", "application/json",
                objectMapper.writeValueAsBytes(requestDto)
        );

        given(userService.create(any(), any())).willReturn(responseDto);

        // when & then
        mockMvc.perform(multipart("/api/users")
                        .file(userPart)
                        .content(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nickname").value("nickname"))
                .andExpect(jsonPath("$.data.email").value("test@email.com"));
    }

    @Test
    @DisplayName("유저 요청 시 필수 항목을 누락할 시 Validation 예외가 발생한다.")
    void createUserFailed() throws Exception {
        // given
        UserSignupRequestDto requestDto = new UserSignupRequestDto(
                "username", "nickname", "test@email.com", null,
                "010-1234-5678", Phone.RegionCode.KR, User.UserType.COMMON, "자기소개"
        );

        MockMultipartFile userPart = new MockMultipartFile(
                "user", "user.json", "application/json",
                objectMapper.writeValueAsBytes(requestDto)
        );

        // when & then
        mockMvc.perform(multipart("/api/users")
                        .file(userPart)
                        .content(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value(ErrorCode.INVALID_REQUEST.getCode()))
                .andExpect(jsonPath("$.error.details.validationError[0]")
                        .value(Matchers.containsString("password")));
    }

}