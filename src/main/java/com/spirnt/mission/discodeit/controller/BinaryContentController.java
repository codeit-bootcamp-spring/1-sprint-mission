package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.swagger.BinaryContentApiDocs;
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

  private boolean isImage(String fileType) {
    return fileType.startsWith("image/");
  }

  // 단건 조회 및 다운로드
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContent> getFile(@PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(binaryContent);
  }

  @GetMapping("")
  public ResponseEntity<List<BinaryContent>> getFiles(@RequestParam List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(binaryContents);
  }

//  // 삭제
//  @RequestMapping(value = "/{fileId}", method = RequestMethod.DELETE)
//  public ResponseEntity<?> deleteFile(@PathVariable UUID fileId) {
//    binaryContentService.delete(fileId);
//    return ResponseEntity.noContent().build();
//  }

}
