package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.BinaryContentSwagger;
import com.sprint.mission.discodeit.dto.response.BinaryContentListResponse;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents") //binary-contents로 변경, 요구사항 맞추기위해 Camel 로 수정
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentSwagger {

  // implements BinaryContentSwagger
  private final BinaryContentService binaryContentService;

  @GetMapping(value = "/{binaryContentId}")
  public ResponseEntity<BinaryContentResponse> find(
      @PathVariable UUID binaryContentId
  ) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(BinaryContentResponse.from(binaryContent));
  }


  //    @GetMapping("/batch")  요구사항 맞추기위해 일단 보류
//  @GetMapping
  public ResponseEntity<BinaryContentListResponse> batchV0(
      @RequestParam List<UUID> ids
  ) {
    List<BinaryContent> contents = binaryContentService.findAllByIdIn(ids);
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(BinaryContentListResponse.from(contents));
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentResponse>> batch(
      @RequestParam("binaryContentIds") List<UUID> ids
  ) {
    List<BinaryContentResponse> contents = binaryContentService.findAllByIdIn(ids)
        .stream().map(BinaryContentResponse::from).toList();
    return ResponseEntity.ok()
        .body(contents);
  }
}
