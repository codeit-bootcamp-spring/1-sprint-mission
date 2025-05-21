package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.basic.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @GetMapping(value = "/csrf-token")
  public ResponseEntity<CsrfToken> crsf(CsrfToken token) {
    return ResponseEntity.ok(token);
  }
  
}
