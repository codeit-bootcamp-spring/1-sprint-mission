package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BinaryContentController {
  private final BinaryContentService binaryContentService;

  @RequestMapping(value = "/binary-content/{binaryContentId}", method = RequestMethod.GET)
  public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable String binaryContentId){
    BinaryContent content = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(content);
  }

  @RequestMapping(value = "/binary-content", method = RequestMethod.GET)
  public ResponseEntity<List<BinaryContent>> getAllBinaryContent(){
    List<BinaryContent> contents = binaryContentService.findAll();
    return ResponseEntity.ok(contents);
  }
}
