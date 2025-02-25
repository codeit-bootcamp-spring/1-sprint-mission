package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.entity.BinaryContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentApiDocs {

  @Operation(summary = "첨부 파일 조회", description = "특정 첨부 파일을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부 파일 조회 성공"),
      @ApiResponse(responseCode = "404", description = "첨부 파일을 찾을 수 없음")
  })
  ResponseEntity<BinaryContent> find(UUID binaryContentId);

  @Operation(summary = "여러 첨부 파일 조회", description = "여러 개의 첨부 파일을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부 파일 목록 조회 성공")
  })
  ResponseEntity<List<BinaryContent>> findAllByIdIn(List<UUID> binaryContentIds);
}
