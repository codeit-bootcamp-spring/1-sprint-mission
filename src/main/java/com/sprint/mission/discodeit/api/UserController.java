package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    public UserController(UserService userService, UserStatusService userStatusService) {
        this.userService = userService;
        this.userStatusService = userStatusService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request) {
        User user = userService.create(request, Optional.empty());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable UUID userId, @RequestBody UserUpdateRequest request) {
        User updatedUser = userService.update(userId, request, Optional.empty());
        return ResponseEntity.ok(updatedUser);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/{userId}/status", method = RequestMethod.PUT)
    public ResponseEntity<UserStatus> updateUserStatus(@PathVariable UUID userId, @RequestBody UserStatusUpdateRequest request) {
        UserStatus updatedStatus = userStatusService.updateByUserId(userId, request);
        return ResponseEntity.ok(updatedStatus);
    }
}
