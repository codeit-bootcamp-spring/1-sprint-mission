package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.BinaryContentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

@Tag(name = "BinaryContent API", description = "BinaryContent 관련 API")
public interface BinaryContentApiDocs {

  @Operation(summary = "바이너리 파일 가져오기", description = "id로 바이너리 파일 가져오기")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "파일 가져오기 성공"),
      @ApiResponse(responseCode = "404", description = "해당 파일을 찾을 수 없음")
  })
  BinaryContentResponse getFile(UUID fileId);

  @Operation(summary = "바이너리 파일 여러 개 가져오기", description = "id List로 바이너리 파일 가져오기")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "파일 가져오기 성공"),
      @ApiResponse(responseCode = "404", description = "해당 파일을 찾을 수 없음")
  })
  List<BinaryContentResponse> getFileList(List<UUID> fileIds);
}
