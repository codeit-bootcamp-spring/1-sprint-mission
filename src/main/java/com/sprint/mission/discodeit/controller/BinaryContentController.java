package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/binary-content")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/{id}")
    public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable UUID id) {
        Optional<BinaryContent> binaryContent = binaryContentService.getBinaryContent(id);
        return binaryContent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BinaryContent> saveBinaryContent(@RequestBody BinaryContent binaryContent) {
        BinaryContent savedBinaryContent = binaryContentService.saveBinaryContent(binaryContent);
        return ResponseEntity.ok(savedBinaryContent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBinaryContent(@PathVariable UUID id) {
        binaryContentService.deleteBinaryContent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<BinaryContent>> getBinaryContentListByIds(@RequestParam List<UUID> ids) {
        List<BinaryContent> binaryContents = binaryContentService.getBinaryContentListByIds(ids);
        return ResponseEntity.ok(binaryContents);
    }
}
