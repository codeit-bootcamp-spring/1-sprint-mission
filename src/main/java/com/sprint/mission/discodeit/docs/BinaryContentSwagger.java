package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentSwagger {

  @Operation(operationId = "find", summary = "첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부 파일 조회 성공"),
      @ApiResponse(responseCode = "404", description = "첨부 파일을 찾을 수 없음", content = @Content(
          examples = @ExampleObject(value = "BinaryContent with id {binaryContentId} not found")
      )),
  })
  ResponseEntity<BinaryContentDto> find(UUID binaryContentId);

  @Operation(operationId = "batch", summary = "여러 첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부 파일 목록 조회 성공")
  })
  ResponseEntity<List<BinaryContentDto>> batch(List<UUID> ids);

  @Operation(operationId = "download", summary = "파일 다운로드")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "파일 다운로드 성공")
  })
  public ResponseEntity<Resource> download(
      @PathVariable("binaryContentId") UUID binaryContentId
  );
}


