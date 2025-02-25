package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdatePasswordRequest;
import com.sprint.mission.discodeit.dto.user.UpdateProfileRequest;
import com.sprint.mission.discodeit.dto.user.UserDTO;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserDTO userDTO = userService.create(request);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUser(@RequestParam("id") UUID userId) {
        UserDTO userDTO = userService.find(userId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<UserDTO> all = userService.findAll();
        return ResponseEntity.ok(all);
    }

    @PutMapping("/user/password")
    public ResponseEntity<User> updatePassword(@RequestParam("id") UUID userId, @RequestBody UpdatePasswordRequest request) {
        User update = userService.updatePassword(userId, request);
        return ResponseEntity.ok(update);
    }

    @PutMapping( "/user/profile")
    public ResponseEntity<User> updateProfile(@RequestParam("id") UUID userId, @RequestBody UpdateProfileRequest request) {
        User update = userService.updateProfile(userId, request);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping( "/users")
    public ResponseEntity<String> deleteUser(@RequestParam("id") UUID id) {
        UserDTO delete = userService.delete(id);
        return ResponseEntity.ok(
                "Delete user ID: " + delete.getId() +
                        " name: " + delete.getName() +
                        " delete complete!"
        );
    }
}
