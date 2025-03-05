package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentSwagger {

  @Operation(operationId = "find", summary = "첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부 파일 조회 성공"),
      @ApiResponse(responseCode = "404", description = "첨부 파일을 찾을 수 없음", content = @Content(
          examples = @ExampleObject(value = "BinaryContent with id {binaryContentId} not found")
      )),
  })
  ResponseEntity<BinaryContentResponse> find(UUID binaryContentId);

  @Operation(operationId = "batch", summary = "여러 첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부 파일 목록 조회 성공")
  })
  ResponseEntity<List<BinaryContentResponse>> batch(List<UUID> ids);
}


