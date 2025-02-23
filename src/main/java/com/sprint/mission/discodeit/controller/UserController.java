package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
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

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateRequest request) {
        User user = userService.create(request, Optional.empty());
        return ResponseEntity.ok(new UserDTO(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUserName(),
                user.getEmail(),
                user.getProfileId(),
                false
        ));
    }

    @RequestMapping(value = "/update/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID userId, @RequestBody UserUpdateRequest request) {
        User user = userService.update(userId, request, Optional.empty());
        return ResponseEntity.ok(new UserDTO(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUserName(),
                user.getEmail(),
                user.getProfileId(),
                false
        ));
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(value = "/status/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUserStatus(@PathVariable UUID userId, @RequestBody UserStatusUpdateRequest request) {
        userStatusService.update(userId, request);
        return ResponseEntity.ok().build();
    }
}
