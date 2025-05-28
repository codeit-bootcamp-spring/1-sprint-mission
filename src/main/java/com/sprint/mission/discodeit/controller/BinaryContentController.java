package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.BinaryContentControllerDocs;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentControllerDocs {

  private final BinaryContentService binaryContentService;

  @GetMapping("/{fileId}")
  public ResponseEntity<BinaryContentResponseDto> find(@PathVariable UUID fileId) {
    return ResponseEntity.ok(binaryContentService.find(fileId));
  }

  @GetMapping("/{fileId}/download")
  public ResponseEntity<?> download(@PathVariable UUID fileId) {
    return binaryContentService.download(fileId);
  }

}
