package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "BinaryContent API", description = "BinaryContent 관련 API")
public interface BinaryContentApiDocs {

  @Operation(summary = "바이너리 파일 가져오기", description = "id로 바이너리 파일 가져오기")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "파일 가져오기 성공"),
      @ApiResponse(responseCode = "404", description = "해당 파일을 찾을 수 없음")
  })
  ResponseEntity<CustomApiResponse<BinaryContentResponse>> getFile(UUID fileId);

  @Operation(summary = "바이너리 파일 여러 개 가져오기", description = "id List로 바이너리 파일 가져오기")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "파일 가져오기 성공"),
      @ApiResponse(responseCode = "404", description = "해당 파일을 찾을 수 없음")
  })
  ResponseEntity<CustomApiResponse<List<BinaryContentResponse>>> getFileList(List<UUID> fileIds);

  @Operation(summary = "바이너리 파일 다운로드", description = "id로 파일 다운로드")
  ResponseEntity<?> downloadFile(UUID binaryContentId);
}
