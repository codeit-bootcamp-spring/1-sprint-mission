package com.sprint.mission.discodeit.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Auth API", description = "인증 관리 API")
public interface AuthControllerDocs {

  @Operation(summary = "csrf token 발급")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "토큰 발급 성공"),
  })
  @GetMapping("/csrf-token")
  ResponseEntity<CsrfToken> csrfToken(CsrfToken token);
}
