package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.Interface.UserService;
import com.sprint.mission.discodeit.service.Interface.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping
    public ResponseEntity<UserDto> createUser( @RequestParam("name") String name,
                                               @RequestParam("email") String email,
                                               @RequestParam("password") String password,
                                               @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        UserCreateRequestDto request = new UserCreateRequestDto(name, email, password, profileImage);

        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable UUID id,
            @RequestBody UserUpdateRequestDto request){
        return ResponseEntity.ok(userService.updateUser(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateUserStatus(@PathVariable UUID id,@RequestBody
    UserStatusUpdateRequest request) {
        userStatusService.updateByUserId(id,request);
        return ResponseEntity.ok("UserStatus updated successfully");
    }
}
