package discodeit.controller;

import discodeit.dto.user.*;
import discodeit.dto.userstatus.UpdateUserStatusRequest;
import discodeit.entity.User;
import discodeit.entity.UserStatus;
import discodeit.service.UserService;
import discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.create(request, Optional.empty());
        boolean online = userStatusService.findByUserId(user.getId())
                .isOnline();
        return ResponseEntity.ok(UserDto.fromDomain(user, online));
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUser(@RequestParam("id") UUID userId) {
        User user = userService.find(userId);
        boolean online = userStatusService.findByUserId(user.getId())
                .isOnline();
        return ResponseEntity.ok(UserDto.fromDomain(user, online));
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<UserDto> userDtoList = userService.findAll().stream()
                .map(user -> {
                    boolean online = userStatusService.findByUserId(user.getId()).isOnline();
                    return UserDto.fromDomain(user, online);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtoList);
    }

    @RequestMapping(value = "/users/phonenum", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updatePhoneNum(@RequestBody UpdatePhoneNumRequest request) {
        User user = userService.find(request.userId());
        user.updatePhoneNum(request.newPhoneNum());
        userStatusService.findByUserId(request.userId()).update(Instant.now());
        boolean online = userStatusService.findByUserId(user.getId())
                .isOnline();
        return ResponseEntity.ok(UserDto.fromDomain(user, online));
    }

    @RequestMapping(value = "users/password", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updatePassword(@RequestBody UpdatePasswordRequest request) {
        User user = userService.find(request.userId());
        user.updatePassword(request.oldPassword(), request.newPassword());
        userStatusService.findByUserId(request.userId()).update(Instant.now());
        boolean online = userStatusService.findByUserId(user.getId())
                .isOnline();
        return ResponseEntity.ok(UserDto.fromDomain(user, online));
    }

    @RequestMapping(value = "users/profile", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updateProfile(@RequestBody UpdateProfileRequest request) {
        User user = userService.find(request.userId());
        user.updateProfile(request.newProfileId());
        userStatusService.findByUserId(request.userId()).update(Instant.now());
        boolean online = userStatusService.findByUserId(user.getId())
                .isOnline();
        return ResponseEntity.ok(UserDto.fromDomain(user, online));
    }

    @RequestMapping(value = "users/{id}")
    public ResponseEntity<UserDto> updateUserStatus(@PathVariable("id") UUID userId, @RequestBody UpdateUserStatusRequest request) {
        User user = userService.find(userId);
        UserStatus userStatus = userStatusService.findByUserId(userId);
        userStatus.update(request.newLastActiveAt());
        boolean online = userStatusService.findByUserId(user.getId())
                .isOnline();
        return ResponseEntity.ok(UserDto.fromDomain(user, online));
    }

    @RequestMapping(value = "/users")
    public ResponseEntity<String> deleteUser(@RequestParam("id") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.ok("사용자 ID: " + userId + " delete Complete!!");
    }
}
