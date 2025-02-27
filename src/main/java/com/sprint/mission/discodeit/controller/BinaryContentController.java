package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @Operation(summary = "바이너리 파일 조회", description = "바이너리 파일을 조회한다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "조회 실패")})
  @GetMapping("/{id}")
  public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable UUID id) {
    Optional<BinaryContent> binaryContent = binaryContentService.getBinaryContent(id);
    return binaryContent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "바이너리 파일 추가", description = "바이너리 파일을 추가한다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "추가 성공"),
      @ApiResponse(responseCode = "500", description = "추가 실패")})
  @PostMapping
  public ResponseEntity<BinaryContent> saveBinaryContent(@RequestBody BinaryContent binaryContent) {
    BinaryContent savedBinaryContent = binaryContentService.saveBinaryContent(binaryContent);
    return ResponseEntity.ok(savedBinaryContent);
  }

  @Operation(summary = "바이너리 파일 삭제", description = "바이너리 파일을 삭제한다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "삭제 성공"),
      @ApiResponse(responseCode = "500", description = "삭제 실패")})
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBinaryContent(@PathVariable UUID id) {
    binaryContentService.deleteBinaryContent(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "바이너리 파일 리스트 조회", description = "바이너리 파일 목록을 조회한다.")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "500", description = "조회 실패")})
  @GetMapping
  public ResponseEntity<List<BinaryContent>> getBinaryContentListByIds(
      @RequestBody List<UUID> ids) {
    List<BinaryContent> binaryContents = binaryContentService.getBinaryContentListByIds(ids);
    return ResponseEntity.ok(binaryContents);
  }
}
