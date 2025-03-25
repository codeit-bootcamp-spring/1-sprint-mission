package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Operation(summary = "조회", description = "단건 조회")
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> getBinaryContent(
      @PathVariable("binaryContentId") UUID binaryContentId) {
    BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(binaryContent);
  }

  @Operation(summary = "조회", description = "전부 조회")
  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> getBinaryContents(
      @RequestParam("ids") List<UUID> binaryContentIds) {

    if (binaryContentIds == null || binaryContentIds.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    List<BinaryContentDto> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(binaryContents);
  }

  @GetMapping({"/download"})
  public ResponseEntity<?> downloadContent(
      @RequestParam("binaryContentId") UUID binaryContentId) throws IOException {
    try {
      BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);
      return binaryContentStorage.download(binaryContent);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }
}