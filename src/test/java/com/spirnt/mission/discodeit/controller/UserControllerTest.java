package com.spirnt.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserStatusService userStatusService;

    @Test
    @DisplayName("GET /api/users - 성공")
    void testGetAllUsers() throws Exception {
        // given
        UserDto userDto1 = new UserDto(null, null, null, "Alice", "Alice@mail.com", true, null);
        UserDto userDto2 = new UserDto(null, null, null, "Bob", "Bob@mail.com", true, null);
        UserDto userDto3 = new UserDto(null, null, null, "Cindy", "Cindy@mail.com", true, null);
        when(userService.findAll()).thenReturn(List.of(userDto1, userDto2, userDto3));

        // when & then
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].username").value("Alice"))
            .andExpect(jsonPath("$[0].email").value("Alice@mail.com"))
            .andExpect(jsonPath("$[0].online").value(true))
            .andExpect(jsonPath("$[1].username").value("Bob"))
            .andExpect(jsonPath("$[1].email").value("Bob@mail.com"))
            .andExpect(jsonPath("$[1].online").value(true))
            .andExpect(jsonPath("$[2].username").value("Cindy"))
            .andExpect(jsonPath("$[2].email").value("Cindy@mail.com"))
            .andExpect(jsonPath("$[2].online").value(true));
    }

    @Test
    @DisplayName("POST /api/users - 성공")
    void testPostUser() throws Exception {
        UserCreateRequest userCreateRequest = new UserCreateRequest("name", "email@mail.com",
            "password");
        UserDto userDto = new UserDto(null, null, null, "name", "email@mail.com", null, null);
        when(userService.create(userCreateRequest, null)).thenReturn(userDto);

        // RequestPart로 받기 때문에 media type을 json이 아니라 multipartfile form data로 해야 함
        MockMultipartFile jsonPart = new MockMultipartFile(
            "userCreateRequest",
            "",
            "application/json",
            new ObjectMapper().writeValueAsBytes(userCreateRequest)
        );

        mockMvc.perform(multipart("/api/users")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("name"))
            .andExpect(jsonPath("$.email").value("email@mail.com"));
    }

    @Test
    @DisplayName("POST /api/users - 실패")
    void testPostUserFail() throws Exception {
        UserCreateRequest userCreateRequest = new UserCreateRequest(null, "email@mail.com",
            "password");

        // RequestPart로 받기 때문에 media type을 json이 아니라 multipartfile form data로 해야 함
        MockMultipartFile jsonPart = new MockMultipartFile(
            "userCreateRequest",
            "",
            "application/json",
            new ObjectMapper().writeValueAsBytes(userCreateRequest)
        );

        mockMvc.perform(multipart("/api/users")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("InvalidMethodArgumentException"))
            .andExpect(jsonPath("$.exceptionType").value("MethodArgumentNotValidException"));
    }


    @Test
    @DisplayName("PATCH /api/users/{userId} - 성공)")
    void testPatchUser() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest updateRequest = new UserUpdateRequest("newname", "newemail@mail.com",
            "newpassword");
        UserDto userDto = new UserDto(userId, null, null, "newname", "newemail@mail.com", true,
            null);

        when(userService.update(eq(userId), any(UserUpdateRequest.class), any())).thenReturn(
            userDto);

        // multipart json 데이터 생성
        MockMultipartFile jsonPart = new MockMultipartFile(
            "userUpdateRequest", "", "application/json",
            new ObjectMapper().writeValueAsBytes(updateRequest)
        );

        // when & then
        mockMvc.perform(multipart("/api/users/{userId}", userId)
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(request -> {
                    request.setMethod("PATCH");
                    return request;
                })) // PATCH로 변경
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("newname"))
            .andExpect(jsonPath("$.email").value("newemail@mail.com"));
    }

    @Test
    @DisplayName("PATCH /api/users/{userId} - 실패)")
    void testPatchUserFail() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest updateRequest = new UserUpdateRequest("newname", "newemail@mail.com",
            "newpassword");
        when(userService.update(eq(userId), any(UserUpdateRequest.class), any())).thenThrow(
            new UserNotFoundException(
                Instant.now(),
                Map.of("userId", userId)
            ));

        // multipart json 데이터 생성
        MockMultipartFile jsonPart = new MockMultipartFile(
            "userUpdateRequest", "", "application/json",
            new ObjectMapper().writeValueAsBytes(updateRequest)
        );

        // when & then
        mockMvc.perform(multipart("/api/users/{userId}", userId)
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(request -> {
                    request.setMethod("PATCH");
                    return request;
                }))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("USER_404"))
            .andExpect(jsonPath("$.exceptionType").value("UserNotFoundException"));
    }

    @Test
    @DisplayName("DELETE /api/users/{userId} - 성공")
    void testDeleteUserSuccess() throws Exception {
        // given
        UUID userId = UUID.randomUUID();

        // when & then
        mockMvc.perform(delete("/api/users/{userId}", userId))
            .andExpect(status().isNoContent());

        verify(userService).delete(userId);
    }

    @Test
    @DisplayName("DELETE /api/users/{userId} - 실패")
    void testDeleteUserFail() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        // void 반환은 when().thenThrow가 아니라 doThrow()
        doThrow(new UserNotFoundException(
            Instant.now(),
            Map.of("userId", userId)
        )).when(userService).delete(eq(userId));

        // when & then
        mockMvc.perform(delete("/api/users/{userId}", userId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("USER_404"))
            .andExpect(jsonPath("$.exceptionType").value("UserNotFoundException"));
    }
}
