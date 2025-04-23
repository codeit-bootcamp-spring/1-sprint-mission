package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
@Tag(name = "BinaryContents", description = "파일 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> getBinaryContent(@PathVariable String binaryContentId) {
    BinaryContentDto binaryContentDto = binaryContentService.findById(binaryContentId);
    return ResponseEntity.ok(binaryContentDto);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> getBinaryContents(
      @RequestParam List<String> binaryContentIds) {
    List<BinaryContentDto> contentList = binaryContentService.findAllByIdIn(binaryContentIds);

    return ResponseEntity.ok(contentList);
  }

  @PostMapping
  public ResponseEntity<BinaryContentDto> uploadBinaryContent(
      @RequestParam("file") MultipartFile file) {
    log.info("파일 업로드 요청");

    try {
      BinaryContentDto binaryContentDto = binaryContentService.create(file);
      return ResponseEntity.status(HttpStatus.CREATED).body(binaryContentDto);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    }
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> downloadBinaryContent(@PathVariable String binaryContentId) {
    log.info("파일 다운로드 요청: binaryContentId = {}", binaryContentId);
    try {
      BinaryContentDto binaryContentDto = binaryContentService.findById(binaryContentId);
      return binaryContentStorage.download(binaryContentDto);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    }
  }
}
