package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.controller.swagger.BinaryContentApiDocs;
import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentApiDocs {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  // 단건 조회 및 다운로드
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> getFile(@PathVariable UUID binaryContentId) {
    BinaryContentDto binaryContentDto = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(binaryContentDto);
  }

  @GetMapping("")
  public ResponseEntity<List<BinaryContentDto>> getFiles(
      @RequestParam List<UUID> binaryContentIds) {
    List<BinaryContentDto> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(binaryContents);
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> downloadFile(@PathVariable UUID binaryContentId) {
    BinaryContentDto binaryContentDto = binaryContentService.find(binaryContentId);
    return binaryContentStorage.download(binaryContentDto);
  }

}
