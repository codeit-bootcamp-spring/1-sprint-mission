package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping("/binaryContent/{binaryContentId}")
  public ApiResponse<BinaryContent> getBinaryContent(@PathVariable UUID binaryContentId) {
    return ApiResponse.<BinaryContent>builder()
        .code(HttpStatus.OK.value())
        .message("바이터리 파일 조회")
        .data(binaryContentService.find(binaryContentId))
        .build();
  }

  @PostMapping("/binaryContent")
  public ApiResponse<List<BinaryContent>> getBinaryContentList(@RequestBody List<UUID> ids) {
    return ApiResponse.<List<BinaryContent>>builder()
        .code(HttpStatus.OK.value())
        .message("바이너리 파일 조회")
        .data(binaryContentService.findAllByIdIn(ids))
        .build();
  }
}
