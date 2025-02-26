package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusResponseDto;
import com.sprint.mission.discodeit.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User API")
public interface UserStatusApiDocs {

  @Operation(summary = "User 온라인 상태 업데이트")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User 온라인 상태 업데이트 성공"),
      @ApiResponse(
          responseCode = "404",
          description = "User를 찾을 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<UserStatusResponseDto> updateUserStatus(@Parameter(required = true, description = "업데이트 할 User UUID") String userId, @Parameter(required = true, description = "업데이트 시간") UpdateUserStatusDto userStatusDto);
}
