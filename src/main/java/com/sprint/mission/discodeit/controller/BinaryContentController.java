package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.ResponseBinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/binary-contents")
@RequiredArgsConstructor
@Tag(name = "BinaryContents", description = "파일 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping("/{contentId}")
  public ResponseBinaryContentDto viewFile(@PathVariable String contentId) {
    return binaryContentService.findById(contentId);
  }

  @GetMapping
  public List<ResponseBinaryContentDto> getBinaryContents(@RequestParam List<String> contentIds) {
    return binaryContentService.findAllByIdIn(contentIds);
  }

  @PostMapping
  public ResponseBinaryContentDto uploadBinaryContent(@RequestParam("file") MultipartFile file) {
    return binaryContentService.create(file);
  }
}
