package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentFindResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryFileController {

  private final BinaryContentService binaryContentService;

  @GetMapping(value = "/{binaryContentId}")
  public ResponseEntity<BinaryContent> binaryContentFindById(
      @PathVariable UUID binaryContentId) {
    return ResponseEntity.ok(binaryContentService.findBinaryContentById(binaryContentId)); // 200
  }

  @GetMapping
  public ResponseEntity<List<BinaryContent>> findAllByIdIn(
      @RequestParam List<UUID> ids
  ) {
    return ResponseEntity.ok(binaryContentService.findAllByIdIn(ids));
  }
}
