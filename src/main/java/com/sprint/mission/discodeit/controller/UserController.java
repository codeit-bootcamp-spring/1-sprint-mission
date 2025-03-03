package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserReadResponse;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
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
    private final ObjectMapper objectMapper; // JSON 변환용

    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<UserReadResponse> createUser(@RequestBody UserCreateRequest userCreateRequest) {
        UserReadResponse createdUser = userService.create(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // 업데이트 API: multipart/form-data로 "userUpdateRequest" 파트와 선택적 "profile" 파트를 처리
    @PatchMapping(value = "/{userId}", consumes = "multipart/form-data")
    public ResponseEntity<Void> updateUser(
            @PathVariable UUID userId,
            @RequestPart("userUpdateRequest") String userUpdateRequestJson,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {

        try {
            // JSON 문자열을 UserUpdateRequest 객체로 변환 (DTO는 newUsername, newEmail, newPassword 필드를 포함)
            UserUpdateRequest userUpdateRequest = objectMapper.readValue(userUpdateRequestJson, UserUpdateRequest.class);

            // 프로필 파일이 첨부되었으면 처리 (예: 파일 저장 후 imageId 생성)
            if (profile != null && !profile.isEmpty()) {
                UUID imageId = UUID.randomUUID();
                userService.updateProfileImage(userId, imageId);
            }

            userService.update(userId, userUpdateRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserReadResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.readAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserReadResponse> getUserById(@PathVariable UUID userId) {
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
            UUID imageId = UUID.randomUUID();
            userService.updateProfileImage(userId, imageId);
            return ResponseEntity.ok("파일 업로드 성공! 이미지 ID: " + imageId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
        }
    }
}
