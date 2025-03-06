package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.BinaryContentApiDocs;
import com.sprint.mission.discodeit.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binary-content")
public class BinaryContentController implements BinaryContentApiDocs {

  private final BinaryContentService binaryContentService;

  @GetMapping("/{fileId}")
  @Override
  public ResponseEntity<CustomApiResponse<BinaryContentResponse>> getFile(
      @PathVariable UUID fileId
  ) {
    return ResponseEntity.ok(
        CustomApiResponse.success(binaryContentService.findByIdOrThrow(fileId)));
  }

  @GetMapping
  @Override
  public ResponseEntity<CustomApiResponse<List<BinaryContentResponse>>> getFileList(
      @RequestParam("ids") List<UUID> fileIds) {
    return ResponseEntity.ok(
        CustomApiResponse.success(binaryContentService.findAllByIdIn(fileIds)));
  }
}
