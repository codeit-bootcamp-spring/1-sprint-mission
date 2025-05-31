package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.BinaryContentControllerDocs;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentControllerDocs {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @GetMapping("/{id}")
  @Override
  public ResponseEntity<BinaryContentDto> getBinaryContentById(@PathVariable UUID id) {
    log.debug("GET /api/binaryContents/{}", id);
    return ResponseEntity.ok(binaryContentService.find(id));
  }

  @GetMapping
  @Override
  public ResponseEntity<List<BinaryContentDto>> getBinaryContents(
      @RequestParam List<UUID> binaryContentIds) {
    log.debug("GET /api/binaryContents");
    ArrayList<BinaryContentDto> responses = new ArrayList<>(100);

    binaryContentIds.stream()
        .map(binaryContentService::find)
        .forEach(responses::add);

    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> getFile(@PathVariable UUID id) {
    log.debug("GET /api/binaryContents/{}/download", id);
    return binaryContentStorage.download(binaryContentService.find(id));
  }
}