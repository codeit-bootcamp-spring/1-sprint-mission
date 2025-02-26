package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable("binaryContentId") UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);

        if (binaryContent == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(binaryContent);
    }

    @GetMapping
    public ResponseEntity<List<BinaryContent>> getBinaryContents(
            @RequestParam("ids") List<UUID> binaryContentIds) {

        if (binaryContentIds == null || binaryContentIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
        return ResponseEntity.ok(binaryContents);
    }
}