package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.controller.swagger.AuthApi;
import com.sprint.mission.discodeit.dto.auth.AuthLoginDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  private final AuthService authService;

  @PostMapping("/api/auth/login")
  public ResponseEntity<UserDto> login(@RequestBody @Valid AuthLoginDTO request) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(authService.login(request));
  }

  @GetMapping("/csrf-token")
  public CsrfToken csrfToken(CsrfToken token) {
    // Spring Security가 자동으로 CsrfToken 객체를 주입해 줌
    return token;
  }
}
