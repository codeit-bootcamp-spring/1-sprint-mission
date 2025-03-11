package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentApi {

  @Operation(summary = "첨부 파일 조회", description = "특정 파일을 조회합니다.")
  ResponseEntity<BinaryContentDto> find(@RequestParam("id") UUID id);

  @Operation(summary = "여러 첨부 파일 조회", description = "파일 ID 목록을 받아 파일 정보를 반환합니다.")
  ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds);

  @Operation(
      summary = "첨부 파일 다운로드",
      description = "특정 파일을 다운로드합니다.",
      parameters = {
          @Parameter(name = "id", description = "다운로드할 파일의 ID", required = true)
      }
  )
  ResponseEntity<?> download(@RequestParam("id") UUID binaryContentId);
}
