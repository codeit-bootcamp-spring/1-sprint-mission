package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;

  @PostMapping(path = "login")
  public ResponseEntity<UserDto> login(@RequestBody @Valid LoginRequest loginRequest) {
    log.info("로그인 요청: username={}", loginRequest.username());
    UserDto user = authService.login(loginRequest);
    log.debug("로그인 응답: {}", user);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(user);
  }

  //CSRF 토큰 발급 API
  //CsrfToken 파라미터를 @GetMapping 메서드 인자로 선언하면
  // Spring MVC가 HandlerMethodArgumentResolver를 통해 자동으로 주입
  @GetMapping("csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
    log.debug("CSRF 토큰 요청");
    return ResponseEntity.status(HttpStatus.OK).body(csrfToken);
  }

  //사용자 정보 조회 API 구현
  @GetMapping("me")
  public ResponseEntity<UserDto> me(@AuthenticationPrincipal DiscodeitUserDetails userDetails) {
    log.info("내 정보 조회 요청");
    return ResponseEntity.status(HttpStatus.OK).body(userDetails.getUserDto());
  }
}
