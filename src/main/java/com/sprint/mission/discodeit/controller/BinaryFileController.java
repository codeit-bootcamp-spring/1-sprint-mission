package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
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
  public ResponseEntity<BinaryContentDto> binaryContentFindById(
      @PathVariable UUID binaryContentId) {
    return ResponseEntity.ok(binaryContentService.findBinaryContentById(binaryContentId)); // 200
  }

  @GetMapping(value = "/{binaryContentId}/download")
  public ResponseEntity<?> downloadBinaryContent(@PathVariable UUID id) {
    // BinaryContentStorage 를 직접 들고와서 쓰라는 것(클래스 다이어그램)으로 이해는 했지만,
    // 그러면 컨트롤러 단에서 Mapper를 통한 변환(id로 BinaryContent 를 부르고, BinaryContent <-> DTO)이 이뤄이지 때문에
    // 저는 우선 binaryContentService 에 download 관련 메서드를 추가했습니다.
    // 내부적으로는 binaryContentStorage 의 downlaod 메서드가 호출됩니다.
    // 즉, 다운로드 API -> (컨트롤러 - 서비스 - 스토리지) 단에서 기능 구현
    return binaryContentService.downloadBinaryContent(id);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @RequestParam List<UUID> binaryContentIds
  ) {
    return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
  }


}
