package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.service.basic.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;

  @GetMapping(value = "/csrf-token")
  public ResponseEntity<CsrfToken> crsf(CsrfToken token) {
    return ResponseEntity.ok(token);
  }

  @GetMapping(value = "/me")
  public ResponseEntity<UserDto> me() {
    return ResponseEntity.ok(authService.getUserBySession());
  }

}
