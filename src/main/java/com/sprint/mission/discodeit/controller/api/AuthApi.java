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
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

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


  @Operation(summary = "Refresh Token 기반 Access Token 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(type = "string", format = "JWT"))
      ),
      @ApiResponse(
          responseCode = "401", description = "리프레시 토큰이 유효하지 않음"
      )
  })
  ResponseEntity<String> me(@CookieValue("refreshToken") String refreshToken);


  @Operation(summary = "사용자 권한 수정 및 세션 강제 로그아웃")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "권한 변경 성공",
          content = @Content(schema = @Schema(implementation = UserDto.class))
      )
  })
  ResponseEntity<UserDto> role(@RequestBody RoleUpdateRequest request);
}
