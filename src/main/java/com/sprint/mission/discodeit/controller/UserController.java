package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreateRequest request,
                                              @RequestParam Optional<BinaryContentCreateRequest> profileRequest) {
        User user = userService.create(request, profileRequest);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getCreatedAt(), user.getUpdatedAt(),
                user.getUsername(), user.getEmail(), user.getProfileId(), null));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.find(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID userId,
                                              @RequestBody UserUpdateRequest request,
                                              @RequestParam Optional<BinaryContentCreateRequest> profileRequest) {
        User updatedUser = userService.update(userId, request, profileRequest);
        return ResponseEntity.ok(new UserDto(updatedUser.getId(), updatedUser.getCreatedAt(), updatedUser.getUpdatedAt(),
                updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getProfileId(), null));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}