package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontetnt.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BinaryContent")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @Operation(summary = "바이너리 파일 조회", description = "바이너리 파일을 조회한다.")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "조회 실패")})
  @GetMapping("/{id}")
  public ResponseEntity<BinaryContentResponse> getBinaryContent(@PathVariable UUID id) {
    Optional<BinaryContentResponse> binaryContent = binaryContentService.getBinaryContent(id);
    return binaryContent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "바이너리 파일 추가", description = "바이너리 파일을 추가한다.")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "추가 성공"),
      @ApiResponse(responseCode = "500", description = "추가 실패")})
  @PostMapping
  public ResponseEntity<BinaryContentResponse> saveBinaryContent(
      @RequestBody BinaryContent binaryContent) {
    BinaryContentResponse savedBinaryContent = binaryContentService.saveBinaryContent(
        binaryContent);
    return ResponseEntity.ok(savedBinaryContent);
  }

  @Operation(summary = "바이너리 파일 삭제", description = "바이너리 파일을 삭제한다.")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "삭제 성공"),
      @ApiResponse(responseCode = "500", description = "삭제 실패")})
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBinaryContent(@PathVariable UUID id) {
    binaryContentService.deleteBinaryContent(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "여러 첨부 파일 조회", description = "바이너리 파일 목록을 조회한다.")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "500", description = "조회 실패")})
  @GetMapping
  public ResponseEntity<List<BinaryContentResponse>> findAllByIdIn(
      @RequestBody List<UUID> binaryContentIds) {
    List<BinaryContentResponse> binaryContents = binaryContentService.getBinaryContentListByIds(
        binaryContentIds);
    return ResponseEntity.ok(binaryContents);
  }

  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "파일 다운로드 성공")
  })
  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(
      @Parameter(description = "다운로드할 파일 ID") @PathVariable UUID binaryContentId) {
    return binaryContentService.downloadBinaryContent(binaryContentId);
  }
}
