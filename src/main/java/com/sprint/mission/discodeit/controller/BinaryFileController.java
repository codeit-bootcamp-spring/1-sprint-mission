package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryFileController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;

  @GetMapping(value = "/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> binaryContentFindById(
      @PathVariable UUID binaryContentId) {
    return ResponseEntity.ok(binaryContentService.findBinaryContentById(binaryContentId)); // 200
  }

  @GetMapping(value = "/{binaryContentId}/download")
  public ResponseEntity<?> downloadBinaryContent(@PathVariable UUID id) {
    log.info("파일 다운로드 요청(Request)");

    ResponseEntity<?> downloadBinaryContent = binaryContentService.downloadBinaryContent(id);
    log.info("파일 다운로드 응답(Response): HttpStatus={}", HttpStatus.OK);
    return downloadBinaryContent;
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @RequestParam List<UUID> binaryContentIds
  ) {
    return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
  }


}
