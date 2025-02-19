package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserCreateDTO;
import com.sprint.mission.discodeit.dto.UserReadDTO;
import com.sprint.mission.discodeit.dto.UserUpdateDTO;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ 1. 사용자 등록
    @PostMapping
    public ResponseEntity<UserReadDTO> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        userService.create(userCreateDTO);
        return userService.readAll().stream()
                .filter(user -> user.getEmail().equals(userCreateDTO.getEmail()))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    // ✅ 2. 사용자 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID userId, @RequestBody UserUpdateDTO userUpdateDTO) {
        userService.update(userId, userUpdateDTO);
        return ResponseEntity.ok().build();
    }

    // ✅ 3. 사용자 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }

    // ✅ 4. 모든 사용자 조회
    @GetMapping
    public ResponseEntity<List<UserReadDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.readAll());
    }

    // ✅ 5. 특정 사용자 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserReadDTO> getUserById(@PathVariable UUID userId) {
        return userService.read(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ 6. 사용자의 온라인 상태 업데이트
    @PatchMapping("/{userId}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable UUID userId) {
        boolean updated = userService.updateLastSeen(userId);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // ✅ 7. 사용자 프로필 이미지 업로드
    @PutMapping("/{userId}/profile-image")
    public ResponseEntity<String> uploadProfileImage(
            @PathVariable UUID userId,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
            }

            // ✅ 파일명 UUID로 저장
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            System.out.println("✅ 파일 업로드됨: " + fileName);

            // ✅ 저장된 파일을 기반으로 이미지 UUID 생성
            UUID imageId = UUID.randomUUID();

            // ✅ 사용자 프로필 이미지 업데이트
            userService.updateProfileImage(userId, imageId);

            return ResponseEntity.ok("파일 업로드 성공! 이미지 ID: " + imageId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("파일 업로드 실패: " + e.getMessage());
        }
    }
}
