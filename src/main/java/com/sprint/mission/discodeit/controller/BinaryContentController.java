package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController {
  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> getBinaryContent(@PathVariable String binaryContentId){
    BinaryContent content = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(binaryContentMapper.toDto(content));
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> getAllBinaryContent(@RequestParam List<String> binaryContentIds){
    List<BinaryContent> contents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(binaryContentMapper.toDtoList(contents));
  }
}
