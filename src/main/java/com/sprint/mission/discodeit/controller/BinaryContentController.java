package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
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
@RequestMapping("/api/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @GetMapping(value = "/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> getBinaryContent(
      @PathVariable("binaryContentId") UUID binaryContentId) {
    log.info("바이너리 컨텐츠 조회 요청: id={}", binaryContentId);
    BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);
    log.debug("바이너리 컨텐츠 조회 응답: {}", binaryContent);
    return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
  }

  @GetMapping(value = "/all")
  public ResponseEntity<List<BinaryContentDto>> getMultipleBinaryContents(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    log.info("바이너리 컨텐츠 목록 조회 요청: ids={}", binaryContentIds);
    List<BinaryContentDto> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    log.debug("바이너리 컨텐츠 목록 조회 응답: count={}", binaryContents.size());
    return ResponseEntity.status(HttpStatus.OK).body(binaryContents);
  }

  @GetMapping(value = "/{binaryContentId}/download")
  public ResponseEntity<?> download(
      @PathVariable("binaryContentId") UUID binaryContentId
  ) {
    log.info("바이너리 컨텐츠 다운로드 요청: id={}", binaryContentId);
    BinaryContentDto binaryContentDto = binaryContentService.find(binaryContentId);
    ResponseEntity<?> download = binaryContentStorage.download(binaryContentDto);
    log.debug("바이너리 컨텐츠 다운로드 응답: contentType={}, contentLength={}",
        download.getHeaders().getContentType(), download.getHeaders().getContentLength());
    return download;
  }
}
