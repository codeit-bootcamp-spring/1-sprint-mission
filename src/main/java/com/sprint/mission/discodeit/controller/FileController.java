package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    // ✅ 파일 다운로드 API
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID fileId) {
        Resource file = fileService.downloadFile(fileId);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileId + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)  // ✅ 바이너리 파일 형식
                .body(file);
    }

    // ✅ 파일 삭제 API
    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable UUID fileId) {
        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.ok("✅ 파일 삭제 성공!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ 파일 삭제 실패: " + e.getMessage());
        }
    }
}
