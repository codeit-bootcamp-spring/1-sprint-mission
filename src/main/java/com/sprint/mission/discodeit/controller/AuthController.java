package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.AuthControllerDocs;
import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.security.role.RoleUpdateRequest;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

  private final UserService userService;
  private final AuthService authService;

  @GetMapping("/csrf-token")
  public CsrfToken csrf(CsrfToken token) {
    return token;
  }

  @GetMapping("/me")
  public ResponseEntity<String> me(
      @CookieValue(value = "refreshToken", required = false) String refreshToken) {
    return ResponseEntity.ok(userService.getUserFromRefreshToken(refreshToken));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/role")
  public ResponseEntity<UserResponseDto> updateRole(
      @RequestBody @Valid RoleUpdateRequest roleUpdateRequest,
      HttpServletRequest httpServletRequest) {
    return ResponseEntity.ok(userService.updateUserRole(roleUpdateRequest, httpServletRequest));
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest,
      HttpServletResponse response) {
    return ResponseEntity.ok(authService.login(loginRequest, response));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @CookieValue(value = "refreshToken", required = false) String refreshToken,
      HttpServletResponse response) {
    authService.logout(refreshToken, response);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refresh(
      @CookieValue(value = "refreshToken", required = false) String refreshToken,
      HttpServletResponse response) {
    return ResponseEntity.ok().body(authService.refresh(refreshToken, response));
  }

}
