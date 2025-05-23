package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  private final AuthService authService;

  @Override
  @GetMapping("/csrf-token")
  public CsrfToken csrf(CsrfToken csrfToken) {
    return csrfToken;
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponse> me(HttpServletRequest request) {
    HttpSession session = request.getSession();
    if (session == null) {
      return ResponseEntity.status(401).build();
    }
    String sessionId = session.getId();
    UUID userId = (UUID) session.getAttribute("userId");

    return ResponseEntity.ok(authService.getUserById(userId));
  }
}
