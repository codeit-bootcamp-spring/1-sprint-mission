package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/find/{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable UUID binaryContentId) {
        return ResponseEntity.status(HttpStatus.OK).body(binaryContentService.find(binaryContentId));
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public ResponseEntity<List<BinaryContent>> getMultipleBinaryContents(@RequestBody List<UUID> binaryContentIds) {
        return ResponseEntity.status(HttpStatus.OK).body(binaryContentService.findAllByIdIn(binaryContentIds));
    }

}
