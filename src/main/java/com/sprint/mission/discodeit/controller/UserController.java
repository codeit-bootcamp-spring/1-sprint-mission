package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserCreateDTO;
import com.sprint.mission.discodeit.dto.UserReadDTO;
import com.sprint.mission.discodeit.dto.UserUpdateDTO;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 등록: 새 사용자 정보를 생성 후 바로 반환 (HTTP 201)
    @PostMapping
    public ResponseEntity<UserReadDTO> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        UserReadDTO createdUser = userService.create(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID userId, @RequestBody UserUpdateDTO userUpdateDTO) {
        userService.update(userId, userUpdateDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserReadDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.readAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserReadDTO> getUserById(@PathVariable UUID userId) {
        return userService.read(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable UUID userId) {
        boolean updated = userService.updateLastSeen(userId);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{userId}/profile-image")
    public ResponseEntity<String> uploadProfileImage(
            @PathVariable UUID userId,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            System.out.println("✅ 파일 업로드됨: " + fileName);

            UUID imageId = UUID.randomUUID();
            userService.updateProfileImage(userId, imageId);

            return ResponseEntity.ok("파일 업로드 성공! 이미지 ID: " + imageId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("파일 업로드 실패: " + e.getMessage());
        }
    }
}
