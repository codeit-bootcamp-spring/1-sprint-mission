package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.dto.user.userStatus.UserStatusCreate;
import com.sprint.mission.discodeit.dto.user.userStatus.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreate userCreate, @RequestBody(required = false)BinaryContentCreateDto profileCreateDto) {
        User user = userService.createUser(userCreate, Optional.ofNullable(profileCreateDto));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
        UserDto userDto = userService.findById(userId);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable UUID userId, @RequestBody UserUpdate userUpdate, @RequestBody(required = false) BinaryContentCreateDto profileCreateDto) {
        User updateUser = userService.update(userId, userUpdate, Optional.ofNullable(profileCreateDto));
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId){
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    //UserStatus
    @PostMapping("/{userId}/status")
    public ResponseEntity<UserStatus> createUserStatus(@PathVariable UUID userId, @RequestBody UserStatusCreate userStatusCreate) {
        UserStatus userStatus = userStatusService.create(userStatusCreate);
        return ResponseEntity.ok(userStatus);
    }
    @GetMapping("/{userId}/status")
    public ResponseEntity<UserStatus> getUserStatus(@PathVariable UUID userId) {
        UserStatus userStatus = userStatusService.updateByUserId(userId, new UserStatusUpdate(null, null));
        return ResponseEntity.ok(userStatus);
    }
    @PutMapping("/{userId}/status")
    public ResponseEntity<UserStatus> updateUserStatus(@PathVariable UUID userId, @RequestBody UserStatusUpdate userStatusUpdate) {
        UserStatus updateStatus = userStatusService.updateByUserId(userId, userStatusUpdate);
        return ResponseEntity.ok(updateStatus);
    }
    @DeleteMapping("/{userId}/status")
    public ResponseEntity<Void> deleteUserStatus(@PathVariable UUID userId) {
        UserStatus userStatus = userStatusService.updateByUserId(userId, new UserStatusUpdate(null, null));
        userStatusService.delete(userStatus.getId());
        return ResponseEntity.noContent().build();
    }
}
