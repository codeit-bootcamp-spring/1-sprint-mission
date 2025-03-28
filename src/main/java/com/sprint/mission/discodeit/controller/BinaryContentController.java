package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping(value = "/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> getBinaryContent(
      @PathVariable("binaryContentId") UUID binaryContentId) {
    BinaryContentDto binaryContentDto = binaryContentService.find(binaryContentId);
    return ResponseEntity.status(HttpStatus.OK).body(binaryContentDto);
  }

  @GetMapping(value = "/all")
  public ResponseEntity<List<BinaryContentDto>> getMultipleBinaryContents(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContentDto> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.status(HttpStatus.OK).body(binaryContents);
  }

}
