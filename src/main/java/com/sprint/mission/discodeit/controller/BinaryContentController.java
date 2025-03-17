package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @GetMapping(value = "/{id}")
  public ResponseEntity<BinaryContentDto> findBinaryContent(@PathVariable UUID id) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(binaryContentService.findBinaryContentById(id));
  }

  @GetMapping(value = "")
  public ResponseEntity<List<BinaryContentDto>> findBinaryContents(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIdList) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(binaryContentService.findAllBinaryContentsById(binaryContentIdList));
  }

  @GetMapping("{id}/download")
  public ResponseEntity<Resource> getBinaryContent(@PathVariable UUID id) {
    return binaryContentStorage.download(binaryContentService.findBinaryContentById(id));
  }
}
