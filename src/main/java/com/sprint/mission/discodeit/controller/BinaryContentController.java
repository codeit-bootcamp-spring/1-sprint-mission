package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/binary-content")
@RequiredArgsConstructor
public class BinaryContentController {
  private final BinaryContentService binaryContentService;
  
  @RequestMapping(method = RequestMethod.GET, value = "/files")
  public String getBinaryContent(@RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContentDto> binaryContent = binaryContentService.findAllByIdIn(binaryContentIds);
    return "binary-content-detail";
  }
  
  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  public String deleteBinaryContent(@RequestParam("binaryContentId") UUID binaryContentId) {
    binaryContentService.remove(binaryContentId);
    return "redirect:/";
  }
}
