package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.ResponseBinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
@Tag(name = "BinaryContents", description = "파일 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping("/{contentId}")
  public ResponseEntity<ResponseBinaryContentDto> getBinaryContent(@PathVariable String contentId,
      @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

    ResponseBinaryContentDto binaryContentDto = binaryContentService.findById(contentId);
    String etag = "\"" + binaryContentDto.hashCode() + "\""; // 해시값을 ETag로 사용

    if (etag.equals(ifNoneMatch)) {
      //변경 없으므로 304 응답
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    return ResponseEntity.ok().eTag(etag).cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
        .body(binaryContentDto);
  }

  @GetMapping
  public ResponseEntity<List<ResponseBinaryContentDto>> getBinaryContents(
      @RequestParam List<String> contentIds,
      @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

    List<ResponseBinaryContentDto> contentList = binaryContentService.findAllByIdIn(contentIds);
    String etag = "\"" + contentList.hashCode() + "\"";

    if (etag.equals(ifNoneMatch)) {
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    return ResponseEntity.ok().eTag(etag).cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
        .body(contentList);
  }

  @PostMapping
  public ResponseEntity<ResponseBinaryContentDto> uploadBinaryContent(
      @RequestParam("file") MultipartFile file) {
    return ResponseEntity.status(HttpStatus.CREATED).body(binaryContentService.create(file));
  }
}
