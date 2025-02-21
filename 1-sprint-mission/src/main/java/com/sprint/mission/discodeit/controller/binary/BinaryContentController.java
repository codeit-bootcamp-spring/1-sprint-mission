package com.sprint.mission.discodeit.controller.binary;

import com.sprint.mission.discodeit.dto.response.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.response.binary.BinaryContentResponseData;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

  final BinaryContentService binaryContentService;

  public BinaryContentController(BinaryContentService binaryContentService) {
    this.binaryContentService = binaryContentService;
  }

  //binaryId로 binaryContentDTO 찾기 -> messageId & fileData 포함 되어 있는 DTO
  @GetMapping("/find")
  public ResponseEntity<BinaryContentDTO> getBinaryContent(
      @RequestParam("binaryContentId") UUID binaryContentId) {
    BinaryContentDTO binaryContentDTO = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(binaryContentDTO);
  }

  //userId로 binaryResponseContentDTO 찾기-> messageId & fileData 포함 X DTO
  @GetMapping("/list/{userId}")
  public ResponseEntity<List<BinaryContentResponseData>> getBinaryContentsUserById(
      @PathVariable UUID userId) {
    List<BinaryContentResponseData> binaryList = binaryContentService.findAllByUserId(userId);
    return ResponseEntity.ok(binaryList);
  }


}
