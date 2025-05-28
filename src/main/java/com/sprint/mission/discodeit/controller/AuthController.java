package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PutMapping("role")
  public ResponseEntity<UserResponse> role(@RequestBody RoleUpdateRequest request) {
    UserResponse userDto = authService.updateRole(request);

    return ResponseEntity.status(HttpStatus.OK).body(userDto);
  }
}
