package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontetnt.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest request,
                                   @RequestBody(required = false) CreateBinaryContentRequest binaryRequest) {
        return userService.createUser(request, Optional.ofNullable(binaryRequest));
    }

    @GetMapping("/{id}")
    public Optional<UserResponse> getUserById(@PathVariable UUID id) {
        return userService.findUserById(id);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.findAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @PatchMapping
    public Optional<UserResponse> updateUser(@RequestBody UpdateUserRequest request,
                                             @RequestBody(required = false) CreateBinaryContentRequest binaryRequest) {
        return userService.updateUser(request, Optional.ofNullable(binaryRequest));
    }
}
