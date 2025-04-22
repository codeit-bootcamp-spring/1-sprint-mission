package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> find(
      @PathVariable("binaryContentId") UUID binaryContentId) {
    BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);
    return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
  }

  @Override
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(List<UUID> binaryContentIds) {
    return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> downloadBinaryContent(@PathVariable UUID binaryContentId) {
    BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);
    if (binaryContent == null) {
      return ResponseEntity.notFound().build();
    }
    return binaryContentStorage.download(binaryContent);
  }
}
