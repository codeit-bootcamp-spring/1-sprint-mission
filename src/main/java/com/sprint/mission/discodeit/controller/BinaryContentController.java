package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.BinaryContentSwagger;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
@RequestMapping("/api/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentSwagger {

  private final BinaryContentService binaryContentService;

  @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
  public ResponseEntity<BinaryContent> findBinaryContent(@PathVariable UUID binaryContentId) {

    BinaryContent binaryContent = binaryContentService.find(binaryContentId);

    return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<BinaryContent>> findBinaryContents(
      @RequestParam("id") List<UUID> ids
  ) {

    List<BinaryContent> allBinaryContent = binaryContentService.findAllByIdIn(ids);

    return ResponseEntity.status(HttpStatus.OK).body(allBinaryContent);
  }
}
