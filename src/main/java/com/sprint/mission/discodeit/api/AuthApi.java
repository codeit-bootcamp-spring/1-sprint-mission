package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth", description = "로그인 관련 api")
@RequestMapping("/api/auth")
public interface AuthApi {

  @Operation(summary = "로그인", description = "사용자가 로그인하면 인증된 사용자 정보를 반환합니다.")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "로그인 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")})
  @PostMapping("/login")
  ResponseEntity<UserResponse> login(@RequestBody LoginRequest request);
}
