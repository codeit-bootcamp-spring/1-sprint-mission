package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/{id}") //파일 다운로드
    public ResponseEntity<byte[]> downloadFile(@PathVariable UUID id) {
        BinaryContent content = binaryContentService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + content.getFileName() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, content.getContentType());
        return new ResponseEntity<>(content.getBytes(), headers, HttpStatus.OK);
    }

    @PostMapping("/batch") // 여러 파일 조회
    public ResponseEntity<List<BinaryContent>>getFilesByIds(@RequestBody List<UUID> ids) {
        List<BinaryContent> contents = binaryContentService.findAllById(ids);
        return ResponseEntity.ok(contents);
    }

}
