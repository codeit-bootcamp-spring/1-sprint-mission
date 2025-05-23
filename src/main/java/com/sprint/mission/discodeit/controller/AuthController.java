package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.AuthControllerDocs;
import com.sprint.mission.discodeit.dto.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerDocs {

  private final AuthService authService;

  @PostMapping("/login")
  @Override
  public ResponseEntity<UserResponse> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
    UserResponse userResponse = authService.login(userLoginRequest);
    return ResponseEntity.ok(userResponse);
  }

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> csrfToken(CsrfToken token) {
    return ResponseEntity.ok(token);
  }
}
