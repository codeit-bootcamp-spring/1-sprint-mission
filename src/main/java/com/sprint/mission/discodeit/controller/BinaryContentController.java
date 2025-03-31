package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @GetMapping(path = "{binaryContentId}")
  public ResponseEntity<BinaryContentDto> find(
      @PathVariable("binaryContentId") UUID binaryContentId) {

    log.info("BinaryContent 단건 조회 요청 : binaryContentId={}", binaryContentId);

    BinaryContentDto binaryContentDto = binaryContentService.find(binaryContentId);

    log.info("BinaryContent 단건 조회 성공 : fileName={}", binaryContentDto.fileName());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(binaryContentDto);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {

    log.info("BinaryContent 다건 조회 요청 : binaryContentIds={}", binaryContentIds);

    List<BinaryContentDto> binaryContentDtos = binaryContentService.findAllByIdIn(binaryContentIds);

    log.info("BinaryContent 다건 조회 성공 : 요청 개수={}, 반환 개수={}", binaryContentIds.size(),
        binaryContentDtos.size());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(binaryContentDtos);
  }

  @Override
  @GetMapping(path = "{binaryContentId}/download")
  public ResponseEntity<?> download(
      @PathVariable("binaryContentId") UUID binaryContentId) {

    log.info("BinaryContent 다운로드 요청 : binaryContentId={}", binaryContentId);

    BinaryContentDto binaryContentDto = binaryContentService.find(binaryContentId);

    log.info("BinaryContent 다운로드 성공 : fileName={}", binaryContentDto.fileName());

    return binaryContentStorage.download(binaryContentDto);
  }
}
