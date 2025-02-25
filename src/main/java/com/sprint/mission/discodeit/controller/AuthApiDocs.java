package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApiDocs {

  @Operation(summary = "로그인", description = "사용자 인증을 통해 로그인합니다.")
  ResponseEntity<User> login(
      @Parameter(description = "로그인 요청 정보", required = true) LoginRequest loginRequest
  );
}