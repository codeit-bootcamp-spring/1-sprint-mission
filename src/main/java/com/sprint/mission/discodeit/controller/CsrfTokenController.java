package com.sprint.mission.discodeit.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfTokenController {

  @GetMapping("/api/auth/csrf-token")
  public ResponseEntity<?> getCsrfToken(HttpServletRequest request) {
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

    if (csrfToken == null) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("CSRF 토큰을 찾을 수 없습니다");
    }

    Map<String, String> response = new HashMap<>();
    response.put("token", csrfToken.getToken());

    return ResponseEntity.ok(response);
  }
}