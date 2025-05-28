package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.AuthControllerDocs;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerDocs {

  private final AuthService authService;

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> csrfToken(CsrfToken token) {
    return ResponseEntity.ok(token);
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal DiscodeitUserDetails principal) {
    return ResponseEntity.ok(authService.toUserResponse(principal));
  }

  @PutMapping("/role")
  public ResponseEntity<UserResponse> updateUserRole(@RequestBody RoleUpdateRequest roleUpdateRequest, HttpServletRequest request, HttpServletResponse response) {
    UserResponse userResponse = authService.updateUserRole(roleUpdateRequest);

    SecurityContextHolder.clearContext();
    String cookieNames = "JSESSIONID";
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    ResponseCookie expired = ResponseCookie.from(cookieNames, "")
        .path("/")
        .maxAge(0)
        .httpOnly(true)
        .build();
    response.addHeader(HttpHeaders.SET_COOKIE, expired.toString());
    return ResponseEntity.ok(userResponse);
  }
}
