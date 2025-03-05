package com.sprint.mission.discodeit.controller.binary;

import com.sprint.mission.discodeit.dto.response.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.response.binary.BinaryContentResponseDTO;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent Controller", description = "바이너리 파일 관련 API 앤드포인트 관리")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  public BinaryContentController(BinaryContentService binaryContentService) {
    this.binaryContentService = binaryContentService;
  }

  @Operation(summary = "바이너리 파일 찾기", description = " messageId & fileData 포함 되어 있는 DTO로 반환")
  @GetMapping("/find")
  public ResponseEntity<BinaryContentDTO> getBinaryContent(
      @RequestParam("binaryContentId") UUID binaryContentId) {
    BinaryContentDTO binaryContentDTO = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(binaryContentDTO);
  }

  //userId로 binaryResponseContentDTO 찾기-> messageId & fileData 포함 X DTO
  @Operation(summary = "유저 ID로 바이너리 파일 찾기", description = " messageId & fileData 포함 되어 있지 않은 DTO로 반환")
  @GetMapping("/list/{userId}")
  public ResponseEntity<List<BinaryContentResponseDTO>> getBinaryContentsUserById(
      @PathVariable UUID userId) {
    List<BinaryContentResponseDTO> binaryList = binaryContentService.findAllByUserId(userId);
    return ResponseEntity.ok(binaryList);
  }


}
