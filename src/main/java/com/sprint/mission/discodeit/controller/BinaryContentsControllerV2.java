package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Binary Content Controller")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentsControllerV2 {

  private final BinaryContentService binaryContentService;

  /**
   * 첨부파일 조회
   */
  @Operation(summary = "첨부 파일 단건 조회")
  @GetMapping("/{binaryContentId}")
  public BinaryContentResponse findFile(@PathVariable("binaryContentId") String fileName) {
    return binaryContentService.find(fileName);
  }

  /**
   * 첨부파일 여러건 조회
   */
  @Operation(summary = "첨부 파일 여러건 조회")
  @GetMapping
  public List<BinaryContentResponse> findAllFiles(
      @RequestParam("binaryContentIds") List<String> binaryContentIds) {
    return binaryContentService.findAll(binaryContentIds);
  }

}
