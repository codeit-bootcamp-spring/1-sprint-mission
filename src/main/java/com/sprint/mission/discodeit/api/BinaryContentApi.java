package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.binarycontetnt.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "BinaryContent", description = "바이너리 파일 관련 API")
@RequestMapping("/api/binaryContents")
public interface BinaryContentApi {

  @Operation(summary = "바이너리 파일 조회", description = "바이너리 파일을 조회한다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "조회 실패")})
  @GetMapping("/{id}")
  ResponseEntity<BinaryContentResponse> getBinaryContent(@PathVariable UUID id);

  @Operation(summary = "바이너리 파일 추가", description = "바이너리 파일을 추가한다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "추가 성공"),
      @ApiResponse(responseCode = "500", description = "추가 실패")})
  @PostMapping
  ResponseEntity<BinaryContentResponse> saveBinaryContent(@RequestBody BinaryContent binaryContent);

  @Operation(summary = "바이너리 파일 삭제", description = "바이너리 파일을 삭제한다.")
  @ApiResponses({@ApiResponse(responseCode = "204", description = "삭제 성공"),
      @ApiResponse(responseCode = "500", description = "삭제 실패")})
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteBinaryContent(@PathVariable UUID id);

  @Operation(summary = "여러 첨부 파일 조회", description = "바이너리 파일 목록을 조회한다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "500", description = "조회 실패")})
  @GetMapping
  ResponseEntity<List<BinaryContentResponse>> findAllByIdIn(
      @RequestBody List<UUID> binaryContentIds);

  @Operation(summary = "파일 다운로드", description = "특정 바이너리 파일을 다운로드한다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "파일 다운로드 성공")})
  @GetMapping("/{binaryContentId}/download")
  ResponseEntity<?> download(
      @Parameter(description = "다운로드할 파일 ID") @PathVariable UUID binaryContentId);
}
