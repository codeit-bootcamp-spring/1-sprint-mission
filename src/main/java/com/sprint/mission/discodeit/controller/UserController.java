package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.CustomApiResponse;
import com.sprint.mission.discodeit.docs.UserControllerDocs;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/// / TODO: Mapping시 /api/v1/users 처럼 버전 추가 (확장성을 위함)
/// / -> 프론트 코드,,, 내가 고치고 싶지 않아서 일단 지금은 냅두는 것

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public CustomApiResponse<UserResponseDto> createUser(
            @RequestPart("user") UserSignupRequestDto reqUserDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        return CustomApiResponse.created(userService.create(reqUserDto, profile));
    }

    @GetMapping
    public CustomApiResponse<List<UserResponseDto>> getAllUsers() {
        return CustomApiResponse.ok(userService.findAll());
    }

    @GetMapping("/{userId}")
    public CustomApiResponse<UserResponseDto> getUser(@PathVariable UUID userId) {
        return CustomApiResponse.ok(userService.find(userId));
    }

    @PutMapping(path = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CustomApiResponse<UserResponseDto> updateUser(
            @PathVariable UUID userId,
            @RequestPart("update") UserUpdateDto userUpdateDto,
            @RequestPart(value = "file", required = false) MultipartFile updateProfile
    ) {
        return CustomApiResponse.ok(userService.update(userId, userUpdateDto, updateProfile));
    }

    @DeleteMapping(path = "/{userId}")
    public CustomApiResponse<UserResponseDto> deleteUser(@PathVariable UUID userId) {
        return CustomApiResponse.ok(userService.delete(userId));
    }

    @PutMapping(path = "/{userId}/userStatus")
    public CustomApiResponse<UserStatusResponseDto> updateUserStatus(
            @PathVariable UUID userId,
            @RequestParam("updateStatus") UserStatusRequestDto userStatusRequestDto) {
        return CustomApiResponse.ok(userStatusService.update(userId, userStatusRequestDto));
    }
}
