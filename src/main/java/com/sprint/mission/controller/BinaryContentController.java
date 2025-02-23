package com.sprint.mission.controller;

import com.sprint.mission.common.CommonResponse;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.service.jcf.addOn.BinaryService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContent")
public class BinaryContentController {

  private final BinaryService binaryContentService;

  @RequestMapping(path = "find")
  public ResponseEntity<CommonResponse> find(@RequestParam("id") UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
    return CommonResponse.toResponseEntity
        (OK, "BinaryContent 조회 성공", binaryContent);
  }

  @RequestMapping(path = "findAllByIdIn")
  public ResponseEntity<CommonResponse> findAllByIdIn(
      @RequestParam("ids") List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    return CommonResponse.toResponseEntity
        (OK, "BinaryContent 목록 조회 성공", binaryContents);
  }
}
