package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Binary_content API", description = "파일 관리 API")
public interface BinaryContentControllerDocs {

  @Operation(summary = "파일 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "파일 조회 성공"),
      @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음")
  })
  @GetMapping("/{id}")
  ResponseEntity<BinaryContentDto> getBinaryContentById(@PathVariable UUID id);

  @Operation(summary = "파일 여러개 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "파일 조회 성공")
  })
  @GetMapping
  ResponseEntity<List<BinaryContentDto>> getBinaryContents(
      @RequestParam List<UUID> binaryContentIds);

  @Operation(summary = "파일 다운로드")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "파일 다운로드 성공")
  })
  @GetMapping("/{id}/download")
  ResponseEntity<Resource> getFile(@PathVariable UUID id);
}
