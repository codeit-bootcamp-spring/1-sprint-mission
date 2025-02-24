package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UserStatusDTO;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ApiResponse<UserDTO.idResponse> createUser(
            @RequestPart("user") UserDTO.request reqUserDto,
            @RequestPart(value = "file", required = false) MultipartFile profile
    ) {
        return ApiResponse.created(userService.create(reqUserDto, profile));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ApiResponse<List<UserDTO.response>> getAllUsers() {
        return ApiResponse.ok(userService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResponse<UserDTO.response> getUser(@PathVariable Long id) {
        return ApiResponse.ok(userService.find(id));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserDTO.idResponse> updateUser(
            @PathVariable Long id,
            @RequestPart("update") UserDTO.request reqUserDto,
            @RequestPart(value = "file", required = false) MultipartFile updateProfile
    ) {
        return ApiResponse.ok(userService.update(UserDTO.update.builder()
                .id(id)
                .userReqDTO(reqUserDto)
                .profile(updateProfile)
                .build()
        ));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiResponse<UserDTO.idResponse> deleteUser(@PathVariable Long id) {
        return ApiResponse.ok(userService.delete(id));
    }

    // 사용자의 온라인 상태 업데이트 (UserStatus)
    // - 현재는 업데이트만 존재하기에 User에 넣어둠 (확장 시 따로 분리 가능)
    @RequestMapping(value = "/{userId}/status", method = RequestMethod.PUT)
    public ApiResponse<UserStatusDTO.idResponse> updateUserStatus(@PathVariable Long userId, @RequestParam("accessAt") Instant accessAt) {
        UUID userUUID = userService.find(userId).uuid();
        Long statusId = userStatusService.findByUserId(userUUID).id();
        return ApiResponse.ok(userStatusService.update(UserStatusDTO.update.builder()
                .userId(userUUID)
                .id(statusId)
                .accessedAt(accessAt)
                .build()
        ));
    }
}
