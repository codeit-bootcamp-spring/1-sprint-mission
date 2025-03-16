package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @GetMapping("{binaryContentId}")
  public ResponseEntity<BinaryContentDto> find(@PathVariable UUID binaryContentId) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(binaryContentService.find(binaryContentId));
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(binaryContentService.findAllByIdIn(binaryContentIds));
  }

  @GetMapping("{binaryContentId}/download")
  public ResponseEntity<Resource> fileDownload(
      @PathVariable("binaryContentId") UUID binaryContentId) {
    return binaryContentStorage.download(binaryContentService.find(binaryContentId));
  }

}
