package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/binary-content")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @PostMapping("/create")
    public ResponseEntity<BinaryContent> createBinaryContent(@RequestBody BinaryContentCreateRequest request) {
        BinaryContent createdBinaryContent = binaryContentService.create(request);
        return ResponseEntity.ok(createdBinaryContent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable UUID id) {
        return ResponseEntity.ok(binaryContentService.find(id));
    }

    @GetMapping("/batch")
    public ResponseEntity<List<BinaryContent>> getBinaryContentBatch(@RequestParam List<UUID> ids) {
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(ids));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBinaryContent(@PathVariable UUID id) {
        binaryContentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
