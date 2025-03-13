package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binarycontent")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  // 단일 바이너리 콘텐츠 조회 (GET /binarycontent/{binaryContentId})
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContent> find(@PathVariable("binaryContentId") UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
    return ResponseEntity.ok(binaryContent);
  }

  // 여러 개의 바이너리 콘텐츠 조회 (GET /binarycontent?binaryContentIds=xxx,yyy,zzz)
  @GetMapping
  public ResponseEntity<List<BinaryContent>> findAllByIdIn(
      @RequestParam List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(binaryContents);
  }
}
