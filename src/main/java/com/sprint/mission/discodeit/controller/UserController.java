package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @GetMapping
    public List<UserResponse> getAllUser() {
        return userService.findAll();
    }

    @PostMapping
    public UserResponse createUser(
            @RequestPart("userRequest") UserRequest userRequest,
            @RequestPart(value="image", required = false) MultipartFile userProfileImage
            ) {
        return userService.createUser(userRequest, userProfileImage);
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(
            @PathVariable UUID userId,
            @RequestPart("userRequest") UserRequest userRequest,
            @RequestPart(value="image", required = false) MultipartFile userProfileImage
            ) {
        return userService.update(userId, userRequest, userProfileImage);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable UUID userId) {
        userService.deleteById(userId);
        return "delete ok";
    }

    @PutMapping("/{userId}/status")
    public String updateUserStatus(@PathVariable UUID userId) {
        userStatusService.updateByUserId(userId);
        return "user status update ok";
    }

}
