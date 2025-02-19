package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    // ✅ 특정 파일 조회
    @GetMapping("/{fileId}")
    public ResponseEntity<BinaryContentDTO> getFile(@PathVariable UUID fileId) {
        Optional<BinaryContentDTO> file = binaryContentService.read(fileId);
        return file.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ 모든 파일 조회
    @GetMapping
    public ResponseEntity<List<BinaryContentDTO>> getAllFiles() {
        return ResponseEntity.ok(binaryContentService.readAll());
    }

    // ✅ 파일 업로드 (ownerId가 없으면 기본값 생성)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "ownerId", required = false) UUID ownerId // ✅ 필수 X
    ) {
        try {
            if (ownerId == null) {
                ownerId = UUID.randomUUID(); // ✅ 기본값 설정 (임시 UUID 생성)
            }

            UUID fileId = binaryContentService.upload(file, ownerId);
            return ResponseEntity.ok("✅ 파일 업로드 성공! 파일 ID: " + fileId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ 파일 업로드 실패: " + e.getMessage());
        }
    }
}
