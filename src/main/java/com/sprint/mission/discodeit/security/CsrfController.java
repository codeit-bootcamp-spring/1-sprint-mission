package com.sprint.mission.discodeit.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CsrfController {

  private final CsrfTokenRepository csrfTokenRepository;

  @GetMapping("/api/auth/csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(HttpServletRequest request,
      HttpServletResponse response) {
    request.getSession(true);
    CsrfToken token = csrfTokenRepository.generateToken(request);
    csrfTokenRepository.saveToken(token, request, response);
    return ResponseEntity.ok(token);
  }
}
