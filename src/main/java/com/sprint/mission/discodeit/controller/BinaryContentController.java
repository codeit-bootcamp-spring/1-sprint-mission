package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.BinaryContentSwagger;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentSwagger {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @GetMapping(value = "/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> find(
      @PathVariable UUID binaryContentId
  ) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(binaryContentMapper.toDto(binaryContent));
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> batch(
      @RequestParam("binaryContentIds") List<UUID> ids
  ) {
    List<BinaryContentDto> contents = binaryContentMapper.toDtoList(
        binaryContentService.findAllByIdIn(ids));

    return ResponseEntity.ok()
        .body(contents);
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<Resource> download(
      @PathVariable("binaryContentId") UUID binaryContentId
  ) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);
    return binaryContentStorage.download(binaryContentMapper.toDto(binaryContent));
  }
}
