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

    // 특정 사용자가 속한 채널 목록 조회
    @GetMapping("/{userId}/channels")
    public ResponseEntity<List<Channel>> getUserChannels(@PathVariable UUID userId) {
        List<Channel> channels = channelService.getUserChannels(userId);
        return ResponseEntity.ok(channels);
    }

    // 사용자 등록
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.registerUser(userDto));
    }

    // 모든 사용자 조회
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 특정 사용자 조회
    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // 사용자 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    // 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 온라인 상태 업데이트
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOnlineStatus(@PathVariable UUID id, @RequestParam boolean status) {
        userService.updateOnlineStatus(id, status);
        return ResponseEntity.noContent().build();
    }

}

