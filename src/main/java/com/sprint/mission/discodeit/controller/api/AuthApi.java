package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApi {

  @Operation(summary = "CSRF 토큰 발급")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "발급 성공",
          content = @Content(schema = @Schema(implementation = CsrfToken.class))
      )
  })
  ResponseEntity<CsrfToken> getCsrfToken(@Parameter(hidden = true) CsrfToken csrfToken);

  @Operation(summary = "리프레시 토큰을 활용한 엑세스 토큰 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = String.class))
      ),
      @ApiResponse(
          responseCode = "401", description = "유효하지 않은 리프레시 토큰"
      )
  })
  ResponseEntity<String> me(@Parameter(hidden = true) String refreshToken);

  @Operation(summary = "사용자 권한 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "권한 변경 성공",
          content = @Content(schema = @Schema(implementation = UserDto.class))
      )
  })
  ResponseEntity<UserDto> role(@Parameter(description = "권한 수정 요청 정보") RoleUpdateRequest request);

  @Operation(summary = "리프레시 토큰을 활용한 엑세스 토큰 재발급")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "재발급 성공",
          content = @Content(schema = @Schema(implementation = String.class))
      ),
      @ApiResponse(
          responseCode = "401", description = "유효하지 않은 리프레시 토큰"
      )
  })
  ResponseEntity<String> refresh(
      @Parameter(hidden = true) String refreshToken,
      @Parameter(hidden = true) HttpServletResponse response
  );
} 