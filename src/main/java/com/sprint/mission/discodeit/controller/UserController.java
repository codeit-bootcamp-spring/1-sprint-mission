package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserReadResponse;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Value("${file.upload.max-size:5MB}")  // ✅ 환경변수에서 파일 최대 크기 제한 (기본값: 5MB)
    private String maxFileSize;

    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<UserReadResponse> createUser(@RequestBody UserCreateRequest userCreateRequest) {
        UserReadResponse createdUser = userService.create(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PatchMapping(value = "/{userId}", consumes = "multipart/form-data")
    public ResponseEntity<Void> updateUser(
            @PathVariable UUID userId,
            @RequestPart("userUpdateRequest") String userUpdateRequestJson,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {

        try {
            UserUpdateRequest userUpdateRequest = objectMapper.readValue(userUpdateRequestJson, UserUpdateRequest.class);

            if (profile != null && !profile.isEmpty()) {
                UUID imageId = validateAndProcessImage(profile);
                userService.updateProfileImage(userId, imageId);
            }

            userService.update(userId, userUpdateRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
                .map(ResponseEntity::ok)  // ✅ 값이 존재하면 200 OK 응답
                .orElseGet(() -> ResponseEntity.notFound().build()); // ✅ 값이 없으면 404 Not Found 응답
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
                return ResponseEntity.badRequest().body("❌ 파일이 비어 있습니다.");
            }

            UUID imageId = validateAndProcessImage(file);
            userService.updateProfileImage(userId, imageId);

            return ResponseEntity.ok("✅ 파일 업로드 성공! 이미지 ID: " + imageId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ 파일 업로드 실패: " + e.getMessage());
        }
    }

    /**
     * ✅ 파일 유효성 검사 및 처리
     * @param file 업로드된 파일
     * @return UUID (파일 저장 후 ID)
     * @throws IOException 파일 처리 중 오류 발생 시
     */
    private UUID validateAndProcessImage(MultipartFile file) throws IOException {
        // ✅ 파일 크기 검사 (5MB 이하)
        long maxSizeInBytes = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSizeInBytes) {
            throw new IllegalArgumentException("❌ 파일 크기가 너무 큽니다. 최대 " + maxFileSize + "까지 허용됩니다.");
        }

        // ✅ MIME 타입 검사 (이미지 파일만 허용)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("❌ 올바른 이미지 파일을 업로드해주세요 (JPG, PNG, GIF 등)");
        }

        // ✅ 파일 확장자 검사 (jpg, png, gif만 허용)
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.matches(".*\\.(jpg|jpeg|png|gif)$")) {
            throw new IllegalArgumentException("❌ 지원되지 않는 파일 형식입니다. (jpg, jpeg, png, gif만 허용)");
        }

        // ✅ UUID 기반으로 파일 저장 (DB 또는 스토리지 연동 필요)
        UUID imageId = UUID.randomUUID();
        System.out.println("✅ 프로필 이미지 검증 완료: " + originalFilename + " → 저장 ID: " + imageId);

        return imageId;
    }
}
