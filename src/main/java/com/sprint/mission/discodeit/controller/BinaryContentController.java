package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<BinaryContent> createBinaryContent(@RequestBody BinaryContentCreateRequest request) {
        return ResponseEntity.ok(binaryContentService.create(request));
    }

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable UUID binaryContentId) {
        return ResponseEntity.ok(binaryContentService.find(binaryContentId));
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<List<BinaryContent>> getMultipleBinaryContents(@RequestBody List<UUID> binaryContentIds) {
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
    }

    @RequestMapping(value = "/delete/{binaryContentId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteBinaryContent(@PathVariable UUID binaryContentId) {
        binaryContentService.delete(binaryContentId);
        return ResponseEntity.noContent().build();
    }
}
