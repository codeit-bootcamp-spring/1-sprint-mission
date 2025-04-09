package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.controller.openapi.BinaryContentApiDocs;
import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
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
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentApiDocs {

  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> getBinaryContent(@PathVariable String binaryContentId) {
    BinaryContent content = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(binaryContentMapper.toDto(content));
  }

  @Override
  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> getAllBinaryContent(
      @RequestParam List<String> binaryContentIds) {
    List<BinaryContent> contents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(binaryContentMapper.toDtoList(contents));
  }

  @Override
  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<Resource> downloadBinaryContent(@PathVariable String binaryContentId) {
    log.debug("[DOWNLOAD FILE REQUEST] : [ID : {}]", binaryContentId);
    return binaryContentService.download(binaryContentId);
  }

}
