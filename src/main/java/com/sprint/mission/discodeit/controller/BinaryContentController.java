package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;

  @Override
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<BinaryContentDto> findBinaryContent(@PathVariable UUID id) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(binaryContentService.findBinaryContentById(id));
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<List<BinaryContentDto>> findBinaryContents(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIdList) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(binaryContentService.findAllBinaryContentsById(binaryContentIdList));
  }
}
