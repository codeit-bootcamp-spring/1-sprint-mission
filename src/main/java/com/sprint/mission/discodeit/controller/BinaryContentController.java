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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/binary")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    /**
     * 단일 파일 다운로드
     */
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<byte[]> getBinaryFile(@PathVariable UUID binaryContentId) {
        BinaryContent file = binaryContentService.find(binaryContentId);
        return createFileResponse(file);
    }

    /**
     * 여러 개의 파일 다운로드
     */
    @PostMapping("/multiple")
    public ResponseEntity<List<ResponseEntity<byte[]>>> getMultipleBinaryFiles(@RequestBody List<UUID> binaryContentIds) {
        List<BinaryContent> files = binaryContentService.findAllByIdIn(binaryContentIds);

        List<ResponseEntity<byte[]>> responses = files.stream()
                .map(this::createFileResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * 파일 삭제
     */
    @DeleteMapping("/{binaryContentId}")
    public ResponseEntity<Void> deleteBinaryFile(@PathVariable UUID binaryContentId) {
        binaryContentService.delete(binaryContentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 바이너리 파일 응답 생성 메서드
     */
    private ResponseEntity<byte[]> createFileResponse(BinaryContent file) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment; filename=" + file.getFileName());
        headers.set("Content-Type", file.getContentType());

        return new ResponseEntity<>(file.getBytes(), headers, HttpStatus.OK);
    }
}
