package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/{id}")
    public ResponseEntity<BinaryContent> getFile(@PathVariable UUID id) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(binaryContentService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<BinaryContent>> getFiles(@RequestParam List<UUID> uuids) {
        return ResponseEntity.ok(binaryContentService.findAllById(uuids));
    }


}
