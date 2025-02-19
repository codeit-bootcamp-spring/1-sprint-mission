package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/binary-content")
@RequiredArgsConstructor
public class BinaryController {
    private final BinaryContentService binaryContentService;

    @GetMapping
    public ResponseEntity<List<BinaryContent>> findAll() {
       return ResponseEntity.ok(binaryContentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getBinaryContent(@PathVariable UUID id) {
        BinaryContent binaryContent=binaryContentService.find(id);
        String contentType=binaryContent.getContentType();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,contentType)
                .body(binaryContent.getData());
    }

    @GetMapping("/multiple")
    public ResponseEntity<List<BinaryContent>> getMultipleBinaryContent(@RequestParam List<UUID> ids) {
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(ids));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBinaryContent(@PathVariable UUID id) {
        binaryContentService.delete(id);
        return ResponseEntity.ok("delete success");
    }
}
