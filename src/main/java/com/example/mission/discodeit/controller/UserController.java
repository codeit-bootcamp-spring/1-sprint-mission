package com.example.mission.discodeit.controller;

import com.example.mission.discodeit.entity.Channel;
import com.example.mission.discodeit.entity.User;
import com.example.mission.discodeit.dto.UserDto;
import com.example.mission.discodeit.service.ChannelService;
import com.example.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ChannelService channelService;

    @GetMapping("/{userId}/channels")
    public ResponseEntity<List<Channel>> getUserChannels(@PathVariable UUID userId) {
        List<Channel> channels = channelService.getUserChannels(userId);
        return ResponseEntity.ok(channels);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.registerUser(userDto));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOnlineStatus(@PathVariable UUID id, @RequestParam boolean status) {
        userService.updateOnlineStatus(id, status);
        return ResponseEntity.noContent().build();
    }

}

