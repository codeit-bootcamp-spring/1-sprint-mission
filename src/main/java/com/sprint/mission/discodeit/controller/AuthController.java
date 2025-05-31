package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.AuthControllerDocs;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
  public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal DiscodeitUserDetails principal) {
    return ResponseEntity.ok(principal.getUser());
  }

  @PutMapping("/role")
  public ResponseEntity<UserDto> updateUserRole(@RequestBody RoleUpdateRequest roleUpdateRequest, HttpServletRequest request, HttpServletResponse response) {
    UserDto userDto = authService.updateUserRole(roleUpdateRequest);
    return ResponseEntity.ok(userDto);
  }
}
