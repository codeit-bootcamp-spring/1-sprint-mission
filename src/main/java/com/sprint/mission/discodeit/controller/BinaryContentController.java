package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binarycontetnt.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;

  @Override
  public ResponseEntity<BinaryContentResponse> getBinaryContent(UUID id) {
    Optional<BinaryContentResponse> binaryContent = binaryContentService.getBinaryContent(id);
    return binaryContent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<BinaryContentResponse> saveBinaryContent(BinaryContent binaryContent) {
    BinaryContentResponse savedBinaryContent = binaryContentService.saveBinaryContent(
        binaryContent);
    return ResponseEntity.ok(savedBinaryContent);
  }

  @Override
  public ResponseEntity<Void> deleteBinaryContent(UUID id) {
    binaryContentService.deleteBinaryContent(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<List<BinaryContentResponse>> findAllByIdIn(List<UUID> binaryContentIds) {
    List<BinaryContentResponse> binaryContents = binaryContentService.getBinaryContentListByIds(
        binaryContentIds);
    return ResponseEntity.ok(binaryContents);
  }

  @Override
  public ResponseEntity<?> download(UUID binaryContentId) {
    return binaryContentService.downloadBinaryContent(binaryContentId);
  }
}
