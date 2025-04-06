package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

class UserApiIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 생성 및 조회 성공")
    void createAndFindUserSuccess() throws Exception {
        // Given
        UserCreateRequest request = new UserCreateRequest("testuser", "test@email.com", "password123!");

        // When
        MvcResult result = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.email").value("test@email.com"))
            .andReturn();

        String response = result.getResponse().getContentAsString();
        UserDto responseDto = objectMapper.readValue(response, UserDto.class);

        // Then
        User savedUser = userRepository.findById(responseDto.id()).orElseThrow();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("test@email.com");
    }

    @Test
    @DisplayName("중복된 이메일로 사용자 생성 실패")
    void createUserFailDuplicateEmail() throws Exception {
        // Given
        User existingUser = new User("existinguser", "test@email.com", "password123!", null);
        userRepository.save(existingUser);

        UserCreateRequest request = new UserCreateRequest("newuser", "test@email.com", "password123!");

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
        User user = new User("testuser", "test@email.com", "password123!", null);
        userRepository.save(user);

        UserUpdateRequest request = new UserUpdateRequest("newuser", "new@email.com", "newpassword123!");

        // When
        mockMvc.perform(put("/api/v1/users/{userId}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("newuser"))
            .andExpect(jsonPath("$.email").value("new@email.com"));

        // Then
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getUsername()).isEqualTo("newuser");
        assertThat(updatedUser.getEmail()).isEqualTo("new@email.com");
    }

    @Test
    @DisplayName("사용자 삭제 성공")
    void deleteUserSuccess() throws Exception {
        // Given
        User user = new User("testuser", "test@email.com", "password123!", null);
        userRepository.save(user);

        // When
        mockMvc.perform(delete("/api/v1/users/{userId}", user.getId()))
            .andExpect(status().isNoContent());

        // Then
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
} 