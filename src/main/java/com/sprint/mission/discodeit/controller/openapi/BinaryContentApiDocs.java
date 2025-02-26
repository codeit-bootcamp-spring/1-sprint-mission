package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "BinaryContent API", description = "첨부파일 작업")
public interface BinaryContentApiDocs {

  @Operation(summary = "BinaryContent 단건 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "첨부 파일 조회 성공"),
      @ApiResponse(responseCode = "404", description = "첨부파일을 찾을 수 없음",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )
      )
  })
  ResponseEntity<BinaryContentDto> getBinaryContent(@PathVariable String binaryContentId);


  @Operation(summary = "여러 BinaryContent 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "첨부 파일 조회 성공"),
      @ApiResponse(responseCode = "404", description = "첨부파일을 찾을 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<List<BinaryContentDto>> getAllBinaryContent(@Parameter(required = true, description = "조회할 첨부파일 UUID List") List<String> binaryContentIds);
}
