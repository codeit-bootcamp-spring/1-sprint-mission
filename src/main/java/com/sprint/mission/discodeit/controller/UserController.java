package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.CustomApiResponse;
import com.sprint.mission.discodeit.docs.UserControllerDocs;
import com.sprint.mission.discodeit.dto.CommonDTO;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

// TODO: Mapping시 /api/v1/users 처럼 버전 추가 (확장성을 위함)
// -> 프론트 코드,,, 내가 고치고 싶지 않아서 일단 지금은 냅두는 것

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public CustomApiResponse<CommonDTO.idResponse> createUser(
            @RequestPart("user") UserDTO.request reqUserDto,
            @RequestPart(value = "file", required = false) MultipartFile profile
    ) {
        return CustomApiResponse.created(userService.create(reqUserDto, profile));
    }

    @GetMapping
    public CustomApiResponse<List<UserDTO.response>> getAllUsers() {
        return CustomApiResponse.ok(userService.findAll());
    }

    @GetMapping("/{userId}")
    public CustomApiResponse<UserDTO.response> getUser(@PathVariable Long userId) {
        return CustomApiResponse.ok(userService.find(userId));
    }

    @PutMapping(path = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CustomApiResponse<CommonDTO.idResponse> updateUser(
            @PathVariable Long userId,
            @RequestPart("update") UserDTO.request reqUserDto,
            @RequestPart(value = "file", required = false) MultipartFile updateProfile
    ) {
        return CustomApiResponse.ok(userService.update(userId, reqUserDto, updateProfile));
    }

    @DeleteMapping(path = "/{userId}")
    public CustomApiResponse<CommonDTO.idResponse> deleteUser(@PathVariable Long userId) {
        return CustomApiResponse.ok(userService.delete(userId));
    }

    // 사용자의 온라인 상태 업데이트 (UserStatus)
    // - 현재는 업데이트만 존재하기에 User에 넣어둠 (확장 시 따로 분리 가능)
    @PutMapping(path = "/{userId}/status")
    public CustomApiResponse<CommonDTO.idResponse> updateUserStatus(@PathVariable Long userId, @RequestParam("accessAt") Instant accessAt) {
        UUID userUUID = userService.find(userId).uuid();
        return CustomApiResponse.ok(userStatusService.update(userStatusService.findByUserId(userUUID).id(), userUUID, accessAt));
    }
}
