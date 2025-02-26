package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.auth.LoginDto;
import com.sprint.mission.discodeit.dto.user.LoginResponseDto;
import com.sprint.mission.discodeit.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth API", description = "Auth 작업")
public interface AuthApiDocs {

  @Operation(summary = "User 로그인")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "로그인 성공"),
      @ApiResponse(
          responseCode = "400",
          description = "비밀번호 오류",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "User 를 찾을 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      ),
  })
  ResponseEntity<LoginResponseDto> userLogin(@Parameter(required = true, description = "로그인 정보") LoginDto loginDto);
}
