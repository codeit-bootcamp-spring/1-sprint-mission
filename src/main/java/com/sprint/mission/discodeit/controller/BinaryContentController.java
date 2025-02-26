package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ResponseDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping("{binaryContentId}")
  public ResponseDTO<BinaryContent> find(@PathVariable UUID binaryContentId) {
    return ResponseDTO.<BinaryContent>builder()
        .code(HttpStatus.OK.value())
        .message("바이터리 파일 조회 성공")
        .data(binaryContentService.find(binaryContentId))
        .build();
  }

  @PostMapping
  public ResponseDTO<List<BinaryContent>> findAllByIdIn(
      @RequestParam("binaryContentIds") List<UUID> ids) {
    return ResponseDTO.<List<BinaryContent>>builder()
        .code(HttpStatus.OK.value())
        .message("바이너리 파일 조회 성공")
        .data(binaryContentService.findAllByIdIn(ids))
        .build();
  }
}
