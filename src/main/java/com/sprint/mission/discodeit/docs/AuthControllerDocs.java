package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth API", description = "인증 관리 API")
public interface AuthControllerDocs {

  @Operation(summary = "csrf token 발급")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "토큰 발급 성공"),
  })
  @GetMapping("/csrf-token")
  ResponseEntity<CsrfToken> csrfToken(CsrfToken token);
}
