package com.sprint.mission.discodeit.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfTokenController {

  @GetMapping("/api/auth/csrf-token")
  public CsrfToken csrf(HttpServletRequest request) {
    return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
  }
}