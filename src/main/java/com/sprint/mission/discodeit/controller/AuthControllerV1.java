package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {

  private final AuthService authService;

  @PostMapping(value = "/login")
  public ResponseEntity<User> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
  }
}
