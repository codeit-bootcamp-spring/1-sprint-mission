package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.BinaryContentApiDocs;
import com.sprint.mission.discodeit.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentApiDocs {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @GetMapping("/{binaryContentId}")
  @Override
  public ResponseEntity<CustomApiResponse<BinaryContentResponse>> getFile(
      @PathVariable(value = "binaryContentId") UUID fileId
  ) {
    return ResponseEntity.ok(
        CustomApiResponse.success(binaryContentService.findById(fileId)));
  }

  @GetMapping
  @Override
  public ResponseEntity<CustomApiResponse<List<BinaryContentResponse>>> getFileList(
      @RequestParam("binaryContentIds") List<UUID> fileIds) {
    return ResponseEntity.ok(
        CustomApiResponse.success(binaryContentService.findAllByIdIn(fileIds)));
  }

  @GetMapping("/{binaryContentId}/download")
  @Override
  public ResponseEntity<?> downloadFile(@PathVariable UUID binaryContentId) {
    log.info("GET /api/binaryContents/{}/download - download attempt for file", binaryContentId);
    return binaryContentStorage.download(binaryContentService.findById(binaryContentId));
  }
}
