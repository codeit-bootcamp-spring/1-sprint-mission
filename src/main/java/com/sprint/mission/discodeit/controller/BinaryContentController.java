package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @PostMapping
    public ResponseEntity<BinaryContent> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            BinaryContent binaryContent = binaryContentService.saveFile(file);
            return ResponseEntity.ok(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
        }
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable("id") UUID id) {
        BinaryContent binaryContent = binaryContentService.find(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(binaryContent.getContentType())) // MIME 타입 지정
                .body(binaryContent.getContent()); // 파일 데이터 반환
    }

    @GetMapping
    public ResponseEntity<List<BinaryContent>> getFiles() {
        List<BinaryContent> binaryContents = binaryContentService.findAll();
        return ResponseEntity.ok(binaryContents);
    }

    @GetMapping("/find")
    public ResponseEntity<BinaryContent> findFile(@RequestParam("id") UUID id) {
        BinaryContent binaryContent = binaryContentService.find(id);
        return ResponseEntity.ok(binaryContent);
    }
}
