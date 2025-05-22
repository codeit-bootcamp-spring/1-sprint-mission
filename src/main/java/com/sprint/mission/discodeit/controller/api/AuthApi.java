package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth", description = "로그인 관련 api")
public interface AuthApi {

  @Operation(
      summary = "CSRF 토큰 발급",
      description = "현재 요청에 대한 CSRF 토큰을 발급합니다. 프론트엔드는 이 토큰을 이후 요청의 헤더에 포함시켜야 합니다."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "CSRF 토큰 발급 성공", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = CsrfToken.class)
      )),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  CsrfToken csrf(CsrfToken csrfToken);
}
