package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.apidocs.AuthApiDocs;
import com.sprint.mission.discodeit.dto.login.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {

  private final AuthService authService;

  @PostMapping("/login")
  @Override
  public ResponseEntity<User> login(@Valid @RequestBody LoginRequest loginRequest) {
    User loginUser = authService.login(loginRequest);
    return ResponseEntity.status(HttpStatus.OK).body(loginUser);
  }
}
