package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binarycontents")
@Slf4j
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  // 단일 바이너리 콘텐츠 조회 (GET /binarycontent/{binaryContentId})
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> find(
      @PathVariable("binaryContentId") UUID binaryContentId) {
    return ResponseEntity.ok(binaryContentService.findById(binaryContentId));
  }

  // 여러 개의 바이너리 콘텐츠 조회 (GET /binarycontent?binaryContentIds=xxx,yyy,zzz)
  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @RequestParam List<UUID> binaryContentIds) {
    return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
  }

  //파일 다운로드
  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(@RequestParam("id") UUID binaryContentId) {
    log.info("Received Downloading binary content with id {}", binaryContentId);
    BinaryContentDto binaryContentDto = binaryContentService.findById(binaryContentId);
    return ResponseEntity.ok(binaryContentStorage.download(binaryContentDto)); //로직 위임
  }
}
