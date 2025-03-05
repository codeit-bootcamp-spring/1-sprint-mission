package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.AuthSwagger;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthSwagger {

  // implements AuthSwagger
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<User> login(@RequestBody LoginRequest request) {
    User user = authService.login(request);

    return ResponseEntity.ok()
        .body(user);
  }
}
