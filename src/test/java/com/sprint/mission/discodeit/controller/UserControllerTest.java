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
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("사용자 생성 성공")
    void createUserSuccess() throws Exception {
        // Given
        UserCreateRequest request = new UserCreateRequest("testuser", "test@email.com", "password123!");
        UserDto response = new UserDto(UUID.randomUUID(), "testuser", "test@email.com", null);
        given(userService.create(any(UserCreateRequest.class), any())).willReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.email").value("test@email.com"));

        then(userService).should().create(any(UserCreateRequest.class), any());
    }

    @Test
    @DisplayName("중복된 이메일로 사용자 생성 실패")
    void createUserFailDuplicateEmail() throws Exception {
        // Given
        UserCreateRequest request = new UserCreateRequest("testuser", "test@email.com", "password123!");
        given(userService.create(any(UserCreateRequest.class), any()))
            .willThrow(new UserException.DuplicateEmailException("test@email.com"));

        // When & Then
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("사용자 정보 수정 성공")
    void updateUserSuccess() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest request = new UserUpdateRequest("newuser", "new@email.com", "newpassword123!");
        UserDto response = new UserDto(userId, "newuser", "new@email.com", null);
        given(userService.update(any(UUID.class), any(UserUpdateRequest.class), any())).willReturn(response);

        // When & Then
        mockMvc.perform(put("/api/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("newuser"))
            .andExpect(jsonPath("$.email").value("new@email.com"));
    }

    @Test
    @DisplayName("존재하지 않는 사용자 정보 수정 실패")
    void updateUserFailUserNotFound() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest request = new UserUpdateRequest("newuser", "new@email.com", "newpassword123!");
        given(userService.update(any(UUID.class), any(UserUpdateRequest.class), any()))
            .willThrow(new UserException.UserNotFoundException(userId));

        // When & Then
        mockMvc.perform(put("/api/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("사용자 삭제 성공")
    void deleteUserSuccess() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(delete("/api/v1/users/{userId}", userId))
            .andExpect(status().isNoContent());

        then(userService).should().delete(userId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 삭제 실패")
    void deleteUserFailUserNotFound() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        given(userService.delete(userId))
            .willThrow(new UserException.UserNotFoundException(userId));

        // When & Then
        mockMvc.perform(delete("/api/v1/users/{userId}", userId))
            .andExpect(status().isNotFound());
    }
} 